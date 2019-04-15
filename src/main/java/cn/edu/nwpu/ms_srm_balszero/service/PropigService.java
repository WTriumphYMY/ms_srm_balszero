package cn.edu.nwpu.ms_srm_balszero.service;

import cn.edu.nwpu.ms_srm_balszero.domain.PropellantIgParas;

public interface PropigService {

	void addPropig(PropellantIgParas propIg);
	PropellantIgParas getPropigBySrmName(String srmName);
	void updatePropigBySrmName(PropellantIgParas propIg);
}
