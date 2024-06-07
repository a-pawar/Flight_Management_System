package com.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.entity.AdminDetail;
import com.auth.entity.CurrentlyLogged;
import com.auth.entity.FlightsAvailable;
import com.auth.entity.PassengersBooked;
import com.auth.entity.UserDetail;
import com.auth.repository.AdminLoginRepository;
import com.auth.repository.AdminRepository;
import com.auth.repository.BookingsRepository;
import com.auth.repository.LoggedInRepository;
import com.auth.repository.PassengerBookedRepository;
import com.auth.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired
	private LoggedInRepository logRepo;
	
	@Autowired
	private AdminLoginRepository adminLoginRepo;
	
	@Autowired
    private BCryptPasswordEncoder bp;
	
	@Autowired
	private BookingsRepository bookRepo;
	
	@Autowired
	private PassengerBookedRepository passRepo;
    
    public UserDetail login(String username,String password) {
    	 List<UserDetail> users = repo.findByRegisterName(username);
         for (UserDetail user : users) {
             if (bp.matches(password, user.getRegisterPassword())) {
                 return user;
             }
         }
         return null; // Return null if no matching user or password found
    }
    
    public AdminDetail adminLogin(String username,String password) {
   	 List<AdminDetail> users = adminLoginRepo.findByRegisterAdminName(username);
        for (AdminDetail user : users) {
            if (bp.matches(password, user.getRegisterPassword())) {
                return user;
            }
        }
        return null; // Return null if no matching user or password found
   }
    
    
    
    
    public boolean isEmailRegistered(String email) {
    	List<UserDetail> users = repo.findByRegisterEmail(email);
    	System.out.println(users);
    	if(users.size()==0) {
    		return false;
    	}
    	return true;
    }
    
    //find all userDetails by email for resetting
    public UserDetail giveMeAllUsersByEmail(String email) {
    	List<UserDetail> users = repo.findByRegisterEmail(email);
    	if(users.size()==0) {
    		return null;
    	}
    	return users.get(0);
    }
    
    // for performing flight book related search
    public List<FlightsAvailable> fetch(String currentLocation,String Destination, String departure_date, int seats) {
    	List<String> ans=adminRepo.find(departure_date);
    	List<FlightsAvailable> flight=adminRepo.findByLocationDestination(currentLocation,Destination);
    	List<FlightsAvailable> flights=adminRepo.findByLocation(currentLocation,Destination,departure_date);
    	
    	System.out.println(flight);
    	System.out.println(ans);
    	System.out.println(flights);
    	
    	List<FlightsAvailable> fligh=adminRepo.findByLocationDestinationAndSeatsAvailable(currentLocation,Destination,departure_date,seats);
    	if(flights!=null) {
    		return flights;
    	}
    	System.out.println(flights);
    	return new ArrayList<>();
    }
    
    //for performing admin update operation
    public boolean isUpdateDone(String flightName, String arrival, String departure, String seatsAvailable, int costPrice) {
    	System.out.println(flightName);
    	int ans = adminRepo.updateFlightDetails(flightName, arrival, departure, seatsAvailable, costPrice);
    	return ans>0?true:false;
    }
    
    //for perfroming admin view operation
    public List<FlightsAvailable> isViewAvailable(String currentLocation,String Destination) {
    	List<FlightsAvailable> flights = adminRepo.findByLocationDestination(currentLocation,Destination);
    	if(flights==null) {
    		return new ArrayList<>();
    	}
    	return flights;
    	
    }
    
    //for performing admin search operation
    public List<FlightsAvailable> isSearchAvailable(String currentLocation,String Destination,String departure_date) {
    	List<FlightsAvailable> flights=adminRepo.findByLocation(currentLocation,Destination,departure_date);
    	if(flights==null) {
    		return new ArrayList<>();
    	}
    	return flights;
    	
    }
    
    
    //to get all the passengers names registered so far
    public CurrentlyLogged fetchLogged() {
    	CurrentlyLogged curr = logRepo.select();
    	return curr;
    }
    
    
    // for finding all userId current user who booked
    public List<Integer> findAllCurrentUserId(String currentMail) {
    	List<Integer> ans = bookRepo.findAllIdsByRegisterEmail(currentMail);
    	if(ans == null || ans.size()==0) {
    		return new ArrayList<>();
    	}
    	
    	return ans;
    }
    
    //for deleting userId with specific userId who booked
    public int deleteUserBookId(int uid) {
    	int ans=bookRepo.deleteUid(uid);
    	return ans;
    }
    
    
    //find all passengers list with the userId
    public List<PassengersBooked> findAllPassengers(List<Integer> currentUserIds){
    	List<PassengersBooked> ans = passRepo.findByCurrentUserIds(currentUserIds);
    	if(ans == null || ans.size()==0) {
    		return new ArrayList<>();
    	}
    	
    	return ans;
    }
    
    //delete the all list with userId
    public int deleteAllPassengers(Integer currentUserIds){
    	int ans = passRepo.deleteByUserId(currentUserIds);
    	
    	return ans;
    }
    
    
    
    //updating the password for resetting
    public int updatingPassword(String mail,String pass) {
    	int ans = repo.updateByRegisterEmail(mail, pass);
    	 
    	return ans;
    }
    
    
    //to search for given flightName
    
    public boolean isFlightNameFound(String flightName) {
    	List<FlightsAvailable> ans = adminRepo.isFlightNameFound(flightName);
    	
    	if(ans.size()==0) {
    		return false;
    	}
    	
    	return true;
    }
    
    
    //delete the flight with that name
    public int deleteFlight(String flightName) {
    	int ans = adminRepo.deleteFlight(flightName);
    	
    	return ans;
    }
    
    //find the number of seats in flightname
    public String seats(String flightName) {
    	List<String> ans =  adminRepo.seatsCapacity(flightName);
    	return ans.get(0);
    }
    
    //updating the seats in flights
    public int updateSeats(String flightName,String seat) {
    	int ans = adminRepo.updateSeats(flightName, seat);
    	
    	return ans;
    }
    
    //updating the profile 
    public int updateEntry(String registerName,String registerEmail) {
    	int ans = bookRepo.updateEntry(registerName, registerEmail);
    	
    	return ans;
    }
    
  //updating the profile 
    public int updateDetailsEntry(String registerName,String registerEmail,String dob) {
    	int ans = repo.updateDetails(registerName, registerEmail,dob);
    	
    	return ans;
    }
    
    //find all bookings
    public List<PassengersBooked> findAllPassengersBookings(){
    	List<PassengersBooked> ans = passRepo.findByAllUserIds();
    	if(ans == null || ans.size()==0) {
    		return new ArrayList<>();
    	}
    	
    	return ans;
    }
    
    public int deleteAllPassengersSpecificToFlight(String flightName) {
    	String ids[] = flightName.split(" ");
    	String id = ids[1];
    	int ans=passRepo.deleteAllPassengersSpecificToFlight(id);
    	
    	return ans;
    }
    
    //finding all userIds specific to flight
    public List<Integer> findAllUserIdsSpecificToFlight(String flightName){
    	
    	String ids[] = flightName.split(" ");
    	String id = ids[1];
    	List<Integer> ans=passRepo.findAllIdsSpecificToFlight(id);
    	if(ans.size()==0) {
    		return new ArrayList<>();
    	}
    	return ans;
    }
    
    //deleting all ids booked
    public int deletedBookingCurrentUserIds(List<Integer> ids) {
    	int ans=bookRepo.deleteByCurrentUserIds(ids);
    	
    	return ans;
    	
    }
    
    public List<String> findAllEmailIdsWithThisFlight(String flightName){
    	List<String> ans = passRepo.findAllEmailIdsWithThisFlight(flightName);
    	
    	if(ans.size()==0)  return new ArrayList<>();
    	
    	return ans;
    }
    
    public String findFlightWithUserId(int uid) {
    	List<PassengersBooked> ans = passRepo.findFlightWithUserId(uid);
    	
    	return ans.get(0).getFlightId() + " " + ans.get(0).getFlightName();
    }

}
