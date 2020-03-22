package io.idwangmo.testing.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.slf4j.Slf4jLogger;
import io.idwangmo.testing.client.JiraClient;
import io.creams.testing.client.oauth.OAuth2Api;
import io.creams.testing.client.web.UsersApi;
import io.idwangmo.testing.config.ApiClient;
import io.idwangmo.testing.config.SpecType;
import io.idwangmo.testing.model.config.TestConfig;
import io.idwangmo.testing.model.jira.JiraResponse;
import io.creams.testing.model.oauth.UserAccessTokenModel;
import io.creams.testing.model.web.UserDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于测试中使用的一些基本工具类.
 *
 * @author idwangmo
 * @since 1.0
 **/
@Slf4j
public class TestingUtil {

    private static final String JIRA_ERROR = "测试用例不存在";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String TOKEN = "token";
    private static final Pattern p = Pattern.compile("\\s*|\t|\r|\n");
    private static Gson gson = new Gson();

    private TestingUtil() {

    }

    /**
     * 从资源文件中读取json文件.
     *
     * @param filePath 资源文件的路径
     * @return 解析好的json数据
     */
    public static String getJson(String filePath) {
        String json = null;
        try {
            ClassLoader classLoader = TestingUtil.class.getClassLoader();

            json = IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream(filePath)), "UTF-8");
        } catch (IOException e) {
            log.error("读取 Json 文件错误:", e);
        }
        return json;
    }

    /**
     * 从redis中获取本次测试需要的测试用户的token。
     *
     * @return string 用户token
     */
    public static String getToken() {
        String token = RedisClientUtil.getInstance().getRedisClient().get(TOKEN);

        if (Objects.isNull(token)) {
            token = authRequest();

            RedisClientUtil.getInstance().getRedisClient().set(TOKEN, token);
            RedisClientUtil.getInstance().getRedisClient().expire(TOKEN, 20L * 60L);
        }
        return token;
    }

    /**
     * 更新 redis 中的 token
     */
    public static void setToken(String token) {
        RedisClientUtil.getInstance().getRedisClient().set(TOKEN, token);
        RedisClientUtil.getInstance().getRedisClient().expire(TOKEN, 20 * 60L);
    }


    private static String authRequest() {
        OAuth2Api oAuth2Api = new ApiClient().buildClient(OAuth2Api.class, SpecType.OAUTH);

        OAuth2Api.QueryTokenByPasswordUsingPOSTQueryParams params = OAuth2Api.QueryTokenByPasswordUsingPOSTQueryParams.builder()
            .client_id(getTestingConfig().getCreams().getClientID())
            .username(getTestingConfig().getCreams().getUsername())
            .password(getTestingConfig().getCreams().getPassword())
            .build();

        UserAccessTokenModel model = oAuth2Api.queryTokenByPasswordUsingPOST(null, params);
        return "Bearer " + model.getAccessToken();
    }

    /**
     * 写入在其他测试套件中需要使用的一些基本数据.
     *
     * @param key   写入的关键字
     * @param value 需要写入的数据
     */
    public static void setValue(String key, String value) {
        RedisClientUtil.getInstance().getRedisClient().set(key, value);
        RedisClientUtil.getInstance().getRedisClient().expire(key, 3L * 60L);
    }

    /**
     * 写入在其他测试套件中需要使用的一些基本数据，可自定义超时时间.
     *
     * @param key    写入的关键字
     * @param value  需要写入的数据
     * @param expire 过期的时间，单位是分钟
     */
    public static void setValue(String key, String value, long expire) {
        RedisClientUtil.getInstance().getRedisClient().set(key, value);
        RedisClientUtil.getInstance().getRedisClient().expire(key, expire * 60L);
    }


    /**
     * 从redis中读取共享的测试数据.
     *
     * @param key 需要取出的键值
     * @return string 存在redis中的共享数据
     */
    public static String getValue(String key) {
        return RedisClientUtil.getInstance().getRedisClient().get(key);
    }

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);


        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());

        objectMapper.registerModule(javaTimeModule);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

    public static ObjectMapper createObjectMapperByEpochMilli() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);


        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());

        objectMapper.registerModule(javaTimeModule);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }


    public static JiraClient createJiraClient() {
        BasicAuthRequestInterceptor interceptor = new BasicAuthRequestInterceptor(getTestingConfig().getJira().getUsername(),
            getTestingConfig().getJira().getPassword());
        return new Feign.Builder()
            .requestInterceptor(interceptor)
            .logLevel(Logger.Level.FULL)
            .logger(new Slf4jLogger())
            .decoder(new JacksonDecoder(createObjectMapper()))
            .target(JiraClient.class, "https://jira.souban.io");
    }

    /**
     * 获取在jira中data的数据.
     *
     * @param jiraId      jira中测试用例的的编号
     * @param index       数据所在的需要
     * @param resultClass 需要转换的模型的类型
     * @return 转换后的数据类型
     */
    public static <T> T getJiraData(String jiraId, int index, Class<T> resultClass) {

        // 先去 redis 中取数据，没有再去 jira 上读取数据
        String redisData = TestingUtil.getValue("CREAMS-JIRA-DATA-" + jiraId);

        Optional<JiraResponse> jiraResponse = getJiraIndex(jiraId, index, redisData, "CREAMS-JIRA-DATA-" );

        if (jiraResponse.isPresent()) {
            String data = jiraResponse.get().getData().getRaw();
            return deserializationClass(data, resultClass);
        } else {
            throw new NullPointerException(jiraId + JIRA_ERROR);
        }
    }

    /**
     * 获取在jira中结果数据.
     *
     * @param jiraId      jira中测试用例的的编号
     * @param index       数据所在的需要
     * @param resultClass 需要转换的模型的类型
     * @return 转换后的数据类型
     */
    public static <T> T getJiraResult(String jiraId, int index, Class<T> resultClass) {

        // 先去 redis 中取数据，没有再去 jira 上读取数据
        String redisData = TestingUtil.getValue("CREAMS-JIRA-RESULT-" + jiraId);

        Optional<JiraResponse> jiraResponse = getJiraIndex(jiraId, index, redisData, "CREAMS-JIRA-RESULT-" );

        if (jiraResponse.isPresent()) {
            String data = jiraResponse.get().getResult().getRaw();

            return deserializationClass(data, resultClass);
        } else {
            throw new NullPointerException(jiraId + JIRA_ERROR);
        }
    }

    private static Optional<JiraResponse> getJiraIndex(String jiraId, int index, String redisData, String key) {
        List<JiraResponse> jiraResponseList;
        if (Objects.nonNull(redisData)) {
            jiraResponseList = deserializationListClass(redisData, JiraResponse.class);

        } else {
            jiraResponseList = createJiraClient().getJiraTestCaseInfo(jiraId, System.currentTimeMillis());
            try {
                TestingUtil.setValue(key + jiraId, TestingUtil.createObjectMapper().writeValueAsString(jiraResponseList));
            } catch (JsonProcessingException e) {
                log.error("反序列化错误", e);
            }
        }

        return jiraResponseList.stream().filter(n -> n.getIndex() == index).findFirst();
    }

    private static <T> T deserializationClass(String data, Class<T> resultClass) {
        log.info(data);

        T instance = null;

        try {
            instance = createObjectMapper().readValue(data, resultClass);
        } catch (IOException e) {
            log.error("反序列化数据出错", e);
        }
        return instance;
    }

    /**
     * 返回jira中测试用例的请求数据.
     *
     * @param jiraId 测试用例的编号
     * @param index  测试用类的数据位置
     * @return 测试用例的请求数据
     */
    public static String getJiraDataString(String jiraId, int index) {
        // 先去 redis 中取数据，没有再去 jira 上读取数据
        String redisData = TestingUtil.getValue("CREAMS-JIRA-DATA-" + jiraId);

        Optional<JiraResponse> jiraResponse = getJiraIndex(jiraId, index, redisData, "CREAMS-JIRA-DATA-" );

        if (jiraResponse.isPresent()) {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);


            String data = null;
            try {
                Object instance = objectMapper.readValue(jiraResponse.get().getData().getRaw(), Object.class);
                data = objectMapper.writeValueAsString(instance);
            } catch (IOException e) {
                log.error("序列化数据出错", e);
            }
            log.info("数据集: " + data);
            return data;
        } else {
            throw new NullPointerException(jiraId + JIRA_ERROR);
        }
    }

    /**
     * 返回jira中测试用例的结果集.
     *
     * @param jiraId 测试用例的编号
     * @param index  测试用类的数据位置
     * @return 测试用例的结果集合
     */
    public static String getJiraResultString(String jiraId, int index) {
        // 先去 redis 中取数据，没有再去 jira 上读取数据
        String redisData = TestingUtil.getValue("CREAMS-JIRA-RESULT-" + jiraId);

        Optional<JiraResponse> jiraResponse = getJiraIndex(jiraId, index, redisData, "CREAMS-JIRA-RESULT-" );

        if (jiraResponse.isPresent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);


            String result = null;
            try {
                Object instance = objectMapper.readValue(jiraResponse.get().getResult().getRaw(), Object.class);
                result = objectMapper.writeValueAsString(instance);
            } catch (IOException e) {
                log.error("序列化数据出错", e);
            }
            log.info("结果集: " + result);
            return result;

        } else {
            throw new NullPointerException(jiraId + JIRA_ERROR);
        }
    }

    /**
     * 获取配置文件.
     *
     * @return 配置文件对象
     */
    public static TestConfig getTestingConfig() {
        Yaml yaml = new Yaml(new Constructor(TestConfig.class));
        InputStream inputStream = TestingUtil.class.getClassLoader().getResourceAsStream("testing-config.yml");
        return yaml.load(inputStream);
    }

    /**
     * 返回一个随机的 Enum 值.
     *
     * @param enumClass 枚举类
     * @return 返回的枚举值
     */
    public static <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
        int index = RANDOM.nextInt(enumClass.getEnumConstants().length);

        return enumClass.getEnumConstants()[index];
    }

    /**
     * 将 jira 的测试用例数据反序列化为 List.
     *
     * @param jiraId      测试用例编号
     * @param index       测试用例序号
     * @param resultClass 返回的数据类型
     * @return 序列化好的集合
     */
    public static <T> List<T> getJiraDataByList(String jiraId, int index, Class<T> resultClass) {
        String jiraData = getJiraDataString(jiraId, index);
        return deserializationListClass(jiraData, resultClass);
    }

    /**
     * 将 jira 的测试用例结果反序列化为 List.
     *
     * @param jiraId      测试用例编号
     * @param index       测试用例序号
     * @param resultClass 返回的数据类型
     * @return 序列化好的集合
     */
    public static <T> List<T> getJiraResultByList(String jiraId, int index, Class<T> resultClass) {
        String jiraData = getJiraResultString(jiraId, index);
        return deserializationListClass(jiraData, resultClass);
    }

    private static <T> List<T> deserializationListClass(String jsonData, Class<T> resultClass) {
        List<T> instanceList = new ArrayList<>();

        try {
            CollectionType collectionType = createObjectMapper().getTypeFactory().constructCollectionType(List.class, resultClass);
            instanceList = createObjectMapper().readValue(jsonData, collectionType);
        } catch (IOException e) {
            log.error("反序列化数据出错", e);
        }
        return instanceList;
    }

    public static UserDetailResponse getUserInfo() {
        String userInfoStr = RedisClientUtil.getInstance().getRedisClient().get("CREAMS-USER-INFO");
        if (Objects.isNull(userInfoStr)) {
            UsersApi usersApi = new ApiClient().buildClient(UsersApi.class);

            UserDetailResponse response = usersApi.queryUserDetailInfoUsingGET(getToken(), null, null);

            setValue("CREAMS-USER-INFO", gson.toJson(response), 10);

            return response;
        }

        return gson.fromJson(userInfoStr, UserDetailResponse.class);

    }


    /**
     * 返回列表中的一个随机值
     *
     * @return 返回的枚举值
     */
    public static String randomString(List<String> typeList) {
        int index = RANDOM.nextInt(typeList.size());

        return typeList.get(index);
    }

    public static String getJiraRawData(String jiraId, int index) {
        List<JiraResponse> jiraResponseList = createJiraClient().getJiraTestCaseInfo(jiraId, System.currentTimeMillis());

        Optional<JiraResponse> jiraResponse = jiraResponseList.stream().filter(n -> n.getIndex() == index).findFirst();

        if (jiraResponse.isPresent()) {
            return jiraResponse.get().getData().getRaw();
        } else {
            throw new NullPointerException(jiraId + JIRA_ERROR);
        }
    }

    public static String getJiraRawResult(String jiraId, int index){
        List<JiraResponse> jiraResponseList = createJiraClient().getJiraTestCaseInfo(jiraId, System.currentTimeMillis());

        Optional<JiraResponse> jiraResponse = jiraResponseList.stream().filter(n -> n.getIndex() == index).findFirst();

        if (jiraResponse.isPresent()) {
            return jiraResponse.get().getResult().getRaw();
        } else {
            throw new NullPointerException(jiraId + JIRA_ERROR);
        }
    }

    /**
     * 解决429等待
     *
     * @param time 毫秒数
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            log.error("sleep error", e);
        }
    }


    /**
     * 返回jira中测试用例的请求数据.
     * 处理类似 page=1&limit=20
     *
     * @param jiraId 测试用例的编号
     * @param index  测试用类的数据位置
     * @return 测试用例的请求数据
     */
    public static <T> T getJiraDataStringForGetRequest(String jiraId, int index, Class<T> resultClass) {
        List<JiraResponse> jiraResponseList = createJiraClient().getJiraTestCaseInfo(jiraId, System.currentTimeMillis());
        Optional<JiraResponse> jiraResponse = jiraResponseList.stream().filter(n -> n.getIndex() == index).findFirst();
        if (jiraResponse.isPresent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

            String raw = jiraResponse.get().getData().getRaw();
            log.info(raw);
            String result = "";
            if (raw != null) {
                Matcher m = p.matcher(raw);
                result = m.replaceAll("");
            }
            return TestingUtil.convertUrlToObject(result, resultClass);
        } else {
            throw new NullPointerException(jiraId + JIRA_ERROR);
        }
    }


    /**
     * 转换get请求参数到指定的对象
     */
    public static <T> T convertUrlToObject(String data, Class<T> resultClass) {
        if (data == null || data.length() <= 0) {
            log.info("从jira中获取文本失败.");
            return null;
        }
        List<String> collections = Arrays.asList("java.util.List", "java.lang.String[]", "java.lang.Long[]", "java.util.Set");
        Map<String, Object> map = new HashMap<>();
        String[] strings = data.split("&");
        Arrays.stream(strings).forEach(item -> {
            String[] split = item.split("=");
            Object value = split[1];
            try {
                String typeName = resultClass.getDeclaredField(split[0]).getType().getTypeName();
                if (collections.contains(typeName)) {
                    String string = value.toString();
                    String[] arr = string.split(",");
                    String arrString = "[";
                    for (String s : arr) {
                        try {
                            value = Long.parseLong(s);
                        } catch (Exception e) {
                            value = "\"" + s + "\"";
                        }
                        if (arrString.length() <= 1) {
                            arrString = arrString + value;
                        } else {
                            arrString = arrString + "," + value;
                        }
                    }
                    value = arrString + "]";
                } else {
                    try {
                        value = Long.parseLong(value.toString());
                    } catch (Exception e) {
                        value = "\"" + value.toString() + "\"";
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            map.put("\"" + split[0] + "\"", value);
        });
        String all = map.toString().replaceAll("=", ":");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            T t = objectMapper.readValue(all, resultClass);
            log.info(t.toString());
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
