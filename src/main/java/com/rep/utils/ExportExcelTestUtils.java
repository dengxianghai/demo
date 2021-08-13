//package com.rep.utils;
//
//import com.alibaba.fastjson.JSONArray;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xddf.usermodel.*;
//import org.apache.poi.xddf.usermodel.chart.*;
//import org.apache.poi.xssf.usermodel.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//
///**
// * ------这是一个测试的类
// *
// *
// */
//public class ExportExcelTestUtils {
//
//    private static final Logger logger = LoggerFactory.getLogger(ExportExcelTestUtils.class);
//
//
//    /**
//     * 创建曲线和散点图
//     *
//     * 用于：
//     * 1.递减曲线
//     * 2.水驱曲线
//     *
//     * @param headerList 列名
//     * @param dataList 数据
//     * @param resp 响应
//     * @throws IOException
//     */
//    public static void createOneYaxisLinePointChart(LinkedList<String> headerList, String [][]dataList,
//                                                    LinkedList<HashMap> group,
//                                                    HttpServletResponse resp) throws IOException {
//
//        XSSFWorkbook wb = new XSSFWorkbook();
//        String sheetName = "图表";
//        FileOutputStream fileOut = null;
//        try {
//            XSSFSheet sheet = wb.createSheet(sheetName);
//
//            //把数据做成表格-------------start
//            //第一行
//            Row row = sheet.createRow(0);
//            Cell cell =null;
//            for(int i=0;i<headerList.size();i++){
//                cell = row.createCell(i);
//                cell.setCellValue(headerList.get(i));
//
//            }
//            // 第二行数据
//            for(int i=0;i<dataList.length;i++){
//                row = sheet.createRow(i+1);
//                for(int j=0;j<dataList[i].length;j++){
//                    cell = row.createCell(j);
//                    if(j==0){
//                        cell.setCellValue(dataList[i][j]);
//                    }else{
//                        if(dataList[i][j]==null||"".equals(dataList[i][j])){
//
//                        }else {
//                            cell.setCellValue(getDoubleValue(dataList[i][j]));
//                        }
//                    }
//                }
//            }
//            //把数据做成表格-------------end
//
//
//
//            //创建一个画布
//            XSSFDrawing drawing = sheet.createDrawingPatriarch();
//            //默认宽度(14-8)*12
//            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, 1, headerList.size()+dataList.length, 26);
//
//
//            for(int i=0;i<group.size();i++){
//                HashMap map=group.get(i);
//
////                picData
//                LinkedList<String> colorList= (LinkedList) map.get("colorList");
//                LinkedList<String> seriesList= (LinkedList) map.get("seriesList");
//                String yName= (String) map.get("yName");
//                String xName= (String) map.get("xName");
//                String title= (String) map.get("title");
//                LinkedList<String> lineList= (LinkedList) map.get("lineList");
//            }
//
//            //创建一个chart对象
//            XSSFChart chart = drawing.createChart(anchor);
//            //标题
//            chart.setTitleText(title);
//            //标题覆盖
//            chart.setTitleOverlay(false);
//
//            //图例位置
//            XDDFChartLegend legend = chart.getOrAddLegend();
//            legend.setPosition(LegendPosition.TOP);
//
//            //分类轴标(X轴),标题位置
//            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(org.apache.poi.xddf.usermodel.chart.AxisPosition.BOTTOM);
//            bottomAxis.setTitle(xName);
//            //值(Y轴)轴,标题位置
//            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
//            leftAxis.setTitle(yName);
//
//
//
//            //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
//            //分类轴标(X轴)数据，单元格范围位置
//            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));
//
////            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromArray(allString.toArray(new String [allString.size()]));
//
//
//
//            LinkedList yList=new LinkedList();
//            for(int i=1;i<dataList[0].length;i++){
//                XDDFNumericalDataSource<Double> area=null;
//                area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, i, i));
//                yList.add(area);
//            }
//
//            //数据1，单元格范围位置[1, 0]到[1, 6]
////            XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 1, 1));
//
//            //LINE：折线图，Scatter散点图
//            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
//            XDDFScatterChartData data2 = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);
//
//            for(int i=0;i<yList.size();i++){
//                //图表加载数据
//                String type=series.getJSONObject(i).getString("type");
////                XDDFChartData.Series series1=null;
////                XDDFLineChartData.Series series1 = null;
//                if(type.equals("line")){
//                    XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, (XDDFNumericalDataSource<Double>)yList.get(i));
//                    //TODO:折线图例标题
//                    series1.setTitle(headerList.get(i+1).toString(), null);
//                    //直线  false是折线图，true是曲线图
//                    series1.setSmooth(isSmooth);
//
//                    //设置标记大小
//                    series1.setMarkerSize((short) 6);
//                    //设置标记样式
//                    //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
//                    series1.setMarkerStyle(MarkerStyle.NONE);
//
//                    //TODO:枚举类---------设置颜色特性,根据传来的值
//                    String color=null;
//                    color=getColor(series.getJSONObject(i));
////                    try {
////                         color = series.getJSONObject(i).getJSONObject("markPoint").getJSONArray("data").
////                                getJSONObject(0).getJSONObject("itemStyle").getString("color");
////                    }catch (Exception e){
////                        color=
////                    }
//                    if(color!=null&&!"".equals(color)){
//
//                        XDDFSolidFillProperties fill;
//                        if(color.substring(0,1).equals("#")){
//                            byte []colorArray=hexToByteArray(color.substring(1,color.length()-1));
//                            fill = new XDDFSolidFillProperties(XDDFColor.from(colorArray));
//                        }else{
//                            color= color.toUpperCase();
//                            PresetColor lineColor=PresetColor.valueOf(color);
//                            fill = new XDDFSolidFillProperties(XDDFColor.from(lineColor));
//                        }
//
//                        series1.setFillProperties(fill);
//                        XDDFLineProperties lineProperties=new XDDFLineProperties(fill);
//                        series1.setLineProperties(lineProperties);
//                    }
//                }
//                if(type.equals("scatter")){
//
//                    XDDFScatterChartData.Series series1=
//                            (XDDFScatterChartData.Series) data2.addSeries(countries, (XDDFNumericalDataSource<Double>)yList.get(i));
//                    //TODO:折线图例标题
//                    series1.setTitle(headerList.get(i+1).toString(), null);
//                    //直线  false是折线图，true是曲线图
//                    series1.setSmooth(false);
//                    series1.setMarkerStyle(MarkerStyle.CIRCLE);
//                    //设置标记大小
//                    series1.setMarkerSize((short) 6);
//                    //设置标记样式
//                    //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
////                    series1.setMarkerStyle(MarkerStyle.NONE);
//
//                    //TODO:枚举类---------设置颜色特性,根据传来的值
////                    String color= series.getJSONObject(i).getString("color");
////                    PresetColor lineColor=PresetColor.valueOf(color);
//
////                    XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(lineColor));
////                    series1.setFillProperties(fill);
////                    XDDFLineProperties lineProperties=new XDDFLineProperties(fill);
////                    series1.setLineProperties(lineProperties);
//                    setLineNoFill(series1);
//                }
//
//
//            }
//
//
//            //绘制
//            chart.plot(data);
//            chart.plot(data2);
//            //TODO:关键语句，当只有一组y值时，必须设置，否则会出现把x每个值都当作图例的错误
//            chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);
//
//            // 将输出写入excel文件
//            String filename = "F://"+title+getTime()+".xlsx";
//            fileOut = new FileOutputStream(filename);
//            wb.write(fileOut);
//            fileOut.close();
//
//            String fileName=title+getTime()+".xlsx";
//            fileName = new String(fileName.getBytes(), "ISO8859-1");
//
//            resp.setContentType("application/octet-stream");
//            resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
//            resp.flushBuffer();
//            wb.write(resp.getOutputStream());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            wb.close();
//            if (fileOut != null) {
//                fileOut.close();
//            }
//        }
//
//
//    }
//
//    public static double getDoubleValue(String data){
//        try{
//            return Double.parseDouble(data);
//        }catch (Exception e){
//            return 0;
//        }
//    }
//
//
//    /**
//     * 获取格式时间戳，用于生成文件的文件名
//     *
//     * @return
//     */
//    public static String getTime(){
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
////        System.out.println(format.format(date));
//        return format.format(date);
//    }
//    /**
//     * 散点图去除连线属性
//     * @param series 单条散点图单位
//     */
//    private static void setLineNoFill(XDDFScatterChartData.Series series) {
//        XDDFNoFillProperties noFillProperties = new XDDFNoFillProperties();
//        XDDFLineProperties lineProperties = new XDDFLineProperties();
//        lineProperties.setFillProperties(noFillProperties);
//        XDDFShapeProperties shapeProperties = series.getShapeProperties();
//        if (shapeProperties == null) shapeProperties = new XDDFShapeProperties();
//        shapeProperties.setLineProperties(lineProperties);
//        series.setShapeProperties(shapeProperties);
//    }
//}
