package org.myweb.first.board.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.myweb.first.board.model.dto.Board;
import org.myweb.first.common.Paging;
import org.myweb.first.notice.model.dto.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("boardDao")
public class BoardDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public ArrayList<Board> selectTop3(){
		List<Board> list = sqlSessionTemplate.selectList("boardMapper.selectTop3");
		return (ArrayList<Board>)list;
	}
	
	public int selectListCount() {
		return sqlSessionTemplate.selectOne("boardMapper.selectListCount");
	}
	
	public ArrayList<Board> selectList(Paging paging){
		List<Board> list = sqlSessionTemplate.selectList("boardMapper.selectList", paging);
		return (ArrayList<Board>)list;
	}
	
	public Board selectBoard(int boardNum) {
		return sqlSessionTemplate.selectOne("boardMapper.selectBoard", boardNum);
	}
	
	// dml --------------------------------------------------------	
	public void updateAddReadCount(int boardNum) {
		sqlSessionTemplate.update("boardMapper.updateAddReadCount", boardNum);
	}
	
	public int insertBoard(Board board) {
		return sqlSessionTemplate.insert("boardMapper.insertBoard", board);
	}
}















