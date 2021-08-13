package com.example.impl;

import com.example.algorithm.Inverse;
import com.example.algorithm.Point;
import com.example.demo.Lagrange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

/**
 *  插值计算接口
 * @author d
 * @date 2021/5/12
 * @version 1.0
 */
@Controller
@RestController
@RequestMapping("/grid")
public class Grid {

    private static final Logger logger = LoggerFactory.getLogger(Grid.class);

    @RequestMapping("/test")
    public void test(){
        System.out.println("1111");

    }
    /**
     *  计算插值的统一接口
     * @param points 已知点的集合
     * @param minX 最小x值
     * @param minY 最小y值
     * @param spacingX x步长
     * @param spacingY y步长
     * @param maxX 最大x值
     * @param maxY 西大y值
     * @param type 插值类型
     * @return 插值点集合
     */
    @RequestMapping("/getGrid")
    public LinkedList<Point> getGrid(Point[] points, double minX, double minY, double spacingX, double spacingY, double maxX, double maxY, String type){

        if(type==null||"".equals(type)){
            logger.info("字符串问题");
            return null;
        }
        if(type.equals("Inverse")){
            Inverse inverse=new Inverse();
            LinkedList<Point> pointsList=inverse.getInversePoints(points,minX,minY,spacingX,spacingY,maxX,maxY);
            logger.info("进入Inverse插值");
            return pointsList;
        }
        if(type.equals("Lagrange")){
            Lagrange lagrange=new Lagrange();
            LinkedList<Point> pointsList=lagrange.getLagrange(points,minX,minY,spacingX,spacingY,maxX,maxY);
            logger.info("进入Lagrange插值");
            return pointsList;
        }



        return null;
    }
}
