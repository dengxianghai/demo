package com.rep.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rep.controller.ExportController;
import com.rep.utils.ExportExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 数据处理层，将获取json进行处理
 *
 *
 * @author  dxh
 * @date  2021/6/15
 *
 */
@Service
public class ExportExcelService {

    private static final Logger logger = LoggerFactory.getLogger(ExportExcelService.class);
//    @Autowired
//    private ExportExcelUtils exportExcelUtils;

    /**
     * 组织rep递减曲线数据
     *
     * @param resp http响应
     * @param param json数据
     * @throws IOException
     */
    public void organizeDeclineCurveData(HttpServletResponse resp, JSONObject param) throws IOException {
        logger.info("进入减曲线/水驱");
        JSONArray source=param.getJSONObject("dataset").getJSONArray("source");

        String [][]dataList=new String[source.size()][3];
        for(int i=0;i<source.size();i++){
            dataList [i][0]=source.getJSONObject(i).getString("date");
            dataList [i][1]=source.getJSONObject(i).getString("pre");

            dataList [i][2]=source.getJSONObject(i).getString("q");
        }
        //获取x轴名字
        JSONObject xAxis=param.getJSONArray("xAxis").getJSONObject(0);
//        String xName=xAxis.getString("name");
        String xName=getValue(xAxis,"name");

        //获取y轴名字
        JSONObject yAxis=param.getJSONArray("yAxis").getJSONObject(0);
        String yName=getValue(yAxis,"name");

        //获取表格列名
        LinkedList headerList=new LinkedList();
        headerList.add(xName);

        LinkedList colorList=new LinkedList();
        JSONArray series=param.getJSONArray("series");
        for(int i=0;i<series.size();i++){
            headerList.add(series.getJSONObject(i).getString("name"));
            colorList.add(series.getJSONObject(i).getString("color"));
        }

        String title=param.getJSONObject("title").getString("text");
        ExportExcelUtils.createOneYaxisLinePointChart( headerList,dataList,
                       title,xName,yName, resp,true,series);

    }


    /**
     *   组织单井生产现状数据
     *
     * @param resp
     * @param param
     * @throws IOException
     */
    public void organizeSingleWellProductionData(HttpServletResponse resp, JSONObject param) throws IOException {
        logger.info("进入单井");
        JSONArray source=param.getJSONObject("dataset").getJSONArray("source");
        String [][]dataList=new String[source.size()][9];

        //获取x轴名字
//        JSONObject xAxis=param.getJSONObject("xAxis");
//        String xName=getValue(xAxis,"name");
        //TODO:x轴名字如何获取
        String xName="x";
        //获取表格列名
        LinkedList<String> headerList=new LinkedList();
        headerList.add(xName);

        //获取y轴名字
        LinkedList<String> leftY=new LinkedList();
        LinkedList<String> rightY=new LinkedList();
        JSONArray yAxis=param.getJSONArray("yAxis");
        for(int i=0;i<yAxis.size();i++){
            JSONObject y=yAxis.getJSONObject(i);
            String name=getValue(y,"name");
            String position=y.getString("position");
            if(position.equals("left")){
                leftY.add(name);
            }else{
                rightY.add(name);
            }
        }
        LinkedList<String> seriesName=new LinkedList<>();
        for(int j=0;j<leftY.size();j++){
            seriesName.add(singleWellProductionChangeSeries(leftY.get(j)));
            seriesName.add(singleWellProductionChangeSeries(rightY.get(j)));
            headerList.add(singleWellProductionChangeSeries(leftY.get(j)));
            headerList.add(singleWellProductionChangeSeries(rightY.get(j)));
        }

        for(int i=0;i<source.size();i++){
            dataList [i][0]=source.getJSONObject(i).getString("date");
            for(int j=0;j<leftY.size();j++){
                dataList [i][2*j+1]=getValue(source.getJSONObject(i),singleWellProductionChange(leftY.get(j)));//左边y轴
//                dataList [i][2*j+1]=source.getJSONObject(i).getString(singleWellProductionChange(leftY.get(j)));//左边y轴
                dataList [i][2*j+2]=source.getJSONObject(i).getString(singleWellProductionChange(rightY.get(j)));//右边y
                dataList [i][2*j+2]=getValue(source.getJSONObject(i),singleWellProductionChange(rightY.get(j)));//右边y
            }
        }

        LinkedList colorList=new LinkedList();
        JSONArray series=param.getJSONArray("series");
        for(int i=0;i<series.size();i++){
            colorList.add(series.getJSONObject(i).getString("color"));
        }
        String title=param.getJSONObject("title").getString("text");
        ExportExcelUtils.createTwoChart( headerList,dataList,
               title,xName, resp,series,leftY,rightY,seriesName);
    }

    /**
     *   组织井组生产现状数据
     *
     * @param resp
     * @param param
     * @throws IOException
     */
    public void organizeWellProductionData(HttpServletResponse resp, JSONObject param) throws IOException {
        logger.info("进入井组service");
        JSONArray dataset=param.getJSONArray("dataset");
//        JSONArray source=param.getJSONObject("dataset").getJSONArray("source");
        LinkedList<HashMap> group=new LinkedList();

        for(int i=0;i<dataset.size();i++){
            HashMap map=new HashMap();
            map.put("name",dataset.getJSONObject(i).getJSONArray("source").getJSONObject(0).getString("uwi"));
            JSONArray source=dataset.getJSONObject(i).getJSONArray("source");
            String [][]dataList=new String[source.size()][7];

            for(int j=0;j<source.size();j++){
                JSONObject jsonObject=source.getJSONObject(j);
                dataList[j][0]=jsonObject.getString("uwi");//井名
                dataList[j][1]=jsonObject.getString("date");
                dataList[j][2]=jsonObject.getString("oil");//月产油
                dataList[j][3]=jsonObject.getString("water");//月产水
                dataList[j][4]=jsonObject.getString("gas");//月产气
                dataList[j][5]=jsonObject.getString("waterIn");//月注水
//                dataList[j][6]=jsonObject.getString("allocationWaterIn");//月配注
                dataList[j][6]=getValue(jsonObject,"allocationWaterIn");
            }
            map.put("dataList",dataList);

            group.add(map);
        }

        //获取x轴名字
//        JSONObject xAxis=param.getJSONObject("xAxis");
//        String xName=getValue(xAxis,"name");
        //TODO:x轴名字如何获取
        String xName="date";

        //获取表格列名
        LinkedList<String> headerList=new LinkedList();
        headerList.add("井号");
        headerList.add(xName);
        headerList.add("月产油(t)");
        headerList.add("月产水");
        headerList.add("月产气");
        headerList.add("月注水");
        headerList.add("月配注");

        //获取y轴名字
        LinkedList<String> leftY=new LinkedList();
        JSONArray yAxis=param.getJSONArray("yAxis");
        for(int i=0;i<yAxis.size();i++){
            JSONObject y=yAxis.getJSONObject(i);
            String name=getValue(y,"name");
            leftY.add(name);
        }

//        LinkedList<String> seriesName=new LinkedList<>();
//        for(int j=0;j<leftY.size();j++){
//            seriesName.add(singleWellProductionChangeSeries(leftY.get(j)));
//            headerList.add(singleWellProductionChangeSeries(leftY.get(j)));
//        }

//        for(int i=0;i<source.size();i++){
//            dataList [i][0]=source.getJSONObject(i).getString("date");
//            for(int j=0;j<leftY.size();j++){
//                dataList [i][2*j+1]=getValue(source.getJSONObject(i),singleWellProductionChange(leftY.get(j)));//左边y轴
////                dataList [i][2*j+1]=source.getJSONObject(i).getString(singleWellProductionChange(leftY.get(j)));//左边y轴
////                dataList [i][2*j+2]=source.getJSONObject(i).getString(singleWellProductionChange(rightY.get(j)));//右边y
////                dataList [i][2*j+2]=getValue(source.getJSONObject(i),singleWellProductionChange(rightY.get(j)));//右边y
//            }
//        }

        //TODO:颜色获取series变成了中文？？？
        LinkedList<String> colorList=new LinkedList();
        colorList.add("#5793f3");
        colorList.add("#A86C0B");
        colorList.add("#9F9F9F");
        colorList.add("#FF9A5B");
        colorList.add("#BE9EFF");

        JSONArray series=param.getJSONArray("series");

        String title=param.getJSONObject("title").getString("text");
        ExportExcelUtils.createManyPicOneYaxisManyLineChart( headerList,group,
                title,xName, resp,series,leftY,colorList);
    }


    /**
     *   组织全区开采数据
     *
     * @param resp
     * @param param
     * @throws IOException
     */
    public void organizeDevelopData(HttpServletResponse resp, JSONObject param) throws IOException {
        logger.info("进入全区开采service");
        JSONArray source=param.getJSONObject("dataset").getJSONArray("source");
        JSONArray series=param.getJSONArray("series");

        HashMap<String,String> colorMap=new HashMap();
        for(int i=0;i<series.size();i++){
            String y=series.getJSONObject(i).getJSONObject("encode").getString("y");
            String color=series.getJSONObject(i).getJSONObject("itemStyle").getJSONObject("normal").getString("color");
            colorMap.put(y,color);
        }

        String [][]wellData=new String[source.size()][3];
        String [][]monthData=new String[source.size()][5];
        String [][]dayBlockOil=new String[source.size()][2];
        String [][]daySingleOil=new String[source.size()][2];
        String [][]comWaterPercent=new String[source.size()][2];
        String [][]injProdRatio=new String[source.size()][2];//月注采比

        for(int i=0;i<source.size();i++){
            JSONObject jsonObject=source.getJSONObject(i);

            wellData[i][0]=jsonObject.getString("date");
            wellData[i][1]=getValue(jsonObject,"openWaterWell");
            wellData[i][2]=getValue(jsonObject,"openOilWell");

            monthData[i][0]=jsonObject.getString("date");
            monthData[i][1]=getValue(jsonObject,"oil");
            monthData[i][2]=getValue(jsonObject,"water");
            monthData[i][3]=getValue(jsonObject,"gas");
            monthData[i][4]=getValue(jsonObject,"waterIn");

            dayBlockOil[i][0]=jsonObject.getString("date");
            dayBlockOil[i][1]=getValue(jsonObject,"dayBlockOil");

            daySingleOil[i][0]=jsonObject.getString("date");
            daySingleOil[i][1]=getValue(jsonObject,"daySingleOil");

            comWaterPercent[i][0]=jsonObject.getString("date");
            comWaterPercent[i][1]=getValue(jsonObject,"comWaterPercent");

            injProdRatio[i][0]=jsonObject.getString("date");
            injProdRatio[i][1]=getValue(jsonObject,"injProdRatio");
        }

        //获取x轴名字
//        JSONObject xAxis=param.getJSONObject("xAxis");
//        String xName=getValue(xAxis,"name");
        //TODO:x轴名字如何获取
        String xName="date";
        //获取表格列名
        LinkedList<String> headerList=new LinkedList();
        headerList.add(xName);
        //有序的数据列表
        LinkedList <HashMap> group=new LinkedList<>();

        //获取y轴名字
        LinkedList<String> leftY=new LinkedList();
        JSONArray yAxis=param.getJSONArray("yAxis");
        for(int i=0;i<yAxis.size();i++){
            JSONObject y=yAxis.getJSONObject(i);
            String name=getValue(y,"name");
            leftY.add(name);
            //按照y轴顺序，获取列表名和数据集
            HashMap map=new HashMap();
            LinkedList<String> seriesName=new LinkedList<>();
            LinkedList<String> colorList=new LinkedList<>();
            switch (name){
                case "开井数据":
                    seriesName.add("水井开井数");
                    seriesName.add("油井开井数");
                    map.put("seriesName",seriesName);

                    map.put("dataList",wellData);

                    colorList.add(colorMap.get("openWaterWell"));
                    colorList.add(colorMap.get("openOilWell"));
                    map.put("colorList",colorList);

                    headerList.add("水井开井数(口)");
                    headerList.add("油井开井数(口)");
                    break;
                case "月产数据":
                    seriesName.add("月产油");
                    seriesName.add("月产水");
                    seriesName.add("月产气");
                    seriesName.add("月注水");
                    map.put("seriesName",seriesName);

                    map.put("dataList",monthData);

                    colorList.add(colorMap.get("oil"));
                    colorList.add(colorMap.get("water"));
                    colorList.add(colorMap.get("gas"));
                    colorList.add(colorMap.get("waterIn"));
                    map.put("colorList",colorList);

                    headerList.add("月产油");
                    headerList.add("月产水");
                    headerList.add("月产气");
                    headerList.add("月注水");
                    break;
                case "区块日油":
                    seriesName.add("区块日油");
                    map.put("seriesName",seriesName);
                    map.put("dataList",dayBlockOil);

                    colorList.add(colorMap.get("dayBlockOil"));
                    map.put("colorList",colorList);

                    headerList.add("区块日油");
                    break;
                case "单井日油":
                    seriesName.add("单井日油");
                    map.put("seriesName",seriesName);
                    map.put("dataList",daySingleOil);

                    colorList.add(colorMap.get("daySingleOil"));
                    map.put("colorList",colorList);

                    headerList.add("单井日油");
                    break;
                case "综合含水":
                    seriesName.add("综合含水");
                    map.put("seriesName",seriesName);
                    map.put("dataList",comWaterPercent);

                    colorList.add(colorMap.get("comWaterPercent"));
                    map.put("colorList",colorList);

                    headerList.add("综合含水");
                    break;
                case "月注采比":
                    seriesName.add("月注采比");
                    map.put("seriesName",seriesName);
                    map.put("dataList",injProdRatio);

                    colorList.add(colorMap.get("injProdRatio"));
                    map.put("colorList",colorList);

                    headerList.add("月注采比");
                    break;
            }
            group.add(map);
        }

//        LinkedList colorList=new LinkedList();
//        for(int i=0;i<series.size();i++){
//            colorList.add(series.getJSONObject(i).getString("color"));
//        }
        String title=param.getJSONObject("title").getString("text");


        ExportExcelUtils.createManyPicManyLineChart( headerList,group,
                title,xName, resp,series,leftY);
    }

    /**
     * 全区开采状况中，根据y轴名称，获取图像内线条的信息
     * （包括条数和键值对的键名）
     *
     * @param name  y轴名称
     * @return
     */
    public HashMap getSeriesKeyByYNameForWholeDistrictDevelop(String name){
        
        int length = 0;
        String []strings = new String[0];
        switch (name){
            case"开井数据":
                length=2;
                strings = new String[length];
                strings[0]="openWaterWell";
                strings[1]="openOilWell";
                break;
            case"月产数据":
                length=4;
                strings = new String[length];
                strings[0]="oil";
                strings[1]="water";
                strings[2]="gas";
                strings[3]="waterIn";
                break;
            case"区块日油":
                length=1;
                strings = new String[length];
                strings[0]="dayBlockOil";
                break;
            case"单井日油":
                length=1;
                strings = new String[length];
                strings[0]="daySingleOil";
                break;
            case"综合含水":
                length=1;
                strings = new String[length];
                strings[0]="comWaterPercent";
                break;
            case"月注采比":
                length=1;
                strings = new String[length];
                strings[0]="injProdRatio";
                break;
        }
        HashMap map=new HashMap();
        map.put("length",length);
        map.put("key",strings);
        return map;

    }


    /**
     *   组织全区注水数据
     *
     * @param resp
     * @param param
     * @throws IOException
     */
    public void organizeWaterData(HttpServletResponse resp, JSONObject param) throws IOException {
        logger.info("进入全区注水service");
        JSONArray source=param.getJSONObject("dataset").getJSONArray("source");
        String [][]dataList=new String[source.size()][7];

        //获取x轴名字
//        JSONObject xAxis=param.getJSONObject("xAxis");
//        String xName=getValue(xAxis,"name");
        //TODO:x轴名字如何获取
        String xName="x";
        //获取表格列名
        LinkedList<String> headerList=new LinkedList();
        headerList.add(xName);
        //获取y轴名字
        LinkedList<String> leftY=new LinkedList();

        JSONArray yAxis=param.getJSONArray("yAxis");
        LinkedList<String> colorList=new LinkedList();
        for(int i=0;i<yAxis.size();i++){
            JSONObject y=yAxis.getJSONObject(i);
            String name=getValue(y,"name");
            leftY.add(name);
            colorList.add(y.getJSONObject("axisLine").getJSONObject("lineStyle").getString("color"));
        }
        //图例名称
        LinkedList<String> seriesName=new LinkedList<>();

        for(int j=0;j<leftY.size();j++){
            seriesName.add((leftY.get(j)));
            headerList.add((leftY.get(j)));
        }

        for(int i=0;i<source.size();i++){
            dataList [i][0]=source.getJSONObject(i).getString("date");
            for(int j=0;j<leftY.size();j++){
                dataList [i][j+1]=getValue(source.getJSONObject(i),singleWaterChange(leftY.get(j)));//左边y轴
            }
        }

        JSONArray series=param.getJSONArray("series");
        //标题
        String title=param.getJSONObject("title").getString("text");
        ExportExcelUtils.createManyOneYaxisLineChart( headerList,dataList,
                title,xName, resp,series,seriesName,leftY,colorList);
    }



    /**
     * 判断json是否有键，或者是否为空
     * @param jsonObject
     * @param key
     * @return
     */
    public String getValue(JSONObject jsonObject,String key){
        if(jsonObject.containsKey(key)&&jsonObject.getString(key)!=null){
               return jsonObject.getString(key);
        }else{
            return "0";
        }
    }

    /**
     * 单井的y轴转换
     * @param yName
     * @return
     */
    public String singleWellProductionChange(String yName){
        String dataName=null;
        switch (yName){
            case "日产油":
                dataName="oil";
                break;
            case "静压":
                dataName="static_pressure";
                break;
            case "日注水":
                dataName="water_in";
                break;
            case "动液面":
                dataName="dynamic_level";
                break;
            case "套压":
                dataName="casing_pressure";
                break;
            case "油压":
                dataName="oil_pressure";
                break;
            case "日产气":
                dataName="gas";
                break;
            case "日产水":
                dataName="water";
                break;

        }
        return dataName;
    }

    /**
     * 单井的y轴转换
     * @param yName
     * @return
     */
    public String singleWellProductionChangeSeries(String yName){
        String seriesName=null;
        switch (yName){
            case "日产油":
                seriesName="日产油(t)";
                break;
            case "静压":
                seriesName="静压";
                break;
            case "日注水":
                seriesName="日注水(m³)";
                break;
            case "动液面":
                seriesName="动液面(m)";
                break;
            case "套压":
                seriesName="套压";
                break;
            case "油压":
                seriesName="油压";
                break;
            case "日产气":
                seriesName="日产气(m³)";
                break;
            case "日产水":
                seriesName="日产水(t)";
                break;

        }
        return seriesName;
    }

    /**
     * 全区注水的y轴，转换中文为英文
     * @param yName
     * @return
     */
    public String singleWaterChange(String yName){
        String dataName=null;
        switch (yName){
            case "水井开井数":
                dataName="openOilWell";
                break;
            case "单井日注":
                dataName="daySingleWaterIn";
                break;
            case "区块日注":
                dataName="dayBlockWaterIn";
                break;
            case "月注水":
                dataName="waterIn";
                break;
            case "月注采比":
                dataName="injProdRatio";
                break;
            case "累计注采比":
                dataName="cumInjProdRatio";
                break;
        }
        return dataName;
    }

}
