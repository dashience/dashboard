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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.MultiValueMap;
import static test.Linkedin.getColumnDefObject;

public class TwitterApiService {

    public static void main(String args[]) throws ParseException, Exception {

        /*
        String url = "https://api.twitter.com/1.1/users/lookup.json?screen_name=Enlivant&user_id=2964932975&oauth_consumer_key=DC0sePOBbQ8bYdC8r4Smg&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1493999004&oauth_nonce=458542271&oauth_version=1.0&oauth_token=2964932975-RlNIi6QnoQtydUosFxNWUTuWgJlsJKCuGX4HmZS&oauth_signature=bfeMg7mE0MiRLT5xvvcb9fYa7KA%3D";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(data);

        List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonArray;
        List<Map<String, Object>> twitterData = new ArrayList<>();

        for (Iterator<Map<String, Object>> iterator = myData.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            Map<String, Object> twitterMapData = new HashMap<>();
            twitterMapData.put("followers_count", mapData.get("followers_count"));
            twitterMapData.put("friends_count", mapData.get("friends_count"));
            twitterMapData.put("listed_count", mapData.get("listed_count"));
            twitterMapData.put("statuses_count", mapData.get("statuses_count"));

            twitterData.add(twitterMapData);

        }
        List<ColumnDef> columnDefObject = getColumnDefObject(twitterData);
        Map returnMap = new HashMap();
        returnMap.put("columnDef", columnDefObject);
        returnMap.put("data", twitterData);
         */
//        String screenName = getScreeName();
        String userTimeLineMetrics = getUserTimeLineMetrics();

//        List<Map<String,Object>> twitterGenericMetrics=getPagePerformance();
    }

    private static List<Map<String, Object>> getPagePerformance() throws ParseException {

        String url = "https://api.twitter.com/1.1/users/lookup.json?screen_name=JungleSabari&user_id=780021988039335936&oauth_consumer_key=3oaqt0QK31AGCAzHlX3c9xPbL&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1506441804&oauth_nonce=7OZUaf&oauth_version=1.0&oauth_token=780021988039335936-mst1OUPP5411m4YSiIEDYCUPUVhwjmm&oauth_signature=hMycckE0z6BsAot69avj88lH8WQ%3D";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(data);

        List<Map<String, Object>> myData = (List<Map<String, Object>>) jsonArray;
        List<Map<String, Object>> twitterData = new ArrayList<>();

        for (Iterator<Map<String, Object>> iterator = myData.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            Map<String, Object> twitterMapData = new HashMap<>();
            twitterMapData.put("followers_count", mapData.get("followers_count"));
            twitterMapData.put("friends_count", mapData.get("friends_count"));
            twitterMapData.put("listed_count", mapData.get("listed_count"));
            twitterMapData.put("statuses_count", mapData.get("statuses_count"));

            twitterData.add(twitterMapData);

        }
        System.out.println("Page performance -----");
        System.out.println(twitterData);

//        List<ColumnDef> columnDefObject = getColumnDefObject(twitterData);
//        Map returnMap = new HashMap();
//        returnMap.put("columnDef", columnDefObject);
//        returnMap.put("data", twitterData);
        return null;
    }

    private static String getUserTimeLineMetrics() throws Exception {
        int ownTweet=0,retweet=0;
        long ownTweetLikes=0,retweetLikes=0;
        String url = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=SivanesanGovind&user_id=2526475147&oauth_consumer_key=FH0z2IiKd46IHVrftBXhyyGjY&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1506528376&oauth_nonce=EwlN94AhGNf&oauth_version=1.0&oauth_token=2526475147-SJeXiGSn6P9Fg1N4AZtACrzdANEz0y9wQ32pncu&oauth_signature=3zFPKetFk6O0Hzyo5ClCVksLW5Q%3D";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        //object =
        jsonArray = (JSONArray) parser.parse(data);

        for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
            JSONObject next = (JSONObject) iterator.next();
            
              if((boolean)next.get("is_quote_status")) {
                  retweet=retweet+1;
                  retweetLikes=retweetLikes+(long)next.get("favorite_count");
              } else {
                  ownTweet=ownTweet+1;
                  ownTweetLikes=ownTweetLikes+(long)next.get("favorite_count");
              }
        }
        
        System.out.println("Own Tweets -->"+ownTweet);
        System.out.println("Own Tweets Likes-->"+ownTweetLikes);
        System.out.println("ReTweets-->"+retweet);
        System.out.println("ReTweets Likes-->"+retweetLikes);

        
        List<Map<String,Object>> returnMap=new ArrayList<>();
        
        Map dataMap=new HashMap();
        dataMap.put("own_tweets",ownTweet);
        dataMap.put("own_tweet_likes",ownTweetLikes);
        dataMap.put("retweets",retweet);
        dataMap.put("retweet_likes",retweetLikes);
        
        returnMap.add(dataMap);
        
        return null;
    }

    private static String getScreeName() throws ParseException {
        String url = "https://api.twitter.com/1.1/account/settings.json?oauth_consumer_key=3oaqt0QK31AGCAzHlX3c9xPbL&oauth_token=780021988039335936-mst1OUPP5411m4YSiIEDYCUPUVhwjmm&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1506435622&oauth_nonce=wbPspq&oauth_version=1.0&oauth_signature=VXqg4hNPSRezuoHapMc8nep7LuQ%3D";

        MultiValueMap<String, String> valueMap = null;
        String data = Rest.getData(url, valueMap);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(data);
        String srcName = (String) jsonObject.get("screen_name");
        System.out.println(srcName);

        List<Map<String, Object>> arrayObjects = new ArrayList<>();
        Map<String, Object> object = new HashMap<>();
        object.put("screen_name", srcName);
        arrayObjects.add(object);
        System.out.println("My objects are ---->");

        System.out.println(arrayObjects);

        return srcName;
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

}
