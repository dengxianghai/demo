package com.example.example;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;


/**
 * 递减曲线计算
 * 各符号说明：
 * q0：递减初期年产量
 * d0：初始递减率
 * t : 递减时间
 * a : 拟合模型斜率
 * b : 拟合模型截距
 *
 * @author ccs
 * @date 2020/11/25
 *
 * @version 1.1

 */
@Component
public class DeclineCurve {

    /**
     * 直线递减 n = -1
     * (直线递减就是当n=-1时的双曲递减)
     * 公式         Q = Q0 * (1-D0t)
     * 拟合公式     Q = Q0 - Q0*D0*t
     *
     * Qa：油田废弃时的产量，为一个需要输入的参数，必须大于0
     * Nrc=Npo+Q0n/((1-n)*D0)*(Q01-n-Qa1-n)
     *
     * @param t   时间数组
     * @param q   产油量数组
     * @param qa  Qa,油田废弃时的产量
     * @param npo 曲线第一个数据坐在时间点对应的累计产量
     * @return    计算参数结果及预测结果
     */
    public Map<String, Object> computeLineDeclineParams(double[] t,
                                                        double[] q,
                                                        double qa,
                                                        double npo){
        var data = handleComputeOriginalData(t,q,y -> y);
        double[] doubles = CommonAlgorithm.fitLinearFunction(data);
        // b = q0 因此省略b
        double q0 = doubles[0];
        double a = doubles[1] * -1;
        double d0 = a / q0;
        double [] qPre = new double[q.length];
        for (int i = 0; i < q.length; i++) {
            qPre[i] = q0 * (1 - d0*t[i]);
        }

        //计算nr    start          dxh 2021/5/13
        double n=-1;
        double nr=npo+Math.pow(q0,n)/((1-n)*d0)*(Math.pow(q0,1-n)-Math.pow(qa,1-n));
        //计算nr   end    dxh 2021/5/13


        return getStringObjectHashMap(q, q0, d0,a, qPre,t,nr);
    }

    /**
     * 双曲线递减  n = 0.5
     * 拟合函数： 1/pow(Q,0.5) = 1/pow(Q0,0.5) + 0.5*D0 / pow(Q0,0.5) * t
     *
     *           其中 a = 0.5*D0 / pow(Q0,0.5)
     *               b = 1/pow(Q0,0.5)           =>  Q0 = pow(1/b,2)
     *                                         =>  D0 = 2* a * pow(Q0,0.5)
     *  Qa：油田废弃时的产量，为一个需要输入的参数，必须大于0
     *  Nrc=Npo+Q0n/((1-n)*D0)*(Q01-n-Qa1-n)
     *
     * @param t   时间数组
     * @param q   产油量数组
     * @param qa  Qa,油田废弃时的产量
     * @param npo 曲线第一个数据坐在时间点对应的累计产量
     * @return    计算参数结果及预测结果
     */
    public Map<String, Object>  computeAttenuationDeclineParams(double[] t,
                                                                double[] q,
                                                                double qa,
                                                                double npo){
        var data = handleComputeOriginalData(t,q,y -> 1 / Math.pow(y,0.5));
        double[] doubles = CommonAlgorithm.fitLinearFunction(data);
        double b = doubles[0];
        double a = doubles[1];


        //拟合函数修改    start   dxh   2021/5/13
        double q0 = Math.pow(Math.abs(1/b),2);
        double d0 = 2 * a * Math.pow(q0,0.5);
        //拟合函数修改    end   dxh   2021/5/13

        double [] qPre = new double[q.length];
        for (int i = 0; i < q.length; i++) {
            qPre[i]=Math.pow(1/(b+a*t[i]),2);
        }


        //计算nr    start          dxh 2021/5/13
        double n=0.5;
        double nr=npo+Math.pow(q0,n)/((1-n)*d0)*(Math.pow(q0,1-n)-Math.pow(qa,1-n));
        //计算nr    end          dxh 2021/5/13

        return getStringObjectHashMap(q, q0, d0,a, qPre, t,nr);
    }

    /**
     * 双曲线递减（二次递减）  n = -0.5
     * 拟合函数： pow(Q,0.5) = pow(Q0,0.5) - 2*D0 *pow(Q0,0.5) * t
     *           其中 a = - 0.5*D0*pow(Q0,0.5)
     *               b = pow(Q0,0.5)           =>  Q0 = pow(b,2)
     *                                       =>  D0 = -2*a / (pow(Q0,0.5))
     * @param t   时间数组
     * @param q   产油量数组
     * @param qa  Qa,油田废弃时的产量
     * @param npo 曲线第一个数据坐在时间点对应的累计产量
     * @return    计算参数结果及预测结果
     */
    public Map<String, Object> computeQuadraticDeclineParams(double[]t,
                                                             double[]q,
                                                             double qa,
                                                             double npo){
        var data = handleComputeOriginalData(t,q,y -> Math.pow(y,0.5));
        double[] doubles = CommonAlgorithm.fitLinearFunction(data);
        double b = doubles[0];
        double a = doubles[1];




        //拟合函数修改    start   dxh   2021/5/13
        double q0 = Math.pow(b,2);
        double d0 = (-2)*a / (  Math.sqrt(q0));
        //拟合函数修改    end   dxh   2021/5/13

        double [] qPre = new double[q.length];
        for (int i = 0; i < q.length; i++) {
            qPre[i]=Math.pow(Math.sqrt(q0)+t[i]*a,2);
        }


        //计算nr    start          dxh 2021/5/13
        double n=-0.5;
        double nr=npo+Math.pow(q0,n)/((1-n)*d0)*(Math.pow(q0,1-n)-Math.pow(qa,1-n));
        //计算nr    end          dxh 2021/5/13

        return getStringObjectHashMap(q, q0, d0,a, qPre, t,nr);
    }

    /**
     * 调和递减 n = 1
     * 公式：     Q=Q0 / (1+D0*t)
     * 拟合公式： 1/Q = 1/Q0 + D0/Q0 *t
     *
     * Nrc=Npo+(Q0/D0)*ln(Q0/Qa)
     *
     * @param t   时间数组
     * @param q   产油量数组
     * @param qa  Qa,油田废弃时的产量
     * @param npo 曲线第一个数据坐在时间点对应的累计产量
     * @return    计算参数结果及预测结果
     */
    public Map<String, Object>  computeHarmonicDeclineParams(double[] t,
                                                             double[] q,
                                                             double qa,
                                                             double npo){
        var data = handleComputeOriginalData(t,q,y -> 1 / y);
        double[] doubles = CommonAlgorithm.fitLinearFunction(data);
        double b = doubles[0];
        double a = doubles[1];
        double q0 = 1 / b;
        double d0 = q0 * a;
        double [] qPre = new double[q.length];
        for (int i = 0; i < q.length; i++) {
            qPre[i] = q0 / (1 + d0 * t[i]);
        }

        //计算nr    start          dxh 2021/5/13
        double n=1;
        double nr=npo+(q0/d0)*(Math.log(q0/qa)/Math.log(Math.E));
        //计算nr   end          dxh 2021/5/13

        return getStringObjectHashMap(q, q0, d0, a, qPre,t,nr);
    }

    /**
     * 指数递减  n = 0
     * @param t   时间数组
     * @param q   产油量数组
     * @param qa  Qa,油田废弃时的产量
     * @param npo 曲线第一个数据坐在时间点对应的累计产量
     * @return    计算参数结果及预测结果
     */
    public Map<String, Object>  computeExponentialDeclineParams(double[] t,
                                                                double[] q,
                                                                double qa,
                                                                double npo){
        var data = handleComputeOriginalData(t,q, Math::log10);
        double[] doubles = CommonAlgorithm.fitLinearFunction(data);
        double b = doubles[0];
        double a = -1 * doubles[1];
        double qi = Math.pow(10,b);
        double di = 2.303 * a;
        double [] qPre = new double[q.length];
        for (int i = 0; i < q.length; i++) {
            qPre[i] = qi * Math.exp(-1*di*t[i]);
        }

        //计算nr   start          dxh 2021/5/13
        double n=0;
        double nr=npo+(qi/qa)/di;
        //计算nr    end          dxh 2021/5/13

        return getStringObjectHashMap(q, qi, di,a, qPre,t,nr);
    }

    /**
     * 组织返回数据
     * @param q  产量
     * @param q0
     * @param d0
     * @param a  斜率
     * @param qPre 预测产量
     * @param t  时间
     * @param nr 剩余可采储量
     * @return
     */
    private Map<String, Object> getStringObjectHashMap(double[] q,
                                                       double q0,
                                                       double d0,
                                                       double a,
                                                       double[] qPre,
                                                       double[] t,
                                                       double nr) {
        double r = CommonAlgorithm.correlationCoefficient(q,qPre);
        List<Map<String, Object>> listData = new LinkedList<>();
        Map<String, Object> results = new HashMap<>(2);
        Map<String, Object> scatterData = new HashMap<>(5);
        for (int i = 0; i < qPre.length; i++) {
            LinkedHashMap<String, Object> item = new LinkedHashMap<>();
            item.put("t",t[i]);
            item.put("q",q[i]);
            item.put("pre",qPre[i]);
            listData.add(item);
        }
        scatterData.put("Qo",q0);
        scatterData.put("Do",d0);
        scatterData.put("r",r);

        //Nrc计算方式修改 start    dxh  2021/5/13
//        scatterData.put("Nr",q0 / (1-1/a) / d0);
        scatterData.put("Nr",nr);
        //Nrc计算方式修改 end     dxh  2021/5/13

        results.put("list",listData);
        results.put("scatter",scatterData);
        return results;
    }

    private double[][] handleComputeOriginalData(double[] x,
                                                 double[] y,
                                                 Function<Double,Double> f){
        var minLength = Math.min(x.length,y.length);
        var data = new double[minLength][2];
        double average = 0, sum = 0;
        for (int i = 0; i < minLength; i++) {
            data[i][0] = x[i];
            data[i][1] = f.apply(y[i]);
            // 检查数据合法性,对不合法数据使用平均数代替
            if (data[i][1] == Double.POSITIVE_INFINITY || Double.isNaN(data[i][1]) || data[i][1] == Double.NEGATIVE_INFINITY){
                data[i][1] = average;
            }
            average = sum / (i+1);
            sum = sum + data[i][1];
        }
        return data;
    }



}
