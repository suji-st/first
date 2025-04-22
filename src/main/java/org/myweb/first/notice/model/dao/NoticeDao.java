package org.myweb.first.notice.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.myweb.first.common.Paging;
import org.myweb.first.common.Search;
import org.myweb.first.notice.model.dto.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("noticeDao")
public class NoticeDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public ArrayList<Notice> selectTop3(){
		List<Notice> list = sqlSessionTemplate.selectList("noticeMapper.selectTop3");
		return (ArrayList<Notice>)list;
	}
	
	public int selectListCount() {
		return sqlSessionTemplate.selectOne("noticeMapper.selectListCount");
	}
	
	public ArrayList<Notice> selectList(Paging paging){
		List<Notice> list = sqlSessionTemplate.selectList("noticeMapper.selectList", paging);
		return (ArrayList<Notice>)list;
	}
	
	public Notice selectNotice(int noticeNo) {
		return sqlSessionTemplate.selectOne("noticeMapper.selectNotice", noticeNo);
	}
	
	// dml -----------------------------------------------------------
	public void updateAddReadCount(int noticeNo) {
		sqlSessionTemplate.update("noticeMapper.updateAddReadCount", noticeNo);
	}
	
	public int insertNotice(Notice notice) {
		return sqlSessionTemplate.insert("noticeMapper.insertNotice", notice);
	}
	
	public int deleteNotice(int noticeNo) {
		return sqlSessionTemplate.delete("noticeMapper.deleteNotice", noticeNo);
	}
	
	public int updateNotice(Notice notice) {
		return sqlSessionTemplate.update("noticeMapper.updateNotice", notice);
	}
	
	
	//공지글 검색 관련 메소드 --------------------------------------------------------
	public int selectSearchTitleCount(String keyword) {
		return sqlSessionTemplate.selectOne("noticeMapper.selectSearchTitleCount", keyword);
	}
	
	public int selectSearchContentCount(String keyword) {
		return sqlSessionTemplate.selectOne("noticeMapper.selectSearchContentCount", keyword);
	}
	
	public int selectSearchDateCount(Search search) {
		return sqlSessionTemplate.selectOne("noticeMapper.selectSearchDateCount", search);
	}
	
	public ArrayList<Notice> selectSearchTitle(Search search){
		List<Notice> list = sqlSessionTemplate.selectList("noticeMapper.selectSearchTitle", search);
		return (ArrayList<Notice>)list;
	}
	
	public ArrayList<Notice> selectSearchContent(Search search){
		List<Notice> list = sqlSessionTemplate.selectList("noticeMapper.selectSearchContent", search);
		return (ArrayList<Notice>)list;
	}
	
	public ArrayList<Notice> selectSearchDate(Search search){
		List<Notice> list = sqlSessionTemplate.selectList("noticeMapper.selectSearchDate", search);
		return (ArrayList<Notice>)list;
	}
}






