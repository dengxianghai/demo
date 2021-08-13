package com.example.algorithm;

import java.util.LinkedList;

/**
 *  反距离加权插值法
 *
 * @author deng
 * @date 2021.5.12
 * @version 1.0
 */

public class Inverse {
    /**
     *  反距离加权插值法
     * @param points 已知的点集合
     * @param minX 最小x值
     * @param minY 最小y值
     * @param spacingX x步长
     * @param spacingY y步长
     * @param maxX 最大x值
     * @param maxY 最大 y值
     * @return 插值集合
     */
    public LinkedList<Point> getInversePoints(Point[] points, double minX, double minY, double spacingX, double spacingY, double maxX, double maxY){

        //返回点的集合
        LinkedList<Point> list=new LinkedList<Point>();

        for(double i=minX;i<=maxX;i=i+spacingX){
            for(double j=minY;j<=maxY;j=j+spacingY){
                Point p=Inverse(points,new Point(i,j));
                list.add(p);
            }

        }
        return list;

    }

//    static Point[] points;  //用户存放离散点
//    static Point point = new Point();//目标点
    /**
     * 计算单个点的插值
     * @param points  已知点集合
     * @param p       目标点
     * @return        返回计算出z值的目标点
     */
    public Point Inverse(Point[] points,Point p){

        /*局部变量*/
        int n=0;   //离散点个数
        int beta=1;   //beta值，一般设为1或2

        points=GetDistance(points,p);//计算距离
        //double fenmu=GetFenmu(beta,points);
        GetWeight(beta,points);//计算权重
        p=GetTargetZ(points,p);//得到最终高程值
        return p;
    }


    /**
     * 计算距离，每一个离散点至目标点的平面距离
     * @param points 已知点集合
     * @param point  目标点
     * @return       计算出权重的已知点
     */
    static Point[] GetDistance(Point[] points,Point point)
    {
        for (int i = 0; i < points.length; i++)
        {

            points[i].d = Math.sqrt(Math.pow((point.x - points[i].x), 2) + Math.pow((point.y - points[i].y), 2));
        }
        return points;
    }



    /**
     * 获取分母，计算中会用到
     * @param beta    beta值
     * @param points  已知点集合
     * @return        分母
     */
    static double GetFenmu(int beta,Point[] points)
    {
        double fenmu = 0;
        for (int i = 0; i < points.length; i++)
        {
            fenmu += Math.pow((1/points[i].d),beta);
        }
        return fenmu;
    }



    /**
     *   计算权重
     * @param beta   beta值
     * @param points 已知点集合
     *               （传递数组会传递修改后的值，因此可以不返回值）
     */
    public void GetWeight(int beta,Point[] points)
    {
        //权重是距离的倒数的函数
        double fenmu = GetFenmu(beta,points);
        for (int i = 0; i < points.length; i++)
        {
            points[i].w = Math.pow((1 / points[i].d),beta) / fenmu;
        }
    }


    /**
     * 得到最终高程值
     * @param points  已知点集合
     * @param point   目标点
     * @return        计算出z值的目标点
     */
    static Point GetTargetZ(Point[] points,Point point)
    {
        for (int i = 0; i < points.length; i++)
        {
            point.z += points[i].z * points[i].w;
        }
        return point;
    }








}
