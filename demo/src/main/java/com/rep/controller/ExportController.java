package com.rep.controller;


import com.alibaba.fastjson.JSONObject;
import com.rep.service.ExamineDataService;
import com.rep.service.ExportExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * http接口生成Excel，获取数据
 *
 *
 *
 * @author  dxh
 * @date 2021/6/15
 *
 */
@RestController
@RequestMapping("export")
public class ExportController {

    private static final Logger logger = LoggerFactory.getLogger(ExportController.class);

    @Autowired
    private ExamineDataService examineDataService;

    @Autowired
    private ExportExcelService exportExcelService;


    /**
     *  根据不同业务生成的Excel接口
     * @param req
     * @param resp
     * @param param
     * @throws Exception
     */
    @RequestMapping("exportExcel")
    public void exportExcel(HttpServletRequest req, HttpServletResponse resp,
                           @RequestBody JSONObject param) throws Exception {

        logger.info("进入接口exportExcel：");
        //获取业务类型
        String businessType=param.getString("businessType");
        boolean isDataCorrect=false;
        switch (businessType){
            case "declineCurve"://递减曲线
                isDataCorrect=examineDataService.checkDeclineCurveData(param);
                break;
            case "waterDrive"://水驱曲线
                isDataCorrect=examineDataService.checkDeclineCurveData(param);
                break;
            case  "singleWellProduction"://单井
                isDataCorrect=examineDataService.checkSingleWellProductionData(param);
                break;
            case  "wellProduction"://井组
                isDataCorrect=examineDataService.checkWellProductionData(param);
                break;
            case  "wholeDistrictDevelop"://全区开采
                isDataCorrect=examineDataService.checkDevelopData(param);
                break;
            case  "wholeDistrictWater"://全区注水
                isDataCorrect=examineDataService.checkWaterData(param);
                break;
        }
        if(isDataCorrect==false){
            logger.info("数据存在错误！");
            setWrongRes(resp,"数据存在错误！");
        }

        //生成Excel
        switch (businessType){
            case "declineCurve"://递减曲线
                exportExcelService.organizeDeclineCurveData(resp,param);
                break;
            case "waterDrive"://水驱曲线
                exportExcelService.organizeDeclineCurveData(resp,param);
                break;
            case  "singleWellProduction"://单井
                exportExcelService.organizeSingleWellProductionData(resp,param);
                break;
            case  "wellProduction"://井组
                exportExcelService.organizeWellProductionData(resp,param);
                break;
            case  "wholeDistrictDevelop"://全区开采
                exportExcelService.organizeDevelopData(resp,param);
                break;
            case  "wholeDistrictWater"://全区注水
                exportExcelService.organizeWaterData(resp,param);
                break;
        }
//        exportExcelService.organizeDeclineCurveData(resp,param);


    }


    public void setWrongRes(HttpServletResponse resp,String info) throws IOException {
        resp.setCharacterEncoding("utf-8");
        resp.sendError(500,info);
    }



}
