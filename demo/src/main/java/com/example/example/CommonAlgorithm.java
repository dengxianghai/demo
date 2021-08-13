package com.example.example;


import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.List;



/**
 * 通用、零散的计算算法
 *
 * @author ccs
 * @date 2020/11/19
 */
public class CommonAlgorithm {

    /**
     * 计算两个数组的相关性
     */
    public static double correlationCoefficient(double[] array1, double[] array2) {
        return new PearsonsCorrelation().correlation(array1,array2);
    }

    /**
     * 计算两数组的相关性
     */
    public static double correlationCoefficient(List<Double> array1, List<Double> array2) {
        double[] doubles1 = new double[array1.size()];
        double[] doubles2 = new double[array2.size()];
        for (int i = 0; i < array1.size(); i++) {
            doubles1[i] = array1.get(i);
            doubles2[i] = array2.get(i);
        }
        return new SpearmansCorrelation().correlation(doubles1, doubles2);
    }


    /**
     * 计算两直角坐标距离
     *
     * @param fromX 起始点x
     * @param fromY 起始点y
     * @param toX   终点x
     * @param toY   终点y
     * @return 两点距离(m)
     */
    public static double coordinateDistance(double fromX, double fromY, double toX, double toY) {
        double x = fromX - toX;
        double y = fromY - toY;
        return Math.sqrt(x * x + y * y);
    }


//    /**
//     * 根据距离以centerWells为中心划分斤组
//     *
//     * @param centerWells 中心井
//     * @param wells       四周井
//     * @param distance    距离
//     * @return 井组map
//     */
//    public static Map<String, List<Well>> groupingWellsByDistance(List<Well> centerWells, List<Well> wells, double distance) {
//        return null;
//    }


    /**
     * 将距离转换为经度
     *
     * @param distance 距离
     * @param latitude 起始经度
     * @return 计算后经度
     */
    public static Double convertDistanceToLatitude(long distance, Double latitude) {
        double lngDegree = 2 * Math.asin(Math.sin((double) distance / 12742) / Math.cos(latitude));
        lngDegree = lngDegree * (180 / Math.PI);
        return lngDegree;
    }

    /**
     * 拟合指定次项
     * @param x        x
     * @param y        y
     * @param degree   项数
     * @return         拟合截距和系数
     */
    public static double[] fitLinearFunction(double[] x, double[] y, int degree){
        final WeightedObservedPoints points = new WeightedObservedPoints();
        for (int i = 0; i < Math.min(x.length,y.length); i++) {
            points.add(x[i],y[i]);
        }
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        return fitter.fit(points.toList());
    }



    /**
     * 拟合指定次项
     * @param points    x,y值
     * @param degree    项数
     * @return          拟合截距和系数
     */
    public static double[] fitLinearFunction(WeightedObservedPoints points, int degree){
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        return fitter.fit(points.toList());
    }

    /**
     * 拟合指定次项
     * @param data data
     * @return double type result
     */
    public static double[] fitLinearFunction(double [][] data){
        return startToFit(data,true);
    }


    /**
     * 拟合指定次项
     * @param x array of x
     * @param y array of y
     * @return result
     */
    public static double[] fitLinearFunction(Double[] x,Double[] y){
        var minLength = Math.min(x.length,y.length);
        var data = new double[minLength][2];
        for (int i = 0; i < minLength; i++) {
            data[i][0] = x[i];
            data[i][1] = y[i];
        }
        return startToFit(data, true);
    }

    /**
     * 拟合指定次项
     * @param x array of x
     * @param y array of y
     * @return result
     */
    public static double[] fitLinearFunction(Double[] x,Double[] y, boolean hasIntercept){
        var minLength = Math.min(x.length,y.length);
        var data = new double[minLength][2];
        for (int i = 0; i < minLength; i++) {
            if (x[i] == Double.POSITIVE_INFINITY || x[i] == Double.NEGATIVE_INFINITY ) {
                continue;
            }
            if (y[i] == Double.POSITIVE_INFINITY || y[i] == Double.NEGATIVE_INFINITY ) {
                continue;
            }
            data[i][0] = x[i];
            data[i][1] = y[i];
        }
        return startToFit(data, hasIntercept);
    }
    /**
     * 拟合指定次项
     * @param x      array of x
     * @param y      array of y
     * @param start  数据开始索引
     * @param end    数据结束索引
     * @return       result
     */
    public static double[] fitLinearFunction(Double[] x,Double[] y,int start,int end){
        var minLength = Math.min(x.length,y.length);
        if (start >= minLength){
            return new double[]{0,0};
        }
        end = end > minLength? minLength : end;
        var data = new double[end - start + 1][2];
        for (int i = 0; i <= end - start; i++) {
            data[i][0] = x[i+start];
            data[i][1] = y[i+start];
        }
        return startToFit(data, true);
    }

    private static double[] startToFit(double[][] data,boolean hasIntercept){
        SimpleRegression regression = new SimpleRegression();
        if (!hasIntercept) {
            regression = new SimpleRegression(false);
        }
        regression.addData(data);
        RegressionResults regress = regression.regress();
        var result = new double[2];
        result[0] = regress.getParameterEstimate(0);
        if (hasIntercept) {
            result[1] = regress.getParameterEstimate(1);
        }
        return result;
    }

    /**
     * 计算List的最大最小值
     * @param nums     数据源
     * @return         [max,min]
     */
    public static double[] extent(List<Double> nums){
        double min = nums.get(0);
        double max = nums.get(0);
        for (double item: nums) {
            min = Math.min(item,min);
            max = Math.max(item,max);
        }
        return new double[]{max,min};
    }

    /**
     * 归一化计算
     * @param x      被归一化数据
     * @param min    最小值
     * @param max    最大值
     * @return       归一化后值
     */
    public static double normalization(double x, double min, double max){
        return (x - min) / (max - min);
    }


    /**
     * 更具油水比例和半径获取 分界点位 相对坐标的x,y
     *
     * @param ratio   水 / （油 + 水）
     * @param r       半径
     * @return        [x,y]
     */
    public static double[] bubbleLocation(double ratio,double r){
        var angle = ratio * Math.PI;
        return new double[]{r*Math.cos(angle),r*Math.sin(angle)};
    }


}

