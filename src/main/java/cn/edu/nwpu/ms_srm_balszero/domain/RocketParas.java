package cn.edu.nwpu.ms_srm_balszero.domain;

import javax.persistence.*;

/**
 *发动机结构参数的pojo类
 * @author Trium
 */
@Entity
@Table(name = "tb_motor")
public class RocketParas {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "motor_id")
    private Integer rocketId;

    @Column(name = "motor_dt")
    private double dt; //喷管喉径（直径，单位m）

    @Column(name = "motor_dtig")
    private double dtig; //喷管喉径（直径，单位m）

    @Column(name = "motor_vc")
    private double vc0; //燃烧室初始容积（单位m^3）

    @Column(name = "motor_xq")
    private double xq; //燃烧室燃烧效率

    @Column(name = "motor_popen")
    private double pOpen; //喷管堵盖打开压强

    @Column(name = "motor_ek")
    private double ek; //喷管喉径烧蚀斜率

    @Column(name = "motor_ekt")
    private double ekTime; //开始烧蚀时间

    @Column(name = "motor_epsa")
    private double epsA; //喷管膨胀比（单位m）

    @Column(name = "motor_coeig")
    private double coeIg; //点火修正系数

    @Column(name = "srm_name")
    private String srmName;

    public double getDt() {
        return dt;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }

    public double getVc0() {
        return vc0;
    }

    public void setVc0(double vc0) {
        this.vc0 = vc0;
    }

    public double getXq() {
        return xq;
    }

    public void setXq(double xq) {
        this.xq = xq;
    }

    public double getpOpen() {
        return pOpen;
    }

    public void setpOpen(double pOpen) {
        this.pOpen = pOpen;
    }

    public double getEk() {
        return ek;
    }

    public void setEk(double ek) {
        this.ek = ek;
    }

    public double getEpsA() {
        return epsA;
    }

    public void setEpsA(double epsA) {
        this.epsA = epsA;
    }

    public double getDtig() {
        return dtig;
    }

    public void setDtig(double dtig) {
        this.dtig = dtig;
    }

    public double getCoeIg() {
        return coeIg;
    }

    public void setCoeIg(double coeIg) {
        this.coeIg = coeIg;
    }

    public double getEkTime() {
        return ekTime;
    }

    public void setEkTime(double ekTime) {
        this.ekTime = ekTime;
    }

    public Integer getRocketId() {
        return rocketId;
    }

    public void setRocketId(Integer rocketId) {
        this.rocketId = rocketId;
    }

    public String getSrmName() {
        return srmName;
    }

    public void setSrmName(String srmName) {
        this.srmName = srmName;
    }
}
