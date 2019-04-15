package cn.edu.nwpu.ms_srm_balszero.repository;

import cn.edu.nwpu.ms_srm_balszero.domain.PropellantIgParas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropigRepository extends JpaRepository<PropellantIgParas, Integer> {
    PropellantIgParas findBySrmName(String srmName);
}
