package cn.edu.nwpu.ms_srm_balszero.repository;

import cn.edu.nwpu.ms_srm_balszero.domain.BalsResult;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @InterfaceName BalsResultRepository
 * @Author: wkx
 * @Date: 2019/4/15 15:17
 * @Version: v1.0
 * @Description:
 */
public interface BalsResultRepository extends JpaRepository<BalsResult, Integer> {
    BalsResult findBySrmName(String srmName);
}
