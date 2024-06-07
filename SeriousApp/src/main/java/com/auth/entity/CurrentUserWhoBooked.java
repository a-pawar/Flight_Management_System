package com.auth.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "current_users")
public class CurrentUserWhoBooked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String registerName;
    private String registerEmail;

    // One user can have many bookings
    @OneToMany(mappedBy="currentUserWhoBooked")
    private List<PassengersBooked> bookings;

    public CurrentUserWhoBooked(String registerName, String registerEmail) {
        
        this.registerName = registerName;
        this.registerEmail = registerEmail;
    }

    public CurrentUserWhoBooked() {
        
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
    
    
    public List<PassengersBooked> getBookings() {
		return bookings;
	}

	public void setBookings(List<PassengersBooked> bookings) {
		this.bookings = bookings;
	}

	@Override
    public String toString() {
        return "Currently Logged In UserDetail [id=" + id + ", registerName=" + registerName + " registerEmail="
                + registerEmail + "]";
    }
}
