package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auth.entity.CurrentlyLogged;

@Repository
public interface LoggedInRepository extends JpaRepository<CurrentlyLogged,Integer>{
	
	@Query("select u from CurrentlyLogged u")
	CurrentlyLogged select();

}
