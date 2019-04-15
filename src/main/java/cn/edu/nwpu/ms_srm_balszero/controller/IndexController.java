package cn.edu.nwpu.ms_srm_balszero.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * 显示网页的控制器
 */
@Controller
public class IndexController {

	@GetMapping("/balsindex")
	public String showIndex() {
		return "index";
	}
}
