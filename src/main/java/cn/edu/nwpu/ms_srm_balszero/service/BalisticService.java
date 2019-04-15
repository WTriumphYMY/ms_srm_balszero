package cn.edu.nwpu.ms_srm_balszero.service;


import cn.edu.nwpu.ms_srm_balszero.domain.SrmVO;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface BalisticService {

	Map<String, List> calBals(SrmVO srmVO, File igGrain, File mainGrain) throws Exception;
}
