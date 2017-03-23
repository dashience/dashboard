/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.itextpdf.text.Rectangle;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.sl.usermodel.TableCell.BorderEdge;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;

/**
 *
 * @author deldot
 */
public class PPTTableTesting {
    public static void main(String[] args) throws IOException{
        XMLSlideShow ppt = new XMLSlideShow();

        // XSLFSlide#createSlide() with no arguments creates a blank slide
        XSLFSlide slide = ppt.createSlide();

        XSLFTable tbl = slide.createTable();
        tbl.setAnchor(new java.awt.Rectangle(100, 100, 450, 300));

        int numColumns = 3;
        int numRows = 5;
        XSLFTableRow headerRow = tbl.addRow();
        headerRow.setHeight(50);
        // header
        for(int i = 0; i < numColumns; i++) {
            XSLFTableCell th = headerRow.addCell();
            XSLFTextParagraph p = th.addNewTextParagraph();
            p.setTextAlign(TextAlign.CENTER);
            XSLFTextRun r = p.addNewTextRun();
            r.setText("Header " + (i+1));
            r.setBold(true);
            r.setFontColor(Color.white);
            th.setFillColor(new Color(79, 129, 189));
            th.setBorderWidth(BorderEdge.bottom, 2.0);
            th.setBorderColor(BorderEdge.bottom, Color.white);

            tbl.setColumnWidth(i, 150);  // all columns are equally sized
        }
        
        // rows
        
        for(int rownum = 0; rownum < numRows; rownum ++){
            XSLFTableRow tr = tbl.addRow();
            tr.setHeight(50);
            // header
            for(int i = 0; i < numColumns; i++) {
                XSLFTableCell cell = tr.addCell();
                XSLFTextParagraph p = cell.addNewTextParagraph();
                XSLFTextRun r = p.addNewTextRun();

                r.setText("Cell " + (i+1));
                if(rownum % 2 == 0)
                    cell.setFillColor(new Color(208, 216, 232));
                else
                    cell.setFillColor(new Color(233, 247, 244));

            }
            
        }


        FileOutputStream out = new FileOutputStream("/tmp/table.pptx");
        ppt.write(out);
        out.close();
        
        ppt.close();
    }
    
}
