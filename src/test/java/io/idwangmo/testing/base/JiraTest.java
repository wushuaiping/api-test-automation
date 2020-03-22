package io.idwangmo.testing.base;

import com.google.gson.Gson;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.slf4j.Slf4jLogger;
import io.idwangmo.testing.client.JiraClient;
import io.idwangmo.testing.model.jira.JiraResponse;
import io.idwangmo.testing.model.jira.TestExec;
import io.idwangmo.testing.util.RedisClientUtil;
import io.idwangmo.testing.util.TestingUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author idwangmo
 * @since 1.0
 */
@Slf4j
public class JiraTest {

    private static Gson gson = new Gson();

    @Test(description = "测试 Jira 联通")
    public void testJiraClient() {
        BasicAuthRequestInterceptor interceptor = new BasicAuthRequestInterceptor(TestingUtil.getTestingConfig().getJira().getUsername(),
            TestingUtil.getTestingConfig().getJira().getPassword());
        JiraClient jiraClient = new Feign.Builder()
            .requestInterceptor(interceptor)
            .logLevel(Logger.Level.FULL)
            .logger(new Slf4jLogger())
            .decoder(new JacksonDecoder())
            .target(JiraClient.class, "https://jira.souban.io");

        List<JiraResponse> jiraResponseList = jiraClient.getJiraTestCaseInfo("MAR-17414", new Date().getTime());

        assertThat(jiraResponseList).isNotEmpty();
    }

    @Test(description = "测试 Jira 连接工具")
    public void testJiraUtil() {
        Object object = TestingUtil.getJiraData("MAR-17414", 1, Object.class);

        assertThat(object).isNotNull();
    }

    @Test(description = "在所有操作前进行数据重置")
    public void testTestExec() {
        List<TestExec> testExecs = TestingUtil.createJiraClient().getTestExec("MAR-17290", System.currentTimeMillis());

        testExecs.parallelStream().forEach(n -> {
            if (!"TODO".equals(n.getStatus())) {
                log.info(n.getKey() + "is TODO");
                TestingUtil.createJiraClient().updateJiraTestStatus(n.getId(), System.currentTimeMillis(), "TODO");
            }

            // 将测试结果放到redis中，用于以后测试使用
            RedisClientUtil.getInstance().getRedisClient().set("CREAMS-JIRA-" + n.getKey(), n.getStatus());
            RedisClientUtil.getInstance().getRedisClient().expire("CREAMS-JIRA-" + n.getKey(), 10 * 60L);

        });
    }
}
