package org.myweb.first.member.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.myweb.first.common.Paging;
import org.myweb.first.common.Search;
import org.myweb.first.member.model.dto.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("memberDao")  // servlet-context.xml 에 dao 클래스로 자동 등록하는 어노테이션임. (id 속성의 아이디지정할 수도 있음)
public class MemberDao {
	//마이바티스 매퍼 파일에 실행시킬 쿼리문(sql 문) 별도로 작성함
	//root-context.xml 에 설정된 마이바티스 세션 객체를 연결해서 사용함
	
	@Autowired  // root-context.xml 에서 생성한 객체를 자동 연결하는 어노테이션임
	private SqlSessionTemplate sqlSessionTemplate;
	
	public Member selectLogin(Member member) {
		return sqlSessionTemplate.selectOne("memberMapper.selectLogin", member);
	}
	
	public int selectCheckId(String userId) {
		return sqlSessionTemplate.selectOne("memberMapper.selectCheckId", userId);
	}
	
	public Member selectMember(String userId) {
		return sqlSessionTemplate.selectOne("memberMapper.selectMember", userId);
	}
	
	//dml -----------------------------------------
	public int insertMember(Member member) {
		return sqlSessionTemplate.insert("memberMapper.insertMember", member);
	}
	
	public int updateMember(Member member) {
		return sqlSessionTemplate.update("memberMapper.updateMember", member);
	}
	
	public int deleteMember(String userId) {
		return sqlSessionTemplate.delete("memberMapper.deleteMember", userId);
	}
	
	// 관리자용 **************************************
	
	public int selectListCount() {
		return sqlSessionTemplate.selectOne("memberMapper.selectListCount");
	}
	
	public ArrayList<Member> selectList(Paging paging){
		List<Member> list = sqlSessionTemplate.selectList("memberMapper.selectList", paging);
		return (ArrayList<Member>)list;
	}
	
	public int updateLoginOk(Member member) {
		return sqlSessionTemplate.update("memberMapper.updateLoginOk", member);
	}
	
	//관리자용 회원 검색용 ************************************************
	public int selectSearchUserIdCount(String keyword) {
		return sqlSessionTemplate.selectOne("memberMapper.selectSearchUserIdCount", keyword);
	}
	
	public int selectSearchGenderCount(String keyword) {
		return sqlSessionTemplate.selectOne("memberMapper.selectSearchGenderCount", keyword);
	}
	
	public int selectSearchAgeCount(int age) {
		return sqlSessionTemplate.selectOne("memberMapper.selectSearchAgeCount", age);
	}
	
	public int selectSearchEnrollDateCount(Search search) {
		return sqlSessionTemplate.selectOne("memberMapper.selectSearchEnrollDateCount", search);
	}
	
	public int selectSearchLoginOKCount(String keyword) {
		return sqlSessionTemplate.selectOne("memberMapper.selectSearchLoginOKCount", keyword);
	}
	
	public ArrayList<Member> selectSearchUserId(Search search){
		List<Member> list = sqlSessionTemplate.selectList("memberMapper.selectSearchUserId", search);
		return (ArrayList<Member>)list;
	}
	
	public ArrayList<Member> selectSearchGender(Search search){
		List<Member> list = sqlSessionTemplate.selectList("memberMapper.selectSearchGender", search);
		return (ArrayList<Member>)list;
	}
	
	public ArrayList<Member> selectSearchAge(Search search){
		List<Member> list = sqlSessionTemplate.selectList("memberMapper.selectSearchAge", search);
		return (ArrayList<Member>)list;
	}
	
	public ArrayList<Member> selectSearchEnrollDate(Search search){
		List<Member> list = sqlSessionTemplate.selectList("memberMapper.selectSearchEnrollDate", search);
		return (ArrayList<Member>)list;
	}
	
	public ArrayList<Member> selectSearchLoginOK(Search search){
		List<Member> list = sqlSessionTemplate.selectList("memberMapper.selectSearchLoginOK", search);
		return (ArrayList<Member>)list;
	}
}










