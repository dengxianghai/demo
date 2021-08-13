package com.excel.test;


import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Test {

    public static void main(String[] args) {
//        try {
//            Test2.bb();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        double a=0,b=0;
        a=62.0;
        System.out.println(2.0 / 1000 );
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


    /**
     * 创建excel
     *
     * @param listresult 是需要写入excel中的数据，通过map中的k-v来将数据写入excel
     * @return
     */
    private XSSFWorkbook createUserListExcel(List<Map<String, Object>> listresult, String[] titel) {
        // 1.创建HSSFWorkbook，一个HSSFWorkbook对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();

        // 2.在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet("sheet1");

        // 3.设置表头，即每个列的列名
//        String[] titel = {"rowName1","rowName2","rowName3","rowName4"};


        // 3.1创建第一行
        XSSFRow row = sheet.createRow(0);
        // 此处创建一个序号列
        row.createCell(0).setCellValue("序号");


        // 将列名写入
        for (int i = 0; i < titel.length; i++) {
            // 给列写入数据,创建单元格，写入数据
            row.createCell(i + 1).setCellValue(titel[i]);
        }
        // 写入正式数据
        for (int i = 0; i < listresult.size(); i++) {
            // 创建行
            row = sheet.createRow(i + 1);
            // 序号
            row.createCell(0).setCellValue(i + 1);

            row.createCell(1).setCellValue(listresult.get(i).get("rowKey1").toString());
            sheet.autoSizeColumn(1, true);

            row.createCell(2).setCellValue(listresult.get(i).get("rowKey2").toString());

            row.createCell(3).setCellValue(listresult.get(i).get("rowKey3").toString());

            row.createCell(4).setCellValue(listresult.get(i).get("rowKey4").toString());
        }
        /**
         * 上面的操作已经是生成一个完整的文件了，只需要将生成的流转换成文件即可；
         * 下面的设置宽度可有可无，对整体影响不大
         */
        // 设置单元格宽度
        int curColWidth = 0;
        for (int i = 0; i <= titel.length; i++) {
            // 列自适应宽度，对于中文半角不友好，如果列内包含中文需要对包含中文的重新设置。
            sheet.autoSizeColumn(i, true);
            // 为每一列设置一个最小值，方便中文显示
            curColWidth = sheet.getColumnWidth(i);
            if (curColWidth < 2500) {
                sheet.setColumnWidth(i, 2500);
            }
            // 第3列文字较多，设置较大点。
            sheet.setColumnWidth(3, 8000);
        }
        return wb;
    }
}
