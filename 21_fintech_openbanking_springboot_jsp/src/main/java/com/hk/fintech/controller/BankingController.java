package com.hk.fintech.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hk.fintech.dtos.UserDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/banking")
public class BankingController {
   
   @GetMapping("/main")
   public String main() {
      return "main";
   }
   
   @ResponseBody
   @GetMapping("/myinfo")
   public JSONObject myInfo(HttpServletRequest request) throws IOException, ParseException {
      System.out.println("나의정보조회[계좌목록]");
      
      HttpURLConnection conn = null;
      JSONObject result = null;
      
      // 사용자 일련 번호를 가져오기 위해 session객체 구함
      HttpSession session = request.getSession();
      UserDto ldto = (UserDto)session.getAttribute("ldto");
      int userSeqNo = ldto.getUserseqno(); // 사용자 일련번호
      String useraccesstoken = ldto.getUseraccesstoken();
      URL url = new URL("https://testapi.openbanking.or.kr/v2.0/user/me?user_seq_no="+userSeqNo);
      
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Authorization","Bearer " + useraccesstoken);
      conn.setDoOutput(true);
      
      // java에서 사용할 수 있도록 읽어들이는 코드
      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
      
      StringBuilder response = new StringBuilder();
      String responseLine = null;
      
      while((responseLine = br.readLine())!=null) {
         response.append(responseLine.trim());
      }
      
      // json 형태의 문자열을 json객체로 변환 -> 값을 가져오기 편함
      result = (JSONObject)new JSONParser().parse(response.toString());
      System.out.println("result:"+result.get("res_list"));
      return result;
   }
   
   @ResponseBody
   @GetMapping("/balance")
   public JSONObject balance(String fintech_use_num, HttpServletRequest request) throws ParseException, IOException {
      System.out.println("잔액조회하기");
      HttpURLConnection conn = null;
      JSONObject result = null;
      
      HttpSession session = request.getSession();
      UserDto ldto = (UserDto)session.getAttribute("ldto");
      
      URL url = new URL("https://testapi.openbanking.or.kr/v2.0/account/balance/fin_num?"+"bank_tran_id=M202201886U"+createNum()
      + "&fintech_use_num="+fintech_use_num + "&tran_dtime="+getDateTime());
      
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Authorization", "Bearer "+ ldto.getUseraccesstoken());
      conn.setDoOutput(true);
      
      // java에서 사용할 수 있도록 읽어들이는 코드
      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
      
      StringBuilder response = new StringBuilder();
      String responseLine = null;
      
      while((responseLine = br.readLine())!=null) {
         response.append(responseLine.trim());
      }
      
      result = (JSONObject)new JSONParser().parse(response.toString());
      System.out.println("잔액:"+result.get("balance_amt"));

      return result;
   }
   
   // 이용기관 부여번호 9자리를 생성하는 메서드
   public String createNum() {
      String createNum="";
      for (int i = 0; i < 9; i++) {
         createNum+=((int)(Math.random()*10))+"";
      }
      System.out.println("이용기관부여번호9자리"+createNum);
      return createNum;
   }
   
   // 현재시간
   public String getDateTime() {
      LocalDateTime now = LocalDateTime.now(); // 현재시간 구하기
      String formatNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
      return formatNow;
   }
}