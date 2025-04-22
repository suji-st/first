package org.myweb.first.notice.model.service;

import java.util.ArrayList;

import org.myweb.first.common.Paging;
import org.myweb.first.common.Search;
import org.myweb.first.notice.model.dto.Notice;

public interface NoticeService {
	/*인터페이스 안의 모든 추상메소드 앞에 public abstract 표기 생략함*/ 
	ArrayList<Notice> selectTop3();
	int selectListCount();
	ArrayList<Notice> selectList(Paging paging);
	Notice selectNotice(int noticeNo);
	//dml ------------------------------------
	void updateAddReadCount(int noticeNo);
	int insertNotice(Notice notice);
	int deleteNotice(int noticeNo);
	int updateNotice(Notice notice);
	//검색관련 ------------------------------
	int selectSearchTitleCount(String keyword);
	int selectSearchContentCount(String keyword);
	int selectSearchDateCount(Search search);
	ArrayList<Notice> selectSearchTitle(Search search);
	ArrayList<Notice> selectSearchContent(Search search);
	ArrayList<Notice> selectSearchDate(Search search);
}
