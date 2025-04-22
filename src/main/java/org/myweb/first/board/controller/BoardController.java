package org.myweb.first.board.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.myweb.first.board.model.dto.Board;
import org.myweb.first.board.model.service.BoardService;
import org.myweb.first.common.Paging;
import org.myweb.first.notice.model.dto.Notice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BoardController {
	// 로그 객체 생성
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private BoardService boardService;

	// 뷰 페이지 내보내기용 메소드 ---------------------------------------
	// 새 게시글 원글 등록 페이지 이동 처리용
	@RequestMapping("bwform.do")
	public String moveWritePage() {
		return "board/boardWriteForm";
	}
	
	// 댓글, 대댓글 등록 페이지로 이동 처리용
	@RequestMapping("breplyform.do")
	public ModelAndView moveReplyPage(ModelAndView mv, 
			@RequestParam("bnum") int boardNum, @RequestParam("page") int currentPage) {
		
		mv.addObject("bnum", boardNum);
		mv.addObject("currentPage", currentPage);
		mv.setViewName("board/boardReplyForm");
		
		return mv;
	}
	
	// 게시글 (원글, 댓글, 대댓글) 수정 페이지로 이동 처리용
	@RequestMapping("bupview.do")
	public String moveBoardUpdatePage(
			Model model, @RequestParam("bnum") int boardNum, @RequestParam("page") int currentPage) {
		
		//수정 페이지로 전달할 board 정보 조회함
		Board board = boardService.selectBoard(boardNum);
		
		if(board != null) {
			model.addAttribute("board", board);
			model.addAttribute("currentPage", currentPage);
			return "board/boardUpdateView";
		} else {
			model.addAttribute("message", boardNum + "번 게시글 수정페이지로 이동 실패!");
			return "common/error";
		}
	}
	

	// 요청 처리용 메소드 -----------------------------------------------------

	@RequestMapping(value = "btop3.do", method = RequestMethod.POST, produces = "application/json; charset:UTF-8")
	@ResponseBody
	// public String boardCountTop3Method() { //JSONObject 클래스 사용시 리턴 방식
	public Map<String, Object> boardCountTop3Method() { // Jackson library 사용 리턴 방식
		logger.info("btop3.do run...");

		// 서비스 모델로 메소드 실행 요청하고 결과 받기
		ArrayList<Board> list = boardService.selectTop3();
		logger.info("btop3.do list : " + list);

//		JSONArray jarr = new JSONArray();
//		
//		for(Board board : list) {
//			JSONObject job = new JSONObject();
//			job.put("bnum"	, board.getBoardNum());
//			job.put("btitle", board.getBoardTitle());
//			job.put("rcount", board.getBoardReadCount());
//			
//			jarr.add(job);
//		}
//		
//		JSONObject sendJson = new JSONObject();
//		sendJson.put("blist", jarr);
//		
//		return sendJson.toJSONString();	

		// Jackson 라이브러리 사용 ---------------------------------------------
		Map<String, Object> top3Result = new HashMap<>();
		top3Result.put("blist", list);

		return top3Result; // Jackson이 자동으로 JSON으로 변환
	}

	// 게시글 전체 목록보기 요청 처리용 (페이징 처리 : 한 페이지에 10개씩 출력 처리)
	@RequestMapping("blist.do")
	public ModelAndView boardListMethod(ModelAndView mv, @RequestParam(name = "page", required = false) String page,
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
		int listCount = boardService.selectListCount();
		// 페이지 관련 항목들 계산 처리
		Paging paging = new Paging(listCount, limit, currentPage, "blist.do");
		paging.calculate();

		// 서비스 모델로 페이징 적용된 목록 조회 요청하고 결과받기
		ArrayList<Board> list = boardService.selectList(paging);

		if (list != null && list.size() > 0) { // 조회 성공시
			// ModelAndView : Model + View
			mv.addObject("list", list); // request.setAttribute("list", list) 와 같음
			mv.addObject("paging", paging);

			mv.setViewName("board/boardListView");
		} else { // 조회 실패시
			mv.addObject("message", currentPage + "페이지에 출력할 게시글 목록 조회 실패!");
			mv.setViewName("common/error");
		}

		return mv;
	}

	// 게시글 (원글, 댓글, 대댓글) 상세 내용보기 요청 처리용
	@RequestMapping("bdetail.do")
	public ModelAndView boardDetailMethod(@RequestParam("bnum") int boardNum,
			@RequestParam(name = "page", required = false) String page, ModelAndView mv) {
		logger.info("bdetail.do : " + boardNum);

		int currentPage = 1; // 상세보기 페이지에서 목록 버튼 누르면, 보고있던 목록 페이지로 돌아가기 위해 저장함
		if (page != null) {
			currentPage = Integer.parseInt(page);
		}

		Board board = boardService.selectBoard(boardNum);

		// 조회수 1증가 처리
		boardService.updateAddReadCount(boardNum);

		if (board != null) {
			mv.addObject("board", board);
			mv.addObject("currentPage", currentPage);
			mv.setViewName("board/boardDetailView");
		} else {
			mv.addObject("message", boardNum + "번 게시글 상세보기 요청 실패!");
			mv.setViewName("common/error");
		}

		return mv;
	}

	// 첨부파일 다운로드 요청 처리용 메소드
	// 스프링에서는 파일 다운로드는 스프링이 제공하는 View 클래스를 상속받은 클래스를 사용하도록 정해 놓았음
	// => 공통모듈로 파일다운로드용 뷰 클래스를 따로 만듦 => 뷰리졸버에서 연결 처리함
	// => 리턴타입은 반드시 ModelAndView 여야 함
	@RequestMapping("bfdown.do")
	public ModelAndView fileDownMethod(ModelAndView mv, HttpServletRequest request,
			@RequestParam("ofile") String originalFileName, @RequestParam("rfile") String renameFileName) {

		// 게시글 첨부파일 저장 폴더 경로 지정
		String savePath = request.getSession().getServletContext().getRealPath("resources/board_upfiles");
		// 저장 폴더에서 읽을 파일에 대한 File 객체 생성
		File downFile = new File(savePath + "\\" + renameFileName);
		// 파일 다운시 브라우저로 내보낼 원래 파일에 대한 File 객체 생성
		File originFile = new File(originalFileName);

		// 파일 다운 처리용 뷰클래스 id명과 다운로드할 File 객체를 ModelAndView 에 담아서 리턴함
		mv.setViewName("filedown"); // 뷰클래스의 id명 기입
		mv.addObject("originFile", originFile);
		mv.addObject("renameFile", downFile);

		return mv;
	}
}
