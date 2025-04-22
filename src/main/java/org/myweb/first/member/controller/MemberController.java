package org.myweb.first.member.controller;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

import org.myweb.first.common.Paging;
import org.myweb.first.common.Search;
import org.myweb.first.member.model.dto.Member;
import org.myweb.first.member.model.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller // sevlet-context.xml 에 Controller 로 자동 등록되는 어노테이션임
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	// 서비스 모델과 연결 처리 (의존성 주입, 자동 연결 처리)
	@Autowired
	private MemberService memberService;
	// spring framework 에서는 부모 인터페이스 타입으로 레퍼런스 선언함 (다형성 사용함)
	// 실행시 후손의 오버라이딩한 메소드가 연결 실행됨 (동적 바인딩)

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;

	// 뷰 페이지 내보내기용 메소드 작성부
	// ------------------------------------------------------------------

	// 로그인 페이지 내보내기용
	@RequestMapping("loginPage.do") // 뷰페이지에서 사용하는 요청이름 등록용 어노테이션임, Mapping Handler 가 관리함
	public String moveLoginPage() {
		return "member/loginPage"; // 뷰리졸버로 리턴됨 >> 뷰리졸버가 지정된 폴더에서 해당 jsp 파일 찾아서 클라이언트로 전송함
	}

	// 회원가입 페이지 내보내기용
	@RequestMapping("enrollPage.do")
	public String moveEnrollPage() {
		return "member/enrollPage";
	}

	// 요청 받아서 서비스 모델쪽으로 전달정보 넘기고 결과받는 메소드 ------------------------------------

	// 로그인 처리용 메소드 : Servlet 방식으로 처리
//	@RequestMapping(value = "login.do", method = RequestMethod.POST)
//	public String loginMethod(HttpServletRequest request, HttpServletResponse respone) {
//		// 1. 전송온 값 꺼내서, 변수 또는 dto 객체에 저장 처리함
//
//		// dto (vo) 객체에 저장한다면
//		Member member = new Member();
//		member.setUserId(request.getParameter("userid"));
//		member.setUserPwd(request.getParameter("userpwd"));
//
//		System.out.println("login : " + member/* .toString() */);
//
//		// 3. 모델측 서비스 클래스의 메소드를 실행시키고 결과받기
//
//		Member loginUser = memberService.selectLogin(member);
//
//		// 결과값 확인
//		System.out.println("loginUser : " + loginUser);
//
//		// 4. 받은 결과를 가지고 성공 또는 실패 페이지 내보내기 (응답하기)
//		if (loginUser != null) { // 로그인 성공시
//			// 로그인 여부 상태관리를 위해서 세션 객체를 생성
//			HttpSession session = request.getSession(); // == request.getSession(true);
//			// 기존의 세션 객체가 있으면 그 객체를 리턴하고, 세션 객체가 없으면 새 세션 객체를 생성해서 리턴함
//			// 새 세션객체가 생성되면 자동으로 세션id가 세션객체에 등록됨
//
//			System.out.println("생성된 세션 객체의 id : " + session.getId());
//			// 자동으로 request 객체에 발급된 세션id가 기록됨, 서버와 클라이언트를 왔다갔다함
//
//			// 자동 로그아웃 타임을 지정할 수도 있음
//			// session.setMaxInactiveInterval(30 * 60); //초단위로 설정함 : 30분동안 요청이 없으면 세션객체 무효화됨
//
//			// 세션 객체 안에 로그인한 회원 정보를 기록 저장시킬수도 있음 (뷰페이지에서 꺼내서 사용할 경우)
//			session.setAttribute("loginUser", loginUser);
//
//			// 내보낼 뷰페이지 지정 : 뷰페이지만 내보내는 경우
//			return "common/main"; // 뷰리졸버로 리턴
//		}
//		
//		return "common/main"; // 뷰리졸버로 리턴
//	} //login.do

	// 로그인 처리용 메소드 : command 객체 사용
	// 서버로 전송온 parameter 값들을 저장하는 객체를 command 객체라고 함 (dto 객체임)
	// input 태그의 name 속성의 이름을 dto 의 필드명(property)과 일치시키면,
	// 컨트롤러 메소드의 매개변수 위치에서 command 객체가 전송온 값들을 자동으로 받아서 저장함
//	@RequestMapping(value = "login.do", method = RequestMethod.POST)
//	public String loginMethod(Member member, HttpSession session, SessionStatus status, Model model) {
//		logger.info("login.do : " + member);
//		
//		Member loginUser = memberService.selectLogin(member);
//		
//		if(loginUser != null) {
//			session.setAttribute("loginUser", loginUser);
//			status.setComplete();  //로그인 성공 결과를 보냄 (HttpStatus 200 코드 보냄)
//			return "common/main";
//		} else { // 로그인 실패시
//			// 스프링에서는 서블릿의 RequestDispatcher 를 대신하는 Model 클래스를 제공함
//			// Model 이 제공되는 이유 : 스프링에서는 포워딩 못함 (뷰리졸버로 리턴되기 때문임)
//			// 뷰로 같이 전달할 정보를 뷰와 함께 내보내기 위해 사용함
//			// model 에 뷰로 내보낼 정보 저장 : addAttribute("이름", 객체) == request.setAttribute("이름", 객체) 
//			model.addAttribute("message", "로그인 실패! 아이디나 암호를 다시 확인하세요. 또는 로그인 제한 회원입니다. 관리자에게 문의하세요.");
//			return "common/error";
//		}		
//	}

	// 로그인 요청 처리용 메소드 : 패스워드 암호화 처리 적용
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	public String loginMethod(Member member, HttpSession session, SessionStatus status, Model model) {
		logger.info("login.do : " + member);

		// Member loginUser = memberService.selectLogin(member);

		// 암호화된 패스워드와 일치한지는 select 해 온 값으로 비교함
		// 로그인 요청은 회원의 아이디로 먼저 회원정보를 조회해 옴 >> 조회해 온 정보에서 암호를 꺼내서 비교 확인함
		Member loginUser = memberService.selectMember(member.getUserId());

		// 조회해 온 회원 정보가 있고, 암호화된 패스워드와 로그인시 전송온 패스워드가 일치한지 비교함
		// matches(전달받은 글자암호, 암호화된 패스워드) 사용함, true 이면 일치를 의미함
		if (loginUser != null && this.bcryptPasswordEncoder.matches(member.getUserPwd(), loginUser.getUserPwd())) {
			session.setAttribute("loginUser", loginUser);
			status.setComplete(); // 로그인 성공 결과를 보냄 (HttpStatus 200 코드 보냄)
			return "common/main";
		} else { // 로그인 실패시
			// 스프링에서는 서블릿의 RequestDispatcher 를 대신하는 Model 클래스를 제공함
			// Model 이 제공되는 이유 : 스프링에서는 포워딩 못함 (뷰리졸버로 리턴되기 때문임)
			// 뷰로 같이 전달할 정보를 뷰와 함께 내보내기 위해 사용함
			// model 에 뷰로 내보낼 정보 저장 : addAttribute("이름", 객체) == request.setAttribute("이름",
			// 객체)
			model.addAttribute("message", "로그인 실패! 아이디나 암호를 다시 확인하세요. 또는 로그인 제한 회원입니다. 관리자에게 문의하세요.");
			return "common/error";
		}
	}

	// 로그아웃 처리용 메소드
	// 요청에 대한 전송방식이 get 이면, @RequestMapping 에 method 속성 생략해도 됨
	// method 속성을 생략하면, value 속성도 표기를 생략해도 됨
	@RequestMapping("logout.do")
	public String logoutMethod(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
		// 해당 세션 객체가 있으면 리턴받고, 없으면 null 리턴받음
		if (session != null) {
			// 해당 세션객체가 있다면, 세션 객체를 없앰
			session.invalidate();
			return "common/main";
		} else {
			// 세션 자동 타임아웃으로 세션 객체가 없을 때
			// 에러 메세지를 error.jsp 로 내보내기함 => 서블릿이라면 RequestDispatcher 의 forward() 사용함
			// Spring 에서는 Model 클래스를 제공함 => request.setAttribute("에러메세지");
			// Model 의 addAttribute() 메소드 사용함
			// Model 객체 생성은 메소드 안에서 직접하지 않고 메소드 매개변수로 선언함
			model.addAttribute("message", "로그인 세션이 존재하지 않습니다.");
			return "common/error";
		}
	}

	// ajax 통신으로 가입할 회원의 아이디 중복 검사 요청 처리용 메소드
	// 클라이언트쪽에서 입력한 userId 를 전송보내면, request 객체에 저장되어서 전송옴
	// 전송온 값 꺼내기 : String 변수 = request.getParameter("전송용이름");
	// 위의 코드를 처리해 주는 어노테이션을 메소드 매개변수 위치에 사용할 수도 있음
	// @RequestParam("전송용이름") 자료형 변수명

//	@RequestMapping(value="idchk.do", method=RequestMethod.POST)
//	public void dupCheckUserIdMethod(HttpServletRequest request) {
//		String userId = request.getParameter("userId");
	/*
	 * public void dupCheckUserIdMethod(@RequestParam("userId") String userId,
	 * HttpServletResponse response) { //ajax 요청시 결과 응답하는 방식 : @ResponseBody 에 담아서
	 * 보내는 방식, 별도의 출력스트림을 만들어서 내보내기 //방식2, 별도의 출력스트림 연결해서 문자열 내보내기 방식 사용함 (반환자료형이
	 * void 임)
	 * 
	 * int result = memberService.selectCheckId(userId); String resultStr = null;
	 * if(result == 0) { resultStr = "ok"; }else { resultStr = "dup"; }
	 * 
	 * //response 를 이용해서 클라이언트와 출력스트림을 열어서 문자열값 내보내기할 것임
	 * response.setContentType("text/html; charset=UTF-8"); try { PrintWriter out =
	 * response.getWriter(); out.append(resultStr); out.flush(); out.close(); }
	 * catch (IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 */

	@RequestMapping(value = "idchk.do", method = RequestMethod.POST)
	@ResponseBody
	public String dupCheckUserIdMethod(@RequestParam("userId") String userId) {
		// 방식1, ResponseBody 에 담아서 문자열 내보내기 방식 사용함 (반환자료형이 String 임)
		int result = memberService.selectCheckId(userId);
		return (result == 0) ? "ok" : "dup";
	}

	// 회원 가입 요청 처리용 메소드 (파일 첨부 기능이 없는 form 전송일 때 처리 방식)
//	@RequestMapping(value="enroll.do", method=RequestMethod.POST)
//	public String memberInsertMethod(Member member, Model model) {
//		logger.info("enroll.do : " + member);
//		
//		//서비스 모델의 메소드 실행 요청하고 결과받기
//		int result = memberService.insertMember(member);
//		
//		if(result > 0) {  // 회원 가입 성공시
//			return "member/loginPage";
//		}else {  // 회원 가입 실패시
//			model.addAttribute("message", "회원 가입 실패! 확인하고 다시 가입해 주세요.");
//			return "common/error";
//		}		
//	}

//	// 회원 가입 요청 처리용 메소드 (파일 첨부 기능이 없는 form 전송일 때 처리 방식)
//	// 비밀번호(패스워드) 암호화 처리 기능 추가
//	@RequestMapping(value="enroll.do", method=RequestMethod.POST)
//	public String memberInsertMethod(Member member, Model model) {
//		logger.info("enroll.do : " + member);
//		
//		//패스워드 암호화 처리
//		String encodePwd = bcryptPasswordEncoder.encode(member.getUserPwd());
//		logger.info("암호화된 패스워드 : " + encodePwd);
//		member.setUserPwd(encodePwd);
//		
//		//서비스 모델의 메소드 실행 요청하고 결과받기
//		int result = memberService.insertMember(member);
//		
//		if(result > 0) {  // 회원 가입 성공시
//			return "member/loginPage";
//		}else {  // 회원 가입 실패시
//			model.addAttribute("message", "회원 가입 실패! 확인하고 다시 가입해 주세요.");
//			return "common/error";
//		}		
//	}

	// 회원 가입 요청 처리용 메소드 (파일 첨부 기능이 있는 form 전송일 때 처리 방식) => 첨부파일은 별도로 전송받도록 처리함
	// 서버상의 파일 저장 폴더 지정을 위해서 request 객체가 필요함
	// 업로드되는 파일은 따로 전송받음 => multipart 방식으로 전송옴 => 스프링이 제공하는 MutipartFile 클래스 사용해서 받음
	// 비밀번호(패스워드) 암호화 처리 기능 추가
	@RequestMapping(value = "enroll.do", method = RequestMethod.POST)
	public String memberInsertMethod(Member member, Model model, HttpServletRequest request,
			@RequestParam("photofile") MultipartFile mfile) {
		logger.info("enroll.do : " + member);

		// 패스워드 암호화 처리
		String encodePwd = bcryptPasswordEncoder.encode(member.getUserPwd());
		logger.info("암호화된 패스워드 : " + encodePwd);
		member.setUserPwd(encodePwd);

		// 사진 파일 첨부가 있을 경우, 저장 폴더 지정 ---------------------------------------------
		String savePath = request.getSession().getServletContext().getRealPath("resources/photoFiles");
		// 서버 엔진이 구동하는 웹애플리케이션(Context)의 루트(webapp) 아래의 "resources/photoFiles" 까지의 경로
		// 정보를 저장함
		logger.info("savePath : " + savePath);

		// 첨부파일이 있다면 지정 폴더에 저장 처리 ----------------------------
		if (!mfile.isEmpty()) {
			// 전송온 파일 이름 추출함
			String fileName = mfile.getOriginalFilename();
			// 여러 회원이 업로드한 사진파일명이 중복될 경우를 대비해서 저장파일명 이름 바꾸기함
			// 바꿀 파일이름은 개발자가 정함
			// userId 가 기본키(primary key)이므로 중복이 안됨 => userId_filename.확장자 형태로 정해봄
			String renameFileName = member.getUserId() + "_" + fileName;

			// 저장 폴더에 저장 처리
			if (fileName != null && fileName.length() > 0) {
				try {
					mfile.transferTo(new File(savePath + "\\" + renameFileName));
				} catch (Exception e) {
					// 첨부파일 저장시 에러 발생
					e.printStackTrace(); // 개발자가 볼 정보
					model.addAttribute("message", "첨부파일 업로드 실패!");
					return "common/error";
				}
			} // if

			// 파일 업로드 정상 처리되었다면
			member.setPhotoFileName(renameFileName); // db 테이블에는 변경된 파일명이 기록 저장됨
		} // 첨부파일이 있다면 --------------------------------------------------------

		// 서비스 모델의 메소드 실행 요청하고 결과받기
		int result = memberService.insertMember(member);

		if (result > 0) { // 회원 가입 성공시
			return "member/loginPage";
		} else { // 회원 가입 실패시
			model.addAttribute("message", "회원 가입 실패! 확인하고 다시 가입해 주세요.");
			return "common/error";
		}
	}

	// '내 정보 보기' 요청 처리용 메소드
	/*
	 * 컨트롤러 메소드에서 뷰리졸버로 리턴하는 타입은 String (뷰파일명 리턴시), ModelAndView 를 사용할 수 있음 클라이언트가
	 * 보낸 데이터 추출은 String 변수 = request.getParameter("전송이름"); 스프링에서는 전송값 추출을 위한 위의 구문과
	 * 동일한 동작을 수행하는 어노테이션 제공하고 있음
	 * 
	 * @RequestParam("전송이름) 자료형 변수명 == request.getParameter("전송이름") 과 같음 이 어노테이션은
	 * 메소드 () 안에 사용함
	 */
	@RequestMapping("myinfo.do") // 전송방식 get 임
	public String memberDetailMethod(@RequestParam("userId") String userId, Model model) {
		logger.info("myinfo.do : " + userId);

		// 서비스 모델로 아이디 전달해서, 회원 정보 조회한 결과 리턴받기
		Member member = memberService.selectMember(userId);

		// 리턴받은 결과를 가지고 성공 또는 실패 페이지 내보내기
		if (member != null) { // 조회 성공시
			// 첨부된 사진파일이 있다면, 원래 파일명으로 변경해서 전달함
			String originalFileName = null;
			if (member.getPhotoFileName() != null) {
				// 아이디_파일명.확장자 => 파일명.확장자 로 바꿈
				originalFileName = member.getPhotoFileName().substring(member.getPhotoFileName().indexOf('_') + 1);
				logger.info("사진파일명 확인 : " + member.getPhotoFileName() + ", " + originalFileName);
			}

			model.addAttribute("member", member);
			model.addAttribute("ofile", originalFileName);

			return "member/infoPage";
		} else { // 조회 실패시
			model.addAttribute("message", userId + " 에 대한 회원 정보 조회 실패! 아이디를 다시 확인하세요.");
			return "common/error";
		}
	}

	// 회원 정보 수정 처리용 메소드
	@RequestMapping(value="mupdate.do", method=RequestMethod.POST)
	public String memberUpdateMethod(Member member, Model model, HttpServletRequest request,
			@RequestParam("photofile") MultipartFile mfile, @RequestParam("originalPwd") String originalUserPwd,
			@RequestParam("ofile") String originalFileName) {
		logger.info("mupdate.do : " + member);

		// 암호가 전송이 왔다면 (새로운 암호가 전송온 경우임)
		if (member.getUserPwd() != null && member.getUserPwd().length() > 0) {
			// 패스워드 암호화 처리함
			member.setUserPwd(this.bcryptPasswordEncoder.encode(member.getUserPwd()));
			logger.info("변경된 암호 확인 : " + member.getUserPwd() + ", length : " + member.getUserPwd().length());
		} else { // 새로운 암호가 전송오지 않았다면, 현재 member.userPwd = null 임 => 쿼리문에 적용되면 기존 암호 지워짐
			member.setUserPwd(originalUserPwd); // 원래 패스워드 기록함
		}

		// 사진 파일 첨부가 있을 경우, 저장 폴더 지정 ---------------------------------------------
		String savePath = request.getSession().getServletContext().getRealPath("resources/photoFiles");
		// 서버 엔진이 구동하는 웹애플리케이션(Context)의 루트(webapp) 아래의 "resources/photoFiles" 까지의 경로
		// 정보를 저장함
		logger.info("savePath : " + savePath);

		// 수정된 첨부파일이 있다면 지정 폴더에 저장 처리 ----------------------------
		if (!mfile.isEmpty()) {
			// 전송온 파일 이름 추출함
			String fileName = mfile.getOriginalFilename();
			
			// 이전 첨부파일명과 새로 첨부된 파일명이 다른지 확인
			if(!fileName.equals(originalFileName)) {		
			
				// 여러 회원이 업로드한 사진파일명이 중복될 경우를 대비해서 저장파일명 이름 바꾸기함
				// 바꿀 파일이름은 개발자가 정함
				// userId 가 기본키(primary key)이므로 중복이 안됨 => userId_filename.확장자 형태로 정해봄
				String renameFileName = member.getUserId() + "_" + fileName;
	
				// 저장 폴더에 저장 처리
				if (fileName != null && fileName.length() > 0) {
					try {
						mfile.transferTo(new File(savePath + "\\" + renameFileName));
					} catch (Exception e) {
						// 첨부파일 저장시 에러 발생
						e.printStackTrace(); // 개발자가 볼 정보
						model.addAttribute("message", "첨부파일 업로드 실패!");
						return "common/error";
					}
				} // if

				// 파일 업로드 정상 처리되었다면
				member.setPhotoFileName(renameFileName); // db 테이블에는 변경된 파일명이 기록 저장됨
			} // 첨부파일이 있고, 파일명이 다르다면 --------------------------------------------------------
		}else { // 새로운 첨부파일이 없다면
			// 기존 파일명을 member 에 다시 저장함
			member.setPhotoFileName(member.getUserId() + "_" + originalFileName);
		}

		// 서비스 모델의 메소드 실행 요청하고 결과받기
		if (memberService.updateMember(member) > 0) { // 회원 정보 수정 성공시
			// 컨트롤러 메소드에서 다른 컨트롤러 메소드를 실행시킬 경우
			return "redirect:main.do";
		} else { // 회원 정보 수정 실패시
			model.addAttribute("message", "회원 정보 수정 실패! 확인하고 다시 가입해 주세요.");
			return "common/error";
		}
	}
	
	//회원 탈퇴 (삭제) 처리용 메소드
	@RequestMapping("mdelete.do")
	public String memberDeleteMethod(@RequestParam("userId") String userId, Model model) {
		if(memberService.deleteMember(userId) > 0) {  
			// 회원 탈퇴 성공시 자동 로그아웃 처리해야 함
			return "redirect:logout.do";			
		}else {
			model.addAttribute("message", userId + "님의 회원 탈퇴 실패! 관리자에게 문의하세요");
			return "common/error";
		}
	}
	
	// 관리자용 기능 *********************************************************
	
	// 회원 목록 보기 요청 처리용 (페이징 처리 포함)
	@RequestMapping("mlist.do")
	public ModelAndView memberListMethod(ModelAndView mv, 
			@RequestParam(name="page", required=false) String page,
			@RequestParam(name="limit", required=false) String slimit) {
		// page : 목록 출력 페이지, limit : 한 페이지에 출력할 목록 갯수
		
		// 페이징 처리
		int currentPage = 1;
		if (page != null) {
			currentPage = Integer.parseInt(page);
		}
		
		// 한 페이지에 출력할 목록 갯수 기본 10개로 지정함
		int limit = 10;
		if (slimit != null) {
			limit = Integer.parseInt(slimit);
		}
		
		// 총 목록 갯수 조회해서, 총 페이지 수 계산함
		int listCount = memberService.selectListCount();
		// 페이지 관련 항목들 계산 처리
		Paging paging = new Paging(listCount, limit, currentPage, "mlist.do");
		paging.calculate();
		
		//서비스 모델로 페이징 적용된 목록 조회 요청하고 결과받기
		ArrayList<Member> list = memberService.selectList(paging);
		
		if(list != null && list.size() > 0) {  //조회 성공시
			//ModelAndView : Model + View 
			mv.addObject("list", list);  //request.setAttribute("list", list) 와 같음
			mv.addObject("paging", paging);
			
			mv.setViewName("member/memberListView");
		} else {  //조회 실패시
			mv.addObject("message", currentPage + "페이지에 출력할 회원 조회 실패!");
			mv.setViewName("common/error");
		}
		
		return mv;
	}
	
	//회원 로그인 제한/허용 처리용 메소드
	@RequestMapping("loginok.do")
	public String changeLoginOKMethod(Member member, Model model) {
		if(memberService.updateLoginOk(member) > 0) {
			return "redirect:mlist.do";
		}else {
			model.addAttribute("message", "로그인 제한/허용 처리 오류 발생");
			return "common/error";
		}
	}
	
	//관리자용 검색 기능 요청 처리용 메소드 (만약, 전송방식을 GET, POST 둘 다 사용할 수 있게 한다면)	
	@RequestMapping(value="msearch.do", method= {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView memberSearchMethod(HttpServletRequest request, ModelAndView mv) {
		//전송 온 값 꺼내기
		String action = request.getParameter("action");
		String keyword = request.getParameter("keyword");
		String begin = request.getParameter("begin");
		String end = request.getParameter("end");
		
		Search search = new Search();
		
		if(action.equals("uenroll")) {
			if(begin != null && end != null) {
				search.setBegin(Date.valueOf(begin));
				search.setEnd(Date.valueOf(end));
			}
		} else if(keyword != null) {
			if(action.equals("uage")) {
				search.setAge(Integer.parseInt(keyword));
			}else {
				search.setKeyword(keyword);
			}
		}
		
		//검색 결과에 대한 페이징 처리
		int currentPage = 1;
		if(request.getParameter("page") != null) {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
		
		//한 페이지에 출력할 목록 갯수 지정
		int limit = 10;
		if(request.getParameter("limit") != null) {
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		
		//총 페이지수 계산을 위해 검색 결과가 적용된 총 목록 갯수 조회
		int listCount = 0;
		switch(action) {
		case "uid":	listCount = memberService.selectSearchUserIdCount(keyword);		break;
		case "ugen":	listCount = memberService.selectSearchGenderCount(keyword);		break;
		case "uage":	listCount = memberService.selectSearchAgeCount(Integer.parseInt(keyword));	break;
		case "uenroll":	listCount = memberService.selectSearchEnrollDateCount(search);	break;
		case "ulogok":	listCount = memberService.selectSearchLoginOKCount(keyword);	break;
		}
		
		//페이징 계산 처리
		Paging paging = new Paging(listCount, limit, currentPage, "msearch.do");
		paging.calculate();
		
		//원하는 페이지의 출력할 검색 결과 목록 조회
		ArrayList<Member> list = null;
		search.setStartRow(paging.getStartRow());
		search.setEndRow(paging.getEndRow());
		
		switch(action) {
		case "uid":	list = memberService.selectSearchUserId(search);		break;
		case "ugen":	list = memberService.selectSearchGender(search);		break;
		case "uage":	list = memberService.selectSearchAge(search);	break;
		case "uenroll":	list = memberService.selectSearchEnrollDate(search);	break;
		case "ulogok":	list = memberService.selectSearchLoginOK(search);	break;
		}
		
		//조회 결과 성공 또는 실패에 따라 뷰페이지 내보내기
		if(list != null && list.size() > 0) {
			mv.addObject("list", list);
			mv.addObject("paging", paging);
			mv.addObject("action", action);
			
			if(keyword != null) {
				mv.addObject("keyword", keyword);
			}else {
				mv.addObject("begin", begin);
				mv.addObject("end", end);
			}
			
			mv.setViewName("member/memberListView");
		}else {
			mv.addObject("message", "회원 관리 검색 결과가 존재하지 않습니다.");
			mv.setViewName("common/error");
		}
		
		return mv;
	}

} // class









