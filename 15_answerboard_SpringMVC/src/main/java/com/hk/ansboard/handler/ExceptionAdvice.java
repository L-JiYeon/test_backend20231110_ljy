package com.hk.ansboard.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

	private Logger logger=LoggerFactory.getLogger(ExceptionAdvice.class);
	
	//실행 중에 오류가 발생하면 실행되는 메서드
	@ExceptionHandler(Exception.class)
	public String handleException(Exception e, Model model) {
		logger.error("Exception 발생:{}", e.getMessage());
		model.addAttribute("msg","오류가 발생하여 확인중");
		return "error";
	}
}
