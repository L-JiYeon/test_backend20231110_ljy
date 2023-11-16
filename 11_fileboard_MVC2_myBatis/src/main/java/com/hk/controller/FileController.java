package com.hk.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hk.daos.FileDao;
import com.hk.dtos.FileDto;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("*.file")
public class FileController extends HttpServlet {
	private static final long serialVersionUID = 1L;


	// ex) enctype=“multipart/form-data” 이런 식으로 쓰여지면 httpservletrequest 사용 불가
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset-utf-8");
		
		String command=request.getRequestURI().substring(request.getContextPath().length());
		System.out.println("command값:"+command);
		
		FileDao dao=new FileDao();
		
		if(command.equals("/fileUploadForm.file")) {
			response.sendRedirect("uploadForm.jsp");
		} else if(command.equals("/fileUpload.file")) {
			MultipartRequest multi=null;
			
			//1. 경로 정의(상대경로, 절대경로)
			// - 절대경로지정 => 현 개발환경에서 사용 가능, 다른 개발환경에서는 경로 변경 필수
//			String saveDirectory="C:/workspace_20230710_backend_ui/11_fileboard_MVC2_myBatis/src/main/webapp/upload";
			
			// - 상대경로지정
			String saveDirectory=request.getSession().getServletContext().getRealPath("upload");
			
			System.out.println("상대경로:"+saveDirectory);
			//2. file 업로드 사이즈: 1b -> 1024b -> 1kb -> 1024kb -> 1MB * 10 = 10MB
			int maxPostSize=1*1024*1024*10;	//10MB
			
			try {
				//MultipartRequest 객체가 생성되면 업로드가 실행된다
				//파라미터내용(요청객체, 저장경로, 최대업로드, 사이즈, 인코딩, 중복파일명 재정의)
				multi=new MultipartRequest(request, saveDirectory, maxPostSize, "utf-8", new DefaultFileRenamePolicy());
//				multi.getParameter("title");	//text들도 multi객체로 받아야 함
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			//DB에 파일정보 추가하기
			//1. 원본 파일명 구하기
			// "filename"은 페이지에서 전달될 때 정의했던 이름 name="filename"
			String origin_name=multi.getOriginalFileName("filename");
			System.out.println("원본파일명:"+origin_name);
			
			//2. 저장 파일명 구하기: UUID객체 ---> 32자리 값을 구해서 저장
			// ex. "12345678-12345678-12345678-12345678"
			String random32=UUID.randomUUID().toString().replace("-", "");	//"-" 제거
			String stored_name=random32		//abc.jpg -> random32 + .jpg 추출
					+(origin_name.substring(origin_name.lastIndexOf(".")));
			
			System.out.println("저장 파일명:"+stored_name);
			
			//3. 파일사이즈 구하기: length()는 long타입으로 반환 --> int로 형변환 필요
			int file_size=(int)multi.getFile("filename").length();
			System.out.println("파일사이즈:"+file_size);
			
			//4. DB에 정보 추가하기
			boolean isS=dao.insertFile(new FileDto(0, origin_name, stored_name, file_size, null));
			
			//5. 저장된 파일명 변경하기(old이름 --> stored이름)
			//											  getFilesystemName(): 실제 저장되어 있는 파일명
			File oldFile=new File(saveDirectory+"/"+multi.getFilesystemName("filename"));
			File newFile=new File(saveDirectory+"/"+stored_name);
			oldFile.renameTo(newFile);	//old파일명을 new파일명으로 변경
			
			if(isS) {
				response.sendRedirect("uploadForm.jsp?filename="+stored_name);
			}else {
				response.sendRedirect("error.jsp");
			}
			
		} else if(command.equals("/downloadList.file")) {
			List<FileDto> list= dao.getFileList();
			
			request.setAttribute("list", list);
			
			//forward()메서드
			request.getRequestDispatcher("fileList.jsp").forward(request, response);
		} else if(command.equals("/download.file")) {	//다운로드하기
			int seq=Integer.parseInt(request.getParameter("seq"));
			
			//DB에 저장되어 있는 파일의 정보를 가져오기(origin_name, stored_name)
			//origin_name은 다운로드할 때 사용자에게 보내줄 이름
			//stored_name은 실제 파일의 경로를 구하기 위해서...
			FileDto dto=dao.getFileInfo(seq);
			
			//파일의 저장 경로
			String saveDirectory=request.getSession().getServletContext().getRealPath("upload");
			
			String filePath=saveDirectory+ "/"+ dto.getStored_name();
			
			// 다운로드 환경을 위한 설정 --------------시작------
			//- 브라우저로 응답할 때 설정 초기화
			response.reset();
			
			//- 다운로드 파일의 형식을 모른다면 octet-stream으로 설정
//			response.setContentType("text/html");
//			response.setContentType("text/msword");
			response.setContentType("application/octet-stream");
			
			//파일의 다운로드 버튼을 클릭했을 때, 저장화면창이 나오도록 처리
			//파일명을 원본파일명으로 바꿔주는 과정
			String filename=new String(dto.getOrigin_name().getBytes("utf-8"),"8859_1");
			response.setHeader("Content-Disposition", "attachment; filename="+filename);
			// 다운로드 환경을 위한 설정 --------------종료-----
			
			//다운로드할 파일 읽어와서 클라이언트로 출력하기
			// 디렉토리에 있는 저장파일(바이트 단위) ----> JAVA ----> 클라이언트 출력 (읽어와서 내보내는 흐름)
			
			File file=new File(filePath);	//file객체 생성
			
			//java가 한번에 읽을 수 있는 양의 크기만큼 배열을 생성
//			byte[] b = {1,2,3,4};
			byte[] b = new byte[(int)file.length()];
			
			FileInputStream in = null;		//파일을 읽어들이는 파이프(입력)
			ServletOutputStream out=null;	//내보내기 위한 파이프(출력)
			
			try {
				in=new FileInputStream(file);
				out=response.getOutputStream();
				int numRead=0;		//읽어들이는 값의 개수를 저장할 변수
				
				while((numRead=in.read(b, 0, b.length))!= -1) {	//입력
					out.write(b, 0, numRead);	//출력
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace(); 
			} finally {
				out.flush();	//남은 데이터가 있으면 모두 밀어내서 내보내기
				out.close();
				in.close();
			}
		}
	}

}













