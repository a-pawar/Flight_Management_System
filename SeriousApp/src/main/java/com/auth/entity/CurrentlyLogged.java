package com.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "currently_logged_in")
public class CurrentlyLogged {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String registerName;
	private String registerEmail;
	
	public CurrentlyLogged(String registerName, String registerEmail) {
		super();
		this.registerName = registerName;
		this.registerEmail = registerEmail;
	}

	public CurrentlyLogged() {
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
	
	@Override
    public String toString() {
        return "Currently Logged In UserDetail [id=" + id + ", registerName=" + registerName + " registerEmail="
                + registerEmail 
                + "]";
    }
	
	
	
	
	
}
