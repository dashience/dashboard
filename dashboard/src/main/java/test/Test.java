/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.math.BigInteger;

/**
 *
 * @author sabari
 */
public class Test {

    public static void main(String[] args) {

        String accountID = "104869273610744594745";
//        Long id=Long.parseLong(accountID);
        BigInteger i = new BigInteger(accountID);
        System.out.println("Account id  value is -->" + i);

    }
}
