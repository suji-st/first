package org.myweb.first.notice.model.service;


import java.util.ArrayList;

import org.myweb.first.common.Paging;
import org.myweb.first.common.Search;
import org.myweb.first.notice.model.dao.NoticeDao;
import org.myweb.first.notice.model.dto.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	private NoticeDao noticeDao;

	@Override
	public ArrayList<Notice> selectTop3() {
		return noticeDao.selectTop3();
	}

	@Override
	public int selectListCount() {
		return noticeDao.selectListCount();
	}

	@Override
	public ArrayList<Notice> selectList(Paging paging) {
		return noticeDao.selectList(paging);
	}

	@Override
	public int selectSearchTitleCount(String keyword) {
		return noticeDao.selectSearchTitleCount(keyword);
	}

	@Override
	public int selectSearchContentCount(String keyword) {
		return noticeDao.selectSearchContentCount(keyword);
	}

	@Override
	public int selectSearchDateCount(Search search) {
		return noticeDao.selectSearchDateCount(search);
	}

	@Override
	public ArrayList<Notice> selectSearchTitle(Search search) {
		return noticeDao.selectSearchTitle(search);
	}

	@Override
	public ArrayList<Notice> selectSearchContent(Search search) {
		return noticeDao.selectSearchContent(search);
	}

	@Override
	public ArrayList<Notice> selectSearchDate(Search search) {
		return noticeDao.selectSearchDate(search);
	}

	@Override
	public Notice selectNotice(int noticeNo) {
		return noticeDao.selectNotice(noticeNo);
	}

	@Override
	public void updateAddReadCount(int noticeNo) {
		noticeDao.updateAddReadCount(noticeNo);
	}

	@Override
	public int insertNotice(Notice notice) {
		return noticeDao.insertNotice(notice);
	}

	@Override
	public int deleteNotice(int noticeNo) {
		return noticeDao.deleteNotice(noticeNo);
	}

	@Override
	public int updateNotice(Notice notice) {
		return noticeDao.updateNotice(notice);
	}
	

}





