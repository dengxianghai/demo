package com.caiyou.entity;

/**
 * 气井的基本数据
 */
public class GasData {

    //地质数据
    private double YSQCYL;//原始气藏压力
    private double QCWD;//气藏温度  注意要用攝氏度
    private double QCZS;//气藏中深
    private double Dwtd;//地温梯度
    private Integer SCFS;//生产方式 ，是否油管生产：0 油管生产  1油套管环空生产
    //流体数据
    private double Tpc;//拟临界温度
    private double Ppc;//拟临界压力
    private double Rg;//气体相对密度
    private double Ro;//油的密度
    private double Rw;//水的密度
    private double yco2;//二氧化碳摩尔分数
    private double yh2s;//硫化氢摩尔分数
    private double Yn2;//氮的摩尔分数
    private double JRZS;//绝热指数
    //压力模型
    private int YLMethod;//压力计算方法 TODO：枚举值
    private int DxlMethod;//多相流计算方法,  Hagedorn-Brown=0、Beggs-Brill=1、Duns-Ros=2、Okiszewski=3、Mukherjee-Brill=4
    private double YLJZXS;//压力校正系数

    //试井数据
    //积液产产能数据
    private int CNMethod;//产能计算模型，0指数式，1压力平方，2拟压力平方
    private double Jw;//积液指数
    private double Pstart;//启动压差
    private double A;
    private double B;
    private double Ae;
    private double Be;
    private double C;
    private double N;
    private double CurrentPr;//积液前气藏压力
    //积液后产能数据
    private int CNMethod_b;//产能计算模型
    private double Jw_b;
    private double Pstart_b;
    private double A_b;
    private double B_b;
    private double Ae_b;
    private double Be_b;
    private double C_b;
    private double N_b;
    private double JiYePr;//积液后气藏压力

    //地面管线数据
    private double DM_DMGXCD;//管线长度
    private double DM_DMGXNJ;//管线内径Dop(mm)
    private double DM_SPQJ;//水平倾角ap(度)：
    private double DM_DMGXWJ;//管线外径(mm)：
    private double DM_GXFDCD;//管线分段每段长度
    private double DM_CCD;//粗糙度
    private double DM_DMHJWD;//地面环境温度
    private double DM_QZZJ; //气嘴直径
    private double DM_QZRKWD; //气嘴入口温度
    private double DM_JKDQZGC;//井口到气嘴的管长
    private double DM_FLQYL;//分离器压力
    private double DM_FLQWD;//分离器温度

    private double Ar; //气体摩尔Ar
    private double He; //气体摩尔He
    private double H2; //气体摩尔H2
    private double H2O; //气体摩尔H2O
    private double H2S; //气体摩尔H2S
    private double CO2; //气体摩尔CO2
    private double N2; //气体摩尔N2
    private double C0; //气体摩尔C0
    private double C1; //气体摩尔C1
    private double C2; //气体摩尔C2
    private double C3; //气体摩尔C3
    private double iC4; //气体摩尔iC4
    private double nC4; //气体摩尔nC4
    private double iC5; //气体摩尔iC5
    private double nC5; //气体摩尔nC5
    private double iC6; //气体摩尔iC6
    private double nC6; //气体摩尔nC6
    private double C7; //气体摩尔C7
    private double C8; //气体摩尔C8
    private double C9; //气体摩尔C9
    private double C10; //气体摩尔C10
    private double C11; //气体摩尔C11
    private double C12; //气体摩尔C12
    private double C13; //气体摩尔C13
    private double C14; //气体摩尔C14
    private double C15; //气体摩尔C15
    private double C16; //气体摩尔C16


    public GasData() {
    }

    public GasData(double YSQCYL, double QCWD, double QCZS, double dwtd, Integer SCFS, double tpc, double ppc, double rg, double ro, double rw, double yco2, double yh2s, double yn2, double JRZS, int YLMethod, int dxlMethod, double YLJZXS, int CNMethod, double jw, double pstart, double a, double b, double ae, double be, double c, double n, double currentPr, int CNMethod_b, double jw_b, double pstart_b, double a_b, double b_b, double ae_b, double be_b, double c_b, double n_b, double jiYePr, double DM_DMGXCD, double DM_DMGXNJ, double DM_SPQJ, double DM_DMGXWJ, double DM_GXFDCD, double DM_CCD, double DM_DMHJWD, double DM_QZZJ, double DM_QZRKWD, double DM_JKDQZGC, double DM_FLQYL, double DM_FLQWD, double ar, double he, double h2, double h2O, double h2S, double CO2, double n2, double c0, double c1, double c2, double c3, double iC4, double nC4, double iC5, double nC5, double iC6, double nC6, double c7, double c8, double c9, double c10, double c11, double c12, double c13, double c14, double c15, double c16) {
        this.YSQCYL = YSQCYL;
        this.QCWD = QCWD;
        this.QCZS = QCZS;
        Dwtd = dwtd;
        this.SCFS = SCFS;
        Tpc = tpc;
        Ppc = ppc;
        Rg = rg;
        Ro = ro;
        Rw = rw;
        this.yco2 = yco2;
        this.yh2s = yh2s;
        Yn2 = yn2;
        this.JRZS = JRZS;
        this.YLMethod = YLMethod;
        DxlMethod = dxlMethod;
        this.YLJZXS = YLJZXS;
        this.CNMethod = CNMethod;
        Jw = jw;
        Pstart = pstart;
        A = a;
        B = b;
        Ae = ae;
        Be = be;
        C = c;
        N = n;
        CurrentPr = currentPr;
        this.CNMethod_b = CNMethod_b;
        Jw_b = jw_b;
        Pstart_b = pstart_b;
        A_b = a_b;
        B_b = b_b;
        Ae_b = ae_b;
        Be_b = be_b;
        C_b = c_b;
        N_b = n_b;
        JiYePr = jiYePr;
        this.DM_DMGXCD = DM_DMGXCD;
        this.DM_DMGXNJ = DM_DMGXNJ;
        this.DM_SPQJ = DM_SPQJ;
        this.DM_DMGXWJ = DM_DMGXWJ;
        this.DM_GXFDCD = DM_GXFDCD;
        this.DM_CCD = DM_CCD;
        this.DM_DMHJWD = DM_DMHJWD;
        this.DM_QZZJ = DM_QZZJ;
        this.DM_QZRKWD = DM_QZRKWD;
        this.DM_JKDQZGC = DM_JKDQZGC;
        this.DM_FLQYL = DM_FLQYL;
        this.DM_FLQWD = DM_FLQWD;
        Ar = ar;
        He = he;
        H2 = h2;
        H2O = h2O;
        H2S = h2S;
        this.CO2 = CO2;
        N2 = n2;
        C0 = c0;
        C1 = c1;
        C2 = c2;
        C3 = c3;
        this.iC4 = iC4;
        this.nC4 = nC4;
        this.iC5 = iC5;
        this.nC5 = nC5;
        this.iC6 = iC6;
        this.nC6 = nC6;
        C7 = c7;
        C8 = c8;
        C9 = c9;
        C10 = c10;
        C11 = c11;
        C12 = c12;
        C13 = c13;
        C14 = c14;
        C15 = c15;
        C16 = c16;
    }

    public GasData(double YSQCYL, double QCWD, double QCZS, double dwtd, Integer SCFS, double tpc, double ppc, double rg, double ro, double rw, double yco2, double yh2s, double yn2, double JRZS, int YLMethod, int dxlMethod, double YLJZXS, int CNMethod, double jw, double pstart, double a, double b, double ae, double be, double c, double n, double currentPr, int CNMethod_b, double jw_b, double pstart_b, double a_b, double b_b, double ae_b, double be_b, double c_b, double n_b, double jiYePr, double DM_DMGXCD, double DM_DMGXNJ, double DM_SPQJ, double DM_DMGXWJ, double DM_GXFDCD, double DM_CCD, double DM_DMHJWD, double DM_QZZJ, double DM_QZRKWD, double DM_JKDQZGC, double DM_FLQYL, double DM_FLQWD) {
        this.YSQCYL = YSQCYL;
        this.QCWD = QCWD;
        this.QCZS = QCZS;
        Dwtd = dwtd;
        this.SCFS = SCFS;
        Tpc = tpc;
        Ppc = ppc;
        Rg = rg;
        Ro = ro;
        Rw = rw;
        this.yco2 = yco2;
        this.yh2s = yh2s;
        Yn2 = yn2;
        this.JRZS = JRZS;
        this.YLMethod = YLMethod;
        DxlMethod = dxlMethod;
        this.YLJZXS = YLJZXS;
        this.CNMethod = CNMethod;
        Jw = jw;
        Pstart = pstart;
        A = a;
        B = b;
        Ae = ae;
        Be = be;
        C = c;
        N = n;
        CurrentPr = currentPr;
        this.CNMethod_b = CNMethod_b;
        Jw_b = jw_b;
        Pstart_b = pstart_b;
        A_b = a_b;
        B_b = b_b;
        Ae_b = ae_b;
        Be_b = be_b;
        C_b = c_b;
        N_b = n_b;
        JiYePr = jiYePr;
        this.DM_DMGXCD = DM_DMGXCD;
        this.DM_DMGXNJ = DM_DMGXNJ;
        this.DM_SPQJ = DM_SPQJ;
        this.DM_DMGXWJ = DM_DMGXWJ;
        this.DM_GXFDCD = DM_GXFDCD;
        this.DM_CCD = DM_CCD;
        this.DM_DMHJWD = DM_DMHJWD;
        this.DM_QZZJ = DM_QZZJ;
        this.DM_QZRKWD = DM_QZRKWD;
        this.DM_JKDQZGC = DM_JKDQZGC;
        this.DM_FLQYL = DM_FLQYL;
        this.DM_FLQWD = DM_FLQWD;
    }

    public double getYSQCYL() {
        return YSQCYL;
    }

    public void setYSQCYL(double YSQCYL) {
        this.YSQCYL = YSQCYL;
    }

    public double getQCWD() {
        return QCWD;
    }

    public void setQCWD(double QCWD) {
        this.QCWD = QCWD;
    }

    public double getQCZS() {
        return QCZS;
    }

    public void setQCZS(double QCZS) {
        this.QCZS = QCZS;
    }

    public double getDwtd() {
        return Dwtd;
    }

    public void setDwtd(double dwtd) {
        Dwtd = dwtd;
    }

    public Integer getSCFS() {
        return SCFS;
    }

    public void setSCFS(Integer SCFS) {
        this.SCFS = SCFS;
    }

    public double getTpc() {
        return Tpc;
    }

    public void setTpc(double tpc) {
        Tpc = tpc;
    }

    public double getPpc() {
        return Ppc;
    }

    public void setPpc(double ppc) {
        Ppc = ppc;
    }

    public double getRg() {
        return Rg;
    }

    public void setRg(double rg) {
        Rg = rg;
    }

    public double getRo() {
        return Ro;
    }

    public void setRo(double ro) {
        Ro = ro;
    }

    public double getRw() {
        return Rw;
    }

    public void setRw(double rw) {
        Rw = rw;
    }

    public double getYco2() {
        return yco2;
    }

    public void setYco2(double yco2) {
        this.yco2 = yco2;
    }

    public double getYh2s() {
        return yh2s;
    }

    public void setYh2s(double yh2s) {
        this.yh2s = yh2s;
    }

    public double getYn2() {
        return Yn2;
    }

    public void setYn2(double yn2) {
        Yn2 = yn2;
    }

    public double getJRZS() {
        return JRZS;
    }

    public void setJRZS(double JRZS) {
        this.JRZS = JRZS;
    }

    public int getYLMethod() {
        return YLMethod;
    }

    public void setYLMethod(int YLMethod) {
        this.YLMethod = YLMethod;
    }

    public int getDxlMethod() {
        return DxlMethod;
    }

    public void setDxlMethod(int dxlMethod) {
        DxlMethod = dxlMethod;
    }

    public double getYLJZXS() {
        return YLJZXS;
    }

    public void setYLJZXS(double YLJZXS) {
        this.YLJZXS = YLJZXS;
    }

    public int getCNMethod() {
        return CNMethod;
    }

    public void setCNMethod(int CNMethod) {
        this.CNMethod = CNMethod;
    }

    public double getJw() {
        return Jw;
    }

    public void setJw(double jw) {
        Jw = jw;
    }

    public double getPstart() {
        return Pstart;
    }

    public void setPstart(double pstart) {
        Pstart = pstart;
    }

    public double getA() {
        return A;
    }

    public void setA(double a) {
        A = a;
    }

    public double getB() {
        return B;
    }

    public void setB(double b) {
        B = b;
    }

    public double getAe() {
        return Ae;
    }

    public void setAe(double ae) {
        Ae = ae;
    }

    public double getBe() {
        return Be;
    }

    public void setBe(double be) {
        Be = be;
    }

    public double getC() {
        return C;
    }

    public void setC(double c) {
        C = c;
    }

    public double getN() {
        return N;
    }

    public void setN(double n) {
        N = n;
    }

    public double getCurrentPr() {
        return CurrentPr;
    }

    public void setCurrentPr(double currentPr) {
        CurrentPr = currentPr;
    }

    public int getCNMethod_b() {
        return CNMethod_b;
    }

    public void setCNMethod_b(int CNMethod_b) {
        this.CNMethod_b = CNMethod_b;
    }

    public double getJw_b() {
        return Jw_b;
    }

    public void setJw_b(double jw_b) {
        Jw_b = jw_b;
    }

    public double getPstart_b() {
        return Pstart_b;
    }

    public void setPstart_b(double pstart_b) {
        Pstart_b = pstart_b;
    }

    public double getA_b() {
        return A_b;
    }

    public void setA_b(double a_b) {
        A_b = a_b;
    }

    public double getB_b() {
        return B_b;
    }

    public void setB_b(double b_b) {
        B_b = b_b;
    }

    public double getAe_b() {
        return Ae_b;
    }

    public void setAe_b(double ae_b) {
        Ae_b = ae_b;
    }

    public double getBe_b() {
        return Be_b;
    }

    public void setBe_b(double be_b) {
        Be_b = be_b;
    }

    public double getC_b() {
        return C_b;
    }

    public void setC_b(double c_b) {
        C_b = c_b;
    }

    public double getN_b() {
        return N_b;
    }

    public void setN_b(double n_b) {
        N_b = n_b;
    }

    public double getJiYePr() {
        return JiYePr;
    }

    public void setJiYePr(double jiYePr) {
        JiYePr = jiYePr;
    }

    public double getDM_DMGXCD() {
        return DM_DMGXCD;
    }

    public void setDM_DMGXCD(double DM_DMGXCD) {
        this.DM_DMGXCD = DM_DMGXCD;
    }

    public double getDM_DMGXNJ() {
        return DM_DMGXNJ;
    }

    public void setDM_DMGXNJ(double DM_DMGXNJ) {
        this.DM_DMGXNJ = DM_DMGXNJ;
    }

    public double getDM_SPQJ() {
        return DM_SPQJ;
    }

    public void setDM_SPQJ(double DM_SPQJ) {
        this.DM_SPQJ = DM_SPQJ;
    }

    public double getDM_DMGXWJ() {
        return DM_DMGXWJ;
    }

    public void setDM_DMGXWJ(double DM_DMGXWJ) {
        this.DM_DMGXWJ = DM_DMGXWJ;
    }

    public double getDM_GXFDCD() {
        return DM_GXFDCD;
    }

    public void setDM_GXFDCD(double DM_GXFDCD) {
        this.DM_GXFDCD = DM_GXFDCD;
    }

    public double getDM_CCD() {
        return DM_CCD;
    }

    public void setDM_CCD(double DM_CCD) {
        this.DM_CCD = DM_CCD;
    }

    public double getDM_DMHJWD() {
        return DM_DMHJWD;
    }

    public void setDM_DMHJWD(double DM_DMHJWD) {
        this.DM_DMHJWD = DM_DMHJWD;
    }

    public double getDM_QZZJ() {
        return DM_QZZJ;
    }

    public void setDM_QZZJ(double DM_QZZJ) {
        this.DM_QZZJ = DM_QZZJ;
    }

    public double getDM_QZRKWD() {
        return DM_QZRKWD;
    }

    public void setDM_QZRKWD(double DM_QZRKWD) {
        this.DM_QZRKWD = DM_QZRKWD;
    }

    public double getDM_JKDQZGC() {
        return DM_JKDQZGC;
    }

    public void setDM_JKDQZGC(double DM_JKDQZGC) {
        this.DM_JKDQZGC = DM_JKDQZGC;
    }

    public double getDM_FLQYL() {
        return DM_FLQYL;
    }

    public void setDM_FLQYL(double DM_FLQYL) {
        this.DM_FLQYL = DM_FLQYL;
    }

    public double getDM_FLQWD() {
        return DM_FLQWD;
    }

    public void setDM_FLQWD(double DM_FLQWD) {
        this.DM_FLQWD = DM_FLQWD;
    }

    public double getAr() {
        return Ar;
    }

    public void setAr(double ar) {
        Ar = ar;
    }

    public double getHe() {
        return He;
    }

    public void setHe(double he) {
        He = he;
    }

    public double getH2() {
        return H2;
    }

    public void setH2(double h2) {
        H2 = h2;
    }

    public double getH2O() {
        return H2O;
    }

    public void setH2O(double h2O) {
        H2O = h2O;
    }

    public double getH2S() {
        return H2S;
    }

    public void setH2S(double h2S) {
        H2S = h2S;
    }

    public double getCO2() {
        return CO2;
    }

    public void setCO2(double CO2) {
        this.CO2 = CO2;
    }

    public double getN2() {
        return N2;
    }

    public void setN2(double n2) {
        N2 = n2;
    }

    public double getC0() {
        return C0;
    }

    public void setC0(double c0) {
        C0 = c0;
    }

    public double getC1() {
        return C1;
    }

    public void setC1(double c1) {
        C1 = c1;
    }

    public double getC2() {
        return C2;
    }

    public void setC2(double c2) {
        C2 = c2;
    }

    public double getC3() {
        return C3;
    }

    public void setC3(double c3) {
        C3 = c3;
    }

    public double getiC4() {
        return iC4;
    }

    public void setiC4(double iC4) {
        this.iC4 = iC4;
    }

    public double getnC4() {
        return nC4;
    }

    public void setnC4(double nC4) {
        this.nC4 = nC4;
    }

    public double getiC5() {
        return iC5;
    }

    public void setiC5(double iC5) {
        this.iC5 = iC5;
    }

    public double getnC5() {
        return nC5;
    }

    public void setnC5(double nC5) {
        this.nC5 = nC5;
    }

    public double getiC6() {
        return iC6;
    }

    public void setiC6(double iC6) {
        this.iC6 = iC6;
    }

    public double getnC6() {
        return nC6;
    }

    public void setnC6(double nC6) {
        this.nC6 = nC6;
    }

    public double getC7() {
        return C7;
    }

    public void setC7(double c7) {
        C7 = c7;
    }

    public double getC8() {
        return C8;
    }

    public void setC8(double c8) {
        C8 = c8;
    }

    public double getC9() {
        return C9;
    }

    public void setC9(double c9) {
        C9 = c9;
    }

    public double getC10() {
        return C10;
    }

    public void setC10(double c10) {
        C10 = c10;
    }

    public double getC11() {
        return C11;
    }

    public void setC11(double c11) {
        C11 = c11;
    }

    public double getC12() {
        return C12;
    }

    public void setC12(double c12) {
        C12 = c12;
    }

    public double getC13() {
        return C13;
    }

    public void setC13(double c13) {
        C13 = c13;
    }

    public double getC14() {
        return C14;
    }

    public void setC14(double c14) {
        C14 = c14;
    }

    public double getC15() {
        return C15;
    }

    public void setC15(double c15) {
        C15 = c15;
    }

    public double getC16() {
        return C16;
    }

    public void setC16(double c16) {
        C16 = c16;
    }
}
