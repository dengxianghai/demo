//package com.caiyou.WenDuYaLi;
//
//
//import com.caiyou.algorithm.UnitGasFun;
//import com.caiyou.entity.*;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//public class MultiphaseFlowFitting {
//
//
//    /**
//     * 多相流管拟合
//     *
//     * @param DxlMethod 多相流计算方法      Hagedorn-Brown==0、Beggs-Brill==1、Duns-Ros==2、Okiszewski==3、Mukherjee-Brill==4
//     * @param Qg        产气量
//     * @param WGR       水气比
//     * @param OGR       油气比
//     * @param PStart    初始压力
//     * @param TStart    初始温度 K 温度要用K
//     * @param Step      计算步长
//     * @param PressJZ   压力校正系数
//     * @param gasData   基础数据
//     * @throws Exception
//     */
//    public static LinkedList<VDxlResult> CalWenDuYaLi(Integer DxlMethod, double Qg, double WGR, double OGR, double PStart, double TStart, double Step, double PressJZ, GasData gasData) throws Exception {
//        Integer Direct;
//        double HStart;
//        double Hend;
//        double H;
//        //方向
//        Direct = 1;
//        HStart = 0;
//
//        Map<String, Object> map = UnitGasFun.GetYTGData();
//        if ((boolean) map.get("result") == false) {
//            throw new Exception("error");
//        }
//        List<com.caiyou.entity.RecTube> list = (List<RecTube>) map.get("RecTubeList");
//        H = 0;
//        for (int i = 0; i < list.size(); i++) {
//            H = H + list.get(i).getLenwell();
//        }
//        Hend = H;
//
//
////        GetGasBaseData; //得到相关基础数据
//
//
//        DXLCalRequest dxlCalRequest = new DXLCalRequest(list, DxlMethod, Direct, gasData.getSCFS(), Step,
//                PressJZ, WGR, OGR, gasData.getRg(), gasData.getRo(), gasData.getRw(), gasData.getDwtd(),
//                H, Qg, gasData.getPstart(), TStart, HStart, Hend, gasData.getTpc(), gasData.getPpc());
//
//        DXLCalResponse dxlCalResponse = UnitGasFun.DXLCal(dxlCalRequest);
//        LinkedList<VDxlResult> vDxlResults = dxlCalResponse.getvDxlResults();
//        return vDxlResults;
//
//    }
//
//
//}
