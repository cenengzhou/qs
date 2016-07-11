package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.quartz.QrtzTriggers;
import com.gammon.qs.service.admin.QrtzTriggerService;

@RestController
@RequestMapping(value = "service/quartz", method = RequestMethod.POST)
public class QuartzController {

	@Autowired
	private QrtzTriggerService qrtzTriggerService;
	
	@RequestMapping("getAllTriggers")
	public List<QrtzTriggers> getAllTriggers(){
		return qrtzTriggerService.getAllTriggers();
	}
	
	@RequestMapping(value = "updateQrtzTriggerList", method = RequestMethod.POST)
	public void updateQrtzTriggerList(@RequestBody List<QrtzTriggers> updatedQrtzList){
		qrtzTriggerService.updateQrtzTriggerList(updatedQrtzList);
	}
	
}
