package com.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.auth.entity.FlightsAvailable;

@Repository
public interface AdminRepository extends JpaRepository<FlightsAvailable,Integer>{
	
	@Query("SELECT fa FROM FlightsAvailable fa WHERE SUBSTRING_INDEX(fa.arrival, ' ', 1) = SUBSTRING_INDEX(:cl, ' ', 1) AND SUBSTRING_INDEX(fa.departure, ' ', 1) = SUBSTRING_INDEX(:dl, ' ', 1) AND SUBSTRING_INDEX(SUBSTRING_INDEX(fa.arrival, ' ', 2), ' ', -1) = :dt AND fa.seatsAvailable >= :se")
	List<FlightsAvailable> findByLocationDestinationAndSeatsAvailable(@Param("cl") String location, @Param("dl") String destination, @Param("dt") String departDate, @Param("se") int seats);
	
	@Query("SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(fa.arrival, ' ', 2), ' ', -1) FROM FlightsAvailable fa WHERE SUBSTRING_INDEX(SUBSTRING_INDEX(fa.arrival, ' ', 2), ' ', -1) = :dt")
	List<String> find(@Param("dt") String departDate);
	
	@Query("SELECT fa FROM FlightsAvailable fa WHERE SUBSTRING_INDEX(fa.arrival, ' ', 1) = SUBSTRING_INDEX(:cl, ' ', 1) AND SUBSTRING_INDEX(fa.departure, ' ', 1) = SUBSTRING_INDEX(:dl, ' ', 1) ")
	List<FlightsAvailable> findByLocationDestination(@Param("cl") String location, @Param("dl") String destination);
	
	@Query("SELECT fa FROM FlightsAvailable fa WHERE SUBSTRING_INDEX(fa.arrival, ' ', 1) = SUBSTRING_INDEX(:cl, ' ', 1) AND SUBSTRING_INDEX(fa.departure, ' ', 1) = SUBSTRING_INDEX(:dl, ' ', 1) AND SUBSTRING_INDEX(SUBSTRING_INDEX(fa.arrival, ' ', 2), ' ', -1) = :dt")
	List<FlightsAvailable> findByLocation(@Param("cl") String location, @Param("dl") String destination,@Param("dt") String departDate);
	

	@Modifying
    @Transactional
    @Query("UPDATE FlightsAvailable f SET f.arrival = :arrival, f.departure = :departure, f.seatsAvailable = :seatsAvailable, f.costPrice = :costPrice WHERE f.flightName = :flightName")
    int updateFlightDetails(@Param("flightName") String flightName, 
                            @Param("arrival") String arrival, 
                            @Param("departure") String departure, 
                            @Param("seatsAvailable") String seatsAvailable, 
                            @Param("costPrice") int costPrice);
	
	@Query("SELECT fa from FlightsAvailable fa where fa.flightName = :flightName")
	List<FlightsAvailable> isFlightNameFound(@Param("flightName") String flightName);
	
	@Modifying
	@Transactional
	@Query("DELETE from FlightsAvailable fa where fa.flightName = :flightName")
	public int deleteFlight(@Param("flightName") String flightName);
	
	
	@Modifying
	@Transactional
	@Query("UPDATE FlightsAvailable fa SET fa.seatsAvailable = :seat where fa.flightName = :flightName")
	public int updateSeats(@Param("flightName") String flightName,@Param("seat") String seat);
	
	@Query("SELECT fa.seatsAvailable from FlightsAvailable fa where fa.flightName = :flightName")
	List<String> seatsCapacity(@Param("flightName") String flightName);
	
	

}
