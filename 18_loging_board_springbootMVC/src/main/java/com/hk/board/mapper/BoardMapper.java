package com.hk.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hk.board.dtos.BoardDto;

@Mapper
public interface BoardMapper {

	//글 목록
	public List<BoardDto> getAllList();
	
	//글 상세조회
	public BoardDto getBoard(int board_seq);
	
	//글 추가
	public boolean insertBoard(BoardDto dto);
	
	//글 수정
	public boolean updateBoard(BoardDto dto);
	
	//글 삭제
	public boolean mulDel(String[] seqs);
}








