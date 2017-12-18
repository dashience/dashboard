
package test;


import com.visumbu.vb.utils.Rest;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import static org.apache.http.HttpVersion.HTTP;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.mortbay.util.ajax.JSON;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Nabeel Mukhtar 
 * 
 */ 
public class TestLinkedin { 
    public static void openlink(){
        
        
//    try {
//
//                String url = "https://www.linkedin.com/oauth/v2/authorization?format=json&response_type=code&client_id=81tt2j6dtqazgl&redirect_uri=https://www.getpostman.com/oauth2/callback&state=DCEeFWf45A53sdfKef424";
//                Connection.Response response = Jsoup
//                        .connect(url)
//                        .method(Connection.Method.GET)
//                        .execute();
//
//                Document responseDocument = response.parse();
//                Element loginCsrfParam = responseDocument
//                        .select("input[name=loginCsrfParam]")
//                        .first();
//                System.out.println("Element----------"+loginCsrfParam);
//
//                response = Jsoup.connect("https://www.linkedin.com/oauth/v2/authorization?format=json&response_type=code&client_id=81tt2j6dtqazgl&redirect_uri=https://www.getpostman.com/oauth2/callback&state=DCEeFWf45A53sdfKef424")
//                        .cookies(response.cookies())
//                        .data("loginCsrfParam", loginCsrfParam.attr("value"))
//                        .data("session_key", "info@deetaanalytics.com")
//                        .data("session_password", "D@@tA!23")
//                        .method(Connection.Method.POST)
//                        .followRedirects(true)
//                        .execute();
//
//        System.out.println("esss-------"+response.body());
//                String message = response.statusMessage();
//                                    System.out.println("response------"+response.url());
//    //            System.out.println(document)
//
//
//            } catch (IOException e) {
//            }
    }
 
//public static void oAuthSessionProvider(String loginHost, String username,
//   String password, String clientId, String secret)
//
//	        throws HttpException, IOException
//
//	{
//
//
//	    DefaultHttpClient client = new DefaultHttpClient();
//
//	    HttpParams params = client.getParams();
//
//	    HttpClientParams.setCookiePolicy(params, CookiePolicy.RFC_2109);
//
//	    params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 30000);
//
//	 
//
//	    // Set the SID.
//
//	    System.out.println("Logging in as " + username + " in environment " + loginHost);
//
//	    String baseUrl = loginHost + "/token";
//
//	    // Send a post request to the OAuth URL.
//
//	    HttpPost oauthPost = new HttpPost(baseUrl);
//
//	    // The request body must contain these 5 values.
//
//	    List<BasicNameValuePair> parametersBody = new ArrayList<>();
//
//	    parametersBody.add(new BasicNameValuePair("grant_type", "password"));
//
//	    parametersBody.add(new BasicNameValuePair("username", username));
//
//	    parametersBody.add(new BasicNameValuePair("password", password));
//
//	    parametersBody.add(new BasicNameValuePair("client_id", clientId));
//
//	    parametersBody.add(new BasicNameValuePair("client_secret", secret));
//
//	    oauthPost.setEntity(new UrlEncodedFormEntity(parametersBody,StandardCharsets.UTF_8));
//
//	 
//
//	    // Execute the request.
//
//	    System.out.println("POST " + baseUrl + "...\n");
//
//	    HttpResponse response = client.execute(oauthPost);
//
//	    int code = response.getStatusLine().getStatusCode();
//
//	    Map<String, String> oauthLoginResponse = (Map<String, String>)
//
//	        JSON.parse(EntityUtils.toString(response.getEntity()));
//
//	    System.out.println("OAuth login response");
//
//	    for (Map.Entry<String, String> entry : oauthLoginResponse.entrySet())
//
//	    {
//
//	        System.out.println(String.format("  %s = %s", entry.getKey(), entry.getValue()));
//
//	    }
//
//	    System.out.println("");
//
//	 
//
//	    // Get user info.
//
//	    String userIdEndpoint = oauthLoginResponse.get("id");
//
//	    String accessToken = oauthLoginResponse.get("access_token");
//
//	    List<BasicNameValuePair> qsList = new ArrayList<>();
//
//	    qsList.add(new BasicNameValuePair("oauth_token", accessToken));
//
//	    String queryString = URLEncodedUtils.format(qsList, HTTP);
//
//	    HttpGet userInfoRequest = new HttpGet(userIdEndpoint + "?" + queryString);
//
//	    HttpResponse userInfoResponse = client.execute(userInfoRequest);
//
//	    Map<String, Object> userInfo = (Map<String, Object>)
//
//	        JSON.parse(EntityUtils.toString(userInfoResponse.getEntity()));
//
//	    System.out.println("User info response");
//
//	    for (Map.Entry<String, Object> entry : userInfo.entrySet())
//
//	    {
//
//	        System.out.println(String.format("  %s = %s", entry.getKey(), entry.getValue()));
//
//	    }
//
//	    System.out.println("");
//
//	 
//
//	    // Use the user info in interesting ways.
//
//	    System.out.println("Username is " + userInfo.get("username"));
//
//	    System.out.println("User's email is " + userInfo.get("email"));
//
//	    Map<String, String> urls = (Map<String, String>)userInfo.get("urls");
//
//	    System.out.println("REST API url is " + urls.get("rest").replace("{version}", "41.0"));
//
//	}
//    public static void getToken(String authenticationCode,String clientId,String clientSecret){
//        String url ="https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code"+
//                "&code="+authenticationCode +
//                "&redirect_uri=https://www.getpostman.com/oauth2/callback"+
//                "&client_id="+clientId +
//                "&client_secret="+clientSecret;
//        String data = Rest.getData(url, null);
//    }
}