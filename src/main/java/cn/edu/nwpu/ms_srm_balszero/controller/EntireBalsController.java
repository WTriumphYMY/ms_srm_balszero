package cn.edu.nwpu.ms_srm_balszero.controller;

import cn.edu.nwpu.ms_srm_balszero.domain.*;
import cn.edu.nwpu.ms_srm_balszero.repository.BalsResultRepository;
import cn.edu.nwpu.ms_srm_balszero.service.*;
import com.alibaba.druid.support.json.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * 全过程内弹道控制器
 */
@RestController
public class EntireBalsController {

    @Autowired
    private PropMainService propMainService;
    @Autowired
    private PropigService propigService;
    @Autowired
    private MotorService motorService;
    @Autowired
    private EnviromentService enviromentService;
    @Autowired
    private BalisticService balisticService;
    @Autowired
    private BalsResultRepository balsResultRepository;


    @PostMapping("/module/bals")
    public Map<String, List> calculateBals(SrmVO srmVO, @RequestParam("srmName") String srmName,
                                           @RequestParam("grains") MultipartFile[] grains, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.addHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
        saveData(srmVO, srmName);
        String igGrainName = UUID.randomUUID() + "igprop.grain";
        String mainGrainName = UUID.randomUUID() + "mainprop.grain";
        String path = null;
        try {
            path = ResourceUtils.getURL("classpath:").getPath() + "static/grain/";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File iggrainPath = new File(path,igGrainName);
        File maingrainPath = new File(path, mainGrainName);
        //判断路径是否存在，如果不存在就创建一个
        if (!iggrainPath.getParentFile().exists()) {
            iggrainPath.getParentFile().mkdirs();
        }
        //判断文件是否存在，存在就把原来的删了
        Map<String, List> result;
        List<Boolean> success = new ArrayList<>();
        List<String> message = new ArrayList<>();
        try {
            Files.deleteIfExists(iggrainPath.toPath());
            Files.deleteIfExists(maingrainPath.toPath());
            grains[0].transferTo(iggrainPath);
            grains[1].transferTo(maingrainPath);
//            igGrain.transferTo(iggrainPath);
//            mainGrain.transferTo(maingrainPath);
        } catch (IOException e) {
            result = new HashMap<>();
            success.add(false);
            message.add("服务器向您抛出了一个问题，请联系管理员");
            e.printStackTrace();
            result.put("success", success);
            result.put("message", message);
            return result;
        }

        //计算内弹道，返回Map
        try {
            result = balisticService.calBals(srmVO, iggrainPath, maingrainPath);
            saveResult(result, srmName);
            success.add(true);
            message.add("可以查看结果啦");
        }catch (Exception e){
            result = new HashMap<>();
            success.add(false);
            message.add("请检查参数是否合理");
        }

        result.put("success", success);
        result.put("message", message);

        return result;

    }

    @PostMapping("/module/bals/blur")
    public SrmVO getData(String srmName) {
        SrmVO srmVO = new SrmVO();
        PropellantMainParas tbPropmain = propMainService.getPropmainBySrmName(srmName);
        PropellantIgParas tbPropig = propigService.getPropigBySrmName(srmName);
        RocketParas tbMotor = motorService.getMotorBySrmName(srmName);
        EnvironmentParas tbEnv = enviromentService.getEnvBySrmName(srmName);
        srmVO.setPropmain(tbPropmain);
        srmVO.setPropig(tbPropig);
        srmVO.setMotor(tbMotor);
        srmVO.setEnv(tbEnv);
        return srmVO;
    }


    /**
     * 将全过程内弹道中发动机的参数持久化到数据库
     * @param srmVO 发动机参数的包装类
     */
    public void saveData(SrmVO srmVO, String srmName){
        //如果数据库中已有此型号发动机，则更新参数，没有就新增
        PropellantMainParas propMain = srmVO.getPropmain();
        propMain.setSrmName(srmName);
        if (propMainService.getPropmainBySrmName(srmName) == null){
            propMainService.addPropmain(propMain);
        }else {
            propMainService.updatePropmainBySrmName(propMain);
        }

        PropellantIgParas propIg = srmVO.getPropig();
        propIg.setSrmName(srmName);
        if (propigService.getPropigBySrmName(srmName) == null){
            propigService.addPropig(propIg);
        }else {
            propigService.updatePropigBySrmName(propIg);
        }

        RocketParas motor = srmVO.getMotor();
        motor.setSrmName(srmName);
        if (motorService.getMotorBySrmName(srmName) == null){
            motorService.addMotor(motor);
        }else {
            motorService.updateMotorBySrmName(motor);
        }

        EnvironmentParas env = srmVO.getEnv();
        env.setSrmName(srmName);
        if (enviromentService.getEnvBySrmName(srmName) == null){
            enviromentService.addEnv(env);
        }else {
            enviromentService.updateEnvBySrmName(env);
        }
    }

    /**
     * 其他调用查看已算出来的结果
     * @return
     */
    @PostMapping("/getCalculatedResultList")
    public List getCalculatedResultList(){
        List<String> savedBalsNames = new ArrayList<>();
        for (BalsResult balsResult : balsResultRepository.findAll()) {
            savedBalsNames.add(balsResult.getSrmName());
        }
        return savedBalsNames;
    }

    /**
     * 思路，先用零维内弹道模块算，放到map里，其他模块调用时先能看到已经算出来的型号，然后再选择
     * @return
     */
    @GetMapping("/bals/{srmname}")
    public String getBalsResult(@PathVariable String srmName){
        return balsResultRepository.findBySrmName(srmName).getResult();
    }

//    @PostMapping(value = "/bals/calzero", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public String feignCalBals(@RequestPart("grains") MultipartFile[] grains, @RequestParam("srmVoJson") String srmVoJson, @RequestParam("srmName") String srmName){
//        return JSONUtils.toJSONString(calculateBals((SrmVO)JSONUtils.parse(srmVoJson), srmName, grains));
//    }

    private void saveResult(Map<String, List> result, String srmName){
        BalsResult balsResult = new BalsResult();
        balsResult.setResult(JSONUtils.toJSONString(result));
        balsResult.setSrmName(srmName);
        balsResultRepository.save(balsResult);
    }
}
