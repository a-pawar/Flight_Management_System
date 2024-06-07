package com.auth.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "passengers_b")
public class PassengersBooked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String nation;
    private String seatName;
    
    private String flightName;
    private String flightId;
    private String parsedArrivalDate;
    private String parsedDepartDate;
    private String arrivalTime;
    private String departTime;
    private String from_location;
    private String to_location;
    private String totalPrice;
    
    
    
    

    // Mapping the column of this table 
    @ManyToOne
    CurrentUserWhoBooked currentUserWhoBooked;

    public PassengersBooked() {
        super();
    }

	public PassengersBooked( String firstName, String lastName, int age, String nation, String seatName,
			String flightName, String flightId, String parsedArrivalDate, String parsedDepartDate, String arrivalTime,
			String departTime, String from_location, String to_location, String totalPrice,
			CurrentUserWhoBooked currentUserWhoBooked) {
		super();
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.nation = nation;
		this.seatName = seatName;
		this.flightName = flightName;
		this.flightId = flightId;
		this.parsedArrivalDate = parsedArrivalDate;
		this.parsedDepartDate = parsedDepartDate;
		this.arrivalTime = arrivalTime;
		this.departTime = departTime;
		this.from_location = from_location;
		this.to_location = to_location;
		this.totalPrice = totalPrice;
		this.currentUserWhoBooked = currentUserWhoBooked;
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

	public String getSeatName() {
		return seatName;
	}

	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}

	public String getFlightName() {
		return flightName;
	}

	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public String getParsedArrivalDate() {
		return parsedArrivalDate;
	}

	public void setParsedArrivalDate(String parsedArrivalDate) {
		this.parsedArrivalDate = parsedArrivalDate;
	}

	public String getParsedDepartDate() {
		return parsedDepartDate;
	}

	public void setParsedDepartDate(String parsedDepartDate) {
		this.parsedDepartDate = parsedDepartDate;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDepartTime() {
		return departTime;
	}

	public void setDepartTime(String departTime) {
		this.departTime = departTime;
	}

	public String getFrom_location() {
		return from_location;
	}

	public void setFrom_location(String from_location) {
		this.from_location = from_location;
	}

	public String getTo_location() {
		return to_location;
	}

	public void setTo_location(String to_location) {
		this.to_location = to_location;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public CurrentUserWhoBooked getCurrentUserWhoBooked() {
		return currentUserWhoBooked;
	}

	public void setCurrentUserWhoBooked(CurrentUserWhoBooked currentUserWhoBooked) {
		this.currentUserWhoBooked = currentUserWhoBooked;
	}

	@Override
	public String toString() {
		return "PassengersBooked [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age
				+ ", nation=" + nation + ", seatName=" + seatName + ", flightName=" + flightName + ", flightId="
				+ flightId + ", parsedArrivalDate=" + parsedArrivalDate + ", parsedDepartDate=" + parsedDepartDate
				+ ", arrivalTime=" + arrivalTime + ", departTime=" + departTime + ", from_location=" + from_location
				+ ", to_location=" + to_location + ", totalPrice=" + totalPrice + ", currentUserWhoBooked="
				+ currentUserWhoBooked + "]";
	}
    
    
    
    

    




	
}
