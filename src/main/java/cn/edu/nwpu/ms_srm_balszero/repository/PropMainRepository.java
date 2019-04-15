package cn.edu.nwpu.ms_srm_balszero.repository;


import cn.edu.nwpu.ms_srm_balszero.domain.PropellantMainParas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropMainRepository extends JpaRepository<PropellantMainParas, Integer> {
    PropellantMainParas findBySrmName(String srmName);
}
