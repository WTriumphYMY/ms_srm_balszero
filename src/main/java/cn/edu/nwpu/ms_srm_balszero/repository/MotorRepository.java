package cn.edu.nwpu.ms_srm_balszero.repository;

import cn.edu.nwpu.ms_srm_balszero.domain.RocketParas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotorRepository extends JpaRepository<RocketParas, Integer> {
    RocketParas findBySrmName(String srmName);
}
