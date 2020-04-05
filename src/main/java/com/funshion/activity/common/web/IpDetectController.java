package com.funshion.activity.common.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpDetectController {
	
	@RequestMapping("/detect/netdetection/*")
	public String detect(){
		return "My baby,your url is inavailable $_$ $_$ !!!";
	}
}
