/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author user
 */
public class FileUtils {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER = "Ticket Id, Type, Status, Subject, "
            + "Description, Suspected Trouble Component, Action Type, "
            + "SLA Responded, SLA Resolved, Created By, Created Time, Last Modified Time";

    public static Object readXML(String fileName, Class inputClass) {
        try {
            JAXBContext context = JAXBContext.newInstance(inputClass);
            Unmarshaller um = context.createUnmarshaller();
            Object obj = (Object) um.unmarshal(new FileReader(fileName));
            return obj;
        } catch (Exception ex) {

        }
        return null;
    }

    public static void writeXML(String fileName, Object object, Class inputClass) {
        try {
            // create JAXB context and instantiate marshaller
            JAXBContext context = JAXBContext.newInstance(inputClass);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Write to File
            m.marshal(object, new File(fileName));
            m.marshal(object, System.out);

        } catch (JAXBException ex) {

        }
    }

    public static String readFileAsString(String filename) throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(filename);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        String fileAsString = sb.toString();
        return fileAsString;
    }

}
