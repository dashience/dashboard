/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.PdfService;
import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.controller.BaseController;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author jp
 */
@Controller
@RequestMapping("pdf")
public class PdfController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UiService uiService;
    @Autowired
    private PdfService pdfService;

    private String pdfGeneratorCommand = "wkhtmltopdf";
    private String pdfFilesPath = "/tmp/";

    @RequestMapping(value = "download", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    void download(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getParameter("url");
        System.out.println("PDF URL ___________________"+url);
        String windowStatus = request.getParameter("windowStatus");
        System.out.println("Start Date "  + request.getParameter("startDate"));
        System.out.println("End Date " + request.getParameter("endDate"));
        if (url == null) {
            url = request.getHeader("referer");
        }
        if (url == null) {
            return;
        }
        String filePath = pdfService.generatePdf(url, windowStatus);
        System.out.println("PDF GENERATED " + filePath);
        InputStream fis = null;
        try {
            java.io.File file = new java.io.File(filePath);
            String filename = file.getName();
            response.setContentType("application/octet-stream");
            fis = new FileInputStream(filePath);
            response.addHeader("content-disposition", "attachment; filename=\"" + filename + "\"");
            OutputStream out = response.getOutputStream();
            byte[] bufferData = new byte[1024];
            int read = 0;
            while ((read = fis.read(bufferData)) != -1) {
                out.write(bufferData, 0, read);
            }
            out.flush();
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PdfController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(PdfController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
