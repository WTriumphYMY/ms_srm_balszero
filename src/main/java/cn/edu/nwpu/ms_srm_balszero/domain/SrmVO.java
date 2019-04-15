package cn.edu.nwpu.ms_srm_balszero.domain;

public class SrmVO {

	private PropellantMainParas propmain;
	private PropellantIgParas propig;
	private RocketParas motor;
	private EnvironmentParas env;

	public PropellantMainParas getPropmain() {
		return propmain;
	}

	public void setPropmain(PropellantMainParas propmain) {
		this.propmain = propmain;
	}

	public PropellantIgParas getPropig() {
		return propig;
	}

	public void setPropig(PropellantIgParas propig) {
		this.propig = propig;
	}

	public RocketParas getMotor() {
		return motor;
	}

	public void setMotor(RocketParas motor) {
		this.motor = motor;
	}

	public EnvironmentParas getEnv() {
		return env;
	}

	public void setEnv(EnvironmentParas env) {
		this.env = env;
	}
}
