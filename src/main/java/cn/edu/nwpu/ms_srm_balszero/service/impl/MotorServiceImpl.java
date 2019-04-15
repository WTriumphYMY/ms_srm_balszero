package cn.edu.nwpu.ms_srm_balszero.service.impl;

import cn.edu.nwpu.ms_srm_balszero.domain.RocketParas;
import cn.edu.nwpu.ms_srm_balszero.repository.MotorRepository;
import cn.edu.nwpu.ms_srm_balszero.service.MotorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MotorServiceImpl implements MotorService {

	@Autowired
	private MotorRepository motorRepository;


	@Override
	public void addMotor(RocketParas motor) {
		motorRepository.save(motor);
	}

	@Override
	public RocketParas getMotorBySrmName(String srmName) {
		return motorRepository.findBySrmName(srmName);
	}

	@Override
	public void updateMotorBySrmName(RocketParas motor) {
		RocketParas oldMotor = getMotorBySrmName(motor.getSrmName());
		motor.setRocketId(oldMotor.getRocketId());
		motorRepository.save(motor);
	}
}
