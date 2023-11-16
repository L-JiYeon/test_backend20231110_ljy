package com.hk.main;

import java.util.List;
import java.util.Scanner;

import com.hk.board.daos.HkDao;
import com.hk.board.dtos.HkDto;

public class MainTest {

	public static void main(String[] args) {
		
		HkDao dao=new HkDao();
		
		Scanner scan=new Scanner(System.in);
		
		while(true) {
			
			System.out.println("1.글목록 2.글추가 3.글상세조회 4.글수정하기 5.글삭제하기 6.종료");
			
			int num=scan.nextInt();
			
			if(num==1) {
				//글목록 가져오는 기능
				System.out.println("==글목록==");
				List<HkDto> list=dao.getAllList();
			} else if(num==2) {
				//글 추가
				System.out.println("==글추가==");
				System.out.println("아이디를 입력하세요:");
				String id=scan.next();
				System.out.println("제목을 입력하세요:");
				String title=scan.next();
				System.out.println("내용을 입력하세요:");
				String content=scan.next();	
				dao.insertBoard(new HkDto(id,title,content));
				
				dao.insertBoard(new HkDto("hk2","제목추가","내용추가"));
			} else if(num==3) {
				//상세 조회
				System.out.println("==상세내용==");
				System.out.println("조회할 글 번호를 입력하세요:");
				int seq=scan.nextInt();
				HkDto dto=dao.getBoard(seq);
			} else if(num==4) {
				//글 수정
				System.out.println("==수정하기==");
				System.out.println("수정할 글의 번호를 입력하세요:");
				int seq =scan.nextInt();
				System.out.println("수정할 제목을 입력하세요:");
				String title=scan.next();
				System.out.println("수정할 내용을 입력하세요:");
				String content=scan.next();
				dao.updateBoard(new HkDto(seq,title,content));
			} else if(num==5) {
				//글 삭제
				System.out.println("==글 삭제하기==");
				System.out.println("삭제할 글 번호를 입력하세요:");
				int seq=scan.nextInt();
				System.out.println("삭제할 글의 번호:"+seq);
				dao.deleteBoard(seq);
			} else if(num==6) {
				System.out.println("게시판 관리 시스템을 종료합니다.");
				break;
			}
		}
//		
//		//글목록 가져오는 기능
//		System.out.println("==글목록==");
//		List<HkDto> list=dao.getAllList();
//		
//		//글 추가
//		System.out.println("==글추가==");
//		dao.insertBoard(new HkDto("hk2","제목추가","내용추가"));
//		
		//상세 조회
		
		//글 수정
		
		//글 삭제
	}

}
