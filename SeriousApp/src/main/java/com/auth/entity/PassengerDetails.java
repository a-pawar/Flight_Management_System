package com.auth.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PassengerEntries")
public class PassengerDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String firstName;
	private String lastName;
	private int age;
	private String nation;
	
	
	
	
	public PassengerDetails() {
		super();
	}

	public PassengerDetails(String firstName, String lastName, int age, String nation) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.nation = nation;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	
	@Override
    public String toString() {
        return "PassengerDetails [id=" + id + ", firstName=" + firstName + ", lastName="
                + lastName + ", age=" + age + ", nation=" + nation;
    }

	
	
	
	

}
