package com.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.entity.FlightsAvailable;
import com.auth.entity.PassengersBooked;

import jakarta.transaction.Transactional;

@Repository
public interface PassengerBookedRepository extends JpaRepository<PassengersBooked,Integer>{
	
	
	@Query("SELECT p FROM PassengersBooked p WHERE p.currentUserWhoBooked.id IN :currentUserIds")
    List<PassengersBooked> findByCurrentUserIds(@Param("currentUserIds") List<Integer> currentUserIds);
	
	
	@Query("SELECT p FROM PassengersBooked p")
    List<PassengersBooked> findByAllUserIds();
	
	@Transactional
	@Modifying
	@Query("DELETE FROM PassengersBooked p WHERE p.currentUserWhoBooked.id = :uid")
	int deleteByUserId(@Param("uid") Integer uid);
	
	
	@Query("SELECT p FROM PassengersBooked p WHERE p.currentUserWhoBooked.id = :uid")
	List<PassengersBooked> findFlightWithUserId(@Param("uid") Integer uid);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM PassengersBooked p WHERE p.flightName = :flightName")
	int deleteAllPassengersSpecificToFlight(@Param("flightName") String flightName);
	
	@Query("SELECT p.currentUserWhoBooked.id FROM PassengersBooked p WHERE p.flightName = :flightName")
    List<Integer> findAllIdsSpecificToFlight(@Param("flightName") String flightName);
	
	
	@Query("SELECT DISTINCT p.currentUserWhoBooked.registerEmail FROM PassengersBooked p WHERE p.flightName = :flightName")
    List<String> findAllEmailIdsWithThisFlight(@Param("flightName") String flightName);
	

	
}
