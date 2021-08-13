package com.caiyou.entity;

import java.util.List;

public class QJJTYLRequest {
    private Integer DMFlag;
    private Integer cal;
    private Integer Flow;
    private Integer SepExist;
    private Integer JiaoDu;
    private Integer YLMethod;
    private Integer DXLMethod;
    private List<RecTube> recTubes;

    private double Wm;
    private double yqb;
    private double sqb;
    private double hm;
    private double p;
    private double rg;
    private double ro;
    private double rw;
    private double yco2;
    private double yn2;
    private double yh2s;
    private double ppc;
    private double tpc;
    private double Tt;
    private double tw;
    private double qsc;
    private double d2;
    private double d1;
    private double e;
    private double l;
    private double H;

    public QJJTYLRequest() {
    }

    public QJJTYLRequest(Integer DMFlag, Integer cal, Integer flow, Integer sepExist, Integer jiaoDu, Integer YLMethod, Integer DXLMethod, List<RecTube> recTubes, double wm, double yqb, double sqb, double hm, double p, double rg, double ro, double rw, double yco2, double yn2, double yh2s, double ppc, double tpc, double tt, double tw, double qsc, double d2, double d1, double e, double l, double h) {
        this.DMFlag = DMFlag;
        this.cal = cal;
        Flow = flow;
        SepExist = sepExist;
        JiaoDu = jiaoDu;
        this.YLMethod = YLMethod;
        this.DXLMethod = DXLMethod;
        this.recTubes = recTubes;
        Wm = wm;
        this.yqb = yqb;
        this.sqb = sqb;
        this.hm = hm;
        this.p = p;
        this.rg = rg;
        this.ro = ro;
        this.rw = rw;
        this.yco2 = yco2;
        this.yn2 = yn2;
        this.yh2s = yh2s;
        this.ppc = ppc;
        this.tpc = tpc;
        Tt = tt;
        this.tw = tw;
        this.qsc = qsc;
        this.d2 = d2;
        this.d1 = d1;
        this.e = e;
        this.l = l;
        H = h;
    }

    public Integer getDMFlag() {
        return DMFlag;
    }

    public void setDMFlag(Integer DMFlag) {
        this.DMFlag = DMFlag;
    }

    public Integer getCal() {
        return cal;
    }

    public void setCal(Integer cal) {
        this.cal = cal;
    }

    public Integer getFlow() {
        return Flow;
    }

    public void setFlow(Integer flow) {
        Flow = flow;
    }

    public Integer getSepExist() {
        return SepExist;
    }

    public void setSepExist(Integer sepExist) {
        SepExist = sepExist;
    }

    public Integer getJiaoDu() {
        return JiaoDu;
    }

    public void setJiaoDu(Integer jiaoDu) {
        JiaoDu = jiaoDu;
    }

    public Integer getYLMethod() {
        return YLMethod;
    }

    public void setYLMethod(Integer YLMethod) {
        this.YLMethod = YLMethod;
    }

    public Integer getDXLMethod() {
        return DXLMethod;
    }

    public void setDXLMethod(Integer DXLMethod) {
        this.DXLMethod = DXLMethod;
    }

    public List<RecTube> getRecTubes() {
        return recTubes;
    }

    public void setRecTubes(List<RecTube> recTubes) {
        this.recTubes = recTubes;
    }

    public double getWm() {
        return Wm;
    }

    public void setWm(double wm) {
        Wm = wm;
    }

    public double getYqb() {
        return yqb;
    }

    public void setYqb(double yqb) {
        this.yqb = yqb;
    }

    public double getSqb() {
        return sqb;
    }

    public void setSqb(double sqb) {
        this.sqb = sqb;
    }

    public double getHm() {
        return hm;
    }

    public void setHm(double hm) {
        this.hm = hm;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getRg() {
        return rg;
    }

    public void setRg(double rg) {
        this.rg = rg;
    }

    public double getRo() {
        return ro;
    }

    public void setRo(double ro) {
        this.ro = ro;
    }

    public double getRw() {
        return rw;
    }

    public void setRw(double rw) {
        this.rw = rw;
    }

    public double getYco2() {
        return yco2;
    }

    public void setYco2(double yco2) {
        this.yco2 = yco2;
    }

    public double getYn2() {
        return yn2;
    }

    public void setYn2(double yn2) {
        this.yn2 = yn2;
    }

    public double getYh2s() {
        return yh2s;
    }

    public void setYh2s(double yh2s) {
        this.yh2s = yh2s;
    }

    public double getPpc() {
        return ppc;
    }

    public void setPpc(double ppc) {
        this.ppc = ppc;
    }

    public double getTpc() {
        return tpc;
    }

    public void setTpc(double tpc) {
        this.tpc = tpc;
    }

    public double getTt() {
        return Tt;
    }

    public void setTt(double tt) {
        Tt = tt;
    }

    public double getTw() {
        return tw;
    }

    public void setTw(double tw) {
        this.tw = tw;
    }

    public double getQsc() {
        return qsc;
    }

    public void setQsc(double qsc) {
        this.qsc = qsc;
    }

    public double getD2() {
        return d2;
    }

    public void setD2(double d2) {
        this.d2 = d2;
    }

    public double getD1() {
        return d1;
    }

    public void setD1(double d1) {
        this.d1 = d1;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
    }

    public double getH() {
        return H;
    }

    public void setH(double h) {
        H = h;
    }
}
