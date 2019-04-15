package cn.edu.nwpu.ms_srm_balszero.repository;

import cn.edu.nwpu.ms_srm_balszero.domain.EnvironmentParas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvRepository extends JpaRepository<EnvironmentParas, Integer> {
    EnvironmentParas findBySrmName(String srmName);
}
