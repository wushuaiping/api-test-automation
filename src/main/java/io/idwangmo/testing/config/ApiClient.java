package io.idwangmo.testing.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.idwangmo.testing.util.CustomLocalDateTimeSerializer;
import io.idwangmo.testing.util.TestingUtil;

import java.time.LocalDateTime;

import static io.idwangmo.testing.util.TestingUtil.createObjectMapper;
import static io.idwangmo.testing.util.TestingUtil.getTestingConfig;

/**
 * feign 的配置类.
 *
 * @author idwangmo
 * @since 1.0
 */
public class ApiClient {

    private static final String BASE_SCHEMA = "https://";

    private static final String BASE_URL = "-api.example.io/";

    private Feign.Builder feignBuilder;

    public ApiClient() {
        ObjectMapper objectMapper = createObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());

        objectMapper.registerModule(javaTimeModule);
        feignBuilder = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new JacksonEncoder(objectMapper))
            .decoder(new JacksonDecoder(TestingUtil.createObjectMapper()))
            .errorDecoder(new TestErrorDecode())
            .options(new Request.Options(10 * 1000, 10 * 1000))
            .logLevel(Logger.Level.FULL)
            .logger(new Slf4jLogger());
    }

    /**
     * 默认对 web 端的请求使用
     */
    public <T extends Api> T buildClient(Class<T> clientClass) {

        String url = BASE_SCHEMA + getTestingConfig().getEnvironment() + BASE_URL + SpecType.WEB.toString().toLowerCase();

        return feignBuilder.target(clientClass, url);
    }

    /**
     * 对于其他端的使用
     */
    public <T extends Api> T buildClient(Class<T> clientClass, SpecType type) {

        String url = BASE_SCHEMA + getTestingConfig().getEnvironment() + BASE_URL + type.toString().toLowerCase();

        return feignBuilder.target(clientClass, url);
    }

    /**
     * 需要手动构建 url 的，可以用于临时测试接口，尽量在 SpecType 中新增端点
     */
    public <T extends Api> T buildClient(Class<T> clientClass, String url) {
        return feignBuilder.target(clientClass, url);
    }

    public interface Api {
    }
}
