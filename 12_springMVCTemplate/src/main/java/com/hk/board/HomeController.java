package com.hk.board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//DispatcherServlet 객체가 @Controller로 선언된 클래스를 찾는다.
//"home.do"라고 요청했으면, 매핑되어 있는 메서드를 실행한다.
@Controller
public class HomeController {

//	@GetMapping
//	@PostMapping
//	@PutMapping
	
	@RequestMapping(value = "/home.do", method = RequestMethod.GET)
	public String home(Model model, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("home 요청"); 
		String str="Hello Spring"; 
		
//		request.setAttribute("str",str);
		model.addAttribute("str",str);
		
		//우리가 사용하면 sendRedirect() 기능임
//		return "redirect:home.jsp";
		
		// 우리가 사용하면 forward() 기능임
		return "home";	//페이지 이름 쓰기 //ViewResolver가 만들어줌: "WEB-INF/views/"+"home"+".jsp"
	}
	
	@RequestMapping(value = "/boardList.do", method = RequestMethod.GET)
	public String boardList() {
		return "";
	}
}
