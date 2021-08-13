package com.caiyou.shengChangXiTong;

import com.caiyou.algorithm.UnitGasFun;
import com.caiyou.entity.GasData;
import com.caiyou.entity.QJJTYLRequest;
import com.caiyou.entity.RecTube;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生产系统分析
 *
 * @author dxh
 * @date
 */
public class ProductionSystemAnalysis {

    private static final double P_Ntemp = 273.15;
    private static final Integer pointCount = 20;

    /**
     * 节点分析
     *
     * @param gasData
     * @param ptf
     * @param isSep
     * @param selectNode
     * @throws Exception
     */
    public static void nodeAnalysis(GasData gasData, double ptf, boolean isSep, Integer selectNode, List<RecTube> recTubes) throws Exception {
        Integer i;
//        Integer pointCount = 20;
        double[] APtf2 = new double[0], AQsc2, APwf2 = new double[0], AQsc1, APtf1= new double[0], APwf1= new double[0], APr2 = new double[0], APr1 = new double[0];//可能是公有的变量

        double Pr = gasData.getCurrentPr();
        if (isSep == false) {
            if (ptf < 0) {
                throw new Exception("请输入井口压力！");
            }
        }
        switch (selectNode) {
            case 0:
                //井口位置
                if (isSep == false) { //无分离器，无地面气嘴时井口直线
                    APtf2 = new double[pointCount + 1];
                    for (i = 0; i <= pointCount; i++) {
                        APtf2[i] = ptf;
                    }

                    Map<String, double[]> map = CalcArray1Pwf(Pr, gasData); //地层算到井底
                    AQsc2 = map.get("ArrayQsc");
                    APwf2 = map.get("ArrayPwf");

//                    CalcArray2Ptf(1, APwf2, 0, AQsc1, APtf1); //井底算到井口
                    Map<String, double[]> map2 = CalcArray2Ptf(1, APwf2, 0, gasData, recTubes); //井底算到井口
                    AQsc1 = map2.get("ArrayQsc");
                    APtf1 = map2.get("Arrayptf");

                } else //有分离器，无地面气嘴时井口流压曲线
                {
                    Map<String, double[]> map = CalcArray1Pwf(Pr, gasData); //地层算到井底
                    AQsc2 = map.get("ArrayQsc");
                    APwf2 = map.get("ArrayPwf");

                    Map<String, double[]> map2 = CalcArray2Ptf(1, APwf2, 0, gasData, recTubes); //井底算到井口
                    AQsc1 = map2.get("ArrayQsc");
                    APtf1 = map2.get("Arrayptf");
                    //计算分离器压力
                    if (GetDMGas(gasData) == true) { //如果地面气嘴数据输入完整
                        Map<String, double[]> map3 = CalArray_SepToJKPr(gasData, recTubes);

                        AQsc2 = map3.get("ArrayQsc");
                        APtf2 = map3.get("Arrayptf");
                    } else {
                        Map<String, double[]> map3 = CalArray1SepPr(gasData, recTubes); //无地面气嘴

                        AQsc2 = map3.get("ArrayQsc");
                        APtf2 = map3.get("Arrayptf");
                    }
                }
                break;
            case 1:
                //井底位置
                if (isSep == false) { //无分离器，无地面气嘴时井口直线
                    Map<String, double[]> map = CalcArray1Pwf(Pr, gasData); //地层算到井底
                    AQsc2 = map.get("ArrayQsc");
                    APwf2 = map.get("ArrayPwf");


                    map = CalcArray2Pwf(1, ptf, 0, gasData, recTubes, ptf); //井口算到井底
                    AQsc1 = map.get("ArrayQsc");
                    APwf1 = map.get("ArrayPwf");

                } else {
                    Map<String, double[]> map = CalcArray1Pwf(Pr, gasData); //地层算到井底位置
                    AQsc2 = map.get("ArrayQsc");
                    APwf2 = map.get("ArrayPwf");

                    map = CalcArray3Pwf(gasData, recTubes); //从分离器算到算到井底
                    AQsc1 = map.get("ArrayQsc");
                    APwf1 = map.get("ArrayPwf");
                }
                break;
            case 2:
                //地层位置
                if (isSep == false) { //无分离器，无地面气嘴时井口直线

                    APr2 = new double[pointCount + 1];

                    for (i = 0; i <= pointCount; i++) {
                        APr2[i] = Pr;
                    }

                    Map<String, double[]> map = CalcArray2Pwf(1, ptf, 0, gasData, recTubes, ptf); //井口算到井底
                    AQsc1 = map.get("ArrayQsc");
                    APwf1 = map.get("ArrayPwf");


                    map=CalcArray1Pr(gasData,APwf1); //井底算到地层
                    AQsc2 = map.get("ArrayQsc");
                    APr1 = map.get("ArrayPr");

                } else {
                    APr2 = new double[pointCount + 1];

                    for (i = 0; i <= pointCount; i++) {

                        APr2[i] = Pr;
                    }
                    //CalcArray2Pwf(1, ptf, 0, AQsc1, APwf1);
                    Map<String, double[]> map = CalcArray3Pwf(gasData, recTubes); //从分离器算到算到井底
                    AQsc1 = map.get("ArrayQsc");
                    APwf1 = map.get("ArrayPwf");

                    map =CalcArray1Pr(gasData,APwf1); //井底算到地层
                    AQsc2 = map.get("ArrayQsc");
                    APr1 = map.get("ArrayPr");
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectNode);
        }

        Map<String,double[]> returnMap=new HashMap<>();
        returnMap.put("AQsc1",AQsc1);
        returnMap.put("APr1",APr1);
        returnMap.put("AQsc2",AQsc2);
        returnMap.put("APr2",APr2);
        returnMap.put("APwf1",APwf1);
        returnMap.put("APwf2",APwf2);
        returnMap.put("APtf1",APtf1);
        returnMap.put("APtf2",APtf2);


    }


    public static void  sensitivityAnalysis(GasData gasData,Integer options){
        K, i, j: integer;
    }

    //-----------------------------------------------------上面是接口，下面是内部算法------------------------------------------------------------------------------

    /**
     * //井底算到地层压力
     *
     * @param gasData
     * @param ArrayPwf
     * @return returnMap.put(" ArrayQsc ", ArrayQsc);
     *         returnMap.put("ArrayPr", ArrayPr);
     */
    public static Map<String, double[]> CalcArray1Pr(GasData gasData, double[] ArrayPwf) throws Exception {
        Integer SelectGS; //选择的模型公式
        double A, B, C, N, Ae, Be;
        Integer i;
        double Aof;
        double Step;
        double Rg, yco2, yh2s, Yn2, ppc, tpc, t, TmpPr, Prout, Pr;
        double[] ArrayQsc, ArrayPr;

        //产能预测
        Pr = gasData.getCurrentPr();
        A = gasData.getA();
        B = gasData.getB();
        C = gasData.getC();
        N = gasData.getN();
        Ae = gasData.getAe();
        Be = gasData.getBe();
        SelectGS = gasData.getCNMethod();
        switch (SelectGS) {//根据选择的计算方法
            case 0:
                if ((C <= 0) || (N <= 0)) {
                    throw new Exception("请在基本数据中输入C和N的值!");
                }
                break;
            case 1:
                if ((A <= 0) || (B <= 0)) {
                    throw new Exception("请在基本数据中输入A和B的值!");
                }
                break;
            case 2:
                if ((Ae <= 0) || (Be <= 0)) {
                    throw new Exception("请在基本数据中输入Ae和Be的值!");
                }
                break;
        }
        Aof = UnitGasFun.GetAof(SelectGS, A, B, Ae, Be, C, N, Pr, gasData);
        if (Aof <= 0) {
            throw new Exception("Aof不正确,请检查基本数据中选用的模型！");
        }

        Rg = gasData.getRg(); //气体相对密度
        yco2 = gasData.getYco2(); //二氧化碳摩尔分数
        yh2s = gasData.getYh2s(); //硫化氢摩尔分数
        Yn2 = gasData.getYn2(); //氮的摩尔分数
        ppc = gasData.getPpc(); //拟临界压力Mpa
        tpc = gasData.getTpc(); //拟临界温度K
        t = gasData.getQCWD() + P_Ntemp; //气藏温度

        Step = Aof / (pointCount + 1); //分成10个点来画

        ArrayPr = new double[pointCount + 1];
        ArrayQsc = new double[pointCount + 1];


        for (i = 0; i <= pointCount; i++) {

            ArrayQsc[i] = Step * (i + 1);
            if (ArrayQsc[i] < GetlimitQ(gasData)) {
                ArrayQsc[i] = GetlimitQ(gasData) + (i + 1) * 10;
            }
            switch (SelectGS) {
                //根据选择的计算方法
                case 0:
                    ArrayPr[i] = UnitGasFun.ZhishuShiD(C, N, ArrayPwf[i], ArrayQsc[i]);
                    break;
                case 1:
                    ArrayPr[i] = UnitGasFun.ErXiangshiD(A, B, ArrayPwf[i], ArrayQsc[i]);
                    break;
                case 2:

                    TmpPr = UnitGasFun.ErXiangshiD_NIYALI(Ae, Be, UnitGasFun.Get_NiYaLi(gasData,ArrayPwf[i]), ArrayQsc[i]);
                    Prout=UnitGasFun.presem(Rg, yco2, yh2s, Yn2, ppc, tpc, t, TmpPr );

                    ArrayPr[i] = Prout;
                    break;
            }
        }
        Map<String, double[]> returnMap = new HashMap<>();
        returnMap.put("ArrayQsc", ArrayQsc);
        returnMap.put("ArrayPr", ArrayPr);
        return returnMap;

    }


    /**
     * 从分离器算到井底
     *
     * @param gasData
     * @param recTubes
     * @return returnMap.put(" ArrayQsc ", ArrayQsc);
     * returnMap.put("ArrayPwf", Arraypwf);
     * @throws Exception
     */
    public static Map<String, double[]> CalcArray3Pwf(GasData gasData, List<RecTube> recTubes) throws Exception {
        double Aof; //Aof
        double Step; //将Aof分段
        double Q; //产气量
        double Rg; //气体相对密度
        double e; //管粗糙度，m；
        double d1; //油管外径;
        double h; //井深度m;
        double l; //井深长度，m；
        double tt1, tt2; //井口温度，K
        double tw; //井底温度，K
        double yn2; //氮的摩尔分数；
        double yco2; //二氧化碳的摩尔分数
        double yh2s; //硫化氢摩尔分数
        double ppc, tpc;
        double t1, z1;
        Integer i;
        double pwf;
        double SepP, SepT, SepL, SepD, SepLJD, SepE, SepLFD, Duch, Lwch, Tuch;
        Integer JiaoDu;
        double D2, Pr, Ro, Rw;
        double QZ_d, QZ_t, QZ_L;
        double h1, h2, K, P2, P1, t2;
        double A, B, C, N, Ae, Be, Hwell, YQB, SQB, Wm, tBottom;
        Integer SelectGS;
        double DWell2, DWell1;
        double Qwater, Pwater;

        QZ_d = gasData.getDM_QZZJ(); //气嘴直径
        QZ_t = gasData.getDM_QZRKWD(); //气嘴温度
        QZ_L = gasData.getDM_JKDQZGC(); //气嘴到井口管长
        Pr = gasData.getCurrentPr(); //当前气藏压力
        SepP = gasData.getDM_FLQYL(); //分离器压力;
        SepT = gasData.getDM_FLQWD(); //分离器温度;
        SepL = gasData.getDM_DMGXCD(); //地面管线长度
        SepD = gasData.getDM_DMGXNJ(); //地面管线内径
        SepLJD = gasData.getDM_SPQJ(); //地面管线水平倾角
        SepE = gasData.getDM_CCD(); //地面管线粗糙度
        JiaoDu = (int) Math.floor(SepLJD);

        h1 = SepL - QZ_L; //分离器到气嘴长度
        h2 = QZ_L; //气嘴到井口长度

        A = gasData.getA();
        B = gasData.getB();
        C = gasData.getC();
        N = gasData.getN();
        Ae = gasData.getAe();
        Be = gasData.getBe();
        SelectGS = gasData.getCNMethod();
        switch (SelectGS) { //根据选择的计算方法

            case 0:
                if ((C <= 0) || (N <= 0)) {
                    throw new Exception("请在基本数据中输入C和N的值!");
                }
                break;
            case 1:
                if ((A <= 0) || (B <= 0)) {
                    throw new Exception("请在基本数据中输入A和B的值!");
                }
                break;
            case 2:
                if ((Ae <= 0) || (Be <= 0)) {
                    throw new Exception("请在基本数据中输入Ae和Be的值!");
                }
                break;
        }
        Aof = UnitGasFun.GetAof(SelectGS, A, B, Ae, Be, C, N, Pr, gasData);
        if (Aof <= 0) {
            throw new Exception("Aof不正确,请检查基本数据中选用的模型！");
        }


        Wm = gasData.getDwtd();
        YQB = 0;// StrtoFloatDef(TextYQB.Text, 0);
        Rg = gasData.getRg(); //气体相对密度
        Ro = gasData.getRo();
        Rw = gasData.getRw();
        yco2 = gasData.getYco2(); //二氧化碳摩尔分数
        yh2s = gasData.getYh2s(); //硫化氢摩尔分数
        yn2 = gasData.getYn2(); //氮的摩尔分数
        ppc = gasData.getPpc(); //拟临界压力Mpa
        tpc = gasData.getTpc(); //拟临界温度K
        tt1 = SepT + P_Ntemp; //分离器温度
        tw = QZ_t + P_Ntemp; //气嘴温度

        Hwell = 0;
        for (i = 0; i < recTubes.size(); i++) {
            Hwell = Hwell + recTubes.get(i).getLenwell();
        }
        tt2 = UnitGasFun.GetJKWD(gasData, Hwell); ///井口温度
        tBottom = gasData.getQCWD() + P_Ntemp;
        //下面只考虑一根油管的情况
        if (gasData.getSCFS() == 0) { //生产方式 0为油管生产

            DWell2 = recTubes.get(0).getDti(); //油管内径
            DWell1 = 0;
        } else {
            DWell2 = recTubes.get(0).getDci();
            DWell1 = recTubes.get(0).getDto();
        }
        D2 = SepD; //地面管线内径mm
        d1 = 0; //mm转m
        e = SepE / 1000; //管粗糙度 mm 转 m
        l = SepL; //井身长度m
        h = l; //井深度m 现在是与井身的长度相等


        double[] Arraypwf = new double[pointCount + 1];
        double[] ArrayQsc = new double[pointCount + 1];

        Step = Aof / (pointCount + 1);
        K = gasData.getJRZS(); //绝热指数
        for (i = 0; i <= pointCount; i++) {

            Q = (i + 1) * Step;
            if (Q < GetlimitQ(gasData)) {
                Q = GetlimitQ(gasData) + (i + 1) * 10;
            }
            ArrayQsc[i] = Q;
            pwf = SepP;

            Map<String, double[]> map = CalcArray1Pwf(Pr, gasData); //地层算到井底
            double[] ArrQ_gas = map.get("ArrayQsc");
            double[] ArrP_gas = map.get("ArrayPwf");
            Pwater = ChaZhi_Pof_Q(ArrP_gas, ArrQ_gas, Q);

            map = CalWaterCL(Pr, gasData);
            double[] ArrP_water = map.get("Y1");
            double[] ArrQ_Water = map.get("X1");
            Qwater = ChaZhi_Qw_P(ArrP_water, ArrQ_Water, Pwater);


            SQB = Qwater / (Q / 10000); //水气比

            if (GetDMGas(gasData) == true) {

                //从分离器算到气嘴
                QJJTYLRequest qjjtylRequest = new QJJTYLRequest(1, 1, 1, 1, JiaoDu, gasData.getYLMethod(), gasData.getDxlMethod(), recTubes,
                        Wm, YQB, SQB, h1, pwf, Rg, Ro, Rw, yco2, yn2, yh2s, ppc, tpc, tt1, tw, Q, D2, d1, e, l, h);
                Map<String, Object> QJJTYLmap = UnitGasFun.QJJTYL(qjjtylRequest, gasData);
                P1 = (double) QJJTYLmap.get("p1");
                t1 = (double) QJJTYLmap.get("t1");
                z1 = (double) QJJTYLmap.get("z1");

//                QJJTYL(1, 1, 1, 1, JiaoDu, GasData.YLMethod, GasData.DxlMethod, RecTube, Wm, yqb, sqb, h1, pwf, rg, ro, rw, yco2, yn2, yh2s, ppc, tpc, Tt1, tw, Q, d2, d1, e, l, h, P1, t1, z1);

                //Tw 气嘴温度   tt1分离器温度
                P2 = P1 * Math.pow(2 / (K + 1), K / (K - 1));
                t2 = t1 * Math.pow(P2 / P1, (K - 1) / K);

                //从气嘴算到井口位置
                qjjtylRequest = new QJJTYLRequest(1, 1, 1, 1, JiaoDu, gasData.getYLMethod(), gasData.getDxlMethod(), recTubes,
                        Wm, YQB, SQB, h2, P2, Rg, Ro, Rw, yco2, yn2, yh2s, ppc, tpc, t2, tt2, Q, D2, d1, e, l, h);
                QJJTYLmap = UnitGasFun.QJJTYL(qjjtylRequest, gasData);
                P1 = (double) QJJTYLmap.get("p1");
                t1 = (double) QJJTYLmap.get("t1");
                z1 = (double) QJJTYLmap.get("z1");

//                QJJTYL(1, 1, 1, 1, JiaoDu, GasData.YLMethod, GasData.DxlMethod, RecTube, Wm, yqb, sqb, h2, P2, rg, ro, rw, yco2, yn2, yh2s, ppc, tpc, T2, Tt2, Q, d2, d1, e, l, h, P1, t1, z1);
                //Tw 井口  Tt:嘴前
            } else {
                //从分离器算到井口
                QJJTYLRequest qjjtylRequest = new QJJTYLRequest(1, 1, 1, 1, JiaoDu, gasData.getYLMethod(), gasData.getDxlMethod(), recTubes,
                        Wm, YQB, SQB, SepL, pwf, Rg, Ro, Rw, yco2, yn2, yh2s, ppc, tpc, tt1, tt2, Q, D2, d1, e, l, h);
                Map<String, Object> QJJTYLmap = UnitGasFun.QJJTYL(qjjtylRequest, gasData);
                P1 = (double) QJJTYLmap.get("p1");
                t1 = (double) QJJTYLmap.get("t1");
                z1 = (double) QJJTYLmap.get("z1");


//                QJJTYL(1, 1, 1, 1, JiaoDu, GasData.YLMethod, GasData.DxlMethod, RecTube, Wm, yqb, sqb, SepL, pwf, rg, ro, rw, yco2, yn2, yh2s, ppc, tpc, Tt1, tt2, Q, d2, d1, e, l, h, P1, t1, z1);
            }

            //从井口算到井底
            QJJTYLRequest qjjtylRequest2 = new QJJTYLRequest(0, 1, 1, 0, 90, gasData.getYLMethod(), gasData.getDxlMethod(), recTubes,
                    Wm, YQB, SQB, Hwell, P1, Rg, Ro, Rw, yco2, yn2, yh2s, ppc, tpc, t1, tBottom, Q, DWell2, DWell1, e, Hwell, Hwell);
            Map<String, Object> QJJTYLmap2 = UnitGasFun.QJJTYL(qjjtylRequest2, gasData);
            Arraypwf[i] = (double) QJJTYLmap2.get("p1");
            t1 = (double) QJJTYLmap2.get("t1");
            z1 = (double) QJJTYLmap2.get("z1");

//            QJJTYL(0, 1, 1, 0, 90, GasData.YLMethod, GasData.DxlMethod, RecTube, Wm, yqb, sqb, Hwell, P1, rg, ro, rw, yco2, yn2, yh2s, ppc, tpc, t1, tBottom, Q, DWell2, DWell1, e, Hwell, Hwell, Arraypwf[i], t1, z1);

        }
        Map<String, double[]> returnMap = new HashMap<>();
        returnMap.put("ArrayQsc", ArrayQsc);
        returnMap.put("ArrayPwf", Arraypwf);
        return returnMap;

    }

    /**
     * //从井口算到井底
     *
     * @param ident
     * @param ptf
     * @param D2
     * @return
     */
    public static Map<String, double[]> CalcArray2Pwf(Integer ident, double ptf, double D2, GasData gasData, List<RecTube> recTubes, double TextJKYL) throws Exception {
        double Aof; //Aof
        double Step; //将Aof分段
        double Q; //产气量
        double Rg; //气体相对密度
        double e; //管粗糙度，m；
        double d1; //油管外径;
        double h; //井深度m;
        double l; //井深长度，m；
        double tt1, tt2; //井口温度，K

        double tt; //井口温度，K
        double tw; //井底温度，K
        double yn2; //氮的摩尔分数；
        double yco2; //二氧化碳的摩尔分数
        double yh2s; //硫化氢摩尔分数
        double ppc, tpc;
        double t1, z1;
        Integer i;
        double pwf;
        double SepP, SepT, SepL, SepD, SepLJD, SepE, SepLFD, Duch, Lwch, Tuch;
        Integer JiaoDu;
        double Pr, Ro, Rw;
        double QZ_d, QZ_t, QZ_L;
        double h1, h2, K, P2, P1, t2;
        double A, B, C, N, Ae, Be, Hwell, YQB, SQB, Wm;
        double Qwater, Pwater;


        Pr = gasData.getCurrentPr();
        Aof = UnitGasFun.GetAof(gasData.getCNMethod(), gasData.getA(), gasData.getB(), gasData.getAe(), gasData.getBe(), gasData.getC(), gasData.getN(), Pr, gasData);
        if (Aof <= 0) {
            throw new Exception("Aof不正确,请检查基本数据中选用的模型！");
        }


        tw = gasData.getQCWD() + P_Ntemp; //井底温度

        Wm = gasData.getDwtd();
        Rg = gasData.getRg(); //气体相对密度
        Ro = gasData.getRo();
        Rw = gasData.getRw();
        yco2 = gasData.getYco2(); //二氧化碳摩尔分数
        yh2s = gasData.getYh2s(); //硫化氢摩尔分数
        yn2 = gasData.getYn2(); //氮的摩尔分数
        ppc = gasData.getPpc(); //拟临界压力Mpa
        tpc = gasData.getTpc(); //拟临界温度K


        YQB = 0;// StrtoFloatDef(TextYQB.Text, 0);
        h = 0;
        for (i = 0; i < recTubes.size(); i++) {
            h = h + recTubes.get(i).getLenwell();
        }
        tt = UnitGasFun.GetJKWD(gasData, h);
        if (ident == 1) {

            //下面只考虑一根油管的情况
            if (gasData.getSCFS() == 0) { //生产方式 0为油管生产

                D2 = recTubes.get(0).getDti(); //油管内径
                d1 = 0;
            } else {
                D2 = recTubes.get(0).getDci();
                d1 = recTubes.get(0).getDto();
            }
        } else {
            ptf = TextJKYL;
            if (ptf <= 0) {
                throw new Exception("井口油压必须大于零,请检查基本数据！");

            }
            D2 = D2; //油管内径
            recTubes.get(i).setDti(D2);
            d1 = 0;
        }
        e = recTubes.get(0).getRoughwell() / 1000; //管粗糙度 mm 转 m
        l = h; //井身长度m
        //分成10个点来显示
        Step = Aof / (pointCount + 1);

        double[] Arraypwf = new double[pointCount + 1];
        double[] ArrayQsc = new double[pointCount + 1];

        for (i = 0; i <= pointCount; i++) {

            Q = (i + 1) * Step;
            if (Q < GetlimitQ(gasData)) {
                Q = GetlimitQ(gasData) + (i + 1) * 10;
            }
            ArrayQsc[i] = Q;

            Map<String, double[]> map = CalcArray1Pwf(Pr, gasData); //地层算到井底
            double[] ArrQ_gas = map.get("ArrayQsc");
            double[] ArrP_gas = map.get("ArrayPwf");
            Pwater = ChaZhi_Pof_Q(ArrP_gas, ArrQ_gas, Q);

            map = CalWaterCL(Pr, gasData);
            double[] ArrP_water = map.get("Y1");
            double[] ArrQ_Water = map.get("X1");
            Qwater = ChaZhi_Qw_P(ArrP_water, ArrQ_Water, Pwater);
            SQB = Qwater / (Q / 10000); //水气比

            //井口算到井底
            QJJTYLRequest qjjtylRequest = new QJJTYLRequest(0, 1, 1, 0, 90, gasData.getYLMethod(), gasData.getDxlMethod(), recTubes,
                    Wm, YQB, SQB, h, ptf, Rg, Ro, Rw, yco2, yn2, yh2s, ppc, tpc, tt, tw, Q, D2, d1, e, l, h);
            Map<String, Object> QJJTYLmap = UnitGasFun.QJJTYL(qjjtylRequest, gasData);
            Arraypwf[i] = (double) QJJTYLmap.get("p1");
            t1 = (double) QJJTYLmap.get("t1");
            z1 = (double) QJJTYLmap.get("z1");

        }
        Map<String, double[]> returnMap = new HashMap<>();
        returnMap.put("ArrayQsc", ArrayQsc);
        returnMap.put("ArrayPwf", Arraypwf);
        return returnMap;

    }


    public static Map CalArray1SepPr(GasData gasData, List<RecTube> recTubes) throws Exception {
        //分离器算到井口，无地面气嘴数据的情况
        double Aof; //Aof
        double Step; //将Aof分段
        double Q; //产气量
        double Rg; //气体相对密度
        double e; //管粗糙度，m；
        double d1; //油管外径;
        double h; //井深度m;
        double l; //井深长度，m；
        double tt1, tt2; //井口温度，K
        double tw; //井底温度，K
        double yn2; //氮的摩尔分数；
        double yco2; //二氧化碳的摩尔分数
        double yh2s; //硫化氢摩尔分数
        double ppc, tpc;
        double t1, z1;
        Integer i;
        double pwf;
        double SepP, SepT, SepL, SepD, SepLJD, SepE, SepLFD, Duch, Lwch, Tuch;
        Integer JiaoDu;
        double D2, Pr, Ro, Rw;
        double QZ_d, QZ_t, QZ_L;
        double h1, h2, K, P2, P1, t2;
        double A, B, C, N, Ae, Be, Hwell, YQB, SQB, Wm;
        Integer SelectGS;

        double Qwater, Pwater;
        Pr = gasData.getCurrentPr();
        SepP = gasData.getDM_FLQYL(); //分离器压力;
        SepT = gasData.getDM_FLQWD(); //分离器温度;
        SepL = gasData.getDM_DMGXCD(); //地面管线长度
        SepD = gasData.getDM_DMGXNJ(); //地面管线内径
        SepLJD = gasData.getDM_SPQJ(); //地面管线水平倾角
        SepE = gasData.getDM_CCD(); //地面管线粗糙度
        JiaoDu = (int) Math.floor(SepLJD);
        A = gasData.getA();
        B = gasData.getB();
        C = gasData.getC();
        N = gasData.getN();
        Ae = gasData.getAe();
        Be = gasData.getBe();
        SelectGS = gasData.getCNMethod();
        switch (SelectGS) { //根据选择的计算方法
            case 0:
                if ((C <= 0) || (N <= 0)) {
                    throw new Exception("请在基本数据中输入C和N的值!");
                }
                break;
            case 1:
                if ((A <= 0) || (B <= 0)) {
                    throw new Exception("请在基本数据中输入A和B的值!");
                }
                break;
            case 2:
                if ((Ae <= 0) || (Be <= 0)) {
                    throw new Exception("请在基本数据中输入Ae和Be的值!");
                }
                break;
        }
        Aof = UnitGasFun.GetAof(SelectGS, A, B, Ae, Be, C, N, Pr, gasData);
        if (Aof <= 0) {
            throw new Exception("Aof不正确,请检查基本数据中选用的模型！");
        }
        YQB = 0;

        Rg = gasData.getRg(); //气体相对密度
        Ro = gasData.getRo();
        Rw = gasData.getRw();
        yco2 = gasData.getYco2(); //二氧化碳摩尔分数
        yh2s = gasData.getYh2s(); //硫化氢摩尔分数
        yn2 = gasData.getYn2(); //氮的摩尔分数
        ppc = gasData.getPpc(); //拟临界压力Mpa
        tpc = gasData.getTpc(); //拟临界温度K
        tt1 = SepT + P_Ntemp; //分离器温度
        Wm = gasData.getDwtd();


        Hwell = 0;
        for (i = 0; i < recTubes.size(); i++) {
            Hwell = Hwell + recTubes.get(i).getLenwell();
        }
        tw = UnitGasFun.GetJKWD(gasData, Hwell); ///井口温度
        D2 = SepD; //地面管线内径mm
        d1 = 0; //mm转m
        e = SepE / 1000; //管粗糙度 mm 转 m
        l = SepL; //井身长度m
        h = l; //井深度m 现在是与井身的长度相等

        double[] Arrayptf = new double[pointCount + 1];
        double[] ArrayQsc = new double[pointCount + 1];

        Step = Aof / (pointCount + 1);
        for (i = 0; i <= pointCount; i++) {

            Q = (i + 1) * Step;
            if (Q < GetlimitQ(gasData)) {
                Q = GetlimitQ(gasData) + (i + 1) * 10;
            }
            ArrayQsc[i] = Q;
            pwf = SepP;

            Map<String, double[]> map = CalcArray1Pwf(Pr, gasData); //地层算到井底
            double[] ArrQ_gas = map.get("ArrayQsc");
            double[] ArrP_gas = map.get("ArrayPwf");
            Pwater = ChaZhi_Pof_Q(ArrP_gas, ArrQ_gas, Q);

            map = CalWaterCL(Pr, gasData);
            double[] ArrP_water = map.get("Y1");
            double[] ArrQ_Water = map.get("X1");
            Qwater = ChaZhi_Qw_P(ArrP_water, ArrQ_Water, Pwater);
            SQB = Qwater / (Q / 10000); //水气比

            //从分离算到井口
            QJJTYLRequest qjjtylRequest = new QJJTYLRequest(1, 1, 1, 1, JiaoDu, gasData.getYLMethod(), gasData.getDxlMethod(), recTubes,
                    Wm, YQB, SQB, h, pwf, Rg, Ro, Rw, yco2, yn2, yh2s, ppc, tpc, tt1, tw, Q, D2, d1, e, l, h);
            Map<String, Object> QJJTYLmap = UnitGasFun.QJJTYL(qjjtylRequest, gasData);
            Arrayptf[i] = (double) QJJTYLmap.get("p1");
            t1 = (double) QJJTYLmap.get("t1");
            z1 = (double) QJJTYLmap.get("z1");
//            QJJTYL(1, 1, 1, 1, JiaoDu, GasData.YLMethod, GasData.DxlMethod, RecTube, Wm, yqb, sqb, h, pwf, rg, ro, rw, yco2, yn2, yh2s, ppc, tpc, Tt1, tw, Q, d2, d1, e, l, h, Arrayptf[i], t1, z1);

        }
        Map<String, double[]> returnMap = new HashMap<>();
        returnMap.put("ArrayQsc", ArrayQsc);
        returnMap.put("Arrayptf", Arrayptf);
        return returnMap;
    }

    /**
     * //分离器算到井口，有地面气嘴数据的情况
     *
     * @param gasData
     */
    public static Map<String, double[]> CalArray_SepToJKPr(GasData gasData, List<RecTube> recTubes) throws Exception {
        double Aof; //Aof
        double Step; //将Aof分段
        double Q; //产气量
        double Rg; //气体相对密度
        double e; //管粗糙度，m；
        double d1; //油管外径;
        double h; //井深度m;
        double l; //井深长度，m；
        double tt1, tt2; //井口温度，K
        double tw; //井底温度，K
        double yn2; //氮的摩尔分数；
        double yco2; //二氧化碳的摩尔分数
        double yh2s; //硫化氢摩尔分数
        double ppc, tpc;
        double t1, z1;
        Integer i;
        double pwf;
        double SepP, SepT, SepL, SepD, SepLJD, SepE, SepLFD, Duch, Lwch, Tuch;
        Integer JiaoDu;
        double D2, Pr, Ro, Rw;
        double QZ_d, QZ_t, QZ_L;
        double h1, h2, K, P2, P1, t2;
        double A, B, C, N, Ae, Be, Hwell, YQB, SQB, Wm;
        Integer SelectGS;

        double Qwater, Pwater;


        QZ_d = gasData.getDM_QZZJ(); //气嘴直径
        QZ_t = gasData.getDM_QZRKWD(); //气嘴温度
        QZ_L = gasData.getDM_JKDQZGC(); //气嘴到井口管长
        Pr = gasData.getCurrentPr(); //当前气藏压力
        SepP = gasData.getDM_FLQYL(); //分离器压力;
        SepT = gasData.getDM_FLQWD(); //分离器温度;
        SepL = gasData.getDM_DMGXCD(); //地面管线长度
        SepD = gasData.getDM_DMGXNJ(); //地面管线内径
        SepLJD = gasData.getDM_SPQJ(); //地面管线水平倾角
        SepE = gasData.getDM_CCD(); //地面管线粗糙度
        JiaoDu = (int) Math.floor(SepLJD);
        Wm = gasData.getDwtd();

        h1 = SepL - QZ_L; //分离器到气嘴长度
        h2 = QZ_L; //气嘴到井口长度

        A = gasData.getA();
        B = gasData.getB();
        C = gasData.getC();
        N = gasData.getN();
        Ae = gasData.getAe();
        Be = gasData.getBe();
        SelectGS = gasData.getCNMethod();
        switch (SelectGS) { //根据选择的计算方法
            case 0:
                if ((C <= 0) || (N <= 0)) {
                    throw new Exception("请在基本数据中输入C和N的值!");
                }
                break;
            case 1:
                if ((A <= 0) || (B <= 0)) {
                    throw new Exception("请在基本数据中输入A和B的值!");
                }
                break;
            case 2:
                if ((Ae <= 0) || (Be <= 0)) {
                    throw new Exception("请在基本数据中输入Ae和Be的值!");
                }
                break;
        }
        Aof = UnitGasFun.GetAof(SelectGS, A, B, Ae, Be, C, N, Pr, gasData);
        if (Aof <= 0) {
            throw new Exception("Aof不正确,请检查基本数据中选用的模型！");
        }
        Wm = gasData.getDwtd();
        YQB = 0;//StrtoFloatDef(TextYQB.Text, 0);
        Rg = gasData.getRg(); //气体相对密度
        Ro = gasData.getRo();
        Rw = gasData.getRw();
        yco2 = gasData.getYco2(); //二氧化碳摩尔分数
        yh2s = gasData.getYh2s(); //硫化氢摩尔分数
        yn2 = gasData.getYn2(); //氮的摩尔分数
        ppc = gasData.getPpc(); //拟临界压力Mpa
        tpc = gasData.getTpc(); //拟临界温度K
        tt1 = SepT + P_Ntemp; //分离器温度
        tw = QZ_t + P_Ntemp; //气嘴温度

        Hwell = 0;
        for (i = 0; i < recTubes.size(); i++) {
            Hwell = Hwell + recTubes.get(i).getLenwell();
        }
        tt2 = UnitGasFun.GetJKWD(gasData, Hwell); ///井口温度
        D2 = SepD; //地面管线内径mm
        d1 = 0; //mm转m
        e = SepE / 1000; //管粗糙度 mm 转 m
        l = SepL; //井身长度m
        h = l; //井深度m 现在是与井身的长度相等

        double[] Arrayptf = new double[pointCount + 1];
        double[] ArrayQsc = new double[pointCount + 1];

        Step = Aof / (pointCount + 1);
        K = gasData.getJRZS(); //绝热指数
        for (i = 0; i <= pointCount; i++) {

            Q = (i + 1) * Step;
            if (Q < GetlimitQ(gasData)) {
                Q = GetlimitQ(gasData) + (i + 1) * 10;
            }
            ArrayQsc[i] = Q;
            pwf = SepP;


            Map<String, double[]> map = CalcArray1Pwf(Pr, gasData); //地层算到井底
            double[] ArrQ_gas = map.get("ArrayQsc");
            double[] ArrP_gas = map.get("ArrayPwf");
            Pwater = ChaZhi_Pof_Q(ArrP_gas, ArrQ_gas, Q);
            map = CalWaterCL(Pr, gasData);
            double[] ArrP_water = map.get("Y1");
            double[] ArrQ_Water = map.get("X1");
            Qwater = ChaZhi_Qw_P(ArrP_water, ArrQ_Water, Pwater);
            SQB = Qwater / (Q / 10000); //水气比

            //从分离算到气嘴
            QJJTYLRequest qjjtylRequest = new QJJTYLRequest(1, 1, 1, 1, JiaoDu, gasData.getYLMethod(), gasData.getDxlMethod(),
                    recTubes, Wm, YQB, SQB, h1, pwf, Rg, Ro, Rw, yco2, yn2, yh2s, ppc, tpc, tt1, tw, Q, D2, d1, e, l, h);
            Map<String, Object> QJJTYLmap = UnitGasFun.QJJTYL(qjjtylRequest, gasData);
            P1 = (double) QJJTYLmap.get("p1");
            t1 = (double) QJJTYLmap.get("t1");
            z1 = (double) QJJTYLmap.get("z1");

//            QJJTYL(1, 1, 1, 1, JiaoDu,gasData.getYLMethod(),gasData.getDxlMethod(),recTubes, Wm,YQB,SQB, h1, pwf,Rg,Ro,Rw, yco2, yn2, yh2s, ppc, tpc,tt1, tw, Q,D2, d1, e, l, h, P1, t1, z1);
            //Tw 气嘴温度   tt1分离器温度
            P2 = P1 * Math.pow(2 / (K + 1), K / (K - 1));
            t2 = t1 * Math.pow(P2 / P1, (K - 1) / K);


            //从气嘴算到井口位置
            qjjtylRequest = new QJJTYLRequest(1, 1, 1, 1, JiaoDu, gasData.getYLMethod(),
                    gasData.getDxlMethod(), recTubes, Wm, YQB, SQB, h2, P2, Rg, Ro, Rw, yco2, yn2, yh2s, ppc, tpc, t2, tt2, Q, D2, d1, e, l, h);
            QJJTYLmap = UnitGasFun.QJJTYL(qjjtylRequest, gasData);
            Arrayptf[i] = (double) QJJTYLmap.get("p1");
            t1 = (double) QJJTYLmap.get("t1");
            z1 = (double) QJJTYLmap.get("z1");

//            QJJTYL(1, 1, 1, 1, JiaoDu, GasData.YLMethod, GasData.DxlMethod, RecTube, Wm, yqb, sqb, h2, P2, rg, ro, rw, yco2, yn2, yh2s, ppc, tpc, T2, Tt2, Q, d2, d1, e, l, h, Arrayptf[i], t1, z1);
            //Tw 井口  Tt:嘴前

        }
        Map<String, double[]> returnMap = new HashMap<>();
        returnMap.put("ArrayQsc", ArrayQsc);
        returnMap.put("Arrayptf", Arrayptf);
        return returnMap;
    }


    public static boolean GetDMGas(GasData gasData) {
        //判断是否输入了地面气嘴的相关数据
        double a, b, c;
        boolean result = false;

        a = gasData.getDM_QZZJ();
        b = gasData.getDM_QZRKWD();
        c = gasData.getDM_JKDQZGC();
        if ((a <= 0) || (b <= 0) || (c <= 0)) {
            return result;
        }
        return true;


    }

    public static Map CalcArray1Pwf(double Pr, GasData gasData) throws Exception {
        double Aof, Step, Pwf2, tmpPWF;
        double[] ArrayQsc, ArrayPwf;
        ArrayQsc = new double[pointCount + 1];
        ArrayPwf = new double[pointCount + 1];
        //产能预测
        double A = gasData.getA();
        double B = gasData.getB();
        double C = gasData.getC();
        double N = gasData.getN();
        double Ae = gasData.getAe();
        double Be = gasData.getBe();
        Integer SelectGS = gasData.getCNMethod();
        switch (SelectGS) { //根据选择的计算方法
            case 0:
                if ((C <= 0) || (N <= 0)) {
                    throw new Exception("请在基本数据中输入C和N的值!");
                }
                break;
            case 1:
                if ((A <= 0) || (B <= 0)) {
                    throw new Exception("请在基本数据中输入A和B的值!");
                }
                break;
            case 2:
                if ((Ae <= 0) || (Be <= 0)) {
                    throw new Exception("请在基本数据中输入Ae和Be的值!");
                }
                break;
        }
        Aof = UnitGasFun.GetAof(SelectGS, A, B, Ae, Be, C, N, Pr, gasData);
        if (Aof <= 0) {
            throw new Exception("Aof不正确,请检查基本数据中选用的模型！");
        }

        double Rg = gasData.getRg(); //气体相对密度
        double yco2 = gasData.getYco2() / 100; //二氧化碳摩尔分数
        double yh2s = gasData.getYh2s() / 100; //硫化氢摩尔分数
        double Yn2 = gasData.getYn2() / 100; //氮的摩尔分数
        double ppc = gasData.getPpc(); //拟临界压力Mpa
        double tpc = gasData.getTpc(); //拟临界温度K
        double t = gasData.getQCWD() + P_Ntemp; //气藏温度

        Step = Aof / (pointCount + 1); //分成10个点来画


        for (int i = 0; i <= pointCount; i++) {
            ArrayQsc[i] = Step * (i + 1);
            if (ArrayQsc[i] < GetlimitQ(gasData)) {
                ArrayQsc[i] = GetlimitQ(gasData) + (i + 1) * 10;
            }
            switch (SelectGS) {
                //根据选择的计算方法
                case 0:
                    ArrayPwf[i] = UnitGasFun.ZhishuShiC(C, N, Pr, ArrayQsc[i]);
                    break;
                case 1:
                    ArrayPwf[i] = UnitGasFun.ErXiangshiC(A, B, Pr, ArrayQsc[i]);
                    break;
                case 2:
                    tmpPWF = UnitGasFun.ErXiangshiC_NiYaLi(Ae, Be, UnitGasFun.Get_NiYaLi(gasData, Pr), ArrayQsc[i]);
                    Pwf2 = UnitGasFun.presem(Rg, yco2, yh2s, Yn2, ppc, tpc, t, tmpPWF);
                    ArrayPwf[i] = Pwf2;
                    break;
            }
        }

        Map<String, double[]> map = new HashMap<>();
        map.put("ArrayPwf", ArrayPwf);
        map.put("ArrayQsc", ArrayQsc);
        return map;
    }

    public static Map CalcArray2Ptf(Integer Flag, double[] Arraypwf, double D2, GasData gasData, List<RecTube> RecTubeList) throws Exception {
        //ident=1  根据井口压力ptf求
        //ident=2  根据油管直径d2求
        //ptf井口油压(井口压力)
        //d2//套管内径; d2-d1 为油管内径
        double Aof; //Aof
        double Step; //将Aof分段
        double Q; //产气量
        double rg; //气体相对密度
        double e; //管粗糙度，m；
        double D1; //油管外径;
        double h; //井深度m;
        double l; //井深长度，m；
        double tt; //井口温度，K
        double tw; //井底温度，K
        double yn2; //氮的摩尔分数；
        double yco2; //二氧化碳的摩尔分数
        double yh2s; //硫化氢摩尔分数
        double ppc, tpc;
        double t1, z1;
        Integer i;
        double pwf;
        double Pr;
        double YQB, SQB, Ro, Rw, Wm;
        double Qwater, Pwater;
        double[] Arrayptf = new double[pointCount + 1];
        double[] ArrayQsc = new double[pointCount + 1];

        Pr = gasData.getCurrentPr();
        Integer CNMethod = Integer.valueOf(gasData.getCNMethod());
        Aof = UnitGasFun.GetAof(CNMethod, gasData.getA(), gasData.getB(), gasData.getAe(), gasData.getBe(), gasData.getC(), gasData.getN(), Pr, gasData);
        if (Aof <= 0) {
            throw new Exception("Aof不正确,请检查基本数据中选用的模型！");
        }

        Wm = gasData.getDwtd();
        rg = gasData.getRg(); //气体相对密度
        Ro = gasData.getRo();
        Rw = gasData.getRw();
        yco2 = gasData.getYco2(); //二氧化碳摩尔分数
        yh2s = gasData.getYh2s(); //硫化氢摩尔分数
        yn2 = gasData.getYn2(); //氮的摩尔分数
        ppc = gasData.getPpc(); //拟临界压力Mpa
        tpc = gasData.getTpc(); //拟临界温度K
        tw = gasData.getQCWD() + P_Ntemp; //井底温度
        YQB = 0; // StrtoFloatDef(TextYQB.Text, 0);
        h = 0;
        for (i = 0; i < RecTubeList.size(); i++) {
            h = h + RecTubeList.get(i).getLenwell();
        }
        l = h;
        tt = UnitGasFun.GetJKWD(gasData, h);
        if (Flag == 1) {
            //下面只考虑一根油管的情况
            if (gasData.getSCFS() == 0) { //生产方式 0为油管生产
                D2 = RecTubeList.get(0).getDti(); //油管内径
                D1 = 0;
            } else {
                D2 = RecTubeList.get(0).getDci();
                D1 = RecTubeList.get(0).getDto();
            }
        } else {
            D2 = D2;
            RecTubeList.get(0).setDti(D2); //油管内径
            D1 = 0;
        }
        e = RecTubeList.get(0).getRoughwell() / 1000; //管粗糙度 mm 转 m
        Step = Aof / (pointCount + 1);
        for (i = 0; i <= pointCount; i++) {
            //分成10个点来显示
            Q = (i + 1) * Step;
            if (Q < GetlimitQ(gasData)) {
                Q = GetlimitQ(gasData) + (i + 1) * 10;
            }
            ArrayQsc[i] = Q;
            pwf = Arraypwf[i];

            Map<String, double[]> map = CalcArray1Pwf(Pr, gasData); //地层算到井底
            double[] ArrQ_gas = map.get("ArrayQsc");
            double[] ArrP_gas = map.get("ArrayPwf");

            Pwater = ChaZhi_Pof_Q(ArrP_gas, ArrQ_gas, Q);

            map = CalWaterCL(Pr, gasData);
            double[] ArrP_water = map.get("Y1");
            double[] ArrQ_Water = map.get("X1");

            Qwater = ChaZhi_Qw_P(ArrP_water, ArrQ_Water, Pwater);
            SQB = Qwater / (Q / 10000); //水气比

            if (pwf < 0.1) {
                Arrayptf[i] = 0;
            } else {
                QJJTYLRequest qjjtylRequest = new QJJTYLRequest(0, -1, 1, 0, 90, gasData.getYLMethod(),
                        gasData.getDxlMethod(), RecTubeList, Wm, YQB, SQB, h, pwf, rg, Ro, Rw,
                        yco2, yn2, yh2s, ppc, tpc, tt, tw, Q, D2, D1, e, l, h);
                Map<String, Object> QJJTYLmap = UnitGasFun.QJJTYL(qjjtylRequest, gasData);
                Arrayptf[i] = (double) QJJTYLmap.get("p1");
                t1 = (double) QJJTYLmap.get("t1");
                z1 = (double) QJJTYLmap.get("z1");


//            QJJTYL(0, -1, 1, 0, 90, GasData.YLMethod, GasData.DxlMethod, RecTube, Wm, yqb, sqb, h, pwf, rg, ro, rw, yco2, yn2, yh2s, ppc, tpc, Tt, tw, Q, d2, d1, e, l, h, Arrayptf[i], t1, z1);}
                ////井底算到井口

            }
        }
        Map<String, double[]> returnMap = new HashMap<>();
        returnMap.put("Arrayptf", Arrayptf);
        returnMap.put("ArrayQsc", ArrayQsc);
        return returnMap;
    }


    public static int GetlimitQ(GasData gasData) {
        int LimitQ = 3000;
        if (gasData.getYLMethod() == 2) {
            LimitQ = 15000;
        }
        return LimitQ;
    }

    public static double ChaZhi_Pof_Q(double[] YL, double[] Qg, double Q) {
        //根据产气量求压力
        Integer i;
        Integer N;
        double Result = 0;
        N = YL.length;
        for (i = 0; i <= N - 2; i++) {
            if ((Q >= Qg[i]) && (Q <= Qg[i + 1])) {
                Result = YL[i] + (Q - Qg[i]) / (Qg[i + 1] - Qg[i]) * (YL[i + 1] - YL[i]);
                break;
            }
        }
        return Result;

    }

    public static double ChaZhi_Qw_P(double[] YL, double[] Qw, double P) {
        Integer i, N;
        double tmp;

        double Result = 0;
        N = YL.length;
        for (i = 0; i <= N - 2; i++) {
            if ((P >= YL[i]) && (P <= YL[i + 1])) {
                tmp = Qw[i] + (P - YL[i]) / (YL[i + 1] - YL[i]) * (Qw[i + 1] - Qw[i]);
                if (tmp <= 0) {
                    tmp = 0.001;
                }
                Result = tmp;
                break;
            }
        }
        return Result;
    }

    public static Map CalWaterCL(double Pr, GasData gasData) throws Exception {
        double Jw, Pstart;
        Integer i;

//        Integer result = 0;
        //Pr = ReadDbValue('FrmData_ShiJing', Name_CurrentPr, 0); //当前气藏压力
        Jw = gasData.getJw();
        if (Jw == 0) {
            throw new Exception("请输入产液能试井数据！");
        }
        Pstart = gasData.getPstart();

        double[] X1 = new double[101];
        double[] Y1 = new double[101];
        for (i = 0; i < 101; i++) {
            Y1[i] = Pr * i / 100;
            X1[i] = (Pr - Y1[i] - Pstart) * Jw;
        }
//        result = 1;
        Map<String, double[]> map = new HashMap<>();
        map.put("X1", X1);//ArrQ_Water
        map.put("Y1", Y1);//ArrP_water
        return map;
    }
}
