package com.intern.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intern.entity.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls,Integer>{

}
