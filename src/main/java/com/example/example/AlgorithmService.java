//package com.example.example;
//
//
//import com.baomidou.dynamic.datasource.annotation.DS;
//import com.bbc.rep.repcommon.algorithm.*;
//import com.bbc.rep.repcommon.constant.Constants;
//import com.bbc.rep.repcommon.constant.StatusEnum;
//import com.bbc.rep.repcommon.entity.Result;
//import com.bbc.rep.repcommon.exception.DataParserFailedException;
//import com.bbc.rep.repcommon.exception.NoDataException;
//import com.bbc.rep.repcommon.util.*;
//import com.bbc.rep.repdata.mapper.ResultMapper;
//import com.bbc.rep.repdata.mapper.SAllProdMonthMapper;
//import com.bbc.rep.repdata.mapper.SAllProdYearMapper;
//import com.bbc.rep.repdata.mapper.WellMapper;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.google.gson.JsonObject;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.text.ParseException;
//import java.util.*;
//
///**
// * Algorithm Service
// *
// * @author ccs
// * @date 2020/11/19
// */
//@Service
//@Slf4j
//public class AlgorithmService {
//
//    private WellMapper wellMapper;
//
//    private DeclineCurve declineCurve;
//
//    private DataService dataService;
//
//    private DevelopmentEffectEvaluation effectEvaluation;
//
//    private WaterRelationCurve waterRelationCurve;
//
//    private TechnicalBoundaries technical;
//
//    private PhysicalProperty physicalProperty;
//
//    private RelativePermeabilityCurve relativePermeabilityCurve;
//
//    private ResultMapper resultMapper;
//
//    private MaterialBalanceEquation materialBalanceEquation;
//
//    private GetNr getNr;
//
//    private SAllProdYearMapper sAllProdYearMapper;
//
//    private SAllProdMonthMapper sAllProdMonthMapper;
//
//    /**
//     * 从数据库中获取数据计算器相关性
//     */
//    @DS("#session.dbname")
//    public ObjectNode productionCorrelation(String well,
//                                            String start,
//                                            String end,
//                                            String params,
//                                            int type) {
//        List<Map<String, Object>> data = null;
//        if (type == Constants.DAY_TYPE) {
//            data = wellMapper.selectDayProductionData(well, params, start, end);
//        }
//        if (type == Constants.MONTH_TYPE) {
//            data = wellMapper.selectMonthProductionData(well, params, start, end);
//        }
//        if (data != null) {
//            String[] fields = params.split(",");
//            double[][] numbers = new double[2][data.size()];
//            int i = 0;
//            for (Map<String, Object> item : data) {
//                numbers[0][i] = Double.parseDouble(String.valueOf(item.get(fields[0])));
//                numbers[1][i] = Double.parseDouble(String.valueOf(item.get(fields[1])));
//                i++;
//            }
//            return ResponseUtil.successJson(CommonAlgorithm.correlationCoefficient(numbers[0], numbers[1]));
//        }
//        return ResponseUtil.successJson();
//    }
//
//    /**
//     * 计算用户上传的自定义数据相关性
//     *
//     * @param body 上传参数
//     * @return 相关性
//     */
//    public ObjectNode correlation(ObjectNode body) {
//        try {
//            List<Double> array1 = JsonUtil.convertToDoubleList(body.get("array1"));
//            List<Double> array2 = JsonUtil.convertToDoubleList(body.get("array2"));
//            return ResponseUtil.successJson(CommonAlgorithm.correlationCoefficient(array1, array2));
//        } catch (JsonProcessingException e) {
//            return ResponseUtil.successJson();
//        }
//    }
//
//    /**
//     * 计算递减曲线
//     *
//     * @param wellId 井id
//     * @param n      根据n选择不同递减算法
//     * @return 计算结果
//     */
//    public ObjectNode computeDeclineCurve(String wellId,
//                                          String startDate,
//                                          String endDate,
//                                          String n,
//                                          int type,
//                                          JsonNode list) {
//        ArrayList<Double> t = new ArrayList<>();
//        ArrayList<Double> q = new ArrayList<>();
//        if (list == null){
//            List<Map<String, Object>> oilData = null;
//            if ((StringUtil.isNullOrEmpty(startDate) && StringUtil.isNullOrEmpty(endDate)) || StringUtil.isNullOrEmpty(wellId)) {
//                // 获取全区数据
//                oilData = dataService.getAverOilData();
//            } else {
//                oilData = dataService.getWellData(wellId, startDate, endDate, "oil", type);
//            }
//            if (oilData.isEmpty()) {
//                return ResponseUtil.errorJson(StatusEnum.E_3001);
//            }
//            String dataStartDate = (String) oilData.get(0).get("date");
//            for (Map<String, Object> item : oilData) {
//                if (item.get("date") == null || item.get("oil") == null) {
//                    continue;
//                }
//                try {
//                    if (type == Constants.DAY_TYPE) {
//                        t.add(Double.parseDouble(String.valueOf(DateTimeUtil.getDaysBetween(dataStartDate, item.get("date").toString()))));
//                    }
//                    if (type == Constants.MONTH_TYPE) {
//                        t.add(Double.parseDouble(String.valueOf(DateTimeUtil.betweenMonth(dataStartDate, item.get("date").toString(), true))));
//                    }
//                    q.add(Double.valueOf(item.get("oil").toString()));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            try {
//                List<Object> data = JsonUtil.convertToList(list);
//                for (var item: data) {
//                    LinkedHashMap<String, Object> mapItem = CastUtil.cast(item);
//                    if(CommonUtil.isNumeric(mapItem.get("q").toString())){
//                        t.add(Double.parseDouble(mapItem.get("t").toString()));
//                        q.add(Double.parseDouble(mapItem.get("q").toString()));
//                    }
//                }
//
//            }catch (JsonProcessingException e){
//                throw new DataParserFailedException();
//            }
//        }
//        double[] doubleT = new double[t.size()];
//        double[] doubleQ = new double[q.size()];
//        for (int i = 0; i < Math.min(t.size(), q.size()); i++) {
//            doubleT[i] = t.get(i);
//            doubleQ[i] = q.get(i);
//        }
//        switch (n) {
//            case "0":
//                return ResponseUtil.successJson(declineCurve.computeExponentialDeclineParams(doubleT, doubleQ));
//            case "1":
//                return ResponseUtil.successJson(declineCurve.computeHarmonicDeclineParams(doubleT, doubleQ));
//            case "0.5":
//                return ResponseUtil.successJson(declineCurve.computeAttenuationDeclineParams(doubleT, doubleQ));
//            case "-0.5":
//                return ResponseUtil.successJson(declineCurve.computeQuadraticDeclineParams(doubleT, doubleQ));
//            case "-1":
//                return ResponseUtil.successJson(declineCurve.computeLineDeclineParams(doubleT, doubleQ));
//            default:
//                return ResponseUtil.errorJson(StatusEnum.E_4001);
//        }
//    }
//
//    public ObjectNode computeDeclineCurve(ObjectNode node, String n) {
//        var wellId = node.get("wellId").asText();
//        var start = node.get("start").asText();
//        var end = node.get("end").asText();
//        var type = node.get("type").asInt();
//        var list = node.get("list");
//        return computeDeclineCurve(wellId, start, end, n, type,list);
//    }
//
//    public ObjectNode computeDeclineCurveN1(ObjectNode node) {
//        return computeDeclineCurve(node, "1");
//    }
//
//    public ObjectNode computeDeclineCurveN0(ObjectNode node) {
//        return computeDeclineCurve(node, "0");
//    }
//
//    public ObjectNode computeDeclineCurveN05(ObjectNode node) {
//        return computeDeclineCurve(node, "0.5");
//    }
//
//    public ObjectNode computeDeclineCurveN_05(ObjectNode node) {
//        return computeDeclineCurve(node, "-0.5");
//    }
//
//    public ObjectNode computeDeclineCurveN_1(ObjectNode node) {
//        return computeDeclineCurve(node, "-1");
//    }
//
//    /**
//     * 数据库中：
//     * <p>
//     * 累产油(np) : cum_oil
//     * 累产水(wp) : cum_water
//     * 累产液(lp) : cum_liquid
//     *
//     * @param node params from user
//     * @param kind to decide which method
//     * @return result
//     */
//    public ObjectNode computeWaterRelation(ObjectNode node, int kind) {
//        List<Map<String, Object>> data = dataService.getWellData(node, "cum_oil,cum_water,cum_liquid");
//        var length = data.size();
//        var np = new double[length];
//        var wp = new double[length];
//        var lp = new double[length];
//        var dates = new String[length];
//        //var f = new double[length];
//        for (int i = 0; i < length; i++) {
//            var item = data.get(i);
//            np[i] = (double) item.getOrDefault("cum_oil",0.0);
//            wp[i] = (double) item.getOrDefault("cum_water",0.0);
//            lp[i] = (double) item.getOrDefault("cum_liquid",0.0);
//            dates[i] = (String) item.get("date");
//            //f[i] = (double)item.get("f");
//        }
//        switch (kind) {
//            case WaterRelationCurve.A_TYPE:
//                return ResponseUtil.successJson(waterRelationCurve.computeWaterRelationA(wp, np,dates));
//            case WaterRelationCurve.B_TYPE:
//                return ResponseUtil.successJson(waterRelationCurve.computeWaterRelationB(lp, np,dates));
//            case WaterRelationCurve.C_TYPE:
//                return ResponseUtil.successJson(waterRelationCurve.computeWaterRelationC(lp, np,dates));
//            case WaterRelationCurve.D_TYPE:
//                return ResponseUtil.successJson(waterRelationCurve.computeWaterRelationD(lp, np, wp,dates));
//            case WaterRelationCurve.E_TYPE:
//                return ResponseUtil.successJson(waterRelationCurve.computeWaterRelationE(np, wp,dates));
//            case WaterRelationCurve.YU_TYPE:
//                return ResponseUtil.successJson(waterRelationCurve.computeWaterRelationYu(lp, np, wp,dates));
//            case WaterRelationCurve.ZHANG_TYPE:
//                return ResponseUtil.successJson(waterRelationCurve.computeWaterRelationZhang(np, wp,dates));
//            default:
//                return ResponseUtil.errorJson(StatusEnum.E_4001);
//        }
//    }
//
//    public ObjectNode computeWaterRelationA(ObjectNode node) {
//        return computeWaterRelation(node, WaterRelationCurve.A_TYPE);
//    }
//
//    public ObjectNode computeWaterRelationB(ObjectNode node) {
//        return computeWaterRelation(node, WaterRelationCurve.B_TYPE);
//    }
//
//    public ObjectNode computeWaterRelationC(ObjectNode node) {
//        return computeWaterRelation(node, WaterRelationCurve.C_TYPE);
//    }
//
//    public ObjectNode computeWaterRelationD(ObjectNode node) {
//        return computeWaterRelation(node, WaterRelationCurve.D_TYPE);
//    }
//
//    public ObjectNode computeWaterRelationE(ObjectNode node) {
//        return computeWaterRelation(node, WaterRelationCurve.E_TYPE);
//    }
//
//    public ObjectNode computeWaterRelationYu(ObjectNode node) {
//        return computeWaterRelation(node, WaterRelationCurve.YU_TYPE);
//    }
//
//    public ObjectNode computeWaterRelationZhang(ObjectNode node) {
//        return computeWaterRelation(node, WaterRelationCurve.ZHANG_TYPE);
//    }
//
//    /**
//     * 地层原油粘度计算
//     */
//    public ObjectNode computeFormationCrudeOilViscosity(ObjectNode node) {
//        double uod = node.get("μod").asDouble();
//        double pi = node.get("Pi").asDouble();
//        double pb = node.get("Pb").asDouble();
//        double rs = node.get("Rs").asDouble();
//        double uo = physicalProperty.calculateFormationCrudeOilViscosity(uod, pi, pb, rs);
//        return ResponseUtil.formulaResult(null, new String[]{"μo&(MPa*s)"}, uo);
//    }
//
//    /**
//     * 原油密度计算
//     */
//    public ObjectNode computeCrudeOilDensity(ObjectNode node) {
//        double cod = node.get("ρo").asDouble();
//        double rg = node.get("λgs").asDouble();
//        double rs = node.get("rs").asDouble();
//        double v = node.get("Bo").asDouble();
//        double result = physicalProperty.calculateCrudeOilDensity(cod, rg, rs, v);
//        return ResponseUtil.formulaResult(null, new String[]{"ρo&(g/cm³)"}, result);
//    }
//
//    /**
//     * 地层水密度计算
//     * [{"desc":"","params":{"ρo":0,"λgs":0,"rs":0,"Bo":0},"unit":{"ρo":"地层原油密度(g/cm³)","λgs":"天然气相对密度，空气=1.0","rs":"溶解气油比(%)","Bo":"地层原油体积系数"}}]
//     */
//    public ObjectNode computeFormationWaterDensity(ObjectNode node) {
//        double cs = node.get("Cs").asDouble();
//        double pr = node.get("Pr").asDouble();
//        Map<String, Double> scatter = physicalProperty.calculateFormationWaterDensity(cs, pr);
//        return ResponseUtil.formulaResult(null,scatter);
//    }
//
//    /**
//     * 地层水体积系数计算
//     */
//    public ObjectNode computeFormationWaterVolumeCoefficient(ObjectNode node) {
//        double pr = node.get("Pr").asDouble();
//        return ResponseUtil.formulaResult(null, new String[]{"Bw&(f)"}, physicalProperty.calculateFormationWaterVolumeCoefficient(pr));
//    }
//
//    /**
//     * 地层水压缩系数计算
//     */
//    public ObjectNode computeWaterCompressibilityCoefficient(ObjectNode node) {
//        double rsw = node.get("Rsw").asDouble();
//        double pr = node.get("Pr").asDouble();
//        double t = node.get("T").asDouble();
//        double cw = physicalProperty.calculateWaterCompressibilityCoefficient(rsw, pr, t);
//        return ResponseUtil.formulaResult(null, new String[]{"Cw&(MPa-1)"}, cw);
//    }
//
//    /**
//     * 地层水粘度计算
//     */
//    public ObjectNode computeWaterViscosity(ObjectNode node) {
//        double t = node.get("T").asDouble();
//        return ResponseUtil.formulaResult(null, new String[]{"μw&(MPa*s)"}, physicalProperty.calculateWaterViscosity(t));
//    }
//
//    /**
//     * 地面脱气原油粘度
//     */
//    public ObjectNode computeViscosityOfSurfaceDegassedCrudeOil(ObjectNode node) {
//        double ro = node.get("ρo").asDouble();
//        double t = node.get("T").asDouble();
//        double uod = physicalProperty.calculateViscosityOfSurfaceDegassedCrudeOil(ro, t);
//        return ResponseUtil.formulaResult(null, new String[]{"μod&(MPa*s)"}, uod);
//
//    }
//
//
//
//    // ------ 相渗曲线经验公式
//
//    public ObjectNode computeEmpiricalFormula(ObjectNode node, int tag) {
//        float interval = (float) node.get("interval").asDouble();
//        float sw = (float) node.get("sw").asDouble();
//        float sor = (float) node.get("sor").asDouble();
//        List<Map<String, Double>> result = null;
//        switch (tag) {
//            case RelativePermeabilityCurve.CHEN:
//                result = relativePermeabilityCurve.chenMethod(interval, sw, sor);
//                break;
//            case RelativePermeabilityCurve.PIRSON:
//                result = relativePermeabilityCurve.pirsonMethod(interval, sw, sor);
//                break;
//            case RelativePermeabilityCurve.JONES:
//                result = relativePermeabilityCurve.jonesMethod(interval, sw, sor);
//                break;
//            case RelativePermeabilityCurve.CORRECT_JONES:
//                result = relativePermeabilityCurve.correctJonesMethod(interval, sw, sor);
//                break;
//            case RelativePermeabilityCurve.PIRSON2:
//                result = relativePermeabilityCurve.pirsonMethod2(interval, sw, sor);
//                break;
//            default:
//        }
//        return ResponseUtil.formulaResult(result,new String[]{});
//    }
//
//    public ObjectNode chenMethod(ObjectNode body) {
//        return computeEmpiricalFormula(body,RelativePermeabilityCurve.CHEN);
//    }
//
//    public ObjectNode pirsonMethod(ObjectNode body) {
//        return computeEmpiricalFormula(body,RelativePermeabilityCurve.PIRSON);
//    }
//
//    public ObjectNode jonesMethod(ObjectNode body) {
//        return computeEmpiricalFormula(body,RelativePermeabilityCurve.JONES);
//    }
//
//    public ObjectNode correctJonesMethod(ObjectNode body) {
//        return computeEmpiricalFormula(body,RelativePermeabilityCurve.CORRECT_JONES);
//    }
//
//    public ObjectNode pirson2Method(ObjectNode body) {
//        return computeEmpiricalFormula(body,RelativePermeabilityCurve.PIRSON2);
//    }
//
//    // 相渗曲线的标准化
//
//    public ObjectNode curveStandardizationOfWang(ObjectNode body) throws JsonProcessingException {
//        var list = getRelativeListResult(body);
//        assert list != null;
//        return ResponseUtil.formulaResult(relativePermeabilityCurve.curveStandardizationOfWang(list),null);
//    }
//    // 的应用
//
//    /**
//     * 计算无因此采油(液)指数
//     */
//    public ObjectNode dimensionlessOilRecoveryIndex(ObjectNode body) {
//        double uw = body.get("μw").asDouble();
//        double uo = body.get("μo").asDouble();
//        try{
//            double[][] data = getRelativeResult(body);
//            if (data == null){
//                return ResponseUtil.errorJson(StatusEnum.E_4004);
//            }
//            return ResponseUtil.formulaResult(relativePermeabilityCurve.dimensionlessOilRecoveryIndex(uw,uo,data[0],data[1]),new String[]{});
//        }catch (JsonProcessingException e){
//            log.error("解析Json数据时发生错误");
//            return ResponseUtil.errorJson(StatusEnum.E_4004);
//        }
//    }
//
//    /**
//     * 水驱指数
//     */
//    public ObjectNode waterDriveIndex(ObjectNode body){
//        double uw = body.get("μw").asDouble();
//        double uo = body.get("μo").asDouble();
//        double bo = body.get("bo").asDouble();
//        try{
//            double[][] data = getRelativeResult(body);
//            if (data == null){
//                return ResponseUtil.errorJson(StatusEnum.E_4004);
//            }
//            return ResponseUtil.formulaResult(relativePermeabilityCurve.waterDriveIndex(uw,uo,bo,data[0],data[1]),new String[]{});
//        }catch (JsonProcessingException e){
//            log.error("解析Json数据时发生错误");
//            return ResponseUtil.errorJson(StatusEnum.E_4004);
//        }
//    }
//
//    /**
//     * 存水率
//     */
//    public ObjectNode waterStorageRate(ObjectNode body){
//        double uw = body.get("μw").asDouble();
//        double uo = body.get("μo").asDouble();
//        double bo = body.get("bo").asDouble();
//        try{
//            double[][] data = getRelativeResult(body);
//            if (data == null){
//                return ResponseUtil.errorJson(StatusEnum.E_4004);
//            }
//            return ResponseUtil.formulaResult(relativePermeabilityCurve.waterStorageRate(uw,uo,bo,data[0],data[1]),new String[]{});
//        }catch (JsonProcessingException e){
//            log.error("解析Json数据时发生错误");
//            return ResponseUtil.errorJson(StatusEnum.E_4004);
//        }
//    }
//
//    /**
//     * 驱油效率
//     */
//    public ObjectNode oilDisplacementEfficiencyForRpc(ObjectNode body){
//        double uw = body.get("μw").asDouble();
//        double uo = body.get("μo").asDouble();
//        try{
//            double[][] data = getRelativeResult(body);
//            if (data == null){
//                return ResponseUtil.errorJson(StatusEnum.E_4004);
//            }
//            return ResponseUtil.formulaResult(relativePermeabilityCurve.oilDisplacementEfficiency(uw,uo,data[0],data[1],data[2]),new String[]{});
//        }catch (JsonProcessingException e){
//            log.error("解析Json数据时发生错误");
//            return ResponseUtil.errorJson(StatusEnum.E_4004);
//        }
//    }
//
//    private double[][] getRelativeResult(ObjectNode body) throws JsonProcessingException {
//        List<Integer> chooseResult = JsonUtil.convertToIntegerList(body.get("chooseResult"));
//        if (chooseResult.size() < 1) {
//            log.warn("必须选择中间结果作为输入");
//            return null;
//        }
//        // 相渗曲线的应用输入只有一个相渗曲线因此只选择第一个结果id
//        var resultId = chooseResult.get(0);
//        Result result = resultMapper.selectById(resultId);
//        var resultStr = result.getResult().substring(1,result.getResult().length()-1).replaceAll("\\\\","");
//        JsonNode node = JsonUtil.convertToJson(resultStr);
//        if (node == null){
//            return null;
//        }
//        JsonNode listData = node.get("list");
//        ObjectMapper mapper = new ObjectMapper();
//        var input  = mapper.convertValue(listData, new TypeReference<List<Map<String, Double>>>() {});
//        var length = input.size();
//        int i = 0;
//        double[][] data = new double[3][length];
//        for (var item:input) {
//            data[0][i] = item.get("kro");
//            data[1][i] = item.get("krw");
//            data[2][i] = item.get("sw");
//            i++;
//        }
//        return data;
//    }
//
//    private List<Map<String,List<Double>>> getRelativeListResult(ObjectNode body) throws JsonProcessingException {
//        List<Integer> chooseResult = JsonUtil.convertToIntegerList(body.get("chooseResult"));
//        if (chooseResult.size() < 2) {
//            log.warn("必须选择足够的输入");
//            return null;
//        }
//        // 相渗曲线的应用输入只有一个相渗曲线因此只选择第一个结果id
//        var result = new ArrayList<Map<String,List<Double>>>(chooseResult.size());
//        List<Result> results = resultMapper.selectBatchIds(chooseResult);
//        for (var item: results) {
//            var resultStr = item.getResult().substring(1,item.getResult().length()-1).replaceAll("\\\\","");
//            JsonNode node = JsonUtil.convertToJson(resultStr);
//            if (node == null){
//                continue;
//            }
//            JsonNode listData = node.get("list");
//            ObjectMapper mapper = new ObjectMapper();
//            var input  = mapper.convertValue(listData, new TypeReference<List<Map<String, Double>>>() {});
//            var swList = new ArrayList<Double>();
//            var kroList = new ArrayList<Double>();
//            var krwList = new ArrayList<Double>();
//            for (var i: input) {
//                swList.add(i.get("sw"));
//                kroList.add(i.get("kro"));
//                krwList.add(i.get("krw"));
//            }
//            var map = new HashMap<String,List<Double>>();
//            map.put("sw",swList);
//            map.put("kro",kroList);
//            map.put("krw",krwList);
//            result.add(map);
//        }
//
//
//        return result;
//    }
//
//    /**
//     * 未饱和油藏-封闭型-封闭弹性驱动
//     * params:{"Np":0,"Bo":0,"Boi":0,"Ct":0,"△p":0}
//     */
//    public ObjectNode usCEhd(ObjectNode body){
//        double n = materialBalanceEquation.computeMaterialBalanceEquation(MaterialBalanceEquation.US_C_EHD,
//                body.get("Np").asDouble(), body.get("Bo").asDouble(), body.get("Boi").asDouble(),
//                body.get("Ct").asDouble(), body.get("△p").asDouble(),
//                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//        return ResponseUtil.formulaResult(null,new String[]{"N&(m³) "},n);
//    }
//
//    /**
//     * 未饱和油藏-不封闭型-天然弹性水压驱动
//     * params: {"Np":0,"Bo":0,"Boi":0,"Ct":0,"△p":0,"Bw":0,"We":0,"Wp":0}
//     */
//    public ObjectNode usUcNdEhd(ObjectNode body){
//        double n = materialBalanceEquation.computeMaterialBalanceEquation(MaterialBalanceEquation.US_UC_ND_EHD,
//                body.get("Np").asDouble(), body.get("Bo").asDouble(), body.get("Boi").asDouble(),
//                body.get("Ct").asDouble(), body.get("△p").asDouble(),
//                body.get("Bw").asDouble(), body.get("We").asDouble(), 0, body.get("Wp").asDouble(), 0,
//                0, 0, 0, 0, 0, 0, 0, 0, 0);
//        return ResponseUtil.formulaResult(null,new String[]{"N&(m³) "},n);
//    }
//
//    /**
//     * 未饱和油藏-不封闭型-天然水驱和人工注水的弹性水压驱动
//     * params: {"Np":0,"Bo":0,"Boi":0,"Ct":0,"△p":0,"Bw":0,"We":0,"Wp":0,"Wi":0}
//     */
//    public ObjectNode usUcNdAwdEhd(ObjectNode body){
//        double n = materialBalanceEquation.computeMaterialBalanceEquation(MaterialBalanceEquation.US_UC_ND_AWD_EHD,
//                body.get("Np").asDouble(), body.get("Bo").asDouble(), body.get("Boi").asDouble(),
//                body.get("Ct").asDouble(), body.get("△p").asDouble(), body.get("Bw").asDouble(),
//                body.get("We").asDouble(), body.get("Wi").asDouble(), body.get("Wp").asDouble(),
//                0,0, 0, 0, 0, 0, 0, 0, 0, 0);
//        return ResponseUtil.formulaResult(null,new String[]{"N&(m³) "},n);
//    }
//
//    /**
//     * 饱和油藏-溶解气驱动
//     * params: {"Np":0,"Bt":0,"Bti":0,"Rp":0,"Rsi":0,"Bg":0}
//     */
//    public ObjectNode sDgd(ObjectNode body){
//        double n = materialBalanceEquation.computeMaterialBalanceEquation(MaterialBalanceEquation.S_DGD,
//                body.get("Np").asDouble(), 0, 0,0, 0, 0, 0, 0, 0,
//                body.get("Bt").asDouble(),body.get("Rp").asDouble(), body.get("Rsi").asDouble(), body.get("Bg").asDouble(),
//                body.get("Bti").asDouble(), 0, 0, 0, 0, 0);
//        return ResponseUtil.formulaResult(null,new String[]{"N&(m³) "},n);
//    }
//
//    /**
//     * 饱和油藏-气顶、溶解气和弹性混合驱动
//     * params: {"Np":0,"△p":0,"Bt":0,"Rp":0,"Rsi":0,"Bg":0,"Bti":0,"Bgi":0,"Cw":0,"Swc":0,"Cf":0,"m":0}
//     */
//    public ObjectNode sGcDgdEhd(ObjectNode body){
//        double n = materialBalanceEquation.computeMaterialBalanceEquation(MaterialBalanceEquation.S_GC_DGD_EHD,
//                body.get("Np").asDouble(), 0, 0,0, body.get("△p").asDouble(), 0, 0, 0, 0,
//                body.get("Bt").asDouble(),body.get("Rp").asDouble(), body.get("Rsi").asDouble(), body.get("Bg").asDouble(),
//                body.get("Bti").asDouble(), body.get("Bgi").asDouble(), body.get("Cw").asDouble(), body.get("Swc").asDouble(),
//                body.get("Cf").asDouble(), body.get("m").asDouble());
//        return ResponseUtil.formulaResult(null,new String[]{"N&(m³) "},n);
//    }
//
//    /**
//     * 饱和油藏-气顶、溶解气、天然水驱和弹性混合驱动
//     * params: {"Np":0,"△p":0,"Bw":0,"We":0,"Wi":0,"Wp":0,"Bt":0,"Rp":0,"Rsi":0,"Bg":0,"Bti":0,"Bgi":0,"Cw":0,"Swc":0,"Cf":0,"m":0}
//     */
//    public ObjectNode sGcDgdNdEhd(ObjectNode body){
//        double n = materialBalanceEquation.computeMaterialBalanceEquation(MaterialBalanceEquation.S_GC_DGD_ND_EHD,
//                body.get("Np").asDouble(), 0, 0,0, body.get("△p").asDouble(), body.get("Bw").asDouble(), body.get("We").asDouble(),
//                0, body.get("Wp").asDouble(), body.get("Bt").asDouble(),body.get("Rp").asDouble(), body.get("Rsi").asDouble(),
//                body.get("Bg").asDouble(), body.get("Bti").asDouble(), body.get("Bgi").asDouble(), body.get("Cw").asDouble(),
//                body.get("Swc").asDouble(), body.get("Cf").asDouble(), body.get("m").asDouble());
//        return ResponseUtil.formulaResult(null,new String[]{"N&(m³) "},n);
//    }
//
//    /**
//     * 饱和油藏-气顶、溶解气、人工注水和弹性混合驱动
//     * params: {"Np":0,"△p":0,"We":0,"Wp":0,"Bt":0,"Rp":0,"Rsi":0,"Bg":0,"Bti":0,"Bgi":0,"Cw":0,"Swc":0,"Cf":0,"m":0}
//     */
//    public ObjectNode sGcDgdAwdEhd(ObjectNode body){
//        double n = materialBalanceEquation.computeMaterialBalanceEquation(MaterialBalanceEquation.S_GC_DGD_AWD_EHD,
//                body.get("Np").asDouble(), 0, 0,0, body.get("△p").asDouble(),0, body.get("We").asDouble(),
//                0, body.get("Wp").asDouble(), body.get("Bt").asDouble(),body.get("Rp").asDouble(), body.get("Rsi").asDouble(),
//                body.get("Bg").asDouble(), body.get("Bti").asDouble(), body.get("Bgi").asDouble(), body.get("Cw").asDouble(),
//                body.get("Swc").asDouble(), body.get("Cf").asDouble(), body.get("m").asDouble());
//        return ResponseUtil.formulaResult(null,new String[]{"N&(m³) "},n);
//    }
//
//    /**
//     * 油藏驱动指数：
//     * 未饱和油藏，弹性驱动指数
//     * params: {"N":0,"Boi":0,"Ct*":0,"△p":0,"Np":0,"Bo":0,"Wp":0}
//     */
//    public ObjectNode diUsEhd(ObjectNode body){
//        double result = materialBalanceEquation.drivingIndexOfReservoir(MaterialBalanceEquation.DI_US_EHD ,
//                body.get("N").asDouble(),body.get("Boi").asDouble(),body.get("Ct*").asDouble(),body.get("△p").asDouble(),
//                body.get("Np").asDouble(),body.get("Bo").asDouble(),body.get("Wp").asDouble(),0,0,0,
//                0,0,0,0,0,0,0,0,0);
//        return ResponseUtil.formulaResult(null,new String[]{"Di(f)"},result);
//    }
//
//    /**
//     * 油藏驱动指数：
//     * 未饱和油藏，天然水驱动指数
//     * params: {"Np":0,"Bo":0,"Wp":0,"We":0}
//     */
//    public ObjectNode diUsNd(ObjectNode body){
//        double result = materialBalanceEquation.drivingIndexOfReservoir(MaterialBalanceEquation.DI_US_ND ,
//                0,0,0,0,
//                body.get("Np").asDouble(),body.get("Bo").asDouble(),body.get("Wp").asDouble(),body.get("We").asDouble(),
//                0,0,0,0,0,0,0,0,0,0,0);
//        return ResponseUtil.formulaResult(null,new String[]{"Di(f)"},result);
//    }
//
//    /**
//     * 油藏驱动指数：
//     * 未饱和油藏，人工注水驱动指数
//     *  params: {"Np":0,"Bo":0,"Wp":0,"Wi":0}
//     */
//    public ObjectNode diUsAwd(ObjectNode body){
//        double result = materialBalanceEquation.drivingIndexOfReservoir(MaterialBalanceEquation.DI_US_AWD ,
//                0,0,0,0,
//                body.get("Np").asDouble(),body.get("Bo").asDouble(),body.get("Wp").asDouble(),0,
//                body.get("Wi").asDouble(),0,0,0,0,0,0,0,0,0,0);
//        return ResponseUtil.formulaResult(null,new String[]{"Di(f)"},result);
//    }
//
//
//    /**
//     * 油藏驱动指数：
//     * 饱和油藏，溶解气驱动指数
//     * params: {"N":0,"Np":0,"Wp":0,"Bt":0,"Bti":0,"Rp":0,"Rsi":0,"Bg":0}
//     */
//    public ObjectNode diSDgd(ObjectNode body){
//        double result = materialBalanceEquation.drivingIndexOfReservoir(MaterialBalanceEquation.DI_S_DGD ,
//                body.get("N").asDouble(),0,0,0,
//                body.get("Np").asDouble(),0,body.get("Wp").asDouble(),0,
//                0,body.get("Bt").asDouble(),body.get("Bti").asDouble(),body.get("Rp").asDouble(),
//                body.get("Rsi").asDouble(),body.get("Bg").asDouble(),0,0,
//                0,0,0);
//        return ResponseUtil.formulaResult(null,new String[]{"Di(f)"},result);
//    }
//
//
//    /**
//     * 油藏驱动指数：
//     * 饱和油藏，气顶驱动指数
//     * params: {"N":0,"Np":0,"Wp":0,"Bti":0,"Rp":0,"Rsi":0,"Bg":0,"m":0,"Bgi":0}
//     */
//    public ObjectNode diSGc(ObjectNode body){
//        double result = materialBalanceEquation.drivingIndexOfReservoir(MaterialBalanceEquation.DI_S_GC ,
//                body.get("N").asDouble(),0,0,0,
//                body.get("Np").asDouble(),0,body.get("Wp").asDouble(),0,
//                0,0,body.get("Bti").asDouble(),body.get("Rp").asDouble(),
//                body.get("Rsi").asDouble(),body.get("Bg").asDouble(),body.get("m").asDouble(),0,
//                0,0,body.get("Bgi").asDouble());
//        return ResponseUtil.formulaResult(null,new String[]{"Di(f)"},result);
//    }
//
//    /**
//     * 油藏驱动指数：
//     * 饱和油藏，弹性驱动指数
//     * params: {"N":0,"△p":0,"Np":0,"Bt":0,"Bg":0,"Wp":0,"Bti":0,"Rp":0,"Rsi":0,"m":0,"Swc":0,"Cf":0,"Cw":0,"Bgi":0}
//     * n bti m swc cf cw p np bt  rp rsi bg wp
//     */
//    public ObjectNode diSEhd(ObjectNode body){
//        double result = materialBalanceEquation.drivingIndexOfReservoir(MaterialBalanceEquation.DI_S_EHD ,
//                body.get("N").asDouble(),0,0,body.get("△p").asDouble(),
//                body.get("Np").asDouble(),0,body.get("Wp").asDouble(),0,
//                0,body.get("Bt").asDouble(),body.get("Bti").asDouble(),body.get("Rp").asDouble(),
//                body.get("Rsi").asDouble(),body.get("Bg").asDouble(),body.get("m").asDouble(),body.get("Swc").asDouble(),
//                body.get("Cf").asDouble(),body.get("Cw").asDouble(),body.get("Bgi").asDouble());
//        return ResponseUtil.formulaResult(null,new String[]{"Di(f)"},result);
//    }
//
//
//    /**
//     * 油藏驱动指数：
//     * 饱和油藏，天然水驱动指数
//     * params: {"Np":0,"Wp":0,"We":0,"Bt":0,"Rp":0,"Rsi":0,"Bg":0}
//     */
//    public ObjectNode diSNd(ObjectNode body){
//        double result = materialBalanceEquation.drivingIndexOfReservoir(MaterialBalanceEquation.DI_S_ND ,
//                0,0,0,0,
//                body.get("Np").asDouble(),0,body.get("Wp").asDouble(),body.get("We").asDouble(),
//                0,body.get("Bt").asDouble(),0,body.get("Rp").asDouble(),
//                body.get("Rsi").asDouble(),body.get("Bg").asDouble(),0,0,
//                0,0,0);
//        return ResponseUtil.formulaResult(null,new String[]{"Di(f)"},result);
//    }
//
//    /**
//     * 油藏驱动指数：
//     * 饱和油藏，人工注水驱动指数
//     * params: {"Np":0,"Wp":0,"Wi":0,"Bt":0,"Rp":0,"Rsi":0,"Bg":0}
//     */
//    public ObjectNode diSAwd(ObjectNode body){
//        double result = materialBalanceEquation.drivingIndexOfReservoir(MaterialBalanceEquation.DI_S_AWD ,
//                0,0,0,0,
//                body.get("Np").asDouble(),0,body.get("Wp").asDouble(),0,
//                body.get("Wi").asDouble(),body.get("Bt").asDouble(),0,body.get("Rp").asDouble(),
//                body.get("Rsi").asDouble(),body.get("Bg").asDouble(),0,0,
//                0,0,0);
//        return ResponseUtil.formulaResult(null,new String[]{"Di(f)"},result);
//    }
//
//
//    //-----------------------------------------------------------
//    //|                                                         |
//    //|                          可采储量                        |
//    //|                                                         |
//    //-----------------------------------------------------------
//
//    /**
//     * 类比法
//     * 计算可采储量
//     * params: {"N":0, "Er":0}
//     */
//    public ObjectNode analogy(ObjectNode body){
//        float n = (float) body.get("N").asDouble();
//        float er = (float) body.get("Er").asDouble();
//
//        return ResponseUtil.formulaResult(null,getNr.analogy(n,er));
//    }
//
//    /**
//     * 水驱砂岩油藏 方法一
//     * 计算采收率、可采储量
//     */
//    public ObjectNode sandstoneReservoir_1(ObjectNode body){
//        int k = body.get("K").asInt();
//        float uo = (float) body.get("μo").asDouble();
//        float phi = (float) body.get("φ").asDouble();
//        int s = body.get("S").asInt();
//        int n = body.get("N").asInt();
//        return ResponseUtil.formulaResult(null,getNr.sandstoneReservoir_1(k,uo,phi,s,n));
//    }
//
//    /**
//     * 水驱砂岩油藏 方法二
//     * 计算采收率、可采储量
//     */
//    public ObjectNode sandstoneReservoir_2(ObjectNode body){
//        int k = body.get("K").asInt();
//        int h = body.get("h").asInt();
//        int t = body.get("T").asInt();
//        float uo = (float) body.get("μo").asDouble();
//        float uw = (float) body.get("μw").asDouble();
//        float vk = (float) body.get("Vk").asDouble();
//        int s = body.get("S").asInt();
//        int n = body.get("N").asInt();
//        return ResponseUtil.formulaResult(null,getNr.sandstoneReservoir_2(uo, uw, k, h, s, vk,t, n));
//    }
//
//    /**
//     * 岩芯分析法
//     * 计算采收率、可采储量
//     */
//    public ObjectNode coreAnalysis(ObjectNode body){
//        float ed = (float) body.get("Ed").asDouble();
//        float ev = (float) body.get("Ev").asDouble();
//        float n = (float) body.get("N").asDouble();
//        return ResponseUtil.formulaResult(null,getNr.coreAnalysis(ed,ev,n));
//    }
//
//    /**
//     * 驱油效率法
//     * 计算采收率、可采储量
//     */
//    public ObjectNode oilDisplacementEfficiency(ObjectNode body){
//        float ed = (float) body.get("Ed").asDouble();
//        float epa = (float) body.get("Epa").asDouble();
//        float eza = (float) body.get("Eza").asDouble();
//        int n = body.get("N").asInt();
//        return ResponseUtil.formulaResult(null,getNr.oilDisplacementEfficiency(ed,epa,eza,n));
//    }
//
//    /**
//     * 水驱砾岩油藏法
//     * 计算采收率、可采储量
//     */
//    public ObjectNode conglomerateReservoir(ObjectNode body){
//        int pi = body.get("Pi").asInt();
//        int ke = body.get("Ke").asInt();
//        int s = body.get("S").asInt();
//        int lamda = body.get("λ").asInt();
//        double uo =  body.get("μo").asDouble();
//        float vk = (float) body.get("Vk").asDouble();
//        int m = body.get("m").asInt();
//        int n = body.get("N").asInt();
//        return ResponseUtil.formulaResult(null,getNr.conglomerateReservoir(uo,pi,ke,s,lamda,vk,m,n));
//    }
//
//    /**
//     * 水驱稠油油藏
//     * 计算采收率、可采储量
//     */
//    public ObjectNode heavyOilReservoir(ObjectNode body){
//        int k = body.get("K").asInt();
//        float uo = (float) body.get("μo").asDouble();
//        float phi = (float) body.get("φ").asDouble();
//        int s = body.get("S").asInt();
//        int n = body.get("N").asInt();
//        return ResponseUtil.formulaResult(null,getNr.heavyOilReservoir(phi,s,k,uo,n));
//    }
//
//    /**
//     * 水驱碳酸盐裂缝性油藏法
//     * 计算采收率、可采储量
//     */
//    public ObjectNode carbonateFracturedReserivoir(ObjectNode body){
//        var ke = body.get("Ke").asInt();
//        var uo = body.get("μo").asDouble();
//        var phi =  body.get("φ").asDouble();
//        var swi = body.get("Swi").asDouble();
//        var boi = body.get("Bwi").asDouble();
//        var uw = body.get("μw").asDouble();
//        var n = body.get("N").asInt();
//        return ResponseUtil.formulaResult(null,getNr.carbonateFracturedReserivoir(phi,swi,boi,ke,uw,uo,n));
//    }
//
//    /**
//     * 井网密度与采收率关系法                  (*有问题，公式未给出)
//     * 计算采收率、可采储量
//     */
//    public ObjectNode relationshipBetweenWellPattern(ObjectNode body){
//        var k = body.get("K").asInt();
//        var uo = body.get("μo").asDouble();
//        var s =  (float)body.get("S").asDouble();
//        var n = body.get("N").asInt();
//        return ResponseUtil.formulaResult(null,getNr.relationshipBetweenWellPattern(k,uo,s,n));
//    }
//
//    /**
//     * 谢式公式法 常规法
//     * 计算采收率、可采储量
//     */
//    public ObjectNode xieFomulaConventional(ObjectNode body){
//        var k = body.get("K").asInt();
//        var uo = (float)body.get("μo").asDouble();
//        var ed =  (float)body.get("Ed").asDouble();
//        var a =  (float)body.get("a").asDouble();
//        var n = body.get("N").asInt();
//        var s = body.get("S").asInt();
//        return ResponseUtil.formulaResult(null,getNr.XieFomulaConventional(uo,k,s,ed,n,a));
//    }
//
//    /**
//     * 谢式公式法 修正式
//     * 计算采收率、可采储量
//     */
//    public ObjectNode xieFomulaModified(ObjectNode body){
////        var k = body.get("K").asInt();
////        var uo = (float)body.get("uo").asDouble();
//        String[] params = getParams(body, "K So h R Ke Ac S Ed N a");
//        return ResponseUtil.formulaResult(null,getNr.XieFomulaModified(0,0,
//                Float.parseFloat(params[0]),
//                Float.parseFloat(params[1]),
//                Float.parseFloat(params[2]),
//                Float.parseFloat(params[3]),
//                Integer.parseInt(params[4]),
//                Float.parseFloat(params[5]),
//                Integer.parseInt(params[6]),
//                Float.parseFloat(params[7]),
//                Integer.parseInt(params[8]),
//                Float.parseFloat(params[9])));
//    }
//
//    //-----------------------------------------------------------
//    //|                                                         |
//    //|                              井底流压                    |
//    //|                                                         |
//    //-----------------------------------------------------------
//
//    /**
//     * 井底流压计算 方法一
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"flowPressure"}
//     * {"pp":0,"ph":0,"pc":0,"pz":0}
//     */
//    public ObjectNode flowPressure(ObjectNode body) {
//        String[] params = getParams(body, "pp ph pc pz");
//        return ResponseUtil.formulaResult(null,new String[]{"pwf&(MPa)"}, technical.flowPressure(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3])));
//    }
//
//    /**
//     * 抽油泵工作压力计算
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"oilWellPumpWorkingPressure"}
//     * {"rgo":0,"beta":0,"fw":0,"α":0}
//     */
//    public ObjectNode oilWellPumpWorkingPressure(ObjectNode body) {
//        String[] params = getParams(body, "rgo beta fw α");
//        return ResponseUtil.formulaResult(null,new String[]{"pp&(MPa)"}, technical.oilWellPumpWorkingPressure(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3])));
//    }
//
//
//    /**
//     * 液柱压力计算
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"liquidHeightPressure"}
//     * {"lm":0,"lp":0,"fw":0,"ρo":0,"ρw":0}
//     */
//    public ObjectNode liquidHeightPressure(ObjectNode body) {
//        String[] params = getParams(body, "lm lp fw ρo ρw");
//        return ResponseUtil.formulaResult(null,new String[]{"ph&(MPa)"}, technical.liquidHeightPressure(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3]),
//                Double.parseDouble(params[4])));
//    }
//
//
//    /**
//     * 井底流压计算 方法二
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"flowPressure2"}
//     * {"pr":0,"pb":0,"arfa":0,"bo":0,"t":0,"fw":0}
//     */
//    public ObjectNode flowPressure2(ObjectNode body) {
//        String[] params = getParams(body, "pr pb α Bo t fw");
//        return ResponseUtil.formulaResult(null,new String[]{"pwf&(MPa)"}, technical.flowPressure(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3]),
//                Double.parseDouble(params[4]),
//                Double.parseDouble(params[5])));
//    }
//
//
//    /**
//     * 井底流压计算 方法三
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"flowPressure3"}
//     * {"pr":0,"pb":0}
//     */
//    public ObjectNode flowPressure3(ObjectNode body) {
//        String[] params = getParams(body, "pr pb");
//        return ResponseUtil.formulaResult(null,new String[]{"pwf&(MPa)"}, technical.flowPressure3(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1])));
//    }
//
//
//    //-----------------------------------------------------------
//    //|                                                         |
//    //|                    注水压力 & 破裂压力                    |
//    //|                                                         |
//    //-----------------------------------------------------------
//    /**
//     * 油管摩阻计算
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"pipelineFrictionPressure"}
//     * {"re":0,"l":0,"q":0,"d":0}
//     */
//    public ObjectNode pipelineFrictionPressure(ObjectNode body) {
//        String[] params = getParams(body, "re l q d");
//        return ResponseUtil.formulaResult(null,new String[]{"pm&(MPa)"}, technical.pipelineFrictionPressure(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3])));
//    }
//
//    /**
//     * 水嘴摩阻计算
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"waterNozzleFrictionPressure"}
//     * {"q":0,"din":0}
//     */
//    public ObjectNode waterNozzleFrictionPressure(ObjectNode body) {
//        String[] params = getParams(body, "q din");
//        return ResponseUtil.formulaResult(null,new String[]{"ps&(MPa)"}, technical.waterNozzleFrictionPressure(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1])));
//    }
//
//
//    /**
//     * 井口注水压力计算
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"wellInjectPressure"}
//     * {"h":0,"pm":0,"ps":0,"st":0}
//     */
//    public ObjectNode wellInjectPressure(ObjectNode body) {
//        String[] params = getParams(body, "h pm ps st");
//        return ResponseUtil.formulaResult(null,new String[]{"pwi&(MPa)"}, technical.wellInjectPressure(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3])));
//    }
//
//    /**
//     * 上覆岩石压力计算
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateMantleRockPressure"}
//     * {"go":0,"h":0}
//     */
//    public ObjectNode calculateMantleRockPressure(ObjectNode body) {
//        String[] params = getParams(body, "go h");
//        return ResponseUtil.formulaResult(null,new String[]{"po&(MPa)"}, technical.calculateMantleRockPressure(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1])));
//    }
//
//    /**
//     * 地层压力计算
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateFormationPressure"}
//     * {"gp":0,"h":0}
//     */
//    public ObjectNode calculateFormationPressure(ObjectNode body) {
//        String[] params = getParams(body, "gp h");
//        return ResponseUtil.formulaResult(null,new String[]{"pp&(MPa)"}, technical.calculateFormationPressure(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1])));
//    }
//
//    /**
//     * 地层破裂压力计算 常规法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateBreakdownPressureConvention"}
//     * {"po":0,"pp":0}
//     */
//    public ObjectNode calculateBreakdownPressureConvention(ObjectNode body) {
//        String[] params = getParams(body, "po pp");
//        return ResponseUtil.formulaResult(null,new String[]{"pf&(MPa)"}, technical.calculateBreakdownPressureConvention(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1])));
//    }
//
//    /**
//     * 地层破裂压力计算 Eaton法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateBreakdownPressureEaton"}
//     * {"po":0,"pp":0,"mu":0}
//     */
//    public ObjectNode calculateBreakdownPressureEaton(ObjectNode body) {
//        String[] params = getParams(body, "po pp mu");
//        return ResponseUtil.formulaResult(null,new String[]{"pf&(MPa)"}, technical.calculateBreakdownPressureEaton(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2])));
//    }
//
//    /**
//     * 地层破裂压力计算 Anderson
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateBreakdownPressureAnderson"}
//     * {"po":0,"pp":0,"mu":0}
//     */
//    public ObjectNode calculateBreakdownPressureAnderson(ObjectNode body) {
//        String[] params = getParams(body, "po pp mu");
//        return ResponseUtil.formulaResult(null,new String[]{"pf&(MPa)"}, technical.calculateBreakdownPressureAnderson(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2])));
//    }
//
//
//    /**
//     * 地层破裂压力计算 黄荣樽法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateBreakdownPressureHuang"}
//     * {"po":0,"pp":0,"mu":0,"ks":0,"st":0}
//     */
//    public ObjectNode calculateBreakdownPressureHuang(ObjectNode body) {
//        String[] params = getParams(body, "po pp mu ks st");
//        return ResponseUtil.formulaResult(null,new String[]{"pf&(MPa)"}, technical.calculateBreakdownPressureHuang(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3]),
//                Double.parseDouble(params[4])));
//    }
//
//    /**
//     * 地层破裂压力计算 Stephen法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateBreakdownPressureStephen"}
//     * {"po":0,"pp":0,"mu":0,"xi":0}
//     */
//    public ObjectNode calculateBreakdownPressureStephen(ObjectNode body) {
//        String[] params = getParams(body, "po pp mu xi");
//        return ResponseUtil.formulaResult(null,new String[]{"pf&(MPa)"}, technical.calculateBreakdownPressureStephen(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3])));
//    }
//
//    /**
//     * 地层破裂压力计算 Holbrook法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateBreakdownPressureHolbrook"}
//     * {"po":0,"pp":0,"por":0}
//     */
//    public ObjectNode calculateBreakdownPressureHolbrook(ObjectNode body) {
//        String[] params = getParams(body, "po pp por");
//        return ResponseUtil.formulaResult(null,new String[]{"pf&(MPa)"}, technical.calculateBreakdownPressureHolbrook(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2])));
//    }
//
//    /**
//     * 地层破裂压力计算 经验公式法-BB-Williams法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateBreakdownPressureWilliam"}
//     * {"p":0,"h":0,"beta":0,"gc":0}
//     */
//
//    public ObjectNode calculateBreakdownPressureWilliam(ObjectNode body) {
//        String[] params = getParams(body, "p h beta gc");
//        return ResponseUtil.formulaResult(null,new String[]{"pf&(MPa)"}, technical.calculateBreakdownPressureWilliam(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3])));
//    }
//
//    /**
//     * 地层破裂压力计算 经验公式法-PA迪基法-形成垂直裂缝
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateBreakdownPressurePAVertical"}
//     * {"h":0}
//     */
//    public ObjectNode calculateBreakdownPressurePAVertical(ObjectNode body) {
//        String[] params = getParams(body, "h");
//        return ResponseUtil.formulaResult(null,new String[]{"pf&(MPa)"}, technical.calculateBreakdownPressurePAVertical(Double.parseDouble(params[0])));
//    }
//
//
//    /**
//     * 地层破裂压力计算 经验公式法-PA迪基法-形成水平裂缝
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"calculateBreakdownPressurePAHorizon"}
//     * {"h":0,"go":0}
//     */
//    public ObjectNode calculateBreakdownPressurePAHorizon(ObjectNode body) {
//        String[] params = getParams(body, "h go");
//        return ResponseUtil.formulaResult(null,new String[]{"pf&(MPa)"}, technical.calculateBreakdownPressurePAHorizon(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1])));
//    }
//
//
//
//    //-----------------------------------------------------------
//    //|                         注采比                           |
//    //-----------------------------------------------------------
//
//
//
//    /**
//     * 注采比计算方法一
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"IPRation"}
//     * {"bo":0,"bw":0,"fw":0}
//     */
//    public ObjectNode IPRation(ObjectNode body) {
//        String[] params = getParams(body, "bo bw fw");
//        var bo = Double.parseDouble(params[0]);
//        var bw = Double.parseDouble(params[1]);
//        var fw = Double.parseDouble(params[2]);
//        var list = new ArrayList<Map<String, Double>>(2);
//        list.add(Map.of("fw",0.0,"R",technical.IPRation(bo, bw, 0.0)));
//        list.add(Map.of("fw",1.0,"R",technical.IPRation(bo, bw, 100.0)));
//        return ResponseUtil.formulaResult(list,new String[]{"r&(f)"},
//                technical.IPRation(bo, bw, fw));
//    }
//
//    /**
//     * 注采比计算方法二
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"IPRation2"}
//     * {"bo":0,"bw":0,"ct":0,"v":0,"dp":0,"qo":0,"qw":0,"fw":0}
//     */
//    public ObjectNode IPRation2(ObjectNode body) {
//        String[] params = getParams(body, "bo bw ct v dp qo qw fw");
//        var bo = Double.parseDouble(params[0]);
//        var bw = Double.parseDouble(params[1]);
//        var ct = Double.parseDouble(params[2]);
//        var v = Double.parseDouble(params[3]);
//        var dp = Double.parseDouble(params[4]);
//        var qo = Double.parseDouble(params[5]);
//        var qw = Double.parseDouble(params[6]);
//        var fw = Double.parseDouble(params[7]);
//        var list = new ArrayList<Map<String, Double>>(2);
//        list.add(Map.of("fw",0.0,"R",technical.IPRation(bo,bw,ct,v,dp,qo,qw,0.0)));
//        list.add(Map.of("fw",1.0,"R",technical.IPRation(bo,bw,ct,v,dp,qo,qw,100.0)));
//        return ResponseUtil.formulaResult(list,new String[]{"r&(f)"},
//                technical.IPRation(bo,bw,ct,v,dp,qo,qw,fw));
//    }
//
//    /**
//     * 注采比计算方法三 G/H使用默认值
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"IPRation3"}
//     * {"bo":0,"ro":0,"fw":0}
//     */
//    public ObjectNode IPRation3(ObjectNode body) {
//        String[] params = getParams(body, "bo ro fw");
//        var bo = Double.parseDouble(params[0]);
//        var ro = Double.parseDouble(params[1]);
//        var fw = Double.parseDouble(params[2]);
//        var list = new ArrayList<Map<String, Double>>(2);
//        list.add(Map.of("fw",0.0,"R",technical.IPRation2(bo,ro,0.0)));
//        list.add(Map.of("fw",0.98,"R",technical.IPRation2(bo,ro,98.0)));
//
//        return ResponseUtil.formulaResult(list,new String[]{"r&(f)"},
//                technical.IPRation2(bo,ro,fw));
//    }
//
//    /**
//     * 注采比计算方法三 G/H使用油田实际值
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"IPRation4"}
//     * {"bo":0,"ro":0,"fw":0,"G":0,"H":0}
//     */
//    public ObjectNode IPRation4(ObjectNode body) {
//        String[] params = getParams(body, "bo ro fw G H");
//        var ro = Double.parseDouble(params[1]);
//        var bo = Double.parseDouble(params[0]);
//        var fw = Double.parseDouble(params[2]);
//        var G = Double.parseDouble(params[3]);
//        var H = Double.parseDouble(params[4]);
//        var list = new ArrayList<Map<String, Double>>(2);
//        list.add(Map.of("fw",0.0,"R",technical.IPRation(bo,ro,0.0,G,H)));
//        list.add(Map.of("fw",1.0,"R",technical.IPRation(bo,ro,100.0,G,H)));
//        return ResponseUtil.formulaResult(list,new String[]{"r&(f)"},
//                technical.IPRation(bo,ro,fw,G,H));
//    }
//
//    /**
//     * 配注量计算 qiw
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"InjectionAllocation2"}
//     * {"IPR":0,"qo":0,"fw":0,"n":0}
//     */
//    public ObjectNode InjectionAllocation2(ObjectNode body) {
//        String[] params = getParams(body, "IPR qo fw n");
//        var IRP = Double.parseDouble(params[0]);
//        var qo = Double.parseDouble(params[1]);
//        var fw = Double.parseDouble(params[2]);
//        var n = Double.parseDouble(params[3]);
//        var list = new ArrayList<Map<String, Double>>(51);
//        for (int i = 0; i < 100; i+=2) {
//            list.add(Map.of("fw",i*0.01,
//                    "Qiw",technical.InjectionAllocation(IRP,qo,i),
//                    "qiw",technical.InjectionAllocation(IRP,qo,i,n)));
//        }
//        return ResponseUtil.formulaResult(list,new String[]{"Qiw&(f)","qiw&(f)"},
//                technical.InjectionAllocation(IRP,qo,fw),
//                technical.InjectionAllocation(IRP,qo,fw,n));
//    }
//
//
//    //-----------------------------------------------------------
//    //|                        采油速度                          |
//    //-----------------------------------------------------------
//
//    /**
//     * 采油速度的地层系数法
//     */
//    public ObjectNode oilRecoveryRate1(ObjectNode body) {
//        return ResponseUtil.formulaResult(null,new String[]{"vo&(f)"}, technical.oilRecoveryRate1(body));
//    }
//
//    /**
//     * 采油速度-地层系数法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"formationCoefficient"}
//     * {"k":0,"h":0,"μo":0}
//     */
//    public ObjectNode formationCoefficient(ObjectNode body) {
//        String[] params = getParams(body, "K h μo");
//        return ResponseUtil.formulaResult(null,new String[]{"vo&(f)"}, technical.formationCoefficient(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2])));
//    }
//
//    /**
//     * 采油速度-综合研究法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"comprehensiveResearch"}
//     * {"k":0,"h":0,"μo":0,"n":0}
//     */
//    public ObjectNode comprehensiveResearch(ObjectNode body) {
//        String[] params = getParams(body, "k h μo n");
//        return ResponseUtil.formulaResult(null,new String[]{"vo&(f)"}, technical.comprehensiveResearch(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3])));
//    }
//
//    /**
//     * 采油速度-井网密度法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"wellPatternDensity"}
//     * {"n":0}
//     */
//    public ObjectNode wellPatternDensity(ObjectNode body) {
//        String[] params = getParams(body, "n");
//        return ResponseUtil.formulaResult(null,new String[]{"vo&(f)"}, technical.wellPatternDensity(Double.parseDouble(params[0])));
//    }
//
//
//    //-----------------------------------------------------------
//    //|                                                         |
//    //|                    注采井数比                            |
//    //|                                                         |
//    //-----------------------------------------------------------
//
//    /**
//     * 采液指数Jl计算
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"productLiquidIndex"}
//     * {"qo":0,"qw":0,"no":0,"pows":0,"pwf":0}
//     */
//    public ObjectNode productLiquidIndex(ObjectNode body) {
//        String[] params = getParams(body, "qo qw no pows pwf");
//        return ResponseUtil.formulaResult(null,new String[]{"jl"}, technical.productLiquidIndex(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3]),
//                Double.parseDouble(params[4])));
//    }
//
//    /**
//     * 注水指数Iw计算
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"waterInjectIndex"}
//     * {"qinj":0,"nw":0,"pinj":0,"piwf":0}
//     */
//    public ObjectNode waterInjectIndex(ObjectNode body) {
//        String[] params = getParams(body, "qinj nw pinj piwf");
//        return ResponseUtil.formulaResult(null,new String[]{"iw"}, technical.waterInjectIndex(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3])));
//    }
//
//    /**
//     * 注采井数比 注采平衡法1
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"balancedInjPro"}
//     * {"Iw":0,"Jl":0}
//     */
//    public ObjectNode balancedInjPro(ObjectNode body) {
//        String[] params = getParams(body, "qo qw no pows pwf qinj nw pinj piwf");
//        var jl = technical.productLiquidIndex(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3]),
//                Double.parseDouble(params[4]));
//        var iw = technical.waterInjectIndex(Double.parseDouble(params[5]),
//                Double.parseDouble(params[6]),
//                Double.parseDouble(params[7]),
//                Double.parseDouble(params[8]));
//        return ResponseUtil.formulaResult(null,new String[]{"r&(f)"}, technical.balancedInjPro(iw, jl));
//    }
//
//    /**
//     * 注采井数比 注采平衡法2
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"balancedInjPro2"}
//     * {"Iw":0,"Jl":0,"IPR":0,"fw":0,"bo":0}
//     */
//    public ObjectNode balancedInjPro2(ObjectNode body) {
//        String[] params = getParams(body, "qo qw no pows pwf qinj nw pinj piwf IPR fw bo");
//        var jl = technical.productLiquidIndex(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3]),
//                Double.parseDouble(params[4]));
//        var iw = technical.waterInjectIndex(Double.parseDouble(params[5]),
//                Double.parseDouble(params[6]),
//                Double.parseDouble(params[7]),
//                Double.parseDouble(params[8]));
//        return ResponseUtil.formulaResult(null,new String[]{"r&(f)"}, technical.balancedInjPro(iw,
//                jl,
//                Double.parseDouble(params[9]),
//                Double.parseDouble(params[10]),
//                Double.parseDouble(params[11])));
//    }
//
//    /**
//     * 注采井数比 吸水采液指数法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"injProIndext"}
//     * {"Iw":0,"Jl":0}
//     */
//    public ObjectNode injProIndext(ObjectNode body) {
//        String[] params = getParams(body, "qo qw no pows pwf qinj nw pinj piwf");
//        var jl = technical.productLiquidIndex(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3]),
//                Double.parseDouble(params[4]));
//        var iw = technical.waterInjectIndex(Double.parseDouble(params[5]),
//                Double.parseDouble(params[6]),
//                Double.parseDouble(params[7]),
//                Double.parseDouble(params[8]));
//        return ResponseUtil.formulaResult(null,new String[]{"r&(f)"}, technical.injProIndext(iw, jl));
//    }
//
//    /**
//     * 注采井数比         流度比平方根法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"mobilityRation"}
//     * {"μo":0,"μw":0,"kro_si":0,"kro":0,"krw":0}
//     */
//    public ObjectNode mobilityRation(ObjectNode body) {
//        String[] params = getParams(body, "μo μw kro_si kro krw");
//        return ResponseUtil.formulaResult(null,new String[]{"r&(f)"}, technical.mobilityRation(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3]),
//                Double.parseDouble(params[4])));
//    }
//
//    //-----------------------------------------------------------
//    //|                                                         |
//    //|                    技术井网密度                           |
//    //|                                                         |
//    //-----------------------------------------------------------
//
//    /**
//     * 技术井网密度-注采平衡法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"densBalancedInjPro"}
//     * {"递增值":0.2,"递增数":5,"N":0,"A":0,"vo":0,"bo":0,"IPR":0,"qo":0,"rWT":0,"fw":0,"t":0}
//     */
//    public ObjectNode densBalancedInjPro(ObjectNode body) {
//        //递增值  递增数
//        String[] params = getParams(body, "注采比-递增步长 递增数量 N A vo bo IPR qo rWT fw t");
//        var interval = Double.parseDouble(params[0]);
//        var nums = Integer.parseInt(params[1]);
//        var A = Double.parseDouble(params[3]);
//        var N = Double.parseDouble(params[2]);
//        var vo = Double.parseDouble(params[4]);
//        var bo = Double.parseDouble(params[5]);
//        var rIP = Double.parseDouble(params[6]);
//        var qo = Double.parseDouble(params[7]);
//        var rWT = Double.parseDouble(params[8]);
//        var fw = Double.parseDouble(params[9]);
//        var t = Double.parseDouble(params[10]);
//        var list = new ArrayList<Map<String, Double>>(nums + 1);
//        for (int i = 1; i < nums + 1; i++) {
//            list.add(Map.of("rIP",i*interval+rIP,
//                    "S",technical.densBalancedInjPro(N,A,vo,bo,i*interval+rIP,qo,rWT,fw,t)));
//        }
//        return ResponseUtil.formulaResult(list,new String[]{"s&(1/km²)"},
//                technical.densBalancedInjPro(N,A,vo,bo,rIP,qo,rWT,fw,t));
//    }
//
//
//    /**
//     * 技术井网密度-注采平衡约束法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"densBalancedInjProConstraint"}
//     * {"递增值":0.2,"递增数":5,"N":0,"A":0,"vo":0,"bo":0,"IPR":0,"qo":0,"rWT":0,"fw":0,"t":0,"denOil":0,"denWat":0}
//     */
//    public ObjectNode densBalancedInjProConstraint(ObjectNode body) {
//        String[] params = getParams(body, "注采比-递增步长 递增数量 N A vo bo IPR qo rWT fw t denOil denWat");
//        var interval = Double.parseDouble(params[0]);
//        var nums = Integer.parseInt(params[1]);
//        var A = Double.parseDouble(params[3]);
//        var N = Double.parseDouble(params[2]);
//        var vo = Double.parseDouble(params[4]);
//        var bo = Double.parseDouble(params[5]);
//        var rIP = Double.parseDouble(params[6]);
//        var qo = Double.parseDouble(params[7]);
//        var rWT = Double.parseDouble(params[8]);
//        var fw = Double.parseDouble(params[9]);
//        var t = Double.parseDouble(params[10]);
//        var denOil = Double.parseDouble(params[11]);
//        var denWat = Double.parseDouble(params[12]);
//        var list = new ArrayList<Map<String, Double>>(nums + 1);
//        for (int i = 1; i < nums + 1; i++) {
//            list.add(Map.of("rIP",i*interval+rIP,
//                    "S",technical.densBalancedInjProConstraint(N,A,vo,bo,i*interval+rIP,qo,rWT,fw,t,denOil,denWat)));
//        }
//        return ResponseUtil.formulaResult(list,new String[]{"s&(1/km²)"},
//                technical.densBalancedInjProConstraint(N,A,vo,bo,rIP,qo,rWT,fw,t,denOil,denWat));
//    }
//
//    /**
//     * 技术井网密度-采液吸水指数法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"densInjProIndext"}
//     * {"注采压差":10,"递增值":0.2,"递增数":5,"N":0,"A":0,"vo":0,"pwi":0,"pwf":0,"t":0,"Iw":0,"Jl":0}
//     */
//    public ObjectNode densInjProIndext(ObjectNode body) {
//        String[] params = getParams(body, "注采压差 注采比-递增步长 递增数量 N A vo pwi pwf t Iw Jl");
//        var p = Double.parseDouble(params[0]);
//        var interval = Double.parseDouble(params[1]);
//        var nums = Integer.parseInt(params[2]);
//        var N = Double.parseDouble(params[3]);
//        var A = Double.parseDouble(params[4]);
//        var vo = Double.parseDouble(params[5]);
//        var pwi = Double.parseDouble(params[6]);
//        var pwf = Double.parseDouble(params[7]);
//        var t = Double.parseDouble(params[8]);
//        var Iw = Double.parseDouble(params[9]);
//        var Jl = Double.parseDouble(params[10]);
//        var list = new ArrayList<Map<String, Double>>(nums + 1);
//        for (int i = 1; i < nums + 1; i++) {
//            list.add(Map.of("p",i*interval+p,
//                    "S",technical.densInjProIndext(N,A,vo,i*interval+p,0,t,Iw,Jl)));
//        }
//        return ResponseUtil.formulaResult(list,new String[]{"s&(1/km²)"},
//                technical.densInjProIndext(N,A,vo,pwi,pwf,t,Iw,Jl));
//    }
//
//    //-----------------------------------------------------------
//    //|                                                         |
//    //|                    经济井网密度                           |
//    //|                                                         |
//    //-----------------------------------------------------------
//
//    /**
//     * 经济井网密度计算
//     */
//    public ObjectNode economicWellPatternDensity(ObjectNode body){
//        String[] params = getParams(body, "k μo N Ed di p r o A id ib R t vo po wi er");
//        var k = Double.parseDouble(params[0]);
//        var uo = Double.parseDouble(params[1]);
//        var N = Double.parseDouble(params[2]);
//        var Ed = Double.parseDouble(params[3]);
//        var di = Double.parseDouble(params[4]);
//        var p = Double.parseDouble(params[5]);
//        var r = Double.parseDouble(params[6]);
//        var o = Double.parseDouble(params[7]);
//        var A = Double.parseDouble(params[8]);
//        var id = Double.parseDouble(params[9]);
//        var ib = Double.parseDouble(params[10]);
//        var R = Double.parseDouble(params[11]);
//        var t = Double.parseDouble(params[12]);
//        var po = Double.parseDouble(params[13]);
//        var wi = Double.parseDouble(params[14]);
//        var er = Double.parseDouble(params[15]);
//        var vo = Double.parseDouble(params[16]);
//        var a = technical.wellPatternIndex(k,uo); // 井网指数
//        var s = technical.reasonWellPatternDensity(N,Ed,di,p,r,o,
//                A,id,ib,R,t,k,vo);  // 经济合理井网密度
//        var lSquare = technical.limitWellSpaceSquare(s);
//        var lTriangle = technical.limitWellSpaceTriangle(s);
//        var sm = technical.limitWellPatternDensity(id,ib,R,r,t,di,
//                po,o,wi,er,N,A);
//        return ResponseUtil.formulaResult(null,
//                new String[]{"井网指数&(口/km^2)","经济合理井网密度&(口/km^2)","经济极限井距(正方形)&(m)","经济极限井距(三角形)&(m)","经济极限井网密度&(口/km^2)"},
//                a,s,lSquare,lTriangle,sm);
//    }
//
//    //-----------------------------------------------------------
//    //|                                                         |
//    //|                    技术极限井距                           |
//    //|                                                         |
//    //-----------------------------------------------------------
//
//    /**
//     * 技术极限井距-牛顿迭代法求解-牛顿迭代法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"newtonIterative"}
//     * {"rw":0,"result":0}
//     */
//    public ObjectNode newtonIterative(ObjectNode body) {
//        String[] params = getParams(body, "rw result");
//        return ResponseUtil.formulaResult(null,new String[]{"x"}, technical.newtonIterative(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1])));
//    }
//
//    /**
//     * 技术极限井距-经验公式法-极限采油半径
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"empiricalFormular"}
//     * {"pe":0,"pwf":0,"k":0,"uo":0}
//     */
//    public ObjectNode empiricalFormular(ObjectNode body) {
//        String[] params = getParams(body, "pe pwf k μo");
//        return ResponseUtil.formulaResult(null,new String[]{"R&(m)"}, technical.empiricalFormular(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3])));
//    }
//
//    /**
//     * 技术极限井距-经验公式法-技术极限井距
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"empiricalFormular2"}
//     * {"dp":0,"k":0,"uo":0}
//     */
//    public ObjectNode empiricalFormular2(ObjectNode body) {
//        String[] params = getParams(body, "dp k μo");
//        return ResponseUtil.formulaResult(null,new String[]{"R&(m)"}, technical.empiricalFormular(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2])));
//    }
//
//    /**
//     * 技术极限井距-启动压力梯度法1
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"startPressureGradient"}
//     * {"a":0,"b":0,"pe":0,"pwf":0,"k":0,"rw":0}
//     */
//    public ObjectNode startPressureGradient(ObjectNode body) {
//        String[] params = getParams(body, "a b pe pwf k rw");
//        return ResponseUtil.formulaResult(null,new String[]{"R&(m)"}, technical.startPressureGradient(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3]),
//                Double.parseDouble(params[4]),
//                Double.parseDouble(params[5])));
//    }
//
//    /**
//     * 技术极限井距-启动压力梯度法2
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"startPressureGradient2"}
//     * {"a":0,"b":0,"dp":0,"k":0,"rw":0}
//     */
//    public ObjectNode startPressureGradient2(ObjectNode body) {
//        String[] params = getParams(body, "a b dp k rw");
//        return ResponseUtil.formulaResult(null,new String[]{"R&(m)"}, technical.startPressureGradient(Double.parseDouble(params[0]),
//                Double.parseDouble(params[1]),
//                Double.parseDouble(params[2]),
//                Double.parseDouble(params[3]),
//                Double.parseDouble(params[4])));
//    }
//
//    /**
//     * 技术极限井距-驱替压力梯度法
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"displacePressureGradient"}
//     * {"pe":0,"pwf":0,"pinf":0,"R":0,"r":0,"rw":0}
//     */
//    public ObjectNode displacePressureGradient(ObjectNode body) {
//        String[] params = getParams(body, "pe pwf pinf R r rw");
//        var pe = Double.parseDouble(params[0]);
//        var pwf = Double.parseDouble(params[1]);
//        var pinf = Double.parseDouble(params[2]);
//        var R = Double.parseDouble(params[3]);
//        var r = Double.parseDouble(params[4]);
//        var rw = Double.parseDouble(params[5]);
//        var list = new ArrayList<Map<String, Double>>(150);
//        for (int i = 2; i < 300; i+=2) {
//            list.add(Map.of("r",i * 1.0,
//                    "Gd",technical.displacePressureGradient(pe,pwf,pinf,R,i,rw)));
//        }
//        return ResponseUtil.formulaResult(list,new String[]{"gD&(MPa/m)"},
//                technical.displacePressureGradient(pe,pwf,pinf,R,r,rw));
//    }
//
//
//    //-----------------------------------------------------------
//    //|                                                         |
//    //|                    经济极限井距                           |
//    //|                                                         |
//    //-----------------------------------------------------------
//
//
//    /**
//     * 经济极限井距-方法一
//     * {"class":"com.bbc.rep.repcalcu.service.AlgorithmService","method":"economicLimitWellSpacingMethod1"}
//     * {"id":0,"ib":0,"r":0,"t":0,"di":0,"po":0,"o":0,"wi":0,"er":0,"N":0,"A":0,"地面投资":0,"递增数值":0,"递增个数":0,"原油价格":0,"递增数值":0,"递增个数":0,"井网方式(1是正方形":0,"2是三角形)":0}
//     */
//    public ObjectNode economicLimitWellSpacingMethod1(ObjectNode body) {
//        String[] params = getParams(body, "单井的钻井投资 单井的地面建设投资 投资贷款年利率 开发评价年限 原油商品率 原油销售价格 原油生产操作费用 开发评价年限内可采储量采出程度 原油采收率 原油地质储量 含油面积 地面投资 地面递增数值 地面递增个数 原油价格 原油递增数值 原油递增个数 输入1为正方形其他为三角形");
//        var id = Double.parseDouble(params[0]);
//        var ib = Double.parseDouble(params[1]);
//        var r = Double.parseDouble(params[2]);
//        var t = Double.parseDouble(params[3]);
//        var di = Double.parseDouble(params[4]);
//        var po = Double.parseDouble(params[5]);
//        var o = Double.parseDouble(params[6]);
//        var wi = Double.parseDouble(params[7]);
//        var er = Double.parseDouble(params[8]);
//        var N = Double.parseDouble(params[9]);
//        var A = Double.parseDouble(params[10]);
//        var groundPrice = Double.parseDouble(params[11]);
//        var groundInterval = Integer.parseInt(params[12]);
//        var groundNum = Integer.parseInt(params[13]);
//        var oilPrice = Double.parseDouble(params[14]);
//        var oilInterval = Integer.parseInt(params[15]);
//        var oilNum = Integer.parseInt(params[16]);
//        var type = Double.parseDouble(params[17]);
//        double nmink = technical.limitRecoverableReserves(id, ib, r, t, di, po, o, wi);
//        double nming = technical.limitGeoReserves(nmink,er);
//        double sm = technical.limitWellPatternDensity(nming,N,A);
//        double l;
//        if(type == 1){
//            l = technical.limitWellSpace(sm);
//        } else {
//            l = technical.limitWellSpace2(sm);
//        }
//        var list = new ArrayList<Map<String, Object>>(oilNum + 1);
//        for (int i = 0; i < oilNum; i++) {
//            var itemOilPrice = (i+1) * oilInterval + oilPrice;
//            LinkedHashMap<String, Object> item = new LinkedHashMap<>(5 + groundNum);
//            item.put("x",itemOilPrice + "美元/桶");
//            for (int j = 0; j < groundNum; j++) {
//                var itemGroundPrice = (j+1) * groundInterval +groundPrice;
//                var itemSm = technical.limitWellPatternDensity(technical.limitGeoReserves(technical.limitRecoverableReserves(id, itemGroundPrice, r, t, di, itemOilPrice, o, wi),er),N,A);
//                item.put(itemGroundPrice+"万元/口",type == 1 ? technical.limitWellSpace(itemSm) : technical.limitWellSpace2(itemSm));
//            }
//            var itemNmink = technical.limitRecoverableReserves(id, ib, r, t, di, itemOilPrice, o, wi);
//            var itemNming = technical.limitGeoReserves(itemNmink,er);
//            var itemSm = technical.limitWellPatternDensity(itemNming,N,A);
//            double itemL;
//            if(type == 1){
//                itemL = technical.limitWellSpace(itemSm);
//            } else {
//                itemL = technical.limitWellSpace2(itemSm);
//            }
//            item.put("经济极限井网密度",itemL);
//            item.put("经济极限井距",itemSm);
//            item.put("单井最小控制地质储量",itemNming);
//            item.put("单井最小控制可采储量",itemNmink);
//            list.add(item);
//        }
//        return ResponseUtil.formulaResult(list,
//                new String[]{"经济极限井距&(m)","经济极限井网密度&(口/km^2)","单井最小控制地质储量&(万吨)","单井最小控制可采储量&(万吨)"},
//                l,sm,nming,nmink
//        );
//    }
//
//    //-----------------------------------------------------------
//    //|                                                         |
//    //|                    生产压差                              |
//    //|                                                         |
//    //-----------------------------------------------------------
//
//    public ObjectNode producingPressure2(ObjectNode body){
//        String[] params = getParams(body,"ko h ak pi G re rw μo b");
//        var ko = Double.parseDouble(params[0]);
//        var h = Double.parseDouble(params[1]);
//        var ak = Double.parseDouble(params[2]);
//        var pi = Double.parseDouble(params[3]);
//        var G = Double.parseDouble(params[4]);
//        var re = Double.parseDouble(params[5]);
//        var rw = Double.parseDouble(params[6]);
//        var u = Double.parseDouble(params[7]);
//        var b = Double.parseDouble(params[8]);
//        var bestDp = 0.0;
//        var max = Double.MIN_VALUE;
//        var list = new LinkedList<Map<String, Object>>();
//        for(double i  = 0.2; i < pi; i+=0.01) {
//            LinkedHashMap<String, Object> item = new LinkedHashMap<>(3);
//            var j1 = technical.producingIndex2(ko, h, ak, i, G, re, rw, u, b);
//            if (j1 > max) {
//                bestDp = i;
//                max = j1;
//            }
//            if( ((int)(i*100)) % 20 == 0){
//                var q = j1 * i;
//                item.put("dp", i);
//                item.put("j", j1);
//                item.put("q", q);
//                list.add(item);
//            }
//        }
//        return ResponseUtil.formulaResult(list,
//                new String[]{"合理压力水平"},bestDp);
//    }
//
//    /**
//     * 合理生产压差 动态法
//     */
//    public ObjectNode producingPressureForDynamic(ObjectNode body){
//        String[] params = getParams(body,"fw N n t a(VL) b(VL) c(VL) a(JL) b(JL) c(JL)");
//        var fw = Double.parseDouble(params[0]);
//        var N = Double.parseDouble(params[1]);
//        var n = Integer.parseInt(params[2]);
//        var t = Integer.parseInt(params[3]);
//        var aVl = Double.parseDouble(params[4]);
//        var bVl = Double.parseDouble(params[5]);
//        var cVl = Double.parseDouble(params[6]);
//        var aJl = Double.parseDouble(params[7]);
//        var bJl = Double.parseDouble(params[8]);
//        var cJl = Double.parseDouble(params[9]);
////        var type = Integer.parseInt(params[10]);
//        if (true ){
//
//            // todo 判断拟合类型 更新abc的值
//        }
//        var vl = technical.liquidRecoveryRateForDynamic(fw,aVl,bVl,cVl);
//        var jl = technical.producingLiquidIndexForDynamic(fw,aJl,bJl,cJl);
//        var ql = vl*N*10000/(n*t);
//        var dp = ql * 0.01 / jl;
//        return ResponseUtil.formulaResult(null,new String[]{"VL","JL","▲p","qL"},vl,jl,dp,ql);
//
//    }
//
//    //-----------------------------------------------------------
//    //|                水驱指数效果评价                           |
//    //-----------------------------------------------------------
//
//    public ObjectNode waterDriveIndexEffect(ObjectNode body){
//        String[] params = getParams(body,"qi qi2 qw qo Z fw B denoil");
//        var qi = Double.parseDouble(params[0]);
//        var qi2 = Double.parseDouble(params[1]);
//        var qw = Double.parseDouble(params[2]);
//        var qo = Double.parseDouble(params[3]);
//        var Z = Double.parseDouble(params[4]);
//        var fw = Double.parseDouble(params[5]);
//        var B = Double.parseDouble(params[6]);
//        var denoil = Double.parseDouble(params[6]);
//        double real = effectEvaluation.actualValue_WaterDriveIndex(qi,qi2,qw,qo,B,denoil);
//        double theory = effectEvaluation.theoryValue_WaterDriveIndex(Z,fw,B,denoil);
//        return ResponseUtil.formulaResult(null,new String[]{"实际值&(f)","理论值&(f)"},real,theory);
//    }
//
//    public String[] getParams(ObjectNode body,String str){
//        String[] keys = str.split(" ");
//        String[] values = new String[keys.length];
//        var i = 0;
//        for (var key:keys) {
//            values[i] = body.get(key).asText();
//            i++;
//        }
//        return values;
//    }
//
//
//    //-----------------------------------------------------------
//    //|                     累积存水率                           |
//    //-----------------------------------------------------------
//    public ObjectNode waterStorageRate2(ObjectNode body){
//        String[] params = getParams(body,"μr Rm type projectId");
//        var ur = Double.parseDouble(params[0]);
//        var rm = Double.parseDouble(params[1]);
//        var type = Integer.parseInt(params[2]);
//        var projectId = Integer.parseInt(params[3]);
//        List<Map<String, Object>> list = type == 1 ?
//                sAllProdMonthMapper.selectCumData(projectId) : sAllProdYearMapper.selectCumData(projectId);
//        return ResponseUtil.formulaResult(effectEvaluation.waterStorageRate(list,ur,rm),null);
//    }
//
//    @Autowired
//    public void setRelativePermeabilityCurve(RelativePermeabilityCurve relativePermeabilityCurve) {
//        this.relativePermeabilityCurve = relativePermeabilityCurve;
//    }
//
//    @Autowired
//    public void setDeclineCurve(DeclineCurve declineCurve) {
//        this.declineCurve = declineCurve;
//    }
//
//    @Autowired
//    public void setWellMapper(WellMapper wellMapper) {
//        this.wellMapper = wellMapper;
//    }
//
//    @Autowired
//    public void setDataService(DataService dataService) {
//        this.dataService = dataService;
//    }
//
//    @Autowired
//    public void setWaterRelationCurve(WaterRelationCurve waterRelationCurve) {
//        this.waterRelationCurve = waterRelationCurve;
//    }
//
//    @Autowired
//    public void setTechnical(TechnicalBoundaries technical) {
//        this.technical = technical;
//    }
//
//    @Autowired
//    public void setPhysicalProperty(PhysicalProperty physicalProperty) {
//        this.physicalProperty = physicalProperty;
//    }
//
//    @Autowired
//    public void setResultMapper(ResultMapper resultMapper) {
//        this.resultMapper = resultMapper;
//    }
//
//    @Autowired
//    public void setMaterialBalanceEquation(MaterialBalanceEquation materialBalanceEquation) {
//        this.materialBalanceEquation = materialBalanceEquation;
//    }
//
//    @Autowired
//    public void setEffectEvaluation(DevelopmentEffectEvaluation effectEvaluation) {
//        this.effectEvaluation = effectEvaluation;
//    }
//
//    @Autowired
//    public void setsAllProdYearMapper(SAllProdYearMapper sAllProdYearMapper) {
//        this.sAllProdYearMapper = sAllProdYearMapper;
//    }
//
//    @Autowired
//    public void setsAllProdMonthMapper(SAllProdMonthMapper sAllProdMonthMapper) {
//        this.sAllProdMonthMapper = sAllProdMonthMapper;
//    }
//
//    @Autowired
//    public void setGetNr(GetNr getNr) {
//        this.getNr = getNr;
//    }
//}
