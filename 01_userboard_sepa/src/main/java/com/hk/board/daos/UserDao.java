package com.hk.board.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hk.board.dtos.UserDto;

public class UserDao {

	//1단계: 드라이버 로딩
	public UserDao() {
		//강제 객체 생성: 예외처리를 해야 함
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			System.out.println("1단계:드라이버로딩 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("1단계:드라이버로딩 실패");
		}
	}
	
	//회원목록 조회기능
	public List<UserDto> getAllUser(){
		//List는 여러행의 정보(dto객체들)를 담기 위한 객체
		List<UserDto> list=new ArrayList<>();
		
		//DB연결을 위한 정보 정의
		String url="jdbc:mariadb://localhost:3306/hkedu";
		String user="root";
		String password="0905";
		Connection conn=null;//DB 연결 객체
		PreparedStatement psmt=null;// 쿼리 준비 및 실행을 위한 객체
		ResultSet rs=null;//쿼리 실행 결과를 받을 객체
		
		String sql=" SELECT userID , "
				+ "			NAME , "
				+ "			birthYear , "
				+ "			addr , "
				+ "			mobile1 , "
				+ "			mobile2 , "
				+ "			height , "
				+ "			mDate  "
				+ " FROM usertbl";
		
		// java.sql, java.io, java.net : 무조건 예외처리 해줘야 함
		try {
			conn=DriverManager.getConnection(url, user, password);
			System.out.println("2단계:DB연결성공");
			psmt=conn.prepareStatement(sql);//쿼리를 작성한 상태
			System.out.println("3단계:쿼리준비성공");
			rs=psmt.executeQuery();//쿼리 실행된 상태
			System.out.println("4단계:쿼리실행성공");
			//resultSet객체에 쿼리 결과를 받았지만 자바에서 바로 사용 못함
			while(rs.next()) {
				//[userID,name,addr,.........]
				UserDto dto=new UserDto();
				dto.setUserID(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setBirthYear(rs.getInt(3));
				dto.setAddr(rs.getString(4));
				dto.setMobile1(rs.getString(5));
				dto.setMobile2(rs.getString(6));
				dto.setHeight(rs.getInt(7));
				dto.setmDate(rs.getDate(8));
				list.add(dto);//list[dto(row),dto(row),dto(row),....]
			}
			System.out.println("5단계:쿼리결과받기 성공");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("JDBC실패:"+getClass());
		}finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(psmt!=null) {
					psmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
				System.out.println("6단계:DB닫기성공");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("6단계:DB닫기실패");
			}
		}
	
		return list;
	}
	
	//회원등록하기: insert문, 파리미터 전달받을 값들(회원의 정보들)
	public boolean insertUser(UserDto dto) {
		
		int count=0;	//쿼리실행 성공개수
		
		Connection conn=null;
		PreparedStatement psmt=null;
		
		//DB연결을 위한 정보 정의
		String url="jdbc:mariadb://localhost:3306/hkedu";
		String user="root";
		String password="0905";
		
		String sql=" INSERT INTO usertbl "
				 + " VALUES(?,?,?,?,?,?,?,SYSDATE())";
		
		try {
			conn=DriverManager.getConnection(url, user, password);
			System.out.println("2단계:DB연결성공");
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, dto.getUserID());
			psmt.setString(2, dto.getName());
			psmt.setInt(3, dto.getBirthYear());
			psmt.setString(4, dto.getAddr());
			psmt.setString(5, dto.getMobile1());
			psmt.setString(6, dto.getMobile2());
			psmt.setInt(7, dto.getHeight());
			System.out.println("3단계:쿼리준비성공");
			
			//count에 실행된 행의 개수가 저장됨
			count=psmt.executeUpdate();
			System.out.println("4단계:쿼리실행성공");
			
		} catch (SQLException e) {
			System.out.println("JDBC실패: 해당클래스:"+getClass());
			e.printStackTrace();
		}finally {
			//DB가 계속 연결되어 있으면 안되므로 닫아준다
			try {
				if(psmt!=null) {
					psmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
				System.out.println("6단계: DB닫기 성공");
				
			} catch (SQLException e) {
				System.out.println("6단계: DB닫기 실패");
				e.printStackTrace();
			}
		}
		return count>0?true:false;
	}
	
	//회원정보상세조회: select문
	public UserDto getUser(String userId) {
		
		UserDto dto=new UserDto();
		
		Connection conn=null;			//DB연결객체 저장
		PreparedStatement psmt=null;	//쿼리준비 객체 저장
		ResultSet rs=null;				//쿼리 결과 저장
		
		//DB연결을 위한 정보 정의
		String url="jdbc:mariadb://localhost:3306/hkedu";
		String user="root";
		String password="0905";
		
		String sql=" SELECT userID , "
				+ "			NAME , "
				+ "			birthYear , "
				+ "			addr , "
				+ "			mobile1 , "
				+ "			mobile2 , "
				+ "			height , "
				+ "			mDate  "
				+ " FROM usertbl "
				+ " WHERE userID = ? ";
		
		try {
			conn=DriverManager.getConnection(url, user, password);	//마리아DB에 접속되기 위함 + 예외 필수
			System.out.println("2단계: DB연결성공");
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, userId); 		//파라미터 타입에 맞는 Set어쩌구
			System.out.println("3단계: 쿼리준비 성공");
			
			rs=psmt.executeQuery();	//select문을 실행하며, 결과를 반환한다.
			System.out.println("4단계: 쿼리실행 성공");
			
			while(rs.next()) {		//결과가 있는지 행단위로 확인해서 true/false반환
				dto.setUserID(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setBirthYear(rs.getInt(3));
				dto.setAddr(rs.getString(4));
				dto.setMobile1(rs.getString(5));
				dto.setMobile2(rs.getString(6));
				dto.setHeight(rs.getInt(7));
				dto.setmDate(rs.getDate(8));
				System.out.println(dto);
			}
			System.out.println("5단계: 쿼리 결과 받기");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(psmt != null) {
					psmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
				System.out.println("6단계: DB닫기 성공");
			} catch (SQLException e) {
				System.out.println("6단계: DB닫기 실패");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return dto;
	}
	
	//회원정보 수정: update문
	public boolean updateUser(UserDto dto) {
		int count=0;	//쿼리실행 성공개수
		
		Connection conn=null;
		PreparedStatement psmt=null;
		
		//DB연결을 위한 정보 정의
		String url="jdbc:mariadb://localhost:3306/hkedu";
		String user="root";
		String password="0905";
		
		String sql="UPDATE usertbl SET 	NAME = ? , "
				+ "					birthYear = ? , "
				+ " 				addr = ? , "
				+ " 				mobile1 = ? , "
				+ " 				mobile2 = ? , "
				+ "					height = ? "
				+ "WHERE userID = ? ";
		
		try {
			conn=DriverManager.getConnection(url, user, password);
			System.out.println("2단계: DB연결성공");
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, dto.getName());
			psmt.setInt(2, dto.getBirthYear());
			psmt.setString(3, dto.getAddr());
			psmt.setString(4, dto.getMobile1());
			psmt.setString(5, dto.getMobile2());
			psmt.setInt(6, dto.getHeight());
			psmt.setString(7, dto.getUserID());
			System.out.println("3단계: 쿼리준비성공");
			count=psmt.executeUpdate();
			System.out.println("4단계: 쿼리실행성공");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(psmt != null) {
					psmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
				System.out.println("6단계: DB닫기 성공");
			} catch (SQLException e) {
				System.out.println("6단계: DB닫기 실패");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count>0?true:false;
	}
	
	//회원삭제: delete문
	public boolean deleteUser(String userId){
		int count=0;	//쿼리실행 성공개수
		
		Connection conn=null;
		PreparedStatement psmt=null;
		
		//DB연결을 위한 정보 정의
		String url="jdbc:mariadb://localhost:3306/hkedu";
		String user="root";
		String password="0905";
		
		String sql="DELETE FROM usertbl WHERE userID=?";
		try {
			conn=DriverManager.getConnection(url, user, password);
			System.out.println("2단계: DB연결 성공");
			
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, userId);
			System.out.println("3단계: 쿼리준비 성공");
			
			count=psmt.executeUpdate();
			System.out.println("4단계: 쿼리실행 성공");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(psmt != null) {
					psmt.close();
				}
				if(conn!=null) {
					conn.close();
				}
				System.out.println("6단계: DB닫기 성공");
			} catch (SQLException e) {
				System.out.println("6단계: DB닫기 실패");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return count>0?true:false;
	}
}













