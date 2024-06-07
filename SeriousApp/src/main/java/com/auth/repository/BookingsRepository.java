package com.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.auth.entity.AdminDetail;
import com.auth.entity.CurrentUserWhoBooked;
import com.auth.entity.PassengersBooked;
import com.auth.entity.UserDetail;


@Repository
public interface BookingsRepository extends JpaRepository<CurrentUserWhoBooked,Integer>{
	
	@Query("SELECT c.id FROM CurrentUserWhoBooked c WHERE c.registerEmail = :userMail")
    List<Integer> findAllIdsByRegisterEmail(@Param("userMail") String userMail);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM CurrentUserWhoBooked p WHERE p.id = :uid")
    int deleteUid(@Param("uid") int uid);
	
	
	@Transactional
	@Modifying
	@Query("UPDATE CurrentUserWhoBooked cu SET cu.registerName = :registerName WHERE cu.registerEmail = :registerEmail")
	int updateEntry(@Param("registerName") String registerName, @Param("registerEmail") String registerEmail);
	
	
	
	@Transactional
	@Modifying
	@Query("DELETE FROM CurrentUserWhoBooked cu WHERE cu.id IN :currentUserIds")
	int deleteByCurrentUserIds(@Param("currentUserIds") List<Integer> currentUserIds);


}
