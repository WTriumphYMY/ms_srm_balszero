package cn.edu.nwpu.ms_srm_balszero.service;


import cn.edu.nwpu.ms_srm_balszero.domain.RocketParas;

public interface MotorService {
	void addMotor(RocketParas motor);
	RocketParas getMotorBySrmName(String srmName);
	void updateMotorBySrmName(RocketParas motor);
}
