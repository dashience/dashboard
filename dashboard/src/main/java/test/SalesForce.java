/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.visumbu.vb.utils.Rest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dashience
 */
public class SalesForce {

    public static void main(String[] args) {
        getSalesForceData();
    }

  public static List<Map<String, Object>> getSalesForceData() {
        String url = "http://111.93.224.129:5000/contacts";
        String salesForceData = Rest.getData(url);
        System.out.println("data -->" + salesForceData);
        return null;
    }
}
