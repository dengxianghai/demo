package com.caiyou.ChangNengFenXi;

import com.caiyou.algorithm.UnitGasFun;
import com.caiyou.entity.GasData;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 产能分析---气井产能分析
 */
public class GasWellProductivityAnalysis {

    private static double P_Ntemp = 273.15;

    /**
     * 产气
     * @param SelectGs 0指数式，1压力平方，2拟压力平方
     * @param gasData
     */
    public static Map Cal_CNFX(Integer SelectGs, GasData gasData) throws Exception {
        double Pr = gasData.getCurrentPr();//积液前气藏压力
        double A = gasData.getA();
        double B = gasData.getB();
        double Ae = gasData.getAe();
        double Be = gasData.getBe();
        double C = gasData.getC();
        double N = gasData.getN();

        double Rg; //气体相对密度
        double yco2; //二氧化碳摩尔分数
        double yh2s; //硫化氢摩尔分数
        double Yn2; //氮的摩尔分数
        double ppc; //拟临界压力Mpa
        double tpc; //拟临界温度K
        double t = gasData.getQCWD() + P_Ntemp; //气藏温度

        //无需输入参数
        double Aof;//       TextAof: TRzEdit;      如果在积液前页面，不输入对应的值，就会报错Aof错误
        Integer i;
        double Qsc2;
        double Qsc; //产气量
        double Pwf; //测试点井底流压
        double pwf2;
        double TmpPWF;
        double[] X1 = new double[21];
        double[] Y1 = new double[21];

        Rg = 0;
        yco2 = 0;
        yh2s = 0;
        Yn2 = 0;
        ppc = 0;
        tpc = 0;
        t = 0;
//        SelectGs =gasData.getCNMethod();//积液前的产能计算模型，0指数式，1压力平方，2拟压力平方
        if (Pr <= 0) {
            throw new Exception("气藏压力不正确,请检查基本数据!");
        }
        Aof = UnitGasFun.GetAof(SelectGs, A, B, Ae, Be, C, N, Pr, gasData);

        DecimalFormat df = new DecimalFormat("0.0000");
        Aof = Double.parseDouble(df.format(Aof / 10000));
//        TextAof.Text = FormatFloat('0.0000', Aof / 10000);
        Aof = Aof * 10000;
        if (Aof == 0) {
            throw new Exception("Aof为零,请检查基本数据是否输入正确!");
        }
        Pwf = 0;
        Qsc = 0;
        Qsc2 = 0;
        switch (SelectGs) {
            //根据选择的计算方法
            case 0:
                if ((C == 0) || (N == 0)) {
                    throw new Exception("请在基本数据中输入C和N的值!");
                }
                break;
            case 1:
                if ((A == 0) || (B == 0)) {
                    throw new Exception("请在基本数据中输入A和B的值!");
                }
                break;
            case 2:
                if ((Ae == 0) || (Be == 0)) {
                    throw new Exception("请在基本数据中输入Ae和Be的值!");
                }
                break;
        }
        pwf2 = 0;
        Qsc2 = Aof / 20;

        for (i = 0; i <= 20; i++) {
            switch (SelectGs) {
                //根据选择的计算方法
                case 0:
                    pwf2 = UnitGasFun.ZhishuShiC(C, N, Pr, i * Qsc2);
                    break;
                case 1:
                    pwf2 = UnitGasFun.ErXiangshiC(A, B, Pr, i * Qsc2);
                    break;
                case 2:
                    TmpPWF = UnitGasFun.ErXiangshiC_NiYaLi(Ae, Be, UnitGasFun.Get_NiYaLi(gasData,Pr), i * Qsc2);
                    pwf2=UnitGasFun.presem(Rg, yco2, yh2s, Yn2, ppc, tpc, t, TmpPWF);
                    break;
            }
            X1[i] = pwf2;
            Y1[i] = i * Qsc2 / 10000;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("x",X1);
        map.put("y",Y1);
        return map;
    }

    /**
     * 产水
     * @param gasData
     * @return
     * @throws Exception
     */
    public static Map dynamicPredictionProductWaterInflow( GasData gasData) throws Exception {
        double Pr=gasData.getCurrentPr();
        double Jw=gasData.getJw();
        double Pstart=gasData.getPstart();

        if (Jw == 0 ) {
            throw new Exception("请输入产液能试井数据！");
        }
        double []X1=new double[101];
        double []Y1=new double[101];
        for(int i=0;i<101;i++) {
            Y1[i] =Pr * i / 100;
            X1[i] =(Pr - Y1[i] - Pstart) * Jw;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("x",X1);
        map.put("y",Y1);
        return map;
    }


}