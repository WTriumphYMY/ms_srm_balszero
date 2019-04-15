package cn.edu.nwpu.ms_srm_balszero.domain;

import javax.persistence.*;
import java.util.List;

/**
 * 表征推进剂参数的pojo类
 * @author Trium
 */
@Entity
@Table(name = "tb_propmain")
public class PropellantMainParas {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "mainprop_id")
    private Integer mainPropId;

    @Column(name = "mainprop_k")
    private double k; //燃气比热比

    @Column(name = "mainprop_rg")
    private double Rg; //燃气气体常数

    @Column(name = "mainprop_tp")
    private double Tp; //燃烧温度

    @Column(name = "mainprop_dens")
    private double dens; //推进剂密度

    @Column(name = "mainprop_a")
    private double a; //推进剂燃速系数（m/s，MPa）

    @Column(name = "mainprop_n")
    private double n; //推进剂压力指数

    @Column(name = "mainprop_cs")
    private double cstar; //推进剂特征速度

    @Column(name = "srm_name")
    private String srmName; //发动机名字

    @Column(name = "mainprop_tb")
    private double Tburn; //装药点燃温度

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

    public double getTburn() {
        return Tburn;
    }

    public void setTburn(double Tburn) {
        this.Tburn = Tburn;
    }

    public Integer getMainPropId() {
        return mainPropId;
    }

    public void setMainPropId(Integer mainPropId) {
        this.mainPropId = mainPropId;
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
