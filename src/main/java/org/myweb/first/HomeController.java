package org.myweb.first;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 * 스프링 레거시 프로젝트에서는 컨트롤러는 클래스로 작성함
 * 클래스명 위에 @Controller 어노테이션을 표시함
 * 요청이름.do 로 오는 요청은 각 컨트롤러 클래스 안의 메소드로 작성함
 * 메소드 이름 위에 @RequestMapping("요청이름.do") 등록해 놓음
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
		
	//index.jsp 가 웰컴 요청될 때 포워딩된 요청을 받아서, common/main.jsp 를 내보내기 위한 메소드
	@RequestMapping("main.do")
	public String forwardMainView() {
		//return "하위폴더명/내보낼 뷰파일명";
		return "common/main";
		//설정된 뷰리졸버에게로 리턴됨
		//InternalResourceViewResolver 가 받아서, 앞에 "/WEB-INF/views/" 붙이고, 뒤에 ".jsp" 붙여서
		// "/WEB-INF/views/common/main.jsp" 를 찾아서 클라이언트로 내보냄
	}
	
}










