package cn.edu.nwpu.ms_srm_balszero.service;


import cn.edu.nwpu.ms_srm_balszero.domain.PropellantMainParas;

public interface PropMainService {
    void addPropmain(PropellantMainParas propMain);
    PropellantMainParas getPropmainBySrmName(String srmName);
    void updatePropmainBySrmName(PropellantMainParas propMain);
}
