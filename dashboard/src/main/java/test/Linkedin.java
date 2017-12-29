/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author deeta
 */
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.utils.Rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import org.apache.http.HttpException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Linkedin {

    public static void main(String args[]) throws ParseException, HttpException, IOException {

//        try {
        //        String url = "https://api.linkedin.com/v1/companies/10671978/historical-status-update-statistics:(time,like-count,impression-count,click-count,engagement)?oauth2_access_token=AQUdoyU6zxDDcLR8b0FLkAfQPsTiwsy5UUccA15GRRtx-U3cP_VRf8nUdfkM5iDlUGT6ECYE4k8ibguWvOgWwKY8yn5KGvduAob-VXvP5qyJKb8ZxDgnJixyHOMzf4f3ReMWBHK2p9vGuENCG5iP8Iqr_K6qN-1dwGJw3WnQ-YasfhnQ19JN9I9lhHXWGbPZajNjAtcV4VkuduCNfF2UhaHYQgbZtA3XIc8_dvF3P0Npg-tD8BsLQOVfKpWFcMZ0SrFdNzOjq9OK4NkD1Y9Kb2TTIccOZQ7rQnqLUcVevI1joOambAhO4uxn4AmMCym8VsXBe6grJYgbJtDbB8EzJz1bhsN-hA&format=json";
//        MultiValueMap<String, String> valueMap = null;
//        String data = Rest.getData(url, valueMap);
//        JSONParser parser = new JSONParser();
//        Object jsonObj = parser.parse(data);
//        JSONObject json = (JSONObject) jsonObj;
//        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
//        System.out.println(jsonToMap.get("_total"));
//        List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonToMap.get("values");
//
//        List<ColumnDef> columnDefObject = getColumnDefObject(myData);
//        Map returnMap = new HashMap();
//        returnMap.put("columnDef", columnDefObject);
//        returnMap.put("data", myData);
//        System.out.println(returnMap);
// System.out.println(data);
        //    TestLinkedin.oAuthSessionProvider( "https://www.linkedin.com/oauth/v2/authorization?format=json&response_type=code&client_id=81tt2j6dtqazgl&redirect_uri=https://www.getpostman.com/oauth2/callback&state=DCEeFWf45A53sdfKef424", "info@deetaanalytics.com", "D@@tA!23", "81tt2j6dtqazgl", "zQgqM4zKWJZikuQm");
        MultiValueMap<String, String> properties = new LinkedMultiValueMap<>();
        String code = "AQSNaWDojfSK6NQQu21IZzU7jFMjPrs4HJGi6_dek08ivEyuzPpI0BVdGI-laz-CaVZ89yeOVOm4iL4pfxkRWmTc2o_bEeHtyhq57l6cpKWw9Qmym47wMZzRl3Ru8llOcAmKQN-_sq6S5Uy8lSrfCiFhj1bTHw";
        String redirect_uri = "https://www.getpostman.com/oauth2/callback";
        String client_id = "81tt2j6dtqazgl";
        String client_secret = "zQgqM4zKWJZikuQm";
        String url = "https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code";
        properties.put("code", Arrays.asList(code));
        properties.put("redirect_uri", Arrays.asList(redirect_uri));
        properties.put("client_id", Arrays.asList(client_id));
        properties.put("client_secret", Arrays.asList(client_secret));
        String data = Rest.getData(url, properties);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(data);
        System.out.println("---------Acess Token----"+json.get("access_token"));

    }

    public static List<ColumnDef> getColumnDefObject(List<Map<String, Object>> data) {
        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            for (Map.Entry<String, Object> entrySet : mapData.entrySet()) {
                String key = entrySet.getKey();

                Object value = entrySet.getValue();
                System.out.println(value.getClass());
                columnDefs.add(new ColumnDef(key, "string", key));

            }
            return columnDefs;
        }
        return columnDefs;
    }

    public static void signPostTest() throws MalformedURLException, IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, OAuthNotAuthorizedException {
        String clientId = "81tt2j6dtqazgl";
        String clientSecret = "zQgqM4zKWJZikuQm";
        OAuthConsumer consumer = new DefaultOAuthConsumer(clientId, clientSecret);

        OAuthProvider provider = new DefaultOAuthProvider(
                "/oauth/v2/authorization",
                "/oauth/v2/accessToken",
                "https://api.linkedin.com");

        System.out.println("Fetching request token from LinkedIn...");
        System.out.println("provider ------->" + provider.toString());

        // we do not support callbacks, thus pass OOB
        System.out.println("Request token: " + consumer.getToken());

        System.out.println("Token secret: " + consumer.getTokenSecret());
        // we do not support callbacks, thus pass OOB

        String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);

        System.out.println("Request token: " + consumer.getToken());

        System.out.println("Token secret: " + consumer.getTokenSecret());

        System.out.println("Now visit:\n" + authUrl
                + "\n... and grant this app authorization");

        System.out.println("Enter the PIN code and hit ENTER when you're done:");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String pin = br.readLine();

        System.out.println("Fetching access token from LinkedIn...");

        provider.retrieveAccessToken(consumer, pin);

        System.out.println("Access token: " + consumer.getToken());

        System.out.println("Token secret: " + consumer.getTokenSecret());

        URL url = new URL("http://api.linkedin.com/v1/people/~:(id,first-name,last-name,picture-url,headline)");

        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        consumer.sign(request);

        System.out.println("Sending request to LinkedIn...");

        request.connect();

        String responseBody = convertStreamToString(request.getInputStream());

        System.out.println("Response: " + request.getResponseCode() + " "
                + request.getResponseMessage() + "\n\n" + responseBody);
    }

    // Stolen liberally from http://www.kodejava.org/examples/266.html
    public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();

        String line = null;

        try {
            while ((line = reader.readLine()) != null) {

                sb.append(line + "\n");

            }
        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                is.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        return sb.toString();

    }
         
}
