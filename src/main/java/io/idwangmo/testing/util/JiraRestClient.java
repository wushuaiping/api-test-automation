package io.idwangmo.testing.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.naming.AuthenticationException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.idwangmo.testing.model.jira.IssuesRequest;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * https://pastebin.com/JeucUZNG
 */
public class JiraRestClient {

    /**
     *
     * @param args
     */
    public static void main(String... args) {
        try {
            List<IssuesRequest> requests = ExcelReadUtil.getIssues();
            JiraRestClient jrc = new JiraRestClient("https://jira.example.io", "example", "example");
            requests.parallelStream().forEach(r ->{
                try {
                    r.setScenario(r.getScenario().replace("When", "\nWhen").replace("And", "\nAnd").replace("Then", "\nThen" ));
                   String s =   jrc.createIssue(r.getProjectId(), r.getIssuesTypeId(), r.getSummary(), r.getDescription(), r.getComponents(), r.getScenario(), r.getScenarioType());
                   System.out.println(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception ex) {
            System.err.println(ex);
        }
        System.out.println("end");
    }

    private static String getAuth(String username, String password) {
        try {
            String s = username + ":" + password;
            byte[] byteArray = s.getBytes();
            String auth;
            auth = Base64.encodeBase64String(byteArray);

            return auth;
        } catch (Exception ignore) {
            return "";
        }
    }

    private final String host;
    private final String authHeader;

    /**
     *
     * @param host The server's URL.
     * @param username User for logging in.
     * @param password Password for logging in.
     */
    public JiraRestClient(String host, String username, String password) {
        this.authHeader = getAuth(username, password);
        if (host.endsWith("/")) {
            this.host = host.substring(0, host.length() - 1);
        } else {
            this.host = host;
        }
    }

    public String getIssue(String issuekey) throws Exception, JSONException {
        JSONObject result = doREST("GET", "/rest/api/2/issue", issuekey + "?*all,-comment");

        // TODO: format the JSON object to your own favorite object format.
        return result.toString();
    }

    /**
     *
     * @param projectid The ID of the project.
     * @param summary The Summary of the new issue.
     * @param description The body of the new issue.
     * @return JSON serialized message.
     * @throws Exception
     * @throws JSONException
     */
    public String createIssue(Integer projectid, Integer issuetypeid, String summary, String description, Integer components, String scenario, String scenarioType) throws Exception, JSONException {
        JSONObject fields = new JSONObject();
        fields.put("project", new JSONObject().put("id", projectid.toString()));
        fields.put("issuetype", new JSONObject().put("id", issuetypeid.toString()));
        fields.put("summary", summary);
        fields.put("description", description);
        fields.put("components", Collections.singletonList(new JSONObject().put("id", components.toString())));

        fields.put("customfield_10202", new JSONObject().put("id", "10201").put("value", "Cucumber"));
        fields.put("customfield_10203", new JSONObject().put("value",scenarioType));
        fields.put("customfield_10204", scenario);
        JSONObject issue = new JSONObject();
        issue.put("fields", fields);

        JSONObject result = doREST("POST", "/rest/api/2/issue", issue.toString());

        return result.toString();
    }

    @Getter
    @Setter
    class Components{
        private String id;

        public Components(){

        }

        public Components(String s) {
            setId(s);
        }
    }

    /**
     *
     * @param method The HTTP method defines if we want to fetch (GET), modify
     * (PUT), add (POST), or remove (DELETE) entites.
     * @param context The Resource you want to access.
     * @param arg The Parameter(s) assembled to simply send it.
     * @return A JSON object depicting the results, OR an exception detailing
     * the problem.
     * @throws Exception
     */
    private JSONObject doREST(String method, String context, String arg) throws Exception {
        try {
            ClientConfig config = getClientConfig();
            Client client = Client.create(config);

            if (!context.endsWith("/")) {
                context = context.concat("/");
            }

            WebResource webResource;
            if ("GET".equalsIgnoreCase(method)) {
                webResource = client.resource(this.host + context + arg);
            } else {
                webResource = client.resource(this.host + context);
            }

            WebResource.Builder builder = webResource.header("Authorization", "Basic " + this.authHeader).type("application/json").accept("application/json");

            ClientResponse response;

            if ("GET".equalsIgnoreCase(method)) {
                response = builder.get(ClientResponse.class);
            } else if ("POST".equalsIgnoreCase(method)) {
                response = builder.post(ClientResponse.class, arg);
            } else {
                response = builder.method(method, ClientResponse.class);
            }

            if (response.getStatus() == 401) {
                throw new AuthenticationException("HTTP 401 received: Invalid Username or Password.");
            }

            String jsonResponse = response.getEntity(String.class);
            JSONObject responseJson = new JSONObject(jsonResponse);

            return responseJson;
        } catch (JSONException ex) {
            throw new Exception("JSON deserializing failed.", ex);
        } catch (AuthenticationException ex) {
            throw new Exception("Login failed.", ex);
        }
    }

    /**
     *
     * @return A clientconfig accepting all hosts and all ssl certificates
     * unconditionally.
     */
    private ClientConfig getClientConfig() {
        try {
            TrustManager[] trustAllCerts;
            trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};

            // Ignore differences between given hostname and certificate hostname
            HostnameVerifier hv = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            HTTPSProperties prop = new HTTPSProperties(hv, sc);

            ClientConfig config = new DefaultClientConfig();
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, prop);

            return config;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            System.err.println(e);
        }

        return null;
    }
}
