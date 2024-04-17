package com.intern.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.intern.entity.UserDtls;
import com.intern.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository repo;
	@GetMapping("/")
	public String home() {
		return "index";
		
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute UserDtls u,HttpSession session) {
		
		System.out.print(u);
		
		repo.save(u);
		session.setAttribute("message", "User Register Sucessfully...");
		
		return "redirect:/";
	}
}
