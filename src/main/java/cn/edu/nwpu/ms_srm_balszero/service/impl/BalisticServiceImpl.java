package cn.edu.nwpu.ms_srm_balszero.service.impl;

import cn.edu.nwpu.ms_srm_balszero.domain.*;
import cn.edu.nwpu.ms_srm_balszero.service.BalisticService;
import cn.edu.nwpu.ms_srm_balszero.service.cal.BalisticRKCal;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class BalisticServiceImpl implements BalisticService {

	@Override
	public Map<String, List> calBals(SrmVO srmVO, File igGrain, File mainGrain) throws Exception {
	 	PropellantMainParas mainProp = new PropellantMainParas();
        mainProp.setA(srmVO.getPropmain().getA());
        mainProp.setCstar(srmVO.getPropmain().getCstar());
        mainProp.setDens(srmVO.getPropmain().getDens());
        mainProp.setK(srmVO.getPropmain().getK());
        mainProp.setN(srmVO.getPropmain().getN());
        mainProp.setR(srmVO.getPropmain().getRg());
        mainProp.setTp(srmVO.getPropmain().getTp());
        mainProp.setTburn(srmVO.getPropmain().getTburn());
        List<Double> areaMain = new ArrayList<>();
        List<Double> webMain = new ArrayList<>();
        BufferedReader br1 = Files.newBufferedReader(mainGrain.toPath());
        String line1 = null;
        while ((line1 = br1.readLine()) != null) {            
            if (line1.split(",").length == 11) {
                webMain.add(Double.parseDouble(line1.split(",")[0]));
                areaMain.add(Double.parseDouble(line1.split(",")[1]));
            }
        }
        mainProp.setBurningArea(areaMain);
        mainProp.setWeb(webMain);
        
        RocketParas rocket = new RocketParas();
        rocket.setDt(srmVO.getMotor().getDt());
        rocket.setEk(srmVO.getMotor().getEk());
        rocket.setEkTime(srmVO.getMotor().getEkTime());
        rocket.setVc0(srmVO.getMotor().getVc0());
        rocket.setXq(srmVO.getMotor().getXq());
        rocket.setpOpen(srmVO.getMotor().getpOpen());
        rocket.setEpsA(srmVO.getMotor().getEpsA());
        rocket.setDtig(srmVO.getMotor().getDtig());
        rocket.setCoeIg(srmVO.getMotor().getCoeIg());
        
        PropellantIgParas igProp = new PropellantIgParas();
        igProp.setA(srmVO.getPropig().getA());
        igProp.setCstar(srmVO.getPropig().getCstar());
        igProp.setDens(srmVO.getPropig().getDens());
        igProp.setK(srmVO.getPropig().getK());
        igProp.setN(srmVO.getPropig().getN());
        igProp.setR(srmVO.getPropig().getRg());
        igProp.setTp(srmVO.getPropig().getTp());
        List<Double> areaIg = new ArrayList<>();
        List<Double> webIg = new ArrayList<>();
        BufferedReader br2 = Files.newBufferedReader(igGrain.toPath());
        String line2 = null;
        while ((line2 = br2.readLine()) != null) {            
            if (line2.split(",").length == 11) {
                webIg.add(Double.parseDouble(line2.split(",")[0]));
                areaIg.add(Double.parseDouble(line2.split(",")[1]));
            }
        }
        igProp.setBurningArea(areaIg);
        igProp.setWeb(webIg);  
        
        EnvironmentParas envParas = new EnvironmentParas();
        envParas.setP0(srmVO.getEnv().getP0());
        envParas.setRa(srmVO.getEnv().getRa());
        envParas.setTemp0(srmVO.getEnv().getTemp0());
        envParas.setTempw(srmVO.getEnv().getTempw());
        envParas.setH(srmVO.getEnv().getH());
        
        BalisticRKCal calInstance = new BalisticRKCal(igProp, mainProp, rocket, envParas);
        Map<String, List> result = calInstance.runRKCalculate();
		return result;
	}

}
