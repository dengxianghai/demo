//package com.caiyou.algorithm;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * 井下节流
// *
// *
// */
//public class Jingxiajieliu {
//
//    private static final Logger logger = LoggerFactory.getLogger(Jingxiajieliu.class);
//
//    /**
//     *
//     * @param bottom_pressure 是否计算井底流压
//     */
//    public void haha(boolean bottom_pressure){
//        Pr = GasData.CurrentPr; //当前气藏压力
//        SepP = GasData.DM_FLQYL; //分离器压力;
//        SepT = GasData.DM_FLQWD; //分离器温度;
//        SepL = GasData.DM_DMGXCD; //地面管线长度
//        SepD = GasData.DM_DMGXNJ; //地面管线内径
//        SepLJD = GasData.DM_SPQJ; //地面管线水平倾角
//        SepE = GasData.DM_CCD; //地面管线粗糙度
//        JiaoDu = Trunc(SepLJD);
//        A = GasData.A;
//        B = GasData.B;
//        C = GasData.C;
//        N = GasData.N;
//        Ae = GasData.Ae;
//        Be = GasData.Be;
//        SelectGS = GasData.CNMethod;
//        case SelectGS of //根据选择的计算方法
//        0:
//        begin
//        if (C <= 0) or (N <= 0) then
//                begin
//        ShowMsg('请在基本数据中输入C和N的值!');
//        Abort;
//        end;
//        end;
//        1:
//        begin
//        if (A <= 0) or (B <= 0) then
//                begin
//        ShowMsg('请在基本数据中输入A和B的值!');
//        Abort;
//        end;
//        end;
//        2:
//        begin
//        if (Ae <= 0) or (Be <= 0) then
//                begin
//        ShowMsg('请在基本数据中输入Ae和Be的值!');
//        Abort;
//        end;
//        end;
//        end;
//        GetAOF(SelectGS, A, B, Ae, Be, C, N, Pr, Aof);
//        if Aof <= 0 then
//                begin
//        ShowMsg('Aof不正确,请检查基本数据中选用的模型！');
//        Abort;
//        end;
//        YQB = 0;
//
//        Rg = GasData.Rg; //气体相对密度
//        Ro = GasData.Ro;
//        Rw = GasData.Rw;
//        yco2 = GasData.yco2; //二氧化碳摩尔分数
//        yh2s = GasData.yh2s; //硫化氢摩尔分数
//        Yn2 = GasData.Yn2; //氮的摩尔分数
//        ppc = GasData.Ppc; //拟临界压力Mpa
//        tpc = GasData.Tpc; //拟临界温度K
//        tt1 = SepT + P_Ntemp; //分离器温度
//        Wm = GasData.Dwtd;
//        if GetYTGData(1, RecTube) = False then exit;
//        Hwell = 0;
//        for i = 0 to length(RecTube) do
//            Hwell = Hwell + RecTube[i].lenwell;
//        tw = GetJKWD(Hwell); ///井口温度
//        D2 = SepD; //地面管线内径mm
//        D1 = 0; //mm转m
//        e = SepE / 1000; //管粗糙度 mm 转 m
//        l = SepL; //井身长度m
//        h = l; //井深度m 现在是与井身的长度相等
//        SetLength(Arrayptf, 0);
//        SetLength(Arrayptf, NDianShu + 1);
//        SetLength(ArrayQsc, 0);
//        SetLength(ArrayQsc, NDianShu + 1);
//        Step = Aof / (NDianShu + 1);
//        for i = 0 to NDianShu do
//            begin
//        Q = (i + 1) * Step;
//        if Q < LimitQ then Q = LimitQ + (i + 1) * 10;
//        ArrayQsc[i] = Q;
//        Pwf = SepP;
//        Pwater = ChaZhi_Pof_Q(ArrP_gas, ArrQ_gas, Q);
//        Qwater = ChaZhi_Qw_P(ArrP_water, ArrQ_Water, Pwater);
//        SQB = Qwater / (Q / 10000); //水气比
//        try
//        //从分离算到井口
//        QJJTYL(1, 1, 1, 1, JiaoDu, GasData.YLMethod, GasData.DxlMethod, RecTube, Wm, yqb, sqb, h, pwf, rg, ro, rw, yco2, yn2, yh2s, ppc, tpc, Tt1, tw, Q, d2, d1, e, l, h, Arrayptf[i], t1, z1);
//        except end;
//        end;
//    }
//
//
//
//}
