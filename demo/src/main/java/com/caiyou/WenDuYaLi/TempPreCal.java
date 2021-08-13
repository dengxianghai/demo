package com.caiyou.WenDuYaLi;

import com.caiyou.algorithm.UnitGasFun;
import com.caiyou.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 温度压力剖面计算
 * 对应DXL_CalForm和UnitDXL_YaliXiShu文件
 *
 * @author dxh
 * @date 2021/08/06
 */
public class TempPreCal {

    private static final Logger logger = LoggerFactory.getLogger(TempPreCal.class);


    /**
     * 温度压力剖面计算
     *
     * @param DxlMethod 多相流计算方法        Hagedorn-Brown=0、Beggs-Brill=1、Duns-Ros=2、Okiszewski=3、Mukherjee-Brill=4
     * @param Qg        产气量
     * @param WGR       水气比
     * @param OGR       油气比
     * @param PStart    初始压力
     * @param TStart    初始温度 K 温度要用K
     * @param Step      //计算步长
     * @param RdJK      true从井底计算到井口,false反之
     * @param gasData   基础数据
     * @param list      油套管数据
     */
    public static LinkedList<VDxlResult> CalWenDuYaLi(Integer DxlMethod, double Qg, double WGR, double OGR,
                                                      double PStart, double TStart, double Step, boolean RdJK,
                                                      GasData gasData, List<RecTube> list) throws Exception {

        Integer Direct;
        double HStart, Hend;
        if (RdJK) {
            Direct = 1;//计算方向

        } else {
            Direct = -1;
        }

        double H = 0;
        for (int i = 0; i < list.size(); i++) {
            H = H + list.get(i).getLenwell();
        }

        //方向
        if (RdJK) {
            HStart = 0;
            Hend = H;
        } else {
            HStart = H;
            Hend = 0;
        }

        DXLCalRequest dxlCalRequest = new DXLCalRequest(list, DxlMethod, Direct, gasData.getSCFS(), Step,
                gasData.getYLJZXS(), WGR, OGR, gasData.getRg(), gasData.getRo(), gasData.getRw(), gasData.getDwtd(),
                H, Qg, gasData.getPstart(), TStart, HStart, Hend, gasData.getTpc(), gasData.getPpc());

        DXLCalResponse dxlCalResponse = UnitGasFun.DXLCal(dxlCalRequest);
        LinkedList<VDxlResult> vDxlResults = dxlCalResponse.getvDxlResults();

        return vDxlResults;
    }


    /**
     * 多相流管拟合
     *
     * @param DxlMethod 多相流计算方法      Hagedorn-Brown==0、Beggs-Brill==1、Duns-Ros==2、Okiszewski==3、Mukherjee-Brill==4
     * @param Qg        产气量
     * @param WGR       水气比
     * @param OGR       油气比
     * @param PStart    初始压力
     * @param TStart    初始温度 K 温度要用K
     * @param Step      计算步长
     * @param PressJZ   压力校正系数
     * @param gasData   基础数据
     * @param list      油套管数据
     * @throws Exception
     */
    public static LinkedList<VDxlResult> multiphaseFlowFitting(Integer DxlMethod, double Qg, double WGR, double OGR,
                                                               double PStart, double TStart, double Step,
                                                               double PressJZ, GasData gasData, List<RecTube> list) throws Exception {
        Integer Direct;
        double HStart;
        double Hend;
        double H;
        //方向
        Direct = 1;
        HStart = 0;

        H = 0;
        for (int i = 0; i < list.size(); i++) {
            H = H + list.get(i).getLenwell();
        }
        Hend = H;

        DXLCalRequest dxlCalRequest = new DXLCalRequest(list, DxlMethod, Direct, gasData.getSCFS(), Step,
                PressJZ, WGR, OGR, gasData.getRg(), gasData.getRo(), gasData.getRw(), gasData.getDwtd(),
                H, Qg, gasData.getPstart(), TStart, HStart, Hend, gasData.getTpc(), gasData.getPpc());

        DXLCalResponse dxlCalResponse = UnitGasFun.DXLCal(dxlCalRequest);
        LinkedList<VDxlResult> vDxlResults = dxlCalResponse.getvDxlResults();
        return vDxlResults;

    }


}
