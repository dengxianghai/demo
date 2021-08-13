package com.excel.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.impl.Grid;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 生成Excel的接口
 *
 * @author
 * @date
 *
 */
@RestController
@RequestMapping("generateExcel")
public class GenerateExcel {


    private static final Logger logger = LoggerFactory.getLogger(GenerateExcel.class);

    @RequestMapping("buildExcel")
    public void buildExcel(HttpServletRequest req, HttpServletResponse resp,
                            @RequestBody JSONObject param) throws Exception {
        System.out.println("----------------------进入接口------------------");

        //获取xy轴数据
        JSONObject dataSet=param.getJSONObject("dataset");
        JSONArray source=dataSet.getJSONArray("source");


        //TODO:测试写法，后期要根据json参数汇总，y值的个数来进行初始化
        //把json数据进行转换
        String [][]dataList=new String[source.size()][3];
        for(int i=0;i<source.size();i++){

            dataList [i][0]=source.getJSONObject(i).getString("date");
            dataList [i][1]=source.getJSONObject(i).getString("pre");
            dataList [i][2]=source.getJSONObject(i).getString("q");
        }

        //获取x轴名字
        JSONArray xAxis=param.getJSONArray("xAxis");
        String xName=xAxis.getJSONObject(0).getString("name");
        //获取y轴名字
        JSONArray yAxis=param.getJSONArray("yAxis");
        String yName=yAxis.getJSONObject(0).getString("name");

        //获取表格列名
        LinkedList headerList=new LinkedList();
        headerList.add(xName);
        JSONArray series=param.getJSONArray("series");
        for(int i=0;i<series.size();i++){
            headerList.add(series.getJSONObject(i).getString("name"));
        }
//        headerList.add(xName);
//        headerList.add(yName);
        String title="生产现状";

        //获取图线类型
        String type=param.getString("type");
        boolean isSmooth;
        switch (type){
            case "smoothhLine":
                isSmooth=true;//曲线图
                createOneYaxisLineChart( headerList,dataList,
                        title,xName,yName,
                        resp,isSmooth);
                break;
            case "line"://折线图
                isSmooth=false;
                createOneYaxisLineChart( headerList,dataList,
                        title,xName,yName,
                        resp,isSmooth);
                break;
            case "bar"://柱状图
                createBarChart( headerList,dataList,
                        title,xName,yName,
                        resp);
                break;
            case "twoLine"://
                createTwoYaxisLineChart( headerList,dataList,
                        title,xName,yName,
                        resp);
                break;
        }

    }



    /**
     * 折线图（曲线图）
     *    （一个y轴）
     * @param headerList
     * @param dataList
     * @param title
     * @param xName
     * @param yName
     * @param resp
     * @param isSmooth  false是折线图，true是曲线图
     * @throws IOException
     */
    public void createOneYaxisLineChart(LinkedList headerList,String [][]dataList,
                                String title,String xName,String yName,
                                HttpServletResponse resp,boolean isSmooth) throws IOException {

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
                String b="aaaa";
                Class<PresetColor> pp = null;
                PresetColor lineColor=(PresetColor)Enum.valueOf((pp ), b);;
//                (PresetColor)Enum.valueOf((PresetColor), "Email", false);

                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED));
                series1.setFillProperties(fill);
                XDDFLineProperties aaa=new XDDFLineProperties(fill);
                series1.setLineProperties(aaa);
            }
//            //图表加载数据，折线1
//            XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, area);
//            //折线图例标题
//            series1.setTitle(headerList.get(1).toString(), null);
//            //直线  false是折线图，true是曲线图
//            series1.setSmooth(true);
//
//            //设置标记大小
//            series1.setMarkerSize((short) 6);
//            //设置标记样式
//            //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
//            series1.setMarkerStyle(MarkerStyle.NONE);
//
//            //设置颜色特性
//            XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED));
//            series1.setFillProperties(fill);
//            XDDFLineProperties aaa=new XDDFLineProperties(fill);
//            series1.setLineProperties(aaa);

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
     * 折线图
     * （双y轴）
     * @param headerList
     * @param dataList
     * @param title
     * @param xName
     * @param yName
     * @throws IOException
     */
    public void createTwoYaxisLineChart(LinkedList headerList,String [][]dataList,
                                        String title,String xName,String yName,
                                        HttpServletResponse resp) throws IOException {
        logger.info("折线图（双y轴）");
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
//            //值(Y轴)轴,标题位置
//            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
//            leftAxis.setTitle(yName);

            // 左Y轴
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            // 左Y轴和X轴交叉点在X轴0点位置
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
            leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
            // 构建坐标轴
            leftAxis.crossAxis(bottomAxis);
            bottomAxis.crossAxis(leftAxis);
            // 设置左Y轴最大值
//            leftAxis.setMaximum(20000000);
            // leftAxis.setVisible(false);// 隐藏Y轴
            leftAxis.setTitle("左Y轴标题");

            // 右Y轴
            XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.RIGHT);
            // 右Y轴和X轴交叉点在X轴最大值位置
            rightAxis.setCrosses(AxisCrosses.MAX);
            rightAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
            // 构建坐标轴
            rightAxis.crossAxis(bottomAxis);
            bottomAxis.crossAxis(rightAxis);
            // 设置左Y轴最大值
//            rightAxis.setMaximum(40000000);
            // rightAxis.setVisible(false);// 隐藏Y轴
            rightAxis.setTitle("右Y轴标题");





            //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
            //分类轴标(X轴)数据，单元格范围位置
            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));

//            LinkedList yList=new LinkedList();
//            for(int i=1;i<dataList[0].length;i++){
//                XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, i, i));
//                yList.add(area);
//            }


            XDDFNumericalDataSource<Double> leftYdatas = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 1, 1));
            // 右Y轴数据，单元格范围位置[2, 0]到[2, 6]
            XDDFNumericalDataSource<Double> rightYdatas = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 2, 2));


            //LINE：折线图，
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, rightAxis);

//            for(int i=0;i<yList.size();i++){
                //图表加载数据，折线1   (XDDFNumericalDataSource<Double>)yList.get(i)
                XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, rightYdatas);
                //折线图例标题
                series1.setTitle("右边y", null);
                //直线  false是折线图，true是曲线图
                series1.setSmooth(true);
                //设置标记大小
                series1.setMarkerSize((short) 6);
                //设置标记样式
                //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
                series1.setMarkerStyle(MarkerStyle.NONE);
                //设置颜色特性
                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED));
                series1.setFillProperties(fill);
                XDDFLineProperties aaa=new XDDFLineProperties(fill);
                series1.setLineProperties(aaa);
//            }

            XDDFLineChartData data2 = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

//            for(int i=0;i<yList.size();i++){
            //图表加载数据，折线1   (XDDFNumericalDataSource<Double>)yList.get(i)
            XDDFLineChartData.Series series2= (XDDFLineChartData.Series) data.addSeries(countries, leftYdatas);
            //折线图例标题
            series2.setTitle("左边y", null);
            //直线  false是折线图，true是曲线图
            series2.setSmooth(true);
            //设置标记大小
            series2.setMarkerSize((short) 6);
            //设置标记样式
            //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
            series2.setMarkerStyle(MarkerStyle.NONE);
            //设置颜色特性
            XDDFSolidFillProperties fill2 = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED));
            series2.setFillProperties(fill2);
            XDDFLineProperties aaa2=new XDDFLineProperties(fill);
            series2.setLineProperties(aaa2);

            //绘制
            chart.plot(data);
            // 将输出写入excel文件
            String filename = "F://"+title+System.currentTimeMillis()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
            fileOut.close();

            String fileName=title+".xlsx";
            fileName = new String(fileName.getBytes(), "ISO8859-1");

            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-disposition", "attachment;filename=" +URLEncoder.encode(fileName, "utf-8"));
            resp.flushBuffer();
            wb.write(resp.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }


    }


    /**
     * 生成柱状图
     * @param headerList
     * @param dataList
     * @param title
     * @param xName
     * @param yName
     * @throws IOException
     */
    public void createBarChart(LinkedList headerList,String [][]dataList,String title,String xName,String yName,HttpServletResponse resp) throws IOException {

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
                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GRAY));
                series1.setFillProperties(fill);
            }

            //设置标记大小
//                series1.setMarkerSize((short) 6);
            //设置标记样式，星星
            ////NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
//                series1.setMarkerStyle(MarkerStyle.STAR);

            //绘制
            chart.plot(data);

            // 将输出写入excel文件
            String filename = "F://"+title+System.currentTimeMillis()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);

            String fileName=title+".xlsx";
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



    @RequestMapping("createExcel")
    public void createExcel(HttpServletRequest req, HttpServletResponse resp,
                            @RequestBody JSONObject param

                            ) throws Exception {
//        JSONObject param=JSONObject.parseObject(req.getParameter("param"));

        JSONObject staticParam=param.getJSONObject("staticParam");
        JSONArray dynamicParameters=param.getJSONArray("dynamicParameters");
        //@RequestParam(value = "headers" ,required=false) List<String> headers
        //String type, JSONObject staticParam, JSONArray dynamicParameters,
        System.out.println("进入接口");
//        System.out.println(type);
//        System.out.println("headers是大小："+headers.size());
//        System.out.println("headers是否为空："+headers==null);
        List<Map<String, String>> exportDatas = new ArrayList<>();

        System.out.println("--------------");


//        for (int i=0;i<10;i++) {
//            Map<String,String> map=new HashMap<>();
////            map.put("产品名称", jsonArray.getJSONObject(i).getString("s"));
//            map.put("x", "999");
//            map.put("y", "555");
//            map.put("z", "333");
//            map.put("q","2");
//            exportDatas.add(map);
//        }

        List<String> headers=new LinkedList<>();
        headers.add("movingLiquidSurface");//动液面
        headers.add("waterContent");//含水率
        headers.add("staticLiquidSurface");//静液面

        List<String> staticHeaders=new LinkedList<>();
        staticHeaders.add("静态参数");
        staticHeaders.add("油藏中深");
        staticHeaders.add("水密度");
        staticHeaders.add("油密度");

        List<String> movingHeaders=new LinkedList<>();
        movingHeaders.add("动态数据");
        movingHeaders.add("动液面m");
        movingHeaders.add("含水率");
        movingHeaders.add("静液面m");
        movingHeaders.add("油藏静压");
        movingHeaders.add("井底流压");


        generateExcels(dynamicParameters,headers, resp,staticParam,staticHeaders,movingHeaders);
        //导出
//        exportExcel(req,resp,headers,exportDatas);
//        exportExcel(headers,"test");

    }


    @RequestMapping
    public void generateExcels(JSONArray dynamicParameters,List<String> headers,
                                       HttpServletResponse response,JSONObject staticParam,
                               List<String> staticHeaders,List<String> movingHeaders) throws Exception{
        // 1.创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();

        // 2.定义样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(IndexedColors.PINK.getIndex());
//        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // 3.创建工作表
        XSSFSheet sheet = workbook.createSheet("工作表一");
//        // 4.创建行
//        XSSFRow row = sheet.createRow(0);
//        // 5.向单元格赋值，并设置样式
//        XSSFCell cell = row.createCell(0);
//        cell.setCellValue("小明");
//        cell.setCellStyle(cellStyle);   // 设置样式的关键代码



        // 设置列名
        Row sheetRow = sheet.createRow(0);
        Cell cell = null;
        for (int i = 0; i < staticHeaders.size(); i++) {
            cell = sheetRow.createCell(i);
            cell.setCellValue(staticHeaders.get(i));
        }
        //设置列
        int rows = 1;

        Row row1 = sheet.createRow(rows++);
//            int initCellNo = 1;
        row1.createCell(1).setCellValue(staticParam.getDouble("oilDeep"));
        row1.createCell(2).setCellValue(staticParam.getDouble("waterDensity"));
        row1.createCell(3).setCellValue(staticParam.getDouble("oilDensity"));


//            int titleSize = headers.size();
//            for (int i = 0; i < titleSize; i++) {
//                String key = headers.get(i);
//                Object object = data.get(key);
//                if (object == null) {
//                    row.createCell(initCellNo).setCellValue("");
//                } else {
//                    row.createCell(initCellNo).setCellValue(String.valueOf(object));
//                }
//                initCellNo++;
//            }
//        }

        //设置列名
         sheetRow = sheet.createRow(4);
         cell = null;
        for (int i = 0; i < movingHeaders.size(); i++) {
            cell = sheetRow.createCell(i);
            cell.setCellValue(movingHeaders.get(i));
//            cell.setCellStyle(headStyle);
        }

         //设置列值
         rows = 5;
        for (int i=0;i<dynamicParameters.size();i++) {
            Row row = sheet.createRow(rows++);
            int initCellNo = 1;
            int titleSize = headers.size();
            for (int j = 0; j < titleSize; j++) {
                String key = headers.get(j);
                Object object = dynamicParameters.getJSONObject(i).get(key);
                if (object == null) {
                    row.createCell(initCellNo).setCellValue("");
                } else {
                    row.createCell(initCellNo).setCellValue(String.valueOf(object));
                }
                initCellNo++;
            }
        }

        // 5.输出
        File file = new File("F:\\poi实战3.xlsx");
        if (!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();

        String fileName="111.xlsx";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }


//    public void exportExcel(HttpServletRequest req, HttpServletResponse resp, List<String> headers, List<Map<String, String>> exportDatas) throws IOException, IOException {
//        // 创建一个工作薄
//        SXSSFWorkbook wb = new SXSSFWorkbook();
//        //创建sheet
//        Sheet sh = wb.createSheet("订单信息");
//        //设置表头字体
//        Font headFont = wb.createFont();
//        headFont.setFontName("宋体");
//        headFont.setColor(HSSFColor.WHITE.index);
//        headFont.setFontHeightInPoints((short) 10);// 字体大小
//        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
//
//        // 设置表头样式
//        CellStyle headStyle = wb.createCellStyle();
//        headStyle.setFont(headFont);
//        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
//        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
//        headStyle.setLocked(true);
//        headStyle.setWrapText(true);// 自动换行
//        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        headStyle.setFillForegroundColor(HSSFColor.GREEN.index);
//
//        // 设置普通单元格字体
//        Font font = wb.createFont();
//        font.setFontName("宋体");
//        font.setFontHeightInPoints((short) 9);
//
//        // 设置普通单元格样式
//        CellStyle style = wb.createCellStyle();
//        style.setFont(font);
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
//        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 上下居中
//        style.setWrapText(true);
//        style.setLeftBorderColor(HSSFColor.BLACK.index);
//        style.setBorderLeft((short) 1);
//        style.setRightBorderColor(HSSFColor.BLACK.index);
//        style.setBorderRight((short) 1);
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
//        style.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
//        style.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．
//
//        //设置单位格样式为文本
//        DataFormat dataFormat = wb.createDataFormat();
//        style.setDataFormat(dataFormat.getFormat("@"));
//
//        /**
//         * 设置列名
//         */
//        Row sheetRow = sh.createRow(0);
//        Cell cell = null;
//        for (int i = 0; i < headers.size(); i++) {
//            cell = sheetRow.createCell(i);
//            cell.setCellValue(headers.get(i));
//            cell.setCellStyle(headStyle);
//        }
//
//        /**
//         * 设置列值
//         */
//        int rows = 1;
//        for (Map<String, String> data : exportDatas) {
//            Row row = sh.createRow(rows++);
//            int initCellNo = 0;
//            int titleSize = headers.size();
//            for (int i = 0; i < titleSize; i++) {
//                String key = headers.get(i);
//                Object object = data.get(key);
//                if (object == null) {
//                    row.createCell(initCellNo).setCellValue("");
//                } else {
//                    row.createCell(initCellNo).setCellValue(String.valueOf(object));
//                }
//                initCellNo++;
//            }
//        }
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String fileName = "订单信息" + sdf.format(new Date()) + ".xlsx";
//        resp.setContentType("application/octet-stream;charset=utf-8");
//        resp.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//
//
//        File file = new File("F:\\poi实战3.xlsx");
//        if (!file.exists()){
//            file.createNewFile();
//        }
//        OutputStream fos = new FileOutputStream(file);
//
//        wb.write(fos);
//        fos.close();
//
//
////        ServletOutputStream out = resp.getOutputStream();
////        wb.write(out);
////        out.close();
//
//    }




//    public void expoortExcelx(String title, String[] headers, String[] columns,
//                              List<T> list, OutputStream out, String pattern) throws NoSuchMethodException, Exception{
//        //创建工作薄
//        XSSFWorkbook workbook=new XSSFWorkbook();
//        //创建表格
//        Sheet sheet=workbook.createSheet(title);
//        //设置默认宽度
//        sheet.setDefaultColumnWidth(25);
//        //创建样式
//        XSSFCellStyle style=workbook.createCellStyle();
//        //设置样式
//        style.setFillForegroundColor(IndexedColors.GOLD.index);
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        style.setBorderBottom(CellStyle.BORDER_THIN);
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        //生成字体
//        XSSFFont font=workbook.createFont();
//        font.setColor(IndexedColors.VIOLET.index);
//        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
//        //应用字体
//        style.setFont(font);
//
//        //自动换行
//        style.setWrapText(true);
//        //声明一个画图的顶级管理器
//        Drawing drawing=(XSSFDrawing) sheet.createDrawingPatriarch();
//        //表头的样式
//        XSSFCellStyle titleStyle=workbook.createCellStyle();//样式对象
//        titleStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);//水平居中
//        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        //设置字体
//        XSSFFont titleFont=workbook.createFont();
//        titleFont.setFontHeightInPoints((short)15);
//        titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体
//        titleStyle.setFont(titleFont);
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length-1));
//        //指定合并区域
//        Row rowHeader = sheet.createRow(0);
//        //XSSFRow rowHeader=sheet.createRow(0);
//        Cell cellHeader=rowHeader.createCell(0);
//        XSSFRichTextString textHeader=new XSSFRichTextString(title);
//        cellHeader.setCellStyle(titleStyle);
//        cellHeader.setCellValue(textHeader);
//
//        Row row=sheet.createRow(1);
//        for(int i=0;i<headers.length;i++){
//            Cell cell=row.createCell(i);
//            cell.setCellStyle(style);
//            XSSFRichTextString text=new XSSFRichTextString(headers[i]);
//            cell.setCellValue(text);
//        }
//        //遍历集合数据，产生数据行
//        if(list!=null&&list.size()>0){
//            int index=2;
//            for(T t:list){
//                row=sheet.createRow(index);
//                index++;
//                for(short i=0;i<columns.length;i++){
//                    Cell cell=row.createCell(i);
//                    String filedName=columns[i];
//                    String getMethodName="get"+filedName.substring(0,1).toUpperCase()
//                            +filedName.substring(1);
//                    Class tCls=t.getClass();
//                    Method getMethod=tCls.getMethod(getMethodName,new Class[]{});
//                    Object value=getMethod.invoke(t, new Class[]{});
//                    String textValue=null;
//                    if(value==null){
//                        textValue="";
//                    }else if(value instanceof Date){
//                        Date date=(Date)value;
//                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//                        textValue = sdf.format(date);
//                    }else if(value instanceof byte[]){
//                        row.setHeightInPoints(80);
//                        sheet.setColumnWidth(i, 35*100);
//                        byte[] bsValue=(byte[])value;
//                        XSSFClientAnchor anchor=new XSSFClientAnchor(0,0,1023,255,6,index,6,index);
////                        anchor.setAnchorType(2);
//                        drawing.createPicture(anchor, workbook.addPicture(bsValue, XSSFWorkbook.PICTURE_TYPE_JPEG));
//                    }else{
//                        // 其它数据类型都当作字符串简单处理
//                        textValue=value.toString();
//                    }
//
//                    if(textValue!=null){
//                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
//                        Matcher matcher = p.matcher(textValue);
//                        if (matcher.matches()) {
//                            // 是数字当作double处理
//                            cell.setCellValue(Double.parseDouble(textValue));
//                        } else {
//                            XSSFRichTextString richString = new XSSFRichTextString(
//                                    textValue);
//                            // HSSFFont font3 = workbook.createFont();
//                            // font3.setColor(HSSFColor.BLUE.index);
//                            // richString.applyFont(font3);
//                            cell.setCellValue(richString);
//                        }
//                    }
//
//                }
//            }
//        }
//        workbook.write(out);
//    }



    /**
     * 无模块导出Excel方法，
     * 参数data格式: [
     *      {
     *          "姓名": "张三";
     *          "年龄": "23";
     *          "性别": "男"
     *      },
     *      {
     *          "姓名": "李四";
     *          "年龄": "24";
     *          "性别": "男"
     *      }
     * ]
     *
     * @param data 需要导出的数据
     * @param fileName 导出后保存到本地的文件名
     * @return 创建好的Excel文件，用于前端下载
     * @
     */
//    public static HSSFWorkbook exportExcel(List<Map<String, Object>> data, String fileName)  {
//        // 从参数data中解析出打印的每列标题，放入title中
//        List<String> title = new ArrayList();
//        for(Map.Entry<String, Object> entry : data.get(0).entrySet()) {
//            title.add(entry.getKey());
//        }
//        // 新建一个Excel文件
//        HSSFWorkbook wb = new HSSFWorkbook();
//        // Excel中的sheet
//        HSSFSheet sheet = wb.createSheet();
//        // sheet中的行，0表示第一行
//        HSSFRow row = sheet.createRow(0);
//        // 设置标题居中
//        HSSFCellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        // sheet中的单元格
//        HSSFCell cell = null;
//        // 给第一行赋值，值为我们从参数中解析出的标题，因此需要我们在传参的时候需要严格按照约定
//        for(int i = 0; i < title.size(); i++) {
//            cell = row.createCell(i);
//            cell.setCellValue(title.get(i));
//            cell.setCellStyle(cellStyle);
//        }
//        // 根据参数内容数量，创建表格行数
//        for(int i = 0; i < data.size(); i++) {
//            row = sheet.createRow(i + 1);
//            Map<String, Object> values = data.get(i);
//            // 将参数插入每一个单元格
//            for(int k = 0; k < title.size(); k++) {
//                Object value = values.get(title.get(k));
//                if(null == value) {
//                    value = "";
//                }
//                String val = JSON.toJSONString(value);
//                row.createCell(k).setCellValue(val);
//            }
//        }
//        // 将文件写到硬盘中，将来根据需求，或写到服务器中，因此在实际开发中，最好将"E:/Temp/"配置在.properties配置文件中，仪表项目上线事更换方便
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(new File("F:/Temp/" + fileName));
//            wb.write(fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (Exception e) {
//
//        }
//        return wb;
//    }
}
