package org.myweb.first.member.model.service;

import java.util.ArrayList;

import org.myweb.first.common.Paging;
import org.myweb.first.common.Search;
import org.myweb.first.member.model.dao.MemberDao;
import org.myweb.first.member.model.dto.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//spring framework 에서는 서비스 interface 를 상속받는 후손클래스를 작성하도록 정해 놓았음
//후손 Impl 클래스를 서비스 클래스로 등록 처리함
@Service("memberService")  // servlet-context.xml 에 서비스 모델 클래스로 자동 등록되는 어노테이션임
public class MemberServiceImpl implements MemberService {
	//dao 와 연결 처리 (의존성 주입함 : DI)
	@Autowired
	private MemberDao memberDao;

	@Override
	public Member selectLogin(Member member) {
		return memberDao.selectLogin(member);
	}

	@Override
	public int selectCheckId(String userId) {
		return memberDao.selectCheckId(userId);
	}

	@Override
	public int insertMember(Member member) {
		return memberDao.insertMember(member);
	}

	@Override
	public Member selectMember(String userId) {
		return memberDao.selectMember(userId);
	}

	@Override
	public int updateMember(Member member) {
		return memberDao.updateMember(member);
	}

	@Override
	public int deleteMember(String userId) {
		return memberDao.deleteMember(userId);
	}

	// 관리자용 ------------------------------------------------------
	
	@Override
	public int selectListCount() {
		return memberDao.selectListCount();
	}

	@Override
	public ArrayList<Member> selectList(Paging paging) {
		return memberDao.selectList(paging);
	}

	@Override
	public int updateLoginOk(Member member) {
		return memberDao.updateLoginOk(member);
	}

	@Override
	public int selectSearchUserIdCount(String keyword) {
		return memberDao.selectSearchUserIdCount(keyword);
	}

	@Override
	public int selectSearchGenderCount(String keyword) {
		return memberDao.selectSearchGenderCount(keyword);
	}

	@Override
	public int selectSearchAgeCount(int age) {
		return memberDao.selectSearchAgeCount(age);
	}

	@Override
	public int selectSearchEnrollDateCount(Search search) {
		return memberDao.selectSearchEnrollDateCount(search);
	}

	@Override
	public int selectSearchLoginOKCount(String keyword) {
		return memberDao.selectSearchLoginOKCount(keyword);
	}

	@Override
	public ArrayList<Member> selectSearchUserId(Search search) {
		return memberDao.selectSearchUserId(search);
	}

	@Override
	public ArrayList<Member> selectSearchGender(Search search) {
		return memberDao.selectSearchGender(search);
	}

	@Override
	public ArrayList<Member> selectSearchAge(Search search) {
		return memberDao.selectSearchAge(search);
	}

	@Override
	public ArrayList<Member> selectSearchEnrollDate(Search search) {
		return memberDao.selectSearchEnrollDate(search);
	}

	@Override
	public ArrayList<Member> selectSearchLoginOK(Search search) {
		return memberDao.selectSearchLoginOK(search);
	}

}
