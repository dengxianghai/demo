package com.rep.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rep.controller.ExportController;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 导出Excel工具类
 *
 *
 * @author  dxh
 *
 */
public class ExportExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExportExcelUtils.class);

    /**
     * 生成折线图和曲线图
     *
     * @param headerList 列名
     * @param dataList 数据
     * @param title 文件名
     * @param xName x轴名称
     * @param yName y轴名称
     * @param resp http响应
     * @param isSmooth 是否平滑
     * @param colorList 图例颜色
     * @throws IOException
     */
    public static void createOneYaxisLineChart(LinkedList headerList, String [][]dataList,
                                        String title, String xName, String yName,
                                        HttpServletResponse resp, boolean isSmooth,LinkedList colorList) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "图表";
        FileOutputStream fileOut = null;
        try {
            XSSFSheet sheet = wb.createSheet(sheetName);

            //第一行
            Row row = sheet.createRow(0);
            Cell cell =null;
            for(int i=0;i<headerList.size();i++){
                cell = row.createCell(i);
                cell.setCellValue((String)headerList.get(i));

            }
            // 第二行数据
            for(int i=0;i<dataList.length;i++){
                row = sheet.createRow(i+1);
                for(int j=0;j<dataList[i].length;j++){
                    cell = row.createCell(j);
                    if(j==0){
                        cell.setCellValue(dataList[i][j]);
                    }else{
                        cell.setCellValue(Double.parseDouble(dataList[i][j]));
                    }
                }
            }


            //创建一个画布
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            //前四个默认0，[0,5]：从0列5行开始;[7,26]:宽度7个单元格，26向下扩展到26行
            //默认宽度(14-8)*12
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, 1, headerList.size()+dataList.length/2, 26);
//                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 7, 26);


            //创建一个chart对象
            XSSFChart chart = drawing.createChart(anchor);
            //标题
            chart.setTitleText(title);
            //标题覆盖
            chart.setTitleOverlay(false);

            //图例位置
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP);

            //分类轴标(X轴),标题位置
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(org.apache.poi.xddf.usermodel.chart.AxisPosition.BOTTOM);
            bottomAxis.setTitle(xName);
            //值(Y轴)轴,标题位置
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle(yName);



            //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
            //分类轴标(X轴)数据，单元格范围位置
            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));

            LinkedList yList=new LinkedList();
            for(int i=1;i<dataList[0].length;i++){
                XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, i, i));
                yList.add(area);
            }
            //数据1，单元格范围位置[1, 0]到[1, 6]
//            XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 1, 1));

            //LINE：折线图，
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

            for(int i=0;i<yList.size();i++){
                //图表加载数据，折线1
                XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, (XDDFNumericalDataSource<Double>)yList.get(i));
                //TODO:折线图例标题
                series1.setTitle(headerList.get(i+1).toString(), null);
                //直线  false是折线图，true是曲线图
                series1.setSmooth(isSmooth);

                //设置标记大小
                series1.setMarkerSize((short) 6);
                //设置标记样式
                //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
                series1.setMarkerStyle(MarkerStyle.NONE);

                //TODO:枚举类---------设置颜色特性,根据传来的值
                String color= (String) colorList.get(i);
//                Class<PresetColor> presetColorClass = null;
//                PresetColor lineColor=(PresetColor)Enum.valueOf((presetColorClass ), color);
//                (PresetColor)Enum.valueOf((PresetColor), "Email", false);
                PresetColor lineColor=PresetColor.valueOf(color);

                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(lineColor));
                series1.setFillProperties(fill);
                XDDFLineProperties aaa=new XDDFLineProperties(fill);
                series1.setLineProperties(aaa);
            }


            //绘制
            chart.plot(data);
            //TODO:关键语句，当只有一组y值时，必须设置，否则会出现把x每个值都当作图例的错误
            chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);

            // 将输出写入excel文件
            String filename = "F://"+title+System.currentTimeMillis()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
            fileOut.close();

            String fileName=title+".xlsx";
            fileName = new String(fileName.getBytes(), "ISO8859-1");

            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            resp.flushBuffer();
            wb.write(resp.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }


    }

    /**
     * 生成多个的单y轴折线图和曲线图
     *
     * @param headerList 列名
     * @param dataList 数据
     * @param title 文件名
     * @param xName x轴名称
     *
     * @param resp http响应
     *
     * @param colorList 图例颜色
     * @throws IOException
     */
    public static void createManyOneYaxisLineChart(LinkedList headerList, String [][]dataList,
                                               String title, String xName,
                                               HttpServletResponse resp,  JSONArray series,
                                                   LinkedList seriesName,LinkedList<String> leftY,
                                                   LinkedList<String> colorList) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "图表";
        FileOutputStream fileOut = null;
        try {
            XSSFSheet sheet = wb.createSheet(sheetName);
            //第一行
            Row row = sheet.createRow(0);
            Cell cell =null;
            for(int i=0;i<headerList.size();i++){
                cell = row.createCell(i);
                cell.setCellValue((String)headerList.get(i));
            }
            // 第二行数据
            for(int i=0;i<dataList.length;i++){
                row = sheet.createRow(i+1);
                for(int j=0;j<dataList[i].length;j++){
                    cell = row.createCell(j);
                    if(j==0){
                        cell.setCellValue(dataList[i][j]);
                    }else{
                        if(dataList[i][j]!=null)
                            cell.setCellValue(Double.parseDouble(dataList[i][j]));
                    }
                }
            }
            //创建一个画布
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            int rowFirstNum=01;
            int rowLastNum=26;
            for(int i=0;i<leftY.size();i++){
                //默认宽度(14-8)*12
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, rowFirstNum, headerList.size()+10, rowLastNum);


                //创建一个chart对象
                XSSFChart chart = drawing.createChart(anchor);
                //标题
                chart.setTitleText(title);
                //标题覆盖
                chart.setTitleOverlay(false);

                //图例位置
                XDDFChartLegend legend = chart.getOrAddLegend();
                legend.setPosition(LegendPosition.TOP);

                //分类轴标(X轴),标题位置
                XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(org.apache.poi.xddf.usermodel.chart.AxisPosition.BOTTOM);
                bottomAxis.setTitle(xName);
                //值(Y轴)轴,标题位置
                XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                leftAxis.setTitle(leftY.get(i));


                //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
                //分类轴标(X轴)数据，单元格范围位置
                XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));
                XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, i+1, i+1));

                //LINE：折线图，
                XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
                //图表加载数据，折线1
                XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, area);
                //TODO:折线图例标题
                series1.setTitle(headerList.get(i+1).toString(), null);
                //直线  false是折线图，true是曲线图
//                series1.setSmooth(isSmooth);

                //设置标记大小
                series1.setMarkerSize((short) 6);
                //设置标记样式
                //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
                series1.setMarkerStyle(MarkerStyle.NONE);

                //TODO:设置颜色特性,根据传来的值
                String color= colorList.get(i);
                byte []colorArray=hexToByteArray(color.substring(1,color.length()-1));


                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(colorArray));
                series1.setFillProperties(fill);
                XDDFLineProperties aaa=new XDDFLineProperties(fill);
                series1.setLineProperties(aaa);


                //绘制
                chart.plot(data);
                //TODO:关键语句，当只有一组y值时，必须设置，否则会出现把x每个值都当作图例的错误
                chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);
                 rowFirstNum=rowLastNum+2;
                 rowLastNum=rowFirstNum+25;
            }


            // 将输出写入excel文件
            String filename = "F://"+title+getTime()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
            fileOut.close();

            String fileName=title+getTime()+".xlsx";
            fileName = new String(fileName.getBytes(), "ISO8859-1");

            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            resp.flushBuffer();
            wb.write(resp.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }


    }



    /**
     * 生成多个图的单y轴，每个图多条数据的折线图和曲线图
     *
     *
     * 用于：
     * 1.井组生产现状
     *
     *
     * @param headerList 列名
     * @param group 数据
     * @param title 文件名
     * @param xName x轴名称
     *
     * @param resp http响应
     *
     * @param colorList 图例颜色
     * @throws IOException
     */
    public static void createManyPicOneYaxisManyLineChart(LinkedList headerList, LinkedList <HashMap> group,
                                                   String title, String xName,
                                                   HttpServletResponse resp,  JSONArray series,
                                                   LinkedList<String> leftY,
                                                   LinkedList<String> colorList) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "图表";
        FileOutputStream fileOut = null;
        try {
            XSSFSheet sheet = wb.createSheet(sheetName);
            //第一行
            Row row = sheet.createRow(0);
            Cell cell =null;
            for(int i=0;i<headerList.size();i++){
                cell = row.createCell(i);
                cell.setCellValue((String)headerList.get(i));
            }
            // 第二行数据
            int rowNum=1;
            int rowFirstNum=01;
            int rowLastNum=26;
            int firstRow=1;

            for(int i=0;i<group.size();i++){
                String name= (String) group.get(i).get("name");//获取井名
                String [][]dataList= (String[][]) group.get(i).get("dataList");

                for(int j=0;j<dataList.length;j++){
                    row = sheet.createRow(rowNum);
                    for(int k=0;k<dataList[j].length;k++){
                        cell = row.createCell(k);
                        if(k==0||k==1){
                            cell.setCellValue(dataList[j][k]);
                        }else{
                            if(dataList[j][k]!=null)
                                cell.setCellValue(Double.parseDouble(dataList[j][k]));
                        }
                    }
                    rowNum++;
                }
                //创建一个画布
                XSSFDrawing drawing = sheet.createDrawingPatriarch();

                //默认宽度(14-8)*12
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, rowFirstNum, headerList.size()+10, rowLastNum);

                //创建一个chart对象
                XSSFChart chart = drawing.createChart(anchor);
                //标题
                chart.setTitleText(title);
                //标题覆盖
                chart.setTitleOverlay(false);

                //图例位置
                XDDFChartLegend legend = chart.getOrAddLegend();
                legend.setPosition(LegendPosition.TOP);

                //分类轴标(X轴),标题位置
                XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(org.apache.poi.xddf.usermodel.chart.AxisPosition.BOTTOM);
                bottomAxis.setTitle(xName);
                //值(Y轴)轴,标题位置
                XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                leftAxis.setTitle(leftY.get(i));


                //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
                //分类轴标(X轴)数据，单元格范围位置
                XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(firstRow, firstRow+dataList.length-1, 1, 1));
//                XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, i+1, i+1));

                LinkedList yList=new LinkedList();
                //前2个数据，是井名和时间
                for(int j=2;j<dataList[0].length;j++){
                    XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, firstRow+dataList.length-1, j, j));
                    yList.add(area);
                }
                firstRow+=dataList.length;


                //LINE：折线图，
                XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

                for(int j=0;j<yList.size();j++){
                    //图表加载数据，折线1
                    XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, (XDDFNumericalDataSource<Double>)yList.get(j));

                    //TODO:折线图例标题
                    series1.setTitle(headerList.get(j+2).toString(), null);
                    //直线  false是折线图，true是曲线图
//                series1.setSmooth(isSmooth);

                    //设置标记大小
                    series1.setMarkerSize((short) 6);
                    //设置标记样式
                    //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
                    series1.setMarkerStyle(MarkerStyle.NONE);

                    //TODO:设置颜色特性,根据传来的值
                    String color= colorList.get(j);
                    byte []colorArray=hexToByteArray(color.substring(1,color.length()-1));


                    XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(colorArray));
                    series1.setFillProperties(fill);
                    XDDFLineProperties aaa=new XDDFLineProperties(fill);
                    series1.setLineProperties(aaa);
                }

                //绘制
                chart.plot(data);
                //TODO:关键语句，当只有一组y值时，必须设置，否则会出现把x每个值都当作图例的错误
                chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);
                rowFirstNum=rowLastNum+2;
                rowLastNum=rowFirstNum+25;
            }

            // 将输出写入excel文件
            String filename = "F://"+title+getTime()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
            fileOut.close();

            String fileName=title+getTime()+".xlsx";
            fileName = new String(fileName.getBytes(), "ISO8859-1");

            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            resp.flushBuffer();
            wb.write(resp.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }


    }



    /**
     * 生成多个图的单y轴，每个图多条数据的折线图和曲线图
     *
     *
     * 用于：
     * 1.全区开采
     *
     *
     * @param headerList 列名
     * @param group 数据
     * @param title 文件名
     * @param xName x轴名称
     *
     * @param resp http响应
     *
     *
     * @throws IOException
     */
    public static void createManyPicManyLineChart(LinkedList headerList, LinkedList <HashMap> group,
                                                          String title, String xName,
                                                          HttpServletResponse resp,  JSONArray series,
                                                          LinkedList<String> leftY
                                                          ) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "图表";
        FileOutputStream fileOut = null;
        try {
            XSSFSheet sheet = wb.createSheet(sheetName);
            //第一行
            Row row = sheet.createRow(0);
            Cell cell =null;
            for(int i=0;i<headerList.size();i++){
                cell = row.createCell(i);
                cell.setCellValue((String)headerList.get(i));
            }
            // 第二行数据
            int rowNum=1;
            int rowFirstNum=01;
            int rowLastNum=26;
            int firstCell=1;



                String[][] group1 = (String[][]) group.get(0).get("dataList");
                String[][] group2 = (String[][]) group.get(1).get("dataList");
                String[][] group3 = (String[][]) group.get(2).get("dataList");
                String[][] group4 = (String[][]) group.get(3).get("dataList");
                String[][] group5 = (String[][]) group.get(4).get("dataList");
                String[][] group6 = (String[][]) group.get(5).get("dataList");
                int dataNum=group1.length+(group2.length-1)+(group3.length-1)+(group4.length-1)+(group5.length-1)+(group6.length-1);
                //TODO:写入数据，可以优化
                for (int i = 0; i< group1.length; i++) {
                    row = sheet.createRow(i+1);
                    int cellNum=0;
                    for(int j=0;j<group1[0].length;j++){
                        cell = row.createCell(cellNum);
                        if (j == 0) {
                            cell.setCellValue(group1[i][j]);
                        } else {
                            if (group1[i][j] != null)
                                cell.setCellValue(Double.parseDouble(group1[i][j]));
                        }
                        cellNum++;
                    }
                    for(int j=1;j<group2[0].length;j++){
                        cell = row.createCell(cellNum);
                        if (group2[i][j] != null) {
                            cell.setCellValue(Double.parseDouble(group2[i][j]));
                        }
                        cellNum++;
                    }
                    for(int j=1;j<group3[0].length;j++){
                        cell = row.createCell(cellNum);
                        if (group3[i][j] != null) {
                            cell.setCellValue(Double.parseDouble(group3[i][j]));
                        }
                        cellNum++;
                    }
                    for(int j=1;j<group4[0].length;j++){
                        cell = row.createCell(cellNum);
                        if (group4[i][j] != null) {
                            cell.setCellValue(Double.parseDouble(group4[i][j]));
                        }
                        cellNum++;
                    }
                    for(int j=1;j<group5[0].length;j++){
                        cell = row.createCell(cellNum);
                        if (group5[i][j] != null) {
                            cell.setCellValue(Double.parseDouble(group5[i][j]));
                        }
                        cellNum++;
                    }
                    for(int j=1;j<group6[0].length;j++){
                        cell = row.createCell(cellNum);
                        if (group6[i][j] != null) {
                            cell.setCellValue(Double.parseDouble(group6[i][j]));
                        }
                        cellNum++;
                    }


                }

            for(int i=0;i<group.size();i++){
//                String name= (String) group.get(i).get("name");//获取井名
                String [][]dataList= (String[][]) group.get(i).get("dataList");

//                for(int j=0;j<dataList.length;j++){
//                    row = sheet.createRow(rowNum);
//                    for(int k=0;k<dataList[j].length;k++){
//                        cell = row.createCell(k);
//                        if(k==0){
//                            cell.setCellValue(dataList[j][k]);
//                        }else{
//                            if(dataList[j][k]!=null)
//                                cell.setCellValue(Double.parseDouble(dataList[j][k]));
//                        }
//                    }
//                    rowNum++;
//                }
//                //创建一个画布
                XSSFDrawing drawing = sheet.createDrawingPatriarch();

                //默认宽度(14-8)*12
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, rowFirstNum, headerList.size()+10, rowLastNum);

                //创建一个chart对象
                XSSFChart chart = drawing.createChart(anchor);
                //标题
                chart.setTitleText(title);
                //标题覆盖
                chart.setTitleOverlay(false);

                //图例位置
                XDDFChartLegend legend = chart.getOrAddLegend();
                legend.setPosition(LegendPosition.TOP);

                //分类轴标(X轴),标题位置
                XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(org.apache.poi.xddf.usermodel.chart.AxisPosition.BOTTOM);
                bottomAxis.setTitle(xName);
                //值(Y轴)轴,标题位置
                XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                leftAxis.setTitle(leftY.get(i));


                //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
                //分类轴标(X轴)数据，单元格范围位置
                XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));
//                XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, i+1, i+1));

                LinkedList yList=new LinkedList();

                for(int j=1;j<dataList[0].length;j++){
                    XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet,
                            new CellRangeAddress(1, dataList.length, firstCell, firstCell));
                    yList.add(area);
                    firstCell++;
                }

                LinkedList<String> seriesName=(LinkedList<String>)group.get(i).get("seriesName");
                LinkedList<String> colorList=(LinkedList<String>)group.get(i).get("colorList");
                //LINE：折线图，
                XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

                for(int j=0;j<yList.size();j++){
                    //图表加载数据，折线1
                    XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, (XDDFNumericalDataSource<Double>)yList.get(j));

                    //TODO:折线图例标题
                    series1.setTitle(seriesName.get(j), null);
                    //直线  false是折线图，true是曲线图
//                series1.setSmooth(isSmooth);

                    //设置标记大小
                    series1.setMarkerSize((short) 6);
                    //设置标记样式
                    //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
                    series1.setMarkerStyle(MarkerStyle.NONE);

                    //TODO:设置颜色特性,根据传来的值
                    if(colorList.get(j)!=null&&!"".equals(colorList.get(j))){
                        String color= colorList.get(j);
                        XDDFSolidFillProperties fill;
                        if(color.substring(0,1).equals("#")){
                            byte []colorArray=hexToByteArray(color.substring(1,color.length()-1));
                             fill = new XDDFSolidFillProperties(XDDFColor.from(colorArray));
                        }else{
                            color= colorList.get(j).toUpperCase();
                            PresetColor lineColor=PresetColor.valueOf(color);
                             fill = new XDDFSolidFillProperties(XDDFColor.from(lineColor));
                        }

//                        byte []colorArray=hexToByteArray(color.substring(1,color.length()-1));
//                        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(lineColor));
                        series1.setFillProperties(fill);
                        XDDFLineProperties aaa=new XDDFLineProperties(fill);
                        series1.setLineProperties(aaa);
                    }
                }
                //绘制
                chart.plot(data);
                //TODO:关键语句，当只有一组y值时，必须设置，否则会出现把x每个值都当作图例的错误
                chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);
                rowFirstNum=rowLastNum+2;
                rowLastNum=rowFirstNum+25;

            }



            // 将输出写入excel文件
            String filename = "F://"+title+getTime()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
            fileOut.close();

            String fileName=title+getTime()+".xlsx";
            fileName = new String(fileName.getBytes(), "ISO8859-1");

            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            resp.flushBuffer();
            wb.write(resp.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }


    }


    public static void createOneYaxisBarChart(LinkedList headerList,String [][]dataList,
                               String title,String xName,String yName,
                               HttpServletResponse resp,LinkedList colorList) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "图表";
        FileOutputStream fileOut = null;
        try {
            XSSFSheet sheet = wb.createSheet(sheetName);
            //第一行表头
            Row row = sheet.createRow(0);
            Cell cell =null;
            for(int i=0;i<headerList.size();i++){
                cell = row.createCell(i);
                cell.setCellValue((String)headerList.get(i));
            }
            // 第二行数据
            for(int i=0;i<dataList.length;i++){
                row = sheet.createRow(i+1);
                for(int j=0;j<dataList[i].length;j++){
                    cell = row.createCell(j);
                    if(j==0){
                        cell.setCellValue(dataList[i][j]);
                    }else{
                        cell.setCellValue(Double.parseDouble(dataList[i][j]));
                    }
                }
            }

            //创建一个画布
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            //前四个默认0，[0,5]：从0列5行开始;[7,26]:宽度7个单元格，26向下扩展到26行
            //默认宽度(14-8)*12
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, 1, headerList.size()+dataList.length/2, 26);

            //创建一个chart对象
            XSSFChart chart = drawing.createChart(anchor);
            //标题
            chart.setTitleText(title);
            //标题覆盖
            chart.setTitleOverlay(false);

            //图例位置
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP);

            //分类轴标(X轴),标题位置
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(org.apache.poi.xddf.usermodel.chart.AxisPosition.BOTTOM);
            bottomAxis.setTitle(xName);
            //值(Y轴)轴,标题位置
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle(yName);

            //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
            //分类轴标(X轴)数据，单元格范围位置[0, 0]到[0, 6]
            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));



            LinkedList yList=new LinkedList();
            for(int i=1;i<dataList[0].length;i++){
                XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, i, i));
                yList.add(area);
            }



            //LINE：折线图，BAR:柱状图
//                XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
            XDDFBarChartData data = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            data.setBarDirection(BarDirection.COL);//设置柱状图方向，COL竖着，BAR横着
            data.setVaryColors(false);
            leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);//设置使首尾的柱状图不会缺失一半图案

            for(int i=0;i<yList.size();i++){
                //图表加载数据，折线1
                XDDFBarChartData.Series series1 = (XDDFBarChartData.Series) data.addSeries( countries,(XDDFNumericalDataSource<Double>)yList.get(i));
                //折线图例标题
                series1.setTitle(headerList.get(i+1).toString(), null);

                //设置颜色特性
                //TODO:枚举类---------设置颜色特性,根据传来的值
                String color= (String) colorList.get(i);
                PresetColor lineColor=PresetColor.valueOf(color);

                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(lineColor));
                series1.setFillProperties(fill);
            }

            //绘制
            chart.plot(data);

            // 将输出写入excel文件
            String filename = "F://"+title+getTime()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);

            String fileName=title+getTime()+".xlsx";
            fileName = new String(fileName.getBytes(), "ISO8859-1");

            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-disposition", "attachment;filename=" +URLEncoder.encode(fileName, "utf-8"));
            resp.flushBuffer();
            wb.write(resp.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }

    }


    /**
     * 创建曲线和散点图
     *
     * 用于：
     * 1.递减曲线
     * 2.水驱曲线
     *
     * @param headerList 列名
     * @param dataList 数据
     * @param title 文件名
     * @param xName x轴
     * @param yName y轴
     * @param resp 响应
     * @param isSmooth 是否平滑
     * @param series 图例属性json数组
     * @throws IOException
     */
    public static void createOneYaxisLinePointChart(LinkedList headerList, String [][]dataList,
                                                    String title, String xName, String yName,
                                                    HttpServletResponse resp, boolean isSmooth, JSONArray series) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "图表";
        FileOutputStream fileOut = null;
        try {
            XSSFSheet sheet = wb.createSheet(sheetName);

            //第一行
            Row row = sheet.createRow(0);
            Cell cell =null;
            for(int i=0;i<headerList.size();i++){
                cell = row.createCell(i);
                cell.setCellValue((String)headerList.get(i));

            }
            // 第二行数据
            LinkedList<String> allString=new LinkedList<>();
            for(int i=0;i<dataList.length;i++){
                row = sheet.createRow(i+1);
                for(int j=0;j<dataList[i].length;j++){
                    cell = row.createCell(j);
                    if(j==0){
                        allString.add(dataList[i][j]);
                        cell.setCellValue(dataList[i][j]);
                    }else{
                        //j=1为pre，j=2是q
                        if(dataList[i][j]==null||"".equals(dataList[i][j])){
//                            cell.setCellValue("");
                        }else {
                            //q非空的时候才有效
                            cell.setCellValue(getDoubleValue(dataList[i][j]));
//                            Double.parseDouble(dataList[i][j])
                        }
                    }
                }
            }

            //创建一个画布
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            //前四个默认0，[0,5]：从0列5行开始;[7,26]:宽度7个单元格，26向下扩展到26行
            //默认宽度(14-8)*12
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, 1, headerList.size()+9, 26);

            //创建一个chart对象
            XSSFChart chart = drawing.createChart(anchor);
            //标题
            chart.setTitleText(title);
            //标题覆盖
            chart.setTitleOverlay(false);

            //图例位置
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP);

            //分类轴标(X轴),标题位置
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(org.apache.poi.xddf.usermodel.chart.AxisPosition.BOTTOM);
            bottomAxis.setTitle(xName);
            //值(Y轴)轴,标题位置
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle(yName);



            //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
            //分类轴标(X轴)数据，单元格范围位置
            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));

//            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromArray(allString.toArray(new String [allString.size()]));



            LinkedList yList=new LinkedList();
            for(int i=1;i<dataList[0].length;i++){
                XDDFNumericalDataSource<Double> area=null;
                area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, i, i));
                yList.add(area);
            }

            //数据1，单元格范围位置[1, 0]到[1, 6]
//            XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 1, 1));

            //LINE：折线图，Scatter散点图
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
            XDDFScatterChartData data2 = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);

            for(int i=0;i<yList.size();i++){
                //图表加载数据
                String type=series.getJSONObject(i).getString("type");
//                XDDFChartData.Series series1=null;
//                XDDFLineChartData.Series series1 = null;
                if(type.equals("line")){
                    XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, (XDDFNumericalDataSource<Double>)yList.get(i));
                    //TODO:折线图例标题
                    series1.setTitle(headerList.get(i+1).toString(), null);
                    //直线  false是折线图，true是曲线图
                    series1.setSmooth(isSmooth);

                    //设置标记大小
                    series1.setMarkerSize((short) 6);
                    //设置标记样式
                    //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
                    series1.setMarkerStyle(MarkerStyle.NONE);

                    //TODO:枚举类---------设置颜色特性,根据传来的值
                    String color=null;
                    color=getColor(series.getJSONObject(i));
//                    try {
//                         color = series.getJSONObject(i).getJSONObject("markPoint").getJSONArray("data").
//                                getJSONObject(0).getJSONObject("itemStyle").getString("color");
//                    }catch (Exception e){
//                        color=
//                    }
                    if(color!=null&&!"".equals(color)){

                        XDDFSolidFillProperties fill;
                        if(color.substring(0,1).equals("#")){
                            byte []colorArray=hexToByteArray(color.substring(1,color.length()-1));
                            fill = new XDDFSolidFillProperties(XDDFColor.from(colorArray));
                        }else{
                            color= color.toUpperCase();
                            PresetColor lineColor=PresetColor.valueOf(color);
                            fill = new XDDFSolidFillProperties(XDDFColor.from(lineColor));
                        }

                        series1.setFillProperties(fill);
                        XDDFLineProperties lineProperties=new XDDFLineProperties(fill);
                        series1.setLineProperties(lineProperties);
                    }
                }
                if(type.equals("scatter")){

                    XDDFScatterChartData.Series series1=
                            (XDDFScatterChartData.Series) data2.addSeries(countries, (XDDFNumericalDataSource<Double>)yList.get(i));
                    //TODO:折线图例标题
                    series1.setTitle(headerList.get(i+1).toString(), null);
                    //直线  false是折线图，true是曲线图
                    series1.setSmooth(false);
                    series1.setMarkerStyle(MarkerStyle.CIRCLE);
                    //设置标记大小
                    series1.setMarkerSize((short) 6);
                    //设置标记样式
                    //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
//                    series1.setMarkerStyle(MarkerStyle.NONE);

                    //TODO:枚举类---------设置颜色特性,根据传来的值
//                    String color= series.getJSONObject(i).getString("color");
//                    PresetColor lineColor=PresetColor.valueOf(color);

//                    XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(lineColor));
//                    series1.setFillProperties(fill);
//                    XDDFLineProperties lineProperties=new XDDFLineProperties(fill);
//                    series1.setLineProperties(lineProperties);
                    setLineNoFill(series1);
                }

            }

            //绘制
            chart.plot(data);
            chart.plot(data2);
            //TODO:关键语句，当只有一组y值时，必须设置，否则会出现把x每个值都当作图例的错误
            chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);

            // 将输出写入excel文件
            String filename = "F://"+title+getTime()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
            fileOut.close();

            String fileName=title+getTime()+".xlsx";
            fileName = new String(fileName.getBytes(), "ISO8859-1");

            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            resp.flushBuffer();
            wb.write(resp.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }


    }


    /**
     * 创建双y轴图
     *
     * @param headerList
     * @param dataList
     * @param title
     * @param xName
     * @param resp
     * @param series
     * @param leftY
     * @param rightY
     * @param seriesName
     * @throws IOException
     */
    public static void createTwoChart(LinkedList headerList, String [][]dataList,
                                      String title, String xName,
                                      HttpServletResponse resp,  JSONArray series,LinkedList<String> leftY,
                                      LinkedList<String> rightY,LinkedList<String> seriesName) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "图表";
        FileOutputStream fileOut = null;
        try {
            XSSFSheet sheet = wb.createSheet(sheetName);
            //第一行
            Row row = sheet.createRow(0);
            Cell cell =null;
            for(int i=0;i<headerList.size();i++){
                cell = row.createCell(i);
                cell.setCellValue((String)headerList.get(i));
            }
            // 第二行数据
            LinkedList<String> allString=new LinkedList<>();
            for(int i=0;i<dataList.length;i++){
                row = sheet.createRow(i+1);
                for(int j=0;j<dataList[i].length;j++){
                    cell = row.createCell(j);
                    if(j==0){
                        allString.add(dataList[i][j]);
                        cell.setCellValue(dataList[i][j]);
                    }else{
                        if(dataList[i][j]==null||"".equals(dataList[i][j])){
                        }else {
                            //非空的时候才有效
                            cell.setCellValue(Double.parseDouble(dataList[i][j]));
                        }
                    }
                }
            }

            // 创建一个画布
//            XSSFDrawing drawing = sheet.createDrawingPatriarch();

            // 默认宽度(14-8)*12
            logger.info("双y循环画图次数"+((dataList[0].length-1)/2-1));
            int rowFirstNum=1;
            int rowLastNum=26;
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            for(int i=0;i<leftY.size();i++){
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, rowFirstNum, headerList.size()+10, rowLastNum);            // 创建一个chart对象
                XSSFChart chart = drawing.createChart(anchor);
                // 标题
                chart.setTitleText(title);
                // 标题覆盖
                chart.setTitleOverlay(false);

                /*双Y轴*/
                // X轴
                XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                bottomAxis.setTitle(xName);
                // bottomAxis.setVisible(false);// 隐藏X轴

                // 左Y轴
                XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
                // 左Y轴和X轴交叉点在X轴0点位置
                leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
                leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
                // 构建坐标轴
                leftAxis.crossAxis(bottomAxis);
                bottomAxis.crossAxis(leftAxis);
                // 设置左Y轴最大值
//                leftAxis.setMaximum(20000000);
                // leftAxis.setVisible(false);// 隐藏Y轴
                leftAxis.setTitle(leftY.get(i));

                // 右Y轴
                XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.RIGHT);
                // 右Y轴和X轴交叉点在X轴最大值位置
                rightAxis.setCrosses(AxisCrosses.MAX);
                rightAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
                // 构建坐标轴
                rightAxis.crossAxis(bottomAxis);
                bottomAxis.crossAxis(rightAxis);
                // 设置左Y轴最大值
//                rightAxis.setMaximum(40000000);
                // rightAxis.setVisible(false);// 隐藏Y轴
                rightAxis.setTitle(rightY.get(i));

                // 图例位置
                XDDFChartLegend legend = chart.getOrAddLegend();
                legend.setPosition(LegendPosition.TOP);

                // CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
                // X轴数据
                XDDFDataSource<String> xdatas = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));
                // 左Y轴数据
                XDDFNumericalDataSource<Double> leftYdatas = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 2*i+1, 2*i+1));
                // 右Y轴数据
                XDDFNumericalDataSource<Double> rightYdatas = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 2*i+2, 2*i+2));


                // LINE：折线图，
                XDDFLineChartData line1 = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, rightAxis);
                // 图表加载数据，折线1
                XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) line1.addSeries(xdatas, rightYdatas);
                // 折线图例标题
                series1.setTitle(seriesName.get(i*2+1), null);
                // 直线
                series1.setSmooth(false);
                // 设置标记大小
                series1.setMarkerSize((short) 6);
                // 设置标记样式，星星
                series1.setMarkerStyle(MarkerStyle.NONE);

                // 绘制
//                System.out.println(chart.getCTChart());
                chart.plot(line1);

                // LINE：折线图，
                XDDFLineChartData line2 = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
                // 图表加载数据，折线1
                XDDFLineChartData.Series series2 = (XDDFLineChartData.Series) line2.addSeries(xdatas, leftYdatas);
                // 折线图例标题
                series2.setTitle(seriesName.get(i*2), null);
                // 直线
                series2.setSmooth(false);
                // 设置标记大小
                series2.setMarkerSize((short) 6);
                // 设置标记样式，星星
                series2.setMarkerStyle(MarkerStyle.NONE);
                // 绘制
                chart.plot(line2);

                // 填充与线条-标记-填充-依数据点着色（取消勾选）
                chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);


                rowFirstNum=rowLastNum+3;
                rowLastNum=rowFirstNum+25;
            }
            // 将输出写入excel文件
            String filename = "F://"+title+getTime()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);

            String fileName=title+getTime()+".xlsx";
            fileName = new String(fileName.getBytes(), "ISO8859-1");

            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            resp.flushBuffer();
            wb.write(resp.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }

    }






    /**
     * 散点图去除连线属性
     *
     *
     * @param series 单条散点图单位
     */
    private static void setLineNoFill(XDDFScatterChartData.Series series) {
        XDDFNoFillProperties noFillProperties = new XDDFNoFillProperties();
        XDDFLineProperties lineProperties = new XDDFLineProperties();
        lineProperties.setFillProperties(noFillProperties);
        XDDFShapeProperties shapeProperties = series.getShapeProperties();
        if (shapeProperties == null) shapeProperties = new XDDFShapeProperties();
        shapeProperties.setLineProperties(lineProperties);
        series.setShapeProperties(shapeProperties);
    }


    /**
     * 16进制转byte数组，用于颜色转换
     *
     *
     * @param inHex
     * @return
     */
    public static byte[] hexToByteArray(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            //奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {
            //偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2){
            result[j]=hexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }


    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }

    /**
     * 获取格式时间戳，用于生成文件的文件名
     *
     * @return
     */
    public static String getTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//        System.out.println(format.format(date));
        return format.format(date);
    }

    public static double getDoubleValue(String data){
        try{
            return Double.parseDouble(data);
        }catch (Exception e){
            return 0;
        }
    }

    public static String getColor(JSONObject jsonObject){
        try{
            return jsonObject.getJSONObject("markPoint").getJSONArray("data").
                    getJSONObject(0).getJSONObject("itemStyle").getString("color");
        }catch (Exception e){
            return "";
        }
    }

}
