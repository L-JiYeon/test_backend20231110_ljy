package com.hk.user.dtos;

import java.io.Serializable;
import java.util.Date;

public class BoardDto implements Serializable {

	//DTO 만들기 내용 및 순서
	//맴버필드작성
	//default생성자
	//생성자 오버로딩
	//getter, setter 메서드 작성
	//toString() 오버라이딩
	
	private static final long serialVersionUID = 1L;

	private int seq;
	private String id;
	private String name;
	private String password;
	private String address;
	private String email;
	private String enabled;
	private String role;
	private String title;
	private String content;
	private Date regDate;
	
	public BoardDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BoardDto(int seq, String id, String name, String password, String address, String email, String enabled,
			String role, String title, String content, Date regDate) {
		super();
		this.seq = seq;
		this.id = id;
		this.name = name;
		this.password = password;
		this.address = address;
		this.email = email;
		this.enabled = enabled;
		this.role = role;
		this.title = title;
		this.content = content;
		this.regDate = regDate;
	}
	//회원가입시 초기화
	public BoardDto(String id, String name, String password, String address, String email) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.address = address;
		this.email = email;
	}
	//나의정보 수정 초기화
	public BoardDto(String id, String address, String email) {
		super();
		this.id = id;
		this.address = address;
		this.email = email;
	}
	

	public BoardDto(int seq, String id, String title, String content, Date regDate) {
		super();
		this.seq = seq;
		this.id = id;
		this.title = title;
		this.content = content;
		this.regDate = regDate;
	}
	public BoardDto(int seq, String title, String content) {
		super();
		this.seq = seq;
		this.title = title;
		this.content = content;
	}

	public BoardDto(int seq,String id, String title, String content) {
		super();
		this.seq = seq;
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserDto [seq=" + seq + ", id=" + id + ", name=" + name + ", password=" + password + ", address="
				+ address + ", email=" + email + ", enabled=" + enabled + ", title=" + title + ", content="
				+ content + ", role=" + role + ", regDate=" + regDate + "]";
	}
	
	
}





