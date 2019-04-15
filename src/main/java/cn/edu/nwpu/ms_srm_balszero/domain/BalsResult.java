package cn.edu.nwpu.ms_srm_balszero.domain;

import javax.persistence.*;

/**
 * @ClassName BalsResult
 * @Author: wkx
 * @Date: 2019/4/15 15:11
 * @Version: v1.0
 * @Description: 存储零维内弹道的实体类
 */
@Entity
@Table(name = "tb_result")
public class BalsResult {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "pk_id")
    @Id
    private Integer pkId;

    @Column(name = "result")
    private String result;

    @Column(name = "srm_name")
    private String srmName;

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSrmName() {
        return srmName;
    }

    public void setSrmName(String srmName) {
        this.srmName = srmName;
    }
}
