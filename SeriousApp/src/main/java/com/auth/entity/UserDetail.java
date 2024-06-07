package com.auth.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "MyTable")
public class UserDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String registerName;
	private String dob;
	private String registerEmail;
	private String registerPassword; 
	//making confirmPassword not to store in database
	private transient String confirmPassword;
	
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
	public String getDob() {
		return dob;
	}
	
	public void setDob(String dob) {
		this.dob = dob;
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
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	 @Override
	    public String toString() {
	        return "UserDetails [id=" + id + ", registerName=" + registerName + ", dob=" + dob + ", registerEmail="
	                + registerEmail + ", registerPassword=" + registerPassword + ", confirmPassword=" + confirmPassword
	                + "]";
	    }
	
	
	
	
}
