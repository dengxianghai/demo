package com.caiyou.ChangNengFenXi;

import com.caiyou.algorithm.UnitGasFun;
import com.caiyou.algorithm.UnitYDCommon;
import com.caiyou.entity.GasData;
import com.caiyou.entity.ProduceGasResponse;
import com.caiyou.entity.ProduceWaterResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 产能分析---产能方程回归
 *
 * @author dxh
 * @date 2021/8/10
 */
public class ProductivityEquationRegression {

    private static double P_Ntemp = 273.15;

    /**
     * 产气回归
     *
     * @param gasData
     * @param ArrPr    地层压力(界面输入的数据）
     * @param pwf      流压(界面输入的数据）
     * @param Qsc      产气量(界面输入的数据）
     * @param SelectGs 公式选择  0指数式法,1二项式法,2拟压力
     * @param S        修正值
     */
    public static ProduceGasResponse produceGas(GasData gasData, double[] ArrPr, double[] pwf, double[] Qsc, Integer SelectGs, double S) throws Exception {
        double Pr = gasData.getCurrentPr(); //积液前    当前气藏压力
        double Rg = gasData.getRg(); //气体相对密度
        double yco2 = gasData.getCO2() / 100; //二氧化碳摩尔分数
        double yh2s = gasData.getH2S() / 100; //硫化氢摩尔分数
        double Yn2 = gasData.getN2() / 100; //氮的摩尔分数
        double ppc = gasData.getPpc(); //拟临界压力Mpa
        double tpc = gasData.getTpc(); //拟临界温度K
        double t = gasData.getQCWD() + P_Ntemp; //气藏温度

        //无需
        Integer i;
        Integer Pr2, K;
        double a, b;
        double c, n;
        double r, Aof;
        double PwfTmp;
        double[] da, minmax, e;
        double niYali;


        if (Pr <= 0) {
            throw new Exception("当前气藏压力不正确,请检查基本数据!");
        }
        K = ArrPr.length;
        if (K < 3) {
            throw new Exception("数据组数小于3,请输入数据!");
        }
        for (i = 0; i < K; i++) {
            if (ArrPr[i] < pwf[i]) {
                throw new Exception("地层压力小于流压，请检查数据!");
            }
        }
        double[] DeltP = new double[K];
        double[] PwfTZ = new double[K];

        for (i = 0; i < K; i++) {
            if (Qsc[i] == 0) {
                Qsc[i] = 0.001;
            }
            if (SelectGs == 0) {//指数式法
                DeltP[i] = Math.pow(ArrPr[i], 2) - Math.pow(pwf[i], 2);
            } else if (SelectGs == 1) { //二项式法
                DeltP[i] = (Math.pow(ArrPr[i], 2) - Math.pow(pwf[i], 2) - S) / Qsc[i];
            } else {
                DeltP[i] = (UnitGasFun.Get_NiYaLi(gasData, ArrPr[i]) - UnitGasFun.Get_NiYaLi(gasData, pwf[i]) - S) / Qsc[i];
            }
            if (DeltP[i] <= 0) {
                DeltP[i] = 0.001;
            }
            if (pwf[i] > Pr) {
                throw new Exception("错误的数据！(流压不能大于地层压力)");
            }
        }
        double[] x = new double[K];
        double[] y = new double[K];

        Pr2 = Integer.valueOf((int) Math.floor(Pr)); //向下取整
        if (Pr2 < Pr) {
            Pr2 = Pr2 + 1;
        }
        if (SelectGs == 0) { //指数式法
            for (i = 0; i < K; i++) {
                y[i] = pwf[i];
                x[i] = Qsc[i];
            }
            Map map = UnitYDCommon.nihe2_dll(x, y, K);
            da = (double[]) map.get("d");
            minmax = (double[]) map.get("minmax");
            e = (double[]) map.get("e");

            c = Math.exp(da[0]);
            n = da[1];
            Aof = c * Math.pow(Pr * Pr + minmax[0] + 1, n) - Math.abs(minmax[2]) - 1; //ZhishuShi(C,N,Pr,0);

            double[] x1 = new double[K];
            double[] y1 = new double[K];
            for (i = 0; i < K; i++) {
                x1[i] = Pr / (K - 1) * i;
                if (x1[i] == Pr) {
                    y1[i] = 0;
                } else {
                    y1[i] = c * Math.pow(Pr * Pr - x1[i] * x1[i] + minmax[0] + 1, n) - Math.abs(minmax[2]) - 1;
                }
                PwfTZ[i] = Math.sqrt(Math.pow(Pr, 2) - Math.pow(Qsc[i] / c, 1 / n));
            }
            ProduceGasResponse produceGasResponse = new ProduceGasResponse(c, n, Aof, e[1], x1, y1, Qsc, PwfTZ);

            return produceGasResponse;
        } else if (SelectGs == 1) { //二项式法

            for (i = 0; i < K; i++) {
                y[i] = DeltP[i];
                x[i] = Qsc[i];
            }
            Map<String, Double> map = UnitYDCommon.HuiGui02(x, y); //ErXiangshi(a,b,Pr,0);
            a = map.get("a");
            b = map.get("b");
            r = map.get("r");

            Aof = (-a + Math.pow(Math.pow(a, 2) + 4 * b * Math.pow(Pr, 2), 0.5)) / (2 * b);
            Pr2 = Integer.valueOf((int) Math.floor(Aof)); //取整
            double[] x1 = new double[Pr2 + 1];
            double[] y1 = new double[Pr2 + 1];
            for (i = 0; i <= Pr2; i++) {
                if (i == 0) {
                    y1[i] = Aof;
                    x1[i] = 0;
                } else {
                    y1[i] = i;
                    x1[i] = Math.pow(Math.pow(Pr, 2) - a * y1[i] - b * Math.pow(y1[i], 2), 0.5);
                }
            }
            for (i = 0; i < K; i++) {
                if (Math.pow(Pr, 2) - (a * Qsc[i] + b * Qsc[i] * Qsc[i]) < 0) {
                    PwfTZ[i] = 0;
                } else
                    PwfTZ[i] = Math.sqrt(Math.pow(Pr, 2) - (a * Qsc[i] + b * Qsc[i] * Qsc[i]));
            }
            ProduceGasResponse produceGasResponse = new ProduceGasResponse(a, b, Aof, r, x1, y1, Qsc, PwfTZ);
            return produceGasResponse;
        } else {
            for (i = 0; i < K; i++) {
                y[i] = DeltP[i];
                x[i] = Qsc[i];
            }
            Map<String, Double> map = UnitYDCommon.HuiGui02(x, y); //ErXiangshi(a,b,Pr,0);
            a = map.get("a");
            b = map.get("b");
            r = map.get("r");


            Aof = (-a + Math.pow(Math.pow(a, 2) + 4 * b * UnitGasFun.Get_NiYaLi(gasData, Pr), 0.5)) / (2 * b);

            Pr2 = Integer.valueOf((int) Math.floor(Aof)); //取整
            double[] x1 = new double[Pr2 + 1];
            double[] y1 = new double[Pr2 + 1];

            niYali = UnitGasFun.Get_NiYaLi(gasData, Pr);
            for (i = 0; i <= Pr2; i++) {
                if (i == 0) {
                    y1[i] = Aof;
                    x1[i] = 0;
                } else {
                    y1[i] = i;
                    PwfTmp = niYali - a * y1[i] - b * Math.pow(y1[i], 2);
                    x1[i] = UnitGasFun.presem(Rg, yco2, yh2s, Yn2, ppc, tpc, t, PwfTmp);
                }
            }
            for (i = 0; i < K; i++) {

                PwfTmp = niYali - (a * Qsc[i] + b * Qsc[i] * Qsc[i]);
                if (PwfTmp <= 0) {
                    PwfTZ[i] = 0;
                } else {
                    PwfTZ[i] = UnitGasFun.presem(Rg, yco2, yh2s, Yn2, ppc, tpc, t, PwfTmp);
                }
            }

            ProduceGasResponse produceGasResponse = new ProduceGasResponse(a, b, Aof, r, x1, y1, Qsc, PwfTZ);
            return produceGasResponse;
        }

    }

    /**
     * 产水回归
     * @param gasData
     * @param ArrPr 地层压力(界面输入的数据）
     * @param pwf 流压(界面输入的数据）
     * @param Qw 产水量(界面输入的数据）
     * @return
     * @throws Exception
     */
    public static ProduceWaterResponse produceWater(GasData gasData, double[] ArrPr, double[] pwf, double[] Qw) throws Exception {
        //无需
        Integer i;
        double k, b, c;

        double Pr = gasData.getCurrentPr(); //积液前   当前气藏压力
        if (Pr <= 0) {
            throw new Exception("当前气藏压力不正确,请检查基本数据!");
        }
        int N = ArrPr.length;
        if (N < 3) {
            throw new Exception("数据组数小于3,请输入数据!");
        }
        for (i = 0; i < N; i++) {
            if (ArrPr[i] < pwf[i]) {
                throw new Exception("地层压力小于流压，请检查数据!");
            }
        }
        double[] x = new double[N];
        double[] y = new double[N];

        for (i = 0; i < N; i++) {
            y[i] = ArrPr[i] - pwf[i];
            x[i] = Qw[i];
        }
//        if FitLine(x,y,N,k,b,c)=1 then
        Map<String, Double> map = UnitGasFun.FitLine(x, y, N);
        b = map.get("b");
        k = map.get("k");


        x = new double[101];
        y = new double[101];

        for (i = 0; i < 101; i++) {
            y[i] = Pr * i / 100;
            x[i] = (Pr - y[i] - b) / k;
        }
        ProduceWaterResponse produceWaterResponse = new ProduceWaterResponse(k,b,x,y,Qw,pwf);
        return produceWaterResponse;
    }
}
