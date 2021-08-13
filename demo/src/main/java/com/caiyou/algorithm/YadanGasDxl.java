package com.caiyou.algorithm;

import com.caiyou.entity.YDGasDxlRequest;
import com.caiyou.entity.YDGasDxlResponse;

import java.util.HashMap;
import java.util.Map;

import static org.apache.coyote.http11.Constants.Z;


/**
 *
 */
public class YadanGasDxl {

    private static double pi = Math.PI;

    static double f100001x[] = {826.0379496, 847.2274141, 1315.224832, 2432.204009, 931.73804, 10764.65214, 28840.31503, 63973.48355,
            182810.0216, 454988.0602, 1061695.557, 4236429.66, 9908319.449};

    static double f100001[] = {0.020276827, 0.020183664, 0.016032454, 0.012274392, 0.009462372,
            0.007481695, 0.005807644, 0.004819478, 0.003935501, 0.003303695, 0.002864178, 0.002466039, 0.002243882};

    static double f10001x[] = {826.0379496, 1303.166778, 1945.360082, 3435.579479, 6870.6844, 14487.71854,
            30549.21113, 62950.61829, 126473.6347, 232273.6796, 454988.0602, 1039920.166, 2285598.803, 5093308.711, 9862794.856};

    static double f10001[] = {0.020276827, 0.016069413, 0.013396767, 0.010839269, 0.008669619, 0.006982324, 0.005861382,
            0.005164164, 0.004634469, 0.004216965, 0.003828247, 0.003548134, 0.003280953, 0.003155005, 0.003126079};

    static double f1001x[] = {826.0379496, 1218.989599, 1798.870915, 2202.926463, 2728.977783, 3097.419299, 3427.677865, 3837.072455,
            4518.559444, 5533.501092, 7030.723199, 9772.37221, 12560.29964, 19319.68317, 30902.95433, 45708.81896, 68548.82265, 105681.7509, 184501.5419,
            302691.3428, 460256.5736, 736207.0975, 1037528.416, 1465547.841, 2089296.131, 3026913.428, 4159106.105, 5571857.489, 7655966.069, 10000000};

    static double f1001[] = {0.020276827, 0.016634127, 0.013931568, 0.012882496, 0.012445146, 0.012217997, 0.011694994, 0.011091748, 0.010495424,
            0.009954054, 0.009141132, 0.008452788, 0.007906786, 0.007144963, 0.006652732, 0.006223003, 0.005929253, 0.005688529, 0.005432503, 0.005272299,
            0.005152286, 0.005011872, 0.004954502, 0.004988845, 0.005023426, 0.004977371, 0.005011872, 0.005011872, 0.004988845, 0.004988845};

    static double f101x[] = {826.0379496, 1076.465214, 1282.330583, 1592.208727, 2046.444637, 2540.972706, 3311.311215, 4375.221052, 5929.253246,
            8452.788452, 10495.42429, 13677.28826, 17218.68575, 23334.58062, 32885.16309, 51286.1384, 73282.45331, 108642.5624, 171790.8387, 244906.3242, 402717.0343,
            677641.5076, 1099005.839, 1874994.508, 2924152.378, 4178303.666, 6280583.588, 10303861.2};

    static double f101[] = {0.020276827, 0.018030177, 0.016443717, 0.015703628, 0.014757065, 0.014060475, 0.013396767, 0.012764388, 0.011967405,
            0.011455129, 0.011117317, 0.010864256, 0.01081434, 0.010495424, 0.010303861, 0.010139114, 0.010092529, 0.009977001, 0.009862795, 0.009862795, 0.009772372,
            0.009772372, 0.009727472, 0.009571941, 0.009571941, 0.009594006, 0.009484185, 0.009484185};

    static double f105x[] = {826.0379496, 3962.780343, 7211.074792, 10690.54879, 17100.15315, 28444.61107, 47863.00923, 108143.3951, 314774.8314, 701455.2984,
            1122018.454, 1940885.878, 3273406.949, 5649369.748, 10000000};

    static double f105[] = {0.020276827, 0.019453601, 0.019319683, 0.019010783, 0.018923436, 0.018706821, 0.018706821, 0.018535316, 0.018793168, 0.018365383,
            0.018281002, 0.018281002, 0.018323144, 0.018113401, 0.018155157};

    static double f2xs[] = {0.001, 0.00164059, 0.00277332, 0.005420009, 0.010764652, 0.017418069, 0.027352687, 0.041686938, 0.068548823, 0.109144034, 0.176603782,
            0.273526873, 0.393550075, 0.45498806, 0.504661298, 0.558470195, 0.626613865, 0.693425806, 0.801678063, 0.954992586, 1.137627286, 1.355189412, 1.610645635,
            1.945360082, 2.349632821, 2.679168325, 3.097419299, 3.48337315, 4.027170343, 4.456562484, 5.152286446, 6.039486294, 7.19448978, 8.570378452, 10.35142167,
            12.1338885, 13.83566379, 16.71090614, 20.79696687, 25.46830253, 33.11311215, 43.05266105, 54.32503315, 73.79042301, 90.36494737, 109.1440336, 130.0169578,
            154.8816619, 201.372425, 258.2260191, 320.6269325, 423.642966, 558.4701947, 726.1059574, 903.6494737, 1000};


    static double f2s[] = {1.006931669, 1, 1.011579454, 1.011579454, 1.018591388, 1.018591388, 1.023292992, 1.037528416, 1.044720219, 1.044720219, 1.044720219,
            1.035142167, 1.035142167, 1.013911386, 1, 0.972747224, 0.941889597, 0.903649474, 0.860993752, 0.82603795, 0.781627805, 0.726105957, 0.691830971, 0.645654229,
            0.598411595, 0.575439937, 0.552077439, 0.518800039, 0.497737085, 0.475335226, 0.456036916, 0.42854852, 0.39994475, 0.374110588, 0.351560441, 0.334965439,
            0.314774831, 0.298538262, 0.284446111, 0.26915348, 0.251188643, 0.237684029, 0.226986485, 0.223872114, 0.217770977, 0.216271852, 0.215774441, 0.212813905,
            0.211348904, 0.208449088, 0.208449088, 0.208449088, 0.208449088, 0.207014135, 0.20558906, 0.204173794};


    public static YDGasDxlResponse YDGasDxl(YDGasDxlRequest request) {
        Integer Method = request.getMethod();//多相流计算方法        Hagedorn-Brown=0、Beggs-Brill=1、Duns-Ros=2、Okiszewski=3、Mukherjee-Brill=4
        Integer Direction = request.getDirection();//(井底向上计算为负，井口向下计算为正)
        double DeltaH = request.getDeltaH();
        double PressJZ = request.getPressJZ();
        double Dt = request.getDt();
        double WGR = request.getWGR();
        double OGR = request.getOGR();
        double Rg = request.getRg();
        double Ro = request.getRo();
        double Rw = request.getRw();
        double Wm = request.getWm();
        double H = request.getH();
        double Qg = request.getQg();
        double Pstart = request.getPstart();
        double TStart = request.getTStart();
        double H1 = request.getH1();
        double Angle = request.getAngle();
        double Tpc = request.getTpc();
        double Ppc = request.getPpc();
        double e = request.getE();

        double Wf, T2, Pav, Tav, Vsl = 0, Vsg = 0, Ul, Beta, Thwg, PL = 0, Pg = 0, Pm = 0, Nd, Nu, CNu, PHI1, HLPHI, PHI2, PHI, HL = 0, Mg;
        PHI = 0;
        CNu = 0;
        HLPHI = 0;
        String Flow = null;
        double A, B, C, Hg, Pn = 0, X, Y, K, Fm = 0, P2c, thog, Thtotal, HLSeg, HLInter, HLDistri, AngelXZ, HLangle = 0, S, Fmn, Fn;
        C = 0;
        double F1 = 0, F2 = 0, F3 = 0, F4 = 0, F5 = 0, F6 = 0, F7 = 0;
        double Nl; //计算无因次液相粘度Nl
        double Ns; //滑脱速度准数
        double Us; //滑脱速度
        double La, Lb, Ls, Lm; //流态判断
        double Uw, Ug, Um, Nrem; //粘度计算
        double NLv; //无因次液相速度NLv
        double Ngv; //无因次气相速度Ngv
        double Vm; //气液两相混合物流速  Vsl液, Vsg气
        double El; //无滑脱持液率
        double Nfr; //弗鲁德数
        double Mt;//气水总质量流量 Mt
        double Ql;//地面液体体积流量 Ql(m3/d)
        double delp1, delp2; //三种压降
        double Re; //雷诺数
        double f2x = 0; //计算f2的参数
        double f1r, f2r, f3r;
        double fr; // 相对摩阻因数, Mukberjee-brill方法
        double egx; //计算液膜粗糙度的x参数
        double eg; //液膜粗糙度
        double Wt; //质量流量
        double tF; //摩阻压力损失梯度
        double Nb; //速度雷诺数
        double Us1 = 0, Usi, ytfbxs, Pms, PmM, tFs, tFm;
        double c1, c2, c3, c4, c5, c6; //Mukberjee-brill方法计算hl的参数
        double Nvgsm; //流动型态判别准数,若Nvg >=N vgsm , 则为环雾流; 否则, 为泡状流- 段塞流.


        String JieShu = null, FlowState = null;
        double L1 = 0, L2 = 0, L3, L4;
        double g = 9.8, Tsc = 273.15;
        String zSF = "Hall-Yarbough";

        double P1 = Pstart, T1 = TStart;
        double H2 = H1 + Direction * DeltaH;

        double DeltaP = 9800 * DeltaH / 1000000;
        double Ap = pi * Math.pow(Dt / 1000,2) / 4; //油管截面积
        Dt = Dt / PressJZ;
        double P2 = P1 + Direction * DeltaP;
        if (P2 < 0) {
            P2 = 0;
        }
        double TmpXS = Math.pow(Dt / 1000, 2.67);
        Wf = 100 * (0.0034 + 0.79 * (Wm / 100)) / Math.pow(10, Qg * (WGR + OGR) / (86400 * 20 * TmpXS));
        //Wf流体温度梯度， K/（100m）
        T2 = T1 + Direction * Wf * DeltaH / 100;
        int Ncount;
        switch (Method) {
            case 0://Hagedorn-Brown
                Ncount = 0;
                while (true) {
                    Pav = (P1 + P2) / 2;
                    Tav = (T1 + T2) / 2;
                    if (Pav <= 0) {
                        JieShu = "结束";
                        P2 = 0;
                        break;
                    }
                    double Z = (double) QPCYZ_Z(zSF, Ppc, Tpc, Pav, Tav).get("Z");

                    //计算计算地面气水总质量流量 Mt(kg/m3)和地面液体体积流量 Ql(m3/d)
                    Mt = 1000 * Ro * OGR / (OGR + WGR) + 1000 * Rw * WGR / (OGR + WGR) + 1.205 * (WGR + OGR) / 10000 * Rg;
                    Ql = Qg * (OGR + WGR); //地面液体体积流量 (m3/d)
                    //Vsl = Qg * (OGR + WGR) * Rw / (86400 * Ap) / (Rw - 5.10546 * Math.pow(10, -4) * (Tav - 273) - 3.06254 * Math.pow(10, -6) * Math.sqrt(Tav - 273));
                    Vsl = Qg * (OGR + WGR) / (86400 * Ap);
                    //Vsg = Math.pow(10, 4) * Qg * (1.205 * z * Tav) / (3484.4 * Pav * (86400 * Ap));
                    Vsg = Math.pow(10, 4) * Qg / (86400 * Ap) * (0.101325 / Pav) * (Tav / 293.15) * Z;
                    Vm = Vsl + Vsg; //气液两相混合物流速
                    El = Vsl / Vm; //无滑脱持液率
                    Beta = 1.8 * (Tav - 273) + 32;
                    Ul = Math.exp(1.003 - 1.479 / 100 * Beta + 1.982 * Math.pow(10, -5) * Math.pow(Beta,2)); //水的粘度，mpa
                    //Ul=1;
                    //Thwg = 2.4 / 1000 * (410.8 - Tav) * 1.8 / 206 * (7 * Math.exp(-0.0362575 * Pav) - (52.5 - 0.87018 * Pav)) + 52.5 - 0.87018 * Pav; //气水的表面张力
                    Thwg = 0.062; //N.m
                    PL = 1000 * (Ro - 5.10546 * Math.pow(10, -4) * (Tav - 273 - 15) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273 - 15,2)) * (OGR / (OGR + WGR)) +
                            1000 * WGR / (OGR + WGR) * (Rw - 5.10546 * Math.pow(10, -4) * (Tav - 273 - 15) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273 - 15,2));
                    Pg = 1.205 * Rg * Pav / Tav * 2892 / Z;
                    A = 1.071 - 0.7272 * Math.pow(Vm,2) / (Dt / 1000);
                    B = Vsl / Vm;
                    Pn = PL * El + Pg * (1 - El);
                    if (A < 0.13) {
                        A = 0.13;
                    }
                    if (B >= A) {

                        NLv = 3.1775 * Vsl * Math.pow(PL / (1000 * Thwg), 0.25);
                        Ngv = 3.1775 * Vsg * Math.pow(Pg / (1000 * Thwg), 0.25);
                        //Ngv = 3.1775 * vsg * Math.pow(Pl / (1000 * Thwg), 0.25);
                        Nd = 99.045 * (Dt / 1000) * Math.pow(PL / (1000 * Thwg), 0.5);
                        //Nd = 99.045 * (Dt / 1000) * Math.pow(Pn / (1000 * Thwg), 0.5);
                        Nu = 0.3147 * Ul * Math.pow(1 / (PL * Math.pow(1000 * Thwg, 3)), 0.25); //液体粘度数
                        if (Nu <= 0.002) {
                            CNu = 0.0019;
                        } else if ((Nu > 0.002) && (Nu <= 0.04)) {
                            CNu = 10.952 * Math.pow(Nu,2) - 0.0581 * Nu + 0.0021;
                        } else if ((Nu > 0.04) && (Nu <= 0.1)) {
                            CNu = 0.0024 * Math.exp(10.462 * Nu);
                        } else if ((Nu > 0.1) && (Nu <= 0.5)) {
                            CNu = -0.05 * Math.pow(Nu,2) + 0.039 * Nu + 0.0031;
                        } else if (Nu > 0.5) {
                            CNu = 0.01;
                        }
                        PHI1 = NLv / Math.pow(Ngv, 0.575) * Math.pow((Pav / 0.101325), 0.1) * CNu / Nd;
                        if (PHI1 <= 8 * Math.pow(10, -7)) {
                            HLPHI = 0.0001;
                        } else if ((PHI1 > 8 * Math.pow(10, -7)) && (PHI1 <= 2 * Math.pow(10, -5))) {
                            HLPHI = 0.073 * ln(PHI1) + 1.0355;
                        } else if ((PHI1 > 2 * Math.pow(10, -5)) && (PHI1 <= 0.0001)) {
                            HLPHI = 1 * Math.pow(10, 7) * Math.pow(PHI1,2) + 410.72 * PHI1 + 0.1833;
                        } else if ((PHI1 > 0.0001) && (PHI1 <= 0.001)) {
                            HLPHI = 0.2073 * ln(PHI1) + 2.2417;
                        } else if ((PHI1 > 0.001) && (PHI1 <= 0.005)) {
                            HLPHI = -17143 * Math.pow(PHI1,2) + 140.86 * PHI1 + 0.68;
                        } else if (PHI1 > 0.005) {
                            HLPHI = 0.96;
                        }
                        PHI2 = Ngv * Math.pow(Nu, 0.38) / Math.pow(Nd, 2.14);
                        if (PHI2 <= 0.012) {
                            PHI = 1;
                        } else if ((PHI2 > 0.012) && (PHI2 <= 0.023)) {
                            PHI = 1893.91 * Math.pow(PHI2,2) - 48.106 * PHI2 + 1.3045;
                        } else if ((PHI2 > 0.023) && (PHI2 <= 0.04)) {
                            PHI = -504.2 * Math.pow(PHI2,2) + 55.294 * PHI2 + 0.195;
                        } else if ((PHI2 > 0.04) && (PHI2 <= 0.1)) {
                            PHI = -72.619 * Math.pow(PHI2,2) + 13.495 * PHI2 + 1.1758;
                        } else if (PHI2 > 0.1) {
                            PHI = 1.8;
                        }
                        HL = HLPHI * PHI;
                        Mg = Rg * 28.97; //天然气平均分子量，kg/kmol;
                        X = 3.5 + 986 / (1.8 * Tav) + 0.01 * Mg;
                        Y = 2.4 - 0.2 * X;
                        K = (9.4 + 0.02 * Mg) * Math.pow(1.8 * Tav, 1.5) / (209 + 19 * Mg + 1.8 * Tav);
                        Ug = Math.pow(10, -4) * K * Math.exp(X * Math.pow(Pg / 1000, Y)); //在给定温度和压力下天然气的粘度，mPa.s;
                        //Nrem = 1.474 / 100 * Ql * Mt /(Dt/1000 * (Math.pow(Ul, Hl) + Math.pow(Ug ,1 - Hl)));

                        Um = Ul * El + Ug * (1 - El);
                        Nrem = Pn * Vm * Dt / Um;
                        Fm = Math.pow(1.14 - 2 * Math.log(e / Dt + 21.25 / Math.pow(Nrem, 0.9)), -2);
                        Pm = PL * HL + Pg * (1 - HL); //混合物密度
                    } else {
                        //griffith方法计算持液率HL和摩阻系数fm和混合物密度Pm
                        Hg = 0.5 * (1 + Vm / 0.244 - Math.pow(Math.pow(1 + Vm / 0.244,2) - 4 * Vsg / 0.244,2)); //Hg含气率
                        HL = 1 - Hg; //持液率HL
                        Pg = 3484.4 * Rg * Pav / (Z * Tav); //在给定温度和压力下天然气的密度，kg/m3
                        Mg = Rg * 28.97; //天然气平均分子量，kg/kmol;
                        X = 3.5 + 986 / (1.8 * Tav) + 0.01 * Mg;
                        Y = 2.4 - 0.2 * X;
                        K = (9.4 + 0.02 * Mg) * Math.pow(1.8 * Tav, 1.5) / (209 + 19 * Mg + 1.8 * Tav);
                        Ug = Math.pow(10, -4) * K * Math.exp(X * Math.pow(Pg / 1000, Y)); //在给定温度和压力下天然气的粘度，mPa.s;
                        //Nrem = 1.474 / 100 * Ql * Mt / (Dt * (Ul * Hl + Ug * (1 - Hl)));
                        Pn = PL * El + Pg * (1 - El);
                        Um = Ul * El + Ug * (1 - El);
                        Nrem = Pn * Vm * Dt / Um;
                        //Nrem = 1.474 / 100 * Ql * Mt /(Dt/1000 * (Math.pow(Ul, Hl) + Math.pow(Ug ,1 - Hl)));
                        if (Nrem > 2300) {
                            Fm = Math.pow(1.14 - 2 * Math.log(e / Dt + 21.25 / Math.pow(Nrem, 0.9)), -2);
                        } else {
                            Fm = Nrem / 64;
                        }
                        Pm = PL * HL + Pg * (1 - HL); //混合物密度
                        Nu = 0.3147 * Ul * Math.pow(1 / (PL * Math.pow(Thwg, 3)), 0.25);
                    }
                    //fm = PressJZ * fm;
                    //P2c = P1 + Direction * Math.pow(10, -6) * (Pm * g * Math.sin(Angle) + Fm * Ql * Math.sqrt(Mt) / (9.21 * Math.pow(10, 9) * Pm * Math.pow(Dt, 5))) * DeltaH;
                    if (Pm < Pn) {
                        Pm = Pn;
                    }
                    P2c = P1 + Direction * Math.pow(10, -6) * ((Pm * g * Math.sin(Angle) + Fm * Pn * Math.pow(Vm,2) / (2.0 / 1000 * Dt)) / (1 - Pm * Vm * Vsg / (Pav * Math.pow(10, 6)))) * DeltaH;

                    La = Vsg / Vm;
                    Lb = 1.071 - 0.7272 * Math.pow(Vm,2) / (Dt / 1000);
                    if (Lb < 0.13) {
                        Lb = 0.13;
                    }
                    Ngv = Vsg * Math.pow(PL / (g * Thwg), 0.25);
                    //Ngv = 3.1775 * vsg * Math.pow(Pg / (1000 * Thwg), 0.25);
                    Ls = 50 + 36 * Vsl / Vsg * Ngv; //50改成0，36改成22
                    Lm = 75 + 84 * Math.pow(Vsl / Vsg * Ngv, 0.75); //75改成0，84改成19
                    if (La < Lb) {
                        FlowState = "泡状流";
                    } else if ((La >= Lb) && (Ngv < Ls)) {
                        FlowState = "段塞流";
                    } else if ((Lm > Ngv) && (Ngv >= Ls)) {
                        FlowState = "过渡流";
                    } else if (Lm <= Ngv) {
                        FlowState = "雾状流";
                    }
                    Flow = (FlowState);
                    if (Math.abs(P2 - P2c) <= 0.001) {
                        break;
                    } else {
                        P2 = P2c;
                        Ncount++;
                        if (Ncount > 500) {
                            break;
                        }
                    }
                }

                break;
            case 1://Begss-Brill
                Ncount = 0;
                while (true) {
                    Pav = (P1 + P2) / 2;
                    Tav = (T1 + T2) / 2;
                    if (Pav <= 0) {
                        JieShu = "结束";
                        P2 = 0;
                        break;
                    }
                    double Z = (double) QPCYZ_Z(zSF, Ppc, Tpc, Pav, Tav).get("Z");
                    //Vsl = Qg * (OGR + WGR) * Rw / (86400 * Ap) / (Rw - 5.10546 * Math.pow(10, -4) * (Tav - 273) - 3.06254 * Math.pow(10, -6) * sqr(Tav - 273));
                    Vsl = Qg * (OGR + WGR) / (86400 * Ap);
                    //Vsg = Math.pow(10, 4) * Qg * (1.205 * z * Tav) / (3484.4 * Pav * (86400 * Ap));
                    Vsg = Math.pow(10, 4) * Qg / (86400 * Ap) * (0.101325 / Pav) * (Tav / 293.15) * Z;
                    Vm = Vsl + Vsg; //气液两相混合物流速
                    //计算无滑脱持液率El 和弗鲁德数Nfr
                    //El = (WGR + OGR) / (WGR + OGR + 10000);
                    El = Vsl / Vm;
                    //Nfr = sqr(Vm) / g * (Dt / 1000);
                    Nfr = Math.pow(Vm, 2) / (g * Dt / 1000);
                    //计算无因次液相粘度Nl 、无因次液相速度NLv 和无因次气相速度Ngv
                    //PL = Rw * 1000; //液体密度Kg/m^3
                    PL =
                            1000 * (Ro - 5.10546 * Math.pow(10, -4) * (Tav - 273 - 15) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273 - 15, 2)) * (OGR / (OGR + WGR)) +
                                    1000 * WGR / (OGR + WGR) * (Rw - 5.10546 * Math.pow(10, -4) * (Tav - 273 - 15) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273 - 15, 2));
                    Pg = 1.205 * Rg * Pav / Tav * 2892 / Z;
                    //Thog = 1 / Math.pow(10, 1.58 + 0.05 * Pav) - 72 * Math.pow(10, -6) * (Tav - 305); //油气表面张力N.m
                    //if thog < 0 then
                    thog = 0.051;
                    //Thwg = Math.pow(10, -3) * ((410.8 - Tav) * 1.8 / 206 * (7 * Exp(-0.0362575 * Pav) - (52.5 - 0.87018 * Pav)) + (52.5 - 0.87018 * Pav)); //水气表面张力N.m
                    //if Thwg < 0 then
                    Thwg = 0.062;
                    Thtotal = WGR / (WGR + OGR) * Thwg + OGR / (WGR + OGR) * thog;
                    //NLv = Vsl * Math.pow(PL / (g * Thtotal), 0.25);
                    NLv = 3.1775 * Vsl * Math.pow(PL / (1000 * Thtotal), 0.25);
                    //Ngv = Vsg * Math.pow(PL / (g * Thtotal), 0.25);
                    Ngv = 3.1775 * Vsg * Math.pow(Pg / (1000 * Thwg), 0.25);
                    //Nl = Vsl * (Math.pow(g / (Pl * Math.pow(Thtotal, 3)), 3));
                    //计算流型判断数L1,L2,L3,L4并判断流型
                    L1 = 316 * Math.pow(El, 0.302);
                    L2 = 0.0009252 * Math.pow(El, -2.4684);
                    L3 = 0.10 * Math.pow(El, -1.4516);
                    L4 = 0.5 * Math.pow(El, -6.738);
                    //流态
                    if ((El < 0.01) && (Nfr < L1) || (El >= 0.01) && (Nfr < L2)) {
                        FlowState = "分异型";//Segregrated Flow
                    } else if ((El >= 0.01) && (Nfr > L2) && (Nfr <= L3)) {
                        FlowState = "过渡型";//Transition Flow
                    } else if ((El >= 0.01) && (El < 0.4) && (Nfr > L3) && (Nfr <= L1)
                            || (El >= 0.4) && (Nfr > L3) && (Nfr <= L4)) {
                        FlowState = "间隔型";//Intermittent Flow
                    } else if ((El < 0.4) && (Nfr >= L1) || (El >= 0.4) && (Nfr > L4)) {
                        FlowState = "分散型"; //Distributed Flow
                    }
                    //计算持液率HL及混合物密度Pm (Kg/m3)
                    //持液率计算
                    if (Angle == 0) { //水平方向持液率计算方法

                        HLSeg = 0.98 * Math.pow(El, 0.4846) / Math.pow(Nfr, 0.0868); // A= 0.98;  B= 0.4846;  C= 0.0868;
                        HLInter = 0.845 * Math.pow(El, 0.5351) / Math.pow(Nfr, 0.0173); // A= 0.845;  B= 0.5351;  C= 0.0173;
                        HLDistri = 1.065 * Math.pow(El, 0.5824) / Math.pow(Nfr, 0.0609); // A= 1.065;  B= 0.5824;  C= 0.0609;
                        if ("分异型".equals(FlowState)) {
                            HL = HLSeg;
                        } else if ("间隔型".equals(FlowState)) {
                            HL = HLInter;
                        } else if ("分散型".equals(FlowState)) {
                            HL = HLDistri;
                        }
                    } else {
                        if (Direction == -1) { //向上流动

                            if ("分异型".equals(FlowState)) {
                                C = (1 - El) * ln(0.011 * Math.pow(El, -3.768) * Math.pow(NLv, 3.539) * Math.pow(Nfr, -1.614));
                            } else if ("间隔型".equals(FlowState)) {
                                C = (1 - El) * ln(2.96 * Math.pow(El, 0.305) * Math.pow(NLv, -0.4473) * Math.pow(Nfr, 0.0978));
                            } else if ("分散型".equals(FlowState)) {
                                C = 0;
                            }
                        } else {
                            C = (1 - El) * ln(4.7 * Math.pow(El, -0.3692) * Math.pow(NLv, 0.1244) * Math.pow(Nfr, -0.5056));
                        }
                        if (C < 0) {
                            C = 0;
                        }
                        AngelXZ = 1 + C * (Math.sin(1.8 * Angle) - 1 / 3 * Math.pow(Math.sin(1.8 * Angle), 3)); //角度修正
                        if (AngelXZ < 0) {
                            AngelXZ = 0;
                        }
                        HLSeg = 0.98 * Math.pow(El, 0.4846) / Math.pow(Nfr, 0.0868); // A= 0.98;  B= 0.4846;  C= 0.0868;
                        HLInter = 0.845 * Math.pow(El, 0.5351) / Math.pow(Nfr, 0.0173); // A= 0.845;  B= 0.5351;  C= 0.0173;
                        HLDistri = 1.065 * Math.pow(El, 0.5824) / Math.pow(Nfr, 0.0609); // A= 1.065;  B= 0.5824;  C= 0.0609;
                        if ("分异型".equals(FlowState)) {
                            HLangle = HLSeg;
                        } else if ("间隔型".equals(FlowState)) {
                            HLangle = HLInter;
                        } else if ("分散型".equals(FlowState)) {
                            HLangle = HLDistri;
                        }
                        if (HLangle < El) {
                            HLangle = El;
                        }
                        HL = HLangle * AngelXZ;
                    }
                    if ("过渡型".equals(FlowState)) {
                        HL =
                                (L3 - Nfr) / (L3 - L2) * HLSeg + (Nfr - L2) / (L3 - L2) * HLInter;
                    } //P185采气工程基础
                    if (Angle == 0) {
                        if (HL < El) {
                            HL = El;
                        }
                    }//P185采气工程基础
                    if (HL < 0) {
                        HL = 0;
                    }
                    if (HL > 1) {
                        HL = 1;
                    }
                    Pm = PL * HL + Pg * (1 - HL); //混合物密度
                    if ((HL == 0) || (HL == 1)) {
                        S = 0;
                    } else {
                        Y = El / Math.pow(HL, 2);
                        if ((Y > 1) && (Y < 1.2)) {
                            S = ln(2.2 * Y - 1.2);
                        } else {
                            S = ln(Y) / (-0.0523 + 3.182 * ln(Y) - 0.8725 * Math.pow(ln(Y), 2) + 0.01853 * Math.pow(ln(Y), 4));
                        }
                    }
                    ;
                    Fmn = Math.exp(S); //计算摩阻系数
                    //计算无滑脱摩阻系数fn
                    Pn = PL * El + Pg * (1 - El); //无滑脱两相密度
                    //Ug = GasNd(Rg, Tav, Ppc, Tpc, Pav, Z, PChar('Dempsey'));
                    Mg = Rg * 28.97; //天然气平均分子量，kg/kmol;
                    X = 3.5 + 986 / (1.8 * Tav) + 0.01 * Mg;
                    Y = 2.4 - 0.2 * X;
                    K = (9.4 + 0.02 * Mg) * Math.pow(1.8 * Tav, 1.5) / (209 + 19 * Mg + 1.8 * Tav);
                    Ug = Math.pow(10, -4) * K * Math.exp(X * Math.pow(Pg / 1000, Y)); //在给定温度和压力下天然气的粘度，mPa.s;
                    Uw = WaterND(Tav - Tsc); //用摄氏度计算     //水粘度的计算mpa.s
                    //Um = Uw * HL + Ug * (1 - HL); //混合物粘度
                    Um = Uw * El + Ug * (1 - El);
                    //Nrem = Pn * Vm * dt / Um; //雷诺数 P123      原来* 1000，这里不知道需不需要
                    Nrem = Pn * Vm * Dt / Um;
                    Fn = 0.0056 + 0.5 / Math.pow(Nrem, 0.32);
                    Fm = Fmn * Fn; //摩阻系数    PressJZ
                    if (Pm < Pn) {
                        Pm = Pn;
                    }
                    P2c =
                            P1 + Direction * Math.pow(10, -6) * ((Pm * g * Math.sin(Angle) + Fm * Pn * Math.pow(Vm, 2) / (2 / 1000 * Dt)) / (1 - Pm * Vm * Vsg / (Pav * Math.pow(10, 6)))) * DeltaH;
                    //if (Direction=-1) and (P2c>P1) then break;
                    Flow = (FlowState);
                    if (Math.abs(P2 - P2c) <= 0.001) {
                        break;
                    } else {
                        P2 = P2c;
                    }
                    Ncount++;
                    if (Ncount > 500) {
                        break;
                    }
                }
                break;
            case 2://Duns-Ros=2
                Ncount = 0;
                Dt = Dt / 1000;
                while (true) {
                    Pav = (P1 + P2) / 2;
                    Tav = (T1 + T2) / 2;
                    if (Pav <= 0) {

                        JieShu = "结束";
                        P2 = 0;
                        break;
                    }
                    double Z = (double) QPCYZ_Z(zSF, Ppc, Tpc, Pav, Tav).get("Z");
                    //计算计算地面气水总质量流量 Mt(kg/m3)和地面液体体积流量 Ql(m3/d)
                    Mt =
                            1000 * Ro * OGR / (OGR + WGR) + 1000 * Rw * WGR / (OGR + WGR) + 1.205 * (WGR + OGR) / 10000 * Rg;
                    Ql = Qg * (OGR + WGR); //地面液体体积流量 (m3/d)
                    Vsl =
                            Qg * (OGR + WGR) * Rw / (86400 * Ap) / (Rw - 5.10546 * Math.pow(10, -4) * (Tav - 273) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273, 2));
                    //Vsl = Qg * (OGR + WGR) / (86400 * Ap);
                    Vsg = Math.pow(10, 4) * Qg * (1.205 * Z * Tav) / (3484.4 * Pav * (86400 * Ap));
                    //Vsg = Math.pow(10, 4) * Qg / (86400 * Ap) * (0.101325 / Pav) * (Tav / 293.15) * Z;
                    Vm = Vsl + Vsg; //气液两相混合物流速
                    //密度计算
                    PL =
                            1000 * (Ro - 5.10546 * Math.pow(10, -4) * (Tav - 273 - 15) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273 - 15, 2)) * (OGR / (OGR + WGR)) +
                                    1000 * WGR / (OGR + WGR) * (Rw - 5.10546 * Math.pow(10, -4) * (Tav - 273 - 15) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273 - 15, 2));
                    Pg = 1.205 * Rg * Pav / Tav * 2892 / Z;

                    //Thog = 1 / Math.pow(10, 1.58 + 0.05 * Pav) - 72 * Math.pow(10, -6) * (Tav - 305); //油气表面张力N.m
                    //if thog < 0 then
                    thog = 0.051;
                    //Thwg = Math.pow(10, -3) * ((410.8 - Tav) * 1.8 / 206 * (7 * Exp(-0.0362575 * Pav) - (52.5 - 0.87018 * Pav)) + (52.5 - 0.87018 * Pav)); //水气表面张力N.m
                    //if Thwg < 0 then
                    Thwg = 0.062;
                    Thtotal = WGR / (WGR + OGR) * Thwg + OGR / (WGR + OGR) * thog;
                    NLv = Vsl * Math.pow(PL / (g * Thtotal), 0.25);
                    //Nlv = 3.1775 * Vsl * Math.pow(Pl / (1000 * Thtotal), 0.25);
                    Ngv = Vsg * Math.pow(PL / (g * Thtotal), 0.25);
                    //Ngv = 3.1775 * vsg * Math.pow(Pg / (1000 * Thwg), 0.25);
                    Nd = Dt * Math.pow(PL / (g * Thtotal), 0.5);
                    Beta = 1.8 * (Tav - 273) + 32;
                    Ul = Math.exp(1.003 - 1.479 / 100 * Beta + 1.982 * Math.pow(10, -5) * Math.pow(Beta, 2)); //水的粘度，mpa
                    //Ul=1;
                    Mg = Rg * 28.97; //天然气平均分子量，kg/kmol;
                    X = 3.5 + 986 / (1.8 * Tav) + 0.01 * Mg;
                    Y = 2.4 - 0.2 * X;
                    K = (9.4 + 0.02 * Mg) * Math.pow(1.8 * Tav, 1.5) / (209 + 19 * Mg + 1.8 * Tav);
                    Ug = Math.pow(10, -4) * K * Math.exp(X * Math.pow(Pg / 1000, Y)); //在给定温度和压力下天然气的粘度，mPa.s;
                    Nl = Ul * Math.pow(g / (PL * Math.pow(Thtotal, 8)), 0.25) / 1000; //液相粘度准数
                    //L1、L2的计算
                    if (Nd <= 40) {
                        L1 = 2;
                    } else if ((Nd > 40) && (Nd <= 70)) {
                        L1 = (-3.7225 * Math.log(Nd) + 7.9538);
                    } else if ((Nd > 70) && (Nd <= 80)) {
                        L1 = (-1.7244 * Math.log(Nd) + 4.2816);
                    } else if (Nd > 80) {
                        L1 = 1;
                    }


                    if (Nd <= 22) {
                        L2 = 0.48;
                    } else if ((Nd > 22) && (Nd <= 70)) {
                        L2 = 0.9328 * Math.pow(Math.log(Nd), 2) - 1.697 * Math.log(Nd) + 1.0657;
                    } else if (Nd > 70) {
                        L2 = 1.1;
                    }

                    if (Ngv <= (L1 + L2 * NLv)) { //第一区

                        FlowState = "液相连续区";
                        //F1的计算
                        if (Nl <= 0.04) {
                            F1 = 1.3;
                        } else if ((Nl > 0.04) && (Nl <= 0.6)) {
                            F1 =
                                    Math.pow(10, (-0.4882 * Math.log(Nl) * Math.log(Nl) - 0.7194 * Math.log(Nl) + 0.0651));
                        } else if ((Nl > 0.6) && (Nl <= 2.5)) {
                            F1 =
                                    Math.pow(10, (0.4936 * Math.log(Nl) * Math.log(Nl) - 0.4743 * Math.log(Nl) + 0.0711));
                        } else if (Nl > 2.5) {
                            F1 = 0.9;
                        }
                        //F2的计算
                        if (Nl <= 0.02) {
                            F2 = 0.24;
                        } else if ((Nl > 0.02) && (Nl <= 0.03)) {
                            F2 = Math.pow(10, (0.3802 * Math.log(Nl) + 0.0261));
                        } else if ((Nl > 0.03) && (Nl <= 0.09)) {
                            F2 = Math.pow(10, (1.1046 * Math.log(Nl) + 1.124));
                        } else if ((Nl > 0.09) && (Nl <= 1.2)) {
                            F2 = Math.pow(10, (-0.2844 * Math.log(Nl) * Math.log(Nl) - 0.3495 * Math.log(Nl) - 0.0932));
                        } else if (Nl > 1.2) {
                            F2 = 0.75;
                        }
                        //F3的计算
                        if (Nl <= 0.004) {
                            F3 = 0.85;
                        } else if ((Nl > 0.004) && (Nl <= 0.005)) {
                            F3 = Math.pow(10, (0.3546 * Math.log(Nl) + 0.7798));
                        } else if ((Nl > 0.005) && (Nl <= 2)) {
                            F3 =
                                    Math.pow(10, (-0.1334 * Math.log(Nl) * Math.log(Nl) - 0.0221 * Math.log(Nl) + 0.6221));
                        } else if (Nl > 2) {
                            F3 = 4.2;
                        }
                        //F4的计算
                        if (Nl <= 0.003) {
                            F4 = -4;
                        } else if ((Nl > 0.003) && (Nl <= 0.08)) {
                            F4 =
                                    (-10.922 * Math.log(Nl) * Math.log(Nl) + 1.5558 * Math.log(Nl) + 69.994);
                        } else if (Nl > 0.08) {
                            F4 = 55;
                        } //0.8还是0.08

                        F3 = (F3 - F4 / Nd);
                        Ns = F1 + F2 * NLv + F3 * Math.pow(Ngv / (1 + NLv), 2); //滑脱速度准数计算
                        Us = Ns / Math.pow(PL / (g * Thtotal), 0.25); //滑脱速度计算
                        HL = 1 - ((Us + Vsl + Vsg) - Math.sqrt(Math.pow((Us + Vsl + Vsg), 2) - 4 * Us * Vsg)) / 2 / Us;
                        //2 重位压降=((1-hl)*Pg+hl*pl)*g*DeltaH;
                        delp2 = ((1 - HL) * Pg + HL * PL) * g * DeltaH / 1000000;

                        //1 摩阻压降计算
                        Re = (Dt * Vsl * PL / Ul) * 1000; //雷诺数计算
                        f2x = (Vsg / Vsl * Math.pow(Nd, 2 / 3));

                        Integer Result = (Integer) gnn_calf1r(Re, e, Dt, f2x).get("Result");
                        f1r = (double) gnn_calf1r(Re, e, Dt, f2x).get("f1r");
                        f2r = (double) gnn_calf1r(Re, e, Dt, f2x).get("f2r");

                        if (Result != 1) {
                            break;
                        }//单相范宁系数f1、f2的计算
                        f3r = 1 + f1r * Math.sqrt(Vsg / 50 / Vsl);
                        fr = (f1r * f2r / f3r);
                        delp1 = (2 * fr * Vsg * Vsg * PL / Dt * (1 + Vsg / Vsl) * DeltaH) / 1000000; //摩阻压降

                        P2c = P1 + Direction * (delp1 + delp2);
                        Flow = FlowState;
                        Pm = PL * HL + Pg * (1 - HL); //混合物密度
                        if (Math.abs(P2 - P2c) <= 0.001) {
                            break;
                        } else {
                            P2 = P2c;
                        }
                        Ncount++;
                        if (Ncount > 500) {
                            break;
                        }

                    } else if ((Ngv >= (L1 + L2 * NLv)) && (Ngv <= (50 + 36 * NLv))) { //第二区

                        FlowState = "气液两相连续区";
                        //F5的计算
                        if (Nl <= 0.02) {
                            F5 = 0.22;
                        } else if ((Nl > 0.02) && (Nl <= 0.05)) {
                            F5 =
                                    Math.pow(10, (-0.0151 * Math.log(Nl) * Math.log(Nl) - 0.1754 * Math.log(Nl) - 1.0115));
                        } else if ((Nl > 0.05) && (Nl <= 0.4)) {
                            F5 =
                                    Math.pow(10, (-0.9308 * Math.pow(Math.log(Nl), 3) - 0.6945 * Math.log(Nl) * Math.log(Nl) + 0.4673 * Math.log(Nl) - 1.0184));
                        } else if ((Nl > 0.4) && (Nl <= 3)) {
                            F5 =
                                    Math.pow(10, (-0.6163 * Math.log(Nl) * Math.log(Nl) + 0.4134 * Math.log(Nl) - 1.0165));
                        } else if (Nl > 3) {
                            F5 = 0.11;
                        }
                        //F6的计算
                        if (Nl <= 0.02) {
                            F6 = 0.8;
                        } else if ((Nl > 0.02) && (Nl <= 0.032)) {
                            F6 =
                                    (2.5231 * Math.pow(Math.log(Nl), 4) + 22.045 * Math.pow(Math.log(Nl), 3) + 72.375 * Math.pow(Math.log(Nl), 2) + 104.75 * Math.log(Nl) + 55.862);
                        } else if ((Nl > 0.032) && (Nl <= 0.1)
                        ) {
                            F6 = (-2.0984 * Math.pow(Math.log(Nl), 2) - 1.0359 * Math.log(Nl) * +3.0787);
                        } else if ((Nl > 0.1) && (Nl <= 3)) {
                            F6 =
                                    (-0.762 * Math.pow(Math.log(Nl), 4) + 0.3827 * Math.pow(Math.log(Nl), 3) + 1.2881 * Math.pow(Math.log(Nl), 2) - 0.1929 * Math.log(Nl) + 1.6374);
                        } else if (Nl > 3) {
                            F6 = 0.11;
                        }
                        //F7的计算
                        if (Nl <= 0.02) {
                            F7 = 0.13;
                        } else if ((Nl > 0.02) && (Nl <= 3)) {
                            F7 =
                                    Math.pow(10, (0.0352 * Math.pow(Math.log(Nl), 3) + 0.1619 * Math.pow(Math.log(Nl), 2) - 0.0999 * Math.log(Nl) - 1.6442));
                        } else if (Nl > 3) {
                            F7 = 0.022;
                        }
                        F6 = 0.029 * Nd + F6;
                        Ns = (1 + F5) * (Math.pow(Ngv, 0.982) + F6) / Math.pow((1 + F7 * NLv), 2); //滑脱速度准数计算
                        Us = Ns / Math.pow(PL / (g * Thtotal), 0.25); //滑脱速度计算
                        HL = 1 - ((Us + Vsl + Vsg) - Math.sqrt(Math.pow((Us + Vsl + Vsg), 2) - 4 * Us * Vsg)) / 2 / Us;
                        //2 重位压降=((1-hl)*Pg+hl*pl)*g*DeltaH;
                        delp2 = ((1 - HL) * Pg + HL * PL) * g * DeltaH / 1000000;

                        //1 摩阻压降计算
                        Re = (Dt * Vsl * PL / Ul) * 1000; //雷诺数计算
                        f2x = (Vsg / Vsl * Math.pow(Nd, 2 / 3));

                        Integer Result = (Integer) gnn_calf1r(Re, e, Dt, f2x).get("Result");
                        f1r = (double) gnn_calf1r(Re, e, Dt, f2x).get("f1r");
                        f2r = (double) gnn_calf1r(Re, e, Dt, f2x).get("f2r");

                        if (Result != 1) {
                            break;
                        }//单相范宁系数f1、f2的计算
                        f3r = 1 + f1r * Math.sqrt(Vsg / 50 / Vsl);
                        fr = (f1r * f2r / f3r);
                        delp1 = (2 * fr * Vsg * Vsg * PL / Dt * (1 + Vsg / Vsl) * DeltaH) / 1000000; //摩阻压降

                        P2c = P1 + Direction * (delp1 + delp2);
                        Flow = (FlowState);
                        Pm = PL * HL + Pg * (1 - HL); //混合物密度
                        if (Math.abs(P2 - P2c) <= 0.001) {
                            break;
                        } else {
                            P2 = P2c;
                        }
                        Ncount++;
                        if (Ncount > 500) {
                            break;
                        }

                    } else if (Ngv > (75 + 84 * Math.pow(NLv, 0.78)) * 50 + 36 * NLv) { //第三区
//TODO:        else if (Ngv > {75 + 84 *Math.pow(nlv,0.78)} 50 + 36 * nlv) then //第三区

                        FlowState = "气相连续区";
                        HL = 1 - 1 / (1 + Ql / Qg / 10000);
                        //2 重位压降=((1-HL)*Pg+HL*PL)*g*DeltaH;
                        delp2 = ((1 - HL) * Pg + HL * PL) * g * DeltaH / 1000000;

                        //1 摩阻压降计算
                        Re = (Dt * Vsg * Pg / Ug) * 1000; //气相雷诺数计算
                        egx = (Pg * Vsg * Vsg * Ul * Ul / PL / Math.pow(Thtotal, 2)) / 1000000;
                        //液膜粗糙度计算
                        if ((egx >= 0.000101859) && (egx <= 0.004655861)) {
                            eg = (31.47748314 - 33.11311215) / (0.004655861 - 0.000101859) * (egx - 0.004655861) + 31.47748314;
                        }
                        if ((egx >= 0.004655861) && (egx <= 10.28016298)) {
                            eg = (284.4461107 - 31.47748314) / (10.28016298 - 0.004655861) * (egx - 0.004655861) + 31.47748314;
                        }


                        Integer Result = (Integer) gnn_calf1r(Re, e, Dt, f2x).get("Result");
                        f1r = (double) gnn_calf1r(Re, e, Dt, f2x).get("f1r");
                        f2r = (double) gnn_calf1r(Re, e, Dt, f2x).get("f2r");
                        if (Result != 1) {
                            break;
                        } //单相范宁系数f1、f2的计算
                        f3r = 1 + f1r * Math.sqrt(Vsg / 50 / Vsl);
                        fr = (f1r * f2r / f3r);
                        delp1 = Direction * (2 * fr * Vsg * Vsg * PL / Dt * (1 + Vsg / Vsl) * DeltaH) / 1000000; //摩阻压降

                        P2c = P1 + Direction * (delp1 + delp2) / (1 - (PL * Vsl + Pg * Vsg) * Vsg / (P1 * 1000000));
                        Flow = FlowState;
                        Pm = PL * HL + Pg * (1 - HL); //混合物密度
                        if (Math.abs(P2 - P2c) <= 0.001) {
                            break;
                        } else {
                            P2 = P2c;
                        }
                        Ncount++;
                        if (Ncount > 500) {
                            break;
                        }
                    }
                    ;

                }
                break;
            case 3: //Okiszewski//
                Ncount = 0;
                while (true) {
                    Pav = (P1 + P2) / 2;
                    Tav = (T1 + T2) / 2;
                    if (Pav <= 0) {

                        JieShu = "结束";
                        P2 = 0;
                        break;
                    }
                    double Z = (double) QPCYZ_Z(zSF, Ppc, Tpc, Pav, Tav).get("Z");
//                    QPCYZ_Z(zSF, Ppc, Tpc, Pav, Tav, Z);
                    //计算计算地面气水总质量流量 Mt(kg/m3)和地面液体体积流量 Ql(m3/d)
                    Mt = 1000 * Ro * OGR / (OGR + WGR) + 1000 * Rw * WGR / (OGR + WGR) + 1.205 * Rg;
                    //Wt = (1000 * Ro * OGR / (OGR + WGR) * (QG * OGR) + 1000 * Rw * WGR / (OGR + WGR) * (QG * WGR) + 1.205 * Rg * (10000 * Qg)) / 86400;
                    Wt = (1000 * Ro * (Qg * OGR) + 1000 * Rw * (Qg * WGR) + 1.205 * Rg * (10000 * Qg)) / 86400;
                    Ql = Qg * (OGR + WGR); //地面液体体积流量 (m3/d)
                    //Vsl = Qg * (OGR + WGR) * Rw / (86400 * Ap) / (Rw - 5.10546 * Math.pow(10, -4) * (Tav - 273) - 3.06254 * Math.pow(10, -6) * sqr(Tav - 273));
                    Vsl = Qg * (OGR + WGR) / (86400 * Ap);
                    //Vsg = Math.pow(10, 4) * Qg * (1.205 * z * Tav) / (3484.4 * Pav * (86400 * Ap));
                    Vsg = Math.pow(10, 4) * Qg / (86400 * Ap) * (0.101325 / Pav) * (Tav / 293.15) * Z;
                    Vm = Vsl + Vsg; //气液两相混合物流速
                    El = Vsl / Vm;
                    Beta = 1.8 * (Tav - 273) + 32;
                    Ul = Math.exp(1.003 - 1.479 / 100 * Beta + 1.982 * Math.pow(10, -5) * Math.pow(Beta, 2)); //水的粘度，mpa.s
                    Mg = Rg * 28.97; //天然气平均分子量，kg/kmol;
                    X = 3.5 + 986 / (1.8 * Tav) + 0.01 * Mg;
                    Y = 2.4 - 0.2 * X;
                    K = (9.4 + 0.02 * Mg) * Math.pow(1.8 * Tav, 1.5) / (209 + 19 * Mg + 1.8 * Tav);
                    Pg = 1.205 * Rg * Pav / Tav * 2892 / Z;
                    Ug = Math.pow(10, -4) * K * Math.exp(X * Math.pow(Pg / 1000, Y)); //在给定温度和压力下天然气的粘度，mPa.s;
                    //Ug = GasNd(Rg, Tav, Ppc, Tpc, Pav, Z, PChar('Dempsey'));
                    //Thwg = 2.4 / 1000 * (410.8 - Tav) * 1.8 / 206 * (7 * exp(-0.0362575 * Pav) - (52.5 - 0.87018 * Pav)) + 52.5 - 0.87018 * Pav; //气水的表面张力
                    //if Thwg <= 0 then Thwg = 0.00001;
                    Thwg = 0.062;
                    PL =
                            1000 * (Ro - 5.10546 * Math.pow(10, -4) * (Tav - 273 - 15) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273 - 15, 2)) * (OGR / (OGR + WGR)) +
                                    1000 * WGR / (OGR + WGR) * (Rw - 5.10546 * Math.pow(10, -4) * (Tav - 273 - 15) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273 - 15, 2));

                    La = Vsg / Vm;
                    Lb = 1.071 - 0.7277 * Math.pow(Vm, 2) / (Dt / 1000);
                    if (Lb < 0.13) {
                        Lb = 0.13;
                    }
                    Ngv = Vsg * Math.pow(PL / (g * Thwg), 0.25);
                    //Ngv = 3.1775 * vsg * Math.pow(Pg / (1000 * Thwg), 0.25);
                    //Ls = 0 + 22 * Vsl / Vsg * Ngv; //50改成0，36改成22
                    //Lm = 0 + 19 * Math.pow(Vsl / Vsg * Ngv, 0.75); //75改成0，84改成19
                    Ls = 50 + 36 * Vsl / Vsg * Ngv;
                    Lm = 75 + 84 * Math.pow(Vsl / Vsg * Ngv, 0.75);
                    if (La < Lb) {

                        FlowState = "泡状流";
                        Hg = 0.5 * (1 + Vm / 0.244 - Math.sqrt(Math.pow(1 + Vm / 0.244, 2) - 4 * Vsg / 0.244)); //Hg含气率
                        HL = 1 - Hg; //持液率HL
                        //Nrem = PL * Dt * Vm / Ul;
                        Pn = PL * El + Pg * (1 - El);
                        Um = Ul * El + Ug * (1 - El);
                        Nrem = Pn * Vm * Dt / Um;
                        if (Nrem > 2300) {
                            Fm = Math.pow(1.14 - 2 * Math.log10(e / Dt + 21.25 / Math.pow(Nrem, 0.9)), -2);
                        } //液相摩阻系数
                        else {
                            Fm = Nrem / 64;
                        }//液相摩阻系数
                        //fm = PressJZ * fm;
                        tF = Fm * PL / (2 * Dt / 1000) * Math.pow(Vsl / HL, 2); //摩阻压力损失梯度
                        Pm = PL * HL + Pg * (1 - HL); //混合物密度
                    } else if ((La >= Lb) && (Ngv < Ls)) {

                        FlowState = "段塞流";
                        Us = 0.1; //滑脱速度
                        //Nrem = PL * Dt * Vm / Ul;
                        Pn = PL * El + Pg * (1 - El);
                        Um = Ul * El + Ug * (1 - El);
                        Nrem = Pn * Vm * Dt / Um;
                        while (true) {
                            Nb = PL * Dt * Us / Ul; //速度雷诺数
                            if (Nb <= 3000) {
                                Us1 = (0.546 + 8.74 * Math.pow(10, -6) * Nrem) * Math.sqrt(g * Dt / 1000);
                            } else if ((Nb > 3000) && (Nb < 8000)) {

                                Usi = (0.251 + 8.74 * Math.pow(10, -6) * Nrem) * Math.sqrt(g * Dt / 1000);
                                Us1 = 0.5 * (Usi + Math.sqrt(Math.pow(Usi, 2) + 11170 * Ul / 1000 / (PL * Math.sqrt(Dt / 1000))));
                            } else if (Nb >= 8000) {
                                Us1 = (0.35 + 8.74 * Math.pow(10, -6) * Nrem) * Math.sqrt(g * Dt / 1000);
                            }
                            if (Math.abs(Us - Us1) <= 0.0001) {
                                break;
                            }
                            Us = Us1;
                        }
                        //Us=0.244;
                        //采油工程P39
                        if (Vm <= 3.048) {

                            ytfbxs =
                                    0.00252 * Math.log10(Ul) / Math.pow(Dt / 1000, 1.38) - 0.782 + 0.232 * Math.log10(Vm) - 0.428 * Math.log10(Dt / 1000); //液体分布系数
                            if (ytfbxs <= -0.2132 * Vm) {
                                ytfbxs = -0.2132 * Vm;
                            }
                        }
                        // ytfbxs = 0.00236 * Log10(1000 * Ul+1) / Math.pow(Dt / 1000, 1.451) - 0.14 + 0.167 * Log10(Vsl) - 0.113 * Log10(Dt / 1000)
                        else {
                            ytfbxs =
                                    0.0174 * Math.log10(Ul) / Math.pow(Dt / 1000, 0.799) - 1.352 - 0.162 * Math.log10(Vm) - 0.888 * Math.log10(Dt / 1000) - 1.2508;
                            //没有Pm的值
                            //if ytfbxs <= -Us * Ap * (1 - Pm / PL) / (Qg * (OGR + WGR) / 86400 + Us * Ap) then
                            //  ytfbxs = -Us * Ap * (1 - Pm / PL) / (Qg * (OGR + WGR) / 86400 + Us * Ap)
                            if (ytfbxs <= -Us * Ap * (1 - Pn / PL) / (Qg * (OGR + WGR) / 86400 + Us * Ap)) {
                                ytfbxs = -Us * Ap * (1 - Pn / PL) / (Qg * (OGR + WGR) / 86400 + Us * Ap);
                            }
                        }
                        //
                        Pm = (Wt + PL * Us * Ap) / (Qg * (WGR + OGR + 10000) / 86400 + Us * Ap) + ytfbxs * PL;
                        if (Pm < Pn) {
                            Pm = Pn;
                        }
                        if (Nrem > 2300) {
                            Fm = Math.pow(1.14 - 2 * Math.log10(e / Dt + 21.25 / Math.pow(Nrem, 0.9)), -2);
                        }//液相摩阻系数
                        else {
                            Fm = Nrem / 64;
                        } //液相摩阻系数
                        //fm = PressJZ * fm;
                        HL = (Pm - Pg) / (PL - Pg);
                        tF =
                                Fm * PL / (2 * Dt / 1000) * Math.pow(Vsl / HL, 2) * ((Ql / 86400 + Us * Ap) / ((Ql + Qg) / 86400 + Us * Ap) + ytfbxs);
                    } else if ((Lm > Ngv) && (Ngv >= Ls)) {

                        FlowState = "过渡流";
                        Us = 0.1; //滑脱速度
                        //Nrem = PL * Dt * Vm / Ul;
                        Pn = PL * El + Pg * (1 - El);
                        Um = Ul * El + Ug * (1 - El);
                        Nrem = Pn * Vm * Dt / Um;
                        while (true) {
                            Nb = Pm * Dt * Us / Ul; //速度雷诺数
                            if (Nb <= 3000) {
                                Us1 = (0.546 + 8.74 * Math.pow(10, -6) * Nrem) * Math.sqrt(g * Dt / 1000);
                            } else if ((Nb > 3000) && (Nb < 8000)) {

                                Usi = (0.251 + 8.74 * Math.pow(10, -6) * Nrem) * Math.sqrt(g * Dt / 1000);
                                Us1 = 0.5 * (Usi + Math.sqrt(Math.pow(Usi, 2) + 11170 * Ul / 1000 / (PL * Math.sqrt(Dt / 1000))));
                            } else if (Nb >= 8000) {
                                Us1 = (0.35 + 8.74 * Math.pow(10, -6) * Nrem) * Math.sqrt(g * Dt / 1000);
                            }
                            if (Math.abs(Us - Us1) <= 0.0001) {
                                break;
                            }
                            Us = Us1;
                        }
                        if (Vm <= 3.048) {

                            ytfbxs =
                                    0.00252 * Math.log10(Ul) / Math.pow(Dt / 1000, 1.38) - 0.782 + 0.232 * Math.log10(Vm) - 0.428 * Math.log10(Dt / 1000); //液体分布系数
                            if (ytfbxs <= -0.2132 * Vm) {
                                ytfbxs = -0.2132 * Vm;
                            }
                        }
                        // ytfbxs = 0.00236 * Log10(1000 * Ul+1) / Math.pow(Dt / 1000, 1.451) - 0.14 + 0.167 * Log10(Vsl) - 0.113 * Log10(Dt / 1000)
                        else {
                            ytfbxs =
                                    0.0174 * Math.log10(Ul) / Math.pow(Dt / 1000, 0.799) - 1.352 - 0.162 * Math.log10(Vm) - 0.888 * Math.log10(Dt / 1000);
                            if (ytfbxs <= -Us * Ap * (1 - Pm / PL) / (Qg * (OGR + WGR) / 86400 + Us * Ap)) {
                                ytfbxs = -Us * Ap * (1 - Pm / PL) / (Qg * (OGR + WGR) / 86400 + Us * Ap);
                            }
                        }
                        Pms = (Wt + PL * Us * Ap) / (Qg * (WGR + OGR + 10000) / 86400 + Us * Ap) + ytfbxs * PL; //段塞流Pm
                        if (Pms < Pn) {
                            Pms = Pn;
                        }
                        HL = Vsl / Vm; //雾状流HL
                        PmM = PL * HL + Pg * (1 - HL); //雾状流Pm
                        Pm = (Lm - Ngv) / (Lm - Ls) * Pms + (Ngv - Ls) / (Lm - Ls) * PmM;
                        if (Pm < Pn) {
                            Pm = Pn;
                        }
                        HL = (Pm - Pg) / (PL - Pg); //段塞流HL
                        if (Nrem > 2300) {
                            Fm = Math.pow(1.14 - 2 * Math.log10(e / Dt + 21.25 / Math.pow(Nrem, 0.9)), -2);
                        }//液相摩阻系数
                        else {
                            Fm = Nrem / 64;
                        } //段塞流液相摩阻系数
                        //fm = PressJZ * fm;
                        tFs =
                                Fm * PL / (2 * Dt / 1000) * Math.pow(Vsl / HL, 2) * ((Ql / 86400 + Us * Ap) / ((Ql + Qg) / 86400 + Us * Ap) + ytfbxs); //段塞流tF
                        HL = Vsl / Vm; //雾状流HL
                        //fm = Math.pow(1.14 - 2 * log10(e / Dt + 21.25 / Math.pow(Nrem, 0.9)), -2); //液相摩阻系数

                        tFm = Fm * Pg / (2 * Dt / 1000) * Math.pow(Vsg / (1 - HL), 2); //雾状流tF
                        tF = (Lm - Ngv) / (Lm - Ls) * tFs + (Ngv - Ls) / (Lm - Ls) * tFm;
                    } else if (Lm <= Ngv) {

                        FlowState = "雾状流";
                        HL = Vsl / Vm;
                        //Nrem = Pg * Dt * Vsg / Ug;
                        Pn = PL * El + Pg * (1 - El);
                        Um = Ul * El + Ug * (1 - El);
                        Nrem = Pn * Vm * Dt / Um;
                        Fm = Math.pow(1.14 - 2 * Math.log10(e / Dt + 21.25 / Math.pow(Nrem, 0.9)), -2); //液相摩阻系数
                        //Fm = PressJZ * Fm;
                        tF = Fm * Pg / (2 * Dt / 1000) * Math.pow(Vsg / (1 - HL), 2);
                        Pm = PL * HL + Pg * (1 - HL); //混合物密度
                        if (Pm < Pn) {
                            Pm = Pn;
                        }
                    }
                    HL = (Pm - Pg) / (PL - Pg); //通用输出
                    P2c =
                            P1 + Direction * Math.pow(10, -6) * ((Pm * g * Math.sin(Angle) + Fm * Pn * Math.pow(Vm, 2) / (2 / 1000 * Dt)) / (1 - Pm * Vm * Vsg / (Pav * Math.pow(10, 6)))) * DeltaH;
                    //P2c = P1 + Direction * Math.pow(10, -6) * ((PmXz * g * Sin(Angle) + Fm * Pn * Sqr(vm) / (2 / 1000 * Dt)) / (1 - PmXz * Vm * Vsg / (Pav * Math.pow(10, 6)))) * DeltaH;
                    Flow = FlowState;
                    if (Math.abs(P2 - P2c) <= 0.001) {
                        break;
                    } else {
                        P2 = P2c;
                    }
                    Ncount++;
                    if (Ncount > 500) {
                        break;
                    }
                }
                break;
            case 4: //Mukherjee-brill方法
                Ncount = 0;
                while (true) {
                    Pav = (P1 + P2) / 2;
                    Tav = (T1 + T2) / 2;
                    if (Pav <= 0) {

                        JieShu = "结束";
                        P2 = 0;
                        break;
                    }
                    double Z = (double) QPCYZ_Z(zSF, Ppc, Tpc, Pav, Tav).get("Z");
//            QPCYZ_Z(zSF, Ppc, Tpc, Pav, Tav, Z);
                    //Vsl = Qg * (OGR + WGR) * Rw / (86400 * Ap) / (Rw - 5.10546 * Math.pow(10, -4) * (Tav - 273) - 3.06254 * Math.pow(10, -6) * sqr(Tav - 273));
                    Vsl = Qg * (OGR + WGR) / (86400 * Ap);
                    //Vsg = Math.pow(10, 4) * Qg * (1.205 * z * Tav) / (3484.4 * Pav * (86400 * Ap));
                    Vsg = Math.pow(10, 4) * Qg / (86400 * Ap) * (0.101325 / Pav) * (Tav / 293.15) * Z;
                    Vm = Vsl + Vsg; //气液两相混合物流速
                    //计算无滑脱持液率El 和弗鲁德数Nfr
                    //El = (WGR + OGR) / (WGR + OGR + 10000);
                    El = Vsl / Vm;
                    // Nfr = sqr(Vm) / g * (Dt / 1000);
                    Nfr = Math.pow(Vm, 2) / (g * Dt / 1000);
                    //计算无因次液相粘度Nl 、无因次液相速度NLv 和无因次气相速度Ngv
                    //PL = Rw * 1000; //液体密度Kg/m^3
                    PL = 1000 * (Ro - 5.10546 * Math.pow(10, -4) * (Tav - 273 - 15) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273 - 15, 2)) * (OGR / (OGR + WGR)) +
                            1000 * WGR / (OGR + WGR) * (Rw - 5.10546 * Math.pow(10, -4) * (Tav - 273 - 15) - 3.06254 * Math.pow(10, -6) * Math.pow(Tav - 273 - 15, 2));
                    Pg = 1.205 * Rg * Pav / Tav * 2892 / Z;
                    //Thog = 1 / Math.pow(10, 1.58 + 0.05 * Pav) - 72 * Math.pow(10, -6) * (Tav - 305); //油气表面张力N.m
                    //if thog < 0 then
                    thog = 0.051;
                    //Thwg = Math.pow(10, -3) * ((410.8 - Tav) * 1.8 / 206 * (7 * Exp(-0.0362575 * Pav) - (52.5 - 0.87018 * Pav)) + (52.5 - 0.87018 * Pav)); //水气表面张力N.m
                    //if Thwg < 0 then
                    Thwg = 0.062;
                    Thtotal = WGR / (WGR + OGR) * Thwg + OGR / (WGR + OGR) * thog;
                    //NLv = Vsl * Math.pow(PL / (g * Thtotal), 0.25);
                    NLv = 3.1775 * Vsl * Math.pow(PL / (1000 * Thtotal), 0.25);
                    //NLv = 3.718 * Vsl * Math.pow(PL / (Thtotal * 0.001),0.25);
                    //Ngv = Vsg * Math.pow(PL / (g * Thtotal), 0.25);
                    Ngv = 3.1775 * Vsg * Math.pow(Pg / (1000 * Thwg), 0.25);
                    //Ngv = 3.718 * Vsg * Math.pow(PL / (Thtotal * 0.001),0.25);
                    Beta = 1.8 * (Tav - 273) + 32;
                    Ul = Math.exp(1.003 - 1.479 / 100 * Beta + 1.982 * Math.pow(10, -5) * Math.pow(Beta, 2)); //水的粘度，mpa
                    //Ul=1;
                    //Nl = Ul * (Math.pow(g / (Pl * Math.pow(Thtotal, 3)), 0.25));
                    Nl = 0.3147 * Ul * Math.pow((PL * Math.pow(1000 * Thtotal, 3)), -0.25);
                    //计算流型判断数L1,L2,L3,L4并判断流型
                    L1 = 316 * Math.pow(El, 0.302);
                    L2 = 0.0009252 * Math.pow(El, -2.4684);
                    L3 = 0.10 * Math.pow(El, -1.4516);
                    L4 = 0.5 * Math.pow(El, -6.738);
                    //流态
                    if ((El < 0.01) && (Nfr < L1) || (El >= 0.01) && (Nfr < L2)) {
                        FlowState = "分异型";
                    }//Segregrated Flow
                    else if ((El >= 0.01) && (Nfr > L2) && (Nfr <= L3)) {
                        FlowState = "过渡型";
                    }  //Transition Flow
                    else if ((El >= 0.01) && (El < 0.4) && (Nfr > L3) && (Nfr <= L1)
                            || (El >= 0.4) && (Nfr > L3) && (Nfr <= L4)) {
                        FlowState = "间隔型";
                    }//Intermittent Flow
                    else if ((El < 0.4) && (Nfr >= L1) || (El >= 0.4) && (Nfr > L4)) {
                        FlowState = "分散型";
                    }//Distributed Flow
                    //计算持液率HL及混合物密度Pm (Kg/m3)
                    //持液率计算
                    if (Direction == -1) {

                        c1 = -0.380113;
                        c2 = 0.129875;
                        c3 = -0.119788;
                        c4 = 2.343227;
                        c5 = 0.475686;
                        c6 = -0.288657;

                    } else if ("分异型".equals(FlowState)) {

                        c1 = -1.330282;
                        c2 = 4.808139;
                        c3 = 4.171584;
                        c4 = 56.262268;
                        c5 = 0.079951;
                        c6 = -0.504887;

                    } else {
                        c1 = -0.516644;
                        c2 = 0.789805;
                        c3 = 0.551627;
                        c4 = 15.51921;
                        c5 = 0.371771;
                        c6 = -0.393952;

                    }
                    HL = Math.exp((c1 + c2 * Math.sin(Angle) + c3 * Math.pow(Math.sin(Angle), 2) + c4 * Math.pow(Nl, 2)) * Math.pow(Ngv, c5) * Math.pow(NLv, c6));
                    //  HL= 2.718 / ((c1 + c2 * Sin(Angle) + c3 * Sqr(Sin(Angle)) + c4 * Sqr(Nl)) * Math.pow(Ngv,c5) * Math.pow(NLv,c6));
                    if (HL > 1) {
                        HL = 1;
                    }
                    Pm = PL * HL + Pg * (1 - HL);

                    /**
                     //计算摩擦阻力系数Fm
                     //                    {
                     //                        if (HL = 0) or(HL = 1) then
                     //                        S=0
                     //          else
                     //                        begin
                     //                        Y=El / Sqr(HL);
                     //                        if (Y > 1) and(Y < 1.2) then
                     //                        S=Ln(2.2 * Y - 1.2)
                     //            else
                     //                        S=Ln(Y) / (-0.0523 + 3.182 * Ln(Y) - 0.8725 * sqr(Ln(Y)) + 0.01853 * Math.pow(Ln(Y), 4));
                     //                        end;
                     //                        Fmn=exp(S); //计算摩阻系数
                     //                        //计算无滑脱摩阻系数fn
                     //                        Pn=PL * El + Pg * (1 - El); //无滑脱两相密度
                     //                        //Ug = GasNd(Rg, Tav, Ppc, Tpc, Pav, Z, PChar('Dempsey'));
                     //                        Mg=Rg * 28.97; //天然气平均分子量，kg/kmol;
                     //                        X=3.5 + 986 / (1.8 * Tav) + 0.01 * Mg;
                     //                        Y=2.4 - 0.2 * X;
                     //                        K=(9.4 + 0.02 * Mg) * Math.pow(1.8 * Tav, 1.5) / (209 + 19 * Mg + 1.8 * Tav);
                     //                        Ug=Math.pow(10, -4) * K * exp(X * Math.pow(Pg / 1000, Y)); //在给定温度和压力下天然气的粘度，mPa.s;
                     //                        Uw=WaterND(Tav - Tsc); //用摄氏度计算     //水粘度的计算mpa.s
                     //                        //Um = Uw * HL + Ug * (1 - HL); //混合物粘度
                     //                        Um=Uw * El + Ug * (1 - El);
                     //                        //Nrem = Pn * Vm * dt / Um; //雷诺数 P123      原来* 1000，这里不知道需不需要
                     //                        Nrem=Pn * Vm * dt / Um;
                     //                        Fn=0.0056 + 0.5 / Math.pow(Nrem, 0.32);
                     //                        Fm=Fmn * Fn; //摩阻系数    PressJZ  }
                     **/
                    Pn = PL * El + Pg * (1 - El); //无滑脱两相密度
                    //Ug = GasNd(Rg, Tav, Ppc, Tpc, Pav, Z, PChar('Dempsey'));
                    Mg = Rg * 28.97; //天然气平均分子量，kg/kmol;
                    X = 3.5 + 986 / (1.8 * Tav) + 0.01 * Mg;
                    Y = 2.4 - 0.2 * X;
                    K = (9.4 + 0.02 * Mg) * Math.pow(1.8 * Tav, 1.5) / (209 + 19 * Mg + 1.8 * Tav);
                    Ug = Math.pow(10, -4) * K * Math.exp(X * Math.pow(Pg / 1000, Y)); //在给定温度和压力下天然气的粘度，mPa.s;
                    Uw = WaterND(Tav - Tsc); //用摄氏度计算     //水粘度的计算mpa.s
                    //Um = Uw * HL + Ug * (1 - HL); //混合物粘度
                    Um = Uw * El + Ug * (1 - El);
                    //Nrem = Pn * Vm * dt / Um; //雷诺数 P123      原来* 1000，这里不知道需不需要
                    Nrem = Pn * Vm * Dt / Um;
                    if (Nrem <= 2300) {
                        Fn = 64 / Nrem;
                    } else {
                        Fn = Math.pow((1.14 - 2 * Math.log10(21.25 / Math.pow(Nrem, 0.9) + e / Dt)), -2);
                    }
                    // Nvgsm = log10(1.401 - 2.694 * Nl + 0.521 * Math.pow(Nlv,0.329));
                    Nvgsm = 10 / (1.401 - 2.694 * Nl + 0.521 * Math.pow(NLv, 0.329));
                    if (Nvgsm > Ngv) {
                        Fm = Fn;
                    } else {
                        fr = 9.4695 * Math.pow(El, 4) - 20.149 * Math.pow(El, 3) + 12.387 * Math.pow(El, 2) - 1.7179 * El + 1.0115;
                        Fm = fr * Fn;
                    }

                    if (Pm < Pn) {
                        Pm = Pn;
                    }
                    P2c =
                            P1 + Direction * Math.pow(10, -6) * ((Pm * g * Math.sin(Angle) + Fm * Pn * Math.pow(Vm, 2) / (2 / 1000 * Dt)) / (1 - Pm * Vm * Vsg / (Pav * Math.pow(10, 6)))) * DeltaH;
                    //if (Direction=-1) and (P2c>P1) then break;
                    Flow = FlowState;
                    if (Math.abs(P2 - P2c) <= 0.001) {
                        break;
                    } else {
                        P2 = P2c;
                    }
                    Ncount++;
                    if (Ncount > 500) {
                        break;
                    }

                }
                break;
        }


        YDGasDxlResponse response = new YDGasDxlResponse();
        response.setP2(P2);
        response.setT2(T2);
        response.setH2(H2);
        response.setPL(PL);
        response.setPg(Pg);
        response.setPm(Pm);
        response.setVsl(Vsl);
        response.setVsg(Vsg);
        response.setHL(HL);
        response.setZ(Z);
        response.setFlow(Flow);
        response.setJieShu(JieShu);


        return response;


    }


    public static Map QPCYZ_Z(String ZSF, double ppc, double tpc, double P, double T) {
        double Z = 0.86;
        boolean result = false;
        switch (ZSF) {
            case "Gopal":
                if (!((P / ppc <= 0.02) || (P / ppc >= 15) || (T / tpc <= 1.05) || (T / tpc >= 3))) {
                    result = true;
                    Z = z_Gopal(T, P, ppc, tpc);
                }
                break;
            case "Carlile-Gillett":
                if (!((T / tpc < 1.2) || (T / tpc > 3) || (P / ppc < 0) || (P / ppc > 15))) {
                    result = true;
                    Z = z_CG(T, P, ppc, tpc);
                }
                break;
            case "Cranmer":
                if (!((T / tpc < 1.2) || (T / tpc > 3))) {

                    result = true;
                    Z = z_Cranmer(T, P, ppc, tpc);
                }
                break;
            case "Hall-Yarbough":
                if (!((T / tpc <= 1.1) || (T / tpc > 3))) {
                    result = true;
                    Z = z_HY(T, P, ppc, tpc);
                }
                break;
            case "Dranchuk-Abu-Kassem":
                result = true;
                Z = z_DAK(T, P, ppc, tpc);
                break;
            case "Sarem":
                result = true;
                Z = z_Sarem(T, P, ppc, tpc);
                break;
            default:
                result = true;
                Z = Z_DPR(T, P, ppc, tpc);

        }

        Map<String, Object> map = new HashMap<>();
        map.put("result", result);
        map.put("Z", Z);
        return map;
    }

    public static double z_Gopal(double T, double P, double Ppc, double Tpc) {
        double ppr, tpr, a, b, c, d;

        double Result = 0;
        a = 0;
        b = 0;
        c = 0;
        d = 0;
        ppr = P / Ppc;
        tpr = T / Tpc;
        if ((ppr >= 0.2) && (ppr < 1.2)) {

            if ((tpr > 1.05) && (tpr < 1.2)) {

                a = 1.6643;
                b = -2.2114;
                c = -0.3647;
                d = 1.4385;
            } else if ((tpr >= 1.2) && (tpr < 1.4)) {

                a = 0.5222;
                b = -0.8511;
                c = -0.3647;
                d = 1.049;
            } else if ((tpr >= 1.4) && (tpr < 2)) {

                a = 0.1391;
                b = 0.2988;
                c = 0.0007;
                d = 0.9969;
            } else if ((tpr >= 2) && (tpr < 3)) {

                a = 0.0295;
                b = -0.0825;
                c = 0.0009;
                d = 0.9967;
            }
            Result = ppr * (a * tpr + b) + c * tpr + d;
        } else if ((ppr >= 1.2) && (ppr < 2.8)) {

            if ((tpr > 1.05) && (tpr < 1.2)) {

                a = -1.357;
                b = 1.4942;
                c = 4.6315;
                d = -4.7009;
            } else if ((tpr >= 1.2) && (tpr < 1.4)) {

                a = 0.1711;
                b = -0.3232;
                c = 0.5869;
                d = 0.1229;
            } else if ((tpr >= 1.4) && (tpr < 2)) {

                a = 0.0984;
                b = -0.2053;
                c = 0.0621;
                d = 0.858;
            } else if ((tpr >= 2) && (tpr < 3)) {

                a = 0.0221;
                b = -0.0527;
                c = 0.0127;
                d = 0.9549;
            }

            Result = ppr * (a * tpr + b) + c * tpr + d;
        } else if ((ppr >= 2.8) && (ppr < 5.4)) {

            if ((tpr > 1.05) && (tpr < 1.2)) {

                a = -0.3278;
                b = 0.4752;
                c = 1.8223;
                d = -1.9036;
            } else if ((tpr >= 1.2) && (tpr < 1.4)) {

                a = -0.2521;
                b = 0.3871;
                c = 1.6087;
                d = -1.6635;
            } else if ((tpr >= 1.4) && (tpr < 2)) {

                a = -0.0284;
                b = 0.0625;
                c = 0.4714;
                d = -0.0011;
            } else if ((tpr >= 2) && (tpr < 3)) {

                a = 0.0041;
                b = 0.0039;
                c = 0.0607;
                d = 0.7927;
            }
            ;
            Result = ppr * (a * tpr + b) + c * tpr + d;
        } else if ((ppr >= 5.4) && (ppr < 15)) {

            if ((tpr > 1.05) && (tpr < 3)) {
                Result = ppr * Math.pow(0.711 + 3.66 * tpr, -1.4667) - 1.637 / (0.319 * tpr + 0.522) + 2.071;
            }
            ;

        }
        return Result;

    }


    public static double z_CG(double T, double P, double ppc, double tpc) {
        double ppr = P / ppc;
        double tpr = T / tpc;
        double tprr[] = new double[17];
        double aa0[] = new double[17];
        double aa1[] = new double[17];
        double aa2[] = new double[17];
        double aa3[] = new double[17];
        double aa4[] = new double[17];
        double aa5[] = new double[17];
        double aa6[] = new double[17];
        double aa7[] = new double[17];
        double aa8[] = new double[17];
        tprr[0] = 1.2;
        aa0[0] = 0.99983959;
        aa1[0] = -0.26628974;
        aa2[0] = 0.2704917;
        aa3[0] = -0.42818909;
        aa4[0] = 0.277933568;
        aa5[0] = -0.087309949;
        aa6[0] = 0.014514544;
        aa7[0] = -0.001234195;
        aa8[0] = 0.000042371;

        tprr[1] = 1.25;
        aa0[1] = 0.9999853;
        aa1[1] = -0.23876105;
        aa2[1] = 0.22977682;
        aa3[1] = -0.32633899;
        aa4[1] = 0.19999835;
        aa5[1] = -0.06038164;
        aa6[1] = 0.0097420416;
        aa7[1] = -0.000809442;
        aa8[1] = 0.0000272939;

        tprr[2] = 1.3;
        aa0[2] = 0.99965221;
        aa1[2] = -0.17025216;
        aa2[2] = 0.042058027;
        aa3[2] = -0.06287547;
        aa4[2] = 0.036649912;
        aa5[2] = -0.008721294;
        aa6[2] = 0.0009444972;
        aa7[2] = -0.0000388028;
        aa8[2] = aa7[2];

        tprr[3] = 1.35;
        aa0[3] = 0.99998854;
        aa1[3] = -0.16861722;
        aa2[3] = 0.068264942;
        aa3[3] = -0.06987774;
        aa4[3] = 0.034462448;
        aa5[3] = -0.00076188928;
        aa6[3] = 0.0007902305;
        aa7[3] = -0.0000315298;
        aa8[3] = aa7[3];

        tprr[4] = 1.4;
        aa0[4] = 0.99909017;
        aa1[4] = -0.10684532;
        aa2[4] = -0.03534352;
        aa3[4] = 0.020061953;
        aa4[4] = -0.00275615;
        aa5[4] = 0.00012614114;
        aa6[4] = 0;
        aa7[4] = 0;
        aa8[4] = 0;

        tprr[5] = 1.45;
        aa0[5] = 0.9998472;
        aa1[5] = -0.11695971;
        aa2[5] = 0.0095017017;
        aa3[5] = -0.005455711;
        aa4[5] = 0.0033801628;
        aa5[5] = -0.0005627668;
        aa6[5] = 0.00002957458;
        aa8[5] = 0;
        aa7[5] = 0;

        tprr[6] = 1.5;
        aa0[6] = 0.99996811;
        aa1[6] = -0.10715986;
        aa2[6] = 0.012619472;
        aa3[6] = -0.00563558;
        aa4[6] = 0.0028513376;
        aa5[6] = -0.0004502881;
        aa6[6] = 0.00002303405;
        aa7[6] = 0;
        aa8[6] = 0;

        tprr[7] = 1.6;
        aa0[7] = 0.9994404;
        aa1[7] = -0.07604385;
        aa2[7] = -0.00368593;
        aa3[7] = -0.00408729;
        aa4[7] = -0.0003098573;
        aa5[7] = 0;
        aa6[7] = 0;
        aa7[7] = 0;
        aa8[7] = 0;

        tprr[8] = 1.7;
        aa0[8] = 0.9997573;
        aa1[8] = -0.05869864;
        aa2[8] = -0.002931822;
        aa3[8] = 0.003049573;
        aa4[8] = -0.0002192152;
        aa5[8] = 0;
        aa6[8] = 0;
        aa7[8] = 0;
        aa8[8] = 0;

        tprr[9] = 1.8;
        aa0[9] = 0.9989023;
        aa1[9] = -0.04424067;
        aa2[9] = -0.002463972;
        aa3[9] = 0.00240427;
        aa4[9] = -0.001686693;
        aa5[9] = 0;
        aa6[9] = 0;
        aa7[9] = 0;
        aa8[9] = 0;

        tprr[10] = 1.9;
        aa0[10] = 1.00001315;
        aa1[10] = -0.0428965;
        aa2[10] = 0.004959038;
        aa3[10] = -0.0005260271;
        aa4[10] = 0.0002500162;
        aa5[10] = -0.0000213384;
        aa6[10] = 0;
        aa7[10] = 0;
        aa8[10] = 0;

        tprr[11] = 2;
        aa0[11] = 0.99993498;
        aa1[11] = -0.03020987;
        aa2[11] = -0.004498492;
        aa3[11] = 0.005712788;
        aa4[11] = -0.001626191;
        aa5[11] = 0.0002262125;
        aa6[11] = -0.00001194358;
        aa7[11] = 0;
        aa8[11] = 0;

        tprr[12] = 2.2;
        aa0[12] = 0.99988811;
        aa1[12] = -0.01960693;
        aa2[12] = -0.0010136275;
        aa3[12] = 0.001394749;
        aa4[12] = -0.0001027373;
        aa5[12] = 0;
        aa6[12] = 0;
        aa7[12] = 0;
        aa8[12] = 0;

        tprr[13] = 2.4;
        aa0[13] = 0.99986756;
        aa1[13] = -0.008115691;
        aa2[13] = -0.003911533;
        aa3[13] = 0.001896349;
        aa4[13] = -0.000141257;
        aa5[13] = 0;
        aa6[13] = 0;
        aa7[13] = 0;
        aa8[13] = 0;

        tprr[14] = 2.6;
        aa0[14] = 1.00116764;
        aa1[14] = -0.01021189;
        aa2[14] = 0.002993536;
        aa3[14] = 0;
        aa4[14] = 0;
        aa5[14] = 0;
        aa6[14] = 0;
        aa7[14] = 0;
        aa8[14] = 0;

        tprr[15] = 2.8;
        aa0[15] = 1.00085;
        aa1[15] = -0.00228269;
        aa2[15] = 0.001994505;
        aa3[15] = 0;
        aa4[15] = 0;
        aa5[15] = 0;
        aa6[15] = 0;
        aa7[15] = 0;
        aa8[15] = 0;

        tprr[16] = 3;
        aa0[16] = 1.0000693;
        aa1[16] = 0.006308548;
        aa2[16] = 0.0001932414;
        aa3[16] = 0.0000992123;
        aa4[16] = 0;
        aa5[16] = 0;
        aa6[16] = 0;
        aa7[16] = 0;
        aa8[16] = 0;

        double a0, a1, a2, a3, a4, a5, a6, a7, a8;
        a0 = lgl(tprr, aa0, 17, tpr); //拉格朗日插值法求解
        a1 = lgl(tprr, aa1, 17, tpr); //拉格朗日插值法求解
        a2 = lgl(tprr, aa2, 17, tpr); //拉格朗日插值法求解
        a3 = lgl(tprr, aa3, 17, tpr); //拉格朗日插值法求解
        a4 = lgl(tprr, aa4, 17, tpr); //拉格朗日插值法求解
        a5 = lgl(tprr, aa5, 17, tpr); //拉格朗日插值法求解
        a6 = lgl(tprr, aa6, 17, tpr); //拉格朗日插值法求解
        a7 = lgl(tprr, aa7, 17, tpr); //拉格朗日插值法求解
        a8 = lgl(tprr, aa8, 17, tpr); //拉格朗日插值法求解

        double Result = a0 + a1 * ppr + a2 * Math.pow(ppr, 2) + a3 * Math.pow(ppr, 3) +
                a4 * Math.pow(ppr, 4) + a5 * Math.pow(ppr, 5) + a6 * Math.pow(ppr, 6) +
                a7 * Math.pow(ppr, 7) + a8 * Math.pow(ppr, 8);
        return Result;
    }


    public static double z_Cranmer(double T, double P, double ppc, double tpc) {

        double result = 0;
        double ppr = P / ppc;
        double tpr = T / tpc;
        double Z = 1;
        int err = 0;
        double nmd;
        while (true) {
            err++;
            nmd = (0.27 * ppr) / (Z * tpr);
            result = 1 + (0.31506 - 1.0467 / tpr - 0.5783 / Math.pow(tpr, 3)) * nmd +
                    (0.5353 - 0.6123 / tpr) * Math.pow(nmd, 2) + (0.6815 * Math.pow(nmd, 2) / Math.pow(tpr, 3));

            if (Math.abs(result - Z) > 0.001) {
                Z = result;
            } else {
                break;
            }
            if (err > 10000) {
                result = -1;
                break;
            }

        }

        return result;
    }

    public static double z_HY(double T, double P, double ppc, double tpc) {
        double result = 0;
        double y = 0;
        double ppr = P / ppc;
        double tpr = T / tpc;
        double pr = 1 / tpr;
        double y1 = 0.001;
        int n, k = 0;
        double ly1, Ly, a;

        while (true) {
            n = (int) Math.round(2.18 + 2.82 * pr);
            Ly = -0.06125 * ppr * pr * Math.exp(-1.2 * Math.pow(1 - pr, 2)) +
                    (y1 + Math.pow(y1, 2) + Math.pow(y1, 3) - Math.pow(y1, 4)) / Math.pow(1 - y1, 3) -
                    (14.76 * pr - 9.76 * Math.pow(pr, 2) + 4.58 * Math.pow(pr, 3)) * Math.pow(y1, 2) +
                    (90.7 * pr - 242.2 * Math.pow(pr, 2) + 42.4 * Math.pow(pr, 3)) * Math.pow(y1, n);
            n = (int) Math.round(1.18 + 2.82 * pr);
            ly1 = (1 + 4 * y1 + 4 * Math.pow(y1, 2) - 4 * Math.pow(y1, 3) + Math.pow(y1, 4)) / Math.pow(1 - y1, 4) -
                    (29.52 * pr - 19.52 * Math.pow(pr, 2) + 9.16 * Math.pow(pr, 3)) * y1 +
                    (2.18 + 2.82 * pr) * (90.7 * pr - 242.2 * Math.pow(pr, 2) + 42.4 * Math.pow(pr, 3)) * Math.pow(y1, n);
            y = y1 - Ly / ly1;
            if ((Math.abs(Ly) < 0.00001) && (Math.abs(y - y1) < 0.00001)) {
                break;
            } else {
                y1 = y;
            }
            k++;
            if (k > 200) {
                break;
            }
        }
        a = (0.06125 * ppr * pr) / y * Math.exp(-1.2 * Math.pow(1 - pr, 2));
        if (a <= 0) {
            a = 0.86;
        }
        result = a;

        return result;
    }


    public static double z_DAK(double T, double P, double ppc, double tpc) {
        double result, lf, lf1;
        double a1 = 0.3265;
        double a2 = -1.07;
        double a3 = -0.5339;
        double a4 = 0.01569;
        double a5 = -0.05165;
        double a6 = 0.5475;
        double a7 = -0.7361;
        double a8 = 0.1844;
        double a9 = 0.1056;
        double a10 = 0.6134;
        double a11 = 0.721;
        double ppr = P / ppc;
        double tpr = T / tpc;
        double nmd1 = 1;
        double nmd = 0;
        while (true) {

            lf = nmd1 - 0.27 * ppr / tpr +
                    (a1 + a2 / tpr + a3 / Math.pow(tpr, 3) + a4 / Math.pow(tpr, 4) + a5 / Math.pow(tpr, 5)) * Math.pow(nmd1, 2) +
                    (a6 + a7 / tpr + a8 / Math.pow(tpr, 2)) * Math.pow(nmd1, 3) - a9 * (a7 / tpr + a8 / Math.pow(tpr, 2)) * Math.pow(nmd1, 6) +
                    (a10 * Math.pow(nmd1, 3) / Math.pow(tpr, 3)) * (1 + a11 * Math.pow(nmd1, 2)) * Math.exp(-a11 * Math.pow(nmd1, 2));
            lf1 = 1 + (a1 + a2 / tpr + a3 / Math.pow(tpr, 3) + a4 / Math.pow(tpr, 4) + a5 / Math.pow(tpr, 5)) * 2 * nmd1 +
                    (a6 + a7 / tpr + a8 / Math.pow(tpr, 2)) * 3 * Math.pow(nmd1, 2) -
                    a9 * (a7 / tpr + a8 / Math.pow(tpr, 2)) * 6 * Math.pow(nmd1, 5) +
                    (a10 / Math.pow(tpr, 3)) * (3 * Math.pow(nmd1, 2) + a11 * (3 * Math.pow(nmd1, 4) - 2 * a11 * Math.pow(nmd1, 6))) * Math.exp(-a11 * Math.pow(nmd1, 2));
            nmd = nmd1 - lf / lf1;
            if ((lf < 0.0001) && (Math.abs(nmd - nmd1) < 0.001)) {
                break;
            } else {
                nmd1 = nmd;
            }
        }
        result = 1 + (a1 + a2 / tpr + a3 / Math.pow(tpr, 3) + a4 / Math.pow(tpr, 4) + a5 / Math.pow(tpr, 5)) * nmd +
                (a6 + a7 / tpr + a8 / Math.pow(tpr, 2)) * Math.pow(nmd, 2) -
                a9 * (a7 / tpr + a8 / Math.pow(tpr, 2)) * Math.pow(nmd, 5) +
                a10 * (1 + a11 * Math.pow(nmd, 2)) * Math.pow(nmd, 2) / Math.pow(tpr, 3) * Math.exp(1 - a11 * Math.pow(nmd, 2));

        return result;
    }


    public static double z_Sarem(double T, double P, double ppc, double tpc) {
        double result;
        double a[][] = new double[6][6];
        double ppr, tpr, x, Y;
        double[] pm = new double[6];
        double[] Pn = new double[6];


        a[0][0] = 2.1433504;
        a[0][1] = 0.33123524;
        a[0][2] = 0.10572871;
        a[0][3] = -0.05218404;
        a[0][4] = 0.01970398;
        a[0][5] = -0.00530959;

        a[1][0] = 0.083176184;
        a[1][1] = -0.13403614;
        a[1][2] = -0.050393654;
        a[1][3] = 0.044312146;
        a[1][4] = -0.026383354;
        a[1][5] = 0.008917833;

        a[2][0] = -0.021467042;
        a[2][1] = 0.066880961;
        a[2][2] = 0.0050924798;
        a[2][3] = -0.019329465;
        a[2][4] = 0.019262143;
        a[2][5] = -0.010894821;

        a[3][0] = -0.00087140318;
        a[3][1] = -0.027174261;
        a[3][2] = 0.010551336;
        a[3][3] = 0.0058972516;
        a[3][4] = -0.01153539;
        a[3][5] = 0.009559389;

        a[4][0] = 0.0042846283;
        a[4][1] = 0.0088512291;
        a[4][2] = -0.0073181933;
        a[4][3] = 0.0015366676;
        a[4][4] = 0.0042910089;
        a[4][5] = -0.0060114017;

        a[5][0] = -0.0016595343;
        a[5][1] = -0.0021520929;
        a[5][2] = 0.0026959963;
        a[5][3] = -0.0028326809;
        a[5][4] = -0.00081302526;
        a[5][5] = 0.003117517;

        ppr = P / ppc;
        tpr = T / tpc;
        x = (2 * ppr - 15) / 14.8;
        Y = (2 * tpr - 4) / 1.9;
        pm[0] = 0.7071068;
        pm[1] = 1.224745 * x;
        pm[2] = 0.7905695 * (3 * Math.pow(x, 2) - 1);
        pm[3] = 0.9354154 * (3 * Math.pow(x, 2) - 3 * x);
        pm[4] = 0.265165 * (35 * Math.pow(x, 4) - 30 * Math.pow(x, 2) + 3);
        pm[5] = 0.293151 * (63 * Math.pow(x, 5) - 70 * Math.pow(x, 3) + 15 * x);

        Pn[0] = 0.7071068;
        Pn[1] = 1.224745 * Y;
        Pn[2] = 0.7905695 * (3 * Math.pow(Y, 2) - 1);
        Pn[3] = 0.9354154 * (3 * Math.pow(Y, 2) - 3 * Y);
        Pn[4] = 0.265165 * (35 * Math.pow(Y, 4) - 30 * Math.pow(Y, 2) + 3);
        Pn[5] = 0.293151 * (63 * Math.pow(Y, 5) - 70 * Math.pow(Y, 3) + 15 * Y);

        result = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                result = result + a[i][j] * pm[i] * Pn[j];
            }
        }


        return result;
    }

    public static double Z_DPR(double T, double P, double ppc, double tpc) {
        double result;
        double a1, a2, a3, a4, a5, a6, a7, a8, ppr, tpr, nmd, lf, lf1, nmd1;

        a1 = 0.31506237;
        a2 = -1.0467099;
        a3 = -0.57832729;
        a4 = 0.53530771;
        a5 = -0.61232032;
        a6 = -0.10488813;
        a7 = 0.68157001;
        a8 = 0.68446549;
        ppr = P / ppc;
        tpr = T / tpc;
        nmd1 = 1;
        nmd = 0;

        while (true) {

            lf = nmd1 - 0.27 * ppr / tpr + (a1 + a2 / tpr + a3 / Math.pow(tpr, 3)) * Math.pow(nmd1, 2) +
                    (a4 + a5 / tpr) * Math.pow(nmd1, 3) + (a5 * a6 * Math.pow(nmd1, 6)) / tpr +
                    (a7 * Math.pow(nmd1, 3)) / Math.pow(tpr, 3) * (1 + a8 * Math.pow(nmd1, 2)) * Math.exp(-a8 * Math.pow(nmd1, 2));
            lf1 = 1 + (a1 + a2 / tpr + a3 / Math.pow(tpr, 3)) * (2 * nmd1) +
                    (a4 + a5 / tpr) * (3 * Math.pow(nmd1, 2)) + (a5 * a6 / tpr) * (6 * Math.pow(nmd1, 5)) +
                    a7 / Math.pow(tpr, 3) * (3 * Math.pow(nmd1, 2) + a8 * (3 * Math.pow(nmd1, 4)) - Math.pow(a8, 2) * (2 * Math.pow(nmd1, 6))) * Math.exp(-a8 * Math.pow(nmd1, 2));
            nmd = nmd1 - lf / lf1;
            if ((lf < 0.00001) && (Math.abs(nmd - nmd1) < 0.00001)) {
                break;
            } else {
                nmd1 = nmd;
            }
        }
        ;

        result = 1 + (a1 + a2 / tpr + a3 / Math.pow(tpr, 3)) * nmd +
                (a4 + a5 / tpr) * Math.pow(nmd, 2) + (a5 * a6 / tpr) * Math.pow(nmd, 5) +
                (a7 / Math.pow(tpr, 3) * Math.pow(nmd, 2) * (1 + a8 * Math.pow(nmd, 2)) * Math.exp(-a8 * Math.pow(nmd, 2)));


        return result;
    }

    /**
     * 拉格朗日
     *
     * @param aa
     * @param bb
     * @param N
     * @param T
     * @return
     */
    public static double lgl(double[] aa, double[] bb, Integer N, double T) {
        double result = 0;
        double a;
        for (int i = 0; i < N; i++) {
            a = 1;
            for (int j = 0; j < N; j++) {
                if (i != j) {
                    a = a * (T - aa[j]) / (aa[i] - aa[j]);
                }
            }
            result = result + a * bb[i];
        }

        return result;
    }

    public static double ln(double x) {
        return Math.log1p(x - 1);
    }

    public static double WaterND(double tem) {
        //Tem单位 摄氏度
        double ht = 1.8 * tem + 32;
        double Result = Math.exp(1.003 - 1.479 * Math.pow(10, -2) * ht + 1.982 * Math.pow(10, -5) * Math.pow(ht, 2));
        return Result;
    }

    public static Map gnn_calf1r(double Re, double e, double dt, double f2x) {

        Map<String, Object> map = new HashMap<>();

        double f1r = 0, f2r = 0, f1r1 = 0, f1r2 = 0;
        Integer Result = 0;
        if (Re <= 826.04) { //公共部分

            if (Re <= 338.84) {
                f1r = 0.05;
            } else {
                f1r = 16 / Re;
            }
        } else if ((e / dt) == 0.00001) { //当相对粗糙度=0.00001

            //InputExcelkr('f100001', F12s.f100001x, F12s.f100001);
            //TODO:下面的for循环，都是终值取到了数组的倒数第二位，实际意义待定
            for (int i = 0; i < f100001x.length - 1; i++) {
                if ((Re >= f100001x[i]) && (Re <= f100001x[i + 1])) {

                    f1r = (f100001[i + 1] - f100001[i]) / (f100001x[i + 1] - f100001x[i]) * (Re - f100001x[i + 1]) + f100001[i + 1];
                    break;
                } else {
                    continue;
                }
            }
        }  //当相对粗糙度=0.0001
        else if ((e / dt) == 0.0001) { //当相对粗糙度=0.0001

            //InputExcelkr('f10001', F12s.f10001x, F12s.f10001);
            for (int i = 0; i < f10001x.length - 1; i++) {
                if ((Re >= f10001x[i]) && (Re <= f10001x[i + 1])) {

                    f1r = (f10001[i + 1] - f10001[i]) / (f10001x[i + 1] - f10001x[i]) * (Re - f10001x[i + 1]) + f10001[i + 1];
                    break;
                } else {
                    continue;
                }

            }
        }
        //当相对粗糙度=0.001
        else if ((e / dt) == 0.001) { //当相对粗糙度=0.001

            //InputExcelkr('f1001', F12s.f1001x, F12s.f1001);
            for (int i = 0; i < f1001x.length - 1; i++) {
                if ((Re >= f1001x[i]) && (Re <= f1001x[i + 1])) {

                    f1r = (f1001[i + 1] - f1001[i]) / (f1001x[i + 1] - f1001x[i]) * (Re - f1001x[i + 1]) + f1001[i + 1];
                    break;
                } else {
                    continue;
                }
            }
        }
        //当相对粗糙度=0.01
        else if ((e / dt) == 0.01) { //当相对粗糙度=0.01

            // InputExcelkr('f101', F12s.f101x, F12s.f101);
            for (int i = 0; i < f101x.length - 1; i++) {
                if ((Re >= f101x[i]) && (Re <= f101x[i + 1])) {

                    f1r = (f101[i + 1] - f101[i]) / (f101x[i + 1] - f101x[i]) * (Re - f101x[i + 1]) + f101[i + 1];
                    break;
                } else {
                    continue;
                }
            }
        }
        //当相对粗糙度=0.05
        else if ((e / dt) == 0.05) { //当相对粗糙度=0.05

            //InputExcelkr('f105', F12s.f105x, F12s.f105);
            for (int i = 0; i < f105x.length - 1; i++) {
                if ((Re >= f105x[i]) && (Re <= f105x[i + 1])) {

                    f1r = (f105[i + 1] - f105[i]) / (f105x[i + 1] - f105x[i]) * (Re - f105x[i + 1]) + f105[i + 1];
                    break;
                } else {
                    continue;
                }
            }
        }
        //相对粗糙度在0.00001和0.0001之间
        else if (((e / dt) > 0.00001) && ((e / dt) < 0.0001)) {

            //InputExcelkr('f100001', F12s.f100001x, F12s.f100001);
            // InputExcelkr('f10001', F12s.f10001x, F12s.f10001);
            for (int i = 0; i < f100001x.length - 1; i++) {
                if ((Re >= f100001x[i]) && (Re <= f100001x[i + 1])) {

                    f1r1 = (f100001[i + 1] - f100001[i]) / (f100001x[i + 1] - f100001x[i]) * (Re - f100001x[i + 1]) + f100001[i + 1];
                    break;
                } else {
                    continue;
                }
            }
            for (int i = 0; i < f10001x.length - 1; i++) {
                if ((Re >= f10001x[i]) && (Re <= f10001x[i + 1])) {

                    f1r2 = (f10001[i + 1] - f10001[i]) / (f10001x[i + 1] - f10001x[i]) * (Re - f10001x[i + 1]) + f10001[i + 1];
                    break;
                } else {
                    continue;
                }
            }
            f1r = (f1r2 - f1r1) / (0.0001 - 0.00001) * (e / dt - 0.0001) + f1r2;

        }    //相对粗糙度在0.0001和0.001之间
        else if (((e / dt) > 0.0001) && ((e / dt) < 0.001)) {

            //InputExcelkr('f10001', F12s.f10001x, F12s.f10001);
            // InputExcelkr('f1001', F12s.f1001x, F12s.f1001);
            for (int i = 0; i < f10001x.length; i++) {
                if ((Re >= f10001x[i]) && (Re <= f10001x[i + 1])) {

                    f1r1 = (f10001[i + 1] - f10001[i]) / (f10001x[i + 1] - f10001x[i]) * (Re - f10001x[i + 1]) + f10001[i + 1];
                    break;
                } else {
                    continue;
                }
            }
            for (int i = 0; i < f1001x.length - 1; i++) {
                if ((Re >= f1001x[i]) && (Re <= f1001x[i + 1])) {

                    f1r2 = (f1001[i + 1] - f1001[i]) / (f1001x[i + 1] - f1001x[i]) * (Re - f1001x[i + 1]) + f1001[i + 1];
                    break;
                } else {
                    continue;
                }
            }
            f1r = (f1r2 - f1r1) / (0.001 - 0.0001) * (e / dt - 0.001) + f1r2;
            ;
        }
        //相对粗糙度在0.001和0.01之间
        else if (((e / dt) > 0.001) && ((e / dt) < 0.01)) {

            //InputExcelkr('f1001', F12s.f1001x, F12s.f1001);
            //InputExcelkr('f101', F12s.f101x, F12s.f101);
            for (int i = 0; i < f1001x.length - 1; i++) {
                if ((Re >= f1001x[i]) && (Re <= f1001x[i + 1])) {

                    f1r1 = (f1001[i + 1] - f1001[i]) / (f1001x[i + 1] - f1001x[i]) * (Re - f1001x[i + 1]) + f1001[i + 1];
                    break;
                } else {
                    continue;
                }
            }
            for (int i = 0; i < f101x.length - 1; i++) {
                if ((Re >= f101x[i]) && (Re <= f101x[i + 1])) {

                    f1r2 = (f101[i + 1] - f101[i]) / (f101x[i + 1] - f101x[i]) * (Re - f101x[i + 1]) + f101[i + 1];
                    break;
                } else {
                    continue;
                }
            }
            f1r = (f1r2 - f1r1) / (0.01 - 0.001) * (e / dt - 0.01) + f1r2;
        }
        //相对粗糙度在0.01和0.05之间
        else if (((e / dt) > 0.01) && ((e / dt) < 0.05)) {

            // InputExcelkr('f101', F12s.f101x, F12s.f101);
            // InputExcelkr('f105', F12s.f105x, F12s.f105);
            for (int i = 0; i < f101x.length - 1; i++) {
                if ((Re >= f101x[i]) && (Re <= f101x[i + 1])) {

                    f1r1 = (f101[i + 1] - f101[i]) / (f101x[i + 1] - f101x[i]) * (Re - f101x[i + 1]) + f101[i + 1];
                    break;
                } else {
                    continue;
                }
            }
            for (int i = 0; i < f105x.length - 1; i++) {
                if ((Re >= f105x[i]) && (Re <= f105x[i + 1])) {

                    f1r2 = (f105[i + 1] - f105[i]) / (f105x[i + 1] - f105x[i]) * (Re - f105x[i + 1]) + f105[i + 1];
                    break;
                } else {
                    continue;
                }
            }
            f1r = (f1r2 - f1r1) / (0.05 - 0.01) * (e / dt - 0.05) + f1r2;
        }
        //相对粗糙度>0.05
        else if ((e / dt) > 0.05) {
            f1r = Math.pow((-4 * Math.log10(0.027 * e / dt)), -2) + 0.067 * Math.pow((e / dt), 1.73);

            //f2的计算
            // xlsname=ExtractFilePath(Application.ExeName) + 'f2.xls';
            // InputExcelkr(xlsname, F12s.f2xs, F12s.f2s);
            for (int i = 0; i < f2xs.length - 1; i++) {
                if ((f2x >= f2xs[i]) && (f2x <= f2xs[i + 1])) {

                    f2r = (f2s[i + 1] - f2s[i]) / (f2xs[i + 1] - f2xs[i]) * (f2x - f2xs[i + 1]) + f2s[i + 1];
                    break;
                } else {
                    continue;
                }
            }
            Result = 1;
        }

        map.put("Result", Result);
        map.put("f1r", f1r);
        map.put("f2r", f2r);
        return map;
    }
    public  static  double Nd_Dempsey(double rg,double  T,double  Ppc,double  Tpc,double  P){
//        TODO -otzn -c :   2008-11-19 11:33:44
//        T单位 K
        double ppr, tpr, u1, m;
        double a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15;

        u1 = (1.709 * Math.pow(10, -5) - 2.062 * Math.pow(10, -6) * rg) * (1.8 * (T - 273) + 32) +
                8.188 * Math.pow(10, -3) - 6.15 * Math.pow(10, -3) * ln(rg) / ln(10);
        ppr = P / Ppc;
        tpr = T / Tpc;
        a0 = -2.4621182;
        a1 = 2.97054714;
        a2 = -2.86264054 * Math.pow(10, -1);
        a3 = 8.50420522 * Math.pow(10, -3);
        a4 = 2.80860949;
        a5 = -3.49803305;
        a6 = 3.6037302 * Math.pow(10, -1);
        a7 = -1.044324 * Math.pow(10, -2);
        a8 = -7.93385684 * Math.pow(10, -1);
        a9 = 1.39643306;
        a10 = -1.49144925 * Math.pow(10, -1);
        a11 = 4.41015512 * Math.pow(10, -3);
        a12 = 8.39387178 * Math.pow(10, -2);
        a13 = -1.86408848 * Math.pow(10, -1);
        a14 = 2.03367881 * Math.pow(10, -2);
        a15 = -6.09579263 * Math.pow(10, -4);
        m = a0 + a1 * ppr + a2 * Math.pow(ppr, 2) + a3 * Math.pow(ppr, 3) +
                tpr * (a4 + a5 * ppr + a6 * Math.pow(ppr, 2) + a7 * Math.pow(ppr, 3)) +
                Math.pow(tpr, 2) * (a8 + a9 * ppr + a10 * Math.pow(ppr, 2) + a11 * Math.pow(ppr, 3)) +
                Math.pow(tpr, 3) * (a12 + a13 * ppr + a14 * Math.pow(ppr, 2) + a15 * Math.pow(ppr, 3));
        return  u1 * Math.exp(m) / tpr;
    }

}