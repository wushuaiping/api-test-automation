package io.idwangmo.testing.config;

import com.google.gson.Gson;
import io.idwangmo.testing.model.jira.TestRun;
import io.idwangmo.testing.util.TestingUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TestListener extends TestListenerAdapter {

    private static final Pattern pattern = Pattern.compile("MAR-[1-9]\\d*");
    private static final String TODO = "TODO";
    private static final String FAIL = "FAIL";
    private static final String ABORTED = "ABORTED";
    private static final String PASS = "PASS";
    private static Gson gson = new Gson();

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);

        if (Objects.nonNull(tr.getMethod().getDescription())) {
            updateJiraStatus(tr.getMethod().getDescription(), ABORTED);
        }
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        if (Objects.nonNull(tr.getMethod().getDescription())) {
            updateJiraStatus(tr.getMethod().getDescription(), PASS);
        }
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        if (Objects.nonNull(tr.getMethod().getDescription())) {
            updateJiraStatus(tr.getMethod().getDescription(), FAIL);
        }
    }


//    @Override
//    public void onFinish(ITestContext testContext) {
//        super.onFinish(testContext);
//        String failedTest = testContext.getFailedTests().getAllMethods().stream().map(ITestNGMethod::getDescription).collect(Collectors.joining(","));
//        String skipedTest = testContext.getSkippedTests().getAllMethods().stream().map(ITestNGMethod::getDescription).collect(Collectors.joining("."));
//        String allTest = Arrays.stream(testContext.getAllTestMethods()).map(ITestNGMethod::getDescription).collect(Collectors.joining(","));
//
//        if (Objects.nonNull(failedTest)) {
//            updateJiraStatus(failedTest, FAIL);
//            return;
//        } else if (Objects.nonNull(skipedTest)) {
//            updateJiraStatus(skipedTest, ABORTED);
//            return;
//        }
//
//        if (Objects.nonNull(allTest)) {
//            updateJiraStatus(allTest, PASS);
//        }
//
//
//    }

    private void updateJiraStatus(String description, String status) {

        Matcher matcher = pattern.matcher(description);
        if (matcher.find()) {
            String jiraId = matcher.group();

            log.info(jiraId + " is " + status);

            String redisStatus = TestingUtil.getValue("CREAMS-JIRA-" + jiraId);

            if (Objects.nonNull(redisStatus) && (!redisStatus.equals(status) || !status.equals(TODO))) {
                List<TestRun> testRuns = TestingUtil.createJiraClient().getJiraTestRun(jiraId, System.currentTimeMillis());

                Optional<TestRun> testRun = testRuns.stream().filter(n -> n.getTestKey().equals(jiraId)).findFirst();

                if (testRun.isPresent()) {
                    TestingUtil.setValue("CREAMS-JIRA-" + jiraId, status);
                    // 一定得加，不然jira错误就直接结束了
                    try {
                        TestingUtil.createJiraClient().updateJiraTestStatus(testRun.get().getId(), System.currentTimeMillis(), status);
                    } catch (Exception e) {
                        log.error("jira状态修改错误", e);
                    }
                }
            }

        }

    }

}
