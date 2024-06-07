package com.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.entity.UserDetail;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserDetail,Integer>{
   
	@Query("SELECT u FROM UserDetail u WHERE u.registerEmail = :registerEmail")
	List<UserDetail> findByRegisterEmail(@Param("registerEmail") String registerEmail);
	
	
	@Query("SELECT u FROM UserDetail u WHERE u.registerName = :registerName")
    List<UserDetail> findByRegisterName(@Param("registerName") String registerName);
	
	@Modifying
	@Transactional
	@Query("UPDATE UserDetail u SET u.registerPassword = :newPassword WHERE u.registerEmail = :registerEmail")
	int updateByRegisterEmail(@Param("registerEmail") String registerEmail,@Param("newPassword") String newPassword);
	
	@Modifying
	@Transactional
	@Query("UPDATE UserDetail cu SET cu.registerName = :registerName, cu.dob = :dob WHERE cu.registerEmail = :registerEmail")
	int updateDetails(@Param("registerName") String registerName,@Param("registerEmail") String registerEmail,@Param("dob") String dob);
	
	

}
