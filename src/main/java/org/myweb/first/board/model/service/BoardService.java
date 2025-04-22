package org.myweb.first.board.model.service;

import java.util.ArrayList;

import org.myweb.first.board.model.dto.Board;
import org.myweb.first.common.Paging;

public interface BoardService {
	ArrayList<Board> selectTop3();
	int selectListCount();
	ArrayList<Board> selectList(Paging paging);
	Board selectBoard(int boardNum);
	//dml --------------------
	void updateAddReadCount(int boardNum);
	int insertBoard(Board board);
}
