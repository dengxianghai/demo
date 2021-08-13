package com.caiyou.entity;

/**
 * 产能分析--产水方程回归，返回实体类
 */
public class ProduceWaterResponse {
    private double k;//产液指数
    private double b;//启动压差
    private double[]x;//直线图横坐标
    private double[]y;//直线图纵坐标
    private  double[]Qw;//点图横坐标
    private double[]Pwf;//点图纵坐标

    public ProduceWaterResponse() {
    }

    public ProduceWaterResponse(double k, double b, double[] x, double[] y, double[] qw, double[] pwf) {
        this.k = k;
        this.b = b;
        this.x = x;
        this.y = y;
        Qw = qw;
        Pwf = pwf;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
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

    public double[] getQw() {
        return Qw;
    }

    public void setQw(double[] qw) {
        Qw = qw;
    }

    public double[] getPwf() {
        return Pwf;
    }

    public void setPwf(double[] pwf) {
        Pwf = pwf;
    }
}
