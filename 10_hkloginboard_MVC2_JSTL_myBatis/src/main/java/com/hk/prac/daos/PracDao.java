package com.hk.prac.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.hk.prac.config.SqlMapConfig;
import com.hk.prac.dtos.PracDto;

public class PracDao extends SqlMapConfig{
   
   private String namespace="com.hk.prac.";
   
   //1.글목록 조회하기
//   public List<AnsDto> getAllList(){
//      List<AnsDto> list=new ArrayList();
//      
//      SqlSession sqlSession=null;//쿼리를 실행시켜주는 객체.
//      
//      try {
//         //sqlSessionFactory객체의 openSession()를 통해
//         //sqlSession객체를 구한다.
//         sqlSession=getSqlSessionFactory().openSession(true); //true:autocommit설정
//         
//         //sqlSession객체에 쿼리 실행 메서드를 통해 쿼리 실행
//         //쿼리실행메서드(쿼리이름, 파라미터,)
//         list=sqlSession.selectList(namespace+"boardList");
//      } catch (Exception e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }finally {
//         sqlSession.close();
//      }
//      return list;
//   }
   
   //1. 글목록 조회하기[페이징 처리]
   public List<PracDto> getAllList(String pnum){
	   List<PracDto> list = new ArrayList();
	   
	   SqlSession sqlSession=null;
	   
	   Map<String,String> map = new HashMap<>();
	   map.put("pnum", pnum);	//페이지 번호 저장
	   
	   try {
		sqlSession=getSqlSessionFactory().openSession(true);
		list=sqlSession.selectList(namespace+"boardList", map);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		sqlSession.close();
	}
	   
	   return list;
   }
   
   //1-2. 페이지수 구하기
   public int getPCount() {
	   int count=0;
	   
	   SqlSession sqlSession=null;
	   
	   try {
		sqlSession=getSqlSessionFactory().openSession(true);
		count=sqlSession.selectOne(namespace+"getPCount");
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		sqlSession.close();
	}
	   
	   return count;
   }
   
   
   //2.새글추가하기
   public boolean insertBoard(PracDto dto) {
      int count=0;
      SqlSession sqlSession=null;
      try {
         sqlSession=getSqlSessionFactory().openSession(true);
         //파라미터 타입: DTO,Array, Map(파라미터 기본 타입), 값한개(int,String 해당타입으로 정의)
         count=sqlSession.insert(namespace+"insertBoard", dto);
      } catch (Exception e) {
         e.printStackTrace(); //<-이게 없으면 오류 출력 안됨
      }finally {
         sqlSession.close();
      }      
      return count>0?true:false;
   }
   
   //3. 상세조회
   public PracDto getBoard(int seq) {
	   PracDto dto=null;
	   
	   //쿼리를 실행하려면 필요한 객체는????
	   SqlSession sqlSession=null;
	   
	   //Map에 담아서 파라미터 전달하기
	   Map<String, Integer>map=new HashMap<>();
	   map.put("seq", seq);
	   
	   try {
		   sqlSession = getSqlSessionFactory().openSession(true);
//		   dto = sqlSession.selectOne(namespace+"getBoard", seq);
		   dto = sqlSession.selectOne(namespace+"getBoard", map);
	   } catch(Exception e) {
		   e.printStackTrace();
	   } finally {
		   sqlSession.close();
	   }
	   return dto;
   }
   
   //4. 수정하기
   public boolean updateBoard(PracDto dto) {
	   int count=0;
	   SqlSession sqlSession = null;
	   
	   try {
		sqlSession=getSqlSessionFactory().openSession(true);
		count=sqlSession.update(namespace+"updateBoard",dto);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		sqlSession.close();
	}
	   
	   return count>0?true:false;
   }
   
   //5. 조회수
   public boolean readCount(int seq) {
	   int count=0;
	   SqlSession sqlSession=null;
	   try {
		sqlSession=getSqlSessionFactory().openSession(true);
		count=sqlSession.update(namespace+"readCount",seq);
	} catch (Exception e) {
		e.printStackTrace();
	} 
	 return count>0?true:false;
   }
   
   //6. 삭제하기
   public boolean mulDel(String[] seqs) {
	   int count=0;
	   SqlSession sqlSession=null;
	   Map<String, String[]> map = new HashMap<>();
	   map.put("seqs", seqs);
	   
	   try {
		sqlSession=getSqlSessionFactory().openSession(true);
		count=sqlSession.update(namespace+"mulDel",map);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		sqlSession.close();
	}
	return count>0?true:false;
   }
   
 //글 삭제: delete문 실행, 파라미터(seq)
 	//테이블 수정: ResultSet 없어도 됨  --> 결과값이 없기 때문에
 	public boolean deleteBoard(int seq) {
 		int count=0;
 		SqlSession sqlSession=null;
 		Connection conn=null;
 		PreparedStatement psmt=null;


 		try {
 			sqlSession=getSqlSessionFactory().openSession(true);
 			count=sqlSession.update(namespace+"deleteBoard",seq);

 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} finally {
 			sqlSession.close();
 		}

 		return count>0?true:false;
 	}

   public void test() {
      //쿼리를 실행시킬 수 있는 객체 : sqlSession객체를 구함
      SqlSession sqlSession=getSqlSessionFactory().openSession();
      //쿼리를 실행한다.--> sqlMapper.xml에 있는 쿼리를 실행한거임
      List<PracDto>list= sqlSession.selectList("boardList");
   }
}




