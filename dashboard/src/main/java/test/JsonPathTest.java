/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.jayway.jsonpath.Configuration;
import static com.jayway.jsonpath.JsonPath.parse;
import static com.jayway.jsonpath.Criteria.where;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Filter;
import static com.jayway.jsonpath.Filter.filter;
import com.jayway.jsonpath.JsonPath;
import static com.jayway.jsonpath.JsonPath.using;
import com.jayway.jsonpath.Option;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static test.JsonPathTest.getJsonSamples;

/**
 *
 * @author Lino Alex
 */
public class JsonPathTest {

    public static void main(String args[]) {
        String json = getJsonSamples("complex");
        queryTestMethod1(json);
        try {
            //        queryTestMethod2(json);
//        getPathMethod(json);
//          updateJsonExtLibrary(json);
//          updateJsonNativeLib(json);
//        customAnnotation();
            checkWithJsonLib(json);
        } catch (ParseException ex) {
            Logger.getLogger(JsonPathTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param json
     * @deprecated
     */
    @Deprecated
    public static void queryTestMethod1(String json) {
        List<Map<String, Object>> output = JsonPath.parse(json)
                .read("$..metricHeaderEntries");
//        List<Map<String, Object>> output = JsonPath.parse(json)
//                .read("$..className[?(@.associatedDrug)]");
//        List<Map<String, Object>> books = JsonPath.parse(json)
//                .read("$..accounting[?(@.isbn && @.isbn == \"0-553-21311-3\")]");
        System.out.println("output-------------->" + output);
    }

    public static void queryTestMethod2(String json) {
        Filter cheapFictionFilter = filter(
                where("category").is("fiction").and("price").lte(10D)
        );

        List<Map<String, Object>> output
                = parse(json).read("$.store.book[?]", cheapFictionFilter);
        System.out.println("output-------------->" + output);
    }

    public static void getPathMethod(String json) {
        Configuration conf = Configuration.builder()
                .options(Option.AS_PATH_LIST).build();

        List<String> pathList = using(conf).parse(json).read("$..associatedDrug[?(@.name)]");
        System.out.println("output----------->" + pathList);
    }

    private static void updateJsonNativeLib(String json, JsonPath path) {
        DocumentContext doc = JsonPath.parse(json).
                set(path, "green").
                set("$.store.book[0].price", 9.5);

    }

    private static void checkWithJsonLib(String json) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(json);
        System.out.println("the json lib test------->" + jsonObject.get("associatedDrug"));

    }

    private static void updateJsonExtLibrary(String json) {

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = (JSONObject) parser.parse(json);

        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }

        Map<String, Object> userData = null;
        try {
            userData = new ObjectMapper().readValue(jsonObject.toJSONString(), Map.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MutableJson jsonValue = new MutableJson(userData);

        System.out.println("Before:\t" + jsonValue.map());

        jsonValue.update("$.store.book[0].author", "jigish");
        jsonValue.update("$.store.book[1].category", "action");

        System.out.println("After:\t" + jsonValue.map().toString());

    }

    public static String getJsonSamples(String jsonType) {
        String json = "{ \"store\": {\n"
                + "    \"book\": [ \n"
                + "      { \"category\": \"reference\",\n"
                + "        \"author\": \"Nigel Rees\",\n"
                + "        \"title\": \"Sayings of the Century\",\n"
                + "        \"price\": 8.95\n"
                + "      },\n"
                + "      { \"category\": \"fiction\",\n"
                + "        \"author\": \"Evelyn Waugh\",\n"
                + "        \"title\": \"Sword of Honour\",\n"
                + "        \"price\": 12.99\n"
                + "      },\n"
                + "      { \"category\": \"fiction\",\n"
                + "        \"author\": \"Herman Melville\",\n"
                + "        \"title\": \"Moby Dick\",\n"
                + "        \"isbn\": \"0-553-21311-3\",\n"
                + "        \"price\": 8.99\n"
                + "      },\n"
                + "      { \"category\": \"fiction\",\n"
                + "        \"author\": \"J. R. R. Tolkien\",\n"
                + "        \"title\": \"The Lord of the Rings\",\n"
                + "        \"isbn\": \"0-395-19395-8\",\n"
                + "        \"price\": 22.99\n"
                + "      }\n"
                + "    ],\n"
                + "    \"bicycle\": {\n"
                + "      \"color\": \"red\",\n"
                + "      \"price\": 19.95\n"
                + "    }\n"
                + "  }\n"
                + "}";

        String complexJson = "{\n"
                + "\"problems\": [{\n"
                + "    \"Diabetes\":[{\n"
                + "        \"medications\":[{\n"
                + "            \"medicationsClasses\":[{\n"
                + "                \"className\":[{\n"
                + "                    \"associatedDrug\":[{\n"
                + "                        \"name\":\"asprin\",\n"
                + "                        \"dose\":\"\",\n"
                + "                        \"strength\":\"500 mg\"\n"
                + "                    }],\n"
                + "                    \"associatedDrug#2\":[{\n"
                + "                        \"name\":\"somethingElse\",\n"
                + "                        \"dose\":\"\",\n"
                + "                        \"strength\":\"500 mg\"\n"
                + "                    }]\n"
                + "                }],\n"
                + "                \"className2\":[{\n"
                + "                    \"associatedDrug\":[{\n"
                + "                        \"name\":\"asprin\",\n"
                + "                        \"dose\":\"\",\n"
                + "                        \"strength\":\"500 mg\"\n"
                + "                    }],\n"
                + "                    \"associatedDrug#2\":[{\n"
                + "                        \"name\":\"somethingElse\",\n"
                + "                        \"dose\":\"\",\n"
                + "                        \"strength\":\"500 mg\"\n"
                + "                    }]\n"
                + "                }]\n"
                + "            }]\n"
                + "        }],\n"
                + "        \"labs\":[{\n"
                + "            \"missing_field\": \"missing_value\"\n"
                + "        }]\n"
                + "    }],\n"
                + "    \"Asthma\":[{}]\n"
                + "}]}";
        String gaData = "{\"columnHeader\":{\"dimensions\":[\"ga:browser\"],\"metricHeader\":{\"metricHeaderEntries\":[{\"name\":\"visits\",\"type\":\"INTEGER\"},{\"name\":\"sessions\",\"type\":\"INTEGER\"},{\"name\":\"percentNewSessions\",\"type\":\"PERCENT\"},{\"name\":\"pageViews\",\"type\":\"INTEGER\"},{\"name\":\"exitRate\",\"type\":\"PERCENT\"},{\"name\":\"bounceRate\",\"type\":\"PERCENT\"},{\"name\":\"avgTimeOnPage\",\"type\":\"TIME\"},{\"name\":\"users\",\"type\":\"INTEGER\"},{\"name\":\"newUsers\",\"type\":\"INTEGER\"},{\"name\":\"avgSessionDuration\",\"type\":\"TIME\"}]}},\"data\":{\"maximums\":[{\"values\":[\"22786\",\"22786\",\"100.0\",\"46136\",\"100.0\",\"100.0\",\"251.0\",\"16791\",\"14580\",\"376.5\"]}],\"minimums\":[{\"values\":[\"1\",\"1\",\"41.48936170212766\",\"1\",\"25.0\",\"0.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}],\"rowCount\":43,\"rows\":[{\"dimensions\":[\";__CT_JOB_ID__:0273ade6-4c52-4dd5-8e28-5a1194cd5721;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:038e10e5-8ec6-4924-bef4-cd2d7df75a17;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:15c35a05-f1f6-4fc2-890f-7d49c093b58f;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:24744bc3-bc6f-46dd-b803-ef443871fb59;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:42d534d6-6734-4b5c-bcf3-f666b72056fb;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:459ea86c-ffc9-45bc-9716-81f21f598d26;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:5032f36e-e905-41a3-813e-c7010768c5f8;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:55338362-5caa-483a-a25d-922f5505dc93;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:5a2bd455-2a1c-4048-a531-6dbc4648e709;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:8c0e3c33-ca89-4acf-89a5-aebe933de2f1;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:9cb6ab0d-858d-4850-8b7a-115fd99d54e2;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:b641dd3a-5672-4401-af6c-2df024e6cbb1;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:c2f8dd92-0c32-4c22-9585-6971fc48315c;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:c8528214-a43d-47c5-9321-c1888c3873f5;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:cd18c249-5229-4ddf-b44c-5f26e2be6420;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:d03363b0-40b8-4e16-bbfb-78e6a11ba948;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:e0ba8340-433f-4f04-b1d3-007f2ce5fea8;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\";__CT_JOB_ID__:e9b35675-aa64-4e1e-b4d7-93bea02db645;\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\"Amazon Silk\"],\"metrics\":[{\"values\":[\"274\",\"274\",\"63.503649635036496\",\"514\",\"53.30739299610895\",\"71.8978102189781\",\"110.0875\",\"219\",\"174\",\"96.41970802919708\"]}]},{\"dimensions\":[\"AncestryAndroid\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"2\",\"50.0\",\"0.0\",\"125.0\",\"1\",\"1\",\"125.0\"]}]},{\"dimensions\":[\"Android Browser\"],\"metrics\":[{\"values\":[\"131\",\"131\",\"45.038167938931295\",\"200\",\"65.5\",\"84.7328244274809\",\"105.78260869565217\",\"85\",\"59\",\"55.70229007633588\"]}]},{\"dimensions\":[\"Android Runtime\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\"Android Webview\"],\"metrics\":[{\"values\":[\"2250\",\"2250\",\"66.48888888888888\",\"3096\",\"72.67441860465115\",\"86.62222222222222\",\"79.52600472813239\",\"1888\",\"1496\",\"29.901333333333334\"]}]},{\"dimensions\":[\"BlackBerry\"],\"metrics\":[{\"values\":[\"3\",\"3\",\"66.66666666666666\",\"5\",\"60.0\",\"66.66666666666666\",\"42.5\",\"2\",\"2\",\"28.333333333333332\"]}]},{\"dimensions\":[\"Chrome\"],\"metrics\":[{\"values\":[\"22786\",\"22786\",\"63.98665847450189\",\"46136\",\"49.37142361713196\",\"66.39603265162819\",\"103.51074578302936\",\"16791\",\"14580\",\"106.16022996576845\"]}]},{\"dimensions\":[\"DDG-Android-3.1.1\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"4\",\"25.0\",\"0.0\",\"80.33333333333333\",\"1\",\"1\",\"242.0\"]}]},{\"dimensions\":[\"Edge\"],\"metrics\":[{\"values\":[\"2262\",\"2262\",\"64.45623342175067\",\"5109\",\"44.255235858289296\",\"65.60565870910699\",\"100.37605337078652\",\"1727\",\"1458\",\"126.36870026525199\"]}]},{\"dimensions\":[\"Firefox\"],\"metrics\":[{\"values\":[\"1705\",\"1705\",\"71.5542521994135\",\"3762\",\"45.32163742690059\",\"65.16129032258064\",\"92.96305298979095\",\"1335\",\"1220\",\"112.26568914956012\"]}]},{\"dimensions\":[\"Internet Explorer\"],\"metrics\":[{\"values\":[\"5269\",\"5269\",\"66.17954070981212\",\"10930\",\"48.16102470265325\",\"64.24368950464984\",\"97.92410871867278\",\"4056\",\"3487\",\"105.52799392674132\"]}]},{\"dimensions\":[\"Maxthon\"],\"metrics\":[{\"values\":[\"4\",\"4\",\"100.0\",\"15\",\"26.666666666666668\",\"50.0\",\"68.36363636363636\",\"4\",\"4\",\"188.25\"]}]},{\"dimensions\":[\"Mozilla\"],\"metrics\":[{\"values\":[\"4\",\"4\",\"75.0\",\"13\",\"30.76923076923077\",\"50.0\",\"80.77777777777777\",\"3\",\"3\",\"181.75\"]}]},{\"dimensions\":[\"Mozilla Compatible Agent\"],\"metrics\":[{\"values\":[\"94\",\"94\",\"41.48936170212766\",\"104\",\"90.38461538461539\",\"94.68085106382979\",\"58.6\",\"82\",\"39\",\"6.24468085106383\"]}]},{\"dimensions\":[\"Nintendo Browser\"],\"metrics\":[{\"values\":[\"2\",\"2\",\"100.0\",\"5\",\"40.0\",\"50.0\",\"251.0\",\"2\",\"2\",\"376.5\"]}]},{\"dimensions\":[\"Opera\"],\"metrics\":[{\"values\":[\"50\",\"50\",\"54.0\",\"114\",\"43.859649122807014\",\"74.0\",\"78.53125\",\"33\",\"27\",\"100.58\"]}]},{\"dimensions\":[\"Opera Mini\"],\"metrics\":[{\"values\":[\"5\",\"5\",\"100.0\",\"9\",\"55.55555555555556\",\"60.0\",\"86.0\",\"5\",\"5\",\"68.8\"]}]},{\"dimensions\":[\"Puffin\"],\"metrics\":[{\"values\":[\"4\",\"4\",\"50.0\",\"8\",\"50.0\",\"75.0\",\"31.75\",\"2\",\"2\",\"31.75\"]}]},{\"dimensions\":[\"Python-urllib\"],\"metrics\":[{\"values\":[\"2\",\"2\",\"100.0\",\"5\",\"40.0\",\"0.0\",\"128.66666666666666\",\"2\",\"2\",\"193.0\"]}]},{\"dimensions\":[\"Safari\"],\"metrics\":[{\"values\":[\"13752\",\"13752\",\"69.93164630599186\",\"24947\",\"55.09680522708141\",\"67.62652705061082\",\"111.44438493126228\",\"10761\",\"9617\",\"90.81166375799884\"]}]},{\"dimensions\":[\"Safari (in-app)\"],\"metrics\":[{\"values\":[\"3369\",\"3369\",\"92.96527159394479\",\"4038\",\"83.43239227340268\",\"90.14544375185515\",\"88.98355754857997\",\"3273\",\"3132\",\"17.671119026417333\"]}]},{\"dimensions\":[\"Samsung Internet\"],\"metrics\":[{\"values\":[\"1113\",\"1113\",\"61.18598382749326\",\"2163\",\"51.45631067961165\",\"68.01437556154536\",\"100.37238095238095\",\"796\",\"681\",\"94.71069182389937\"]}]},{\"dimensions\":[\"UC Browser\"],\"metrics\":[{\"values\":[\"4\",\"4\",\"100.0\",\"8\",\"50.0\",\"50.0\",\"103.0\",\"4\",\"4\",\"103.0\"]}]},{\"dimensions\":[\"UCWEB\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]},{\"dimensions\":[\"YaBrowser\"],\"metrics\":[{\"values\":[\"1\",\"1\",\"100.0\",\"1\",\"100.0\",\"100.0\",\"0.0\",\"1\",\"1\",\"0.0\"]}]}],\"totals\":[{\"values\":[\"53106\",\"53106\",\"67.82096184988514\",\"101208\",\"52.451387242115246\",\"68.96019282190336\",\"103.35228477027617\",\"41093\",\"36017\",\"93.71080480548338\"]}]}}";

        if (jsonType.equals("simple")) {
            return json;
        }
        if (jsonType.equals("complex")) {
            return complexJson;
        }
        if (jsonType.equals("gaData")) {
            return gaData;
        }
        return null;
    }

    public static void customAnnotation() {
//        try {
////          Field[] field =   Class.forName("test.AnnotationTest");
////            System.out.println("the fields----------->"+Arrays.toString(field));
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(JsonPathTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
