/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.DealerDao;
import com.visumbu.vb.admin.dao.UserDao;
import com.visumbu.vb.bean.LoginUserBean;
import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.AccountUser;
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.AgencyLicence;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.AgencySettings;
import com.visumbu.vb.model.Currency;
import com.visumbu.vb.model.Dealer;
import com.visumbu.vb.model.Property;
import com.visumbu.vb.model.UserAccount;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.utils.VbUtils;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jp
 */
@Service("pdfService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PdfService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DealerDao dealerDao;
    private String pdfGeneratorCommand = "wkhtmltopdf";
    private String pdfFilesPath = "/tmp/";

    public String generatePdf(String url, String windowStatus) {
        System.out.println("Generating PDF");
        List<String> commandsList = new ArrayList<>();
        commandsList.add("xvfb-run");
        commandsList.add(pdfGeneratorCommand);
        try {
            // command ===> wkhtmltopdf --window-status done cover http://localhost:8080/dashboard/index.html#/viewPdf/27/Jose/30/Product%201/271?startDate=4~2F28~2F2017\&endDate=5~2F27~2F2017 test.pdf
            String windowStatusCommand = "";
            if (windowStatus != null) {
                commandsList.add("--window-status");
                commandsList.add(windowStatus);
            } else {
                
            }
            commandsList.add("--print-media-type");
            commandsList.add("cover");
            commandsList.add(url);
            String filename = pdfFilesPath + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".pdf";
            commandsList.add(filename);
            String command = pdfGeneratorCommand + " " + windowStatusCommand + " cover \"" + url + "\" " + filename;
            System.out.println(command);
            java.lang.Runtime rt = java.lang.Runtime.getRuntime();
            String[] commandToExecute = (String[])commandsList.toArray(new String[commandsList.size()]);
            java.lang.Process p = rt.exec(commandToExecute);
            p.waitFor();
            return filename;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Logger.getLogger(PdfService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(PdfService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
