package cn.edu.nwpu.ms_srm_balszero.domain;

import javax.persistence.*;

/**
 *
 * @author Trium
 */
@Entity
@Table(name = "tb_env")
public class EnvironmentParas {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "env_id")
    private Integer envId;

    @Column(name = "env_ra")
    private double Ra; //空气气体常数

    @Column(name = "env_pa")
    private double p0; //环境压强

    @Column(name = "env_t0")
    private double temp0; //环境温度

    @Column(name = "env_tw")
    private double tempw; //工作温度

    @Column(name = "env_h")
    private double h;

    @Column(name = "srm_name")
    private String srmName;

    public double getRa() {
        return Ra;
    }

    public void setRa(double Ra) {
        this.Ra = Ra;
    }

    public double getP0() {
        return p0;
    }

    public void setP0(double p0) {
        this.p0 = p0;
    }

    public double getTemp0() {
        return temp0;
    }

    public void setTemp0(double temp0) {
        this.temp0 = temp0;
    }

    public double getRou() {
        return p0/Ra/temp0;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public Integer getEnvId() {
        return envId;
    }

    public void setEnvId(Integer envId) {
        this.envId = envId;
    }

    public String getSrmName() {
        return srmName;
    }

    public void setSrmName(String srmName) {
        this.srmName = srmName;
    }

    public double getTempw() {
        return tempw;
    }

    public void setTempw(double tempw) {
        this.tempw = tempw;
    }





}
