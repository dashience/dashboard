/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import au.com.bytecode.opencsv.CSVWriter;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.utils.Rest;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.data.io.CSV;

/**
 *
 * @author deeta1
 */
public class Semrush {

    public static void main(String[] args) throws IOException {
        String path = "/tmp/data.csv";
        String url = "http://api.semrush.com/?type=domain_organic&key=2b88db8e48f0f8b1fdcf3007cfae388a&display_filter=%2B%7CPh%7CCo%7Cseo&display_limit=10&export_columns=Ph,Po,Pp,Pd,Nq,Cp,Ur,Tr,Tc,Co,Nr,Td&domain=seobook.com&display_sort=tr_desc&database=us";
//        parseCsvToJson(path);
        parseUrlByRest(url);

    }

    private static List<Map<String, Object>> parseCsvToJson(String path) throws FileNotFoundException, IOException {

        //Create the CSVFormat object
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(';');
        //initialize the CSVParser object
//        CSVParser parser = new CSVParser(new FileReader(path), format);
        CSVParser parser = new CSVParser(new FileReader(path), format);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        System.out.println("Heder map data");
        System.out.println(headerMap);

        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Map.Entry<String, Integer> entrySet : headerMap.entrySet()) {
            String key = entrySet.getKey();
            Integer value = entrySet.getValue();
            ColumnDef columnDef = new ColumnDef(key, "string", key);
            columnDefs.add(columnDef);

        }
        Map returnMap = new HashMap();
        returnMap.put("columnDefs", columnDefs);
        List<Map<String, Object>> data = new ArrayList<>();
        System.out.println(headerMap);
        for (CSVRecord record : parser) {
            Map<String, Object> dataMap = new HashMap<>();
            for (Map.Entry<String, Integer> entrySet : headerMap.entrySet()) {
                String key = entrySet.getKey();
                dataMap.put(key, record.get(key));
            }
            data.add(dataMap);
        }

        System.out.println(data);
        returnMap.put("data", data);

        //close the parser
        parser.close();
        return data;
    }

    private static void parseUrlByRest(String url) throws IOException {
        String data1 = Rest.getData(url);
        System.out.println("Data is ");
        System.out.println("*************");
        System.out.println(data1);
//        
//        String data = "Keyword;Position;Previous Position;Position Difference;Search Volume;CPC;Url;Traffic (%);Traffic Cost (%);Competition;Number of Results;Trendsseo tools;1;2;1;6600;11.76;http://tools.seobook.com/;10.12;29.94;0.84;10300000;1.00,0.81,0.81,0.81,0.81,0.81,0.81,0.81,0.67,0.67,0.81,0.81seo training;3;3;0;5400;10.23;http://training.seobook.com/;1.58;4.08;0.78;3580000;1.00,0.03,0.02,0.02,0.02,0.01,0.02,0.02,0.01,0.01,0.02,0.01seobook;1;1;0;590;8.26;http://www.seobook.com/;1.54;3.19;0.07;815000;0.82,0.82,0.82,0.82,0.82,0.82,0.67,0.82,0.67,0.67,1.00,0.82free seo tools;1;1;0;1000;8.08;http://tools.seobook.com/;1.53;3.11;0.76;6600000;0.77,0.68,0.77,1.00,0.77,0.68,0.77,0.68,0.68,0.68,0.77,0.77seo;19;18;-1;90500;10.96;http://www.seobook.com/;0.88;2.44;0.70;456000000;1.00,0.55,0.55,0.55,0.55,0.55,0.55,0.55,0.55,0.45,0.55,0.67seo book;1;1;0;480;7.37;http://www.seobook.com/;0.73;1.36;0.28;3900000;0.67,0.67,1.00,0.82,0.82,0.67,0.67,0.82,0.67,0.67,1.00,0.67seo glossary;1;1;0;260;0.00;http://www.seobook.com/glossary/;0.39;0.00;0.14;681000;0.54,0.35,0.81,0.81,0.54,0.54,0.44,0.67,1.00,0.81,0.23,0.19seo tool;2;2;0;880;11.50;http://tools.seobook.com/;0.37;1.07;0.79;3060000;1.00,0.88,1.00,0.88,0.88,0.88,0.88,0.88,0.72,0.59,0.72,0.59seo basics;5;5;0;1600;5.02;http://www.seobook.com/learn-seo/seo-basics/;0.26;0.32;0.50;14400000;1.00,0.84,0.84,1.00,1.00,1.00,1.00,0.68,0.68,0.68,0.84,0.84is seo dead;1;1;0;170;0.00;http://www.seobook.com/learn-seo/infographics/death-of-seo.php;0.25;0.00;0.09;1080000;0.81,0.67,0.81,0.67,0.67,0.81,1.00,1.00,0.81,0.81,1.00,1.00";
//
//        String[] splitString = data1.split("\\;");
//
//        for (int i = 0; i < splitString.length; i++) {
//            System.out.println("splitString[" + i + "] is " + splitString[i]);
//        }
    }
//        
//        String fileName="/tmp/data1.csv";
////        CSVWriter writer=new CSVWriter(new FileWriter(fileName));
//         String[] data1 = data.split("\\;");
}
