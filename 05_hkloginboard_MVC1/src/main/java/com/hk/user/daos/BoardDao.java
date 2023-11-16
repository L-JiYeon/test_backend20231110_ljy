package com.hk.user.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hk.board.datasource.DataBase;
import com.hk.user.dtos.BoardDto;
import com.hk.user.dtos.RoleStatus;

//외부에서 메서드를 호출하는 방법
// 객체생성해서 객체명.메서드()
// Static선언 클래스명.메서드()

public class BoardDao extends DataBase{

	//싱글톤 패턴 : 객체를 한번만 생성하자
	public static BoardDao boardDao;//생성된 객체를 저장
	public BoardDao() {}	
	public static BoardDao getBoardDao() {//메서드를 통해 접근 가능
		if(boardDao==null) {
			boardDao=new BoardDao();//내부에서 객체생성
		}
		return boardDao;
	}
	
	//사용자 기능
	
	//1. 회원가입 기능(enabled:'Y', role:'USER', regDate:현재날짜)
	// insert문 작성
	public boolean insertBoard(BoardDto dto) {
		int count=0;
		Connection conn=null;
		PreparedStatement psmt=null;
		
		String sql=" INSERT INTO newboard "
				 + " VALUES (NULL,?,?,?,?,?,'Y',?,?,?,SYSDATE())";
		
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, dto.getId());
			psmt.setString(2, dto.getName());
			psmt.setString(3, dto.getPassword());
			psmt.setString(4, dto.getAddress());
			psmt.setString(5, dto.getEmail());
			psmt.setString(6, dto.getTitle());
			psmt.setString(7, dto.getContent());	//3단계 완료
			psmt.setString(8, String.valueOf(RoleStatus.user));
			count=psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(null, psmt, conn);
		}
		return count>0?true:false;				
	}
	
	//2. 아이디 중복 체크하기
	//   select문 실행
	public String idCheck(String id) {
		String resultId=null;
		
		Connection conn=null;
		PreparedStatement psmt=null;
		ResultSet rs=null;
		
		String sql=" SELECT id FROM newboard WHERE id=? ";
		
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs=psmt.executeQuery();
			while(rs.next()) {
				resultId=rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(rs, psmt, conn);
		}
		return resultId;
	}
	
	//3. 로그인 기능 : ID와 password를 통해 회원정보 조회
	// 로그인 기능 만들어보기
	// main 메서드에 지금 만든 메서드 실행해 보기
	public BoardDto getLogin(String id, String password) {
		BoardDto dto=new BoardDto();
		
		Connection conn=null;
		PreparedStatement psmt=null;
		ResultSet rs=null;
		
//		String sql=" SELECT id,NAME,role "
//				 + " FROM userinfo "
//				 + " WHERE id=? AND password=? AND enabled='Y' ";
		
		//String 객체에 값이 빈번하게 변경되는 상황이라면.. 객체 생성해서 사용
		//--> 메모리 효율이 더 좋음
		StringBuffer sb=new StringBuffer();
		sb.append(" SELECT id,NAME,role ");
		sb.append(" FROM newboard ");
		sb.append(" WHERE id=? AND password=? AND enabled='Y' ");
		
		try {
			conn=getConnection();
			
			psmt=conn.prepareStatement(sb.toString());
			psmt.setString(1, id);
			psmt.setString(2, password);
			
			rs=psmt.executeQuery();
			
			while(rs.next()) {
				dto.setId(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setRole(rs.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close(rs, psmt, conn);
		}
		
		return dto;
	}
	
	//나의 정보 조회
	public BoardDto getUserInfo(String id) {
		BoardDto dto=new BoardDto();
		
		Connection conn=null;
		PreparedStatement psmt=null;
		ResultSet rs=null;
		
		String sql=" SELECT id, NAME, address, email,ROLE, regdate "
				+ " FROM newboard "
				+ " WHERE id=? ";
		
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, id);
			rs=psmt.executeQuery();
			while(rs.next()) {
				dto.setId(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setAddress(rs.getString(3));
				dto.setEmail(rs.getString(4));
				dto.setRole(rs.getString(5));
				dto.setRegDate(rs.getDate(6));
				System.out.println(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		
		return dto;
	}
	
	//나의 정보 수정하기: update문, 파라미터: UserDto
	public boolean updateUser(BoardDto dto) {
		int count = 0;
		Connection conn=null;
		PreparedStatement psmt=null;
		
		String sql=" UPDATE newboard "
				+ " SET address=?, email=? "
				+ " WHERE id=? ";
		
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, dto.getAddress());
			psmt.setString(2, dto.getEmail());
			psmt.setString(3, dto.getId());
			count=psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
		
		return count>0?true:false;
	}
	
	//회원 탈퇴하기: update문 작성, enabled='N'
	public boolean delUser(String id) {
		int count=0;
		Connection conn=null;
		PreparedStatement psmt=null;
		
		String sql=" UPDATE newboard "
				+ " SET enabled='N' "
				+ " WHERE id=? ";
		
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, id);
			count=psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
		
		return count>0?true:false;
	}
	
	//회원목록 전체조회
	public List<BoardDto> getAllUserList(){
		List<BoardDto> list=new ArrayList<>();
		
		Connection conn=null;
		PreparedStatement psmt=null;
		ResultSet rs=null;
		
		String sql=" SELECT seq, id, NAME, address, email, ROLE, enabled, regdate "
				+ " FROM newboard ";
		
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(sql);
			rs=psmt.executeQuery();
			while(rs.next()) {
				BoardDto dto=new BoardDto();
				dto.setSeq(rs.getInt(1));
				dto.setId(rs.getString(2));
				dto.setName(rs.getString(3));
				dto.setAddress(rs.getString(4));
				dto.setEmail(rs.getString(5));
				dto.setRole(rs.getString(6));
				dto.setEnabled(rs.getString(7));
				dto.setRegDate(rs.getDate(8));
				list.add(dto);
				System.out.println(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		
		return list;
	}
	
	//회원목록 전체조회[사용중]
	public List<BoardDto> getUserList(){
		List<BoardDto> list=new ArrayList<>();
		
		Connection conn=null;
		PreparedStatement psmt=null;
		ResultSet rs=null;
		
		String sql=" SELECT seq, id, NAME, ROLE, regdate "
				+ " FROM newboard "
				+ " WHERE enabled='Y' ";
		
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(sql);
			rs=psmt.executeQuery();
			while(rs.next()) {
				BoardDto dto=new BoardDto();
				dto.setSeq(rs.getInt(1));
				dto.setId(rs.getString(2));
				dto.setName(rs.getString(3));
				dto.setRole(rs.getString(4));
				dto.setRegDate(rs.getDate(5));
				list.add(dto);
				System.out.println(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rs, psmt, conn);
		}
		
		return list;
	}
	
	//회원등급수정
	public boolean boardUpdateRole(String id, String role) {
		int count = 0;
		Connection conn=null;
		PreparedStatement psmt=null;
		
		String sql=" UPDATE newboard "
				+ " SET ROLE=? "
				+ " WHERE id=? ";
		
		try {
			conn=getConnection();
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, role);
			psmt.setString(2, id);
			count=psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(null, psmt, conn);
		}
		
		return count>0?true:false;
	}
	
	//글목록 조회 가능 : 반환값 List<HkDto> --> 여러개의 행일 경우
	public List<BoardDto> getAllList(){
		List<BoardDto> list=new ArrayList<>();
		Connection conn=null;	//DB연결된 객체
		//Statement --> ? 파라미터를 제공X
		PreparedStatement psmt=null;	//쿼리준비를 위한 객체 : ? 파라미터를 제공함
		ResultSet rs=null;	//쿼리실행 결과를 받아줄 객체

		String sql=" SELECT seq, id, title, content, regdate "
				+ " FROM newboard ORDER BY regdate DESC ";

		try {
			conn=getConnection();			 //2단계: DB연결하기
			psmt=conn.prepareStatement(sql); //3단계: 쿼리 준비하기
			rs=psmt.executeQuery();			 //4단계: 쿼리 실행하기
			//5단계: 쿼리결과 받기 -> 자바에서 사용할 수 있게 처리
			while(rs.next()) {			//rs객체 안에 데이터가 존재 여부를 확인
				BoardDto dto=new BoardDto();	//행단위로 저장
				dto.setSeq(rs.getInt(1));		//DB에서 인덱스 체계는 1부터 시작
				dto.setId(rs.getString(2));		
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setRegDate(rs.getDate(5));
				list.add(dto);
				System.out.println(dto);
			}
		} catch (SQLException e) {
			System.out.println("JDB실패:"+getClass()+":"+"getAllList()");
			e.printStackTrace();
		} finally {
			close(rs,psmt,conn);	//6단계: 쿼리결과 닫기
		}

		return list;
	}
	

		//글 상세조회: select문 실행, 파라미터(pk: seq)
		//조회기능이니까 결과가 있음 -> ResultSet 필요
		//반환타입: 
		public BoardDto getBoard(int seq) {
			BoardDto dto=new BoardDto();
			Connection conn=null;
			PreparedStatement psmt=null;
			ResultSet rs=null;

			String sql=" SELECT seq, id, title, content, regdate "
					+ " FROM newboard "
					+ " WHERE seq= ? ";

			try {
				conn=getConnection();
				psmt=conn.prepareStatement(sql);
				psmt.setInt(1, seq);		//3단계 쿼리 준비 끝
				rs=psmt.executeQuery();		//조회 --> executeQuery()사용
				while(rs.next()) {
					dto.setSeq(rs.getInt(1));		//setSeq(int타입)
					dto.setId(rs.getString(2));		//setId(String타입)
					dto.setTitle(rs.getString(3));
					dto.setContent(rs.getString(4));
					dto.setRegDate(rs.getDate(5));	//setRegDate(Date타입)
					System.out.println(dto);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close(rs,psmt,conn);
			}

			return dto;
		}

		//글 수정하기: update문 실행, 파라미터(seq,title,content),regdate는 쿼리에서 수정
		//결과 X -> 테이블을 수정
		public boolean updateBoard(BoardDto dto) {
			int count=0;
			Connection conn=null;
			PreparedStatement psmt=null;

			String sql=" UPDATE newboard "
					+ " SET title=?,content=?,regdate=SYSDATE() "
					+ " WHERE seq= ? ";

			try {
				conn=getConnection();					//2

				psmt=conn.prepareStatement(sql);
				psmt.setString(1,dto.getTitle());
				psmt.setString(2,dto.getContent());
				psmt.setInt(3,dto.getSeq());			//3

				count=psmt.executeUpdate();				//4: 수정된 행의 개수를 반환함
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				close(null,psmt,conn);
			}

			return count>0?true:false;
		}

		//글 삭제: delete문 실행, 파라미터(seq)
		//테이블 수정: ResultSet 없어도 됨  --> 결과값이 없기 때문에
		public boolean deleteBoard(int seq) {
			int count=0;
			Connection conn=null;
			PreparedStatement psmt=null;

			String sql="DELETE FROM newboard WHERE seq=?";

			try {
				conn=getConnection();

				psmt=conn.prepareStatement(sql);
				psmt.setInt(1, seq);

				count=psmt.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				close(null,psmt,conn);
			}

			return count>0?true:false;
		}

		// 화면처리: 여러개의 체크박스 중에 체크된 seq만 전달받기 -> 같은 name 여러값 전달
		//												chk: [sep,sep,sep...]
		//여러개의 쿼리 실행 --> 배치 개념
		//실행하고 오류 없으면
		//conn.commit() 실행 --> DB 반영
		//오류가 있으면 --> conn.rollBack()
		
		public boolean mulDel(String[] seqs) {
			boolean isS=true;	//성공여부

			int [] count=null;	//쿼리 실행결과 개수
			
			Connection conn=null;
			PreparedStatement psmt=null;

			String sql=" delete from newboard where seq = ? ";
			
			try {
				conn=getConnection();
				//자동커밋 - 수동설정
				conn.setAutoCommit(false);	//rollback이 안 되기 때문에
				
				psmt=conn.prepareStatement(sql);
				for (int i = 0; i < seqs.length; i++) {
					psmt.setString(1, seqs[i]);		//여기서는 seq의 타입이 String임: setString()사용	
					psmt.addBatch();	//delete문 하나 저장
				}
				//3단계: 쿼리 준비 성공
				
				count=psmt.executeBatch();	//여러개의 쿼리를 실행시켜야 하고 그 여러개 결과를 저장해야 하기 떄문에 배열로	//[1,1,1]
											//실행 결과를 배열로 반환: [1,1,1]
				conn.commit();	//DB에 반영하기
			} catch (SQLException e) {
				e.printStackTrace();
				
				try {
					//TX 처리
					conn.rollback();	//작업이 실패하면 모두 되돌리기
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 	
			} finally {
				try {
					//TX 처리
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				close(null,psmt,conn);
				//화면처리를 위한 성공여부 확인
				for(int i=0; i < count.length; i++) {
					if(count[i]!=1) {
						isS=false;
						break;
					}
				}
			}
			
			

			return isS;
		}
}
















