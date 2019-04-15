package cn.edu.nwpu.ms_srm_balszero.service.cal;
import static java.lang.Math.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ly
 */
public class SrmFuncs {

    /* ******************************************************************** */
    /*             固体火箭发动机参数计算子程序(SRMFUC.C)                   */
    /* ******************************************************************** */

    /* caculate gas exit pressure after propellant burned out               */
    /* p0(MPa) charV(m/s) At(m*m) deltat(s) Vbf(m*m*m)  */

//由于函数pow()有多个重载函数，在一些引起引用歧义的地方将pow()的参数由double 强制转换为double！/*蔡强 20060819*/
//-------------------------------------------------------------------------------------------------------
    /**
     *
     * @param p0
     * @param gam
     * @param charV
     * @param At
     * @param deltat
     * @param Vbf
     * @return
     */
    double  PTail(double  p0,double  gam,double  charV,double  At,double  deltat,double  Vbf)
    {
        return p0/exp(gam*gam*charV*At*deltat/Vbf);

    }
//--------------------------------------------------------------------------------------
    /**
     * 计算Gamma
     * @param k 绝热指数
     * @return
     */
    public double  GasGamma(double  k)
    {
        return sqrt(k)*pow((2.0/(1.0+k)),0.5*(1.0+k)/(k-1.0));
    }

//-------------------------------------------------------------------------------------
    /*              caculate pressure in chamber(MPa)                       */
    /* a(m/[(MPa)^n*s]) lup(kg/m^3) charV(m/s) Ab(m^2) At(m^2)             */
// R: J/kg.K  T: K
    /**
     * caculate pressure in chamber(MPa)
     * @param a  (m/[(MPa)^n*s]) 燃速系数
     * @param lup kg/m^3 推进剂密度
     * @param charV m/s 特征速度
     * @param Ab m^2 燃面
     * @param At m^2 侯面积
     * @param n 推进剂压力指数
     * @param etac
     * @param phi
     * @param kaba
     * @return
     */
    double  Pressure(double  a,double  lup,double  charV,double  Ab,
                     double  At,double  n,double  etac,double  phi,double  kaba)
    {
	/*return pow((a*lup*etac*charV*Ab*pow(10.0,-6.0*n)*kaba/(phi*At)),
			1.0/(1.0-n))*1.0E-06;*/
        return pow((a*lup*etac*charV*Ab*kaba/(phi*At)),
                1.0/(1.0-n))*pow(10.0,-6.0/(1-n));

    }
//--------------------------------------------------------------------------
    /**
     * 计算燃烧室压强Pc
     * @param a 燃速系数
     * @param lup 推进剂密度
     * @param charV 特征速度
     * @param Ab 燃烧面积
     * @param At 喉部面积
     * @param n 压力指数
     * @param R 燃气气体常数
     * @param T 燃气温度
     * @param etac 热损失系数
     * @param phi
     * @param kaba
     * @return
     */
    public double    PressureExt(double  a,double  lup,double  charV,double  Ab,
                                 double  At,double  n,double  R,double  T,double  etac,double  phi,
                                 double  kaba)
    {
        double  pc;
        double  x=a*etac*charV*Ab*pow(10.0,-6.0*n)*kaba/(phi*At);
        double  y=1.0/(1-n);
        pc=pow(x*lup, y)*1.0E-06;

        double  oldPc;
        oldPc=pc;
        for(int i=0;true;i++)//待测试
        {
            pc=pow((double)(x*(lup-1.0E+06*oldPc/R/T)),(double)y)*1.0E-06;

            if(abs(oldPc-pc)<0.01)
            {
                break;
            }
            oldPc=pc;
        }
        return pc;

    }
    //为上面的函数的重载，功能相同
    public double PressureExt(double  a,double  lup,double  charV,double  Ab,
                                 double  At,double  n,double  R,double  T,double  etac)
    {
        double  pc;
        double kaba=1.0;
        double phi=1.0;
        double  x=a*etac*charV*Ab*pow(10.0,-6.0*n)*kaba/(phi*At);
        double  y=1.0/(1-n);
        pc=pow(x*lup, y)*1.0E-06;

        double  oldPc;
        oldPc=pc;
        for(int i=0;true;i++)//待测试
        {
            pc=pow((double)(x*(lup-1.0E+06*oldPc/R/T)),(double)y)*1.0E-06;

            if(abs(oldPc-pc)<0.01)
            {
                break;
            }
            oldPc=pc;
        }
        return pc;

    }
//---------------------------------------------------------------------------------
    /**
     * 计算壳体壁厚
     * @param sigmaB 壳体强度 MPa
     * @param Kn 结构安全系数
     * @param Pc 燃烧室压力 MPa
     * @param D 燃烧室直径 mm
     * @return 返回壳体壁厚
     */
    public  double  CalcucDelta(double sigmaB,double Kn,double Pc,double D)
    {
        return Pc*D/(2.3*sigmaB/Kn+Pc);
    }
//--------------------------------------------------------------------------
    /**
     *  计算燃烧面积
     * @param At 侯部面积
     * @param Pc 燃烧室压力 MPa
     * @param n 压力指数
     * @param a 燃速系数
     * @param rhoP 推进剂密度 kg/m^3
     * @param charV 特征速度 m/s
     * @return 返回燃面
     */
    public double CalcuAb(double At,double Pc,double n,double a,double rhoP,double charV)
    {
        return At*pow(1.0E+06*Pc,1.0-n)/(a*rhoP*charV*pow(10.0,-6.0*n));
    }
//---------------------------------------------------------------------------
    /**
     * 计算喉部面积
     * @param Ab 燃面
     * @param Pc 燃室压
     * @param n 压力指数
     * @param a 燃速系数
     * @param rhoP 推进剂密度
     * @param charV 特征速度
     * @return 返回喉部面积
     */
    public double CalcuAt(double Ab,double Pc,double n,double a,double rhoP,
                          double charV){
        return Ab/(pow(1.0E+06*Pc,1.0-n)/(a*rhoP*charV*pow(10.0,-6.0*n)));
    }
    /**
     * 由质量流率计算喉面
     * @param mRate 质流
     * @param charV 特征速度
     * @param Pc 燃室压
     * @return
     */
    public double CalcuAt(double mRate,double charV,double Pc){
        return mRate*charV/(10000.0*Pc)/100.0;
    }
//---------------------------------------------------------------------
    /**
     * 计算比冲
     * @param charV 特征速度
     * @param Cf 推力系数
     * @return 比冲
     */
    public double CalcuIsp(double charV,double Cf){
        return charV*Cf;
    }
//------------------------------------------------------------------
    /**
     * 计算燃速
     * @param a
     * @param n
     * @param Pc
     * @return
     */
    public double CalcuRb(double a,double n,double Pc){
        return a*pow(Pc,n);
    }
//-----------------------------------------------------------------------
    /*                p(MPa) At(m^2) Cf etaf         */
    /**
     * calcutate thrust F(kN)
     * @param Pc
     * @param At
     * @param Cf
     * @return
     */
    public double Force(double Pc,double At,double Cf){
        return Cf*At*Pc*1.0E+03;
    }
    double  Force(double  Pc,double  At,double  Cf,double  etaf,double  sp,double  sn)
    {
        return sp*sn*etaf*Cf*At*Pc*1.0E+03;
    }
//-----------------------------------------------------------------------
    /**
     * 计算圆面积
     * @param d 直径
     * @return
     */

    public double  CircleArea(double  d)
    {
        return 0.25*3.1415926*d*d;
    }
//----------------------------------------------------------------
    /*    caculate gas mass flow rate(kg/s)  Pc(MPa) At(m^2) charV(m/s)   */
    /**
     * caculate gas mass flow rate(kg/s)
     * @param Pc
     * @param At
     * @param charV
     * @return
     */
    public double  GasMassFlowRate(double  Pc,double  At,double  charV)
    {
        return 1.0E+06*Pc*At/charV;
    }

//------------------------------------------------------------------------
    /*   caculate burning rate(m/s) a(m/[(MPa)^n*s])  Pc(MPa)             */
    /**
     * caculate burning 计算初始时刻
     * @param a
     * @param Pc
     * @param n
     * @param k
     * @param v
     * @param vcr
     * @return
     */
    double  BurningRate(double  a,double  Pc,double  n, double  k,double  v,double  vcr)
    {
        if(v<=vcr)
            return a*pow(Pc,n);
        else
            return a*pow(Pc,n)*(1.0+k*(v-vcr));
    }

    double  BurningRate(double  a,double  Pc,double  n)
    {
        return a*pow(Pc,n);
    }
//-----------------------------------------------------------------------------------------------------
    /*   caculate parameter in new temperature T0(standard) T(curent) tau   */
    /**
     * caculate parameter in new temperature T0(standard) T(curent)
     * @param PT0
     * @param tau
     * @param T
     * @param T0
     * @return
     */
    double    Para_in_T(double  PT0,double  tau,double  T,double  T0)
    {
        return PT0*exp(tau*(T-T0));
    }
//----------------------------------------------------------------------------------
    /*      calculate expansion area of nozzle    k,Pe,Pc(MPa)               */
    /**
     * calculate expansion area of nozzle
     * @param k
     * @param Pc
     * @param Pe
     * @return
     */
    double  ExpanArea(double  k,double  Pc,double  Pe)
    {
        return pow(2/(k-1),1/(k-1))*sqrt((k-1)/(k+1))/
                sqrt(pow(Pe/Pc,2/k)-pow(Pe/Pc,(k+1)/k));
    }
//-----------------------------------------------------------------------------------
    /*           calculate coefficient of thrust k,Pe,Pa,Pc(MPa)          */
    /**
     * calculate coefficient of thrust
     * @param k
     * @param Pc
     * @param Pe
     * @param Pa
     * @return
     */
    double  C_Force(double  k,double  Pc,double  Pe,double  Pa)
    {
        return GasGamma(k)*sqrt((2*k/(k-1)) *(1-pow(Pe/Pc,(k-1)/k)))+
                ExpanArea(k,Pc,Pe)*(Pe-Pa)/Pc;
    }
//------------------------------------------------------------------------------------
    /*            Special Impulse character velocity(m/s) k,Pc,Pe,Pa      */
    /**
     * Special Impulse character velocity(m/s)
     * @param k
     * @param Pc
     * @param AeAt
     * @param Pa
     * @param charV
     * @return
     */
    double  SpecialI(double  k,double  Pc,double  AeAt,double  Pa,double  charV)
    {
        return charV*CoeForce(k,AeAt,Pa/Pc);
    }
//-----------------------------------------------------------------------
    /*           calculate exit pressure of the nozzle                    */
    /**
     * calculate exit pressure of the nozzle
     * @param k
     * @param Pc
     * @param Pa
     * @param epsA
     * @return
     */
    double    CalcuNozzlePe(double  k,double  Pc,double  Pa,double  epsA)
    {
        double  cf0,eps,gamma_g,pe;

        eps=epsA+2;
        pe=Pa;
        while(abs(eps/epsA-1.0)>0.0005)
        {
            gamma_g=GasGamma(k);
            cf0=gamma_g*sqrt(2*k/(k-1)*(1-pow((pe/Pc),(k-1)/k)));
            eps=gamma_g*gamma_g/pow(pe/Pc,1/k)/cf0;
            pe=pe*eps/epsA;
        }
        return pe;
    }


//------------------------------------------------------------
//							由压强比计算面积比
//------------------------------------------------------------
    /**
     * 由压强比计算面积比
     * @param k
     * @param PePc
     * @return
     */
    double  AeOverAt(double  k,double  PePc)
    {
        return sqrt(k)*pow(2.0/(k+1.0),(k+1.0)/(2.0*k-2.0))/pow((double)PePc,(double)(1.0/k))
                /sqrt(2.0*k/(k-1)*(1.0-pow(PePc,(k-1)/k)));
    }

//------------------------------------------------------------
//							由面积比计算压强比
//------------------------------------------------------------
    /**
     * 由面积比计算压强比
     * @param k
     * @param AeAt
     * @return
     */
    public double  PeOverPc(double  k,double  AeAt)
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
    public double CalcuPe(double k,double AeAt,double Pc){
        return Pc*PeOverPc(k, AeAt);
    }

    //-----------------------------------------------------------------------------
//   	  计算理论推力系数,考虑气流分离的冻结流理论推力系数计算
//-----------------------------------------------------------------------------
    public double  CoeForce(double  k,double  AeAt,double  PaPc)
    {
        //double  PePc=PeOverPc(k,AeAt);

        //   double  PePa=PePc/PaPc;
        //   double  PiPa=0.6666666667*pow((double)PaPc,0.2);
        //   double  CF0,t;

        ////蔡强 09_4_3修改，修改前的如下隐去的部分，主要时考虑气流分离的情况

        //if(PePa<PiPa)      //出现气流分离
        //{
        //	double  PiPc=PiPa*PaPc;

        //	double  AiAt=pow(2.0/(k+1.0),1.0/(k-1.0))*pow((k-1.0)/(k+1.0),0.5)
        //		/(pow(PiPc,1/k)*sqrt(1.0-pow(PiPc,(k-1)/k)));

        //	double  a;
        //	if(AiAt<=(AeAt/1.604+0.377))
        //		a=(AiAt-1)/2.4;
        //	else
        //		a=(AeAt-AiAt)/1.45;

        //	double  A095At=AiAt+a;

        //	double  dCFs=0.55*(PiPc+0.95*PaPc)*a+0.975*PaPc*(AeAt-A095At);
        //	CF0 = sqrt(k)*pow(2.0/(k+1.0),(k+1.0)/(2.0*k-2.0))*
        //		sqrt(2.0*k/(k-1)*(1.0-pow(PiPc,(k-1)/k)))+AiAt*(PiPc-PaPc);
        //	t=CF0+dCFs;//-(PiPc-PaPc)*AeAt;
        //	return t;

        //}
        //else
        //{
        //	CF0 = sqrt(k)*pow(2.0/(k+1.0),(k+1.0)/(2.0*k-2.0))*
        //		sqrt(2.0*k/(k-1)*(1.0-pow(PePc,(k-1)/k)));
        //	t=CF0+AeAt*(PePc-PaPc);
        //	return t;


        //}




        //if(PePa<PiPa)      //出现气流分离
        //       goto loop1;
        //CF0 = sqrt(k)*pow(2.0/(k+1.0),(k+1.0)/(2.0*k-2.0))*
        //				sqrt(2.0*k/(k-1)*(1.0-pow(PePc,(k-1)/k)));
        //t=CF0+AeAt*(PePc-PaPc);
        //   return t;
        //  loop1:
        //   //ShowMessage("OK");
        //   double  PiPc=PiPa*PaPc;

        //   double  AiAt=pow(2.0/(k+1.0),1.0/(k-1.0))*pow((k-1.0)/(k+1.0),0.5)
        //       /(pow(PiPc,1/k)*sqrt(1.0-pow(PiPc,(k-1)/k)));

        //   double  a;
        //   if(AiAt<=(AeAt/1.604+0.377))
        //       a=(AiAt-1)/2.4;
        //   else
        //       a=(AeAt-AiAt)/1.45;

        //   double  A095At=AiAt+a;

        //   double  dCFs=0.55*(PiPc+0.95*PaPc)*a+0.975*PaPc*(AeAt-A095At);
        //   CF0 = sqrt(k)*pow(2.0/(k+1.0),(k+1.0)/(2.0*k-2.0))*
        //				sqrt(2.0*k/(k-1)*(1.0-pow(PiPc,(k-1)/k)))+AiAt*(PiPc-PaPc);
        //t=CF0+dCFs;//-(PiPc-PaPc)*AeAt;
        //   //ShowMessage(AnsiString(" CF0=")+CF0+" dCFS="+dCFs+" AiAt="+AiAt+" A095At="+A095At+" PiPc="+PiPc+" PaPc="+PaPc);
        //   return t;
        double PePc=PeOverPc(k,AeAt);

        double PePa=PePc/PaPc;
        double PiPa=0.6666666667*pow(PaPc,0.2);
        double CF0,t;
        if(PePa>=PiPa)
        {
            CF0 = sqrt(k)*pow(2.0/(k+1.0),(k+1.0)/(2.0*k-2.0))*
                    sqrt(2.0*k/(k-1)*(1.0-pow(PePc,(k-1)/k)));
            t=CF0+AeAt*(PePc-PaPc);
            return t;
        }
        //出现气流分离

        //ShowMessage("OK");
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
        t=CF0-dCFs;//-(PiPc-PaPc)*AeAt;
        //ShowMessage(AnsiString(" CF0=")+CF0+" dCFS="+dCFs+" AiAt="+AiAt+" A095At="+A095At+" PiPc="+PiPc+" PaPc="+PaPc);
        return t;
    }

    double  CaseThick(double  p,double  s, double  dc, double  phi, double  c)
    {
        return p*dc/(2.3*s*phi+p)+c;
    }

    //=============================================================
//								气体动力学函数
//-------------------------------------------------------------
    public double  PLamda(double  lamda,double  k)
    {
        return pow((1.0-(k-1.0)/(k+1.0)*lamda*lamda),k/(k-1.0));
    }

    public double  TLamda(double  lamda,double  k)
    {
        return 1.0-(k-1.0)/(k+1.0)*lamda*lamda;
    }
    public double  ELamda(double  lamda,double  k)
    {
        return pow((1.0-(k-1.0)/(k+1.0)*lamda*lamda),1.0/(k-1.0));
    }

    public double  YLamda(double  lamda,double  k)
    {
        return lamda*pow(0.5*(k+1.0),1.0/(k-1.0))/TLamda(lamda,k);
    }
    public double  FLamda(double  lamda,double  k)
    {
        return (1.0+lamda*lamda)*ELamda(lamda,k);
    }
    public double  RLamda(double  lamda,double  k)
    {
        return TLamda(lamda,k)/(1.0+lamda*lamda);
    }

    public double  MLamda(double  lamda,double  k)
    {
        return lamda*sqrt(2.0/(k+1.0)/(1.0-(k-1.0)/(k+1.0)*lamda*lamda));
    }

    public double  ZLamda(double  lamda)
    {
        return 0.5*(lamda+1.0/lamda);
    }

    public double  ZZLamda(double  lamda)
    {
        return (lamda+1.0/lamda);
    }

    public double  GetLamdaByMa(double  Ma,double  k)
    {
        return Ma*sqrt(0.5*(k+1.0)/(1+0.5*(k-1.0)*Ma*Ma));
    }
    public double  GetMaByLamda(double  lamda,double  k)
    {
        return sqrt((2*lamda*lamda/(k+1))/(1-lamda*lamda*(k-1)/(k+1)));
    }
    public double  QLamda(double  lamda,double  k)
    {
        return YLamda(lamda,k)*PLamda(lamda,k);
    }

//--------------------------------------------------------------------------
// P0: Pa, rho0: kg/m^3, T: K, a0: m/s
    /**
     * get atomspheric parameters at Hight : HE
     * @param HE 海拔高度 m
     * @param P0 大气压力 Pa
     */
    public void AirParas(double  HE,Double P0)
    {
        Double  T0=0.0;
        Double rho0=0.0;
        Double a0=0.0;
        Double gH=0.0;
        airParas(HE,T0,P0,rho0,a0,gH);
        P0=P0*1.0E-06;
    }
    /**
     * get atomspheric parameters at Hight : HE
     * @param HE 海拔高度 m
     * @param T0 大气温度 K
     * @param P0 大气压力 Pa
     * @param rho0 大气密度 kg/m^3
     * @param a0
     * @param gH
     */
    void airParas(double  HE,Double T0,Double P0,Double rho0,Double a0,Double gH)
    {
        double  HG=HE/(1.0+HE/6356766.0);
        double  T=288.15;
        double  PS=101325.0;
        double  RUS=1.225;
        double  Gravity=9.80665;
        double  W;

        if(HG<=11019.1)
        {
            W=1.0-HG/44330.8;
            T0=T*W;
            P0=PS*pow((double)W,5.2559);
            rho0=RUS*pow((double)W,4.2559);
        }
        else if(HG<=20063.1)
        {
            W=exp((14964.7-HG)/6341.6);
            T0=216.65;
            P0=PS*0.11953*W;
            rho0=RUS*0.15898*W;
        }
        else
        {
            W=1.0+(HG-24902.1)/2.21552E+05;
            T0=221.552*W;
            P0=PS*0.025158*pow((double)W,-34.1629);
            rho0=RUS*0.032722*pow((double)W,35.1629);
        }

        a0=20.0468*sqrt(T0);
        gH=Gravity/pow(1.0+HG/6.356766E+06,2.0);
    }
//-----------------------------------------------------------------------------
    /**
     * 用计算大气参数随海拔的变化
     */
    public class airParasClass {
        public double HE;
        public double T0;
        public double P0;
        public double rho0;
        public double a0;
        public double gH;
    }
    /**
     * 由高度计算大气压力
     * @param HE //海拔高度
     * @param a 大气参数类
     */
    public void AirParas(double  HE,airParasClass a)
    {   a.HE=HE;
        airParas(a);
        a.P0=a.P0*1.0E-06;
    }
    /**
     * 由上面的方法调用
     * @param a 大气参数类
     */
    void airParas(airParasClass a)
    {
        double  HG=a.HE/(1.0+a.HE/6356766.0);
        double  T=288.15;
        double  PS=101325.0;
        double  RUS=1.225;
        double  Gravity=9.80665;
        double  W;

        if(HG<=11019.1)
        {
            W=1.0-HG/44330.8;
            a.T0=T*W;
            a.P0=PS*pow((double)W,5.2559);
            a.rho0=RUS*pow((double)W,4.2559);
        }
        else if(HG<=20063.1)
        {
            W=exp((14964.7-HG)/6341.6);
            a.T0=216.65;
            a.P0=PS*0.11953*W;
            a.rho0=RUS*0.15898*W;
        }
        else
        {
            W=1.0+(HG-24902.1)/2.21552E+05;
            a.T0=221.552*W;
            a.P0=PS*0.025158*pow((double)W,-34.1629);
            a.rho0=RUS*0.032722*pow((double)W,35.1629);
        }

        a.a0=20.0468*sqrt(a.T0);
        a.gH=Gravity/pow(1.0+HG/6.356766E+06,2.0);
    }

//------------------------------------------------------------------------
    /**
     * calculate lamda by q(lamda), N=0 subsonic, N=1 supersonic
     * @param QY
     * @param K
     * @param N
     * @return
     */
    double  GetLamdaByQ(double  QY,double  K,int N)
    {
        double  A,B,C;
        if(abs(1.0-QY)<=0.001)
            return 1.0;
        if(N==0)
        {
            A=1.0;
            B=0;
        }
        else
        {
            A=1.0;
            B=2.4;
        }
        do{
            C=B-(QLamda(B,K)-QY)*(B-A)/(QLamda(B,K)-QLamda(A,K));
            A=B;
            B=C;

        }while(abs(QLamda(C,K)-QY)>0.0001);
        return C;
    }
//------------------------------------------------------------------------------
    /**
     * calculate the Mach number angle after delta angle reflection
     * @param delta
     * @param M
     * @param k
     * @return
     */
    public double  GetBeta(double  delta,double  M,double  k)
    {
        double  y,x1;
        x1 = 7.0*1.74532925199433E-002;
        do
        {
            x1 = x1+0.001;
            y  = (M*M*sin(x1)*sin(x1)-1)           //???
                    -(M*M*((k+1)/2-sin(x1)*sin(x1))+1)*tan(delta)*tan(x1);
        }while(abs(y)>1.0E-02);
        return(x1);
    }

//---------------------------------------------------------------------
    /**
     * calculate the Mach number after beta Mach angle
     * @param M
     * @param beta
     * @param k
     * @return
     */
    public double  GetM2AfterShock(double  M, double   beta,double  k)
    {
        double  tmp;
        tmp  = ( M*M+2.0/(k-1.0))/(2.0*k*M*M*sin(beta)*sin(beta)/(k-1.0)-1.0)
                + M*M*cos(beta)*cos(beta)/((k-1.0)*M*M*sin(beta)*sin(beta)/2.0+1.0);
        return(sqrt(tmp));
    }

//---------------------------------------------------------------------
    /**
     * calculate total pressure recovery coefficient after a shock
     * @param beta
     * @param M
     * @param k
     * @return
     */
    public double  GetSigmaP(double  beta,double  M,double  k)
    {
        double  u1,u2,v1,v2;
        u1  =  (k+1)*M*M*sin(beta)*sin(beta)/(2+(k-1)*M*M*sin(beta)*sin(beta));
        u2	 =  2*k*M*M*sin(beta)*sin(beta)/(k+1)-(k-1)/(k+1) ;
        v1	 =  pow(u1,k/(k-1));
        v2	 =  pow(u2,1/(k-1));
        return v1/v2;
    }

    //----------------------------------------------------------------------------
//对分法解方程
//----------------------------------------------------------------------------
    public double  FuncLamda1(double  lamda1,double  qlamda1,double  k)
    {
        return  pow((k+1.0)/2.0,1.0/(k-1.0))*lamda1*pow((1.0-(k-1.0)*lamda1*lamda1/(k+1.0)),1.0/(k-1.0))-qlamda1;
    }
    //----------------------------------------------------------------------------
    public double  FuncLamda2(double  lamda1,double  lamda2,double  Rc,double  Lc,double  f,double  k)
    {
        return  1.0/(lamda1*lamda1)+log(lamda1*lamda1)-1.0/(lamda2*lamda2)-log(lamda2*lamda2)-2.0*k/(k+1.0)*f*4.0*Lc/(2.0*Rc);
    }

    //----------------------------------------------------------------------------
    public double  FuncDuifen1(double  a,double  b,double  qlamda1,double  k) throws Exception
    {
        int Temp1OfThrow=0;
        if(FuncLamda1(a,qlamda1,k)*FuncLamda1(b,qlamda1,k)>0)
        {
            Exception ex = new Exception();
            throw ex;
            //throw Temp2OfThrow;
        }
        for(;true;)
        {if(abs(FuncLamda1((a+b)/2.0,qlamda1,k))<0.001)
            break;
            if(FuncLamda1((a+b)/2.0,qlamda1,k)*FuncLamda1(b,qlamda1,k)<0)
                a=(a+b)/2.0;
            else
                b=(a+b)/2.0;
        }
        return (a+b)/2.0;
    }

//----------------------------------------------------------------------------
    /**
     *
     * @param a
     * @param b
     * @param lamda1
     * @param Rc
     * @param Lc
     * @param f
     * @param k
     * @return
     * @throws Exception
     */
    double  FuncDuifen2(double  a,double  b,double  lamda1,double  Rc,double  Lc,double  f,double  k) throws Exception
    {
        int Temp2OfThrow=0;
        if(FuncLamda2(lamda1,a,Rc,Lc,f,k)*FuncLamda2(lamda1,b,Rc,Lc,f,k)>0)
        {
            Exception ex = new Exception();
            throw ex;
            //throw Temp2OfThrow;
        }
        for(;true;)
        {
            if(abs(FuncLamda2(lamda1,(a+b)/2.0,Rc,Lc,f,k))<0.001) break;

            if(FuncLamda2(lamda1,(a+b)/2.0,Rc,Lc,f,k)*FuncLamda2(lamda1,b,Rc,Lc,f,k)<0.0) a=(a+b)/2.0;
            else b=(a+b)/2.0;
        }
        return (a+b)/2.0;
    }
//----------------------------------------------------------------------------
    /**
     * 利用特征线法计算喷管的初始膨胀角
     * @param k
     * @param epsA
     * @return
     */
    public double  GetNozzleAlphaM(double  k,double  epsA)
    {
        double  a=1.0;
        double  b=a;

        for(;true;)
        {
            a=b;
            b+=0.001;
            double  x=a*epsA-pow(2.0*(1.0+(k-1.0)*a*a/2.0)/(k+1.0),(k+1.0)/(2.0*(k-1.0)));
            double  y=b*epsA-pow(2.0*(1.0+(k-1.0)*b*b/2.0)/(k+1.0),(k+1.0)/(2.0*(k-1.0)));
            if(x*y<=0.0)
                break;
        }
        double  M1=(a+b)/2.0;

        return (1.0/4.0*sqrt((k+1.0)/(k-1.0))*atan(sqrt((k-1.0)/(k+1.0))*sqrt(M1*M1-1.0))
                -1.0/4.0*atan(sqrt(M1*M1-1.0)))*180.0/3.1415926;
    }

    //----------------------------------------------------------------------------
    static double BASE_E=2.71828;
    /**
     * 应用塑性变形理论分析壳体爆破压力
     * @param R
     * @param Rt
     * @param sigmaB
     * @param sigmaS
     * @param E
     * @param Delta
     * @param mu
     * @return
     */
    public double  GetBreakPc(double  R,double  Rt,double  sigmaB,double  sigmaS,double  E,double  Delta,double  mu)
    {
        double  n=0.5;
        double  tmp;
        do{
            tmp=n;
            n=log(sigmaB/sigmaS)/(log(tmp/(sigmaS/E+0.002))-1.0);
        }while(abs(tmp-n)>0.0001);
        double  a=sigmaB*pow((double)(BASE_E/n),(double)n);
        double  beta2=0.5-0.5*(Rt*Rt/(R*R));
        double  beta3=0;
        double  alpha2=(2.0*beta2-1.0-beta3)/(2.0-beta2-beta3);
        double  lamda=sqrt(1-beta2+beta2*beta2);
        double  eps=2*lamda*n/3.0;
        double  sigma=sigmaB*pow((double)(1.5*BASE_E*lamda),(double)n);
        return (Delta/R)*sigma*exp(-n)/lamda;
    }
//----------------------------------------------------------------------------
    /**
     * 应用薄膜应力分析壳体爆破压力
     * @param R2
     * @param R1
     * @param sigmaB
     * @param Lc
     * @param delta
     * @param Delta
     * @param Phi
     * @param StrengthModal
     * @return
     */
    public double  GetBreakPc(double  R2,double  R1,double  sigmaB,double  Lc,double  delta,double  Delta,
                              double  Phi,int StrengthModal )
    {

        double        Y = sqrt(Lc*Lc+(R2-R1)*(R2-R1))/Lc;
        double  d=(Delta-delta)/Y;

        if(StrengthModal==1)
            return 2.3*Phi*sigmaB*d/(2*R2-d);
        else
            return 2.0*Phi*sigmaB*d/(2*R2-d);
    }

//----------------------------------------------------------------------------
    /**
     * 计算超声速段P/Pc与A/At和k的关系
     * @param k
     * @param AAt
     * @return
     */
    double  GetPPcByAAt(double  k,double  AAt)
    {
        double  ppc,ppcold;
        ppcold=ppc=0.5;
        do
        {
            ppcold=ppc;
            double pow1=pow((double) ppc,(double)((k+1)/k));
            double pow2=pow((double)(2.0/(k+1.0)),(double)(2.0/(k-1.0)))*(k-1.0)/(k+1.0)/(AAt*AAt);
            ppc=pow((double)(pow1+pow2),(double)k/2);
        }while(abs(ppc-ppcold)>0.00001);
        return ppc;
    }

}
