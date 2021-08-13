package com.caiyou.entity;

/**
 * 产能分析--产气方程回归，返回实体类
 */
public class ProduceGasResponse {
    private double c;
    private double n;
    private double Aof;
    private double r;
    private double[]x;
    private double[]y;
    private double[]Qsc;
    private double[]PwfTZ;

    public ProduceGasResponse() {
    }

    public ProduceGasResponse(double c, double n, double aof, double r, double[] x, double[] y, double[] qsc, double[] pwfTZ) {
        this.c = c;
        this.n = n;
        Aof = aof;
        this.r = r;
        this.x = x;
        this.y = y;
        Qsc = qsc;
        PwfTZ = pwfTZ;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getAof() {
        return Aof;
    }

    public void setAof(double aof) {
        Aof = aof;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double[] getY() {
        return y;
    }

    public void setY(double[] y) {
        this.y = y;
    }

    public double[] getQsc() {
        return Qsc;
    }

    public void setQsc(double[] qsc) {
        Qsc = qsc;
    }

    public double[] getPwfTZ() {
        return PwfTZ;
    }

    public void setPwfTZ(double[] pwfTZ) {
        PwfTZ = pwfTZ;
    }
}
