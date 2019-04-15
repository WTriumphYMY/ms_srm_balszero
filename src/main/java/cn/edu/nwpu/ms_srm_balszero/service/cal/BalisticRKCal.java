package cn.edu.nwpu.ms_srm_balszero.service.cal;

import cn.edu.nwpu.ms_srm_balszero.domain.EnvironmentParas;
import cn.edu.nwpu.ms_srm_balszero.domain.PropellantIgParas;
import cn.edu.nwpu.ms_srm_balszero.domain.PropellantMainParas;
import cn.edu.nwpu.ms_srm_balszero.domain.RocketParas;
import cn.edu.nwpu.ms_srm_balszero.util.SRMCalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *使用四阶Runge-Kutta法计算内弹道
 * @author Trium
 */
public class BalisticRKCal {

    private PropellantIgParas propellant_ig; //点火药参数
    private PropellantMainParas propellant_main; //主装药参数
    private RocketParas rocketParas; //点火发动机结构参数
    private EnvironmentParas envParas; //环境参数
    private List<Double> burningArea_main; //主装药燃面
    private List<Double> web_main; //主装药肉厚
    private List<Double> burningArea_ig; //主装药燃面
    private List<Double> web_ig; //主装药肉厚

    private List<Double> t = new ArrayList<>(); //时间list
    private List<Double> pb = new ArrayList<>(); //燃烧室压强list
    private List<Double> temperature = new ArrayList<>(); //燃烧室内温度
    private List<Double> rou = new ArrayList<>(); //燃烧室内气体密度list
    private List<Double> force = new ArrayList<>(); //推力list
    private List<Double> vc = new ArrayList<>(); //燃烧室余容list
    private List<Double> massFractionig = new ArrayList<>(); //燃烧室内点火药燃气比例
    private List<Double> cf = new ArrayList<>(); //推力系数list
    private List<Double> qb = new ArrayList<>(); //主装药秒流量
    private List<Double> qt = new ArrayList<>(); //喷管流出
    private List<Double> qig = new ArrayList<>(); //点火药质量流率
    private List<Double> pIgniter = new ArrayList<>();
    private List<Double> dtt = new ArrayList<>();
    private List<Double> ebb = new ArrayList<>();
    private List<Double> abb = new ArrayList<>();
    private List<Double> eigg = new ArrayList<>();
    private List<Double> abig = new ArrayList<>();
    private Map<String,List> resultMap = new HashMap<>(); //压强，温度，时间等结果集

    //计算参数
    private double kb; //主装药燃气比热比
    private double Rgb; //主装药燃气气体常数
    private double Tpb; //主装药燃气温度
    private double densb; //主装药密度
    private double ab; //主装药燃速系数（MPa）
    private double nb; //主装药压强指数
    private double cstarb; //主装药特征速度
    private double Tburn; //主装药点燃温度
    private double kig; //点火药燃气比热比
    private double Rgig; //点火药燃气气体常数
    private double Tpig; //点火药燃气温度
    private double densig; //点火药密度
    private double aig; //点火药燃速系数（MPa）
    private double nig; //点火药压强指数
    private double cstarig; //点火药特征速度
    private double dtig; //点火发动机喷管喉径（直径，单位m）
    private double dtb; //主喷管喉径（直径，单位m）
    private double epsA; //主喷管膨胀比
    private double nozzleAreaig; //点火发动机喷管面积
    private double nozzleAreab; //主发动机喷管面积
    private double vc0; //燃烧室初始容积（单位m^3）
    private double xq; //燃烧室燃烧效率
    private double pOpen; //喷管堵盖打开压强
    private double ek; //喷管喉径烧蚀斜率
    private double k; //混合燃气比热比
    private double Rg; //混合燃气气体常数
    private double h; //计算时间步长
    private double cpb; //主装药燃气定压比热
    private double cvb; //主装药燃气定容比热
    private double cpig; //点火药燃气定压比热
    private double cvig; //点火药燃气定容比热
    private double cp; //混合燃气定压比热
    private double cv; //混合燃气定容比热
    private double coeIg; //点火修正系数


    public BalisticRKCal(PropellantIgParas propellant_ig, PropellantMainParas propellant_main, RocketParas rocketParas, EnvironmentParas envParas) {
        this.propellant_ig = propellant_ig;
        this.propellant_main = propellant_main;
        this.rocketParas = rocketParas;
        this.envParas = envParas;
        kig = propellant_ig.getK();
        Rgig = propellant_ig.getRg();
        Tpig = propellant_ig.getTp();
        densig = propellant_ig.getDens();
        aig = propellant_ig.getA();
        nig = propellant_ig.getN();
        cstarig = propellant_ig.getCstar();
        kb = propellant_main.getK();
        Rgb = propellant_main.getRg();
        Tpb = propellant_main.getTp();
        densb = propellant_main.getDens();
        ab = propellant_main.getA();
        nb = propellant_main.getN();
        cstarb = propellant_main.getCstar();
        Tburn = propellant_main.getTburn();
        dtig = rocketParas.getDt();
        dtb = rocketParas.getDt();
        epsA = rocketParas.getEpsA();
        nozzleAreaig = 0.25 * Math.PI * dtig * dtig; //点火发动机喷管面积
        nozzleAreab = 0.25 * Math.PI * dtb * dtb; //主发动机喷管面积
        vc0 = rocketParas.getVc0(); //燃烧室初始容积（单位m^3）
        xq = rocketParas.getXq(); //燃烧室燃烧效率
        pOpen = rocketParas.getpOpen(); //喷管堵盖打开压强
        ek = rocketParas.getEk(); //喷管喉径烧蚀斜率
        cpb = kb*Rgb/(kb-1);
        cvb = Rgb/(kb-1);
        cpig = kig*Rgig/(kig-1);
        cvig = Rgig/(kig-1);
        cp = cpig;
        cv = cvig;
        k = cp / cv;
        Rg = cp * (k-1)/k;
        coeIg = rocketParas.getCoeIg();
        h = envParas.getH();

        burningArea_main = propellant_main.getBurningArea();
        web_main = propellant_main.getWeb();
        burningArea_ig = propellant_ig.getBurningArea();
        web_ig = propellant_ig.getWeb();

    }

    public Map<String,List> runRKCalculate(){
        double eb = 0;
        double eig = 0;
        double rb;
        double rig;
        double pbig;
        boolean fireFlag = false; //判断主装药是否点燃，true为点燃
        boolean openFlag = false; //判断喷管堵盖是否打开
        int i = 0; //迭代次数
        double[] drou = new double[4]; //用于龙格库塔的密度斜率
        double[] dp = new double[4]; //用于龙格库塔的压强斜率
        double[] dn = new double[4]; //用于龙格库塔的质量分数斜率

        t.add(0.0); //时间list
        pb.add(envParas.getP0()); //燃烧室压强
        temperature.add(envParas.getTempw() + 273); //燃烧室内温度K
        rou.add(envParas.getRou()); //燃烧室内气体密度list
        force.add(0.0);
        cf.add(0.0);
        vc.add(vc0); //燃烧室余容list
        massFractionig.add(1.0); //燃烧室内点火药燃气比例
        qb.add(0.0); //主装药秒流量
        qt.add(0.0); //喷管流出
        qig.add(0.0); //点火药质量流率
        pIgniter.add(0.0);
        dtt.add(dtb);
        ebb.add(eb);
        abb.add(getBurningAreaB(eb));
        eigg.add(eig);
        abig.add(getBurningAreaIg(eig));
        while (eb < web_main.get(web_main.size()-1) || pb.get(i) > 3e5) {
            if (pb.get(i) >= pOpen) {
                openFlag = true;
            }
            if (temperature.get(i) > Tburn) {
                fireFlag = true;
            }

            pbig = SRMCalUtil.getPbBalance(aig/Math.pow(1e6, nig), nig, densig, cstarig, getBurningAreaIg(eig), nozzleAreaig);
            rig = (eig<=web_ig.get(web_ig.size()-1)) ? SRMCalUtil.getR(aig, nig, pbig/1e6, envParas.getTempw(), envParas.getTemp0()) : 0.0;
            drou[0] = getDrou(eig, eb, pb.get(i), rou.get(i), vc.get(i), fireFlag, openFlag, h);
            dp[0] = getDpb(massFractionig.get(i), eb, eig, pb.get(i), rou.get(i), fireFlag, vc.get(i), openFlag, h);
            dn[0] = getDmassFraction(eig, eb, pb.get(i), rou.get(i), vc.get(i), massFractionig.get(i), fireFlag);


            rb = (eb < web_main.get(web_main.size()-1)) ? SRMCalUtil.getR(ab, nb, (pb.get(i)+0.5*h*dp[0])/1e6, envParas.getTempw(), envParas.getTemp0()) : 0.0;
            drou[1] = getDrou(eig + rig*0.5*h, eb + rb*0.5*h, pb.get(i)+0.5*h*dp[0], rou.get(i)+0.5*h*drou[0], vc.get(i), fireFlag, openFlag, 0.5*h);
            dp[1] = getDpb(massFractionig.get(i)+0.5*h*dn[0], eb + rb*0.5*h, eig + rig*0.5*h, pb.get(i)+0.5*h*dp[0], rou.get(i)+0.5*h*drou[0], fireFlag, vc.get(i), openFlag, 0.5*h);
            dn[1] = getDmassFraction(eig + rig*0.5*h, eb + rb*0.5*h, pb.get(i)+0.5*h*dp[0], rou.get(i)+0.5*h*drou[0], vc.get(i), massFractionig.get(i)+0.5*h*dn[0], fireFlag);


            rb = (eb < web_main.get(web_main.size()-1)) ? SRMCalUtil.getR(ab, nb, (pb.get(i)+0.5*h*dp[1])/1e6, envParas.getTempw(), envParas.getTemp0()) : 0.0;
            drou[2] = getDrou(eig + rig*0.5*h, eb + rb*0.5*h, pb.get(i)+0.5*h*dp[1], rou.get(i)+0.5*h*drou[1], vc.get(i), fireFlag, openFlag, 0.5*h);
            dp[2] = getDpb(massFractionig.get(i)+0.5*h*dn[1], eb + rb*0.5*h, eig + rig*0.5*h, pb.get(i)+0.5*h*dp[1], rou.get(i)+0.5*h*drou[1], fireFlag, vc.get(i), openFlag, 0.5*h);
            dn[2] = getDmassFraction(eig + rig*0.5*h, eb + rb*0.5*h, pb.get(i)+0.5*h*dp[1], rou.get(i)+0.5*h*drou[1], vc.get(i), massFractionig.get(i)+0.5*h*dn[1], fireFlag);

            rb = (eb < web_main.get(web_main.size()-1)) ? SRMCalUtil.getR(ab, nb, (pb.get(i)+h*dp[2])/1e6, envParas.getTempw(), envParas.getTemp0()) : 0.0;
            drou[3] = getDrou(eig + rig*h, eb + rb*h, pb.get(i)+h*dp[2], rou.get(i)+h*drou[2], vc.get(i), fireFlag, openFlag, h);
            dp[3] = getDpb(massFractionig.get(i)+h*dn[2], eb + rb*h, eig + rig*h, pb.get(i)+h*dp[2], rou.get(i)+h*drou[2], fireFlag, vc.get(i), openFlag, h);
            dn[3] = getDmassFraction(eig + rig*h, eb + rb*h, pb.get(i)+h*dp[2], rou.get(i)+h*drou[2], vc.get(i), massFractionig.get(i)+h*dn[2], fireFlag);

            pb.add(pb.get(i) + h*(dp[0] + 2*dp[1] + 2*dp[2] + dp[3]) / 6);
            rou.add(rou.get(i) + h*(drou[0] + 2*drou[1] + 2*drou[2] + drou[3]) / 6);
            massFractionig.add(massFractionig.get(i) + h*(dn[0] + 2*dn[1] + 2*dn[2] + dn[3]) / 6);
            temperature.add(pb.get(i+1)/rou.get(i+1)/getRg(massFractionig.get(i+1)));
            vc.add(getVc(eb, vc.get(i), 0.5*(pb.get(i)+pb.get(i+1)),h)); //燃烧室余容list
            t.add(t.get(i)+h);
            cf.add(SRMCalUtil.CoeForce(getK(massFractionig.get(i+1)), epsA, envParas.getP0()/pb.get(i+1)));
            force.add(cf.get(i+1) * pb.get(i+1) * 0.25 * (dtb + ek*t.get(i+1)) * (dtb + ek*t.get(i+1)) * Math.PI);
            //肉厚增加
            if (fireFlag) eb += h * SRMCalUtil.getR(ab, nb, (0.5*(pb.get(i)+pb.get(i+1)))/1e6, envParas.getTempw(), envParas.getTemp0());
            eig += h*rig;

            pIgniter.add(pbig);
            double qtt = getQt(pb.get(i+1), openFlag);
            double qbb = getQb(eb, pb.get(i+1), fireFlag);
            double qigg = getQig(eig);
            qt.add(qtt);
            qb.add(qbb);
            qig.add(qigg);
            dtt.add(dtb + ek*t.get(t.size()-1));
            ebb.add(eb);
            abb.add(getBurningAreaB(eb));
            eigg.add(eig);
            abig.add(getBurningAreaIg(eig));
            i++;
        }
        resultMap.put("pb", pb);
        resultMap.put("t", t);
        resultMap.put("temperature", temperature);
        resultMap.put("rou", rou);
        resultMap.put("pIgniter", pIgniter);
        resultMap.put("qt", qt);
        resultMap.put("qb", qb);
        resultMap.put("qig", qig);
        resultMap.put("dt", dtt);
        resultMap.put("vc", vc);
        resultMap.put("Nig", massFractionig);
        resultMap.put("eb", ebb);
        resultMap.put("ab", abb);
        resultMap.put("eig", eigg);
        resultMap.put("abig", abig);
        resultMap.put("cf", cf);
        resultMap.put("F", force);
        return  resultMap;
    }

    private double getQig(double e){
        double pbig = SRMCalUtil.getPbBalance(aig/Math.pow(1e6, nig), nig, densig, cstarig, getBurningAreaIg(e), nozzleAreaig);
        return SRMCalUtil.getQt(pbig, nozzleAreaig, cstarig);
    }

    private double getQb(double e, double pb, boolean fireFlag){
        if (fireFlag) {
            return densb * SRMCalUtil.getR(ab, nb, pb/1e6, envParas.getTempw(), envParas.getTemp0()) * getBurningAreaB(e);
        }else{
            return  0.0;
        }
    }

    private double getDpb(double massFrac, double eb, double eig, double pb, double rou, boolean fireFlag, double vc, boolean openFlag, double time){
        double k = getK(massFrac);
        double Rg = getRg(massFrac);
        double dp = ((k-1)*(coeIg * kig*getQig(eig)*Rgig*Tpig/(kig-1))
                + (k-1)*(kb*getQb(eb, pb, fireFlag)*Rgb*xq*Tpb/(kb-1) - k*getQt(pb,openFlag)*pb/rou/(k-1))
                - getBurningAreaB(eb) * SRMCalUtil.getR(ab, nb, pb/1e6, envParas.getTempw(), envParas.getTemp0()) * pb)
                / getVc(eb, vc, pb, time);
        return dp;
    }

    private double getDrou(double eig, double eb, double pb, double rou, double vc, boolean fireFlag, boolean openFlag, double time){
        double drou = (getQig(eig) + getQb(eb, pb, fireFlag) - getQt(pb,openFlag) - getBurningAreaB(eb) * SRMCalUtil.getR(ab, nb, pb/1e6, envParas.getTempw(), envParas.getTemp0()) * rou) / getVc(eb, vc, pb,time);
        return drou;
    }

    private double getDmassFraction(double eig, double eb, double pb, double rou, double vc, double massFrac, boolean fireFlag){
        double dn = (getQig(eig) - massFrac*(getQig(eig)+getQb(eb, pb, fireFlag))) / rou / vc;
        return dn;
    }

    private double getVc(double eb, double vc, double pb ,double time){
        return vc + getBurningAreaB(eb) * SRMCalUtil.getR(ab, nb, pb/1e6, envParas.getTempw(), envParas.getTemp0()) * time;
    }

    private double getQt(double pb, boolean openFlag){
        double dt;
        double coe = 1;
        if (openFlag) {
            dt = dtb + ek*t.get(t.size()-1);
            nozzleAreab = 0.25 * dt * dt * Math.PI;
            if(t.get(t.size()-1) < 0.1){
                coe = 1;
            }else{
                coe = 1;
            }
            return coe * SRMCalUtil.getQt(pb, nozzleAreab, cstarb);
        }else{
            return  0.0;
        }
    }

    private double getK(double massFraig){
        return (massFraig*cpig + (1-massFraig)*cpb) / (massFraig*cvig + (1-massFraig)*cvb);
    }

    private double getRg(double massFraig){
        return  (massFraig*cpig + (1-massFraig)*cpb) * (getK(massFraig)-1)/getK(massFraig);
    }

    private double getBurningAreaIg(double e){
        double area = 0.0;
        if (e < web_ig.get(web_ig.size()-1)) {
            for (int i = 0; i<web_ig.size()-1; i++) {
                if (e >= web_ig.get(i) && e < web_ig.get(i+1)) {
                    area = burningArea_ig.get(i) + (burningArea_ig.get(i+1)-burningArea_ig.get(i))/(web_ig.get(i+1)-web_ig.get(i))*(e-web_ig.get(i));
                    return area;
                }
            }
            return area;
        }else{
            return area;
        }
    }

    private double getBurningAreaB(double e){
        double area = 0.0;
        if (e < web_main.get(web_main.size()-1)) {
            for (int i = 0; i<web_main.size()-1; i++) {
                if (e >= web_main.get(i) && e < web_main.get(i+1)) {
                    area = burningArea_main.get(i) + (burningArea_main.get(i+1)-burningArea_main.get(i))/(web_main.get(i+1)-web_main.get(i))*(e-web_main.get(i));
                    return area;
                }
            }
            return area;
        }else{
            return area;
        }
    }



}
