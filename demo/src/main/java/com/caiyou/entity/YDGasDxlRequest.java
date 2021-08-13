package com.caiyou.entity;


/**
 * YDGasDxl请求参数封装
 */
public class YDGasDxlRequest {

    Integer Method;//多相流计算方法        Hagedorn-Brown=0、Beggs-Brill=1、Duns-Ros=2、Okiszewski=3、Mukherjee-Brill=4
    Integer Direction;//(井底向上计算为负，井口向下计算为正)
    double DeltaH;
    double PressJZ;
    double Dt;
    double WGR;
    double OGR;
    double Rg;
    double Ro;
    double Rw;
    double Wm;
    double H;
    double Qg;
    double Pstart;
    double TStart;//要用开尔文（至少，温度压力剖面）
    double H1;
    double Angle;
    double Tpc;
    double Ppc;
    double e;

    public YDGasDxlRequest() {
    }

    public YDGasDxlRequest(Integer method, Integer direction, double deltaH, double pressJZ, double dt, double WGR, double OGR, double rg, double ro, double rw, double wm, double h, double qg, double pstart, double TStart, double h1, double angle, double tpc, double ppc, double e) {
        Method = method;
        Direction = direction;
        DeltaH = deltaH;
        PressJZ = pressJZ;
        Dt = dt;
        this.WGR = WGR;
        this.OGR = OGR;
        Rg = rg;
        Ro = ro;
        Rw = rw;
        Wm = wm;
        H = h;
        Qg = qg;
        Pstart = pstart;
        this.TStart = TStart;
        H1 = h1;
        Angle = angle;
        Tpc = tpc;
        Ppc = ppc;
        this.e = e;
    }

    public Integer getMethod() {
        return Method;
    }

    public void setMethod(Integer method) {
        Method = method;
    }

    public Integer getDirection() {
        return Direction;
    }

    public void setDirection(Integer direction) {
        Direction = direction;
    }

    public double getDeltaH() {
        return DeltaH;
    }

    public void setDeltaH(double deltaH) {
        DeltaH = deltaH;
    }

    public double getPressJZ() {
        return PressJZ;
    }

    public void setPressJZ(double pressJZ) {
        PressJZ = pressJZ;
    }

    public double getDt() {
        return Dt;
    }

    public void setDt(double dt) {
        Dt = dt;
    }

    public double getWGR() {
        return WGR;
    }

    public void setWGR(double WGR) {
        this.WGR = WGR;
    }

    public double getOGR() {
        return OGR;
    }

    public void setOGR(double OGR) {
        this.OGR = OGR;
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

    public double getWm() {
        return Wm;
    }

    public void setWm(double wm) {
        Wm = wm;
    }

    public double getH() {
        return H;
    }

    public void setH(double h) {
        H = h;
    }

    public double getQg() {
        return Qg;
    }

    public void setQg(double qg) {
        Qg = qg;
    }

    public double getPstart() {
        return Pstart;
    }

    public void setPstart(double pstart) {
        Pstart = pstart;
    }

    public double getTStart() {
        return TStart;
    }

    public void setTStart(double TStart) {
        this.TStart = TStart;
    }

    public double getH1() {
        return H1;
    }

    public void setH1(double h1) {
        H1 = h1;
    }

    public double getAngle() {
        return Angle;
    }

    public void setAngle(double angle) {
        Angle = angle;
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

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }
}
