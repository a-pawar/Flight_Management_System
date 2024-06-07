package com.auth.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AdminFlightSet")
public class FlightsAvailable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String flightName;
	private String arrival;
	private String departure;
	private String seatsAvailable;
	private int costPrice;
	
	
	
	


	public int getId() {
		return id;
	}






	public void setId(int id) {
		this.id = id;
	}






	public String getFlightName() {
		return flightName;
	}






	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}






	public String getArrival() {
		return arrival;
	}






	public void setArrival(String arrival) {
		this.arrival = arrival;
	}






	public String getDeparture() {
		return departure;
	}






	public void setDeparture(String departure) {
		this.departure = departure;
	}






	public String getSeatsAvailable() {
		return seatsAvailable;
	}






	public void setSeatsAvailable(String seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}






	public int getCostPrice() {
		return costPrice;
	}






	public void setCostPrice(int costPrice) {
		this.costPrice = costPrice;
	}






	@Override
    public String toString() {
        return "FlightDetails [id=" + id + ", flightName=" + flightName + ", arrival="
                + arrival + ", departure=" + departure + ", seatsAvaiable=" + seatsAvailable
                + "cost= " + costPrice + "]";
    }

	
		

}
