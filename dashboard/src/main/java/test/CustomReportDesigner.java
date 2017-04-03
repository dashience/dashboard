/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.WidgetColumn;
import com.visumbu.vb.pdf.L2TReportHeader;
import com.visumbu.vb.pdf.ReportHeader;
import com.visumbu.vb.utils.ApiUtils;
import com.visumbu.vb.utils.Formatter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.apache.poi.sl.usermodel.Insets2D;
import org.apache.poi.sl.usermodel.TableCell;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;

import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jsoup.Jsoup;

/**
 *
 * @author user
 */
public class CustomReportDesigner {

    private static List<CalcualtedFunction> calcualtedFunctions = new ArrayList<>();
//    private static BaseColor widgetTitleColor = new BaseColor(90, 113, 122, 28);
//    private static BaseColor tableHeaderColor = new BaseColor(255, 0, 0);
//    private static BaseColor tableFooterColor = new BaseColor(241, 241, 241);
//    private static BaseColor widgetBorderColor = BaseColor.DARK_GRAY;

    private static BaseColor widgetTitleColor = BaseColor.WHITE;
    private static BaseColor tableHeaderFontColor = new BaseColor(61, 70, 77);
    private static BaseColor tableHeaderColor = new BaseColor(241, 241, 241);
    private static BaseColor tableFooterColor = new BaseColor(241, 241, 241);
    private static BaseColor widgetBorderColor = new BaseColor(204, 204, 204);

    private static final String FONT = CustomReportDesigner.class.getResource("") + "../../../static/lib/fonts/proxima/proximanova-reg-webfont.woff"; // "E:\\work\\vizboard\\dashboard\\src\\main\\webapp\\static\\lib\\fonts\\proxima\\proximanova-reg-webfont.woff";
    private static final Rectangle pageSize = PageSize.A2;
    private static final float widgetWidth = pageSize.getWidth() - 100;
    private static final float widgetHeight = 300;
    private static final ReportHeader reportHeader = new L2TReportHeader();

    final static Logger log = Logger.getLogger(CustomReportDesigner.class);

    static {
        FontFactory.register(FONT, "proxima_nova_rgregular");
        calcualtedFunctions.add(new CalcualtedFunction("ctr", "data__clicks", "data__impressions"));
        calcualtedFunctions.add(new CalcualtedFunction("cpa", "data__cost", "data__conversions"));
        calcualtedFunctions.add(new CalcualtedFunction("cpas", "data__spend", "data__conversions"));
        calcualtedFunctions.add(new CalcualtedFunction("cpc", "data__cost", "data__clicks"));
        calcualtedFunctions.add(new CalcualtedFunction("cpcs", "data__spend", "data__clicks"));
        calcualtedFunctions.add(new CalcualtedFunction("cpr", "data__spend", "data__actions_post_reaction"));
        calcualtedFunctions.add(new CalcualtedFunction("ctl", "data__spend", "data__actions_like"));
        calcualtedFunctions.add(new CalcualtedFunction("cplc", "data__spend", "data__actions_link_click"));
        calcualtedFunctions.add(new CalcualtedFunction("cpcomment", "data__spend", "data__actions_comment"));
        calcualtedFunctions.add(new CalcualtedFunction("cposte", "data__spend", "data__actions_post_engagement"));
        calcualtedFunctions.add(new CalcualtedFunction("cpagee", "data__spend", "data__actions_page_engagement"));
        calcualtedFunctions.add(new CalcualtedFunction("cpp", "data__spend", "data__actions_post"));
    }
    Font pdfFont = FontFactory.getFont("proxima_nova_rgregular", "Cp1253", true);
    Font pdfFontTitle = FontFactory.getFont("proxima_nova_rgregular", "Cp1253", true);
    Font pdfFontHeader = FontFactory.getFont("proxima_nova_rgregular", "Cp1253", true);

    private Boolean isZeroRow(Map<String, Object> mapData, List<WidgetColumn> columns) {
        System.out.println("Start Function of isZeroRow");
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (ApiUtils.toDouble(mapData.get(column.getFieldName()) + "") != 0) {
                return false;
            }
        }
        System.out.println("End Function of isZeroRow");
        return true;
    }

    private Double sum(List<Map<String, Object>> data, String fieldName) {
        System.out.println("Start Function of sum");
        Double sum = 0.0;
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            sum += ApiUtils.toDouble(mapData.get(fieldName) + "");
        }
        System.out.println("End Function of sum");
        return sum;
    }

    private Double min(List<Map<String, Object>> data, String fieldName) {
        System.out.println("Start Function of min");
        Double min = null;
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            if (min == null || ApiUtils.toDouble(mapData.get(fieldName) + "") < min) {
                min = ApiUtils.toDouble(mapData.get(fieldName) + "");
            }
        }
        System.out.println("End Function of min");
        return min;
    }

    private Double calulatedMetric(List<Map<String, Object>> data, CalcualtedFunction calcualtedFunction) {
        System.out.println("Start Function of calulatedMetric");
        String name = calcualtedFunction.getName();
        String field1 = calcualtedFunction.getField1();
        String field2 = calcualtedFunction.getField2();
        Double sum1 = sum(data, field1);
        Double sum2 = sum(data, field2);
        if (sum1 != 0 && sum2 != 0) {
            return sum1 / sum2;
        }
        System.out.println("End Function of calulatedMetric");
        return 0.0;
    }

    private Double max(List<Map<String, Object>> data, String fieldName) {
        System.out.println("Start Function of max");

        Double max = null;
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            if (max == null || ApiUtils.toDouble(mapData.get(fieldName) + "") > max) {
                max = ApiUtils.toDouble(mapData.get(fieldName) + "");
            }
        }
        System.out.println("End Function of max");
        return max;
    }

    private String format(WidgetColumn column, String value) {
        System.out.println("Start Function of format");
        System.out.println("End Function of format");
        return value;
    }

    enum DAY {
        Monday("Monday", 2), Tuesday("Tuesday", 3), Wednesday("Wednesday", 4), Thursday("Thursday", 5), Friday("Friday", 6), Saturday("Saturday", 7), Sunday("Sunday", 1);
        private String m_name;
        private int m_weight;

        DAY(String name, int weight) {
            m_name = name;
            m_weight = weight;
        }

        public int getWeight() {
            return this.m_weight;
        }
    }

    DateFormat primaryFormat = new SimpleDateFormat("h:mm a");
    DateFormat secondaryFormat = new SimpleDateFormat("H:mm");

    public int timeInMillis(String time) throws ParseException {
        System.out.println("time: " + time);
        return timeInMillis(time, primaryFormat);
    }

    private int timeInMillis(String time, DateFormat format) throws ParseException {
        // you may need more advanced logic here when parsing the time if some times have am/pm and others don't.
        System.out.println("Start Function of timeInMillis");
        try {
            Date date = format.parse(time);
            System.out.println("Date: " + date);
            System.out.println("End Function of timeInMillis");
            return (int) date.getTime();
        } catch (ParseException e) {
            if (format != secondaryFormat) {
                System.out.println("End Function of timeInMillis");
                return timeInMillis(time, secondaryFormat);
            } else {
                throw e;
            }
        }
    }

    private List<Map<String, Object>> sortData(List<Map<String, Object>> data, List<SortType> sortType) {
        System.out.println("Start Function of sortData");
        Collections.sort(data, (Map<String, Object> o1, Map<String, Object> o2) -> {
            for (Iterator<SortType> iterator = sortType.iterator(); iterator.hasNext();) {
                SortType sortType1 = iterator.next();
                // TODO: Should remove and fix with correct logic  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                int order = 1;
                System.out.println("sort Order: " + sortType1.getSortOrder());
                System.out.println("Field type: " + sortType1.getFieldType());
                String day1 = o1.get(sortType1.getFieldName()) + "";
                String day2 = o2.get(sortType1.getFieldName()) + "";
                System.out.println("day1: " + day1);
                System.out.println("day2: " + day2);
                System.out.println("day1 length: " + day1.length());

                if (day1.length() == 10) {
                    if ((day1.substring(4, 5).equalsIgnoreCase("-") || day1.substring(4, 5).equalsIgnoreCase("/")) && (day2.substring(4, 5).equalsIgnoreCase("-") || day2.substring(4, 5).equalsIgnoreCase("/"))) {
                        System.out.println("Date ---->");
                        try {
                            Date date1 = sdf.parse(day1);
                            Date date2 = sdf.parse(day2);
                            return date1.compareTo(date2);
                        } catch (ParseException ex) {
                            log.error("Parse Exception in  sortData function:  " + ex);
                            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                if (day1.length() >= 6) {
                    System.out.println("Days ------>");
                    if (day1.substring(day1.length() - 3, day1.length()).equalsIgnoreCase("day") && day2.substring(day2.length() - 3, day2.length()).equalsIgnoreCase("day")) {
                        DAY dayOne = DAY.valueOf(day1);
                        System.out.println("dayOne: " + dayOne);
                        DAY dayTwo = DAY.valueOf(day2);
                        System.out.println("dayTwo: " + dayTwo);
                        return dayOne.getWeight() - dayTwo.getWeight();
                    }
                }

                if (day1.length() == 4 || day1.length() == 5) {
                    System.out.println("Time ------>");
                    if ((day1.substring(day1.length() - 2, day1.length()).equalsIgnoreCase("pm") || day1.substring(day1.length() - 2, day1.length()).equalsIgnoreCase("am")) && (day2.substring(day2.length() - 2, day2.length()).equalsIgnoreCase("pm") || day2.substring(day2.length() - 2, day2.length()).equalsIgnoreCase("am"))) {
                        try {
                            StringBuilder time1, time2;
                            if (day1.length() == 5 && day2.length() == 5) {
                                System.out.println("if ---> 1");
                                time1 = new StringBuilder(day1);
                                time2 = new StringBuilder(day2);
                                if (time1.substring(0, 2).equalsIgnoreCase("10")) {
                                    time1.replace(1, 2, "0:00");
                                } else if (time1.substring(0, 2).equalsIgnoreCase("11")) {
                                    time1.replace(1, 2, "1:00");
                                } else {
                                    time1.replace(1, 2, ":00");
                                }
                                if (time2.substring(0, 2).equalsIgnoreCase("10")) {
                                    time2.replace(1, 2, "0:00");
                                } else if (time2.substring(0, 2).equalsIgnoreCase("11")) {
                                    time2.replace(1, 2, "1:00");
                                } else {
                                    time2.replace(1, 2, ":00");
                                }
                                return timeInMillis(time1.toString()) - timeInMillis(time2.toString());
                            } else if (day1.length() == 5 && day2.length() == 4) {
                                System.out.println("if ---> 2");
                                time1 = new StringBuilder(day1);
                                time2 = new StringBuilder(day2);
                                if (time1.substring(0, 2).equalsIgnoreCase("10")) {
                                    time1.replace(1, 2, "0:00");
                                } else if (time1.substring(0, 2).equalsIgnoreCase("11")) {
                                    time1.replace(1, 2, "1:00");
                                } else {
                                    time1.replace(1, 2, ":00");
                                }
                                time2.replace(1, 1, ":00");
                                return timeInMillis(time1.toString()) - timeInMillis(time2.toString());
                            } else if (day1.length() == 4 && day2.length() == 5) {
                                System.out.println("if ---> 3");
                                time1 = new StringBuilder(day1);
                                time2 = new StringBuilder(day2);
                                if (time2.substring(0, 2).equalsIgnoreCase("10")) {
                                    time2.replace(1, 2, "0:00");
                                } else if (time2.substring(0, 2).equalsIgnoreCase("11")) {
                                    time2.replace(1, 2, "1:00");
                                } else {
                                    time2.replace(1, 2, ":00");
                                }
                                time1.replace(1, 1, ":00");
                                return timeInMillis(time1.toString()) - timeInMillis(time2.toString());
                            } else if (day1.length() == 4 && day2.length() == 4) {
                                System.out.println("if ---> 4");
                                time1 = new StringBuilder(day1);
                                time2 = new StringBuilder(day2);
                                time1.replace(1, 1, ":00");
                                time2.replace(1, 1, ":00");
                                return timeInMillis(time1.toString()) - timeInMillis(time2.toString());
                            } else {
                                continue;
                            }
                        } catch (ParseException ex) {
                            log.error("Parse Exception in  sortData function:  " + ex);
                            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        continue;
                    }
                }
                // TODO : REMOVE TILL THIS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

                if (sortType1.getSortOrder().equalsIgnoreCase("desc")) {
                    order = -1;
                }
                if (sortType1.getFieldType().equalsIgnoreCase("number")) {
                    Double value1 = ApiUtils.toDouble(o1.get(sortType1.getFieldName()) + "");
                    Double value2 = ApiUtils.toDouble(o2.get(sortType1.getFieldName()) + "");
                    if (value1 != value2) {
                        return order * new Double(value1 - value2).intValue();
                    }
                } else {
                    String value1 = o1.get(sortType1.getFieldName()) + "";
                    String value2 = o2.get(sortType1.getFieldName()) + "";
                    if (value1.compareTo(value2) != 0) {
                        return order * value1.compareTo(value2);
                    }
                }
            }
            return 0;
        });

//        Collections.sort(data, new Comparator<Map<String, Object>>() {
//            @Override
//            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//                for (Iterator<SortType> iterator = sortType.iterator(); iterator.hasNext();) {
//                    SortType sortType = iterator.next();
//                    
//                }
//                return 0;
//            }
//        });
        System.out.println("End function of sortData");
        return data;
    }

    private Map<String, List<Map<String, Object>>> groupBy(List<Map<String, Object>> data, String groupField) {
        System.out.println("Start function of groupBy");
        Map<String, List<Map<String, Object>>> returnMap = new HashMap<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            String fieldValue = dataMap.get(groupField) + "";
            List<Map<String, Object>> groupDataList = returnMap.get(fieldValue);

            if (groupDataList == null) {
                groupDataList = new ArrayList<>();
            }
            groupDataList.add(dataMap);
            returnMap.put(fieldValue, groupDataList);
        }
        System.out.println("End function of groupBy");
        return returnMap;
    }

    private List groupData(List<Map<String, Object>> data, List<String> groupByFields, List<Aggregation> aggreagtionList) {
        System.out.println("Start function of groupData");
        List<String> currentFields = groupByFields;
        if (groupByFields.size() == 0) {
            return data;
        }
        List<Map<String, Object>> actualList = data;
        List<Map<String, Object>> groupedData = new ArrayList<>();
        String groupingField = currentFields.get(0);
        Map<String, List<Map<String, Object>>> currentListGrouped = groupBy(actualList, groupingField);
        for (Map.Entry<String, List<Map<String, Object>>> entrySet : currentListGrouped.entrySet()) {
            String key = entrySet.getKey();
            List<Map<String, Object>> value = entrySet.getValue();
            Map dataToPush = new HashMap<>();
            dataToPush.put("_key", key);
            dataToPush.put(groupingField, key);
            dataToPush.put("_groupField", groupingField);
            // Merge aggregation
            dataToPush.putAll(aggregateData(value, aggreagtionList));
            dataToPush.put("data", groupData(value, groupByFields.subList(1, groupByFields.size()), aggreagtionList));
            groupedData.add(dataToPush);
        }
        System.out.println("End function of groupData");
        return groupedData;
    }

    private Map<String, Object> aggregateData(List<Map<String, Object>> data, List<Aggregation> aggreagtionList) {
        System.out.println("Start function of aggregateData");

        Map<String, Object> returnMap = new HashMap<>();
        for (Iterator<Aggregation> iterator = aggreagtionList.iterator(); iterator.hasNext();) {
            Aggregation aggregation = iterator.next();
            if (aggregation.getAggregationType().equalsIgnoreCase("sum")) {
                returnMap.put(aggregation.getFieldName(), sum(data, aggregation.getFieldName()) + "");
            }
            if (aggregation.getAggregationType().equalsIgnoreCase("avg")) {
                returnMap.put(aggregation.getFieldName(), (sum(data, aggregation.getFieldName()) / data.size()) + "");
            }
            if (aggregation.getAggregationType().equalsIgnoreCase("count")) {
                returnMap.put(aggregation.getFieldName(), data.size() + "");
            }
            if (aggregation.getAggregationType().equalsIgnoreCase("min")) {
                returnMap.put(aggregation.getFieldName(), min(data, aggregation.getFieldName()) + "");
            }
            if (aggregation.getAggregationType().equalsIgnoreCase("max")) {
                returnMap.put(aggregation.getFieldName(), max(data, aggregation.getFieldName()) + "");
            }
            for (Iterator<CalcualtedFunction> iterator1 = calcualtedFunctions.iterator(); iterator1.hasNext();) {
                CalcualtedFunction calcualtedFunction = iterator1.next();
                if (aggregation.getAggregationType().equalsIgnoreCase(calcualtedFunction.getName())) {
                    returnMap.put(aggregation.getFieldName(), calulatedMetric(data, calcualtedFunction) + "");
                }
            }
        }
        System.out.println("Aggregation data: " + returnMap);
        System.out.println("End function of aggregateData");
        return returnMap;
    }

    public PdfPTable generateTextPdfTable(TabWidget tabWidget) {
        System.out.println("Start function of generateTextPdfTable");

        try {
            BaseColor tableTitleFontColor = new BaseColor(132, 140, 99);

            PdfPTable table = new PdfPTable(1);
            PdfPCell titleCell;
            pdfFontTitle.setSize(14);
            pdfFontTitle.setStyle(Font.BOLD);
            pdfFontTitle.setColor(tableTitleFontColor);
            titleCell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
            titleCell.setHorizontalAlignment(1);
            titleCell.setColspan(1);
            titleCell.setBorderColor(widgetBorderColor);
            titleCell.setBackgroundColor(widgetTitleColor);
            titleCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            titleCell.setPadding(10);
            table.addCell(titleCell);

            pdfFont.setSize(12);
            pdfFont.setColor(tableHeaderFontColor);
            if (tabWidget.getContent() != null) {
                String HTML = tabWidget.getContent();
                PdfPCell dataCell = new PdfPCell();
                for (Element e : XMLWorkerHelper.parseToElementList(HTML, null)) {
                    dataCell.addElement(e);
                }
                dataCell.setHorizontalAlignment(1);
                dataCell.setColspan(1);
                dataCell.setNoWrap(false);
                dataCell.setBorderColor(widgetBorderColor);
                dataCell.setBackgroundColor(widgetTitleColor);
                dataCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                dataCell.setPadding(5);
                table.addCell(dataCell);
                table.setWidthPercentage(95f);
            } else if (tabWidget.getContent() == null || tabWidget.getContent().isEmpty()) {
                PdfPCell dataCell = new PdfPCell(new Phrase(""));
                dataCell.setHorizontalAlignment(1);
                dataCell.setColspan(1);
                dataCell.setNoWrap(false);
                dataCell.setBorderColor(widgetBorderColor);
                dataCell.setBackgroundColor(widgetTitleColor);
                dataCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                dataCell.setPadding(5);
                table.addCell(dataCell);
                table.setWidthPercentage(95f);
            }
            return table;
        } catch (IOException ex) {
            log.error("IO Exception in  sortData function:  " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("End function of generateTextPdfTable");
        return null;
    }

    public PdfPTable dynamicPdfTable(TabWidget tabWidget) throws DocumentException {
        System.out.println("Start function of dynamicPdfTable");
//        BaseColor textHighlightColor = new BaseColor(242, 156, 33);
        BaseColor tableTitleFontColor = new BaseColor(132, 140, 99);

        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, Object>> originalData = tabWidget.getData();
        List<Map<String, Object>> data = new ArrayList<>(originalData);
        // System.out.println(tabWidget.getWidgetTitle() + "Actual Size ===> " + data.size());
        List<Map<String, Object>> tempData = new ArrayList<>();

        if (data == null || data.isEmpty()) {
            PdfPTable table = new PdfPTable(columns.size());
            PdfPCell cell;
            pdfFontTitle.setSize(14);
            pdfFontTitle.setStyle(Font.BOLD);
            pdfFontTitle.setColor(tableTitleFontColor);
            cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
            cell.setHorizontalAlignment(1);
            cell.setColspan(columns.size());
            cell.setBorderColor(widgetBorderColor);
            cell.setBackgroundColor(widgetTitleColor);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setPadding(10);
            table.addCell(cell);
            table.setWidthPercentage(95f);
            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                pdfFontHeader.setSize(13);
                pdfFontHeader.setColor(tableHeaderFontColor);

                PdfPCell dataCell = new PdfPCell(new Phrase(WordUtils.capitalize(column.getDisplayName()), pdfFontHeader));
                dataCell.setPadding(5);
                dataCell.setBorderColor(widgetBorderColor);
                dataCell.setBackgroundColor(tableHeaderColor);
                if (column.getAlignment() != null) {
                    dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                }
                table.addCell(dataCell);
            }
            return table;
        }
        // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size****5 " + data.size());

        if (tabWidget.getZeroSuppression() != null && tabWidget.getZeroSuppression()) {
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                if (!isZeroRow(dataMap, columns)) {
                    tempData.add(dataMap);
                }
            }
            // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size****4 " + tempData.size());

            data = tempData;
        }
        // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size****3 " + data.size());

        List<SortType> sortFields = new ArrayList<>();
        List<Aggregation> aggreagtionList = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();

        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getSortOrder() != null) {
                sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
            }
            if (column.getAgregationFunction() != null) {
                aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
            }
            if (column.getGroupPriority() != null) {
                groupByFields.add(column.getFieldName());
            }
        }
        if (sortFields.size() > 0) {
            data = sortData(data, sortFields);
        }
        // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size****2 " + data.size());

        if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
            data = data.subList(0, tabWidget.getMaxRecord());
        }
        Map groupedMapData = new HashMap();
        // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size****1 " + data.size());

        // System.out.prin`tln("Group by Fields --> " + groupByFields.size());
        // System.out.println(groupByFields);
        List<String> originalGroupByFields = new ArrayList<>(groupByFields);
        if (groupByFields.size() > 0) {
            List groupedData = groupData(data, groupByFields, aggreagtionList);

            groupedMapData.putAll(aggregateData(data, aggreagtionList));
            groupedMapData.put("_groupFields", originalGroupByFields);
            groupedMapData.put("data", groupedData);
        } else {
            groupedMapData.putAll(aggregateData(data, aggreagtionList));
            groupedMapData.put("data", data);
        }
        // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size " + data.size());
        // System.out.println(groupedMapData.get("_groupFields"));
        System.out.println("End function of dynamicPdfTable");

        return generateTable(groupedMapData, tabWidget);

    }

    private void generateGroupedRows(Map groupedData, TabWidget tabWidget, PdfPTable table) {
        System.out.println("Start function of generateGroupedRows");
        BaseColor tableTitleFontColor = new BaseColor(132, 140, 99);
        List<WidgetColumn> columns = tabWidget.getColumns();
        List data = (List) groupedData.get("data");
        int count = 0;
        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
            Map mapData = (Map) iterator.next();
            if (mapData.get(mapData.get("_groupField")) != null) {
                count = 1;
                String groupValue = mapData.get(mapData.get("_groupField")) + "";
                pdfFont.setColor(tableHeaderFontColor);
                PdfPCell dataCell = new PdfPCell(new Phrase(groupValue, pdfFont));
                dataCell.setBorderColor(widgetBorderColor);
                table.addCell(dataCell);
            } else {
                if (data.size() > 1) {
                    count = 2;
                    PdfPCell dataCell = new PdfPCell(new Phrase(""));
                    dataCell.setBorderColor(widgetBorderColor);
                    table.addCell(dataCell);
                }
            }
            if (count == 1 || count == 2) {
                for (Iterator<WidgetColumn> iterator1 = columns.iterator(); iterator1.hasNext();) {
                    WidgetColumn column = iterator1.next();
                    if (column.getColumnHide() == null || column.getColumnHide() == 0) {
                        if (mapData.get(column.getFieldName()) != null) {
                            String value = mapData.get(column.getFieldName()) + "";
                            if (column.getDisplayFormat() != null) {
                                value = Formatter.format(column.getDisplayFormat(), value);
                            }
                            pdfFont.setColor(tableHeaderFontColor);
                            PdfPCell dataCell = new PdfPCell(new Phrase(value, pdfFont));
                            if (column.getAlignment() != null) {
                                dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                            }
                            dataCell.setBorderColor(widgetBorderColor);
                            table.addCell(dataCell);
                        } else {
                            PdfPCell dataCell = new PdfPCell(new Phrase(""));
                            dataCell.setBorderColor(widgetBorderColor);
                            table.addCell(dataCell);
                        }
                    }
                }
            }
            if (mapData.get("data") != null) {
                generateGroupedRows(mapData, tabWidget, table);
            }
        }
        System.out.println("End function of generateGroupedRows");
    }

    public XSLFTable staticPptTable(XSLFSlide slide) {
        System.out.println("Start function of staticPptTable");
        XSLFTable tbl = slide.createTable();
        tbl.setAnchor(new java.awt.Rectangle(30, 30, 450, 300));
        int numColumns = 3;
        int numRows = 5;
        XSLFTableRow headerRow = tbl.addRow();
        headerRow.setHeight(50);
        // header
        for (int i = 0; i < numColumns; i++) {
            XSLFTableCell th = headerRow.addCell();
            XSLFTextParagraph p = th.addNewTextParagraph();
            p.setTextAlign(TextParagraph.TextAlign.CENTER);
            XSLFTextRun r = p.addNewTextRun();
            r.setText("Header " + (i + 1));
            r.setBold(true);
            r.setFontColor(Color.white);
            th.setFillColor(new Color(79, 129, 189));
            th.setBorderWidth(TableCell.BorderEdge.bottom, 2.0);
            th.setBorderColor(TableCell.BorderEdge.bottom, Color.white);

            tbl.setColumnWidth(i, 150);  // all columns are equally sized
        }

        // rows
        for (int rownum = 0; rownum < numRows; rownum++) {
            XSLFTableRow tr = tbl.addRow();
            tr.setHeight(50);
            // header
            for (int i = 0; i < numColumns; i++) {
                XSLFTableCell cell = tr.addCell();
                XSLFTextParagraph p = cell.addNewTextParagraph();
                XSLFTextRun r = p.addNewTextRun();

                r.setText("Cell " + (i + 1));
                if (rownum % 2 == 0) {
                    cell.setFillColor(new Color(208, 216, 232));
                } else {
                    cell.setFillColor(new Color(233, 247, 244));
                }

            }

        }
        System.out.println("End function of staticPptTable");
        return tbl;
    }

    private XSLFTable generateTableForPpt(Map groupedData, TabWidget tabWidget, XSLFSlide slide) {
        System.out.println("Start function of generateTableForPpt");
        //       BaseColor textHighlightColor = new BaseColor(242, 156, 33);
        Color tableTitleFontColor = new Color(132, 140, 99);
        Color widgetBorderColor = new Color(204, 204, 204);
        Color widgetTitleColor = Color.WHITE;
        Color tableHeaderFontColor = new Color(61, 70, 77);
        Color tableHeaderColor = new Color(241, 241, 241);
        Color tableFooterColor = new Color(241, 241, 241);

        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, Object>> data = tabWidget.getData();
        List<String> groupFields = (List< String>) groupedData.get("_groupFields");
        Integer noOfColumns = countColumns(columns); //.size();
        if (groupFields != null && groupFields.size() > 0) {
            noOfColumns++;
        }

        List<SortType> sortFields = new ArrayList<>();
        List<Aggregation> aggreagtionList = new ArrayList<>();

        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getSortOrder() != null) {
                sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
            }
            if (column.getAgregationFunction() != null) {
                aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
            }
//            if (column.getGroupPriority() != null) {
//                groupByFields.add(column.getFieldName());
//            }
        }
        if (sortFields.size() > 0) {
            data = sortData(data, sortFields);
        }

        if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
            data = data.subList(0, tabWidget.getMaxRecord());
        }

        //XSLFTableRow titleRow = tbl.addRow();
        //selection of title place holder
        XSLFTextBox txt = slide.createTextBox();
        txt.setText(tabWidget.getWidgetTitle());
        txt.setTextDirection(TextShape.TextDirection.HORIZONTAL);
        txt.setAnchor(new java.awt.Rectangle(25, 30, 300, 50));

        XSLFTextParagraph tp = txt.getTextParagraphs().get(0);
        tp.setTextAlign(TextAlign.LEFT);
        XSLFTextRun run = tp.getTextRuns().get(0);

        run.setBold(true);
        run.setFontSize(13.0);
        run.setFontColor(tableTitleFontColor);

        XSLFTable tbl = slide.createTable();
        tbl.setAnchor(new java.awt.Rectangle(30, 55, 640, 480));
        XSLFTableRow headerRow = tbl.addRow();

        if (groupFields != null && groupFields.size() > 0) {
            XSLFTableCell headerCell = headerRow.addCell();
            XSLFTextParagraph ph = headerCell.addNewTextParagraph();
            ph.setTextAlign(TextParagraph.TextAlign.CENTER);
            XSLFTextRun rh = ph.addNewTextRun();
            rh.setText("Group");
            rh.setBold(true);
            rh.setFontSize(12.0);
            rh.setFontColor(tableHeaderFontColor);
            headerCell.setWordWrap(true);
            headerCell.setFillColor(tableHeaderColor);
            headerCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
            headerCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
            headerCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
            headerCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
            headerCell.setInsets(new Insets2D(15, 0, 0, 0));
        }
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getColumnHide() == null || column.getColumnHide() == 0) {

                XSLFTableCell headerCell = headerRow.addCell();
                if (column.getAlignment() != null) {
                    if (column.getAlignment().equalsIgnoreCase("right")) {
                        headerCell.setLeftInset(2);
                    } else if (column.getAlignment().equalsIgnoreCase("left")) {
                        headerCell.setRightInset(2);
                    } else if (column.getAlignment().equalsIgnoreCase("center")) {
                        headerCell.setHorizontalCentered(Boolean.TRUE);
                    }
                    //headerCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                }
                XSLFTextParagraph ph = headerCell.addNewTextParagraph();
                ph.setTextAlign(TextParagraph.TextAlign.CENTER);
                XSLFTextRun rh = ph.addNewTextRun();
                rh.setText(column.getDisplayName());
                rh.setBold(true);
                rh.setFontSize(12.0);
                rh.setFontColor(tableHeaderFontColor);
                headerCell.setWordWrap(true);
                headerCell.setFillColor(tableHeaderColor);
                headerCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
                headerCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
                headerCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
                headerCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
            }
        }
        int col = tbl.getNumberOfColumns();
        for (int i = 0; i < col; i++) {
            if (col == 10) {
                tbl.setColumnWidth(i, 70);
            } else if (col == 11) {
                tbl.setColumnWidth(i, 65);
            } else if (col == 12) {
                tbl.setColumnWidth(i, 60);
            }
        }
        if (groupFields == null || groupFields.isEmpty()) {
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                XSLFTableRow dataRow = tbl.addRow();
                for (Iterator<WidgetColumn> iterator1 = columns.iterator(); iterator1.hasNext();) {
                    WidgetColumn column = iterator1.next();
                    String value = dataMap.get(column.getFieldName()) + "";
                    if (column.getDisplayFormat() != null) {
                        value = Formatter.format(column.getDisplayFormat(), value);
                    }

                    XSLFTableCell dataCell = dataRow.addCell();
                    if (column.getAlignment() != null) {
                        if (column.getAlignment().equalsIgnoreCase("right")) {
                            dataCell.setLeftInset(2);
                        } else if (column.getAlignment().equalsIgnoreCase("left")) {
                            dataCell.setRightInset(2);
                        } else if (column.getAlignment().equalsIgnoreCase("center")) {
                            dataCell.setHorizontalCentered(Boolean.TRUE);
                        }
                        //dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                    }

                    XSLFTextParagraph pd = dataCell.addNewTextParagraph();
                    pd.setTextAlign(TextParagraph.TextAlign.LEFT);
                    XSLFTextRun rd = pd.addNewTextRun();
                    rd.setText(value);
                    rd.setBold(false);
                    rd.setFontSize(10.0);
                    rd.setFontColor(tableHeaderFontColor);
                    dataCell.setFillColor(widgetTitleColor);
                    dataCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
                    dataCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
                    dataCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
                    dataCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
                }
            }
        } else {
            generateGroupedRows(groupedData, tabWidget, tbl);
        }
        if (tabWidget.getTableFooter() != null && tabWidget.getTableFooter()) {
            XSLFTableRow footerRow = tbl.addRow();
            Boolean totalDisplayed = false;
            if (groupFields != null && groupFields.size() > 0) {
                XSLFTableCell dataCell = footerRow.addCell();
                XSLFTextParagraph pd = dataCell.addNewTextParagraph();
                pd.setTextAlign(TextParagraph.TextAlign.CENTER);
                XSLFTextRun rd = pd.addNewTextRun();
                rd.setText("Total");
                rd.setBold(true);
                rd.setFontSize(10.0);
                rd.setFontColor(tableHeaderFontColor);
                dataCell.setFillColor(tableFooterColor);
                dataCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
                dataCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
                dataCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
                dataCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
                dataCell.setInsets(new Insets2D(15, 0, 0, 0));
                dataCell.setHorizontalCentered(Boolean.TRUE);
                totalDisplayed = true;
            }
            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                if (column.getColumnHide() == null || column.getColumnHide() == 0) {
                    if (totalDisplayed == false) {
                        XSLFTableCell dataCell = footerRow.addCell();
                        XSLFTextParagraph pd = dataCell.addNewTextParagraph();
                        pd.setTextAlign(TextParagraph.TextAlign.CENTER);
                        XSLFTextRun rd = pd.addNewTextRun();
                        rd.setText("Total");
                        rd.setBold(true);
                        rd.setFontSize(10.0);
                        rd.setFontColor(tableHeaderFontColor);
                        dataCell.setFillColor(tableFooterColor);
                        dataCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
                        dataCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
                        dataCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
                        dataCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
                        dataCell.setInsets(new Insets2D(15, 0, 0, 0));
                        dataCell.setHorizontalCentered(Boolean.TRUE);
                        totalDisplayed = true;
                    } else {
                        String value = (String) groupedData.get(column.getFieldName());
                        if (column.getDisplayFormat() != null) {
                            value = Formatter.format(column.getDisplayFormat(), value);
                            System.out.println("Value: " + value);
                        }
                        XSLFTableCell dataCell = footerRow.addCell();
                        if (column.getAlignment() != null) {
                            if (column.getAlignment().equalsIgnoreCase("right")) {
                                dataCell.setLeftInset(2);
                            } else if (column.getAlignment().equalsIgnoreCase("left")) {
                                dataCell.setRightInset(2);
                            } else if (column.getAlignment().equalsIgnoreCase("center")) {
                                dataCell.setHorizontalCentered(Boolean.TRUE);
                            }
                            //dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                        }

                        XSLFTextParagraph pd = dataCell.addNewTextParagraph();
                        pd.setTextAlign(TextParagraph.TextAlign.CENTER);
                        XSLFTextRun rd = pd.addNewTextRun();
                        rd.setText(value);
                        rd.setBold(true);
                        rd.setFontSize(10.0);
                        rd.setFontColor(tableHeaderFontColor);
                        dataCell.setFillColor(tableFooterColor);
                        dataCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
                        dataCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
                        dataCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
                        dataCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
                    }
                }
            }
        }
        System.out.println("End function of generateTableForPpt");

        return tbl;
    }

    public XSLFSlide generateTextPptTable(TabWidget tabWidget, XSLFSlide slide) {
        System.out.println("Start function of generateTextPptTable");

        Color tableTitleFontColor = new Color(132, 140, 99);
        Color widgetBorderColor = new Color(204, 204, 204);
        Color widgetTitleColor = Color.WHITE;
        Color tableHeaderFontColor = new Color(61, 70, 77);
        Color tableHeaderColor = new Color(241, 241, 241);
        Color tableFooterColor = new Color(241, 241, 241);

        XSLFTextBox txt = slide.createTextBox();
        if (tabWidget.getWidgetTitle() != null) {
            txt.setText(tabWidget.getWidgetTitle());
        } else {
            txt.setText("");
        }

        txt.setTextDirection(TextShape.TextDirection.HORIZONTAL);
        txt.setAnchor(new java.awt.Rectangle(25, 30, 300, 50));

        XSLFTextParagraph tp = txt.getTextParagraphs().get(0);
        tp.setTextAlign(TextAlign.LEFT);
        XSLFTextRun run = tp.getTextRuns().get(0);

        run.setBold(true);
        run.setFontSize(13.0);
        run.setFontColor(tableTitleFontColor);

        XSLFTable tbl = slide.createTable();
        tbl.setAnchor(new java.awt.Rectangle(30, 55, 640, 480));
        XSLFTableRow dataRow = tbl.addRow();
        XSLFTableCell dataCell = dataRow.addCell();
        XSLFTextParagraph pd = dataCell.addNewTextParagraph();
        pd.setTextAlign(TextParagraph.TextAlign.LEFT);
        XSLFTextRun rd = pd.addNewTextRun();
        if (tabWidget.getContent() != null) {
            System.out.println(tabWidget.getContent());
            org.jsoup.nodes.Document doc = Jsoup.parse(tabWidget.getContent());
            rd.setText(doc.body().text());
//            XSLFTextBox text = slide.createTextBox();
//            text.setText(html);
//            slide.addShape(text);
        } else if (tabWidget.getContent() == null || tabWidget.getContent().isEmpty()) {
            rd.setText("");
        }
        rd.setBold(false);
        rd.setFontSize(12.0);
        rd.setFontColor(tableHeaderFontColor);
        dataCell.setWordWrap(true);
        dataCell.setFillColor(widgetTitleColor);
        dataCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
        dataCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
        dataCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
        dataCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
        tbl.setColumnWidth(0, 650);
        System.out.println("End function of generateTextPptTable");
        return slide;
    }

    public XSLFTable dynamicPptTable(TabWidget tabWidget, XSLFSlide slide) {
        System.out.println("Start function of dynamicPptTable");
//        BaseColor textHighlightColor = new BaseColor(242, 156, 33);
        BaseColor tableTitleFontColor = new BaseColor(132, 140, 99);

        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, Object>> originalData = tabWidget.getData();
        List<Map<String, Object>> data = new ArrayList<>(originalData);
        // System.out.println(tabWidget.getWidgetTitle() + "Actual Size ===> " + data.size());
        List<Map<String, Object>> tempData = new ArrayList<>();

        // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size****5 " + data.size());
        if (tabWidget.getZeroSuppression() != null && tabWidget.getZeroSuppression()) {
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                if (!isZeroRow(dataMap, columns)) {
                    tempData.add(dataMap);
                }
            }
            // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size****4 " + tempData.size());
            data = tempData;
        }
        // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size****3 " + data.size());

        List<SortType> sortFields = new ArrayList<>();
        List<Aggregation> aggreagtionList = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();

        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getSortOrder() != null) {
                sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
            }
            if (column.getAgregationFunction() != null) {
                aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
            }
            if (column.getGroupPriority() != null) {
                groupByFields.add(column.getFieldName());
            }
        }
        if (sortFields.size() > 0) {
            data = sortData(data, sortFields);
        }
        // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size****2 " + data.size());

        if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
            data = data.subList(0, tabWidget.getMaxRecord());
        }
        Map groupedMapData = new HashMap();
        List<String> originalGroupByFields = new ArrayList<>(groupByFields);
        if (groupByFields.size() > 0) {
            List groupedData = groupData(data, groupByFields, aggreagtionList);

            groupedMapData.putAll(aggregateData(data, aggreagtionList));
            groupedMapData.put("_groupFields", originalGroupByFields);
            groupedMapData.put("data", groupedData);
        } else {
            groupedMapData.putAll(aggregateData(data, aggreagtionList));
            groupedMapData.put("data", data);
        }
        System.out.println("End function of dynamicPptTable");
        return generateTableForPpt(groupedMapData, tabWidget, slide);
    }

    private Integer countColumns(List<WidgetColumn> columns) {
        System.out.println("Start function of countColumns");
        Integer count = 0;
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getColumnHide() == null || column.getColumnHide() == 0) {
                count++;
            }
        }
        System.out.println("End function of countColumns");

        return count;
    }

    private PdfPTable generateTable(Map groupedData, TabWidget tabWidget) {
        System.out.println("Start function of generateTable");

        //       BaseColor textHighlightColor = new BaseColor(242, 156, 33);
        BaseColor tableTitleFontColor = new BaseColor(132, 140, 99);

        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, Object>> data = tabWidget.getData();
        List<String> groupFields = (List< String>) groupedData.get("_groupFields");
        Integer noOfColumns = countColumns(columns); //.size();
        if (groupFields != null && groupFields.size() > 0) {
            noOfColumns++;
        }

        List<SortType> sortFields = new ArrayList<>();
        List<Aggregation> aggreagtionList = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();

        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getSortOrder() != null) {
                sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
            }
            if (column.getAgregationFunction() != null) {
                aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
            }
//            if (column.getGroupPriority() != null) {
//                groupByFields.add(column.getFieldName());
//            }
        }
        if (sortFields.size() > 0) {
            data = sortData(data, sortFields);
        }
        // System.out.println(tabWidget.getWidgetTitle() + " Grouped Data Size****2 " + data.size());

        if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
            data = data.subList(0, tabWidget.getMaxRecord());
        }

        PdfPTable table = new PdfPTable(noOfColumns);
        PdfPCell cell;
        pdfFontTitle.setSize(14);
        pdfFontTitle.setStyle(Font.BOLD);
        pdfFontTitle.setColor(tableTitleFontColor);
        cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
        cell.setFixedHeight(30);
        cell.setBorderColor(widgetBorderColor);
        cell.setBackgroundColor(widgetTitleColor);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(noOfColumns);
        cell.setPaddingTop(5);
        cell.setPaddingLeft(10);
        table.addCell(cell);
        table.setWidthPercentage(95f);
        if (groupFields != null && groupFields.size() > 0) {
            pdfFontHeader.setSize(13);
            pdfFontHeader.setColor(tableHeaderFontColor);
            PdfPCell dataCell = new PdfPCell(new Phrase("Group", pdfFontHeader));
            dataCell.setPadding(5);
            dataCell.setBorderColor(widgetBorderColor);
            dataCell.setBackgroundColor(tableHeaderColor);
            table.addCell(dataCell);
        }
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getColumnHide() == null || column.getColumnHide() == 0) {
                PdfPCell dataCell = new PdfPCell(new Phrase(column.getDisplayName(), pdfFontHeader));
                dataCell.setPadding(5);
                dataCell.setBorderColor(widgetBorderColor);
                dataCell.setBackgroundColor(tableHeaderColor);
                if (column.getAlignment() != null) {
                    dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                }
                table.addCell(dataCell);
            }
        }
        if (groupFields == null || groupFields.isEmpty()) {
            // System.out.println(tabWidget.getWidgetTitle() + "No Group Enabled ===> " + data.size());
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                for (Iterator<WidgetColumn> iterator1 = columns.iterator(); iterator1.hasNext();) {
                    WidgetColumn column = iterator1.next();
                    PdfPCell dataCell;
                    String value = dataMap.get(column.getFieldName()) + "";
                    if (column.getDisplayFormat() != null) {
                        value = Formatter.format(column.getDisplayFormat(), value);
                    }
                    pdfFont.setColor(tableHeaderFontColor);
                    dataCell = new PdfPCell(new Phrase(value, pdfFont));
                    if (column.getAlignment() != null) {
                        dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                    }
                    // dataCell.setBackgroundColor(BaseColor.GREEN);
                    dataCell.setBorderColor(widgetBorderColor);
                    table.addCell(dataCell);
                }
            }
        } else {
            generateGroupedRows(groupedData, tabWidget, table);
        }

        if (tabWidget.getTableFooter() != null && tabWidget.getTableFooter()) {
            Boolean totalDisplayed = false;
            if (groupFields != null && groupFields.size() > 0) {
                pdfFont.setColor(tableHeaderFontColor);
                PdfPCell dataCell = new PdfPCell(new Phrase("Total:", pdfFont));
                dataCell.setBorderColor(widgetBorderColor);
                dataCell.setBackgroundColor(tableFooterColor);
                table.addCell(dataCell);
                totalDisplayed = true;
            }
            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                if (column.getColumnHide() == null || column.getColumnHide() == 0) {
                    if (totalDisplayed == false) {
                        pdfFont.setColor(tableHeaderFontColor);
                        PdfPCell dataCell = new PdfPCell(new Phrase("Total:", pdfFont));
                        dataCell.setBorderColor(widgetBorderColor);
                        dataCell.setBackgroundColor(tableFooterColor);
                        table.addCell(dataCell);
                        totalDisplayed = true;
                    } else {
                        String value = (String) groupedData.get(column.getFieldName());
                        if (column.getDisplayFormat() != null) {
                            value = Formatter.format(column.getDisplayFormat(), value);
                            System.out.println("Value: " + value);
                        }
                        pdfFont.setColor(tableHeaderFontColor);
                        PdfPCell dataCell = new PdfPCell(new Phrase(value, pdfFont));
                        if (column.getAlignment() != null) {
                            dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                        }
                        dataCell.setBorderColor(widgetBorderColor);
                        dataCell.setBackgroundColor(tableFooterColor);
                        table.addCell(dataCell);
                    }
                }
            }
        }
        System.out.println("End function of generateTable");
        return table;
    }

    public void addReportHeader(Document document) {
        System.out.println("Start function of addReportHeader");
        try {
            // 236, 255, 224
            BaseColor backgroundColor = new BaseColor(241, 243, 246);
            BaseColor reportTitleColor = BaseColor.BLACK;
            BaseColor textHighlightColor = new BaseColor(98, 203, 49);
            BaseColor paulWalkerColor = new BaseColor(1, 67, 98);

            Integer headerCellCount = 4;
            Font f = new Font(pdfFont);
            Font pdfFontNormal = new Font(pdfFont);
            Font pdfFontNormalLight = new Font(pdfFont);
            Font pdfFontBold = new Font(pdfFont);
            Font pdfFontHighlight = new Font(pdfFont);
            Font pdfFontBoldSmall = new Font(pdfFont);
            Font pdfFontBoldLarge = new Font(pdfFont);

            pdfFontNormal.setColor(BaseColor.GRAY);
            pdfFontNormal.setSize(14);

            pdfFontNormalLight.setColor(BaseColor.LIGHT_GRAY);
            pdfFontNormalLight.setSize(14);

            pdfFontBold.setStyle(Font.BOLD);
            pdfFontBold.setColor(BaseColor.GRAY);
            pdfFontBold.setSize(14);
            pdfFontBoldSmall.setStyle(Font.BOLD);
            pdfFontBoldSmall.setColor(BaseColor.GRAY);
            pdfFontBoldSmall.setSize(12);
            pdfFontBoldLarge.setStyle(Font.BOLD);
            pdfFontBoldLarge.setColor(paulWalkerColor);
            pdfFontBoldLarge.setSize(16);

            pdfFontHighlight.setSize(14);
            pdfFontHighlight.setColor(textHighlightColor);

            f.setSize(20);
            f.setColor(reportTitleColor);
            f.setStyle(Font.BOLD);

            Paragraph reportTitle = new Paragraph("Month End Report".toUpperCase(), f);
            reportTitle.setAlignment(Paragraph.ALIGN_LEFT);
            //reportTitle.setFirstLineIndent(25);
            reportTitle.setIndentationLeft(25);

            LineSeparator dottedline = new LineSeparator();
            BaseColor dottedLineColor = new BaseColor(98, 203, 49);

            dottedline.setOffset(6);
            dottedline.setLineWidth(4);
            dottedline.setPercentage(95);
            dottedline.setLineColor(dottedLineColor);
            dottedline.setAlignment(Element.ALIGN_TOP);
//            reportTitle.add(dottedline);
            document.add(dottedline);
            document.add(reportTitle);
            document.add(new Phrase("\n"));

            PdfPTable table = new PdfPTable(headerCellCount);
            table.setWidths(new float[]{20, 20, 13, 1});
            table.setWidthPercentage(95f);

            Paragraph leftParagraph = new Paragraph("September 2016", pdfFontNormal);
            leftParagraph.add(new Phrase("\n"));
            leftParagraph.add(new Paragraph("Facebook Monthly Budget", pdfFontBold));
            leftParagraph.add(new Phrase("\n"));
            leftParagraph.add(new Paragraph("Budget ", pdfFontNormal));
            leftParagraph.add(new Paragraph("$1,500", pdfFontHighlight));
            leftParagraph.add(new Phrase("\n\n"));
            leftParagraph.add(new Paragraph("PAUL WALKER", pdfFontBoldLarge));

            Paragraph rightParagraph = new Paragraph("DIGITAL ADVISOR", pdfFontHighlight);
            rightParagraph.add(new Phrase("\n"));
            rightParagraph.add(new Paragraph("Zoe", pdfFontBoldSmall));
            rightParagraph.add(new Phrase("\n"));
            rightParagraph.add(new Paragraph("EMAIL: ", pdfFontNormalLight));
            rightParagraph.add(new Paragraph("info@deetaanalytics.com", pdfFontBoldSmall));
            rightParagraph.add(new Phrase("\n"));
            rightParagraph.add(new Paragraph("PHONE: ", pdfFontNormalLight));
            rightParagraph.add(new Paragraph("773-446-7565", pdfFontBoldSmall));

            PdfPCell bottomCell = new PdfPCell(new Phrase("\n\n"));
            bottomCell.setBackgroundColor(backgroundColor);
            bottomCell.setColspan(headerCellCount);
            PdfPCell topCell = new PdfPCell(new Phrase(""));
            topCell.setBackgroundColor(backgroundColor);
            topCell.setColspan(headerCellCount);
            PdfPCell leftCell = new PdfPCell(leftParagraph);
            PdfPCell middleCell = new PdfPCell();
            PdfPCell rightCornorCell = new PdfPCell(new Phrase("\n"));
            rightCornorCell.setBackgroundColor(backgroundColor);

            PdfPCell rightCell = new PdfPCell(rightParagraph);
            rightCell.setBackgroundColor(BaseColor.WHITE);

            topCell.setBorder(PdfPCell.NO_BORDER);
            bottomCell.setBorder(PdfPCell.NO_BORDER);
            leftCell.setBorder(PdfPCell.NO_BORDER);
            leftCell.setBackgroundColor(backgroundColor);
            middleCell.setBorder(PdfPCell.NO_BORDER);
            middleCell.setBackgroundColor(backgroundColor);

            rightCornorCell.setBorder(PdfPCell.NO_BORDER);
            rightCell.setBorder(PdfPCell.NO_BORDER);

            topCell.setBorderColorBottom(backgroundColor);
            topCell.setBorderColorTop(backgroundColor);
            topCell.setBorderColorLeft(backgroundColor);
            topCell.setBorderColorRight(backgroundColor);

            leftCell.setBorderColorBottom(backgroundColor);
            leftCell.setBorderColorTop(backgroundColor);
            leftCell.setBorderColorLeft(backgroundColor);
            leftCell.setBorderColorRight(backgroundColor);

            middleCell.setBorderColorBottom(backgroundColor);
            middleCell.setBorderColorTop(backgroundColor);
            middleCell.setBorderColorLeft(backgroundColor);
            middleCell.setBorderColorRight(backgroundColor);

            rightCell.setBorderColorBottom(backgroundColor);
            rightCell.setBorderColorTop(backgroundColor);
            rightCell.setBorderColorLeft(backgroundColor);
            rightCell.setBorderColorRight(backgroundColor);

            rightCornorCell.setBorderColorBottom(backgroundColor);
            rightCornorCell.setBorderColorTop(backgroundColor);
            rightCornorCell.setBorderColorLeft(backgroundColor);
            rightCornorCell.setBorderColorRight(backgroundColor);

            bottomCell.setBorderColorBottom(backgroundColor);
            bottomCell.setBorderColorTop(backgroundColor);
            bottomCell.setBorderColorLeft(backgroundColor);
            bottomCell.setBorderColorRight(backgroundColor);

            leftCell.setPaddingLeft(10);
            rightCell.setPadding(10);
            topCell.setBorderWidthTop(5);
            table.addCell(topCell);
            table.addCell(leftCell);
            table.addCell(middleCell);
            table.addCell(rightCell);
            table.addCell(rightCornorCell);
            table.addCell(bottomCell);

            document.add(table);
        } catch (DocumentException ex) {
            log.error("Document Exception in addReportHeader function:  " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("End function of addReportHeader");

    }

    public void dynamicPptTable(List<TabWidget> tabWidgets, OutputStream out) throws IOException {
        System.out.println("Start function of dynamicPptTable");
        //creating a presentation
        XMLSlideShow ppt = new XMLSlideShow();

        //Color
        Color tableTitleFontColor = new Color(132, 140, 99);
        Color widgetBorderColor = new Color(204, 204, 204);
        Color widgetTitleColor = Color.WHITE;
        Color tableHeaderFontColor = new Color(61, 70, 77);
        Color tableHeaderColor = new Color(241, 241, 241);
        Color tableFooterColor = new Color(241, 241, 241);

        try {
            //creating a slide in it
            for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
                TabWidget tabWidget = iterator.next();
                if (tabWidget.getChartType().equalsIgnoreCase("table")) {
                    System.out.println("Table");
                    XSLFSlide slide = ppt.createSlide();
                    XSLFTable table = dynamicPptTable(tabWidget, slide);
                } else if (tabWidget.getChartType().equalsIgnoreCase("text")) {
                    XSLFSlide slide = ppt.createSlide();
                    slide = generateTextPptTable(tabWidget, slide);
                } else if (tabWidget.getChartType().equalsIgnoreCase("pie")) {
                    System.out.println("Pie");
                    JFreeChart pieChart = generatePieJFreeChart(tabWidget);
                    if (pieChart == null) {
                        //creating a slide with title and content layout
                        XSLFSlide slide = ppt.createSlide();

                        //selection of title place holder
                        XSLFTextBox txt = slide.createTextBox();
                        txt.setText(tabWidget.getWidgetTitle());
                        txt.setTextDirection(TextShape.TextDirection.HORIZONTAL);
                        txt.setAnchor(new java.awt.Rectangle(30, 30, 300, 50));

                        XSLFTextParagraph tp = txt.getTextParagraphs().get(0);
                        tp.setTextAlign(TextAlign.LEFT);
                        XSLFTextRun run = tp.getTextRuns().get(0);

                        run.setBold(true);
                        run.setFontSize(13.0);
                        run.setFontColor(tableTitleFontColor);

                    } else {
                        float quality = 1;

                        //creating a slide with title and content layout
                        XSLFSlide slide = ppt.createSlide();

                        //selection of title place holder
                        XSLFTextBox txt = slide.createTextBox();
                        if (tabWidget.getWidgetTitle() != null) {
                            txt.setText(tabWidget.getWidgetTitle());
                        } else {
                            txt.setText("");
                        }

                        txt.setTextDirection(TextShape.TextDirection.HORIZONTAL);
                        txt.setAnchor(new java.awt.Rectangle(30, 30, 300, 50));

                        XSLFTextParagraph tp = txt.getTextParagraphs().get(0);
                        tp.setTextAlign(TextAlign.LEFT);
                        XSLFTextRun run = tp.getTextRuns().get(0);

                        run.setBold(true);
                        run.setFontSize(13.0);
                        run.setFontColor(tableTitleFontColor);

                        ByteArrayOutputStream chart_out = new ByteArrayOutputStream();
                        ChartUtilities.writeChartAsJPEG(chart_out, quality, pieChart, 640, 480);
                        XSLFPictureData idx = ppt.addPicture(chart_out.toByteArray(), org.apache.poi.sl.usermodel.PictureData.PictureType.JPEG);
                        //creating a slide with given picture on it
                        XSLFPictureShape pic = slide.createPicture(idx);
                        pic.setAnchor(new java.awt.Rectangle(30, 55, 640, 480));
                        chart_out.close();
                    }
                } else if (tabWidget.getChartType().equalsIgnoreCase("bar")) {

                    JFreeChart barChart = multiAxisBarJFreeChart(tabWidget);
                    float quality = 1;

                    //creating a slide with title and content layout
                    XSLFSlide slide = ppt.createSlide();

                    //selection of title place holder
                    XSLFTextBox txt = slide.createTextBox();
                    if (tabWidget.getWidgetTitle() != null) {
                        txt.setText(tabWidget.getWidgetTitle());
                    } else {
                        txt.setText("");
                    }

                    txt.setTextDirection(TextShape.TextDirection.HORIZONTAL);
                    txt.setAnchor(new java.awt.Rectangle(30, 30, 300, 50));

                    XSLFTextParagraph tp = txt.getTextParagraphs().get(0);
                    tp.setTextAlign(TextAlign.LEFT);
                    XSLFTextRun run = tp.getTextRuns().get(0);

                    run.setBold(true);
                    run.setFontSize(13.0);
                    run.setFontColor(tableTitleFontColor);

                    ByteArrayOutputStream chart_out = new ByteArrayOutputStream();
                    ChartUtilities.writeChartAsJPEG(chart_out, quality, barChart, 640, 480);

                    XSLFPictureData idx = ppt.addPicture(chart_out.toByteArray(), org.apache.poi.sl.usermodel.PictureData.PictureType.JPEG);

                    //creating a slide with given picture on it
                    XSLFPictureShape pic = slide.createPicture(idx);
                    pic.setAnchor(new java.awt.Rectangle(30, 55, 640, 480));
                    chart_out.close();

                } else if (tabWidget.getChartType().equalsIgnoreCase("area")) {

                    JFreeChart areaChart = multiAxisAreaJFreeChart(tabWidget);
                    float quality = 1;

                    //creating a slide with title and content layout
                    XSLFSlide slide = ppt.createSlide();

                    //selection of title place holder
                    XSLFTextBox txt = slide.createTextBox();
                    if (tabWidget.getWidgetTitle() != null) {
                        txt.setText(tabWidget.getWidgetTitle());
                    } else {
                        txt.setText("");
                    }

                    txt.setTextDirection(TextShape.TextDirection.HORIZONTAL);
                    txt.setAnchor(new java.awt.Rectangle(30, 30, 300, 50));

                    XSLFTextParagraph tp = txt.getTextParagraphs().get(0);
                    tp.setTextAlign(TextAlign.LEFT);
                    XSLFTextRun run = tp.getTextRuns().get(0);

                    run.setBold(true);
                    run.setFontSize(13.0);
                    run.setFontColor(tableTitleFontColor);

                    ByteArrayOutputStream chart_out = new ByteArrayOutputStream();
                    ChartUtilities.writeChartAsJPEG(chart_out, quality, areaChart, 640, 480);

                    XSLFPictureData idx = ppt.addPicture(chart_out.toByteArray(), org.apache.poi.sl.usermodel.PictureData.PictureType.JPEG);

                    //creating a slide with given picture on it
                    XSLFPictureShape pic = slide.createPicture(idx);
                    pic.setAnchor(new java.awt.Rectangle(30, 55, 640, 480));
                    chart_out.close();

                } else if (tabWidget.getChartType().equalsIgnoreCase("line")) {
                    JFreeChart lineChart = multiAxisLineJFreeChart(tabWidget);
                    float quality = 1;

                    //creating a slide with title and content layout
                    XSLFSlide slide = ppt.createSlide();

                    //selection of title place holder
                    XSLFTextBox txt = slide.createTextBox();
                    if (tabWidget.getWidgetTitle() != null) {
                        txt.setText(tabWidget.getWidgetTitle());
                    } else {
                        txt.setText("");
                    }

                    txt.setTextDirection(TextShape.TextDirection.HORIZONTAL);
                    txt.setAnchor(new java.awt.Rectangle(30, 30, 300, 50));

                    XSLFTextParagraph tp = txt.getTextParagraphs().get(0);
                    tp.setTextAlign(TextAlign.LEFT);
                    XSLFTextRun run = tp.getTextRuns().get(0);
                    run.setBold(true);
                    run.setFontSize(13.0);
                    run.setFontColor(tableTitleFontColor);

                    ByteArrayOutputStream chart_out = new ByteArrayOutputStream();
                    ChartUtilities.writeChartAsJPEG(chart_out, quality, lineChart, 640, 480);
                    org.apache.poi.sl.usermodel.PictureData.PictureType test;
                    XSLFPictureData idx = ppt.addPicture(chart_out.toByteArray(), org.apache.poi.sl.usermodel.PictureData.PictureType.JPEG);
                    //creating a slide with given picture on it
                    XSLFPictureShape pic = slide.createPicture(idx);
                    pic.setAnchor(new java.awt.Rectangle(30, 55, 640, 480));
                    chart_out.close();
                }
            }
            // out = new FileOutputStream("/home/deldot/Pictures/ppt/ppttable.pptx");
            ppt.write(out);
        } catch (FileNotFoundException ex) {
            log.error("FileNotFoundException in dynamicPptTable function: " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            log.error("IOException in dynamicPptTable function: " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadElementException ex) {
            log.error("BadElementException in dynamicPptTable function: " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
            ppt.close();
            System.out.println("End function of dynamicPptTable");

        }
    }

//    public XSSFTable dynamicXlsTable(TabWiget tabWiget, ){
//        return "";
//    }
    public void dynamicXlsDownload(List<TabWidget> tabWidgets, OutputStream out) {
        System.out.println("Start function of dynamicXlsDownload");
        try {
            JFreeChart pieChart = null;
            JFreeChart lineChart = null;
            JFreeChart barChart = null;

            String sheetName = "Sheet1";//name of sheet

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet(sheetName);

            /* Specify the height and width of the Pie Chart */
            int width = 640;
            /* Width of the chart */

            int height = 480;
            /* Height of the chart */

            float quality = 1;

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            int addRow = 0;
            int count = 0;
            for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
                TabWidget tabWidget = iterator.next();
                if (tabWidget.getChartType().equalsIgnoreCase("table")) {

                } else if (tabWidget.getChartType().equalsIgnoreCase("pie")) {
                    if (count == 0) {
                        addRow = 0;
                        count++;
                    } else {
                        addRow = 30 + addRow;
                    }
                    pieChart = generatePieJFreeChart(tabWidget);
                    ByteArrayOutputStream chart_out = new ByteArrayOutputStream();
                    ChartUtilities.writeChartAsJPEG(chart_out, quality, pieChart, width, height);
                    int my_picture_id = wb.addPicture(chart_out.toByteArray(), Workbook.PICTURE_TYPE_JPEG);
                    chart_out.close();
                    ClientAnchor my_anchor = new XSSFClientAnchor();
                    my_anchor.setCol1(4);
                    my_anchor.setRow1(7 + addRow);
                    XSSFPicture my_picture = drawing.createPicture(my_anchor, my_picture_id);
                    my_picture.resize();
                } else if (tabWidget.getChartType().equalsIgnoreCase("bar")) {
                    if (count == 0) {
                        addRow = 0;
                        count++;
                    } else {
                        addRow = 30 + addRow;
                    }
                    barChart = multiAxisBarJFreeChart(tabWidget);
                    ByteArrayOutputStream chart_out1 = new ByteArrayOutputStream();
                    ChartUtilities.writeChartAsJPEG(chart_out1, quality, barChart, width, height);
                    int my_picture_id1 = wb.addPicture(chart_out1.toByteArray(), Workbook.PICTURE_TYPE_JPEG);
                    chart_out1.close();
                    ClientAnchor my_anchor1 = new XSSFClientAnchor();
                    my_anchor1.setCol1(4);
                    my_anchor1.setRow1(7 + addRow);
                    XSSFPicture my_picture1 = drawing.createPicture(my_anchor1, my_picture_id1);
                    my_picture1.resize();

                } else if (tabWidget.getChartType().equalsIgnoreCase("line")) {
                    if (count == 0) {
                        addRow = 0;
                        count++;
                    } else {
                        addRow = 30 + addRow;
                    }
                    lineChart = multiAxisLineJFreeChart(tabWidget);
                    ByteArrayOutputStream chart_out2 = new ByteArrayOutputStream();
                    ChartUtilities.writeChartAsJPEG(chart_out2, quality, lineChart, width, height);
                    int my_picture_id2 = wb.addPicture(chart_out2.toByteArray(), Workbook.PICTURE_TYPE_JPEG);
                    chart_out2.close();
                    ClientAnchor my_anchor2 = new XSSFClientAnchor();
                    my_anchor2.setCol1(4);
                    my_anchor2.setRow1(7 + addRow);
                    XSSFPicture my_picture2 = drawing.createPicture(my_anchor2, my_picture_id2);
                    my_picture2.resize();
                }
            }
            wb.write(out);
            out.flush();
            out.close();
        } catch (IOException ex) {
            log.error("IOException in dynamicXlsDownload Function: " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadElementException ex) {
            log.error("BadElementException in dynamicXlsDownload Function: " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("End function of dynamicXlsDownload");
    }

    public JFreeChart getSampleJFreeChart() {
        System.out.println("Start function of getSampleJFreeChart");

        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        line_chart_dataset.addValue(15, "schools", "1970");
        line_chart_dataset.addValue(30, "schools", "1980");
        line_chart_dataset.addValue(60, "schools", "1990");
        line_chart_dataset.addValue(120, "schools", "2000");
        line_chart_dataset.addValue(240, "schools", "2010");
        line_chart_dataset.addValue(300, "schools", "2014");

        JFreeChart lineChartObject = ChartFactory.createLineChart(
                "Schools Vs Years", "Year",
                "Schools Count",
                line_chart_dataset, PlotOrientation.VERTICAL,
                true, true, false);
        System.out.println("End function of getSampleJFreeChart");
        return lineChartObject;
    }

    public void dynamicPdfTable(List<TabWidget> tabWidgets, OutputStream out) {
        System.out.println("Start function of dynamicPdfTable");
        try {
            PdfWriter writer = null;
            Document document = new Document(pageSize, 36, 36, 72, 72);
            BaseColor tableTitleFontColor = new BaseColor(132, 140, 99);

            writer = PdfWriter.getInstance(document, out);
            document.open();
            HeaderFooterTable event = new HeaderFooterTable();
            writer.setPageEvent(event);
            PageNumeration pevent = new PageNumeration();
            writer.setPageEvent(pevent);

            addReportHeader(document);

            for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
                TabWidget tabWidget = iterator.next();
                if (tabWidget.getChartType().equalsIgnoreCase("table")) {
                    PdfPTable pdfTable = dynamicPdfTable(tabWidget);
                    document.add(new Phrase("\n"));
                    document.add(pdfTable);
                } else if (tabWidget.getChartType().equalsIgnoreCase("text")) {
                    PdfPTable pdfTable = generateTextPdfTable(tabWidget);
                    document.add(new Phrase("\n"));
                    document.add(pdfTable);
                } else if (tabWidget.getChartType().equalsIgnoreCase("pie")) {

                    PdfPTable table = new PdfPTable(1);
                    PdfPCell cell;
                    table.setWidthPercentage(95f);
                    pdfFontTitle.setSize(14);
                    pdfFontTitle.setStyle(Font.BOLD);
                    pdfFontTitle.setColor(tableTitleFontColor);
                    cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
                    cell.setFixedHeight(30);
                    cell.setBorderColor(widgetBorderColor);
                    cell.setBackgroundColor(widgetTitleColor);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setColspan(1);
                    cell.setPaddingTop(5);
                    cell.setPaddingLeft(10);
                    table.addCell(cell);
                    Image pieChart = generatePieChart(writer, tabWidget);

                    if (pieChart != null) {
                        PdfPCell chartCell = new PdfPCell(pieChart);
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    } else {
                        PdfPCell chartCell = new PdfPCell();
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    }
                } else if (tabWidget.getChartType().equalsIgnoreCase("bar")) {
                    //document.add(multiAxisBarChart(writer, tabWidget));
                    PdfPTable table = new PdfPTable(1);
                    PdfPCell cell;
                    table.setWidthPercentage(95f);
                    pdfFontTitle.setSize(14);
                    pdfFontTitle.setStyle(Font.BOLD);
                    pdfFontTitle.setColor(tableTitleFontColor);
                    cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
                    cell.setFixedHeight(30);
                    cell.setBorderColor(widgetBorderColor);
                    cell.setBackgroundColor(widgetTitleColor);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setColspan(1);
                    cell.setPaddingTop(5);
                    cell.setPaddingLeft(10);
                    table.addCell(cell);
                    System.out.println("Bar Writer: " + writer);
                    System.out.println("Bar Tab Widget DataSet: " + tabWidget.getDataset());
                    System.out.println("Bar Tab Widget DataSource: " + tabWidget.getDatasource());

                    Image barChart = multiAxisBarChart(writer, tabWidget);
                    if (barChart != null) {
                        PdfPCell chartCell = new PdfPCell(barChart);
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    } else {
                        PdfPCell chartCell = new PdfPCell();
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    }

                } else if (tabWidget.getChartType().equalsIgnoreCase("line")) {
                    PdfPTable table = new PdfPTable(1);
                    PdfPCell cell;
                    table.setWidthPercentage(95f);
                    pdfFontTitle.setSize(14);
                    pdfFontTitle.setStyle(Font.BOLD);
                    cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
                    cell.setFixedHeight(30);
                    cell.setBorderColor(widgetBorderColor);
                    cell.setBackgroundColor(widgetTitleColor);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setColspan(1);
                    cell.setPaddingTop(5);
                    cell.setPaddingLeft(10);
                    table.addCell(cell);
                    System.out.println("line Writer: " + writer);
                    System.out.println("line Tab Widget DataSet: " + tabWidget.getDataset());
                    System.out.println("line Tab Widget DataSource: " + tabWidget.getDatasource());
                    Image lineChart = multiAxisLineChart(writer, tabWidget);
                    if (lineChart != null) {
                        PdfPCell chartCell = new PdfPCell(lineChart);
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(5);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    } else {
                        PdfPCell chartCell = new PdfPCell();
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    }
                } else if (tabWidget.getChartType().equalsIgnoreCase("area")) {
                    //document.add(multiAxisBarChart(writer, tabWidget));
                    PdfPTable table = new PdfPTable(1);
                    PdfPCell cell;
                    table.setWidthPercentage(95f);
                    pdfFontTitle.setSize(14);
                    pdfFontTitle.setStyle(Font.BOLD);
                    pdfFontTitle.setColor(tableTitleFontColor);
                    cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
                    cell.setFixedHeight(30);
                    cell.setBorderColor(widgetBorderColor);
                    cell.setBackgroundColor(widgetTitleColor);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setColspan(1);
                    cell.setPaddingTop(5);
                    cell.setPaddingLeft(10);
                    table.addCell(cell);
                    System.out.println("Area Writer: " + writer);
                    System.out.println("Area Tab Widget DataSet: " + tabWidget.getDataset());
                    System.out.println("Area Tab Widget DataSource: " + tabWidget.getDatasource());

                    Image areaChart = multiAxisAreaChart(writer, tabWidget);
                    if (areaChart != null) {
                        PdfPCell chartCell = new PdfPCell(areaChart);
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    } else {
                        PdfPCell chartCell = new PdfPCell();
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    }

                }
                // System.out.println("Chart Type ===> " + tabWidget.getChartType());
            }
            document.close();
            out.flush();
            out.close();
        } catch (DocumentException ex) {
            log.error("DocumentException in dynamicPdfTable Function: " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            log.error("IOException in dynamicPdfTable Function: " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("End function of dynamicPdfTable");
    }

    public static Image generateLineChart(PdfWriter writer, TabWidget tabWidget) throws BadElementException {
        System.out.println("Start function of generateLineChart");
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        JFreeChart chart = ChartFactory.createBarChart(
                "Number of times user visit", "Count", "Number Of Visits",
                dataSet, PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        final CategoryItemRenderer renderer = new CustomRenderer(
                new Paint[]{new Color(116, 196, 198), new Color(116, 196, 198),
                    new Color(116, 196, 198), new Color(116, 196, 198),
                    new Color(116, 196, 198)
                });
        renderer.setItemLabelsVisible(true);

        plot.setRenderer(renderer);

        plot.setDrawingSupplier(new ChartDrawingSupplier());

        PdfContentByte contentByte = writer.getDirectContent();

        PdfTemplate templatePie = contentByte.createTemplate(widgetWidth, widgetHeight);
        Graphics2D graphics2dPie = templatePie.createGraphics(widgetWidth, widgetHeight,
                new DefaultFontMapper());
        Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, widgetWidth,
                widgetHeight);

        chart.draw(graphics2dPie, rectangle2dPie);

        graphics2dPie.dispose();

        // contentByte.addTemplate(templatePie, 30, 30);
        Image img = Image.getInstance(templatePie);
        System.out.println("End function of generateLineChart");

        return img;
    }

    public Image multiAxisLineChart(PdfWriter writer, TabWidget tabWidget) {
        System.out.println("Start function of multiAxisLineChart");

        try {

            List<WidgetColumn> columns = tabWidget.getColumns();

            List<Map<String, Object>> originalData = tabWidget.getData();

            List<Map<String, Object>> tempData = tabWidget.getData();
            if (originalData == null || originalData.isEmpty()) {
                return null;
            }
            List<Map<String, Object>> data = new ArrayList<>(originalData);

            List<SortType> sortFields = new ArrayList<>();
            List<Aggregation> aggreagtionList = new ArrayList<>();
            List<FirstAxis> firstAxis = new ArrayList<>();
            List<SecondAxis> secondAxis = new ArrayList<>();
            String xAxis = null;
            String xAxisDisplay = null;

            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                if (column.getSortOrder() != null) {
                    sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
                }
                if (column.getAgregationFunction() != null) {
                    aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) == 1) {
                    firstAxis.add(new FirstAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) > 1) {
                    secondAxis.add(new SecondAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getxAxis() != null) {
                    xAxis = column.getFieldName();
                    xAxisDisplay = column.getDisplayName();
                    System.out.println("XAxisDisplay: " + xAxisDisplay);
                }
            }

            if (sortFields.size() > 0) {
                data = sortData(data, sortFields);
            }
            if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
                data = data.subList(0, tabWidget.getMaxRecord());
            }

//            final CategoryDataset dataset1 = createDataset3();
//            final CategoryDataset dataset2 = createDataset4();
            Stream<FirstAxis> firstAxiss = firstAxis.stream().distinct();
            long firstAxisCount = firstAxiss.count();

            Stream<SecondAxis> secondAxiss = secondAxis.stream().distinct();
            long secondAxisCount = secondAxiss.count();

            long totalCount = firstAxisCount + secondAxisCount;
            final CategoryDataset dataset1 = createDataset1(data, firstAxis, secondAxis, xAxis);
            final CategoryDataset dataset2 = createDataset2(data, secondAxis, firstAxis, xAxis);
            final CategoryAxis domainAxis = new CategoryAxis(xAxisDisplay);
            // final NumberAxis rangeAxis = new NumberAxis("Value");

            System.out.println("Dataset1 line data: " + data);
            System.out.println("Dataset1 line first Axis: " + firstAxis);
            System.out.println("Dataset1 line Second Axis: " + secondAxis);
            System.out.println("Dataset1 line X Axis: " + xAxis);

            final NumberAxis rangeAxis = new NumberAxis();
            final LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
            final CategoryPlot plot = new CategoryPlot(dataset1, domainAxis, rangeAxis, renderer1) {

                /**
                 * Override the getLegendItems() method to handle special case.
                 *
                 * @return the legend items.
                 */
                public LegendItemCollection getLegendItems() {

                    final LegendItemCollection result = new LegendItemCollection();

                    if (firstAxis.isEmpty()) {
                    } else {
                        final CategoryDataset data = getDataset();
                        if (data != null) {
                            final CategoryItemRenderer r = getRenderer();
                            r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            r.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_LEFT);
                            r.setBasePositiveItemLabelPosition(position);
                            if (r != null) {
                                for (long i = 0; i < firstAxisCount; i++) {
                                    final LegendItem item = r.getLegendItem(0, (int) i);
                                    result.add(item);
                                }
                            }
                        }
                    }
                    // the JDK 1.2.2 compiler complained about the name of this
                    // variable 
                    if (secondAxis.isEmpty()) {
                    } else {
                        final CategoryDataset dset2 = getDataset(1);
                        System.out.println(dset2);
                        if (dset2 != null) {
                            final CategoryItemRenderer renderer2 = getRenderer(1);
                            renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            renderer2.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_RIGHT);
                            renderer2.setBasePositiveItemLabelPosition(position);
                            if (renderer2 != null) {
                                for (long i = firstAxisCount; i < totalCount; i++) {
                                    final LegendItem item = renderer2.getLegendItem(0, (int) i);
                                    result.add(item);
                                }
                            }
                        }
                    }
                    return result;
                }
            };

            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinesVisible(true);
            plot.setOrientation(PlotOrientation.VERTICAL);
            //final JFreeChart chart = new JFreeChart(tabWidget.getWidgetTitle(), plot);
            final JFreeChart chart = new JFreeChart(plot);
            CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
            axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            chart.setBackgroundPaint(Color.white);
            plot.setBackgroundPaint(Color.white);
            plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
            plot.setDataset(1, dataset2);
            plot.mapDatasetToRangeAxis(1, 1);
            //final ValueAxis axis2 = new NumberAxis("Secondary");
            final ValueAxis axis2 = new NumberAxis();
            plot.setRangeAxis(1, axis2);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
            Paint[] paint = new Paint[]{
                new Color(98, 203, 49),
                new Color(85, 85, 85),
                new Color(161, 225, 132),
                new Color(102, 102, 102)
            };
            int j = 0;
            for (long i = 0; i < totalCount; i++) {
                if (i == 4) {
                    j = 0;
                }
                renderer2.setSeriesPaint((int) i, paint[j++]);
                plot.setRenderer((int) i, renderer2);
            }
            plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
            // OPTIONAL CUSTOMISATION COMPLETED.

            plot.setDrawingSupplier(new ChartDrawingSupplier());

            PdfContentByte contentByte = writer.getDirectContent();

            PdfTemplate templatePie = contentByte.createTemplate(widgetWidth, widgetHeight);
            Graphics2D graphics2dPie = templatePie.createGraphics(widgetWidth, widgetHeight,
                    new DefaultFontMapper());
            Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, widgetWidth,
                    widgetHeight);

            chart.draw(graphics2dPie, rectangle2dPie);

            graphics2dPie.dispose();

            // contentByte.addTemplate(templatePie, 30, 30);
            Image img = Image.getInstance(templatePie);
            return img;
        } catch (BadElementException ex) {
            log.error("BadElementException in multiAxisLineChart Function: " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("End function of multiAxisLineChart");
        return null;
    }

    public JFreeChart multiAxisLineJFreeChart(TabWidget tabWidget) {
        System.out.println("Start function of multiAxisLineJFreeChart");
        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, Object>> originalData = tabWidget.getData();
        List<Map<String, Object>> tempData = tabWidget.getData();
        if (originalData == null || originalData.isEmpty()) {
            return null;
        }
        List<Map<String, Object>> data = new ArrayList<>(originalData);
        List<SortType> sortFields = new ArrayList<>();
        List<Aggregation> aggreagtionList = new ArrayList<>();
        List<FirstAxis> firstAxis = new ArrayList<>();
        List<SecondAxis> secondAxis = new ArrayList<>();
        String xAxis = null;
        String xAxisDisplay = null;
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getSortOrder() != null) {
                sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
            }
            if (column.getAgregationFunction() != null) {
                aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
            }
            if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) == 1) {
                firstAxis.add(new FirstAxis(column.getFieldName(), column.getDisplayName()));
            }
            if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) > 1) {
                secondAxis.add(new SecondAxis(column.getFieldName(), column.getDisplayName()));
            }
            if (column.getxAxis() != null) {
                xAxis = column.getFieldName();
                xAxisDisplay = column.getDisplayName();
            }
        }
        if (sortFields.size() > 0) {
            data = sortData(data, sortFields);
        }
        if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
            data = data.subList(0, tabWidget.getMaxRecord());
        }
        Stream<FirstAxis> firstAxiss = firstAxis.stream().distinct();
        long firstAxisCount = firstAxiss.count();

        Stream<SecondAxis> secondAxiss = secondAxis.stream().distinct();
        long secondAxisCount = secondAxiss.count();

        long totalCount = firstAxisCount + secondAxisCount;
        final CategoryDataset dataset1 = createDataset1(data, firstAxis, secondAxis, xAxis);
        final CategoryDataset dataset2 = createDataset2(data, secondAxis, firstAxis, xAxis);
        final CategoryAxis domainAxis = new CategoryAxis(xAxisDisplay);
        System.out.println("Dataset1 line data: " + data);
        System.out.println("Dataset1 line first Axis: " + firstAxis);
        System.out.println("Dataset1 line Second Axis: " + secondAxis);
        System.out.println("Dataset1 line X Axis: " + xAxis);
        final NumberAxis rangeAxis = new NumberAxis();
        final LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
        final CategoryPlot plot = new CategoryPlot(dataset1, domainAxis, rangeAxis, renderer1) {

            /**
             * Override the getLegendItems() method to handle special case.
             *
             * @return the legend items.
             */
            public LegendItemCollection getLegendItems() {

                final LegendItemCollection result = new LegendItemCollection();
                if (firstAxis.isEmpty()) {
                } else {
                    final CategoryDataset data = getDataset();
                    if (data != null) {
                        final CategoryItemRenderer r = getRenderer();
                        r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                        r.setBaseItemLabelsVisible(true);
                        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                TextAnchor.BASELINE_LEFT);
                        r.setBasePositiveItemLabelPosition(position);
                        if (r != null) {
                            for (long i = 0; i < firstAxisCount; i++) {
                                final LegendItem item = r.getLegendItem(0, (int) i);
                                result.add(item);
                            }
                        }
                    }
                }

                // the JDK 1.2.2 compiler complained about the name of this
                // variable
                if (secondAxis.isEmpty()) {
                } else {
                    final CategoryDataset dset2 = getDataset(1);
                    if (dset2 != null) {
                        final CategoryItemRenderer renderer2 = getRenderer(1);
                        renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                        renderer2.setBaseItemLabelsVisible(true);
                        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                TextAnchor.BASELINE_RIGHT);
                        renderer2.setBasePositiveItemLabelPosition(position);
                        if (renderer2 != null) {
                            for (long i = firstAxisCount; i < totalCount; i++) {
                                final LegendItem item = renderer2.getLegendItem(0, (int) i);
                                result.add(item);
                            }
                        }
                    }
                }
                return result;
            }
        };
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        plot.setOrientation(PlotOrientation.VERTICAL);
        final JFreeChart chart = new JFreeChart(plot);
        CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        chart.setBackgroundPaint(Color.white);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        final ValueAxis axis2 = new NumberAxis();
        plot.setRangeAxis(1, axis2);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        Paint[] paint = new Paint[]{
            new Color(98, 203, 49),
            new Color(85, 85, 85),
            new Color(161, 225, 132),
            new Color(102, 102, 102)
        };
        int j = 0;
        for (long i = 0; i < totalCount; i++) {
            if (i == 4) {
                j = 0;
            }
            renderer2.setSeriesPaint((int) i, paint[j++]);
            plot.setRenderer((int) i, renderer2);
        }
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
        plot.setDrawingSupplier(new ChartDrawingSupplier());
        System.out.println("End function of multiAxisLineJFreeChart");
        return chart;
    }

    public Image multiAxisAreaChart(PdfWriter writer, TabWidget tabWidget) {
        System.out.println("Start function of multiAxisAreaChart");
        try {
            List<WidgetColumn> columns = tabWidget.getColumns();

            List<Map<String, Object>> originalData = tabWidget.getData();
            List<Map<String, Object>> data = new ArrayList<>(originalData);
            List<SortType> sortFields = new ArrayList<>();
            List<Aggregation> aggreagtionList = new ArrayList<>();
            List<FirstAxis> firstAxis = new ArrayList<>();
            List<SecondAxis> secondAxis = new ArrayList<>();
            String xAxis = null;
            String xAxisDisplay = null;

            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                if (column.getSortOrder() != null) {
                    sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
                }
                if (column.getAgregationFunction() != null) {
                    aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) == 1) {
                    firstAxis.add(new FirstAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) > 1) {
                    secondAxis.add(new SecondAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getxAxis() != null) {
                    xAxis = column.getFieldName();
                    xAxisDisplay = column.getDisplayName();
                }
            }

            if (sortFields.size() > 0) {
                data = sortData(data, sortFields);
            }
            if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
                data = data.subList(0, tabWidget.getMaxRecord());
            }

            final CategoryDataset dataset1 = createDataset1(data, firstAxis, secondAxis, xAxis);
            final CategoryDataset dataset2 = createDataset2(data, secondAxis, firstAxis, xAxis);
            final CategoryAxis domainAxis = new CategoryAxis(xAxisDisplay);
            final NumberAxis rangeAxis = new NumberAxis();
            final AreaRenderer renderer1 = new AreaRenderer();

            Stream<FirstAxis> firstAxiss = firstAxis.stream().distinct();
            long firstAxisCount = firstAxiss.count();

            Stream<SecondAxis> secondAxiss = secondAxis.stream().distinct();
            long secondAxisCount = secondAxiss.count();

            long totalCount = firstAxisCount + secondAxisCount;

            final CategoryPlot plot = new CategoryPlot(dataset1, domainAxis, rangeAxis, renderer1) {

                /**
                 * Override the getLegendItems() method to handle special case.
                 *
                 * @return the legend items.
                 */
                public LegendItemCollection getLegendItems() {

                    final LegendItemCollection result = new LegendItemCollection();

                    if (firstAxis.isEmpty()) {
                    } else {
                        final CategoryDataset data = getDataset();
                        if (data != null) {
                            final CategoryItemRenderer r = getRenderer();
                            r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            r.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_LEFT);
                            r.setBasePositiveItemLabelPosition(position);

                            if (r != null) {
                                for (long i = 0; i < firstAxisCount; i++) {
                                    final LegendItem item = r.getLegendItem(0, (int) i);
                                    result.add(item);
                                }
                            }
                        }
                    }
                    // the JDK 1.2.2 compiler complained about the name of this
                    // variable 
                    if (secondAxis.isEmpty()) {
                    } else {
                        final CategoryDataset dset2 = getDataset(1);
                        if (dset2 != null) {
                            final CategoryItemRenderer renderer2 = getRenderer(1);
                            renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            renderer2.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_RIGHT);
                            renderer2.setBasePositiveItemLabelPosition(position);
                            if (renderer2 != null) {
                                for (long i = firstAxisCount; i < totalCount; i++) {
                                    final LegendItem item = renderer2.getLegendItem(0, (int) i);
                                    result.add(item);
                                }
                            }
                        }
                    }
                    return result;
                }
            };
            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinesVisible(true);
            final JFreeChart chart = new JFreeChart(tabWidget.getWidgetTitle(), plot);
            CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
            axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            chart.setBackgroundPaint(Color.white);
            plot.setBackgroundPaint(Color.white);
            plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
            plot.setDataset(1, dataset2);
            plot.mapDatasetToRangeAxis(1, 1);
            // final ValueAxis axis2 = new NumberAxis("Secondary");
            final ValueAxis axis2 = new NumberAxis();
            plot.setRangeAxis(1, axis2);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            final AreaRenderer renderer2 = new AreaRenderer();
            Paint[] paint = new Paint[]{
                new Color(98, 203, 49),
                new Color(85, 85, 85),
                new Color(161, 225, 132),
                new Color(102, 102, 102)
            };
            int j = 0;
            for (long i = 0; i < totalCount; i++) {
                if (i == 4) {
                    j = 0;
                }
                renderer2.setSeriesPaint((int) i, paint[j++]);
                plot.setRenderer((int) i, renderer2);
            }
//            renderer2.setSeriesPaint(0, new Color(98, 203, 49));
//            renderer2.setSeriesPaint(1, new Color(85, 85, 85));
//            plot.setRenderer(0, renderer2);
//            plot.setRenderer(1, renderer2);
            plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
            // OPTIONAL CUSTOMISATION COMPLETED.

            plot.setDrawingSupplier(new ChartDrawingSupplier());

            PdfContentByte contentByte = writer.getDirectContent();

            PdfTemplate templatePie = contentByte.createTemplate(widgetWidth, widgetHeight);
            Graphics2D graphics2dPie = templatePie.createGraphics(widgetWidth, widgetHeight,
                    new DefaultFontMapper());
            Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, widgetWidth,
                    widgetHeight);

            chart.draw(graphics2dPie, rectangle2dPie);

            graphics2dPie.dispose();

            // contentByte.addTemplate(templatePie, 30, 30);
            Image img = Image.getInstance(templatePie);
            return img;
        } catch (BadElementException ex) {
            log.error("BadElementException in multiAxisAreaChart Function: " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("End function of multiAxisAreaChart");
        return null;
    }

    public Image multiAxisBarChart(PdfWriter writer, TabWidget tabWidget) {
        System.out.println("Start function of multiAxisBarChart");
        try {
            List<WidgetColumn> columns = tabWidget.getColumns();

            List<Map<String, Object>> originalData = tabWidget.getData();
            if (originalData == null || originalData.isEmpty()) {
                return null;
            }
            List<Map<String, Object>> data = new ArrayList<>(originalData);

            List<Map<String, Object>> tempData = tabWidget.getData();

            List<SortType> sortFields = new ArrayList<>();
            List<Aggregation> aggreagtionList = new ArrayList<>();
            List<FirstAxis> firstAxis = new ArrayList<>();
            List<SecondAxis> secondAxis = new ArrayList<>();
            String xAxis = null;
            String xAxisDisplay = null;

            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                if (column.getSortOrder() != null) {
                    sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
                }
                if (column.getAgregationFunction() != null) {
                    aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) == 1) {
                    firstAxis.add(new FirstAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) > 1) {
                    secondAxis.add(new SecondAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getxAxis() != null) {
                    xAxis = column.getFieldName();
                    xAxisDisplay = column.getDisplayName();
                    System.out.println("XAxisDisplay: " + xAxisDisplay);
                }
            }

            if (sortFields.size() > 0) {
                data = sortData(data, sortFields);
            }

            if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
                data = data.subList(0, tabWidget.getMaxRecord());
            }

//            final CategoryDataset dataset1 = createDataset3();
//            final CategoryDataset dataset2 = createDataset4();
            Stream<FirstAxis> firstAxiss = firstAxis.stream().distinct();
            long firstAxisCount = firstAxiss.count();

            Stream<SecondAxis> secondAxiss = secondAxis.stream().distinct();
            long secondAxisCount = secondAxiss.count();

            long totalCount = firstAxisCount + secondAxisCount;
            final CategoryDataset dataset1 = createDataset1(data, firstAxis, secondAxis, xAxis);
            final CategoryDataset dataset2 = createDataset2(data, secondAxis, firstAxis, xAxis);

            System.out.println("Dataset1 bar data: " + data);
            System.out.println("Dataset1 bar first Axis: " + firstAxis);
            System.out.println("Dataset1 bar Second Axis: " + secondAxis);
            System.out.println("Dataset1 bar X Axis: " + xAxis);

            final CategoryAxis domainAxis = new CategoryAxis(xAxisDisplay);
            //final NumberAxis rangeAxis = new NumberAxis("Value");
            final NumberAxis rangeAxis = new NumberAxis();
            final BarRenderer renderer1 = new BarRenderer();
            final CategoryPlot plot = new CategoryPlot(dataset1, domainAxis, rangeAxis, renderer1) {

                /**
                 * Override the getLegendItems() method to handle special case.
                 *
                 * @return the legend items.
                 */
                public LegendItemCollection getLegendItems() {

                    final LegendItemCollection result = new LegendItemCollection();

                    if (firstAxis.isEmpty()) {
                    } else {
                        final CategoryDataset data = getDataset();
                        if (data != null) {
                            final CategoryItemRenderer r = getRenderer();
                            r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            r.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_CENTER);
                            r.setBasePositiveItemLabelPosition(position);
                            if (r != null) {
                                for (long i = 0; i < firstAxisCount; i++) {
                                    final LegendItem item = r.getLegendItem(0, (int) i);
                                    result.add(item);
                                }
                            }
                        }
                    }

                    // the JDK 1.2.2 compiler complained about the name of this
                    // variable 
                    if (secondAxis.isEmpty()) {
                    } else {
                        final CategoryDataset dset2 = getDataset(1);
                        if (dset2 != null) {
                            System.out.println("dset2");
                            final CategoryItemRenderer renderer2 = getRenderer(1);
                            renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            renderer2.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_CENTER);
                            renderer2.setBasePositiveItemLabelPosition(position);
                            if (renderer2 != null) {
                                for (long i = firstAxisCount; i < totalCount; i++) {
                                    final LegendItem item = renderer2.getLegendItem(0, (int) i);
                                    result.add(item);
                                }
                            }
                        }
                    }
                    return result;
                }

            };
            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinesVisible(true);
            plot.setOrientation(PlotOrientation.VERTICAL);
            // final JFreeChart chart = new JFreeChart(tabWidget.getWidgetTitle(), plot);
            final JFreeChart chart = new JFreeChart(plot);
            CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
            axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            chart.setBackgroundPaint(Color.white);
            plot.setBackgroundPaint(Color.white);
            plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
            plot.setDataset(1, dataset2);
            plot.mapDatasetToRangeAxis(1, 1);
            final ValueAxis axis2 = new NumberAxis();
            plot.setRangeAxis(1, axis2);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            final BarRenderer renderer2 = new BarRenderer();
            renderer2.setShadowVisible(false);
            Paint[] paint = new Paint[]{
                new Color(98, 203, 49),
                new Color(85, 85, 85),
                new Color(161, 225, 132),
                new Color(102, 102, 102)
            };
            int j = 0;
            for (long i = 0; i < totalCount; i++) {
                if (i == 4) {
                    j = 0;
                }
                renderer2.setSeriesPaint((int) i, paint[j++]);
                plot.setRenderer((int) i, renderer2);
            }
            plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);

            // OPTIONAL CUSTOMISATION COMPLETED.
            plot.setDrawingSupplier(new ChartDrawingSupplier());
            PdfContentByte contentByte = writer.getDirectContent();

            PdfTemplate templatePie = contentByte.createTemplate(widgetWidth, widgetHeight);
            Graphics2D graphics2dPie = templatePie.createGraphics(widgetWidth, widgetHeight,
                    new DefaultFontMapper());
            Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, widgetWidth,
                    widgetHeight);

            chart.draw(graphics2dPie, rectangle2dPie);

            graphics2dPie.dispose();

            // contentByte.addTemplate(templatePie, 30, 30);
            Image img = Image.getInstance(templatePie);
            return img;
        } catch (BadElementException ex) {
            log.error("BadElementException in multiAxisBarChart Function: " + ex);
            //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("End function of multiAxisBarChart");
        return null;
    }

    public JFreeChart multiAxisAreaJFreeChart(TabWidget tabWidget) {
        System.out.println("Start function of multiAxisAreaJFreeChart");

        List<WidgetColumn> columns = tabWidget.getColumns();

        List<Map<String, Object>> originalData = tabWidget.getData();
        List<Map<String, Object>> data = new ArrayList<>(originalData);

        List<Map<String, Object>> tempData = tabWidget.getData();

        List<SortType> sortFields = new ArrayList<>();
        List<Aggregation> aggreagtionList = new ArrayList<>();
        List<FirstAxis> firstAxis = new ArrayList<>();
        List<SecondAxis> secondAxis = new ArrayList<>();
        String xAxis = null;
        String xAxisDisplay = null;

        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getSortOrder() != null) {
                sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
            }
            if (column.getAgregationFunction() != null) {
                aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
            }
            if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) == 1) {
                firstAxis.add(new FirstAxis(column.getFieldName(), column.getDisplayName()));
            }
            if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) > 1) {
                secondAxis.add(new SecondAxis(column.getFieldName(), column.getDisplayName()));
            }
            if (column.getxAxis() != null) {
                xAxis = column.getFieldName();
                xAxisDisplay = column.getDisplayName();
            }
        }

        if (sortFields.size() > 0) {
            data = sortData(data, sortFields);
        }

        if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
            data = data.subList(0, tabWidget.getMaxRecord());
        }

        Stream<FirstAxis> firstAxiss = firstAxis.stream().distinct();
        long firstAxisCount = firstAxiss.count();

        Stream<SecondAxis> secondAxiss = secondAxis.stream().distinct();
        long secondAxisCount = secondAxiss.count();

        long totalCount = firstAxisCount + secondAxisCount;
        final CategoryDataset dataset1 = createDataset1(data, firstAxis, secondAxis, xAxis);
        final CategoryDataset dataset2 = createDataset2(data, secondAxis, firstAxis, xAxis);

        final CategoryAxis domainAxis = new CategoryAxis(xAxisDisplay);
        final NumberAxis rangeAxis = new NumberAxis();
        final BarRenderer renderer1 = new BarRenderer();
        final CategoryPlot plot = new CategoryPlot(dataset1, domainAxis, rangeAxis, renderer1) {

            /**
             * Override the getLegendItems() method to handle special case.
             *
             * @return the legend items.
             */
            public LegendItemCollection getLegendItems() {

                final LegendItemCollection result = new LegendItemCollection();
                if (firstAxis.isEmpty()) {
                } else {
                    final CategoryDataset data = getDataset();
                    if (data != null) {
                        final CategoryItemRenderer r = getRenderer();
                        r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                        r.setBaseItemLabelsVisible(true);
                        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                TextAnchor.BASELINE_CENTER);
                        r.setBasePositiveItemLabelPosition(position);
                        if (r != null) {
                            for (long i = 0; i < firstAxisCount; i++) {
                                final LegendItem item = r.getLegendItem(0, (int) i);
                                result.add(item);
                            }
                        }
                    }
                }

                // the JDK 1.2.2 compiler complained about the name of this
                // variable 
                if (secondAxis.isEmpty()) {
                } else {
                    final CategoryDataset dset2 = getDataset(1);
                    if (dset2 != null) {
                        System.out.println("dset2");
                        final CategoryItemRenderer renderer2 = getRenderer(1);
                        renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                        renderer2.setBaseItemLabelsVisible(true);
                        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                TextAnchor.BASELINE_CENTER);
                        renderer2.setBasePositiveItemLabelPosition(position);
                        if (renderer2 != null) {
                            for (long i = firstAxisCount; i < totalCount; i++) {
                                final LegendItem item = renderer2.getLegendItem(0, (int) i);
                                result.add(item);
                            }
                        }
                    }
                }
                return result;
            }

        };
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        plot.setOrientation(PlotOrientation.VERTICAL);
        final JFreeChart chart = new JFreeChart(plot);
        CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        chart.setBackgroundPaint(Color.white);

        plot.setBackgroundPaint(Color.white);
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        //final ValueAxis axis2 = new NumberAxis("Secondary");
        final ValueAxis axis2 = new NumberAxis();
        plot.setRangeAxis(1, axis2);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        final AreaRenderer renderer2 = new AreaRenderer();
        Paint[] paint = new Paint[]{
            new Color(98, 203, 49),
            new Color(85, 85, 85),
            new Color(161, 225, 132),
            new Color(102, 102, 102)
        };
        int j = 0;
        for (long i = 0; i < totalCount; i++) {
            if (i == 4) {
                j = 0;
            }
            renderer2.setSeriesPaint((int) i, paint[j++]);
            plot.setRenderer((int) i, renderer2);
        }
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);

        // OPTIONAL CUSTOMISATION COMPLETED.
        plot.setDrawingSupplier(new ChartDrawingSupplier());
        System.out.println("End function of multiAxisAreaJFreeChart");

        return chart;

    }

    public JFreeChart multiAxisBarJFreeChart(TabWidget tabWidget) {
        System.out.println("Start function of multiAxisBarJFreeChart");

        List<WidgetColumn> columns = tabWidget.getColumns();

        List<Map<String, Object>> originalData = tabWidget.getData();
        List<Map<String, Object>> data = new ArrayList<>(originalData);

        List<Map<String, Object>> tempData = tabWidget.getData();

        List<SortType> sortFields = new ArrayList<>();
        List<Aggregation> aggreagtionList = new ArrayList<>();
        List<FirstAxis> firstAxis = new ArrayList<>();
        List<SecondAxis> secondAxis = new ArrayList<>();
        String xAxis = null;
        String xAxisDisplay = null;

        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getSortOrder() != null) {
                sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
            }
            if (column.getAgregationFunction() != null) {
                aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
            }
            if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) == 1) {
                firstAxis.add(new FirstAxis(column.getFieldName(), column.getDisplayName()));
            }
            if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) > 1) {
                secondAxis.add(new SecondAxis(column.getFieldName(), column.getDisplayName()));
            }
            if (column.getxAxis() != null) {
                xAxis = column.getFieldName();
                xAxisDisplay = column.getDisplayName();
            }
        }

        if (sortFields.size() > 0) {
            data = sortData(data, sortFields);
        }

        if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
            data = data.subList(0, tabWidget.getMaxRecord());
        }

        Stream<FirstAxis> firstAxiss = firstAxis.stream().distinct();
        long firstAxisCount = firstAxiss.count();

        Stream<SecondAxis> secondAxiss = secondAxis.stream().distinct();
        long secondAxisCount = secondAxiss.count();

        long totalCount = firstAxisCount + secondAxisCount;
        final CategoryDataset dataset1 = createDataset1(data, firstAxis, secondAxis, xAxis);
        final CategoryDataset dataset2 = createDataset2(data, secondAxis, firstAxis, xAxis);

        final CategoryAxis domainAxis = new CategoryAxis(xAxisDisplay);
        final NumberAxis rangeAxis = new NumberAxis();
        final BarRenderer renderer1 = new BarRenderer();
        final CategoryPlot plot = new CategoryPlot(dataset1, domainAxis, rangeAxis, renderer1) {

            /**
             * Override the getLegendItems() method to handle special case.
             *
             * @return the legend items.
             */
            public LegendItemCollection getLegendItems() {

                final LegendItemCollection result = new LegendItemCollection();
                if (firstAxis.isEmpty()) {
                } else {
                    final CategoryDataset data = getDataset();
                    if (data != null) {
                        final CategoryItemRenderer r = getRenderer();
                        r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                        r.setBaseItemLabelsVisible(true);
                        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                TextAnchor.BASELINE_CENTER);
                        r.setBasePositiveItemLabelPosition(position);
                        if (r != null) {
                            for (long i = 0; i < firstAxisCount; i++) {
                                final LegendItem item = r.getLegendItem(0, (int) i);
                                result.add(item);
                            }
                        }
                    }
                }

                // the JDK 1.2.2 compiler complained about the name of this
                // variable 
                if (secondAxis.isEmpty()) {
                } else {
                    final CategoryDataset dset2 = getDataset(1);
                    if (dset2 != null) {
                        System.out.println("dset2");
                        final CategoryItemRenderer renderer2 = getRenderer(1);
                        renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                        renderer2.setBaseItemLabelsVisible(true);
                        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                TextAnchor.BASELINE_CENTER);
                        renderer2.setBasePositiveItemLabelPosition(position);
                        if (renderer2 != null) {
                            for (long i = firstAxisCount; i < totalCount; i++) {
                                final LegendItem item = renderer2.getLegendItem(0, (int) i);
                                result.add(item);
                            }
                        }
                    }
                }
                return result;
            }

        };
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        plot.setOrientation(PlotOrientation.VERTICAL);
        final JFreeChart chart = new JFreeChart(plot);
        CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        chart.setBackgroundPaint(Color.white);

        plot.setBackgroundPaint(Color.white);
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        //final ValueAxis axis2 = new NumberAxis("Secondary");
        final ValueAxis axis2 = new NumberAxis();
        plot.setRangeAxis(1, axis2);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        final BarRenderer renderer2 = new BarRenderer();
        renderer2.setShadowVisible(false);
        Paint[] paint = new Paint[]{
            new Color(98, 203, 49),
            new Color(85, 85, 85),
            new Color(161, 225, 132),
            new Color(102, 102, 102)
        };
        int j = 0;
        for (long i = 0; i < totalCount; i++) {
            if (i == 4) {
                j = 0;
            }
            renderer2.setSeriesPaint((int) i, paint[j++]);
            plot.setRenderer((int) i, renderer2);
        }
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);

        // OPTIONAL CUSTOMISATION COMPLETED.
        plot.setDrawingSupplier(new ChartDrawingSupplier());
        System.out.println("End function of multiAxisBarJFreeChart");

        return chart;

    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private CategoryDataset createDataset1(List<Map<String, Object>> data, List<FirstAxis> firstAxis, List<SecondAxis> secondAxis, String xAxis) {
        System.out.println("Start function of createDataset1");

        // create the dataset...
        DecimalFormat df = new DecimalFormat(".##");
        String value;
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            for (Iterator<FirstAxis> iterator1 = firstAxis.iterator(); iterator1.hasNext();) {
                FirstAxis axis = iterator1.next();
                System.out.println("---->");
                System.out.println("-----> " + dataMap.get(axis.getFieldName()));
                if (dataMap.get(axis.getFieldName()) == null) {
                    value = "0.00";
                    String data1 = value.getClass().getSimpleName();
                    if (data1.equalsIgnoreCase("String")) {
                        System.out.println("if");
                        dataset.addValue(ApiUtils.toDouble(df.format(Float.parseFloat(value)) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    }
                } else {
                    String data1 = dataMap.get(axis.getFieldName()).getClass().getSimpleName();
                    System.out.println("******");
                    System.out.println("Type: " + data1);
                    if (data1.equalsIgnoreCase("String")) {
                        System.out.println("if");
                        System.out.println(ApiUtils.toDouble(dataMap.get(axis.getFieldName()).toString() + "") + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
//                        dataset.addValue(ApiUtils.toDouble(df.format(Float.parseFloat(dataMap.get(axis.getFieldName()).toString())) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                        dataset.addValue(ApiUtils.toDouble(dataMap.get(axis.getFieldName()).toString() + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    } else {
                        System.out.println("else");
                        System.out.println(ApiUtils.toDouble(df.format(dataMap.get(axis.getFieldName())) + "") + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
                        dataset.addValue(ApiUtils.toDouble(df.format(dataMap.get(axis.getFieldName())) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    }
                }
            }
        }
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();

            for (Iterator<SecondAxis> iterator1 = secondAxis.iterator(); iterator1.hasNext();) {
                SecondAxis axis = iterator1.next();
                System.out.println(null + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
                dataset.addValue(null, axis.getDisplayName(), dataMap.get(xAxis) + "");
            }
        }
        System.out.println("End function of createDataset1");
        return dataset;
    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private CategoryDataset createDataset2(List<Map<String, Object>> data, List<SecondAxis> secondAxis, List<FirstAxis> firstAxis, String xAxis) {
        System.out.println("Start function of createDataset2");
        // create the dataset...
        DecimalFormat df = new DecimalFormat(".##");
        String value;
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            for (Iterator<FirstAxis> iterator1 = firstAxis.iterator(); iterator1.hasNext();) {
                FirstAxis axis = iterator1.next();
                System.out.println(null + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
                dataset.addValue(null, axis.getDisplayName(), dataMap.get(xAxis) + "");
            }

        }
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();

            for (Iterator<SecondAxis> iterator1 = secondAxis.iterator(); iterator1.hasNext();) {
                SecondAxis axis = iterator1.next();
                if (dataMap.get(axis.getFieldName()) == null) {
                    value = "0.00";
                    String data1 = value.getClass().getSimpleName();
                    if (data1.equalsIgnoreCase("String")) {
                        System.out.println(ApiUtils.toDouble(df.format(Float.parseFloat(value)) + "") + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
                        dataset.addValue(ApiUtils.toDouble(df.format(Float.parseFloat(value)) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    }
                } else {
                    String data1 = dataMap.get(axis.getFieldName()).getClass().getSimpleName();
                    if (data1.equalsIgnoreCase("String")) {
                        System.out.println(ApiUtils.toDouble(dataMap.get(axis.getFieldName()).toString() + "") + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
//                        dataset.addValue(ApiUtils.toDouble(df.format(Float.parseFloat(dataMap.get(axis.getFieldName()).toString())) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                        dataset.addValue(ApiUtils.toDouble(dataMap.get(axis.getFieldName()).toString() + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    } else {
                        System.out.println(ApiUtils.toDouble(df.format(dataMap.get(axis.getFieldName())) + "") + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
                        dataset.addValue(ApiUtils.toDouble(df.format(dataMap.get(axis.getFieldName())) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    }
                }
            }
        }
        System.out.println("End function of createDataset2");
        return dataset;
    }

    private CategoryDataset createDataset1() {
        // row keys...
        final String series1 = "Series 1";
        final String series2 = "Dummy 1";
//
//        // column keys...
        final String category1 = "Category 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, category1);
        dataset.addValue(4.0, series1, category2);
        dataset.addValue(3.0, series1, category3);
        dataset.addValue(5.0, series1, category4);

        dataset.addValue(null, series2, category1);
        dataset.addValue(null, series2, category2);
        dataset.addValue(null, series2, category3);
        dataset.addValue(null, series2, category4);
        return dataset;

    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset2() {

        // row keys...
        final String series1 = "Dummy 2";
        final String series2 = "Series 2";

        // column keys...
        final String category1 = "Category 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(null, series1, category1);
        dataset.addValue(null, series1, category2);
        dataset.addValue(null, series1, category3);
        dataset.addValue(null, series1, category4);

        dataset.addValue(75.0, series2, category1);
        dataset.addValue(87.0, series2, category2);
        dataset.addValue(96.0, series2, category3);
        dataset.addValue(68.0, series2, category4);

        return dataset;

    }

    private CategoryDataset createDataset3() {
        // row keys...

        final String series1 = "Series 1";
        final String series2 = "Dummy 1";
//        // column keys...
        final String category1 = "Category 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(785, "clicks", "Monday");
        dataset.addValue(572, "clicks", "Tuesday");
        dataset.addValue(558, "clicks", "Wednessday");
        dataset.addValue(391, "clicks", "Thursday");
        dataset.addValue(536, "clicks", "Friday");
        dataset.addValue(490, "clicks", "Saturday");
        dataset.addValue(731, "clicks", "Sunday");

        dataset.addValue(null, "conversions", "Monday");
        dataset.addValue(null, "conversions", "Tuesday");
        dataset.addValue(null, "conversions", "Wednessday");
        dataset.addValue(null, "conversions", "Thursday");
        dataset.addValue(null, "conversions", "Friday");
        dataset.addValue(null, "conversions", "Saturday");
        dataset.addValue(null, "conversions", "Sunday");
        return dataset;

    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset4() {

        // row keys...
        final String series1 = "Dummy 2";
        final String series2 = "Series 2";

        // column keys...
        final String category1 = "Category 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(null, "clicks", "Monday");
        dataset.addValue(null, "clicks", "Tuesday");
        dataset.addValue(null, "clicks", "Wednessday");
        dataset.addValue(null, "clicks", "Thursday");
        dataset.addValue(null, "clicks", "Friday");
        dataset.addValue(null, "clicks", "Saturday");
        dataset.addValue(null, "clicks", "Sunday");

        dataset.addValue(132, "conversions", "Monday");
        dataset.addValue(79, "conversions", "Tuesday");
        dataset.addValue(72, "conversions", "Wednessday");
        dataset.addValue(18, "conversions", "Thursday");
        dataset.addValue(68, "conversions", "Friday");
        dataset.addValue(73, "conversions", "Saturday");
        dataset.addValue(34, "conversions", "Sunday");

        return dataset;

    }

    public static Image generateBarChart(PdfWriter writer, TabWidget tabWidget) throws BadElementException {
        System.out.println("Start function of generateBarChart");
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        JFreeChart chart = ChartFactory.createBarChart(
                "Number of times user visit", "Count", "Number Of Visits",
                dataSet, PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        final CategoryItemRenderer renderer = new CustomRenderer(
                new Paint[]{new Color(116, 196, 198), new Color(116, 196, 198),
                    new Color(116, 196, 198), new Color(116, 196, 198),
                    new Color(116, 196, 198)
                });
//        renderer.setLabelGenerator(new StandardCategoryLabelGenerator());
        renderer.setItemLabelsVisible(true);

        plot.setRenderer(renderer);

        plot.setDrawingSupplier(new ChartDrawingSupplier());

        PdfContentByte contentByte = writer.getDirectContent();

        PdfTemplate templatePie = contentByte.createTemplate(widgetWidth, widgetHeight);
        Graphics2D graphics2dPie = templatePie.createGraphics(widgetWidth, widgetHeight,
                new DefaultFontMapper());
        Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, widgetWidth,
                widgetHeight);

        chart.draw(graphics2dPie, rectangle2dPie);

        graphics2dPie.dispose();

        // contentByte.addTemplate(templatePie, 30, 30);
        Image img = Image.getInstance(templatePie);
        System.out.println("End action of generateBarChart");
        return img;
    }

    public JFreeChart generatePieJFreeChart(TabWidget tabWidget) throws BadElementException {
        System.out.println("End function of generatePieJFreeChart");
        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, Object>> originaldata = tabWidget.getData();
        List<Map<String, Object>> data;
        if (originaldata == null || originaldata.isEmpty()) {
            data = new ArrayList<>();
            return null;
        } else {
            data = new ArrayList<>(originaldata);
        }

        String xAxis = null;
        String yAxis = null;
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getxAxis() != null) {
                xAxis = column.getFieldName();
            }
            if (column.getyAxis() != null) {
                yAxis = column.getFieldName();
            }
        }

        DefaultPieDataset dataSet = new DefaultPieDataset();
        List<String> legends = new ArrayList<>();

        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            dataSet.setValue(dataMap.get(xAxis) + "", ApiUtils.toDouble(dataMap.get(yAxis) + ""));
            legends.add(dataMap.get(xAxis) + "");
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "", dataSet, true, false, false);

        Paint[] paintSequence = new Paint[]{
            //  new Color(255, 191, 128),
            new Color(98, 203, 49),
            new Color(102, 102, 102),
            new Color(165, 209, 105),
            new Color(117, 204, 208)
        };
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setDrawingSupplier(new ChartDrawingSupplier());
        plot.setBackgroundPaint(Color.white);
        plot.setOutlineVisible(false);
        int i = 0;
        for (Iterator<String> iterator = legends.iterator(); iterator.hasNext();) {
            if (i > 3) {
                i = 1;
            }
            String legend = iterator.next();
            plot.setSectionPaint(legend, paintSequence[i++]);
        }
        System.out.println("End function of generatePieJFreeChart");
        return chart;
    }

    public static Image generatePieChart(PdfWriter writer, TabWidget tabWidget) throws BadElementException {
        System.out.println("Start function of generatePieChart");
        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, Object>> originaldata = tabWidget.getData();

        if (originaldata == null || originaldata.isEmpty()) {
            return null;
        }
        List<Map<String, Object>> data = new ArrayList<>(originaldata);
        System.out.println("Data : " + data);
        String xAxis = null;
        String yAxis = null;
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getxAxis() != null) {
                xAxis = column.getFieldName();
                System.out.println("xAxis : " + xAxis);
            }
            if (column.getyAxis() != null) {
                yAxis = column.getFieldName();
                System.out.println("yAxis : " + yAxis);
            }
        }

        DefaultPieDataset dataSet = new DefaultPieDataset();
        List<String> legends = new ArrayList<>();

        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            dataSet.setValue(dataMap.get(xAxis) + "", ApiUtils.toDouble(dataMap.get(yAxis) + ""));
            System.out.println("dataMap: " + dataMap.get(xAxis) + "  " + ApiUtils.toDouble(dataMap.get(yAxis) + " "));
            legends.add(dataMap.get(xAxis) + "");
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "", dataSet, true, false, false);

        Paint[] paintSequence = new Paint[]{
            //   new Color(255, 191, 128),
            new Color(98, 203, 49),
            new Color(102, 102, 102),
            new Color(165, 209, 105),
            new Color(117, 204, 208)
        };
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setDrawingSupplier(new ChartDrawingSupplier());
        plot.setBackgroundPaint(Color.white);
        plot.setOutlineVisible(false);
        int i = 0;
        System.out.println("legends : " + legends);
        for (Iterator<String> iterator = legends.iterator(); iterator.hasNext();) {
            System.out.println("i : " + i);
            if (i > 3) {
                i = 1;
            }
            String legend = iterator.next();
            System.out.println("legend: " + legend);
            plot.setSectionPaint(legend, paintSequence[i++]);
        }

        PdfContentByte contentByte = writer.getDirectContent();
        PdfTemplate templateBar = contentByte.createTemplate(widgetWidth, widgetHeight);
        Graphics2D graphics2dBar = templateBar.createGraphics(widgetWidth, widgetHeight,
                new DefaultFontMapper());
        Rectangle2D rectangle2dBar = new Rectangle2D.Double(0, 0, widgetWidth,
                widgetHeight);

        chart.draw(graphics2dBar, rectangle2dBar);

        graphics2dBar.dispose();
        //contentByte.addTemplate(templateBar, 30, 30);
        Image img = Image.getInstance(templateBar);
        System.out.println("End function of generatePieChart");
        return img;
    }

    private void generateGroupedRows(Map groupedData, TabWidget tabWidget, XSLFTable tbl) {
        System.out.println("Start function of generateGroupedRows - PPT");
        Color tableTitleFontColor = new Color(132, 140, 99);
        Color widgetBorderColor = new Color(204, 204, 204);
        Color widgetTitleColor = Color.WHITE;
        Color tableHeaderFontColor = new Color(61, 70, 77);
        Color tableHeaderColor = new Color(241, 241, 241);
        Color tableFooterColor = new Color(241, 241, 241);

        List<WidgetColumn> columns = tabWidget.getColumns();
        List data = (List) groupedData.get("data");
        int count = 0;
        XSLFTableRow dataRow = null;
        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
            Map mapData = (Map) iterator.next();
            if (mapData.get(mapData.get("_groupField")) != null) {
                count = 1;
                String groupValue = mapData.get(mapData.get("_groupField")) + "";
                dataRow = tbl.addRow();
                //pdfFont.setColor(tableHeaderFontColor);
                //PdfPCell dataCell = new PdfPCell(new Phrase(groupValue, pdfFont));
                //dataCell.setBorderColor(widgetBorderColor);
                //table.addCell(dataCell);
                XSLFTableCell dataCell = dataRow.addCell();
                XSLFTextParagraph pd = dataCell.addNewTextParagraph();
                pd.setTextAlign(TextParagraph.TextAlign.LEFT);
                XSLFTextRun rd = pd.addNewTextRun();
                rd.setText(groupValue);
                System.out.println("If GroupField ----> " + groupValue);
                rd.setBold(false);
                rd.setFontSize(10.0);
                rd.setFontColor(tableHeaderFontColor);
                //titlecell.setLineTailWidth(LineDecoration.DecorationSize.SMALL);
                dataCell.setFillColor(widgetTitleColor);
                //dataCell.setBorderWidth(TableCell.BorderEdge.bottom, 2.0);
                dataCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
                dataCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
                dataCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
                dataCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
                //dataCell.setText(groupValue);
            } else {
                //PdfPCell dataCell = new PdfPCell(new Phrase(""));
                //dataCell.setBorderColor(widgetBorderColor);
                //table.addCell(dataCell);
                if (data.size() > 1) {
                    System.out.println("Else GroupField ---->");
                    count = 2;
                    dataRow = tbl.addRow();
                    XSLFTableCell dataCell = dataRow.addCell();
                    XSLFTextParagraph pd = dataCell.addNewTextParagraph();
                    pd.setTextAlign(TextParagraph.TextAlign.LEFT);
                    XSLFTextRun rd = pd.addNewTextRun();
                    rd.setText("");
                    rd.setBold(false);
                    rd.setFontSize(10.0);
                    rd.setFontColor(tableHeaderFontColor);
                    //titlecell.setLineTailWidth(LineDecoration.DecorationSize.SMALL);
                    dataCell.setFillColor(widgetTitleColor);
                    //dataCell.setBorderWidth(TableCell.BorderEdge.bottom, 2.0);
                    dataCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
                    dataCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
                    dataCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
                    dataCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
                    //dataCell.setText("");
                }
            }
            if (count == 1 || count == 2) {
                for (Iterator<WidgetColumn> iterator1 = columns.iterator(); iterator1.hasNext();) {
                    WidgetColumn column = iterator1.next();
                    if (column.getColumnHide() == null || column.getColumnHide() == 0) {
                        if (mapData.get(column.getFieldName()) != null) {
                            String value = mapData.get(column.getFieldName()) + "";
                            if (column.getDisplayFormat() != null) {
                                value = Formatter.format(column.getDisplayFormat(), value);
                            }
                            XSLFTableCell dataCell = dataRow.addCell();
                            XSLFTextParagraph pd = dataCell.addNewTextParagraph();
                            pd.setTextAlign(TextParagraph.TextAlign.LEFT);
                            XSLFTextRun rd = pd.addNewTextRun();
                            rd.setText(value);
                            rd.setBold(false);
                            rd.setFontSize(10.0);
                            rd.setFontColor(tableHeaderFontColor);
                            System.out.println("If Value Data --- > " + value);

                            //pdfFont.setColor(tableHeaderFontColor);
                            //PdfPCell dataCell = new PdfPCell(new Phrase(value, pdfFont));
                            if (column.getAlignment() != null) {
                                if (column.getAlignment().equalsIgnoreCase("right")) {
                                    dataCell.setLeftInset(2);
                                } else if (column.getAlignment().equalsIgnoreCase("left")) {
                                    dataCell.setRightInset(2);
                                } else if (column.getAlignment().equalsIgnoreCase("center")) {
                                    dataCell.setHorizontalCentered(Boolean.TRUE);
                                }
                                //dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                            }
                            //dataCell.setBorderColor(widgetBorderColor);
                            //table.addCell(dataCell);

                            //titlecell.setLineTailWidth(LineDecoration.DecorationSize.SMALL);
                            dataCell.setFillColor(widgetTitleColor);
                            //dataCell.setBorderWidth(TableCell.BorderEdge.bottom, 2.0);
                            dataCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
                            dataCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
                            dataCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
                            dataCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
                            //dataCell.setText(value);
                        } else {
//                        PdfPCell dataCell = new PdfPCell(new Phrase(""));
//                        dataCell.setBorderColor(widgetBorderColor);
//                        table.addCell(dataCell);
                            System.out.println("Else Value Data --- > ");

                            XSLFTableCell dataCell = dataRow.addCell();
                            XSLFTextParagraph pd = dataCell.addNewTextParagraph();
                            pd.setTextAlign(TextParagraph.TextAlign.LEFT);
                            XSLFTextRun rd = pd.addNewTextRun();
                            rd.setText("");
                            rd.setBold(false);
                            rd.setFontSize(10.0);
                            rd.setFontColor(tableHeaderFontColor);
                            //titlecell.setLineTailWidth(LineDecoration.DecorationSize.SMALL);
                            dataCell.setFillColor(widgetTitleColor);
                            //dataCell.setBorderWidth(TableCell.BorderEdge.bottom, 2.0);
                            dataCell.setBorderColor(TableCell.BorderEdge.bottom, widgetBorderColor);
                            dataCell.setBorderColor(TableCell.BorderEdge.right, widgetBorderColor);
                            dataCell.setBorderColor(TableCell.BorderEdge.left, widgetBorderColor);
                            dataCell.setBorderColor(TableCell.BorderEdge.top, widgetBorderColor);
                            //dataCell.setText("");
                        }
                    }
                }
            }
            if (mapData.get("data") != null) {
                generateGroupedRows(mapData, tabWidget, tbl);
            }
        }
        System.out.println("End function of generateGroupedRows - PPT");
    }

    public static class CustomRenderer extends BarRenderer {

        /**
         * The colors.
         */
        private Paint[] colors;

        /**
         * Creates a new renderer.
         *
         * @param colors the colors.
         */
        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        /**
         * Returns the paint for an item. Overrides the default behaviour
         * inherited from AbstractSeriesRenderer.
         *
         * @param row the series.
         * @param column the category.
         *
         * @return The item color.
         */
        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }

    public static class ChartDrawingSupplier extends DefaultDrawingSupplier {

        public Paint[] paintSequence;
        public int paintIndex;
        public int fillPaintIndex;

        {
            paintSequence = new Paint[]{
                new Color(227, 26, 28),
                new Color(000, 102, 204),
                new Color(102, 051, 153),
                new Color(102, 51, 0),
                new Color(156, 136, 48),
                new Color(153, 204, 102),
                new Color(153, 51, 51),
                new Color(102, 51, 0),
                new Color(204, 153, 51),
                new Color(0, 51, 0)};
        }

        @Override
        public Paint getNextPaint() {
            Paint result
                    = paintSequence[paintIndex % paintSequence.length];
            paintIndex++;
            return result;
        }

        @Override
        public Paint getNextFillPaint() {
            Paint result
                    = paintSequence[fillPaintIndex % paintSequence.length];
            fillPaintIndex++;
            return result;
        }
    }

    public static class CalcualtedFunction {

        private String name;
        private String field1;
        private String field2;

        public CalcualtedFunction(String name, String field1, String field2) {
            this.name = name;
            this.field1 = field1;
            this.field2 = field2;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }
    }

    public class Aggregation {

        private String fieldName;
        private String aggregationType;

        public Aggregation(String fieldName, String aggregationType) {
            this.fieldName = fieldName;
            this.aggregationType = aggregationType;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getAggregationType() {
            return aggregationType;
        }

        public void setAggregationType(String aggregationType) {
            this.aggregationType = aggregationType;
        }

    }

    public class SortType {

        private String fieldName;
        private String sortOrder;
        private String fieldType;

        public SortType(String fieldName, String sortOrder, String fieldType) {
            this.fieldName = fieldName;
            this.sortOrder = sortOrder;
            this.fieldType = fieldType;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }
    }

    public class FirstAxis {

        private String fieldName;
        private String displayName;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public FirstAxis(String fieldName, String displayName) {
            this.fieldName = fieldName;
            this.displayName = displayName;
        }
    }

    public class SecondAxis {

        private String fieldName;
        private String displayName;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public SecondAxis(String fieldName, String displayName) {
            this.fieldName = fieldName;
            this.displayName = displayName;
        }
    }

    public static class PageNumeration extends PdfPageEventHelper {

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            System.out.println("Start function of onEndPage");

            //            ColumnText ct = new ColumnText(writer.getDirectContent());
//            ct.setSimpleColumn(new Rectangle(36, 832, 559, 810));
//            for (Element e : header) {
//                ct.addElement(e);
//            }
            PdfPTable table = new PdfPTable(1);

            table.setTotalWidth(523);
            PdfPCell cell = new PdfPCell(new Phrase("Page Number " + writer.getPageNumber()));
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setBackgroundColor(BaseColor.ORANGE);
            table.addCell(cell);
            //cell = new PdfPCell(new Phrase("This is a copyright notice"));
            //cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //table.addCell(cell);
            table.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
            System.out.println("End function of onEndPage");

        }
    }

    public static class HeaderFooterTable extends PdfPageEventHelper {

        protected PdfPTable footer;

        public HeaderFooterTable() {

        }

        public HeaderFooterTable(PdfPTable footer) {
            this.footer = footer;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            System.out.println("Start function of onEndPage - 2");

            try {
                //            ColumnText ct = new ColumnText(writer.getDirectContent());
//            ct.setSimpleColumn(new Rectangle(36, 832, 559, 810));
//            for (Element e : header) {
//                ct.addElement(e);
//            }
                // System.out.println("LOCATION PATH " + getClass().getProtectionDomain().getCodeSource().getLocation());
                Rectangle rectangle = pageSize; // new Rectangle(10, 900, 100, 850);
                Image img = Image.getInstance(CustomReportDesigner.class.getResource("") + "/../images/deeta-logo.png");
                img.scaleToFit(90, 90);
                img.setAbsolutePosition(62, rectangle.getTop() - 50);
                img.setAlignment(Element.ALIGN_TOP);
                writer.getDirectContent().addImage(img);

                if (footer != null) {
                    footer.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
                }
            } catch (BadElementException ex) {
                log.error("BadElementException in onEndPage Function: " + ex);
                //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | DocumentException ex) {
                log.error("Exception in onEndPage Function: " + ex);
                //Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("End function of onEndPage - 2");
        }
    }
}
