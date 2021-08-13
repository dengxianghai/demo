package com.caiyou.entity;

/**
 * YDGasDxl返回参数封装
 */
public class YDGasDxlResponse {
    double P2;
    double T2;
    double H2;
    double PL;
    double Pg;
    double Pm;
    double Vsl;
    double Vsg;
    double HL;
    double Z;

    String Flow;
    String JieShu;

    public double getP2() {
        return P2;
    }

    public void setP2(double p2) {
        P2 = p2;
    }

    public double getT2() {
        return T2;
    }

    public void setT2(double t2) {
        T2 = t2;
    }

    public double getH2() {
        return H2;
    }

    public void setH2(double h2) {
        H2 = h2;
    }

    public double getPL() {
        return PL;
    }

    public void setPL(double PL) {
        this.PL = PL;
    }

    public double getPg() {
        return Pg;
    }

    public void setPg(double pg) {
        Pg = pg;
    }

    public double getPm() {
        return Pm;
    }

    public void setPm(double pm) {
        Pm = pm;
    }

    public double getVsl() {
        return Vsl;
    }

    public void setVsl(double vsl) {
        Vsl = vsl;
    }

    public double getVsg() {
        return Vsg;
    }

    public void setVsg(double vsg) {
        Vsg = vsg;
    }

    public double getHL() {
        return HL;
    }

    public void setHL(double HL) {
        this.HL = HL;
    }

    public double getZ() {
        return Z;
    }

    public void setZ(double z) {
        Z = z;
    }

    public String getFlow() {
        return Flow;
    }

    public void setFlow(String flow) {
        Flow = flow;
    }

    public String getJieShu() {
        return JieShu;
    }

    public void setJieShu(String jieShu) {
        JieShu = jieShu;
    }
}
