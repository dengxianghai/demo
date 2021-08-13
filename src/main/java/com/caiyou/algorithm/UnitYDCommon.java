package com.caiyou.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class UnitYDCommon {
    private static final Logger logger = LoggerFactory.getLogger(UnitYDCommon.class);

    public static Map<String, Object> nihe2_dll(double[] XInput, double[] YInput, Integer m) {
        Integer i, j, k;
        double sumxy, sumx, sumy, sumxx, sumyy;
        double[][] c = new double[2][2];
        double[][] aa = new double[2][m];
        double[] Arr = new double[4];

        double[] d = new double[2];
        double[] minmax = new double[4];
        double[] e = new double[4];
        double[] x = new double[m];
        double[] y = new double[m];
        double[] x0 = new double[m];
        double[] y0 = new double[m];
        double[] yy = new double[m];

        x = XInput;
        y = YInput;
        minmax[0] = x[0];
        minmax[1] = x[0];
        minmax[2] = y[0];
        minmax[3] = y[0];
        for (i = 0; i < m; i++) {
            x0[i] = x[i];
            y0[i] = y[i];
        }
        for (i = 1; i < m; i++) {
            if (x[i] < minmax[0]) {
                minmax[0] = x[i];
            }
            if (y[i] < minmax[2]) {
                minmax[2] = y[i];
            }
            if (x[i] > minmax[1]) {
                minmax[1] = x[i];
            }
            if (y[i] > minmax[3]) {
                minmax[3] = y[i];
            }
        }
        for (i = 0; i < m; i++) {
            x[i] = x[i] + Math.abs(minmax[0]) + 1;
            y[i] = y[i] + Math.abs(minmax[2]) + 1;
        }

//***************************************88aa中存的是AT
        for (i = 0; i < m; i++) {
            aa[0][i] = 1;
            aa[1][i] = ln(x[i]);
            yy[i] = ln(y[i]);
        }

//***************************求c,其中c=AT*A
        for (i = 0; i < 2; i++) {
            for (j = 0; j < 2; j++) {
                c[i][j] = 0;
                for (k = 0; k < m; k++) {
                    c[i][j] = c[i][j] + aa[i][k] * aa[j][k];
                }
            }
        }

//****************************d为右端向量d=AT*y
        for (i = 0; i < 2; i++) {
            d[i] = 0;
            for (j = 0; j < m; j++) {
                d[i] = d[i] + aa[i][j] * yy[j];
            }
        }

//***************调用解方程的函数，返回值d为拟合系数
        Arr[0]=c[0][0];
        Arr[1]=c[0][1];
        Arr[2]=c[1][0];
        Arr[3]=c[1][1];

        i = achol2(Arr, 2, 1, d);

        for (i = 0; i < m; i++) {
            yy[i] = Math.exp(d[0]) * Math.pow((x0[i] + Math.abs(minmax[0]) + 1), d[1]) - Math.abs(minmax[2]) - 1;
        }

//*********************yy为拟合后对应的y值
//求最大离差
        e[0] = 0;
        for (i = 0; i < m; i++) {
            if (Math.abs(yy[i] - y0[i]) > e[0]) {
                e[0] = Math.abs(yy[i] - y0[i]);
            }
        }
//最大离差结束
//求剩余标准差
        e[2] = 0;
        for(i=0;i<m;i++) {
            e[2] = e[2] + Math.pow((y0[i] - yy[i]), 2);
        }
        e[2] = Math.sqrt(e[2] / m);

//求相关系数
        sumxy = 0;
        sumx = 0;
        sumy = 0;
        sumxx = 0;
        sumyy = 0;
        for(i=0;i<m;i++) {
            sumxy = sumxy + x0[i] * y0[i];
            sumx = sumx + x0[i];
            sumy = sumy + y0[i];
            sumxx = sumxx + x0[i] * x0[i];
            sumyy = sumyy + y0[i] * y0[i];
        }
        sumxy = sumxy / m;
        sumx = sumx / m;
        sumy = sumy / m;
        sumxx = sumxx / m;
        sumyy = sumyy / m;
        e[1] = (sumxy - sumx * sumy) / (Math.sqrt((sumxx - sumx * sumx) * (sumyy - sumy * sumy)));

//求相关系数结束
        e[3] = sumy;
        double[]Outputd = d;
        double[]Outputminmax = minmax;
        double[]Outpute = e;
        Map<String,Object>map=new HashMap<>();
        map.put("d",Outputd);
        map.put("minmax",Outputminmax);
        map.put("e",Outpute);

        return map;
    }

    public static double ln(double x) {
        return Math.log1p(x - 1);
    }

    public static Integer achol2(double[] a, Integer n, Integer m, double[] d) {
        Integer i, j, k, u, v, Result;
        try {
            if ((a[0] + 1.0 == 1.0) || (a[0] < 0.0)) {
                Result = -2;
                logger.info("achol2方法，Result = -2");
                return Result;
            }
            a[0] = Math.sqrt(a[0]);
            for (j = 1; j < n; j++) {
                a[j] = a[j] / a[0];
            }
            for (i = 1; i < n; i++) {
                u = i * n + i;
                for (j = 1; j <= i; j++) {
                    v = (j - 1) * n + i;
                    a[u] = a[u] - a[v] * a[v];
                }
                if ((a[u] + 1.0 == 1.0) || (a[u] < 0.0)) {
                    Result = -2;
                    logger.info("achol2方法，Result = -2");
                    return Result;
                }
                a[u] = Math.sqrt(a[u]);
                if (!(i == (n - 1))) {
                    for (j = i + 1; j < n; j++) {
                        v = i * n + j;
                        for (k = 1; k <= i; k++) {
                            a[v] = a[v] - a[(k - 1) * n + i] * a[(k - 1) * n + j];
                        }
                        a[v] = a[v] / a[u];
                    }
                }
            }
            for (j = 0; j < m; j++) {
                d[j] = d[j] / a[0];
                for (i = 1; i < n; i++) {
                    u = i * n + i;
                    v = i * m + j;
                    for (k = 1; k <= i; k++) {
                        d[v] = d[v] - a[(k - 1) * n + i] * d[(k - 1) * m + j];
                    }
                    d[v] = d[v] / a[u];
                }
            }
            for (j = 0; j < m; j++) {
                u = (n - 1) * m + j;
                d[u] = d[u] / a[n * n - 1];
                for (k = n - 1; k > 0; k--) {
                    u = (k - 1) * m + j;
                    for (i = k; i < n; i++) {
                        v = (k - 1) * n + i;
                        d[u] = d[u] - a[v] * d[i * m + j];
                    }
                    v = (k - 1) * n + k - 1;
                    d[u] = d[u] / a[v];
                }
            }
            Result = 2;
            
        }catch (Exception e){
            Result = -2;
        }
        return Result;
    }

    public static Map HuiGui02(double[]x,double[]y){
        double xx, yy;
        double xy, sx;
        double sy, Ax, Ay;
        double lxx, lyy, lxy;
        Integer i;
        Integer N;
        double a, b, r;

        N = x.length;
        xx = 0; yy = 0; xy = 0;
        sx = 0; sy = 0;
        for(i=0;i<N;i++) {
            sx = sx + x[i];
            sy = sy + y[i];
            xx = xx + x[i] * x[i];
            yy = yy + y[i] * y[i];
            xy = xy + x[i] * y[i];
        }
        lxx = xx - sx * sx / N;
        lyy = yy - sy * sy / N;
        lxy = xy - sx * sy / N;
        r = lxy / (Math.sqrt(Math.abs(lxx * lyy)));
        Ax = sx / N;
        Ay = sy / N;
        a = lxy / lxx; //y=b+ax      //斜率
        b = (Ay - a) / Ax;
        Map<String,Double> map=new HashMap<>();
        map.put("a",a);
        map.put("b",b);
        map.put("r",r);
        return map;
    }
}
