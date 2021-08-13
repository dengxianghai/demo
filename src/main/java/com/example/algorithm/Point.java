package com.example.algorithm;

public class Point {
    public double x;
    public double y;
    public double z;
    public double d;    //该点距目标点的距离
    public double w;    //权重

    public Point() {

    }
    public Point(double x, double y) {
        this.x = x;
        this.y = y;

    }
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getD() {
        return d;
    }

    public double getW() {
        return w;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setW(double w) {
        this.w = w;
    }
}
