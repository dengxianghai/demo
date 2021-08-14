package com.caiyou.algorithm;

import com.caiyou.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.util.*;

public class UnitGasFun {

    private static final Logger logger = LoggerFactory.getLogger(UnitGasFun.class);
    private static double pi = Math.PI;
    private static final double P_Ntemp = 273.15;

    /**
     * 获取油套管数据
     *
     * @return
     */
    public static Map GetYTGData() {
        Map<String, Object> map = new HashMap();
        boolean result = false;


        //油套管数据
        //TODO:select * from Gas_JTYTGSJ order by 标识 asc


        List<RecTube> list = new ArrayList<>();//查询数据
        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getDti() <= 0) {
                logger.info("油管内径不正确!请检查基本数据");
                map.put("result", false);
                return map;
            }
            if (list.get(i).getLenwell() <= 0) {
                logger.info("油管长度不正确!请检查基本数据");
                map.put("result", false);
                return map;
            }
            if (list.get(i).getRoughwell() <= 0) {
                logger.info("油管粗糙度不正确!请检查基本数据");
                map.put("result", false);
                return map;
            }

        }

        map.put("result", true);
        map.put("RecTubeList", list);

        return map;
    }


    public static DXLCalResponse DXLCal(DXLCalRequest request) {
        //需要输入的参数
        List<RecTube> recTubes = request.getRecTubeList();
        Integer Method = request.getMethod(); //多相流计算方法        Hagedorn-Brown=0、Beggs-Brill=1、Duns-Ros=2、Okiszewski=3、Mukherjee-Brill=4
        Integer Fx = request.getFx();//Fx-方向 1向下 －1向上
        Integer SCFS = request.getSCFS(); //是否油管生产：0 油管生产  1油套管环空生产
        double DeltH = request.getDeltH();//温度压力剖面功能时，传入的是步长，其他功能待定
        double PressJZ = request.getPressJZ();//压力校正系数
        double WGR = request.getWGR();//水气比
        double OGR = request.getOGR();//油气比
        double Rg = request.getRg();//气体相对密度
        double Ro = request.getRo();//油的密度
        double Rw = request.getRw();//水的密度
        double Wm = request.getWm();//温度压力剖面计算中，是地温梯度，其他待定
        double H = request.getH();
        double Qg = request.getQg();//产气量
        double PStart = request.getPStart();//初始压力
        double TStart = request.getTStart();//初始温度 K 温度要用K
        double HStart = request.getHStart();//初始高度
        double Hend = request.getHend();//结束高度
        double Tpc = request.getTpc();//拟临界温度
        double Ppc = request.getPpc();//拟临界压力
        double e; //油管粗糙度

        //不需要输入的参数
        LinkedList<String> LiuTai;
        LinkedList<VDxlResult> vDxlResults = new LinkedList<>();
        double pP1;
        double TT1;
        double HH1;
        double pP2, TT2, HH2, Deh1 = 0;
        Integer j;
        double HL, Z;
        double PL, Pg, Pm, Vsl, Vsg;
        double Angle; //角度
        double Dt; //油管生产时为油管内径 Dt=Dti，套管生产时为套管内径Dt=sqrt(sqr(dci)-sqr(dto)) 当量油管尺寸，mm

        String Flow;
        Integer Result = 0;
        pP1 = PStart; // 开始压力
        HH1 = HStart;
        TT1 = TStart; //井口温度
        Angle = pi / 2;
        j = 0;
        while (true) {
            //这一地方主要为初始点计算HL等参数
            if (Fx == 1) {
                if ((HH1 == 0) && (j == 0)) {
                    Deh1 = 0;
                } else {
                    Deh1 = DeltH;
                }
            }
            if (Fx == -1) {
                if ((HH1 == H) && (j == 0)) {
                    Deh1 = 0;
                } else {
                    Deh1 = DeltH;
                }
            }
            if ((Fx == 1) && (HH1 + Deh1 > Hend)) {
                Deh1 = Hend - HH1;
            }
            if ((Fx == -1) && (HH1 - Deh1 < Hend)) {
                Deh1 = HH1 - Hend;
            }
            //if Fx = 1 then //从上往下时
//             Dt = GetDt(RecTube, HH1, SCFS, e);//当量油管尺寸，mm
            Dt = (double) GetDt(recTubes, HH1, SCFS).get("result");
            e = (double) GetDt(recTubes, HH1, SCFS).get("e");

            YDGasDxlRequest ydGasDxlRequest = new YDGasDxlRequest(Method, Fx, Deh1, PressJZ, Dt, WGR, OGR, Rg, Ro, Rw, Wm, H,
                    Qg, pP1, TT1, HH1, Angle, Tpc, Ppc, e);
            YDGasDxlResponse ydGasDxlResponse = YadanGasDxl.YDGasDxl(ydGasDxlRequest);
            pP2 = ydGasDxlResponse.getP2();//keepDecimals(ydGasDxlResponse.getP2(),2);
            TT2 = ydGasDxlResponse.getT2();//keepDecimals(ydGasDxlResponse.getT2(),2);
            HH2 = ydGasDxlResponse.getH2();
            Vsl = ydGasDxlResponse.getVsl();
            Vsg = ydGasDxlResponse.getVsg();
            HL = ydGasDxlResponse.getHL();
            Z = ydGasDxlResponse.getZ();
            Flow = ydGasDxlResponse.getFlow();
            //  Dt =GetDt(RecTube, H - HH1, SCFS, e); //当量油管尺寸，mm

            if (((HH2 >= Hend) && (Fx == 1)) || ((HH2 - Deh1 <= Hend) && (Fx == -1))) {
                break;
            } else {
                pP1 = pP2;
                TT1 = TT2;
                HH1 = HH2;
                VDxlResult vDxlResult = new VDxlResult();
                vDxlResult.setDepth(HH1);//井深
                vDxlResult.setPressure(pP1);//压力
                vDxlResult.setTemperature(TT1 - P_Ntemp);//温度
                vDxlResult.setLiquidFlowRate(Vsl);//液体流速
                vDxlResult.setGasFlowRate(Vsg);//气体流速
                vDxlResult.setLiquidHoldupRate(HL);//持液率
                vDxlResult.setGasCompressibility(Z);//气体压缩系数
                vDxlResult.setFlow(Flow);//流态

                vDxlResults.add(vDxlResult);
                j = j + 1;
            }

        }
        if ((Fx == -1) && (HH2 - Deh1 <= 0)) {
            HH2 = 0;
        }
        VDxlResult vDxlResult = new VDxlResult();
        vDxlResult.setDepth(HH2);//井深
        vDxlResult.setPressure(pP2);//压力
        vDxlResult.setTemperature(TT2 - P_Ntemp);//温度
        vDxlResult.setLiquidFlowRate(Vsl);//液体流速
        vDxlResult.setGasFlowRate(Vsg);//气体流速
        vDxlResult.setLiquidHoldupRate(HL);//持液率
        vDxlResult.setGasCompressibility(Z);//气体压缩系数
        vDxlResult.setFlow(Flow);//流态
        vDxlResults.add(vDxlResult);

        DXLCalResponse response = new DXLCalResponse();
        response.setvDxlResults(vDxlResults);
        return response;
    }

    /**
     * double数字根据需求进行保留位数的计算
     * @param f
     * @param number
     * @return
     */
    public static double keepDecimals(double f,int number){

        BigDecimal b   =   new   BigDecimal(f);
        double   f1   =   b.setScale(number,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    public static Map GetDt(List<RecTube> recTubes, double H, Integer SCFS) {
        double result = 0;
        double Htotal = 0;
        double e = 0.02;
        for (int i = 0; i < recTubes.size(); i++) {

            Htotal = Htotal + recTubes.get(i).getLenwell();
            if (H <= Htotal) {
                switch (SCFS) {
                    case 0:
                        result = recTubes.get(i).getDti();
                        break;
                    case 1:
                        result = Math.sqrt(Math.pow(recTubes.get(i).getDci(), 2) - Math.pow(recTubes.get(i).getDto(), 2));
                        break;
                }
                e = recTubes.get(i).getRoughwell(); //粗糙度
                break;
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("e", e);
        map.put("result", result);
        return map;
    }

    /**
     * 用半解析法由拟压力计算气体压力的过程
     * 用牛顿拉夫逊方法迭代救解
     * 输入变量：
     * rg 气体相对密度
     * rn2 氮的摩尔分数
     * yco2 二气化碳摩尔分数
     * yh2s 硫化氢摩尔分数
     * ppc 拟临界压力MPa
     * tpc 拟临界温度K
     * t 温度K
     * mpp 拟压力 MPa^2/mpa*s
     * 输出变量 P 压力 Mpa
     * 需调用：sempres
     *
     * @param rg
     * @param yco2
     * @param yh2s
     * @param yn2
     * @param ppc
     * @param tpc
     * @param t
     * @param mpp
     * @return
     */
    public static double presem(double rg, double yco2, double yh2s, double yn2, double ppc, double tpc, double t, double mpp) throws Exception {
        double p = 0;
        double tr, mg, r, dc0, dr;
        double a, b, c, d, e, f, g, g1, g2, g3, j, h;
        double ff, exa, exa1, a0, a1, a2, a3, a4, a5, a6;
        double fp, pp;
        double Dr, mp;

        tr = t / tpc;
        mg = 28.97 * rg;
        r = 8.314001E-03;
        dc0 = ppc * mg / (0.27 * r * tpc);
        a = 0.06423;
        b = 0.5353 * tr - 0.6123;
        c = 0.3151 * tr - 1.0467 - 0.5783 / Math.pow(tr, 2);
        d = tr;
        e = 0.6816 / Math.pow(tr, 2);
        f = 0.6845;
        g1 = yh2s * (5.7 * rg - 1.7) * 0.00001;
        g2 = yco2 * (5 * rg + 1.7) * 0.00001;
        g3 = yn2 * (5 * rg + 4.7) * 0.00001;
        g = (9.4 + 0.2 * mg) * Math.pow(1.8 * t, 1.5);
        g = 0.0001 * g / (209 + 19 * mg + 1.8 * t) + g1 + g2 + g3;
        j = 1.7 - 197.2 / (1.8 * t) - 0.002 * mg;
        h = (3.5 + 986 / (1.8 * t) + 0.01 * mg) * Math.pow(0.001, j);
        dr = 1;
        while (true) {
            Map map = SemPres(1, rg, yco2, yh2s, yn2, ppc, tpc, t, p);
            Dr = (double) map.get("Dr");
            mp = (double) map.get("mp");

            ff = mpp - mp;
            exa = Math.exp(-f * Math.pow(dr, 2));
            exa1 = Math.exp(-h * Math.pow(dc0 * dr, j));
            a0 = -2 / (g * tr) * Math.pow(dc0 * r * t / mg, 2);
            a1 = 6 * a * Math.pow(dr, 6);
            a2 = 3 * b * Math.pow(dr, 3);
            a3 = 2 * c * Math.pow(dr, 2);
            a4 = d * dr;
            a5 = 3 * e * Math.pow(dr, 3) * (1 + f * Math.pow(dr, 2)) * exa;
            a6 = -2 * e * Math.pow(f, 2) * Math.pow(dr, 7) * exa;
            fp = a0 * (a1 + a2 + a3 + a4 + a5 + a6) * exa1;
            dr = dr - ff / fp;
            if (Math.abs(ff / fp) < 0.0001) {
                break;
            }
        }
        exa = Math.exp(-f * Math.pow(dr, 2));
        pp = d * dr + e * Math.pow(dr, 3) * (1 + f * Math.pow(dr, 2)) * exa;
        pp = a * Math.pow(dr, 6) + b * Math.pow(dr, 3) + c * Math.pow(dr, 2) + pp;
        p = ppc * pp / 0.27;

        return p;
    }

    /**
     * 用半解析法计算气体拟压力的过程
     * (i) ident=0 已知压力 计算拟压力
     * (ii) ident=1 已知拟压力 计算压力
     * 求解方法：采用一种逐级递增的迭代方法求解
     * 输入变量:
     * rg 气体相对密度
     * yn2 氮的摩尔分数
     * yco2 二氧化碳摩尔分数
     * yh2s 硫化氢摩尔分数
     * ppc 拟临界压力MPa
     * tpc 拟临界温度 K
     * t 温度 K
     * ident=0 P压力,MPa
     * ident=1 dr 拟对比密度
     * 输出变量: mp 拟压力 MPa^2/mPa*s
     * 需调用过程:当ident=0时需要调用dprzf
     *
     * @param Ident
     * @param rg
     * @param Yco2
     * @param Yh2s
     * @param Yn2
     * @param Ppc
     * @param Tpc
     * @param t
     * @param p
     */
    public static Map SemPres(Integer Ident, double rg, double Yco2, double Yh2s, double Yn2, double Ppc, double Tpc, double t, double p) throws Exception {
        double tr, mg, r, dc0;
        double a, b, c, d, e, f, g1, g2, g3, g, j, h, pr;
        double Z, Cpr, mp0;
        double a0, a1, a2, a3, a4, a5, a6, a7, a8;
        double B0, B1, B2, B3, B4, B5, B6;
        double C0, C1, C2, C3, C4, C5, C6, C7, C8, C9, C10, C11;
        Integer m, n, nb, ng, nmg, ML;
        double Dzdtr, Batar, Dr = 0, mp;

        mp0 = 0; //原程序无初始化变量
        tr = t / Tpc;
        mg = 28.97 * rg;
        r = 8.314001E-03;
        dc0 = Ppc * mg / (0.27 * r * Tpc);
        a = 0.06423;
        b = 0.5353 * tr - 0.6123;
        c = 0.3151 * tr - 1.0467 - 0.5783 / Math.pow(tr, 2);
        d = tr;
        e = 0.6816 / Math.pow(tr, 2);
        f = 0.6845;
        g1 = Yh2s * (5.7 * rg - 1.7) * 0.00001;
        g2 = Yco2 * (5 * rg + 1.7) * 0.00001;
        g3 = Yn2 * (5 * rg + 4.7) * 0.00001;
        g = (9.4 + 0.02 * mg) * Math.pow(1.8 * t, 1.5);
        g = 0.0001 * g / (209 + 19 * mg + 1.8 * t) + g1 + g2 + g3;
        j = 1.7 - 197.2 / (1.8 * t) - 0.002 * mg;
        h = (3.5 + 986 / (1.8 * t) + 0.01 * mg) * Math.pow(0.001, j);
        if (Ident == 0) {
            pr = p / Ppc;
            //dprzf(Pr, tr, Dr, Z, Cpr);
            Map map = zfact(1, pr, tr);
            Dr = (double) map.get("Dr");
//            Z = (double) map.get("z");
//            Cpr = (double) map.get("Cpr");
//            Dzdtr = (double) map.get("Dzdtr");
//            Batar = (double) map.get("Batar");
        }
        a0 = 2 / (g * tr) * Math.pow(dc0 * r * t / mg, 2);
        a1 = 6 * a * Math.pow(Dr, 7) / 7;
        a2 = 3 * b * Math.pow(Dr, 4) / 4;
        a3 = 2 * c * Math.pow(Dr, 3) / 3;
        a4 = d * Math.pow(Dr, 2) / 2;
        a5 = 3 * e * Math.pow(Dr, 4) / 4;
        a6 = e * f * Math.pow(Dr, 6) / 2;
        a7 = -e * Math.pow(f, 2) * Math.pow(Dr, 8) / 4;
        a8 = dc0 * Dr;
        n = 1;
        ng = 1;
        C0 = 0;
        C1 = 0;
        C2 = 0;
        C3 = 0;
        C4 = 0;
        C5 = 0;
        C6 = 0;
        C7 = 0;
        C8 = 0;
        while (true) {
            B0 = n * j;
            ng = ng * n;
            B1 = Math.pow(a8, B0);
            B2 = Math.pow(-h, n) * B1 / ng;
            B3 = Math.pow(-f, n) * Math.pow(Dr, 2 * n) / ng;
            C0 = C0 + B2 / (B0 + 7);
            C1 = C1 + B2 / (B0 + 4);
            C2 = C2 + B2 / (B0 + 3);
            C3 = C3 + B2 / (B0 + 2);
            C4 = C4 + B2 / (B0 + 6);
            C5 = C5 + B2 / (B0 + 8);
            C6 = C6 + B3 / (2 * n + 4);
            C7 = C7 + B3 / (2 * n + 6);
            C8 = C8 + B3 / (2 * n + 8);
            C9 = 0;
            C10 = 0;
            C11 = 0;
            nmg = 1;
            for (nb = 1; nb <= n; nb++) {
                ML = 1;
                nmg = nb * nmg;
                B4 = nb * j;
                B5 = Math.pow(-h, nb) * Math.pow(a8, B4) / nmg;
                for (m = 1; m <= n; m++) {
                    ML = m * ML;
                    B6 = Math.pow(-f, m) * Math.pow(Dr, 2 * m) / ML;
                    C9 = C9 + B5 * B6 / (B4 + 2 * m + 4);
                    C10 = C10 + B5 * B6 / (B4 + 2 * m + 6);
                    C11 = C11 + B5 * B6 / (B4 + 2 * m + 8);
                }
            }
            mp = a1 * (1 + 7 * C0) + a2 * (1 + 4 * C1) + a3 * (1 + 3 * C2);
            mp = mp + a4 * (1 + 2 * C3) + a5 * (1 + 4 * (C1 + C9 + C6));
            mp = mp + a6 * (1 + 6 * (C4 + C10 + C7)) + a7 * (1 + 8 * (C5 + C11 + C8));
            mp = a0 * mp;
            if ((n != 1) && (Math.abs(mp - mp0) < 1)) {
                break;
            }
            mp0 = mp;
            n = n + 1;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Dr", Dr);
        map.put("mp", mp);
        return map;
    }


    public static Map zfact(Integer Meth, double pr, double Tr) throws Exception {
        Integer Prlimit;
        double Dzdr = 0, tr2, tr3, a, b, c, d, e, f, Fp, G, ed, exa;
        Integer Niter;
        Integer Iter;
        double ff, fdr, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11;

        double Dr = 0, z = 0, Cpr, Dzdtr, Batar;//输出参数

        Integer Result = -1;
        Prlimit = 30;
        Niter = 20;
        if ((pr <= 0) || (pr > Prlimit)) {
            Result = -1;
            logger.info("pr错误");
            throw new Exception("pr错误");
        }
        switch (Meth) {
            case 1:
                tr2 = Math.pow(Tr, 2);
                tr3 = Math.pow(Tr, 3);
                a = -14.76 / Tr + 9.76 / tr2 - 4.58 / tr3;
                b = 90.7 / Tr - 242.2 / tr2 + 42.4 / tr3;
                c = 1.18 + 2.82 / Tr;
                d = 0.06125 * pr * Math.exp(-1.2 * Math.pow(1 - 1 / Tr, 2)) / Tr;
                Dr = d;
                Iter = 1;
                while (true) {
                    Iter = Iter + 1;
                    f = (1 + Dr + Math.pow(Dr, 2) - Math.pow(Dr, 3)) / Math.pow(1 - Dr, 3);
                    f = f + a * Dr + b * Math.pow(Dr, c) - d / Dr;
                    Fp = (4 + 4 * Dr - 2 * Math.pow(Dr, 2)) / Math.pow(1 - Dr, 4);
                    Fp = Fp + a + b * c * Math.pow(Dr, c - 1) + d / Math.pow(Dr, 2);
                    Dr = Dr - f / Fp;
                    if (Dr <= 0) {
                        Result = -1;
                        logger.info("Dr错误");
                        throw new Exception("Dr错误");
                    }
                    if ((Math.abs(f / Fp) < 0.00001) || (Iter > Niter)) {
                        break;
                    }
                }
                z = d / Dr;
                Dzdr = (4 + 4 * Dr - 2 * Math.pow(Dr, 2)) / Math.pow(1 - Dr, 4) + a + b * c * Math.pow(Dr, c - 1);
                break;
            case 2:
                a = 0.06423;
                b = 0.5353 * Tr - 0.6123;
                c = 0.3151 * Tr - 1.0467 - 0.5783 / Math.pow(Tr, 2);
                d = Tr;
                e = 0.6816 / Math.pow(Tr, 2);
                f = 0.6845;
                Dr = 0.27 * pr / Tr;
                Iter = 1;
                while (true) {
                    Iter = Iter + 1;
                    exa = Math.exp(-f * Math.pow(Dr, 2));
                    ff = a * Math.pow(Dr, 6) + b * Math.pow(Dr, 3) + c * Math.pow(Dr, 2) + d * Dr;
                    ff = ff + e * Math.pow(Dr, 3) * (1 + f * Math.pow(Dr, 2)) * exa - 0.27 * pr;
                    Fp = 3 + f * Math.pow(Dr, 2) * (3 - 2 * f * Math.pow(Dr, 2));
                    Fp = e * Math.pow(Dr, 2) * Fp * exa + 6 * a * Math.pow(Dr, 5);
                    Fp = Fp + 3 * b * Math.pow(Dr, 2) + 2 * c * Dr + d;
                    Dr = Dr - ff / Fp;
                    if (Dr <= 0) {
                        Result = -1;
                        logger.info("Dr错误");
                        throw new Exception("Dr错误");
                    }
                    if ((Math.abs(ff / Fp) < 0.00001) || (Iter > Niter)) {
                        break;
                    }
                }
                z = 0.27 * pr / (Dr * Tr);
                fdr = f * Math.pow(Dr, 2);
                Dzdr = 2 * e * Dr * (1 + fdr - Math.pow(fdr, 2)) * Math.exp(-fdr);
                Dzdr = (5 * a * Math.pow(Dr, 4) + 2 * b * Dr + c + Dzdr) / Tr;
                break;
            case 3:
                a1 = 0.3265;
                a2 = -1.07;
                a3 = -0.5339;
                a4 = 0.01569;
                a5 = -0.05165;
                a6 = 0.5475;
                a7 = -0.7361;
                a8 = 0.1844;
                a9 = 0.1056;
                a10 = 0.6134;
                a11 = 0.721;
                a = a1 + a2 / Tr + a3 / Math.pow(Tr, 3) + a4 / Math.pow(Tr, 4) + a5 / Math.pow(Tr, 5);
                b = a6 + a7 / Tr + a8 / Math.pow(Tr, 2);
                c = -a9 * (a7 / Tr + a8 / Math.pow(Tr, 2));
                d = a10 / Math.pow(Tr, 3);
                e = a11;
                G = -0.27 * pr / Tr;
                Dr = -G;
                Iter = 1;
                while (true) {
                    Iter = Iter + 1;
                    ed = e * Math.pow(Dr, 2);
                    exa = Math.exp(-ed);
                    f = G / Dr + 1 + a * Dr + b * Math.pow(Dr, 2) + c * Math.pow(Dr, 5);
                    f = f + d * Math.pow(Dr, 2) * (1 + e * Math.pow(Dr, 2)) * exa;
                    Fp = -G / Math.pow(Dr, 2) + a + 2 * b * Dr + 5 * c * Math.pow(Dr, 4);
                    Fp = Fp + 2 * d * Dr * (1 + ed - Math.pow(ed, 2)) * exa;
                    Dr = Dr - f / Fp;
                    if (Dr <= 0) {
                        Result = -1;
                        logger.info("Dr错误");
                        throw new Exception("Dr错误");
                    }
                    if ((Math.abs(f / Fp) < 0.00001) || (Iter > Niter)) {
                        break;
                    }
                }
                z = 0.27 * pr / (Tr * Dr);
                ed = e * Math.pow(Dr, 2);
                exa = Math.exp(-ed);
                Dzdr = a + 2 * b * Dr + 5 * c * Math.pow(Dr, 4);
                Dzdr = Dzdr + 2 * d * Dr * (1 + ed - Math.pow(ed, 2)) * exa;
                break;
        } //case
        Cpr = 1 / pr - Dr / (z * pr) * Dzdr / (1 + Dr * Dzdr / z);
        Dzdtr = -Dr / Tr * Dzdr / (1 + Dr * Dzdr / z);
        Batar = 1 / Tr + Dzdtr / z;
        Result = 0;

        Map<String, Object> map = new HashMap<>();
        map.put("Dr", Dr);
        map.put("z", z);
        map.put("Cpr", Cpr);
        map.put("Dzdtr", Dzdtr);
        map.put("Batar", Batar);
        map.put("Result", Result);
        return map;

    }
    public static double ErXiangshiD(double a,double  b, double Pwf,double  Qsc){
        double pr;

        Qsc = Qsc / 10000;
        pr = Math.sqrt(Math.pow(Pwf, 2) + a * Qsc + b * Math.pow(Qsc, 2));
        return  pr;
    }
    public static double ErXiangshiD_NIYALI(double a,double  b, double Pwf,double  Qsc){
        double pr;

        Qsc = Qsc / 10000;
        pr = (Pwf + a * Qsc + b * Math.pow(Qsc, 2));
        return pr;
    }

    public static double ZhishuShiD(double C,double  N,double  Pwf,double  Qsc){
        double pr ;

        Qsc = Qsc / 10000;
        pr = Math.sqrt(Math.pow(Qsc / C, 1 / N) + Math.pow(Pwf, 2));
        return pr;
    }
    public static double GetAof(Integer SelectGS, double A, double B, double Ae, double Be, double C, double N, double Pr, GasData gasData) throws Exception {
        double Aof;

        boolean Result = false;
        if (Pr <= 0) {
//            ShowMsg('当前气藏压力不正确,请检查基本数据!');
            throw new Exception("当前气藏压力不正确,请检查基本数据");
        }
        Aof = 0;
        switch (SelectGS) {  //指数式运算
            case 0:
                if ((C < 0) || (N < 0)) {
                    logger.info("数据没有输入完整！");
                    throw new Exception("数据没有输入完整");
                }
                Aof = ZhishuShi(C, N, Pr, 0);
                break;
            case 1: //二项式运算
                if ((A < 0) || (B < 0)) {
                    throw new Exception("数据没有输入完整");
                }
                Aof = ErXiangshi(A, B, Pr, 0);
                break;
            case 2: //拟压力二项式运算
                if ((Ae < 0) || (Be < 0)) {
                    logger.info("数据没有输入完整！");
                    throw new Exception("数据没有输入完整");
                }

                Aof = ErXiangshi_NiYaLi(Ae, Be, Get_NiYaLi(gasData, Pr), 0);
                break;
        }
        Result = true;
        Map<String, Object> map = new HashMap<>();
        map.put("Result", Result);
        map.put("Aof", Aof);
        return Aof;
    }

    public static double Get_NiYaLi(GasData gasData, double P_SR) {
        double Result;
        double Rg = gasData.getRg();//气体相对密度
        double ppc = gasData.getPpc(); //拟临界压力Mpa
        double tpc = gasData.getTpc(); //拟临界温度K
        double t = gasData.getQCWD() + P_Ntemp; //气藏温度
        double pri = gasData.getYSQCYL(); //原始气藏压力

        boolean Res = (boolean) MpresNew(pri, t, tpc, ppc, Rg, P_SR, gasData).get("Result");
        if (Res == true) {
            Result = (double) MpresNew(pri, t, tpc, ppc, Rg, P_SR, gasData).get("P_NYL");
        } else {
            Result = 0;
        }
        return Result;
    }

    public static Map MpresNew(double Pri, double T, double Tpc, double Ppc, double Rg, double P_SR, GasData gasData) {
        Integer Num;
        double Succes;
        double A, B, e4, k4, h0;
        double mpp, Z2;
        Integer Meth, Zmeth, Vmeth, n;
        double[] y;
//        mw, tc, pc,:ArrayDouble;
        double W, Zc, P_NYL = 0;
        boolean Result;

        try {
            Result = false;
            Meth = 2; //拟压力模型
            Zmeth = 1; //偏差系数模型
            Vmeth = 1; //粘度模型
            y = GetArrayY(gasData);
//            StoreValue(tc, pc, w, mw, Zc);//这行代码，因为switch的原因，永远用不到=-=
            switch (Meth) {
                case 1:
//                    A = 0;
//                    B = A + P_SR;
//                    mpp = 0;
//                    e4 = 1;
//                    k4 = 10;
//                    h0 = 2;
//                    Succes = pseudsimp(A, B, e4, k4, h0, Z2, Zmeth, Vmeth, n, mw, tc, pc, y, Rg, Tpc, Ppc, T);
//                    if Succes = -1 then
//                        begin
//                    Exit;
//                    end;
//                    mpp = mpp + Z2;
//                    P_NYL = mpp;
                    break;
                case 2:
                    B = P_SR;
                    Map map = pseudsem(Rg, y[6], y[5], y[7], Ppc, Tpc, T, B);
                    Succes = (double) map.get("pseudsem");
                    if (Succes == -1) {
                        logger.info("Succes = -1!!!");
                        throw new Exception("Succes = -1");
                    }
                    mpp = (double) map.get("mp");
                    P_NYL = mpp;

            }
            Result = true;
        } catch (Exception e) {
            Result = false;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("P_NYL", P_NYL);
        map.put("Result", Result);
        return map;
    }

    public static Map pseudsem(double Rg, double Yco2, double Yh2s, double Yn2, double Ppc, double Tpc, double T, double P) {
        double Pr, Tr, mg, R, dc0, A, B;
        double C, D, e, F, g1, g2, g3, j;
        double Dr = 0, exa, ff = 0, Fp = 0, Z, H;
        double a0, a1, a2, A3, A4, A5, a6, a7, a8;
        Integer Ident, m, n, ng, nmg, Nb;
        double m1, mp0 = 0, G, C0, C1, C2, C3, C4, C5;
        double C6, C7, C8, C9, C10, C11;
        double B0, b1, b2, B3, B4, B5, B6, pseudsem, mp;
        Map<String, Object> map = new HashMap<>();

        if (P == 0) {
            P = 0.01;
        }
        Pr = P / Ppc;
        Tr = T / Tpc;
        if (Pr > 30) {
            pseudsem = -1;
            map.put("pseudsem", pseudsem);
            return map;
        }
        mg = 28.97 * Rg;
        R = 0.008314001;
        dc0 = Ppc * mg / (0.27 * R * Tpc);
        A = 0.06423;
        B = 0.5353 * Tr - 0.6123;
        C = 0.3151 * Tr - 1.0467 - 0.5783 / (Tr * Tr);
        D = Tr;
        e = 0.6816 / (Tr * Tr);
        F = 0.6845;
        g1 = Yh2s * (5.7 * Rg - 1.7) * 0.001;
        g2 = Yco2 * (5 * Rg + 1.7) * 0.001;
        g3 = Yn2 * (5 * Rg + 4.7) * 0.001;
        G = (9.399999 + 0.02 * mg) * Math.pow((1.8 * T), 1.5);
        G = 0.0001 * G / (209 + 19 * mg + 1.8 * T) + g1 + g2 + g3;
        j = 1.7 - 197.2 / (1.8 * T) - 0.002 * mg;
        H = (3.5 + 986 / (1.8 * T) + 0.01 * mg) * Math.pow((0.001), j);
        Ident = 0;
        if (Ident == 0) {
            Dr = 0.27 * Pr / Tr;
            while (Math.abs(ff / Fp) < 0.00001) {
                exa = Math.exp(-F * Dr * Dr);
                ff = A * Math.pow(Dr, 6) + B * Math.pow(Dr, 3) + C * Dr * Dr + D * Dr;
                ff = ff + e * Math.pow(Dr, 3) * (1 + F * Dr * Dr) * exa - 0.27 * Pr;
                Fp = 3 + F * Dr * Dr * (3 - 2 * F * Dr * Dr);
                Fp = e * Dr * Dr * Fp * exa + 6 * A * Math.pow(Dr, 5);
                Fp = Fp + 3 * B * Dr * Dr + 2 * C * Dr + D;
                Dr = Dr - ff / Fp;
            }
            Z = 0.27 * Pr / (Dr * Tr);
        }
        a0 = 2 / (G * Tr) * Math.pow((dc0 * R * T / mg), 2);
        a1 = 6 * A * Math.pow(Dr, 7) / 7;
        a2 = 3 * B * Math.pow(Dr, 4) / 4;
        A3 = 2 * C * Math.pow(Dr, 3) / 3;
        A4 = D * Dr * Dr / 2;
        A5 = 3 * e * Math.pow(Dr, 4) / 4;
        a6 = e * F * Math.pow(Dr, 6) / 2;
        a7 = -e * F * F * Math.pow(Dr, 8) / 4;
        a8 = dc0 * Dr;
        n = 1;
        ng = 1;
        C0 = 0;
        C1 = 0;
        C2 = 0;
        C3 = 0;
        C4 = 0;
        C5 = 0;
        C6 = 0;
        C7 = 0;
        C8 = 0;
        while (true) {
            B0 = n * j;
            ng = ng * n;
            b1 = Math.pow(a8, B0);
            b2 = Math.pow((-H), n) * b1 / ng;
            B3 = Math.pow((-F), n) * Math.pow(Dr, (2 * n)) / ng;
            C0 = C0 + b2 / (B0 + 7);
            C1 = C1 + b2 / (B0 + 4);
            C2 = C2 + b2 / (B0 + 3);
            C3 = C3 + b2 / (B0 + 2);
            C4 = C4 + b2 / (B0 + 6);
            C5 = C5 + b2 / (B0 + 8);
            C6 = C6 + B3 / (2 * n + 4);
            C7 = C7 + B3 / (2 * n + 6);
            C8 = C8 + B3 / (2 * n + 8);
            C9 = 0;
            C10 = 0;
            C11 = 0;
            nmg = 1;
            for (Nb = 1; Nb <= n; Nb++) {
                m1 = 1;
                nmg = Nb * nmg;
                B4 = Nb * j;
                B5 = Math.pow((-H), Nb) * Math.pow(a8, B4) / nmg;
                for (m = 1; m <= n; m++) {
                    m1 = m * m1;
                    B6 = Math.pow((-F), m) * Math.pow(Dr, (2 * m)) / m1;
                    C9 = C9 + B5 * B6 / (B4 + 2 * m + 4);
                    C10 = C10 + B5 * B6 / (B4 + 2 * m + 6);
                    C11 = C11 + B5 * B6 / (B4 + 2 * m + 8);
                }
            }
            mp = a1 * (1 + 7 * C0) + a2 * (1 + 4 * C1) + A3 * (1 + 3 * C2);
            mp = mp + A4 * (1 + 2 * C3) + A5 * (1 + 4 * (C1 + C9 + C6));
            mp = mp + a6 * (1 + 6 * (C4 + C10 + C7)) + a7 * (1 + 8 * (C5 + C11 + C8));
            mp = a0 * mp;
            if (n != 1) {
                if (Math.abs(mp - mp0) < 1) {
                    break;
                }
            }
            mp0 = mp;
            n = n + 1;
        }
        pseudsem = 0;
        map.put("pseudsem", pseudsem);
        map.put("mp", mp);
        return map;
    }


    public static double[] GetArrayY(GasData gasData) {
        double[] y = new double[28];
        y[1] = gasData.getAr() / 100;
        y[2] = gasData.getHe() / 100;
        y[3] = gasData.getH2() / 100;
        y[4] = gasData.getH2O() / 100;
        y[5] = gasData.getH2S() / 100;
        y[6] = gasData.getCO2() / 100;
        y[7] = gasData.getN2() / 100;
        y[8] = gasData.getC0() / 100;
        y[9] = gasData.getC1() / 100;
        y[10] = gasData.getC2() / 100;
        y[11] = gasData.getC3() / 100;
        y[12] = gasData.getiC4() / 100;
        y[13] = gasData.getnC4() / 100;
        y[14] = gasData.getiC5() / 100;
        y[15] = gasData.getnC5() / 100;
        y[16] = gasData.getiC6() / 100;
        y[17] = gasData.getnC6() / 100;
        y[18] = gasData.getC7() / 100;
        y[19] = gasData.getC8() / 100;
        y[20] = gasData.getC9() / 100;
        y[21] = gasData.getC10() / 100;
        y[22] = gasData.getC11() / 100;
        y[23] = gasData.getC12() / 100;
        y[24] = gasData.getC13() / 100;
        y[25] = gasData.getC14() / 100;
        y[26] = gasData.getC15() / 100;
        y[27] = gasData.getC16() / 100;

        return y;
    }

    public static double ZhishuShi(double C, double N, double Pr, double pwf) {
        double Aof;
        if (pwf == 0) {
            Aof = C * Math.pow(Math.pow(Pr, 2), N);
        } else {
            if (pwf > Pr) {
                pwf = Pr;
            }
            Aof = C * Math.pow(Math.pow(Pr, 2) - Math.pow(pwf, 2), N);
        }
        return Aof * 10000;

    }

    public static double ErXiangshi(double a, double b, double Pr, double pwf) {
        double Aof;
        if ((a * a + 4 * b * Pr * Pr) < 0) {
            Aof = 0;
        } else {
            Aof = (-a + Math.pow(a * a + 4 * b * Pr * Pr, 0.5)) / (2 * b);
        }
        return Aof * 10000;
    }

    public static double ErXiangshi_NiYaLi(double a, double b, double Pr, double pwf) {
        double Aof;
        if ((a * a + 4 * b * Pr) < 0) {
            Aof = 0;
        } else {
            Aof = (-a + Math.pow(a * a + 4 * b * Pr, 0.5)) / (2 * b);
        }
        return Aof * 10000;
    }

    /**
     * 给产量求Pwf
     *
     * @param C
     * @param N
     * @param pr
     * @param Qsc
     * @return
     */
    public static double ZhishuShiC(double C, double N, double pr, double Qsc) {
        double Pwf;
        Qsc = Qsc / 10000;
        double result = 0;

        if (Math.pow(pr, 2) > Math.pow(Qsc / C, 1 / N)) {
            Pwf = Math.sqrt(Math.pow(pr, 2) - Math.pow(Qsc / C, 1 / N));
        } else {
            Pwf = 0;
        }
        result = Pwf;
        return result;
    }

    /**
     * 给产量求Pwf
     *
     * @param a
     * @param b
     * @param pr
     * @param Qsc
     * @return
     */
    public static double ErXiangshiC(double a, double b, double pr, double Qsc) {
        double Pwf = 0;
        Qsc = Qsc / 10000;
        double Result = 0;

        if (Math.pow(pr, 2) > (a * Qsc + b * Qsc * Qsc)) {
            Pwf = Math.sqrt(Math.pow(pr, 2) - (a * Qsc + b * Qsc * Qsc));
        } else {
            Pwf = 0;
        }
        Result = Pwf;
        return Result;
    }

    public static double ErXiangshiC_NiYaLi(double a, double b, double pr, double Qsc) {
        double Pwf = 0;
        Qsc = Qsc / 10000;
        double Result = 0;
        if (pr > (a * Qsc + b * Qsc * Qsc)) {
            Pwf = (pr - (a * Qsc + b * Qsc * Qsc));
        } else {
            Pwf = 0;
        }
        Result = Pwf;
        return Result;
    }

    /**
     * 直线的方程为形式：y=k*x+b ,x=c;
     * X,y为需要处理的数据
     *
     * @param x
     * @param y
     * @param len
     * @return
     */
    public static Map FitLine(double[] x, double[] y, Integer len) throws Exception {
        Integer i;
        double sx, sy, s2x, sxy;
        double k = 0, b = 0, c;

        if (len < 2) { //少于两点无法拟合
            throw new Exception("少于两点无法拟合");
        }
        sx = 0;
        sy = 0;
        s2x = 0;
        sxy = 0;
        for (i = 0; i < len; i++) {
            sx = sx + x[i];
            sy = sy + y[i];
            s2x = s2x + x[i] * x[i];
            sxy = sxy + y[i] * x[i];
        }
        if (sx * sx - len * s2x == 0) {
            c = x[0];
        } else {
//            c = infinity;//无穷大
            k = (sy * sx - sxy * len) / (sx * sx - len * s2x);
            b = (sy - k * sx) / len;
        }

        Map<String, Double> map = new HashMap<>();
        map.put("k", k);
        map.put("b", b);

        return map;
    }

    public static double GetJKWD(GasData gasData, double H) throws Exception {
        double Result, Ta, tw, Wm;

        //注意：下面没有采用K为温度单位
        Ta = gasData.getDwtd();
        if (Ta == 0) {
            throw new Exception("基本数据中的地温梯度不能为零！");
        }
        tw = gasData.getQCWD(); //井底温度
        if (tw <= 0) {
            throw new Exception("基本数据中的气藏温度必须大于零！");
        }
        Wm = 0.0034 + 0.79 * Ta / 100; //流温梯度
        Result = tw - H * Wm;
        Result = Result + P_Ntemp;
        return Result;
    }

    /**
     * //flow=1 ,计算采气井压力；  =-1，计算注气井压力；
     * //JiaoDu向上流动为正，向下流动为负
     * //Cal计算方向
     * //SepExist:有无分离器 1 有分离器　　0　无分离器
     * //yqb,sqb:方/万方
     * //DMFlag=1 算地面管线   0 井筒  主要是为了区分多相流计算中的管径不同
     * //这里传进的Qsc是(方)为单位      传进来管径的单位都是mm
     *
     * @param request
     */
    public static Map QJJTYL(QJJTYLRequest request,GasData gasData) throws Exception {
        Integer DMFlag = request.getDMFlag();
        Integer cal = request.getCal();
        Integer flow = request.getFlow();
        Integer SepExist = request.getSepExist();
        Integer JiaoDu = request.getJiaoDu();
        Integer YLMethod = request.getYLMethod();
        Integer DXLMethod = request.getDXLMethod();
        List<RecTube> recTubes = request.getRecTubes();

        double Wm = request.getWm();
        double YQB = request.getYqb();
        double SQB = request.getSqb();
        double hm = request.getHm();
        double p = request.getP();
        double rg = request.getRg();
        double Ro = request.getRo();
        double Rw = request.getRw();
        double yco2 = request.getYco2();
        double yn2 = request.getYn2();
        double yh2s = request.getYh2s();
        double ppc = request.getPpc();
        double tpc = request.getTpc();
        double Tt = request.getTt();
        double tw = request.getTw();
        double qsc = request.getQsc();
        double d2 = request.getD2();
        double d1 = request.getD1();
        double e = request.getE();
        double l = request.getL();
        double H = request.getH();

        double p1 = 0, t1=0, z1=0;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> returnMap = new HashMap<>();
        double tstart, tend;
        double ynacl, qw, qo;

        if (cal == 1) {
            tstart = Tt;
            tend = tw;
        } else {
            tstart = tw;
            tend = Tt;
        }
        //ynacl = 0; qw = SQB * qsc / 10000; qo = YQB * qsc / 10000;
        switch (YLMethod) {
            case 0:
                map = csmith_Point(cal, flow, SepExist, SQB, YQB, hm, p, rg, yco2, yn2, yh2s, ppc, tpc, Tt, tw, qsc, d2 / 1000, d1 / 1000, e, l, H);
                p1 = (double) map.get("P1");
                t1 = (double) map.get("T1");
                z1 = (double) map.get("Z");
                logger.info("P1=" + p1 + ",  T1=" + t1 + ",  Z=" + z1);
                break;
            case 1:
                map = psPres_Point(cal, flow, SepExist, SQB, YQB, hm, p, Ro, Rw, rg, yco2, yn2, yh2s, ppc, tpc, Tt, tw, Ro, Rw, qsc, d2 / 1000, d1 / 1000, e, l, H);

                p1 = (double) map.get("p1");
                t1 = (double) map.get("t1");
                z1 = (double) map.get("z1");
                logger.info("P1=" + p1 + ",  T1=" + t1 + ",  Z=" + z1);
                break;
            case 2:
                if (cal == 1) {
                    l = 0;
                } else {
                    l = H;
                }
                map =DXLDanDian(recTubes, DMFlag, DXLMethod, cal,gasData.getSCFS(),gasData.getYLJZXS(), SQB, YQB, rg, Ro, Rw, Wm, H, qsc / 10000, p, tstart, l,
                        gasData.getTpc(),gasData.getPpc(), d2, d1);
                p1 = (double) map.get("p1");
                t1 = (double) map.get("t1");
                z1 = (double) map.get("z1");
                logger.info("P1=" + p1 + ",  T1=" + t1 + ",  Z=" + z1);
                break;
            //井深度 H     l开始位置
        }
        returnMap.put("p1",p1 );
        returnMap.put("t1", t1);
        returnMap.put("z1", z1);
        return returnMap;

    }

    /**
     * //利用多相流计算得到目标位置的压力温度和Z
     *
     * @param recTubes
     * @param DMFlag
     * @param Method
     * @param Fx
     * @param SCFS
     * @param PressJZ
     * @param WGR
     * @param OGR
     * @param Rg
     * @param Ro
     * @param Rw
     * @param Wm
     * @param H
     * @param Qg
     * @param Pstart
     * @param TStart
     * @param HStart
     * @param Tpc
     * @param Ppc
     * @param d2
     * @param d1
     * @return
     */
    public static Map DXLDanDian(List<RecTube> recTubes, Integer DMFlag, Integer Method, Integer Fx, Integer SCFS,
                                 double PressJZ, double WGR, double OGR, double Rg, double Ro, double Rw,
                                 double Wm, double H, double Qg, double Pstart, double TStart, double HStart,
                                 double Tpc, double Ppc, double d2, double d1) {
        double p1, t1, z1;
        double pP1, TT1, HH1;
        double pP2, TT2, HH2, Deh1 = 0;
        String Flow, JieShu;
        double HL, Z;
        double PL, Pg, Pm, Vsl, Vsg;
        double Angle; //角度
        //SCFS:integer;     油管生产=0       套管生产=1
        double Dt; //油管生产时为油管内径 Dt=Dti，套管生产时为套管内径Dt=sqrt(sqr(dci)-sqr(dto)) 当量油管尺寸，mm
        double e = 0; //油管粗糙度
        Integer J;
        Integer DeltH;
        // Fx:integer;//Fx-方向 1－－向下 －1－－向上


        Integer Result = 0;
        DeltH = 50;
        pP1 = Pstart; // 开始压力
        HH1 = HStart;
        TT1 = TStart; //井口温度
        Angle = pi / 2;
        J = 0;
        while (true) {
            //这一地方主要为初始点计算HL等参数
            if (Fx == 1) {
                if ((HH1 == 0) && (J == 0)) {
                    Deh1 = 0;
                } else {
                    Deh1 = DeltH;
                }
            }
            if (Fx == -1) {
                if ((HH1 == H) && (J == 0)) {
                    Deh1 = 0;
                } else {
                    Deh1 = DeltH;
                }

            }
            if ((Fx == 1) && (HH1 + Deh1 > H)) {
                Deh1 = H - HH1;
            }
            if (DMFlag == 1) {
                //地面管线时
                Dt = d2 - d1;
                Angle = 0;
            } else {
                //井筒生产时
                //if Fx = 1 then //从上往下时
                Dt = (double) GetDt(recTubes, HH1, SCFS).get("result"); //当量油管尺寸，mm
                e=(double) GetDt(recTubes, HH1, SCFS).get("e");
                //else
                //  Dt = GetDt(RecTube, H - HH1, SCFS, e); //当量油管尺寸，mm
            }
            JieShu = "";
            YDGasDxlRequest request=new YDGasDxlRequest(Method,Fx,Deh1, PressJZ, Dt, WGR, OGR, Rg, Ro, Rw, Wm, H, Qg, pP1, TT1, HH1, Angle, Tpc, Ppc, e);
            YDGasDxlResponse ydGasDxlResponse =YadanGasDxl.YDGasDxl(request);
            pP2 = ydGasDxlResponse.getP2();
            TT2 = ydGasDxlResponse.getT2();
            HH2 = ydGasDxlResponse.getH2();
            Vsl = ydGasDxlResponse.getVsl();
            Vsg = ydGasDxlResponse.getVsg();
            HL = ydGasDxlResponse.getHL();
            Z = ydGasDxlResponse.getZ();
            Flow = ydGasDxlResponse.getFlow();
            JieShu=ydGasDxlResponse.getJieShu();

//            YDGasDxl(Method, Fx, Deh1, PressJZ, Dt, WGR, OGR, Rg, Ro, Rw, Wm, H, Qg, pP1, TT1, HH1, Angle, Tpc, Ppc, e,
//                    pP2, TT2, HH2, PL, Pg, Pm, Vsl, Vsg, HL, Z, Flow, JieShu);
            if (((HH2 >= H) && (Fx == 1)) || ((HH2 - Deh1 <= 0) && (Fx == -1)) || ("结束".equals(JieShu))) {
                break;
            } else {
                pP1 = pP2;
                TT1 = TT2;
                HH1 = HH2;
                z1 = Z;
                J++;
            }
        }
        if ((Fx == -1) &&(HH2 - Deh1 <= 0) ){
            HH2 = 0;
        }
        //VDxlResult[length(VDxlResult) - 1, 0] = HH2; //井深
        p1 = pP2; //压力
        t1 = TT2; //温度
        z1 = Z;
        Map<String,Object> map=new HashMap<>();
        map.put("p1",p1);
        map.put("t1",t1);
        map.put("z1",z1);
        return  map;

    }


    /**
     * '用Cullender-Smith方法计算气井井筒压力        拟单相流的算法
     * '输入变量：
     * '           cal=1,p-井口压力，MPa[已知井口压力]
     * '              =-1 ，p-井底压力，MPa[已知井底压力]
     * '           flow=1 ,计算采气井压力；
     * '               =-1，计算注气井压力；
     * '           hm-指定的求压力和温度的位置 e-管粗糙度，m；
     * '           d1-油管外径，m；    d2-套管内径，m；
     * '           h-井深度，m；       l-井深长度，m；
     * '           tt-井口温度，K；    tw-井底温度，K；
     * '           rg-气体相对密度；   yn2-氮的摩尔分数；
     * '           yco2-二氧化碳的摩尔分数；
     * '           yh2s-硫化氢摩尔分数；
     * '           qsc-气体流量，m^3/d[qsc=0,对静气柱]；
     * '           P 计算的位置 如果从井底计算指从井底至
     * YQB,SQB:油气比，水气比：方/万方
     * '输出变量：
     * '           p1:hm点的压力
     * '           t1:hm点的温度
     *
     * @param cal
     * @param flow
     * @param Sep
     * @param YQB
     * @param SQB
     * @param hm
     * @param p
     * @param ro
     * @param rw
     * @param rg
     * @param yco2
     * @param yn2
     * @param yh2s
     * @param ppc
     * @param tpc
     * @param tt
     * @param tw
     * @param Qo
     * @param Qw
     * @param qsc
     * @param d2
     * @param d1
     * @param e
     * @param l
     * @param H
     * @return
     */
    public static Map psPres_Point(Integer cal, Integer flow, Integer Sep,
                                   double YQB, double SQB, double hm, double p, double ro, double rw, double rg, double yco2,
                                   double yn2, double yh2s, double ppc, double tpc, double tt, double tw, double Qo, double Qw,
                                   double qsc, double d2, double d1, double e, double l, double H) throws Exception {
        double d, rec, fqc, s, t, cs, cs0, cs1, ug, Re, f, fq, p0;
        Integer i;
        double pr, tr, dr, cr;
        double dl, dw, ml, mg, dsc, mgp, rmlg, rgp;
        double rvlg, rvwg, wg, ww, fw;
        double p1, t1, z1;

        if (qsc == 0) {
            qsc = 0.00001;
        }

        dl = 1000 * ro;
        dw = 1000 * rw;
        ml = 44.29 / (1.03 - ro);
        mg = 28.97 * rg;

        rvlg = YQB / 10000; //qo/qsc;
        rvwg = SQB / 10000; //qw/qsc;


        rmlg = 24.04 * rvlg * dl / ml;
        dsc = 1.205 * rg;
        mgp = mg * (1 + rmlg * ml / mg) / (1 + rmlg);
        rgp = mgp / 28.97;

        wg = qsc * dsc * (1 + rvlg * dl / dsc);
        ww = Qw * dw;
        fw = 1 + ww / wg;

        d = Math.pow(d2 - d1, 3) * Math.pow(d2 + d1, 2);
        rec = 0.1776 * qsc * rg * (1 + rvlg * dl / dsc) * fw / (d2 + d1);
        fqc = 7.651E-16 * Math.pow(wg / mgp, 2) * l / H;

        s = 0.03415 * rgp * hm;

        if (cal != 1) {
            hm = H - hm;
        } //从井底计算

        i = 0;

        p0 = p;
        cs = 0;
        cs0 = 0;
        fq = 0;

        tt = tt * (1 + qsc / 10000000);
        //有分离器用平均温度
        if (Sep == 1) {
            t = (tt + tw) / 2;
        } else {
            logger.error("没有GetPointT");
            throw new Exception("没有GetPointT");
//            t = GetPointT(tt, tw, h, hm, d2, Qsc, SQB, YQB);
        }

        while (true) {

            cs1 = cs;
            //(double) YadanGasDxl.QPCYZ_Z("Hall-Yarbough", ppc, tpc, p, t).get("Z");
            z1 = (double) YadanGasDxl.QPCYZ_Z("Hall-Yarbough", ppc, tpc, p, t).get("Z");

            if (qsc > 0) {
                ug = QND_UG(ppc, tpc, p, t, rg);
                Re = rec / ug;
                f = 2 * ln(e / (d2 - d1) + 21.25 / Math.pow(Re, 0.9)) / ln(10);
                f = Math.pow(1 / (1.14 - f), 2);
                fq = Sgn(flow) * fqc * f / d;
            }
            cs = p / (z1 * t) / (Math.pow(p / (t * z1), 2) + fq);
            if (i == 0) {
                cs0 = cs;
                cs1 = cs;
            }
            if (cs0 + cs != 0) {
                p = p0 + Sgn(cal) * 2 * s / (cs0 + cs);
            } else {
                p = p0;
            }
            if (i == 0) {
                break;
            }
            if (Math.abs(cs1 - cs) < 0.001) {
                break;
            }
        }
        p1 = p;
        if (p1 < 0) {
            p1 = 0;
        }
        t1 = t;
        Map<String, Object> map = new HashMap<>();
        map.put("t1", t1);
        map.put("z1", z1);
        map.put("p1", p1);
        return map;
    }

    /**
     * '用Cullender-Smith方法计算气井井筒压力
     * '输入变量：
     * '           cal=1,p-井口压力，MPa[已知井口压力]
     * '              =-1 ，p-井底压力，MPa[已知井底压力]
     * '           flow=1 ,计算采气井压力；
     * '               =-1，计算注气井压力；
     * '           hm-指定的求压力和温度的位置 e-管粗糙度，m；
     * '           d1-油管外径，m；    d2-套管内径，m；
     * '           h-井深度，m；       l-井深长度，m；
     * '           tt-井口温度，K；    tw-井底温度，K；
     * '           rg-气体相对密度；   yn2-氮的摩尔分数；
     * '           yco2-二氧化碳的摩尔分数；
     * '           yh2s-硫化氢摩尔分数；
     * '           qsc-气体流量，m^3/d[qsc=0,对静气柱]；
     * '           P 计算的位置 如果从井底计算指从井底至
     * '           sqb,yqb:水气比/油气比  方/万方
     * '输出变量：
     * '           p1:hm点的压力
     * '           t1:hm点的温度
     *
     * @param cal
     * @param flow
     * @param Sep
     * @param sqb
     * @param yqb
     * @param hm
     * @param p
     * @param rg
     * @param yco2
     * @param yn2
     * @param yh2s
     * @param ppc
     * @param tpc
     * @param tt
     * @param tw
     * @param qsc
     * @param d2
     * @param d1
     * @param e
     * @param l
     * @param H
     */
    public static Map<String, Object> csmith_Point(Integer cal, Integer flow, Integer Sep, double sqb, double yqb, double hm,
                                                   double p, double rg, double yco2, double yn2, double yh2s, double ppc, double tpc, double tt, double tw,
                                                   double qsc, double d2, double d1, double e, double l, double H) throws Exception {
        double d, rec, fqc, s, t, cs, cs0, cs1, ug, Re, f, fq, p0;
        Integer i;
        double pr, tr, dr, cr, Z, P1, T1;


        d = Math.pow(d2 - d1, 3) * Math.pow(d2 + d1, 2); //油管内径
        rec = 0.1776 * qsc * rg / (d2 + d1);
        fqc = 1.324E-18 * Math.pow(qsc, 2) * l / H;
        s = 0.03415 * rg * hm;
        if (cal != 1) {
            hm = H - hm;
        } //从井底计算
        i = 0;
        p0 = p;
        cs = 0;
        cs0 = 0;
        fq = 0;
        tt = tt * (1 + qsc / 10000000);
        //分离器存在用平均温度
        if (Sep == 1) {
            t = (tt + tw) / 2;
        } else {
//            t = GetPointT(tt, tw, h, hm, d2, qsc, sqb, yqb);
            throw new Exception("GetPointT错误");
        }
        while (true) {
            cs1 = cs;
            Z = (double) YadanGasDxl.QPCYZ_Z("Hall-Yarbough", ppc, tpc, p, t).get("Z");
            if (qsc > 0) {
                ug = QND_UG(ppc, tpc, p, t, rg);
                Re = rec / ug;
                f = 2 * ln(e / (d2 - d1) + 21.25 / Math.pow(Re, 0.9)) / ln(10);
                f = Math.pow(1 / (1.14 - f), 2);
                fq = Sgn(flow) * fqc * f / d;
            }
            cs = p / (Z * t) / (Math.pow(p / (t * Z), 2) + fq);
            if (i == 0) {
                cs0 = cs;
                cs1 = cs;
            }
            if (cs0 + cs != 0) {
                p = p0 + Sgn(cal) * 2 * s / (cs0 + cs);
            } else {
                p = p0;
            }
            if (i == 0) {
                break;
            }
            if ((Math.abs(cs1 - cs) < 0.001)) {
                break;
            }
        }
        P1 = p;
        if (P1 < 0) {
            P1 = 0;
        }
        T1 = t;
        Map<String, Object> map = new HashMap<>();
        map.put("P1", P1);
        map.put("Z", Z);
        map.put("T1", T1);
        return map;
    }

    public static double ln(double x) {
        return Math.log1p(x - 1);
    }

    public static double QND_UG(double ppc, double tpc, double P, double T, double rg) {

        double UG;
        UG = 0;
        UG = YadanGasDxl.Nd_Dempsey(rg, T, ppc, tpc, P);
        return UG;
    }

    public static Integer Sgn(double value) {
        if (value > 0) {
            return +1;
        } else if (value < 0) {
            return -1;
        }
        logger.error("Sgn出错");
        return 1;
    }

}