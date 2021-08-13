package com.example.algorithm;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 计算井底流压和油藏静压
 *
 * @author  dxh
 */
@RestController
@RequestMapping("/calculateData")
public class CalculateData{

    private  static final double g=9.80;


    @RequestMapping("/dynamic")
    public JSONArray calculateDynamicData(JSONArray jsonArray,double waterDensity,double oilDensity){
        int length=jsonArray.size();

        for(int i=0;i<length;i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            double movingLiquidSurface=jsonObject.getDouble("movingLiquidSurface");//动液面，单位m
            double waterContent=jsonObject.getDouble("waterContent");
            double staticLiquidSurface=jsonObject.getDouble("staticLiquidSurface");//静液面，单位m

            double mixedDensity=waterDensity*0.6+oilDensity*0.4*1000;//混合流体密度，单位kg/m^3


            double oilPress=mixedDensity*g*staticLiquidSurface/1000000;//计算油藏静压，单位MPa
            double bottomPress=mixedDensity*g*movingLiquidSurface/1000000;//计算井底流压，单位MPa
            jsonArray.getJSONObject(i).put("oilPress",oilPress);
            jsonArray.getJSONObject(i).put("bottomPress",bottomPress);
        }

        return jsonArray;
    }



}
