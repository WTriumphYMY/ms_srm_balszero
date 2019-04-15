package cn.edu.nwpu.ms_srm_balszero.service.impl;

import cn.edu.nwpu.ms_srm_balszero.domain.PropellantIgParas;
import cn.edu.nwpu.ms_srm_balszero.repository.PropigRepository;
import cn.edu.nwpu.ms_srm_balszero.service.PropigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PropigServiceImpl implements PropigService {

	@Autowired
	private PropigRepository propigRepository;


	@Override
	public void addPropig(PropellantIgParas propIg) {
		propigRepository.save(propIg);
	}

	@Override
	public PropellantIgParas getPropigBySrmName(String srmName) {
		return propigRepository.findBySrmName(srmName);
	}

	@Override
	public void updatePropigBySrmName(PropellantIgParas propIg) {
		PropellantIgParas oldPropig = getPropigBySrmName(propIg.getSrmName());
		propIg.setIgPropId(oldPropig.getIgPropId());
		propigRepository.save(propIg);
	}
}
