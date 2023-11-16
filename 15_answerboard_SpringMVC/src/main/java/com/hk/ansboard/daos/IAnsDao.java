package com.hk.ansboard.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.hk.ansboard.dtos.AnsDto;

public interface IAnsDao {
	   //1. 글목록 조회하기[페이징 처리]
	   public List<AnsDto> getAllList(String pnum);
	   //1-2. 페이지수 구하기
	   public int getPCount();
	   //2.새글추가하기
	   public boolean insertBoard(AnsDto dto);
	   //3. 상세조회
	   public AnsDto getBoard(int seq);
	   //4. 수정하기
	   public boolean updateBoard(AnsDto dto);
	   //5. 조회수
	   public boolean readCount(int seq);
	   //6. 삭제하기
	   public boolean mulDel(String[] seqs);
	   //7. 답글 추가하기: update문, insert문 실행 --> transaction 처리 필요
	   public int replyUpdate(AnsDto dto);
	   public int replyInsert(AnsDto dto);
}
