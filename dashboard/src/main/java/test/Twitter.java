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
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.Rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import static test.Linkedin.getColumnDefObject;

public class Twitter {

    private StringBuilder baseStringBuilder = new StringBuilder();

    public static void main(String args[]) throws ParseException {

//        String url = "https://api.twitter.com/1.1/users/lookup.json?screen_name=Enlivant&user_id=2964932975&oauth_consumer_key=DC0sePOBbQ8bYdC8r4Smg&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1493999004&oauth_nonce=458542271&oauth_version=1.0&oauth_token=2964932975-RlNIi6QnoQtydUosFxNWUTuWgJlsJKCuGX4HmZS&oauth_signature=bfeMg7mE0MiRLT5xvvcb9fYa7KA%3D";
//
//        MultiValueMap<String, String> valueMap = null;
//        String data = Rest.getData(url, valueMap);
//
//        JSONParser parser = new JSONParser();
//        JSONArray jsonArray = (JSONArray) parser.parse(data);
//
//        List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonArray;
//        List<Map<String, Object>> twitterData = new ArrayList<>();
//
//        for (Iterator<Map<String, Object>> iterator = myData.iterator(); iterator.hasNext();) {
//            Map<String, Object> mapData = iterator.next();
//            Map<String, Object> twitterMapData = new HashMap<>();
//            twitterMapData.put("followers_count", mapData.get("followers_count"));
//            twitterMapData.put("friends_count", mapData.get("friends_count"));
//            twitterMapData.put("listed_count", mapData.get("listed_count"));
//            twitterMapData.put("statuses_count", mapData.get("statuses_count"));
//
//            twitterData.add(twitterMapData);
//
//        }
        String httpMethod = "POST";
        String baseUrl = "https://api.twitter.com/1.1/statuses/update.json";
        String status =	"Hello Ladies + Gentlemen, a signed OAuth request!";
        String includeEntities = "true";
        String oauthSignatureMethod = "HMAC-SHA1";
        String oauthConsumerKey = "3oaqt0QK31AGCAzHlX3c9xPbL";
        String oauthVersion = "1.0";
        String oauthToken = "780021988039335936-mst1OUPP5411m4YSiIEDYCUPUVhwjmm";
        String timeStamp = new Twitter().generateTimeStamp();
        String nonce = new Twitter().nonceGenerator();
        String SigningKey = new Twitter().generateSigningKey();
        Map<String, String> parameterList = new TreeMap<>();
        parameterList.put("include_entities", includeEntities);
        parameterList.put("oauth_consumer_key", oauthConsumerKey);
        parameterList.put("oauth_nonce", nonce);
        parameterList.put("oauth_signature_method", oauthSignatureMethod);
        parameterList.put("oauth_timestamp", timeStamp);
        parameterList.put("oauth_token", oauthToken);
        parameterList.put("oauth_version", oauthVersion);
        parameterList.put("status", status);
        String baseString = new Twitter().GenerateBaseString(parameterList);
        System.out.println("baseString------------------->"+baseString);
        String signatureBaseString = new Twitter().SignatureBaseString(baseString, httpMethod, baseUrl);
        System.out.println("baseString------------------->"+signatureBaseString);
        try {
            String Signature = HmacSha1Signature.calculateRFC2104HMAC(baseString, signatureBaseString);
                    System.out.println("signature--------"+Signature);
//        List<ColumnDef> columnDefObject = getColumnDefObject(twitterData);
//        Map returnMap = new HashMap();
//        returnMap.put("columnDef", columnDefObject);
//        returnMap.put("data", twitterData);
        } catch (SignatureException ex) {
            Logger.getLogger(Twitter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Twitter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Twitter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static List<ColumnDef> getColumnDefObject(List<Map<String, Object>> data) {
        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            for (Map.Entry<String, Object> entrySet : mapData.entrySet()) {
                String key = entrySet.getKey();

                Object value = entrySet.getValue();
//                System.out.println(value.getClass());
                columnDefs.add(new ColumnDef(key, "string", key));

            }
            return columnDefs;
        }
        return columnDefs;
    }

    public static String getResponse(String url, String authorizationHeaders, org.springframework.util.MultiValueMap<String, String> params) {
        String urlString = url;
        if (params != null) {
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
            urlString = uriComponents.toUriString();
        }
        System.out.println("url -->" + urlString);
        String jsonResponse = null;
        try {
            URL myURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
            conn.setRequestProperty("Authorization", authorizationHeaders);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/vnd.rtx.auth.v2.hal+json");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                jsonResponse += output;
            }
        } catch (IOException e) {
        }
        return jsonResponse;
    }

    public String buildBasicAuthorizationString(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + encodeData(credentials);
    }

    public static String encodeData(String encodeData) {
        try {
            String asB64 = Base64.getEncoder().encodeToString(encodeData.getBytes("utf-8"));
            return asB64;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReviewTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String nonceGenerator() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        String randomNumber = stringBuilder.toString();
        return randomNumber;
    }

    public String generateSigningKey() {
        String consumerSecret = "AuVaSXpGPJWLYMCzDiera1avN8MfChN2mGysCmWeDXPzVlDVZC";
        String encodedConsumerSecret = PercentEncoder.encode(consumerSecret);
        String OAuthTokenSecret = "jhrtiSSryo6jF3Kj9z1cWKDGiUtrKyIQUZ44eG3xRI6rg";
        String encodedOAuthToken = PercentEncoder.encode(OAuthTokenSecret);
        return encodedConsumerSecret + "&" + encodedOAuthToken;
    }

    public String generateTimeStamp() {
        Calendar tempcal = Calendar.getInstance();
        long ts = tempcal.getTimeInMillis();// get current time in milliseconds
        String oauth_timestamp = (new Long(ts / 1000)).toString();
        return oauth_timestamp;
    }

    public String GenerateBaseString(Map<String, String> parameterList) {

        for (Map.Entry<String, String> entry : parameterList.entrySet()) {
            baseStringBuilder.append(PercentEncoder.encode(entry.getKey()));
            baseStringBuilder.append("=");
            baseStringBuilder.append(PercentEncoder.encode(entry.getValue()));
            baseStringBuilder.append("&");
        }
        baseStringBuilder.deleteCharAt(baseStringBuilder.lastIndexOf("&"));
        return baseStringBuilder.toString();
    }

    public String SignatureBaseString(String baseString, String httpMethod, String baseUrl) {
        baseStringBuilder.replace(0, baseStringBuilder.length(),PercentEncoder.encode(baseString));
        System.out.println("baseStringBuilder------------------->"+baseStringBuilder);
        baseStringBuilder.insert(0, "&");
        baseStringBuilder.insert(0, PercentEncoder.encode(baseUrl));
        baseStringBuilder.insert(0, "&");
        baseStringBuilder.insert(0, httpMethod.toUpperCase());
        System.out.println("baseStringBuilder end------------------->"+baseStringBuilder);
        return baseStringBuilder.toString();
        
    }
}
