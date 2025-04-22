package org.myweb.first.board.model.service;

import java.util.ArrayList;

import org.myweb.first.board.model.dao.BoardDao;
import org.myweb.first.board.model.dto.Board;
import org.myweb.first.common.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("boardService")
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;

	@Override
	public ArrayList<Board> selectTop3() {
		return boardDao.selectTop3();
	}

	@Override
	public int selectListCount() {
		return boardDao.selectListCount();
	}

	@Override
	public ArrayList<Board> selectList(Paging paging) {
		return boardDao.selectList(paging);
	}

	@Override
	public Board selectBoard(int boardNum) {
		return boardDao.selectBoard(boardNum);
	}

	@Override
	public void updateAddReadCount(int boardNum) {
		boardDao.updateAddReadCount(boardNum);
	}

	@Override
	public int insertBoard(Board board) {
		return boardDao.insertBoard(board);
	}
}












