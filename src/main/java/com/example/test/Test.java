package com.example.test;


import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.STSchemeColorVal.Enum;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer;



public class Test {

        public static void main(String[] args) throws IOException {
            Test t=new Test();

            //1折线图、曲线图
            LinkedList headerList=new LinkedList();
            headerList.add("1111");
            headerList.add("2222");
            headerList.add("3");
            headerList.add("4");
            headerList.add("5");
            headerList.add("6");
            double [][]dataList={{1,2},{5,6},{3,4},{3,4},{3,4},{7,22},{23,20}};

            String xName="这是x轴";
            String yName="这是y轴";

            String title="折线图测试用例";
            String [][]dataList1={{"2000-1","1"},{"2000-2","0"},
                    {"2000-3","0"},{"2000-4","0"},
                    {"2000-5",null},{"2000-6","0"},
                    {"2000-7","0"}};




//            String [][]dataList1={{"2000-1",""},{"2000-2",""},{"2000-3",""},{"2000-4",""},{"2000-5",""},{"2000-6",""},{"2000-7",""}};
            String []y={"1","","2","3","4","2","2"};

            t.createLineChart(headerList,dataList1,title,xName,yName,y);

//            String [][]dataList1={{"2000-1","1","6","33","40","21"},
//                    {"2000-2","2",null,"33","40","21"},
//                    {"2000-3","5","41","33","40","21"},
//                    {"2000-4","6","31","33","40","21"},
//                    {"2000-5","8","61","33","40","21"},
//                    {"2000-6","9","14","33","40","21"},
//                    {"2000-7","21","31","33","40","21"}};

//            t.createOnePicManyLineChart(headerList,dataList1,title,xName,yName);
//            Date date = new Date(System.currentTimeMillis());
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            System.out.println(format.format(date));



        }



    //测试 ------一个图多条线
    public void createOnePicManyLineChart(LinkedList headerList,String [][]dataList,String title,String xName,String yName) throws IOException {

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
                        cell.setCellValue((dataList[i][j]));
                    }else{
                        if(dataList[i][j]!=null) {
                            cell.setCellValue(Double.parseDouble(dataList[i][j]));
                        }
                    }
                }

            }


            //创建一个画布
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            //前四个默认0，[0,5]：从0列5行开始;[7,26]:宽度7个单元格，26向下扩展到26行
            //默认宽度(14-8)*12
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, 1, headerList.size()+20, 26);
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
            //分类轴标(X轴)数据，单元格范围位置[0, 0]到[0, 6]
            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));
//                XDDFDataSource<Double> countries = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));
            //数据1，单元格范围位置[1, 0]到[1, 6]
//            XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 1, 1));


            LinkedList yList=new LinkedList();
            for(int i=1;i<dataList[0].length;i++){
                XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, i, i));
                yList.add(area);
            }



            //LINE：折线图，
            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);


            for(int i=0;i<yList.size();i++){

                //图表加载数据，折线1
                XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, (XDDFNumericalDataSource<Double>)yList.get(i));
                //折线图例标题
                series1.setTitle(headerList.get(1+i).toString(), null);
                //直线  false是折线图，true是曲线图
                series1.setSmooth(true);

                //设置标记大小
                series1.setMarkerSize((short) 6);
                //设置标记样式
                //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
                series1.setMarkerStyle(MarkerStyle.NONE);

                //设置颜色特性
//                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED));
//                series1.setFillProperties(fill);
//                XDDFLineProperties aaa=new XDDFLineProperties(fill);
//                series1.setLineProperties(aaa);
            }

            //绘制
                chart.plot(data);
            //TODO:关键语句，当只有一组y值时，必须设置，否则会出现把x每个值都当作图例的错误
            chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);

            // 将输出写入excel文件
            title="测试多线单图";
            String filename = "F://"+title+System.currentTimeMillis()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }

    }



    public  void aaa() throws IOException{
        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "Sheet1";
        FileOutputStream fileOut = null;
        try {
            XSSFSheet sheet = wb.createSheet(sheetName);
            // 第一行，国家名称
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("俄罗斯");
            cell = row.createCell(1);
            cell.setCellValue("加拿大");
            cell = row.createCell(2);
            cell.setCellValue("美国");
            cell = row.createCell(3);
            cell.setCellValue("中国");
            cell = row.createCell(4);
            cell.setCellValue("巴西");
            cell = row.createCell(5);
            cell.setCellValue("澳大利亚");
            cell = row.createCell(6);
            cell.setCellValue("印度");
            // 第二行，乡村地区
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue(0);
            cell = row.createCell(1);
            cell.setCellValue(0);
            cell = row.createCell(2);
            cell.setCellValue("");
            cell = row.createCell(3);
            cell.setCellValue("");
            cell = row.createCell(4);
            cell.setCellValue("");
            cell = row.createCell(5);
            cell.setCellValue("");
            cell = row.createCell(6);
            cell.setCellValue("");
            // 第三行，农村人口
            row = sheet.createRow(2);
            cell = row.createCell(0);
            cell.setCellValue("");
            cell = row.createCell(1);
            cell.setCellValue("");
            cell = row.createCell(2);
            cell.setCellValue("");
            cell = row.createCell(3);
            cell.setCellValue("");
            cell = row.createCell(4);
            cell.setCellValue("");
            cell = row.createCell(5);
            cell.setCellValue("");
            cell = row.createCell(6);
            cell.setCellValue("");
            // 第四行，面积平局
            row = sheet.createRow(3);
            cell = row.createCell(0);
            cell.setCellValue(9435701.143);
            cell = row.createCell(1);
            cell.setCellValue(9435701.143);
            cell = row.createCell(2);
            cell.setCellValue(9435701.143);
            cell = row.createCell(3);
            cell.setCellValue(9435701.143);
            cell = row.createCell(4);
            cell.setCellValue(9435701.143);
            cell = row.createCell(5);
            cell.setCellValue(9435701.143);
            cell = row.createCell(6);
            cell.setCellValue(9435701.143);
            // 第四行，人口平局
            row = sheet.createRow(4);
            cell = row.createCell(0);
            cell.setCellValue(22475821.29);
            cell = row.createCell(1);
            cell.setCellValue(22475821.29);
            cell = row.createCell(2);
            cell.setCellValue(22475821.29);
            cell = row.createCell(3);
            cell.setCellValue(22475821.29);
            cell = row.createCell(4);
            cell.setCellValue(22475821.29);
            cell = row.createCell(5);
            cell.setCellValue(22475821.29);
            cell = row.createCell(6);
            cell.setCellValue(22475821.29);

            // 创建一个画布
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFDrawing drawing2 = sheet.createDrawingPatriarch();
            // 前四个默认0，[0,5]：从0列5行开始;[7,26]:到7列26行结束
            // 默认宽度(14-8)*12
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 7, 26);
            XSSFClientAnchor anchor2 = drawing.createAnchor(0, 0, 0, 0, 0, 28, 7, 49);
            // 创建一个chart对象
            XSSFChart chart = drawing.createChart(anchor);
            XSSFChart chart2 = drawing.createChart(anchor2);
            // 标题
            chart.setTitleText("地区排名前七的国家");
            chart2.setTitleText("地区排名前七的国家");
            // 标题覆盖
            chart.setTitleOverlay(false);

            /*双Y轴*/

            // X轴
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("X轴标题");
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
            leftAxis.setMaximum(20000000);
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
            rightAxis.setMaximum(40000000);
            // rightAxis.setVisible(false);// 隐藏Y轴
            rightAxis.setTitle("右Y轴标题");

            // 图例位置
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP);

            // CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
            // X轴数据，单元格范围位置[0, 0]到[0, 6]
            XDDFDataSource<String> xdatas = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(0, 0, 0, 6));
            // XDDFCategoryDataSource xdatas = XDDFDataSourcesFactory.fromArray(new String[] {"俄罗斯","加拿大","美国","中国","巴西","澳大利亚","印度"});
            // 左Y轴数据，单元格范围位置[1, 0]到[1, 6]
            XDDFNumericalDataSource<Double> leftYdatas = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, 0, 6));
            // XDDFNumericalDataSource<Integer> leftYdatas = XDDFDataSourcesFactory.fromArray(new Integer[] {17098242,9984670,9826675,9596961,8514877,7741220,3287263});
            // 右Y轴数据，单元格范围位置[2, 0]到[2, 6]
            XDDFNumericalDataSource<Double> rightYdatas = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(2, 2, 0, 6));
            // XDDFNumericalDataSource<Integer> rightYdatas = XDDFDataSourcesFactory.fromArray(new Integer[] {17098242,9984670,9826675,9596961,8514877,7741220,3287263});

            // LINE：折线图，
            XDDFLineChartData line1 = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, rightAxis);
            // 图表加载数据，折线1
            XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) line1.addSeries(xdatas, rightYdatas);
            // 折线图例标题
            series1.setTitle("右Y轴数据的图例标题", null);
            // 直线
            series1.setSmooth(false);
            // 设置标记大小
            series1.setMarkerSize((short) 6);
            // 设置标记样式，星星
            series1.setMarkerStyle(MarkerStyle.STAR);
            // 绘制
            chart.plot(line1);

            // LINE：折线图，
            XDDFLineChartData line2 = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
            // 图表加载数据，折线1
            XDDFLineChartData.Series series2 = (XDDFLineChartData.Series) line2.addSeries(xdatas, leftYdatas);
            // 折线图例标题
            series2.setTitle("左Y轴数据的图例标题", null);
            // 直线
            series2.setSmooth(false);
            // 设置标记大小
            series2.setMarkerSize((short) 6);
            // 设置标记样式，星星
            series2.setMarkerStyle(MarkerStyle.STAR);
            // 绘制
            chart.plot(line2);

            // 填充与线条-标记-填充-依数据点着色（取消勾选）
            chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);





            XDDFCategoryAxis bottomAxis2 = chart2.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis2.setTitle("X轴标题");
            // bottomAxis.setVisible(false);// 隐藏X轴

            // 左Y轴
            XDDFValueAxis leftAxis2 = chart2.createValueAxis(AxisPosition.LEFT);
            // 左Y轴和X轴交叉点在X轴0点位置
            leftAxis2.setCrosses(AxisCrosses.AUTO_ZERO);
            leftAxis2.setCrossBetween(AxisCrossBetween.BETWEEN);
            // 构建坐标轴
            leftAxis2.crossAxis(bottomAxis2);
            bottomAxis2.crossAxis(leftAxis2);
            // 设置左Y轴最大值
            leftAxis2.setMaximum(20000000);
            // leftAxis.setVisible(false);// 隐藏Y轴
            leftAxis2.setTitle("左Y轴标题");

            // 右Y轴
            XDDFValueAxis rightAxis2 = chart2.createValueAxis(AxisPosition.RIGHT);
            // 右Y轴和X轴交叉点在X轴最大值位置
            rightAxis2.setCrosses(AxisCrosses.MAX);
            rightAxis2.setCrossBetween(AxisCrossBetween.BETWEEN);
            // 构建坐标轴
            rightAxis2.crossAxis(bottomAxis2);
            bottomAxis2.crossAxis(rightAxis2);
            // 设置左Y轴最大值
            rightAxis2.setMaximum(40000000);
            // rightAxis.setVisible(false);// 隐藏Y轴
            rightAxis2.setTitle("右Y轴标题");

            // 图例位置
            XDDFChartLegend legend2 = chart2.getOrAddLegend();
            legend2.setPosition(LegendPosition.TOP);

            // CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
            // X轴数据，单元格范围位置[0, 0]到[0, 6]
            XDDFDataSource<String> xdatas2 = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(0, 0, 0, 6));
            // XDDFCategoryDataSource xdatas = XDDFDataSourcesFactory.fromArray(new String[] {"俄罗斯","加拿大","美国","中国","巴西","澳大利亚","印度"});
            // 左Y轴数据，单元格范围位置[1, 0]到[1, 6]
            XDDFNumericalDataSource<Double> leftYdatas2 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, 0, 6));
            // XDDFNumericalDataSource<Integer> leftYdatas = XDDFDataSourcesFactory.fromArray(new Integer[] {17098242,9984670,9826675,9596961,8514877,7741220,3287263});
            // 右Y轴数据，单元格范围位置[2, 0]到[2, 6]
            XDDFNumericalDataSource<Double> rightYdatas2 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(2, 2, 0, 6));
            // XDDFNumericalDataSource<Integer> rightYdatas = XDDFDataSourcesFactory.fromArray(new Integer[] {17098242,9984670,9826675,9596961,8514877,7741220,3287263});

            // LINE：折线图，
            XDDFLineChartData line12 = (XDDFLineChartData) chart2.createData(ChartTypes.LINE, bottomAxis2, rightAxis2);
            // 图表加载数据，折线1
            XDDFLineChartData.Series series12 = (XDDFLineChartData.Series) line12.addSeries(xdatas2, rightYdatas2);
            // 折线图例标题
            series12.setTitle("右Y轴数据的图例标题", null);
            // 直线
            series12.setSmooth(false);
            // 设置标记大小
            series12.setMarkerSize((short) 6);
            // 设置标记样式，星星
            series12.setMarkerStyle(MarkerStyle.STAR);
            // 绘制
            chart2.plot(line12);

            // LINE：折线图，
            XDDFLineChartData line22 = (XDDFLineChartData) chart2.createData(ChartTypes.LINE, bottomAxis2, leftAxis2);
            // 图表加载数据，折线1
            XDDFLineChartData.Series series22 = (XDDFLineChartData.Series) line22.addSeries(xdatas2, leftYdatas2);
            // 折线图例标题
            series22.setTitle("左Y轴数据的图例标题", null);
            // 直线
            series22.setSmooth(false);
            // 设置标记大小
            series22.setMarkerSize((short) 6);
            // 设置标记样式，星星
            series22.setMarkerStyle(MarkerStyle.STAR);
            // 绘制
            chart2.plot(line22);

            // 填充与线条-标记-填充-依数据点着色（取消勾选）
            chart2.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);




                //--------------------------------------------第三
            XSSFClientAnchor anchor3 = drawing.createAnchor(0, 0, 0, 0, 0, 5, 7, 26);
            XSSFChart chart3 = drawing.createChart(anchor3);
            chart3.setTitleText("地区排名前七的国家");
            XDDFCategoryAxis bottomAxis3 = chart3.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis3.setTitle("X轴标题");
            XDDFValueAxis leftAxis3 = chart3.createValueAxis(AxisPosition.LEFT);
            // 左Y轴和X轴交叉点在X轴0点位置
            leftAxis3.setCrosses(AxisCrosses.AUTO_ZERO);
            leftAxis3.setCrossBetween(AxisCrossBetween.BETWEEN);
            // 构建坐标轴
            leftAxis3.crossAxis(bottomAxis3);
            bottomAxis3.crossAxis(leftAxis3);
            // 设置左Y轴最大值
            leftAxis3.setMaximum(20000000);
            // leftAxis3.setVisible(false);// 隐藏Y轴
            leftAxis3.setTitle("左Y轴标题");
            // 右Y轴
            XDDFValueAxis rightAxis3 = chart3.createValueAxis(AxisPosition.RIGHT);
            // 右Y轴和X轴交叉点在X轴最大值位置
            rightAxis3.setCrosses(AxisCrosses.MAX);
            rightAxis3.setCrossBetween(AxisCrossBetween.BETWEEN);
            // 构建坐标轴
            rightAxis3.crossAxis(bottomAxis3);
            bottomAxis3.crossAxis(rightAxis3);
            // 设置左Y轴最大值
            rightAxis3.setMaximum(40000000);
            // rightAxis3.setVisible(false);// 隐藏Y轴
            rightAxis3.setTitle("右Y轴标题");

            XDDFDataSource<String> xdatas3 = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(0, 0, 0, 6));
            // XDDFCategoryDataSource xdatas3 = XDDFDataSourcesFactory.fromArray(new String[] {"俄罗斯","加拿大","美国","中国","巴西","澳大利亚","印度"});
            // 左Y轴数据，单元格范围位置[1, 0]到[1, 6]
            XDDFNumericalDataSource<Double> leftYdatas3 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, 0, 6));
            // XDDFNumericalDataSource<Integer> leftYdatas3 = XDDFDataSourcesFactory.fromArray(new Integer[] {17098242,9984670,9826675,9596961,8514877,7741220,3287263});
            // 右Y轴数据，单元格范围位置[2, 0]到[2, 6]
            XDDFNumericalDataSource<Double> rightYdatas3 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(2, 2, 0, 6));

            XDDFLineChartData line13 = (XDDFLineChartData) chart3.createData(ChartTypes.LINE, bottomAxis3, rightAxis3);
            // 图表加载数据，折线1
            XDDFLineChartData.Series series13 = (XDDFLineChartData.Series) line13.addSeries(xdatas3, rightYdatas3);
            // 折线图例标题
            series13.setTitle("右Y轴数据的图例标题", null);
            // 直线
            series13.setSmooth(false);
            // 设置标记大小
            series13.setMarkerSize((short) 6);
            // 设置标记样式，星星
            series13.setMarkerStyle(MarkerStyle.STAR);
            // 绘制
            chart3.plot(line13);

            // LINE：折线图，
            XDDFLineChartData line23 = (XDDFLineChartData) chart3.createData(ChartTypes.LINE, bottomAxis3, leftAxis3);
            // 图表加载数据，折线1
            XDDFLineChartData.Series series23 = (XDDFLineChartData.Series) line23.addSeries(xdatas3, leftYdatas3);
            // 折线图例标题
            series23.setTitle("左Y轴数据的图例标题", null);
            // 直线
            series23.setSmooth(false);
            // 设置标记大小
            series23.setMarkerSize((short) 6);
            // 设置标记样式，星星
            series23.setMarkerStyle(MarkerStyle.STAR);
            // 绘制
            chart3.plot(line23);

            // 填充与线条-标记-填充-依数据点着色（取消勾选）
            chart3.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);

//--------------------------------------------第三






            // 打印图表的xml
            // System.out.println(chart.getCTChart());

            // 将输出写入excel文件
            String filename = "F://双Y轴测试"+System.currentTimeMillis()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }

    }


    public void createTwoYaxisLineChart(LinkedList headerList,String [][]dataList,
                                        String title,String xName,String yName
                                         ) throws IOException {
//        logger.info("折线图（双y轴）");
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
            XDDFSolidFillProperties fill2 = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GRAY));
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

//            resp.setContentType("application/octet-stream");
//            resp.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
//            resp.flushBuffer();
//            wb.write(resp.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }


    }

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
     * 折线图（曲线图）
     * （一个y轴）
     *
     * @param headerList
     * @param dataList
     * @param title
     * @param xName
     * @param yName
     * @throws IOException
     */
        public void createLineChart(LinkedList headerList,String [][]dataList,String title,String xName,String yName,String[] y) throws IOException {

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
                            cell.setCellValue((dataList[i][j]));
                        }else{
                            if(dataList[i][j]!=null) {
                                cell.setCellValue(Double.parseDouble(dataList[i][j]));
                            }
                        }
                    }

                }


                //创建一个画布
                XSSFDrawing drawing = sheet.createDrawingPatriarch();
                //前四个默认0，[0,5]：从0列5行开始;[7,26]:宽度7个单元格，26向下扩展到26行
                //默认宽度(14-8)*12
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, 1, headerList.size()+20, 26);
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
                //分类轴标(X轴)数据，单元格范围位置[0, 0]到[0, 6]
                XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));
//                XDDFDataSource<Double> countries = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));
                //数据1，单元格范围位置[1, 0]到[1, 6]
                XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 1, 1));
//                XDDFNumericalDataSource<Double> area1=XDDFDataSourcesFactory.fromArray(y);
                XDDFDataSourcesFactory.fromArray(y);

                //LINE：折线图，
                XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

                //图表加载数据，折线1
                XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, area);
                //折线图例标题
                series1.setTitle(headerList.get(1).toString(), null);
                //直线  false是折线图，true是曲线图
                series1.setSmooth(false);

                //设置标记大小
                series1.setMarkerSize((short) 6);
                //设置标记样式
                //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
                series1.setMarkerStyle(MarkerStyle.NONE);

                //设置颜色特性
//                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED));
//                series1.setFillProperties(fill);
//                XDDFLineProperties aaa=new XDDFLineProperties(fill);
//                series1.setLineProperties(aaa);


//                series1.plot();

//                CTLineChart scatterChart = chart.getCTChart().getPlotArea().getLineChartArray(0);
//                CTLineSer ser = scatterChart.getSerArray(0);
//                for (int i = 0; i < area.getPointCount(); i++) {
//                    Double pointAt = area.getPointAt(i);
//                    if (pointAt == null || pointAt <= 0) {
//                        CTDPt dDPt = ser.addNewDPt();
//                        dDPt.addNewIdx().setVal(i);
//                        CTShapeProperties addNewSpPr = dDPt.addNewMarker().addNewSpPr();
//                        addNewSpPr.addNewNoFill();
//                        addNewSpPr.addNewLn().addNewNoFill();
//                    }
//                }



                //绘制

                chart.plot(data);
                //TODO:关键语句，当只有一组y值时，必须设置，否则会出现把x每个值都当作图例的错误
                chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);

                // 将输出写入excel文件
                String filename = "F://"+title+System.currentTimeMillis()+".xlsx";
                fileOut = new FileOutputStream(filename);
                wb.write(fileOut);
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
     * 折线图（曲线图）
     * （一个y轴多图）
     *
     * @param headerList
     * @param dataList
     * @param title
     * @param xName
     * @param yName
     * @throws IOException
     */
    public void createManyOneYLineChart(LinkedList headerList,String [][]dataList,String title,String xName,String yName) throws IOException {

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
                        cell.setCellValue((dataList[i][j]));
                    }else{
                        if(dataList[i][j]!=null) {
                            cell.setCellValue(Double.parseDouble(dataList[i][j]));
                        }
                    }
                }
            }
            //创建一个画布
            XSSFDrawing drawing = sheet.createDrawingPatriarch();

            for(int i=0;i<(dataList[0].length-1);i++){
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, 1, headerList.size()+20, 26);
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
                //分类轴标(X轴)数据
                XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));
                //数据1，单元格范围位置[1, 0]到[1, 6]
                XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, i+1, i+1));


                //LINE：折线图，
                XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

                //图表加载数据，折线1
                XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, area);
                //折线图例标题
                series1.setTitle(headerList.get(1).toString(), null);
                //直线  false是折线图，true是曲线图
                series1.setSmooth(true);

                //设置标记大小
                series1.setMarkerSize((short) 6);
                //设置标记样式
                //NONE-无 CIRCLE-实心圆圈 STAR-星号  DASH-实线 DIAMOND-方块（菱形） DOT-点 PLUS-加号 SQUARE-正方形 TRIANGLE-三角形 X-X
                series1.setMarkerStyle(MarkerStyle.NONE);
                XDDFNoFillProperties fill = new XDDFNoFillProperties();
                // 散点图
                series1.setFillProperties(fill);
                series1.setSmooth(false);
                series1.setMarkerStyle(MarkerStyle.CIRCLE);
                series1.setMarkerSize((short) 5);
                // 绘制
                series1.plot();
                // 设置颜色
                Color color = new Color(217, 217, 217);// 灰色边框
                color = new Color(255, 0, 0);// 红色
                byte[] colorByte = new byte[] { (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue() };
                CTLineChart scatterChart = chart.getCTChart().getPlotArea().getLineChartArray(0);
                scatterChart.addNewVaryColors().setVal(false);
               CTLineSer ser = scatterChart.getSerArray(0);

                CTShapeProperties spPr = ser.getMarker().addNewSpPr();
                spPr.addNewSolidFill().addNewSrgbClr().setVal(colorByte);
                // 无轮廓
                spPr.addNewLn().addNewNoFill();
                // 隐藏值为空或者0的点
                for (int j = 0; j < area.getPointCount(); j++) {
                    Double pointAt = area.getPointAt(j);
                    if (pointAt == null || pointAt == 0) {
                        CTDPt dDPt = ser.addNewDPt();
                        dDPt.addNewIdx().setVal(j);
                        CTShapeProperties addNewSpPr = dDPt.addNewMarker().addNewSpPr();
                        addNewSpPr.addNewNoFill();
                        addNewSpPr.addNewLn().addNewNoFill();
                    }
                }
                //设置颜色特性
//                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED));
//                series1.setFillProperties(fill);
//                XDDFLineProperties aaa=new XDDFLineProperties(fill);
//                series1.setLineProperties(aaa);


//                series1.plot();
//
//                CTLineChart scatterChart = chart.getCTChart().getPlotArea().getLineChartArray(0);
//                CTLineSer ser = scatterChart.getSerArray(0);
//                for (int j = 0; j < area.getPointCount(); j++) {
//                    Double pointAt = area.getPointAt(j);
//                    if (pointAt == null || pointAt <= 0) {
//                        CTDPt dDPt = ser.addNewDPt();
//                        dDPt.addNewIdx().setVal(j);
//                        CTShapeProperties addNewSpPr = dDPt.addNewMarker().addNewSpPr();
//                        addNewSpPr.addNewNoFill();
//                        addNewSpPr.addNewLn().addNewNoFill();
//                    }
//                }
//                //绘制
////                chart.plot(data);
//                //TODO:关键语句，当只有一组y值时，必须设置，否则会出现把x每个值都当作图例的错误
//                chart.getCTChart().getPlotArea().getLineChartArray(0).addNewVaryColors().setVal(false);
            }



            // 将输出写入excel文件
            title="单y轴多图";
            String filename = "F://"+title+System.currentTimeMillis()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }

    }






     public void createBarChart(LinkedList headerList,double [][]dataList) throws IOException {
            XSSFWorkbook wb = new XSSFWorkbook();
            String sheetName="图表";
            XSSFSheet sheet = wb.createSheet(sheetName);

            Row row;
            Cell cell;

            row = sheet.createRow(0);
            for(int i=0;i<headerList.size();i++){
                row.createCell(i).setCellValue(headerList.get(i).toString());
            }

            for(int i=0;i<dataList.length;i++){
                row = sheet.createRow(i+1);
                for(int j=0;j<dataList[i].length;j++){
                    cell = row.createCell(j);
                    cell.setCellValue(dataList[i][j]);
                }
            }

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = (XSSFClientAnchor) drawing.createAnchor(0, 0, 0, 0, 4, 0, 11, 15);

            Chart chart = drawing.createChart(anchor);

            CTChart ctChart = ((XSSFChart)chart).getCTChart();
            CTPlotArea ctPlotArea = ctChart.getPlotArea();

            //the bar chart
            CTBarChart ctBarChart = ctPlotArea.addNewBarChart();
            CTBoolean ctBoolean = ctBarChart.addNewVaryColors();
            ctBoolean.setVal(true);
            ctBarChart.addNewBarDir().setVal(STBarDir.COL);

            //the bar series柱状图设置   start
            CTBarSer ctBarSer = ctBarChart.addNewSer();
            CTSerTx ctSerTx = ctBarSer.addNewTx();
            CTStrRef ctStrRef = ctSerTx.addNewStrRef();
            //y轴名称
            ctStrRef.setF(sheetName+"!$B$1");
            ctBarSer.addNewIdx().setVal(0);
            //设置x轴数据
            CTAxDataSource cttAxDataSource = ctBarSer.addNewCat();
            ctStrRef = cttAxDataSource.addNewStrRef();
            ctStrRef.setF(sheetName+"!$A$2:$A$"+dataList.length+1);
            //设置y轴数据
            CTNumDataSource ctNumDataSource = ctBarSer.addNewVal();
            CTNumRef ctNumRef = ctNumDataSource.addNewNumRef();
            ctNumRef.setF(sheetName+"!$B$2:$B$"+dataList.length+1);
            //柱状图设置   end



            //at least the border lines in Libreoffice Calc ;-)
            ctBarSer.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(new byte[] {0,0,0});

            //telling the BarChart that it has axes and giving them Ids
            ctBarChart.addNewAxId().setVal(123456); //cat axis 1 (bars)
            ctBarChart.addNewAxId().setVal(123457); //val axis 1 (left)




            //cat axis 1 (bars)
            CTCatAx ctCatAx = ctPlotArea.addNewCatAx();
            ctCatAx.addNewAxId().setVal(123456); //id of the cat axis
            CTScaling ctScaling = ctCatAx.addNewScaling();
            ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
            ctCatAx.addNewDelete().setVal(false);
            ctCatAx.addNewAxPos().setVal(STAxPos.B);
            ctCatAx.addNewCrossAx().setVal(123457); //id of the val axis
            ctCatAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);

            //val axis 1 (left)
            CTValAx ctValAx = ctPlotArea.addNewValAx();
            ctValAx.addNewAxId().setVal(123457); //id of the val axis
            ctScaling = ctValAx.addNewScaling();
            ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
            ctValAx.addNewDelete().setVal(false);
            ctValAx.addNewAxPos().setVal(STAxPos.L);
            ctValAx.addNewCrossAx().setVal(123456); //id of the cat axis
            ctValAx.addNewCrosses().setVal(STCrosses.AUTO_ZERO); //this val axis crosses the cat axis at zero
            ctValAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);



            //legend图例设置
//            CTLegend ctLegend = ctChart.addNewLegend();
//            ctLegend.addNewLegendPos().setVal(STLegendPos.B);
//            ctLegend.addNewOverlay().setVal(false);

            FileOutputStream fileOut = new FileOutputStream("F://BarAndLineChart2.xlsx");
            wb.write(fileOut);
            fileOut.close();

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
        public void createBarChart(LinkedList headerList,double [][]dataList,String title,String xName,String yName) throws IOException {

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
                        cell.setCellValue(dataList[i][j]);
                    }

                }


                //创建一个画布
                XSSFDrawing drawing = sheet.createDrawingPatriarch();
                //前四个默认0，[0,5]：从0列5行开始;[7,26]:宽度7个单元格，26向下扩展到26行
                //默认宽度(14-8)*12
                XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, 1, headerList.size()+8, 26);
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
                //分类轴标(X轴)数据，单元格范围位置[0, 0]到[0, 6]
//                XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));
                XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, dataList.length, 0, 0));

                //数据1，单元格范围位置[1, 0]到[1, 6]
                XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 1, 1));


                //LINE：折线图，BAR:柱状图
//                XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
                XDDFBarChartData data = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
                data.setBarDirection(BarDirection.COL);//设置柱状图方向，COL竖着，BAR横着
                data.setVaryColors(false);
                leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);

                //图表加载数据，折线1
                XDDFBarChartData.Series series1 = (XDDFBarChartData.Series) data.addSeries( countries,area);
//                XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, area);
                //折线图例标题

                series1.setTitle(headerList.get(1).toString(), null);
                //直线  false是折线图，true是曲线图
//                series1.setSmooth(true);

                //设置颜色特性
                XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GRAY));
                series1.setFillProperties(fill);


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
     * duandi
     * （一个y轴）
     *
     * @param headerList
     * @param dataList
     * @param title
     * @param xName
     * @param yName
     * @throws IOException
     */
    public void createPointChart(LinkedList headerList,String [][]dataList,String title,String xName,String yName) throws IOException {

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
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, headerList.size()+1, 1, headerList.size()+20, 26);

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


            //数据1，单元格范围位置[1, 0]到[1, 6]
            XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, dataList.length, 1, 1));


            //LINE：折线图，
//            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
            XDDFScatterChartData data=(XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);

            data.setStyle(ScatterStyle.LINE_MARKER);
            //图表加载数据，折线1
//            XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, area);
            XDDFScatterChartData.Series series1 = (XDDFScatterChartData.Series) data.addSeries(countries, area);




            //折线图例标题
            series1.setTitle(headerList.get(1).toString(), null);
            //直线  false是折线图，true是曲线图
//            series1.setSmooth(true);

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

            //绘制
            chart.plot(data);
            //TODO:关键语句，当只有一组y值时，必须设置，否则会出现把x每个值都当作图例的错误
            chart.getCTChart().getPlotArea().getScatterChartArray(0).addNewVaryColors().setVal(false);

//            chart.getCTChart().getPlotArea().getScatterChartArray(0).setScatterStyle();



            // 将输出写入excel文件
            String filename = "F://"+title+System.currentTimeMillis()+".xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
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
     * duandi
     * （一个y轴）
     *
     * @param headerList
     * @param dataList
     * @param title
     * @param xName
     * @param yName
     * @throws IOException
     */
    public void createPointChart11(LinkedList headerList,String [][]dataList,String title,String xName,String yName) throws IOException {

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Sheet 1");
            final int NUM_OF_ROWS = 3;
            final int NUM_OF_COLUMNS = 10;

            // Create a row and put some cells in it. Rows are 0 based.
            Row row;
            Cell cell;
            for (int rowIndex = 0; rowIndex < NUM_OF_ROWS; rowIndex++) {
                row = sheet.createRow((short) rowIndex);
                for (int colIndex = 0; colIndex < NUM_OF_COLUMNS; colIndex++) {
                    cell = row.createCell((short) colIndex);
                    if(rowIndex==NUM_OF_ROWS-1&&colIndex==NUM_OF_COLUMNS-1){

                        continue;
                    }
                    cell.setCellValue(colIndex * (rowIndex + 1.0));
                }
            }

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 10, 15);

            XSSFChart chart = drawing.createChart(anchor);
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            XDDFValueAxis bottomAxis = chart.createValueAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("x"); // https://stackoverflow.com/questions/32010765
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("f(x)");
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            XDDFDataSource<Double> xs = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(0, 0, 0, NUM_OF_COLUMNS - 1));
            XDDFNumericalDataSource<Double> ys1 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, 0, NUM_OF_COLUMNS - 1));
            XDDFNumericalDataSource<Double> ys2 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(2, 2, 0, NUM_OF_COLUMNS - 1));


//            CellRangeAddress qqq=new  CellRangeAddress(1, 1, 0, NUM_OF_COLUMNS - 1);
//            CellRangeAddress www=new  CellRangeAddress(1, 1, 0, NUM_OF_COLUMNS - 1);
//            CellRangeAddress eee=qqq+www;






            XDDFScatterChartData data = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);
            XDDFScatterChartData.Series series1 = (XDDFScatterChartData.Series) data.addSeries(xs, ys1);
            series1.setTitle("2x", null); // https://stackoverflow.com/questions/21855842
            series1.setSmooth(false); // https://stackoverflow.com/questions/39636138

            series1.setMarkerStyle(MarkerStyle.CIRCLE);
            series1.setMarkerSize((short)5);
            setLineNoFill(series1);

            XDDFScatterChartData.Series series2 = (XDDFScatterChartData.Series) data.addSeries(xs, ys2);
            series2.setTitle("pp", null);
            series2.setSmooth(false);
            series2.setMarkerStyle(MarkerStyle.CIRCLE);
            series2.setMarkerSize((short)5);
            setLineNoFill(series2);


//            data.getSeries(1).getValuesData().;
            chart.plot(data);


            CTChart c=chart.getCTChart();
            c.getPlotArea().getScatterChartList().get(0).getSerArray(1).getYVal().getNumRef().getNumCache().getPtCount().setVal(9);
            c.getPlotArea().getScatterChartList().get(0).getSerArray(1).getXVal().getNumRef().getNumCache().removePt(9);
            c.getPlotArea().getScatterChartList().get(0).getSerArray(1).getXVal().getNumRef().getNumCache().getPtCount().setVal(9);
            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream("F://测试"+System.currentTimeMillis()+".xlsx")) {
                wb.write(fileOut);
            }
        }

    }


    public void createPointChart2(LinkedList headerList,String [][]dataList,String title,String xName,String yName) throws IOException {

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Sheet 1");
            final int NUM_OF_ROWS = 3;
            final int NUM_OF_COLUMNS = 10;

            // Create a row and put some cells in it. Rows are 0 based.
            Row row;
            Cell cell;

            LinkedList<Double> xList=new LinkedList<>();
            xList.add(0d);
            xList.add(1d);
            xList.add(2d);
            xList.add(3d);
            xList.add(4d);
            xList.add(5d);
            xList.add(6d);
            xList.add(7d);
            xList.add(9d);


            LinkedList<Double> yList=new LinkedList<>();
            LinkedList<Double> yList2=new LinkedList<>();
            yList2.add(30d);
            yList2.add(25d);
            yList2.add(28d);
            yList2.add(28d);
            yList2.add(28d);
            yList2.add(28d);
            yList2.add(28d);
            yList2.add(33d);
            yList2.add(36d);

            for (int rowIndex = 0; rowIndex < NUM_OF_ROWS; rowIndex++) {
                row = sheet.createRow((short) rowIndex);
                for (int colIndex = 0; colIndex < NUM_OF_COLUMNS; colIndex++) {
                    cell = row.createCell((short) colIndex);
                    if(rowIndex==NUM_OF_ROWS-1&&colIndex==NUM_OF_COLUMNS-1-1){

                        continue;
                    }
//                    Double ccc=Double.valueOf(rowIndex);
                    if(rowIndex==2){

//                        xList.add(Double.valueOf(rowIndex));
                        yList.add(colIndex * (rowIndex + 1.0));
                    }
                    cell.setCellValue(colIndex * (rowIndex + 1.0));
                }
            }

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 10, 15);

            XSSFChart chart = drawing.createChart(anchor);
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            XDDFValueAxis bottomAxis = chart.createValueAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("x"); // https://stackoverflow.com/questions/32010765
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("f(x)");
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

//            XDDFDataSource<Double> xs = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(0, 0, 0, NUM_OF_COLUMNS - 1));


            XDDFDataSource<Double> xs2 = XDDFDataSourcesFactory.fromArray(xList.toArray(new Double[xList.size()]));
            xList.add(11d);
            XDDFDataSource<Double> xs = XDDFDataSourcesFactory.fromArray(xList.toArray(new Double[xList.size()]));

//            XDDFNumericalDataSource<Double> ys1 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, 0, NUM_OF_COLUMNS - 1));

//            XDDFNumericalDataSource<Double> ys2 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(2, 2, 0, NUM_OF_COLUMNS - 1-1));
            XDDFNumericalDataSource<Double> ys2 = XDDFDataSourcesFactory.fromArray(yList2.toArray(new Double[yList2.size()]));
            yList.add(20d);
            XDDFNumericalDataSource<Double> ys1 = XDDFDataSourcesFactory.fromArray(yList.toArray(new Double[yList.size()]));

//            CellRangeAddress qqq=new  CellRangeAddress(1, 1, 0, NUM_OF_COLUMNS - 1);
//            CellRangeAddress www=new  CellRangeAddress(1, 1, 0, NUM_OF_COLUMNS - 1);
//            CellRangeAddress eee=qqq+www;






            XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
            XDDFScatterChartData data2 = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);


            XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(xs, ys1);
            series1.setTitle("2x", null); // https://stackoverflow.com/questions/21855842
            series1.setSmooth(true); // https://stackoverflow.com/questions/39636138

//            series1.setMarkerStyle(MarkerStyle.CIRCLE);
            series1.setMarkerSize((short)5);
//            setLineNoFill(series1);

            XDDFScatterChartData.Series series2 = (XDDFScatterChartData.Series) data2.addSeries(xs2, ys2);
            series2.setTitle("pp", null);
            series2.setSmooth(false);
            series2.setMarkerStyle(MarkerStyle.CIRCLE);
            series2.setMarkerSize((short)5);
            setLineNoFill(series2);


//            data.getSeries(1).getValuesData().;
            chart.plot(data);
            chart.plot(data2);


//            CTChart c=chart.getCTChart();
//            c.getPlotArea().getScatterChartList().get(0).getSerArray(1).getYVal().getNumRef().getNumCache().getPtCount().setVal(9);
//            c.getPlotArea().getScatterChartList().get(0).getSerArray(1).getXVal().getNumRef().getNumCache().removePt(9);
//            c.getPlotArea().getScatterChartList().get(0).getSerArray(1).getXVal().getNumRef().getNumCache().getPtCount().setVal(9);
            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream("F://测试"+System.currentTimeMillis()+".xlsx")) {
                wb.write(fileOut);
            }
        }

    }



    private static void setLineNoFill(XDDFScatterChartData.Series series) {
        XDDFNoFillProperties noFillProperties = new XDDFNoFillProperties();
        XDDFLineProperties lineProperties = new XDDFLineProperties();
        lineProperties.setFillProperties(noFillProperties);
        XDDFShapeProperties shapeProperties = series.getShapeProperties();
        if (shapeProperties == null) shapeProperties = new XDDFShapeProperties();
        shapeProperties.setLineProperties(lineProperties);
        series.setShapeProperties(shapeProperties);
    }




    public  void hello() throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "Sheet1";
        FileOutputStream fileOut = null;
        try {
            XSSFSheet sheet = wb.createSheet(sheetName);
            // 第一行，表头名称
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("a");
            cell = row.createCell(1);
            cell.setCellValue("b");
            cell = row.createCell(2);
            cell.setCellValue("c");
            // 数据1
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue(1);
            cell = row.createCell(1);
            cell.setCellValue(1);
            cell = row.createCell(2);
            cell.setCellValue(2);
            // 数据2
            row = sheet.createRow(2);
            cell = row.createCell(0);
            cell.setCellValue(2);
            cell = row.createCell(1);
            // cell.setCellValue(2);
            cell = row.createCell(2);
            cell.setCellValue(3);
            // 数据3
            row = sheet.createRow(3);
            cell = row.createCell(0);
            cell.setCellValue(3);
            cell = row.createCell(1);
            cell.setCellValue(3);
            cell = row.createCell(2);
            // cell.setCellValue(4);
            // 数据4
            row = sheet.createRow(4);
            cell = row.createCell(0);
            cell.setCellValue(4);
            cell = row.createCell(1);
            cell.setCellValue(4);
            cell = row.createCell(2);
            cell.setCellValue(5);
            // 数据5
            row = sheet.createRow(5);
            cell = row.createCell(0);
            cell.setCellValue(5);
            cell = row.createCell(1);
            cell.setCellValue(5);
            cell = row.createCell(2);
            cell.setCellValue(5);
            // 数据6
            row = sheet.createRow(6);
            cell = row.createCell(0);
            cell.setCellValue(6);
            cell = row.createCell(1);
            cell.setCellValue(6);
            cell = row.createCell(2);
            cell.setCellValue(5);
            // 数据7
            row = sheet.createRow(7);
            cell = row.createCell(0);
            cell.setCellValue(7);
            cell = row.createCell(1);
            cell.setCellValue(7);
            cell = row.createCell(2);
            cell.setCellValue(5);

            // 创建一个画布
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            // 前四个默认0，[8,7]：从8列7行开始;[15,22]:到15列22行结束
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 8, 7, 15, 22);
            // 创建一个chart对象
            XSSFChart chart = drawing.createChart(anchor);

            // 设置画布边框样式
            CTChartSpace space = chart.getCTChartSpace();
            space.addNewRoundedCorners().setVal(false);// 去掉圆角
            CTLineProperties ln = space.addNewSpPr().addNewLn();
            CTSolidColorFillProperties solidFill = ln.addNewSolidFill();
            Color color = new Color(217, 217, 217);// 灰色边框
            solidFill.addNewSrgbClr().setVal(new byte[] { (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue() });

            // 标题
            chart.setTitleText("图表标题");
            // 标题覆盖
            chart.setTitleOverlay(false);

            // 图例位置
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP);

            // 分类轴标(X轴),标题位置,XDDFCategoryAxis会乱码
            XDDFValueAxis bottomAxis = chart.createValueAxis(AxisPosition.BOTTOM);
            // bottomAxis.setMinimum(0);
            // 设置左X轴最大值
            // bottomAxis.setMaximum(8);
            // bottomAxis.setTitle("X轴标题");
            // 值(Y轴)轴,标题位置
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            // leftAxis.setTitle("Y轴标题");

            // 左Y轴和X轴交叉点在X轴0点位置
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
            // leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
            // 构建坐标轴
            leftAxis.crossAxis(bottomAxis);
            bottomAxis.crossAxis(leftAxis);
            // 设置左Y轴最大值
            // leftAxis.setMaximum(8);
            // leftAxis.setVisible(false);// 隐藏Y轴

            // CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
            // 分类轴标(X轴)数据，单元格范围位置[0, 1]到[0, 7]
            // XDDFDataSource<Double> category = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 7, 0, 0));
            XDDFNumericalDataSource<Integer> category = XDDFDataSourcesFactory.fromArray(new Integer[] { 1, 2, 3, 4, 5, 6, 7 });

            // 数据1，单元格范围位置[1, 1]到[1, 7]
            XDDFNumericalDataSource<Double> data1 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 7, 1, 1));
            // XDDFNumericalDataSource data1 = XDDFDataSourcesFactory.fromArray(new Integer[] {1,2,3,4,5,6,7});
            // 数据2，单元格范围位置[2, 1]到[2, 7]
            XDDFNumericalDataSource<Double> data2 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 7, 2, 2));
            // XDDFNumericalDataSource<Integer> data2 = XDDFDataSourcesFactory.fromArray(new Integer[] {2,3,4,5,5,5,5});

            // Scatter：散点图
            XDDFScatterChartData scatter = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);
            // 设置为可变颜色
            // scatter.setVaryColors(true);
            scatter.setStyle(ScatterStyle.LINE_MARKER);

            // 图表加载数据，数据1
            XDDFScatterChartData.Series series1 = (XDDFScatterChartData.Series) scatter.addSeries(category, data1);
            // 散点图例标题
            series1.setTitle("图例标题b", null);
            // XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.YELLOW));
            XDDFNoFillProperties fill = new XDDFNoFillProperties();
            // 散点图
            series1.setFillProperties(fill);
            series1.setSmooth(false);
            series1.setMarkerStyle(MarkerStyle.CIRCLE);
            series1.setMarkerSize((short) 5);
            // 绘制
            series1.plot();
            // 设置颜色
            color = new Color(255, 0, 0);// 红色
            byte[] colorByte = new byte[] { (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue() };
            CTScatterChart scatterChart = chart.getCTChart().getPlotArea().getScatterChartArray(0);
            CTScatterSer ser = scatterChart.getSerArray(0);
            CTShapeProperties spPr = ser.getMarker().addNewSpPr();
            spPr.addNewSolidFill().addNewSrgbClr().setVal(colorByte);
            // 无轮廓
            spPr.addNewLn().addNewNoFill();
            // 隐藏值为空或者0的点
            for (int i = 0; i < data1.getPointCount(); i++) {
                Double pointAt = data1.getPointAt(i);
                if (pointAt == null || pointAt == 0) {
                    CTDPt dDPt = ser.addNewDPt();
                    dDPt.addNewIdx().setVal(i);
                    CTShapeProperties addNewSpPr = dDPt.addNewMarker().addNewSpPr();
                    addNewSpPr.addNewNoFill();
                    addNewSpPr.addNewLn().addNewNoFill();
                }
            }

            // 图表加载数据，数据2
            XDDFScatterChartData.Series series2 = (XDDFScatterChartData.Series) scatter.addSeries(category, data2);
            // 散点图例标题
            series2.setTitle("图例标题c", null);
            // XDDFSolidFillProperties fill2 = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED));
            XDDFNoFillProperties fill2 = new XDDFNoFillProperties();
            // 散点图
            series2.setFillProperties(fill2);
            series2.setSmooth(false);
            series2.setMarkerStyle(MarkerStyle.CIRCLE);
            series2.setMarkerSize((short) 5);
            // 绘制
            series2.plot();
            // 设置颜色
            color = new Color(0, 0, 139);// 深蓝色
            colorByte = new byte[] { (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue() };
            scatterChart = chart.getCTChart().getPlotArea().getScatterChartArray(0);
            ser = scatterChart.getSerArray(1);
            spPr = ser.getMarker().addNewSpPr();
            spPr.addNewSolidFill().addNewSrgbClr().setVal(colorByte);
            // 无轮廓
            spPr.addNewLn().addNewNoFill();
            // 隐藏值为空或者0的点
            for (int i = 0; i < data2.getPointCount(); i++) {
                Double pointAt = data2.getPointAt(i);
                if (pointAt == null || pointAt <= 0) {
                    CTDPt dDPt = ser.addNewDPt();
                    dDPt.addNewIdx().setVal(i);
                    CTShapeProperties addNewSpPr = dDPt.addNewMarker().addNewSpPr();
                    addNewSpPr.addNewNoFill();
                    addNewSpPr.addNewLn().addNewNoFill();
                }
            }

            // 绘制
            // chart.plot(scatter);

            // 去掉连接线
            java.util.List<CTScatterSer> serList = scatterChart.getSerList();
            for (CTScatterSer sser : serList) {
                sser.getSpPr().addNewLn().addNewNoFill();
                // 删掉x轴系列值
                sser.unsetXVal();
            }
            Enum bg2 = org.openxmlformats.schemas.drawingml.x2006.main.STSchemeColorVal.Enum.forString("bg2");
            // 设置网格线水平方向
            java.util.List<CTValAx> valAxList = chart.getCTChart().getPlotArea().getValAxList();
            for (CTValAx valAx : valAxList) {
                valAx.addNewMajorGridlines().addNewSpPr().addNewLn().addNewSolidFill().addNewSchemeClr().setVal(bg2);
            }
            // 设置网格线垂直方向
            java.util.List<CTCatAx> catAxList = chart.getCTChart().getPlotArea().getCatAxList();
            for (CTCatAx catAx : catAxList) {
                catAx.addNewMajorGridlines().addNewSpPr().addNewLn().addNewSolidFill().addNewSchemeClr().setVal(bg2);
            }

            // 打印图表的xml
            System.out.println(chart.getCTChart());
            System.out.println();
            System.out.println("***********************************************************");
            System.out.println();
            System.out.println("上面打印的xml节点就是 chart.getCTChart() 方法得到的api一一对应");
            System.out.println();
            System.out.println("***********************************************************");

            // 将输出写入excel文件
            String filename = "F://散点图ceshi.xlsx";
            fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wb.close();
            if (fileOut != null) {
                fileOut.close();
            }
        }

    }
}
