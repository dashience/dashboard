/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author deldot
 */
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.visumbu.vb.model.TabWidget;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.poi.sl.usermodel.TableCell;
import org.apache.poi.sl.usermodel.TextParagraph;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;

public class PPTReportDesigner extends CustomReportDesigner{

    public void dynamicPPTTable(List<TabWidget> tabWidgets, OutputStream out) throws DocumentException, IOException {

        //creating a presentation
        XMLSlideShow ppt = new XMLSlideShow();
        try {

            //creating a slide in it
            for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
                TabWidget tabWidget = iterator.next();
                XSLFSlide slide = ppt.createSlide();
                if (tabWidget.getChartType().equalsIgnoreCase("table")) {
                    XSLFTable tbl = slide.createTable();
                    //  XSLFSlideMaster slideMaster = ppt.getSlideMasters().get(0);
                    // XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
                    BaseColor tableTitleFontColor = new BaseColor(132, 140, 99);
                    XSLFTableRow headerRow = tbl.addRow();
                    headerRow.setHeight(50);
                    XSLFTableCell th = headerRow.addCell();
                    XSLFTextParagraph p = th.addNewTextParagraph();
                    p.setTextAlign(TextParagraph.TextAlign.CENTER);
                    XSLFTextRun r = p.addNewTextRun();
                    r.setText("Table ");
                    r.setBold(true);
                    r.setFontColor(Color.white);
                    th.setFillColor(new Color(79, 129, 189));
                    th.setBorderWidth(TableCell.BorderEdge.bottom, 2.0);
                    th.setBorderColor(TableCell.BorderEdge.bottom, Color.white);
                    System.out.println("table if condition");
                    tbl = dynamicPptTable(tabWidget, slide);
                } else if (tabWidget.getChartType().equalsIgnoreCase("pie")) {
                    
                } else if (tabWidget.getChartType().equalsIgnoreCase("bar")) {
                    
                }  else if (tabWidget.getChartType().equalsIgnoreCase("line")) {
                    
                }

            }
            out = new FileOutputStream("/home/deldot/Pictures/ppttable.pptx");
            ppt.write(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PPTReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PPTReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
            ppt.close();
        }
    }
}
