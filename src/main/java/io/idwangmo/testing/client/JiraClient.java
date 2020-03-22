package io.idwangmo.testing.client;

import feign.Param;
import feign.RequestLine;
import io.idwangmo.testing.config.ApiClient;
import io.idwangmo.testing.model.jira.IssuesResponse;
import io.idwangmo.testing.model.jira.JiraResponse;
import io.idwangmo.testing.model.jira.TestExec;
import io.idwangmo.testing.model.jira.TestRun;

import java.util.List;

/**
 * 从jira中直接获取数据.
 *
 * @author idwangmo
 * @since 1.0
 */
public interface JiraClient extends ApiClient.Api {

    @RequestLine("GET /rest/raven/1.0/test/{jiraId}/steps?_={isoDate}")
    List<JiraResponse> getJiraTestCaseInfo(@Param("jiraId") String jiraId, @Param("isoDate") Long isoDate);

    @RequestLine("GET /rest/raven/1.0/api/test/{jiraId}/testruns?_={isoDate}")
    List<TestRun> getJiraTestRun(@Param("jiraId") String jiraId, @Param("isoDate") Long isoDate);

    @RequestLine("PUT /rest/raven/1.0/api/testrun/{id}/status?_={isoDate}&status={status}")
    void updateJiraTestStatus(@Param("id") Long id, @Param("isoDate") Long isoDate, @Param("status") String status);

    @RequestLine("GET /rest/raven/1.0/api/testexec/{jiraId}/test?_={isoDate}")
    List<TestExec> getTestExec(@Param("jiraId") String jiraId, @Param("isoDate") Long isoDate);

    @RequestLine("POST /rest/api/2/issue/")
    IssuesResponse createIssues(String data);



}
