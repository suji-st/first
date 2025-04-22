package org.myweb.first.notice.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.myweb.first.common.FileNameChange;
import org.myweb.first.common.Paging;
import org.myweb.first.common.Search;
import org.myweb.first.member.model.dto.Member;
import org.myweb.first.notice.model.dto.Notice;
import org.myweb.first.notice.model.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class NoticeController {
	// 이 클래스 안의 메소드들이 잘 동작하는지, 매개변수 또는 리턴값을 확인하는 용도로 로그 객체를 생성함
	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

	@Autowired
	private NoticeService noticeService;

	/*
	 * @Autowired 가 내부에서 자동 의존성 주입하고 서비스와 연결해 줌, 생성 코드 필요없음 public
	 * NoticeController() { noticeService = new NoticeService(); }
	 */

	// 뷰 페이지 내보내기용 메소드 -----------------------------------------------------

	// 새 공지글 등록 페이지로 이동 처리용
	@RequestMapping("moveWrite.do")
	public String moveWritePage() {
		return "notice/noticeWriteForm";
	}
	
	// 공지글 수정페이지로 이동 처리용
	@RequestMapping("nmoveup.do")
	public ModelAndView moveUpdatePage(@RequestParam("no") int noticeNo, ModelAndView mv) {
		//수정페이지로 수정할 공지글도 함께 내보내기 위해, 전송받은 공지번호에 대한 공지글 조회해 옴
		Notice notice = noticeService.selectNotice(noticeNo);
		
		if(notice != null) {
			mv.addObject("notice", notice);
			mv.setViewName("notice/noticeUpdateView");
		} else {
			mv.addObject("message", noticeNo + "번 공지글 수정페이지로 이동 실패!");
			mv.setViewName("common/error");
		}
		
		return mv;
	}
	
	
	// 요청 처리하는 메소드 (db 까지 연결되는 요청) -----------------------------------

	/*
	 * produces 속성 : 클라이언트에게 어떤 타입의 데이터를 응답할 것인지를 지정함 HTTP 응답(Response 객체)의
	 * Content-Type 해더를 설정한 것임 브라우저가 이 설정된 정보를 보고 응답 데이터를 올바르게 해석하게 됨
	 * application/json; 응답 데이터가 JSON 형식이라는 의미임 charset=UTF-8 응답 데이터가 UTF-8 문자 인코딩으로
	 * 인코딩되어 있다는 의미임, 한글 깨짐 방지에 아주 중요함
	 */
	@RequestMapping(value = "ntop3.do", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody // ajax 통신에서 사용함, 뷰리졸버로 리턴하지 않고 별도의 출력스트림을 만들어서 클라이언트로 바로 보냄
	public Map<String, Object> noticeNewTop3Method() { // Jackson 라이브러리 사용시 리턴방식
		// public String noticeNewTop3Method() { //JSONObject 리턴시 사용하는 리턴방식
		logger.info("ntop3.do run...");

		// 서비스 모델로 top3 결과 요청
		ArrayList<Notice> list = noticeService.selectTop3();
		logger.info("ntop3 list : " + list);

		// ajax 통신(별도의 출력스트림 사용)으로 json 객체 내보내는 방식 첫번째 :
		// JSONObject 클래스 사용

//		JSONArray jarr = new JSONArray();
//		
//		for(Notice notice : list) {
//			JSONObject job = new JSONObject();
//			
//			//저장 : put("key", value)
//			job.put("no", notice.getNoticeNo());
//			job.put("title", notice.getNoticeTitle());
//			job.put("date", notice.getNoticeDate().toString());  //주의 : 날짜는 반드시 문자열로 바꿈
//			//json 객체에 날짜데이터 그대로 저장시에는 뷰페이지에서 객체정보 출력 안됨
//			
//			jarr.add(job);  //배열에 json객체 저장
//		}
//		
//		JSONObject sendJson = new JSONObject();  //전송용 객체
//		sendJson.put("list", jarr);
//		
//		return sendJson.toJSONString();

		// ajax 통신(별도의 출력스트림 사용)으로 json 객체 내보내는 방식 두번째 :
		// Jackson 라이브러리 사용

		// json 으로 보낼 객체 구조를 Map 으로 생성
		Map<String, Object> resultTop3 = new HashMap<>();
		resultTop3.put("list", list);

		return resultTop3; // Jackson 이 자동으로 JSON 으로 변환
	}

	// 공지사항 전체 목록보기 요청 처리용 (페이징 처리 : 한 페이지에 10개씩 출력 처리)
	@RequestMapping("nlist.do")
	public ModelAndView noticeListMethod(ModelAndView mv, @RequestParam(name = "page", required = false) String page,
			@RequestParam(name = "limit", required = false) String slimit) {
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
		int listCount = noticeService.selectListCount();
		// 페이지 관련 항목들 계산 처리
		Paging paging = new Paging(listCount, limit, currentPage, "nlist.do");
		paging.calculate();

		// 서비스 모델로 페이징 적용된 목록 조회 요청하고 결과받기
		ArrayList<Notice> list = noticeService.selectList(paging);

		if (list != null && list.size() > 0) { // 조회 성공시
			// ModelAndView : Model + View
			mv.addObject("list", list); // request.setAttribute("list", list) 와 같음
			mv.addObject("paging", paging);

			mv.setViewName("notice/noticeListView");
		} else { // 조회 실패시
			mv.addObject("message", currentPage + "페이지에 출력할 공지글 목록 조회 실패!");
			mv.setViewName("common/error");
		}

		return mv;
	}
	
	//공지글 상세보기 요청 처리용
	@RequestMapping("ndetail.do")
	public ModelAndView noticeDetailMethod(
			@RequestParam("no") int noticeNo, 
			ModelAndView mv,
			HttpSession session) {
		//관리자 상세보기 페이지와 일반회원 상세보기 페이지를 구분해서 응답 처리함
		//관리자인지 확인하기 위해 session 매개변수가 추가됨
		
		logger.info("ndetail.do : " + noticeNo); //전송받은 값 확인
		
		Notice notice = noticeService.selectNotice(noticeNo);
		
		//조회수 1증가 처리
		noticeService.updateAddReadCount(noticeNo);
		
		if(notice != null) {
			mv.addObject("notice", notice);
			
			Member loginUser = (Member)session.getAttribute("loginUser");
			if(loginUser != null && loginUser.getAdminYN().equals("Y")) {
				mv.setViewName("notice/noticeAdminDetailView");
			} else {
				mv.setViewName("notice/noticeDetailView");
			}
		} else {
			mv.addObject("message", noticeNo + "번 공지글 상세보기 요청 실패! 관리자에게 문의하세요.");
			mv.setViewName("common/error");
		}
		
		return mv;
	}
	
	// 첨부파일 다운로드 요청 처리용 메소드
	// 스프링에서는 파일 다운로드는 스프링이 제공하는 View 클래스를 상속받은 클래스를 사용하도록 정해 놓았음
	// => 공통모듈로 파일다운로드용 뷰 클래스를 따로 만듦 => 뷰리졸버에서 연결 처리함
	// => 리턴타입은 반드시 ModelAndView 여야 함
	@RequestMapping("nfdown.do")
	public ModelAndView fileDownMethod(
			ModelAndView mv,
			HttpServletRequest request,
			@RequestParam("ofile") String originalFileName,
			@RequestParam("rfile") String renameFileName) {
		
		// 공지사항 첨부파일 저장 폴더 경로 지정
		String savePath = request.getSession().getServletContext().getRealPath("resources/notice_upfiles");
		// 저장 폴더에서 읽을 파일에 대한 File 객체 생성
		File downFile = new File(savePath + "\\" + renameFileName);
		// 파일 다운시 브라우저로 내보낼 원래 파일에 대한 File 객체 생성
		File originFile = new File(originalFileName);
		
		// 파일 다운 처리용 뷰클래스 id명과 다운로드할 File 객체를 ModelAndView 에 담아서 리턴함
		mv.setViewName("filedown");  //뷰클래스의 id명 기입
		mv.addObject("originFile", originFile);
		mv.addObject("renameFile", downFile);
		
		return mv;
	}
	
	// dml ****************************************************
	
	// 새 공지글 등록 요청 처리용 (파일 업로드 기능 포함)
	@RequestMapping(value="ninsert.do", method=RequestMethod.POST)
	public String noticeInsertMethod(
			Notice notice, 
			@RequestParam(name="ofile", required=false) MultipartFile mfile,
			HttpServletRequest request,
			Model model) {
		logger.info("ninsert.do : " + notice);
		
		//공지사항 첨부파일 저장 폴더를 경로 저장
		String savePath = request.getSession().getServletContext().getRealPath("resources/notice_upfiles");
		
		//첨부파일이 있을 때
		if(!mfile.isEmpty()) {
			// 전송온 파일이름 추출함
			String fileName = mfile.getOriginalFilename();
			String renameFileName = null;
			
			//저장 폴더에는 변경된 파일이름을 파일을 저장 처리함
			//바꿀 파일명 : 년월일시분초.확장자
			if(fileName != null && fileName.length() > 0) {
				renameFileName = FileNameChange.change(fileName, "yyyyMMddHHmmss");
				logger.info("변경된 첨부 파일명 확인 : " + renameFileName);
				
				try {
					//저장 폴더에 바뀐 파일명으로 파일 저장하기
					mfile.transferTo(new File(savePath + "\\" + renameFileName));
				} catch (Exception e) {					
					e.printStackTrace();
					model.addAttribute("message", "첨부파일 저장 실패!");
					return "common/error";
				} 
			} //파일명 바꾸어 저장하기
			
			//notice 객체에 첨부파일 정보 저장하기
			notice.setOriginalFilePath(fileName);
			notice.setRenameFilePath(renameFileName);
		} //첨부파일 있을 때
		
		if(noticeService.insertNotice(notice) > 0) {
			//새 공지글 등록 성공시, 공지 목록 페이지로 이동 처리
			return "redirect:nlist.do";
		} else {
			model.addAttribute("message", "새 공지글 등록 실패!");
			return "common/error";
		}
		
	}  // ninsert.do closed
	
	// 공지글 삭제 요청 처리용
	@RequestMapping("ndelete.do")
	public String noticeDeleteMethod(
			Model model,
			@RequestParam("no") int noticeNo,
			@RequestParam(name="rfile", required=false) String renameFileName,
			HttpServletRequest request) {
		if(noticeService.deleteNotice(noticeNo) > 0) {  // 공지글 삭제 성공시
			//공지글 삭제 성공시 저장 폴더에 있는 첨부파일도 삭제 처리함
			if(renameFileName != null && renameFileName.length() > 0) {
				// 공지사항 첨부파일 저장 폴더 경로 지정
				String savePath = request.getSession().getServletContext().getRealPath("resources/notice_upfiles");
				// 저장 폴더에서 파일 삭제함
				new File(savePath + "\\" + renameFileName).delete();
			}
			
			return "redirect:nlist.do";
		} else {  // 공지글 삭제 실패시
			model.addAttribute("message", noticeNo + "번 공지글 삭제 실패!");
			return "common/error";
		}
	}
	
	// 공지글 수정 요청 처리용 (파일 업로드 기능 포함)
	@RequestMapping(value="nupdate.do", method=RequestMethod.POST)
	public String noticeUpdateMethod(
			Notice notice, Model model, HttpServletRequest request,
			@RequestParam(name="deleteFlag", required=false) String delFlag,
			@RequestParam(name="ofile", required=false) MultipartFile mfile) {
		logger.info("nupdate.do : " + notice);
		
		//중요도 체크 안 한 경우 처리
		if(notice.getImportance() == null) {
			notice.setImportance("N");
			notice.setImpEndDate(new java.sql.Date(System.currentTimeMillis()));  //오늘 날짜를 기본 날짜로 지정함
		}
		
		// 첨부파일 관련 변경 사항 처리
		// 공지사항 첨부파일 저장 폴더 경로 지정
		String savePath = request.getSession().getServletContext().getRealPath("resources/notice_upfiles");
		
		// 1. 원래 첨부파일이 있는데, '파일삭제'를 체크한 경우
		//    또는 원래 첨부파일이 있는데 새로운 첨부파일로 변경 업로드된 경우
		//	=> 이전 파일 정보 삭제함
		if(notice.getOriginalFilePath() != null 
				&& ((delFlag != null && delFlag.equals("yes")) || (!mfile.isEmpty()))) {
			//저장 폴더에서 이전 파일은 삭제함
			new File(savePath + "\\" + notice.getRenameFilePath()).delete();
			// notice 안의 파일 정보도 삭제함
			notice.setOriginalFilePath(null);
			notice.setRenameFilePath(null);
		}
		
		//2. 첨부파일이 있을 때 (변경 또는 추가)
		if(!mfile.isEmpty()) {
			// 전송온 파일이름 추출함
			String fileName = mfile.getOriginalFilename();
			String renameFileName = null;
			
			//저장 폴더에는 변경된 파일이름을 파일을 저장 처리함
			//바꿀 파일명 : 년월일시분초.확장자
			if(fileName != null && fileName.length() > 0) {
				renameFileName = FileNameChange.change(fileName, "yyyyMMddHHmmss");
				logger.info("변경된 첨부 파일명 확인 : " + renameFileName);
				
				try {
					//저장 폴더에 바뀐 파일명으로 파일 저장하기
					mfile.transferTo(new File(savePath + "\\" + renameFileName));
				} catch (Exception e) {					
					e.printStackTrace();
					model.addAttribute("message", "첨부파일 저장 실패!");
					return "common/error";
				} 
			} //파일명 바꾸어 저장하기
			
			//notice 객체에 첨부파일 정보 저장하기
			notice.setOriginalFilePath(fileName);
			notice.setRenameFilePath(renameFileName);
		} //첨부파일 있을 때
		
		if(noticeService.updateNotice(notice) > 0) {
			// 공지글 수정 성공시, 관리자 상세보기 페이지로 이동 처리
			return "redirect:ndetail.do?no=" + notice.getNoticeNo();
		} else {
			model.addAttribute("message", notice.getNoticeNo() + "번 공지글 수정 실패!");
			return "common/error";
		}
	}

	// 공지글 검색 관련 **********************************************

	// 공지사항 제목 검색 목록보기 요청 처리용 (페이징 처리 : 한 페이지에 10개씩 출력 처리)
	@RequestMapping("nsearchTitle.do")
	public ModelAndView noticeSearchTitleMethod(ModelAndView mv, @RequestParam("action") String action,
			@RequestParam("keyword") String keyword, @RequestParam(name = "page", required = false) String page,
			@RequestParam(name = "limit", required = false) String slimit) {
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

		// 검색결과가 적용된 총 목록 갯수 조회해서, 총 페이지 수 계산함
		int listCount = noticeService.selectSearchTitleCount(keyword);
		// 페이지 관련 항목들 계산 처리
		Paging paging = new Paging(listCount, limit, currentPage, "nsearchTitle.do");
		paging.calculate();

		// 마이바티스 매퍼에서 사용되는 메소드는 Object 1개만 전달할 수 있음
		// paging.startRow, paging.endRow, keyword 같이 전달해야 하므로 => 객체 하나를 만들어서 저장해서 보냄
		Search search = new Search();
		search.setKeyword(keyword);
		search.setStartRow(paging.getStartRow());
		search.setEndRow(paging.getEndRow());

		// 서비스 모델로 페이징 적용된 목록 조회 요청하고 결과받기
		ArrayList<Notice> list = noticeService.selectSearchTitle(search);

		if (list != null && list.size() > 0) { // 조회 성공시
			// ModelAndView : Model + View
			mv.addObject("list", list); // request.setAttribute("list", list) 와 같음
			mv.addObject("paging", paging);
			mv.addObject("action", action);
			mv.addObject("keyword", keyword);

			mv.setViewName("notice/noticeListView");
		} else { // 조회 실패시
			mv.addObject("message", action + "에 대한 " + keyword + " 검색 결과가 존재하지 않습니다.");
			mv.setViewName("common/error");
		}

		return mv;
	}

	// 공지사항 내용 검색 목록보기 요청 처리용 (페이징 처리 : 한 페이지에 10개씩 출력 처리)
	@RequestMapping("nsearchContent.do")
	public ModelAndView noticeSearchContentMethod(ModelAndView mv, @RequestParam("action") String action,
			@RequestParam("keyword") String keyword, @RequestParam(name = "page", required = false) String page,
			@RequestParam(name = "limit", required = false) String slimit) {
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

		// 검색결과가 적용된 총 목록 갯수 조회해서, 총 페이지 수 계산함
		int listCount = noticeService.selectSearchContentCount(keyword);
		// 페이지 관련 항목들 계산 처리
		Paging paging = new Paging(listCount, limit, currentPage, "nsearchContent.do");
		paging.calculate();

		// 마이바티스 매퍼에서 사용되는 메소드는 Object 1개만 전달할 수 있음
		// paging.startRow, paging.endRow, keyword 같이 전달해야 하므로 => 객체 하나를 만들어서 저장해서 보냄
		Search search = new Search();
		search.setKeyword(keyword);
		search.setStartRow(paging.getStartRow());
		search.setEndRow(paging.getEndRow());

		// 서비스 모델로 페이징 적용된 목록 조회 요청하고 결과받기
		ArrayList<Notice> list = noticeService.selectSearchContent(search);

		if (list != null && list.size() > 0) { // 조회 성공시
			// ModelAndView : Model + View
			mv.addObject("list", list); // request.setAttribute("list", list) 와 같음
			mv.addObject("paging", paging);
			mv.addObject("action", action);
			mv.addObject("keyword", keyword);

			mv.setViewName("notice/noticeListView");
		} else { // 조회 실패시
			mv.addObject("message", action + "에 대한 " + keyword + " 검색 결과가 존재하지 않습니다.");
			mv.setViewName("common/error");
		}

		return mv;
	}

	// 공지사항 등록날짜 검색 목록보기 요청 처리용 (페이징 처리 : 한 페이지에 10개씩 출력 처리)
	@RequestMapping("nsearchDate.do")
	public ModelAndView noticeSearchDateMethod(
			ModelAndView mv, Search search,
			@RequestParam("action") String action,			
			@RequestParam(name = "page", required = false) String page,
			@RequestParam(name = "limit", required = false) String slimit) {
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

		// 검색결과가 적용된 총 목록 갯수 조회해서, 총 페이지 수 계산함
		int listCount = noticeService.selectSearchDateCount(search);
		// 페이지 관련 항목들 계산 처리
		Paging paging = new Paging(listCount, limit, currentPage, "nsearchDate.do");
		paging.calculate();

		// 마이바티스 매퍼에서 사용되는 메소드는 Object 1개만 전달할 수 있음
		// paging.startRow, paging.endRow, begin, end 같이 전달해야 하므로 => 객체 하나를 만들어서 저장해서 보냄			
		search.setStartRow(paging.getStartRow());
		search.setEndRow(paging.getEndRow());

		// 서비스 모델로 페이징 적용된 목록 조회 요청하고 결과받기
		ArrayList<Notice> list = noticeService.selectSearchDate(search);

		if (list != null && list.size() > 0) { // 조회 성공시
			// ModelAndView : Model + View
			mv.addObject("list", list); // request.setAttribute("list", list) 와 같음
			mv.addObject("paging", paging);
			mv.addObject("action", action);
			mv.addObject("begin", search.getBegin());
			mv.addObject("end", search.getEnd());

			mv.setViewName("notice/noticeListView");
		} else { // 조회 실패시
			mv.addObject("message", action + "에 대한 " + search.getBegin() + "부터 "
									+ search.getEnd() + "기간 사이에 등록한 공지글 검색 결과가 존재하지 않습니다.");
			mv.setViewName("common/error");
		}

		return mv;
	}

}
