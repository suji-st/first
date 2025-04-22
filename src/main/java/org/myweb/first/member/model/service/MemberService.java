package org.myweb.first.member.model.service;

import java.util.ArrayList;

import org.myweb.first.common.Paging;
import org.myweb.first.common.Search;
import org.myweb.first.member.model.dto.Member;

//spring framework 에서는 서비스 모델은 인터페이스로 만들도록 정해져 있음
//인터페이스에서 비즈니스 로직 처리용 메소드 규칙을 정의함
public interface MemberService {
	/*public abstract*/ Member selectLogin(Member member);
	int selectCheckId(String userId);
	Member selectMember(String userId);	
	//dml ----------------------------
	int insertMember(Member member);
	int updateMember(Member member);
	int deleteMember(String userId);
	//관리자용 ---------------------------
	int selectListCount();
	ArrayList<Member> selectList(Paging paging);
	int updateLoginOk(Member member);
	//관리자용 검색 카운트용 -----------------
	int selectSearchUserIdCount(String keyword);
	int selectSearchGenderCount(String keyword);
	int selectSearchAgeCount(int age);
	int selectSearchEnrollDateCount(Search search);
	int selectSearchLoginOKCount(String keyword);
	//관리자용 검색 목록 조회용 -------------------
	ArrayList<Member> selectSearchUserId(Search search);
	ArrayList<Member> selectSearchGender(Search search);
	ArrayList<Member> selectSearchAge(Search search);
	ArrayList<Member> selectSearchEnrollDate(Search search);
	ArrayList<Member> selectSearchLoginOK(Search search);
}
