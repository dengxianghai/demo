package com.caiyou.entity;

/**
 * 油套管数据
 */
public class RecTube {
    private double lenwell;//井管长度
    private double alfawell;//水平倾角
    private double dti;//油管内径
    private double dto;//油管外径
    private double dci;//套管外径
    private double dco;//套管内径
    private double dwb;//井筒直径
    private double roughwell;//粗糙度

    public RecTube() {
    }

    public RecTube(double lenwell, double alfawell, double dti, double dto, double dci, double dco, double dwb, double roughwell) {
        this.lenwell = lenwell;
        this.alfawell = alfawell;
        this.dti = dti;
        this.dto = dto;
        this.dci = dci;
        this.dco = dco;
        this.dwb = dwb;
        this.roughwell = roughwell;
    }

    public double getLenwell() {
        return lenwell;
    }

    public void setLenwell(double lenwell) {
        this.lenwell = lenwell;
    }

    public double getAlfawell() {
        return alfawell;
    }

    public void setAlfawell(double alfawell) {
        this.alfawell = alfawell;
    }

    public double getDti() {
        return dti;
    }

    public void setDti(double dti) {
        this.dti = dti;
    }

    public double getDto() {
        return dto;
    }

    public void setDto(double dto) {
        this.dto = dto;
    }

    public double getDci() {
        return dci;
    }

    public void setDci(double dci) {
        this.dci = dci;
    }

    public double getDco() {
        return dco;
    }

    public void setDco(double dco) {
        this.dco = dco;
    }

    public double getDwb() {
        return dwb;
    }

    public void setDwb(double dwb) {
        this.dwb = dwb;
    }

    public double getRoughwell() {
        return roughwell;
    }

    public void setRoughwell(double roughwell) {
        this.roughwell = roughwell;
    }
}
