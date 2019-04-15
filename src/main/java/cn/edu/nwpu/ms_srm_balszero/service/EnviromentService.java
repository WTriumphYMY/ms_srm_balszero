package cn.edu.nwpu.ms_srm_balszero.service;


import cn.edu.nwpu.ms_srm_balszero.domain.EnvironmentParas;

public interface EnviromentService {

	void addEnv(EnvironmentParas env);
	EnvironmentParas getEnvBySrmName(String srmName);
	void updateEnvBySrmName(EnvironmentParas env);
}
