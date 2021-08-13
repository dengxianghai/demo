package com.caiyou.entity;

import java.util.List;

/**
 * DXLCal方法的参数封装
 *
 */
public class DXLCalRequest {
    private List<RecTube> RecTubeList;//数组或者集合，对应基本数据--气井基本数据（井筒油套管数据）
    // lenwell井管长度  alfawell水平倾角 dti油管内径 dto油管外径 dci套管外径 dco套管内径 dwb井筒直径r  oughwell粗糙度

    private Integer Method;//多相流计算方法        Hagedorn-Brown=0、Beggs-Brill=1、Duns-Ros=2、Okiszewski=3、Mukherjee-Brill=4
    private Integer Fx;//Fx-方向 1向下 －1向上
    private Integer SCFS;//是否油管生产：0 油管生产  1油套管环空生产
    private double DeltH;//温度压力剖面功能时，传入的是步长，其他功能待定
    private double PressJZ;//压力校正系数
    private double WGR;//水气比
    private double OGR;//油气比
    private double Rg;//气体相对密度
    private double Ro;//油的密度
    private double Rw;//水的密度
    private double Wm;//温度压力剖面计算中，是地温梯度，其他待定
    private double H;
    private double Qg;//产气量
    private double PStart;//初始压力
    private double TStart;//初始温度 K 温度要用K
    private double HStart;//初始高度
    private double Hend;//结束高度
    private double Tpc;//拟临界温度
    private double Ppc;//拟临界压力

    public DXLCalRequest() {
    }

    public DXLCalRequest(List<RecTube> recTubeList, Integer method, Integer fx, Integer SCFS, double deltH, double pressJZ, double WGR, double OGR, double rg, double ro, double rw, double wm, double h, double qg, double PStart, double TStart, double HStart, double hend, double tpc, double ppc) {
        RecTubeList = recTubeList;
        Method = method;
        Fx = fx;
        this.SCFS = SCFS;
        DeltH = deltH;
        PressJZ = pressJZ;
        this.WGR = WGR;
        this.OGR = OGR;
        Rg = rg;
        Ro = ro;
        Rw = rw;
        Wm = wm;
        H = h;
        Qg = qg;
        this.PStart = PStart;
        this.TStart = TStart;
        this.HStart = HStart;
        Hend = hend;
        Tpc = tpc;
        Ppc = ppc;
    }

    public List<RecTube> getRecTubeList() {
        return RecTubeList;
    }

    public void setRecTubeList(List<RecTube> recTubeList) {
        RecTubeList = recTubeList;
    }

    public Integer getMethod() {
        return Method;
    }

    public void setMethod(Integer method) {
        Method = method;
    }

    public Integer getFx() {
        return Fx;
    }

    public void setFx(Integer fx) {
        Fx = fx;
    }

    public Integer getSCFS() {
        return SCFS;
    }

    public void setSCFS(Integer SCFS) {
        this.SCFS = SCFS;
    }

    public double getDeltH() {
        return DeltH;
    }

    public void setDeltH(double deltH) {
        DeltH = deltH;
    }

    public double getPressJZ() {
        return PressJZ;
    }

    public void setPressJZ(double pressJZ) {
        PressJZ = pressJZ;
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

    public double getPStart() {
        return PStart;
    }

    public void setPStart(double PStart) {
        this.PStart = PStart;
    }

    public double getTStart() {
        return TStart;
    }

    public void setTStart(double TStart) {
        this.TStart = TStart;
    }

    public double getHStart() {
        return HStart;
    }

    public void setHStart(double HStart) {
        this.HStart = HStart;
    }

    public double getHend() {
        return Hend;
    }

    public void setHend(double hend) {
        Hend = hend;
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


}
