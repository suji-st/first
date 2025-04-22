package org.myweb.first.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	//뷰 페이지 내보내기용 메소드 ----------------------------------
	@RequestMapping("moveJSTL.do")
	public String moveJSTLPage() {
		return "test/testJSTL";  //sevlet-context.xml 에 등록된 뷰리졸버로 리턴됨
	}
	
	@RequestMapping("moveCore.do")
	public String moveCorePage() {
		return "test/testCore";
	}
	
	@RequestMapping("moveFmt.do")
	public String moveFmtPage() {
		return "test/testFmt";
	}
	
	@RequestMapping("moveFn.do")
	public String moveFunctionsPage() {
		return "test/testFunctions";
	}
	
	
	
}





