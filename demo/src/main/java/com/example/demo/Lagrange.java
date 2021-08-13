package com.example.demo;

import com.example.algorithm.Point;

import java.util.LinkedList;

/**
 * 拉格朗日插值
 *
 * @author   d
 * @date    2021/5/12
 * @version 1.0
 */
public class Lagrange {

    /**
     * 拉格朗日插值
     * @param points    已知点集合
     * @param minX      x最小值
     * @param minY      y最小值
     * @param spacingX  x步长
     * @param spacingY  y步长
     * @param maxX      x最大值
     * @param maxY      y最大值
     * @return          插值点集合
     */
    public LinkedList<Point> getLagrange(Point[] points, double minX, double minY, double spacingX, double spacingY, double maxX, double maxY) {
        //返回点的集合
        LinkedList<Point> list=new LinkedList<Point>();

        for(double i=minX;i<=maxX;i=i+spacingX){
            for(double j=minY;j<=maxY;j=j+spacingY){
                Point p=lagrange(points,new Point(i,j),points.length);
                list.add(p);
            }

        }

        return list;
    }


    /**
     * 求插值
     * @param points  已知点
     * @param test   目标点
     * @param n      已知点的个数
     * @return       求得插值的点
     */
    Point lagrange(Point[] points, Point test, double n) {
        double re = 0;
        double num=1;

        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                if(j!=i)
                    num *=  (test.x-points[j].x)/(points[i].x-points[j].x)*(test.y-points[j].y)/(points[i].y-points[j].y);
            }
            re += num*points[i].z;
            num=1;
        }
        test.z=re;
        return test;
    }

}
