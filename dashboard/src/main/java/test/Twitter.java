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
import com.visumbu.vb.utils.DateUtils;
import static com.visumbu.vb.utils.DateUtils.dateToTimeStamp;
import com.visumbu.vb.utils.Rest;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ParseException;
import org.springframework.util.MultiValueMap;
import com.visumbu.vb.utils.OauthAuthentication;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.util.LinkedMultiValueMap;

public class Twitter {

//    private static String twitter_consumer_key = "3oaqt0QK31AGCAzHlX3c9xPbL";
//    private static String twitter_consumer_secret = "AuVaSXpGPJWLYMCzDiera1avN8MfChN2mGysCmWeDXPzVlDVZC";
//    private static String oauth_token = "780021988039335936-mst1OUPP5411m4YSiIEDYCUPUVhwjmm";
//    private static String oauth_token_secret = "jhrtiSSryo6jF3Kj9z1cWKDGiUtrKyIQUZ44eG3xRI6rg";
//    private static String oauth_version = "1.0";
//    private static String oauth_signature_method = "HMAC-SHA1";
//    private static String twitter_accountId = "780021988039335936";
//    private static String twitter_screenName = "JungleSabari";
//    private static String httpMethod = "GET";

    public static void main(String args[]) throws ParseException, GeneralSecurityException, UnsupportedEncodingException {
//        String httpMethod = "GET";
//
////        try {
////        String Signature = generateSignature(signatureBaseString, consumerSecret, OAuthTokenSecret);
//
//        /*
//             * - Working url 
//         */
////        String url = "https://api.twitter.com/1.1/statuses/mentions_timeline.json?&"
////                + "oauth_consumer_key=3oaqt0QK31AGCAzHlX3c9xPbL&"
////                + "oauth_signature_method=HMAC-SHA1&"
////                + "oauth_timestamp=" + oauth_timestamp + "&"
////                + "oauth_nonce=" + oauth_nonce + "&"
////                + "oauth_version=1.0&"
////                + "oauth_token=" + oauth_token + "&"
////                + "oauth_signature=" + OauthAuthentication.encode(signature);
////
////        System.out.println("url --->" + url);
////
////        MultiValueMap<String, String> valueMap = null;
////        String data = Rest.getData(url, valueMap);
////
////        System.out.println("*************************************");
////        System.out.println(data);
////        System.out.println("*************************************");
////        String url = "https://api.twitter.com/1.1/users/lookup.json";
//        String url = "https://api.twitter.com/1.1/users/lookup.json";
//        getPagePerformanceReport(twitter_accountId, twitter_screenName, url);
//
 }

//    private static List<Map<String, Object>> getPagePerformanceReport(String twitterAccountId, String twitterScreenName, String url) {
//
////        String startDateStr = DateUtils.dateToString(startDate, "YYYY-MM-dd");
////        String endDateStr = DateUtils.dateToString(endDate, "YYYY-MM-dd");
////        Long timeStamp = dateToTimeStamp(endDateStr);//to be added in feature 
//        String queryString="screen_name=JungleSabari&user_id=780021988039335936";
//        Map<String, Object> parameters = getAuthentionData(url,queryString);
//        String twitterUrl = url
//                + "?screen_name="+twitterScreenName+"&user_id="+twitter_accountId
//                + "&oauth_consumer_key=" + parameters.get("twitter_consumer_key")
//                + "&oauth_signature_method=" + parameters.get("oauth_signature_method")
//                + "&oauth_timestamp=" + parameters.get("oauth_timestamp")
//                + "&oauth_nonce=" + parameters.get("oauth_nonce")
//                + "&oauth_version=1.0"
//                + "&oauth_token=" + parameters.get("oauth_token")
//                + "&oauth_signature=" + parameters.get("signature");
//
//        System.out.println("Twitter Url =====> " + twitterUrl);
//
//        String twitterData = Rest.getData(twitterUrl);
//        System.out.println("twitterData--------" + twitterData);
//        return null;
//    }

//    public static List<ColumnDef> getColumnDefObject(List<Map<String, Object>> data) {
//        List<ColumnDef> columnDefs = new ArrayList<>();
//        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
//            Map<String, Object> mapData = iterator.next();
//            for (Map.Entry<String, Object> entrySet : mapData.entrySet()) {
//                String key = entrySet.getKey();
//
//                Object value = entrySet.getValue();
////                System.out.println(value.getClass());
//                columnDefs.add(new ColumnDef(key, "string", key));
//
//            }
//            return columnDefs;
//        }
//        return columnDefs;
//    }

    public static Map<String, String> getAuthentionData(Map<String, String> properties,String type) {
        String oauth_nonce = OauthAuthentication.oauthNonce();
        String oauth_timestamp = OauthAuthentication.getTimeStamp();
        properties.put("oauth_nonce", oauth_nonce);
        properties.put("oauth_timestamp", oauth_timestamp);

        String signature = OauthAuthentication.generateSignature(properties,type);
        properties.put("oauth_signature", signature);
        return properties;

    }

//        String url = "https://api.twitter.com/1.1/users/lookup.json?screen_name=Enlivant&user_id=2964932975&"
//                + "oauth_consumer_key=DC0sePOBbQ8bYdC8r4Smg&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1493999004&"
//                + "oauth_nonce=458542271&oauth_version=1.0&"
//                + "oauth_token=2964932975-RlNIi6QnoQtydUosFxNWUTuWgJlsJKCuGX4HmZS&"
//                + "oauth_signature=bfeMg7mE0MiRLT5xvvcb9fYa7KA%3D";
//
//        String url="https://api.twitter.com/1.1/users/lookup.json?screen_name=Enlivant&user_id=2964932975&&"
//                + "oauth_consumer_key=3oaqt0QK31AGCAzHlX3c9xPbL&"
//                + "oauth_signature_method=HMAC-SHA1&"
//                + "oauth_timestamp="+generateTimeStamp()+"&"
//                +"oauth_nonce="+nonceGenerator()+"&"
//                +"oauth_token="+oauthToken+"&"
//                +"oauth_signature=";
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
//        String SigningKey = generateSigningKey();
//        String consumerSecret = "AuVaSXpGPJWLYMCzDiera1avN8MfChN2mGysCmWeDXPzVlDVZC";
//        String OAuthTokenSecret = "jhrtiSSryo6jF3Kj9z1cWKDGiUtrKyIQUZ44eG3xRI6rg";
//        Map<String, String> parameterList = new TreeMap<>();
//        parameterList.put("include_entities", includeEntities);
//        parameterList.put("oauth_consumer_key", oauthConsumerKey);
//        parameterList.put("oauth_nonce", nonce);
//        parameterList.put("oauth_signature_method", oauthSignatureMethod);
//        parameterList.put("oauth_timestamp", timeStamp);
//        parameterList.put("oauth_token", oauthToken);
//        parameterList.put("oauth_version", oauthVersion);
//        String baseString = new Twitter().GenerateBaseString(parameterList);
//        String signatureBaseString = new Twitter().SignatureBaseString(baseString, httpMethod, baseUrl);
}
