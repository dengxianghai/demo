package com.example.algorithm;

import Jama.Matrix;

/**
 *    kriging插值算法
 * @author  deng
 * @date 2021-5-8
 * @version 1.0
 */
public class Kriging {

    /**
     * 克里金插值法
     * @param points 已知点集合
     * @param minX 最小x值
     * @param minY 最小y值
     * @param spacingX x步长
     * @param spacingY y步长
     * @param maxX 最大x值
     * @param maxY 最大y值
     * @return z的二维数组
     *
     */
    public double[][] getKriging(Point[] points, double minX, double minY, double spacingX, double spacingY, double maxX, double maxY){
        int length=points.length;
        double[] x=new double[length];
        double[] y=new double[length];
        double[] z=new double[length];

        for(int i=0;i<points.length;i++){
            x[i]=points[i].getX();
            y[i]=points[i].getY();
            z[i]=points[i].getZ();
        }
        int numX= (int) ((maxX-minX)/spacingX+1);
        int numY= (int) ((maxY-minY)/spacingY+1);
        double[][]X=new double[numY][numX];
        double[][]Y=new double[numX][numY];

        double[][]Z=gridData(x,y,z,X,Y);
        return Z;

    }

    public double[][] gridData(double[] x, double[] y, double[] z,
                               double[][] X, double[][] Y) {
        int xLength = x.length;
        int xSize = X.length;//行数
        int ySize = X[0].length;//列数

        double[][] data = new double[xLength + 1][xLength + 1];
        double[][] data2 = new double[xSize][ySize];//与X相同大小的二维数组

        for (int i = 0; i <= xLength; i++) {
            for (int j = 0; j <= xLength; j++) {
                if ((i < xLength) && (j < xLength)) {
                    if (i != j) {
                        data[i][j] = Math.sqrt((x[i] - x[j]) * (x[i] - x[j])
                                + (y[i] - y[j]) * (y[i] - y[j]));  //算出每两点之间的距离
                    } else {
                        data[i][j] = 0.0001;
                    }
                } else if ((i == xLength) || (j == xLength)) {
                    data[i][j] = 1;
                }
                if (i == xLength && j == xLength) {
                    data[i][j] = 0;
                }
            }
        }
        //矩阵的初始化
        Matrix m = new Matrix(data);
        //矩阵求逆  逆矩阵矩阵a*a-1 = 单位矩阵
        Matrix mMatrix = m.inverse();
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                double[] rData = new double[xLength + 1];
                for (int k = 0; k <= xLength; k++) {
                    if (k < xLength) {
                        rData[k] = Math.sqrt((X[i][j] - x[k])
                                * (X[i][j] - x[k]) + (Y[i][j] - y[k])
                                * (Y[i][j] - y[k])); //算出预测值的距离
                    } else {
                        rData[k] = 1;  //预测值的距离
                    }
                }
                double[] rDa = new double[xLength + 1];
                for (int k = 0; k < (xLength + 1); k++) {
                    for (int k2 = 0; k2 < (xLength + 1); k2++) {
                        rDa[k] += mMatrix.get(k, k2) * rData[k2];  //返回逆矩阵*rData距离 求权重
                    }
                }
                for (int k = 0; k < xLength; k++) {
                    data2[i][j] += z[k] * rDa[k];
                }
            }
        }
        return data2;
    }
    // 变差函数，克里金算法特有的函数
    public double function(double h) {
        double r = 0;
        if (h == 0) {
            r = 0;
        } else if (h > 0 && h <= 11) { //此处是他的基程
            Kriging kriging = new Kriging();
            //kriging.variogramModel()
            //r = 0.0 + 1.154 * ((3 * h * h * h * h) / (4 * 8.535 * 8.535 * 8.535 * 8.535));
            r = 0.0 + 0.06 * (1.0D - Math.exp(- h / 11));
        } else {
            r = 3.202;
        }
        return r;
    }



}
