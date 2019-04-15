package cn.edu.nwpu.ms_srm_balszero.util;

import static java.lang.Math.*;

/**
 *发动机常用参数的计算工具类
 * @author Trium
*/
public class SRMCalUtil {
    /**
     *
     * @param a 燃速系数（MPa）
     * @param n 压强指数
     * @param pb 压强（MPa）
     * @return 燃速，单位（m/s）
     */
    public static double getR(double a, double n, double pb, double Tw, double Ta) {
        double aut = 0.003;
        return Math.exp(aut * (Tw - Ta)) * a * Math.pow(pb, n);
    }

    /**
     *
     * @param pb 燃烧室压强
     * @param nozzleArea 喷管喉部面积
     * @param cstar 推进剂特征速度
     * @return 喷管流出的质量流率
     */
    public static double getQt(double pb, double nozzleArea, double cstar){
        return pb * nozzleArea / cstar;
    }

    public static double getPbBalance(double a, double n, double dens, double cstar, double burningArea, double nozzleArea){
        return Math.pow(a*dens*cstar*burningArea/nozzleArea, 1/(1-n));
    }

    //-----------------------------------------------------------------------------
    //   	  计算理论推力系数,考虑气流分离的冻结流理论推力系数计算
    //-----------------------------------------------------------------------------
    /**
     *
     * @param k 燃气比热比
     * @param AeAt 喷管面积比
     * @param PaPc 背压与燃烧室压强之比
     * @return
     */
    public static double CoeForce(double  k,double  AeAt,double  PaPc)
    {
        double PePc=PeOverPc(k,AeAt);

        double PePa=PePc/PaPc;
        double PiPa=0.6666666667*pow(PaPc,0.2);
        double CF0,t;
        if(PePa>=PiPa){
            CF0 = sqrt(k)*pow(2.0/(k+1.0),(k+1.0)/(2.0*k-2.0))*
                    sqrt(2.0*k/(k-1)*(1.0-pow(PePc,(k-1)/k)));
            t=CF0+AeAt*(PePc-PaPc);
            return t;
        }
        //出现气流分离
        double PiPc=PiPa*PaPc;

        double AiAt=pow(2.0/(k+1.0),1.0/(k-1.0))*pow((k-1.0)/(k+1.0),0.5)
                /(pow(PiPc,1/k)*sqrt(1.0-pow(PiPc,(k-1)/k)));

        double a;
        if(AiAt<=(AeAt/1.604+0.377))
            a=(AiAt-1)/2.4;
        else
            a=(AeAt-AiAt)/1.45;

        double A095At=AiAt+a;

        double dCFs=0.55*(PiPc+0.95*PaPc)*a+0.975*PaPc*(AeAt-A095At);
        CF0 = sqrt(k)*pow(2.0/(k+1.0),(k+1.0)/(2.0*k-2.0))*
                sqrt(2.0*k/(k-1)*(1.0-pow(PiPc,(k-1)/k)))+AiAt*(PiPc-PaPc);
        t=CF0-dCFs;
        return t;
    }

    /**
     * 由面积比计算压强比
     * @param k
     * @param AeAt
     * @return
     */
    public static double PeOverPc(double  k,double  AeAt)
    {
        double  gma=sqrt(k)*pow(2.0/(k+1.0),(k+1.0)/(2.0*k-2.0));
        double  pepc0=0.008;
        double  pepc;
        double  eps;
        do{
            double parameter=gma/AeAt/sqrt(2.0*k/(k-1)*(1.0-pow((double)pepc0,(double)((k-1)/k))));
            pepc = pow(parameter,(double)k);
            eps=abs(pepc-pepc0)/pepc;
            pepc0=pepc;
        }while(eps>1.0E-4);
        return pepc;
    }

    /**
     *
     * @param t
     * @param p
     * @param dt
     * @param epsa 面积膨胀比
     * @param theta 膨胀角（弧度）
     * @param Mp0 装药总质量
     * @param dens
     * @param epsi 面积收缩比
     * @param Lsn 喷管潜入深度与装药长度之比
     * @param vLoading 装药装填分数
     * @return
     */
//    double GetEtaCf(double t,double p,double dt,double epsa,double theta,double Mp0, double dens, double epsi, double Lsn, double vLoading){
//        double Z = 0.33;
//        double Kethy=Z/1.0196;
//        double C1,C2,C3,C4;
//        double Vp=Mp0/dens;
//        double Q = 0.9732; //冻结流与平衡流比冲之比
//        double itaBL, itaDIV, itaKIN, itaSUB, itaTP, EtaCf;
//        //边界层损失
//        if(epsa>9)
//            itaBL=0.0009378*Math.pow(p*1000.0,0.8)*(1.0+2.0*Math.exp(-0.0002407
//                     *Math.pow(p*1000.0,0.8)*t/Math.pow(dt/10,0.2)))
//                               *(1.0+0.016*(epsa-9))/Math.pow(dt/10,0.2);
//        else
//            itaBL=0.0009378*Math.pow(p*1000.0,0.8)*(1.0+2.0*Math.exp(-0.0002407*Math.pow(p*1000.0,0.8)*t/Math.pow(dt/10,0.2)));
//        //扩散损失
//        itaDIV=50.0*(1.0-Math.cos(theta));
//
//        //化学不平衡损失
//        if(p>1.379)
//            itaKIN=33.3*(1-Q)*1.379/p;
//        else
//            itaKIN=0.0;
//
//        //潜入损失
////        GetC1C2C3C4();
//        C1=0.017580*Math.pow(Z/epsi,0.8)*Math.pow(Lsn,0.4);
//        C2=16.63*Math.pow(Z,0.5);
//        C3=0.2385*Math.pow(Kethy,0.3333);
//        C4=1000000*(Vp/vLoading-0.5*Vp);
//        itaSUB=C1*Math.pow(p*1000.0,0.8)*Math.pow(dt/10,0.2);
//
//        //两相流损失
//        double Dp=C3*Math.pow(p*1000.0,0.3333)*(1.0-Math.exp(-0.001575*C4*4.0/(Math.PI*dt*dt/100)))
//                                    *(1.0+0.01772*dt/10);
//        itaTP=C2*Math.pow(Dp,0.8)/(Math.pow(p*1000.0,0.15)*Math.pow(epsa,0.08)*Math.pow(0.3937*dt/10,0.4));
//
//        //喷管总效率
//        EtaCf=1.0-(itaBL+itaDIV+itaKIN+itaSUB+itaTP)/100.0;
//        return EtaCf;
//    }
    //同喷管潜入分数有关的C1
//    void CBalsTest::GetC1C2C3C4() {
//        double Z = 0.33;
//        double Kethy=Z/1.0196;
//        C1=0.017580*pow(Z/nData->epsi,0.8)*pow(Lsn,0.4);
//        C2=16.63*pow(Z,0.5);
//        C3=0.2385*pow(Kethy,0.3333);
//        double Vp=Mp0/pData->dens;
//        C4=1000000*(Vp/vLoading-0.5*Vp);
//    }
}