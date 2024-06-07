package com.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="feedback_details")
public class Feeback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String contact_name;
	
	private String contact_email;
	private String contact_phone;
	private String contact_message;
	public Feeback(String contact_name, String contact_email, String contact_phone, String contact_message) {
		super();
		
		this.contact_name = contact_name;
		this.contact_email = contact_email;
		this.contact_phone = contact_phone;
		this.contact_message = contact_message;
	}
	public Feeback() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public String getContact_email() {
		return contact_email;
	}
	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}
	public String getContact_phone() {
		return contact_phone;
	}
	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}
	public String getContact_message() {
		return contact_message;
	}
	public void setContact_message(String contact_message) {
		this.contact_message = contact_message;
	}
	@Override
	public String toString() {
		return "Feeback [id=" + id + ", contact_name=" + contact_name + ", contact_email=" + contact_email
				+ ", contact_phone=" + contact_phone + ", contact_message=" + contact_message + "]";
	}
	
	
	
}
