package com.rep.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 检查数据合法性
 *
 * @author  dxh
 */
@Service
public class ExamineDataService {

    private static final Logger logger = LoggerFactory.getLogger(ExamineDataService.class);

    public boolean checkJSONObject(JSONObject param,String key){
        try{
            return param.containsKey(key)&&param.getJSONObject(key)!=null;
        }catch (Exception e){
            logger.info("checkJSONObject 出现错误");
            return false;
        }
    }
    public boolean checkJSONArray(JSONObject param,String key){
        try{
            return param.containsKey(key)&&param.getJSONArray(key)!=null;
        }catch (Exception e){
            logger.info("checkJSONArray 出现错误");
            return false;
        }
    }
    public boolean checkString(JSONObject param,String key){
        try{
            return param.containsKey(key)&&param.getString(key)!=null&&!"".equals(param.getString(key));
        }catch (Exception e){
            logger.info("checkString 出现错误");
            return false;
        }
    }

    /**
     * 检查递减曲线的数据
     * @param param
     * @return
     */
    public boolean checkDeclineCurveData(JSONObject param){
        return checkJSONObject(param,"title")&&
//                checkJSONArray(param,"dataset")&&
                checkJSONArray(param,"xAxis")&&
                checkJSONArray(param,"yAxis");
    }

    /**
     *  检查单井数据
     * @param param
     * @return
     */
    public boolean checkSingleWellProductionData(JSONObject param){

        return checkJSONArray(param.getJSONObject("dataset"),"source")&&
                checkJSONObject(param,"title");

    }

    /**
     *  检查井组数据
     * @param param
     * @return
     */
    public boolean checkWellProductionData(JSONObject param){

        return
                checkJSONObject(param,"title");

    }

    /**
     *  检查全区开采数据
     * @param param
     * @return
     */
    public boolean checkDevelopData(JSONObject param){

        return checkJSONArray(param.getJSONObject("dataset"),"source")&&
                checkJSONObject(param,"title");

    }
    /**
     *  检查全区注水数据
     * @param param
     * @return
     */
    public boolean checkWaterData(JSONObject param){

        return checkJSONArray(param.getJSONObject("dataset"),"source")&&
                checkJSONObject(param,"title");

    }


}
