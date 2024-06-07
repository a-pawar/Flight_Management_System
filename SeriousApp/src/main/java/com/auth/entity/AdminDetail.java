package com.auth.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "admin_table")
public class AdminDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String registerName;
	
	private String registerEmail;
	private String registerPassword;
	
	
	public AdminDetail(int id, String registerName, String registerEmail, String registerPassword) {
		this.id = id;
		this.registerName = registerName;
		this.registerEmail = registerEmail;
		this.registerPassword = registerPassword;
	}
	public AdminDetail() {
		super();
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRegisterName() {
		return registerName;
	}
	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}
	public String getRegisterEmail() {
		return registerEmail;
	}
	public void setRegisterEmail(String registerEmail) {
		this.registerEmail = registerEmail;
	}
	public String getRegisterPassword() {
		return registerPassword;
	}
	public void setRegisterPassword(String registerPassword) {
		this.registerPassword = registerPassword;
	} 
	
	
	
	 @Override
	    public String toString() {
	        return "AdminDetails [id=" + id + ", registerName=" + registerName + ", registerEmail="
	                + registerEmail + ", registerPassword=" + registerPassword + "]";
	    }
	
	
	
	
	
}
