package cn.edu.nwpu.ms_srm_balszero.domain;

import javax.persistence.*;
import java.util.List;

/**
 * 表征点火药参数的pojo类
 * @author Trium
 */
@Entity
@Table(name = "tb_propig")
public class PropellantIgParas {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "igprop_id")
    private Integer igPropId;

    @Column(name = "igprop_k")
    private double k; //燃气比热比

    @Column(name = "igprop_rg")
    private double Rg; //燃气气体常数

    @Column(name = "igprop_tp")
    private double Tp; //燃烧温度

    @Column(name = "igprop_dens")
    private double dens; //推进剂密度

    @Column(name = "igprop_a")
    private double a; //推进剂燃速系数（m/s，MPa）

    @Column(name = "igprop_n")
    private double n; //推进剂压力指数

    @Column(name = "igprop_cs")
    private double cstar; //推进剂特征速度

    @Column(name = "srm_name")
    private String srmName; //发动机名字

    @Transient
    private List<Double> burningArea; //燃面
    @Transient
    private List<Double> web; //肉厚

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getRg() {
        return Rg;
    }

    public void setR(double Rg) {
        this.Rg = Rg;
    }

    public double getTp() {
        return Tp;
    }

    public void setTp(double Tp) {
        this.Tp = Tp;
    }

    public double getDens() {
        return dens;
    }

    public void setDens(double dens) {
        this.dens = dens;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getCstar() {
        return cstar;
    }

    public void setCstar(double cstar) {
        this.cstar = cstar;
    }

    public void setRg(double rg) {
        Rg = rg;
    }

    public String getSrmName() {
        return srmName;
    }

    public void setSrmName(String srmName) {
        this.srmName = srmName;
    }

    public Integer getIgPropId() {
        return igPropId;
    }

    public void setIgPropId(Integer igPropId) {
        this.igPropId = igPropId;
    }

    public List<Double> getBurningArea() {
        return burningArea;
    }

    public void setBurningArea(List<Double> burningArea) {
        this.burningArea = burningArea;
    }

    public List<Double> getWeb() {
        return web;
    }

    public void setWeb(List<Double> web) {
        this.web = web;
    }
}
