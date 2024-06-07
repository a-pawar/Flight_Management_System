package com.auth.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.auth.component.EmailByAdmin;
import com.auth.component.EmailHelper;
import com.auth.component.EmailOTPHelper;
import com.auth.component.StoreOTP;
import com.auth.entity.AdminDetail;
import com.auth.entity.CurrentUserWhoBooked;
import com.auth.entity.CurrentlyLogged;
import com.auth.entity.Feeback;
import com.auth.entity.FlightsAvailable;
import com.auth.entity.PassengerDetails;
import com.auth.entity.PassengersBooked;
import com.auth.entity.UserDetail;
import com.auth.repository.AdminRepository;
import com.auth.repository.BookingsRepository;
import com.auth.repository.FeedbackRepository;
import com.auth.repository.LoggedInRepository;
import com.auth.repository.PassengerBookedRepository;
import com.auth.repository.PassengerRepository;
import com.auth.repository.UserRepository;
import com.auth.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private LoggedInRepository logRepo;
	
	@Autowired
	private AdminRepository repoAdmin;
	
	@Autowired
	private PassengerRepository repoPass;
	
	@Autowired
	private BCryptPasswordEncoder bp;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookingsRepository repoBook;
	
	@Autowired
	private PassengerBookedRepository repoBookingsStore;
	
	@Autowired
	private FeedbackRepository feedRepo;
	
	
	
	
	
	@Autowired
    private EmailHelper emailHelper;
	
	@Autowired
	private EmailOTPHelper emailOTPHelper;
	
	@Autowired
	private EmailByAdmin emailByAdmin;
	
	@Autowired
	private StoreOTP otpStorage;
	
	@GetMapping("/index")
	public String f() {
		return "index";
	}
	
	@GetMapping("/profile_validation")
	public String profile_log(RedirectAttributes redirectAttributes,HttpServletRequest request,Model model) {
		
		        // Create CurrentUserWhoBooked object
				CurrentlyLogged hereUser = logRepo.select();
				
				
				if(hereUser == null) {
					 redirectAttributes.addFlashAttribute("stored_email","Not Defined");
				     redirectAttributes.addFlashAttribute("stored_dob","");
				     redirectAttributes.addFlashAttribute("userName","Not Defined");
				     redirectAttributes.addFlashAttribute("message","Please Log In First!");
				     // Get the referer URL
					 String referer = request.getHeader("referer");

					 // Redirect back to the referer URL
					 return "redirect:" + referer;

				}
				
				//now getting all details
			    
			     List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
			     
			     System.out.println(user.get(0).getRegisterEmail());
			     
			     redirectAttributes.addFlashAttribute("stored_email",user.get(0).getRegisterEmail());
			     redirectAttributes.addFlashAttribute("stored_dob",user.get(0).getDob());
			     redirectAttributes.addFlashAttribute("userName",user.get(0).getRegisterName());
				
			       // Get the referer URL
				    String referer = request.getHeader("referer");

				    // Redirect back to the referer URL
				    return "redirect:" + referer;

	}
	
	@GetMapping("/signout")
	public String logOut(
			RedirectAttributes redirectAttributes
			){
		
		// Create CurrentUserWhoBooked object
		CurrentlyLogged hereUser = logRepo.select();
		
		//suppose user is not logged in only then handle this case
		long present = logRepo.count();
		if(present == 0) {
			redirectAttributes.addFlashAttribute("messageLogOut","Please Log In First!");
			return "redirect:/";
		}
		
		//delete this data from database
		logRepo.deleteAll();
		
		redirectAttributes.addFlashAttribute("messageLogOut","Logged Out Successfully!");
		
		return "redirect:/";
		
	}
	
	
	@GetMapping("/adminPage")
	public String adminRedirect() {
		return "adminPage";
	}
	
	
	@GetMapping("/reset_password")
    public String resetPassword() {
        
        return "reset_password";
    }
	
    
	
	
	@GetMapping("/home")
	public String home() {
		return "index";
	}
	
	
	
	@GetMapping("/flight_booking")
    public String showFlightBookingPage() {
        return "flight_booking"; // This will return the flight_booking.html template
    }
	
	@GetMapping("/boarding_pass")
	public String getPass() {
		return "/boarding_pass";
	}
	
	@GetMapping("/admin_things")
    public String showAdminView() {
        return "admin_things"; // This will return the flight_booking.html template
    }
	
	@GetMapping("/admin")
	public String handleAdmin() {
		return "adminPage";
	}
	
	@GetMapping("/passenger_details")
	public String handleDetails() {
		return "passenger_booking";
	}
	
	@GetMapping("/book_2")
	public String handleDetail() {
		return "passenger_booking";
	}
	
	@GetMapping("/seatChoose")
	public String handleSeat() {
		return "seatBook";
	}
	
	@GetMapping("/foodChoose")
	public String handleFood() {
		return "shoppingBook";
	}
	
	
	@PostMapping("/update_details_of_profile")
	public String updating_profile(@RequestParam(name="updated_name") String name,
			@RequestParam(name="updated_dob") String dob,
			RedirectAttributes redirectAttributes,
			 HttpServletRequest request
			
			) 
	{
		CurrentlyLogged hereUser = logRepo.select();
		
		String mail=hereUser.getRegisterEmail();
		//delete this data from database
		logRepo.deleteAll();
		
		//updating currentlyLogged
		logRepo.save(new CurrentlyLogged(name,mail));
		
		//updating currentUserWhoBooked
		
		int res = userService.updateEntry(name, mail);
		
		//updating userDetail
		
		userService.updateDetailsEntry(name, mail,dob);
		
		redirectAttributes.addFlashAttribute("message","Details Updated Successfully");
		
		// Get the referer URL
	    String referer = request.getHeader("referer");

	    // Redirect back to the referer URL
	    return "redirect:" + referer;
		
	}
	
	@PostMapping("/contact_support")
	public String storingFeedback(@RequestParam(name="contact_name") String name,
	        @RequestParam(name="contact_email") String email,
	        @RequestParam(name="contact_phone") String phone,
	        @RequestParam(name="contact_message") String message,
	        HttpServletRequest request, // Inject the HttpServletRequest object
	        RedirectAttributes redirectAttributes) {
	    Feeback feed = new Feeback(name, email, phone, message);
	    feedRepo.save(feed);

	    redirectAttributes.addFlashAttribute("message", "Your Feedback is sent successfully");

	    // Get the referer URL
	    String referer = request.getHeader("referer");

	    // Redirect back to the referer URL
	    return "redirect:" + referer;
	}
	
	
	@PostMapping("/reset_things")
	public String resetting(
			@RequestParam(name="new_password") String new_password,
			@RequestParam(name="confirm_pass") String confirm_password,
			@RequestParam(name="reset_email") String email,
			
			RedirectAttributes redirectAttributes
			) {
		
		
		if(!new_password.equals(confirm_password)) {
			redirectAttributes.addFlashAttribute("message", "Passwords do not match");
            return "redirect:/reset_password"; 
		}
		
		UserDetail user = userService.giveMeAllUsersByEmail(email);
		
		boolean isMatch = bp.matches(new_password, user.getRegisterPassword());
		
		if(isMatch == true) {
			redirectAttributes.addFlashAttribute("message", "Please choose different password which you have not used recently");
			
            return "redirect:/reset_password"; 
		}
		
		
		//reset the password
		
		//just updating the things
		int ans = userService.updatingPassword(email, bp.encode(confirm_password));
		
		System.out.println(ans);
		
		redirectAttributes.addFlashAttribute("message", "Password reset successfully");
        
		return "redirect:/";
		
		
	}
	
	@PostMapping("/otpEmail")
	public String otpEmailValidate(
			@RequestParam(name="hidden-otp-mail-reset") String otp,
			@RequestParam(name="mail-reset") String mail,
			RedirectAttributes redirectAttributes
			) 
	{
		
		
		String hereotp=otpStorage.getOTP(mail);
		
		System.out.println(mail);
		System.out.println(otp);
		System.out.println(hereotp);
		
		if(!otp.equals(hereotp)) {
			redirectAttributes.addFlashAttribute("message", "Please Enter Valid OTP");
            return "redirect:/"; 
		}
		
		redirectAttributes.addFlashAttribute("reset_email", mail);
		
		
		return "redirect:/reset_password";
		
	}
	
	@PostMapping("/reset")
	public String resetPassword(@RequestParam(name="hidden-mail") String registerEmail,
			RedirectAttributes redirectAttributes
			) {
		
		//do it search in all database and find this email
		//if get then => then enter otp
		//if otp valid then redirect to that page
		
		boolean ans = userService.isEmailRegistered(registerEmail);
		
		if(ans==false) {
			redirectAttributes.addFlashAttribute("message","Email is not registered with us");
			return "redirect:/"; 
		}
		
		//enter otp
		redirectAttributes.addFlashAttribute("messagesotp","haaa");
		redirectAttributes.addFlashAttribute("reset_email",registerEmail);
		
		
		
        String genotp = emailOTPHelper.generateOTP();
        
        System.out.println(registerEmail);
	    
	    boolean res = emailOTPHelper.sendEmail(registerEmail,genotp);
	    
	    otpStorage.storeOTP(registerEmail, genotp);
		
		return "redirect:/"; 
	}
	
	
	@PostMapping("/otp")
	public String finalRegister(
			@RequestParam(name="registerName") String registerName,
			@RequestParam(name="registerEmail") String registerEmail,
			@RequestParam(name="registerPassword") String password,
			@RequestParam(name="dob") String dob,
			@RequestParam(name="otp-hidden") String otp,
			
			RedirectAttributes redirectAttributes
			
			) {
		
		String hereotp = otpStorage.getOTP(registerEmail);
		System.out.println(registerEmail);
		System.out.println(otp);
		System.out.println(hereotp);
		
		
		if(!hereotp.equals(otp)) {
			redirectAttributes.addFlashAttribute("message", "Please Enter Valid OTP");
            return "redirect:/"; 
		}
		
		otpStorage.removeOTP(registerEmail);
		
		UserDetail user = new UserDetail();
		user.setRegisterName(registerName);
		user.setRegisterPassword(bp.encode(password)); // Encode the password before saving
		user.setDob(dob);
		user.setRegisterEmail(registerEmail);
		
		repo.save(user);
		
		
		redirectAttributes.addFlashAttribute("message", "User Registered Successfully");
        return "redirect:/"; 
	}
	
	

	@PostMapping("/register")
	public String saveUser(@ModelAttribute UserDetail user,HttpSession session,RedirectAttributes redirectAttributes) {
	    
		
	    
		//check if email is already registered
		if (userService.isEmailRegistered(user.getRegisterEmail())) {
			redirectAttributes.addFlashAttribute("message", "Email is already registered");
            return "redirect:/"; 
        }
		
		// Check if the passwords match
	    if (!user.getRegisterPassword().equals(user.getConfirmPassword())) {
	        // If the passwords don't match, set an error message and return to the registration form
	    	redirectAttributes.addFlashAttribute("message", "Passwords do not match");
	        return "redirect:/"; 
	    }
	    
	    String genotp = emailOTPHelper.generateOTP();
	    
	    boolean res = emailOTPHelper.sendEmail(user.getRegisterEmail(),genotp);
	    
	    otpStorage.storeOTP(user.getRegisterEmail(), genotp);
	    
	    System.out.println(res);
	    
	    

//	    // Save the user if passwords match
//	    user.setRegisterPassword(bp.encode(user.getRegisterPassword())); // Encode the password before saving
//	    repo.save(user);
//	    redirectAttributes.addFlashAttribute("message", "User Registered Successfully");
	    
	    redirectAttributes.addFlashAttribute("registerName",user.getRegisterName());
	    redirectAttributes.addFlashAttribute("registerEmail",user.getRegisterEmail());
	    redirectAttributes.addFlashAttribute("registerPassword",user.getRegisterPassword());
	    redirectAttributes.addFlashAttribute("dob",user.getDob());
	    
	    if(res == true)
	          redirectAttributes.addFlashAttribute("messages","haaa");
	    
	    
	    return "redirect:/";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String loginName, @RequestParam String loginPassword, @RequestParam String action,
			HttpSession session,
			RedirectAttributes redirectAttributes,
			Model model) {
		
		
		if ("user".equals(action)) {
            // Handle login logic
            System.out.println("user button clicked");
            UserDetail result = userService.login(loginName,loginPassword);
    		if(Objects.nonNull(result)) {
    			session.setAttribute("message", "Login successfully....");
    			
    			CurrentlyLogged currentlyLogged = new CurrentlyLogged(result.getRegisterName(),result.getRegisterEmail());
    			
    			logRepo.deleteAll();

    			logRepo.save(currentlyLogged);
    			
    			model.addAttribute("message","Welcome to FlyHighHub.com!!!");  
    			
    			model.addAttribute("userName", loginName);
    			
    			
    			// Create CurrentUserWhoBooked object
    			CurrentlyLogged hereUser = logRepo.select();
    			System.out.print(hereUser);
    			
                // now get the user id of this user by using his mail id currentUserWhoBooked 
			    
			    List<Integer> userIds = userService.findAllCurrentUserId(hereUser.getRegisterEmail());
			    
			    System.out.println(userIds);
			    
			    
			    // now use this userids for fetching all passenger list
			    
			    List<PassengersBooked> allBookingsPassengers = userService.findAllPassengers(userIds);
			    
			    // Group bookings by currentUserWhoBooked.id
			    Map<Integer, List<PassengersBooked>> bookingsByUserId = allBookingsPassengers.stream()
			            .collect(Collectors.groupingBy(passenger -> passenger.getCurrentUserWhoBooked().getId()));

			    System.out.println(bookingsByUserId);
			    
			    model.addAttribute("previousBookings", bookingsByUserId);
			    
			    model.addAttribute("stored_dob",result.getDob());
			    model.addAttribute("stored_email",result.getRegisterEmail());
			    
    			return "flight_booking";
    		}
    		else {
    			
    			redirectAttributes.addFlashAttribute("message","Please Enter valid credentials!!!");
    			//session.setAttribute("message", "Please Enter valid credentials!!!");
    		    return "redirect:/";
    		}
    		
        } else {
            // Handle admin login logic
        	 System.out.println("admin button clicked");
        	 AdminDetail result = userService.adminLogin(loginName,loginPassword);
     		if(Objects.nonNull(result)) {
     			session.setAttribute("message", "Login successfully....");
     			//now getting all details
			     CurrentlyLogged hereUser = logRepo.select();
			     List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
			     
			     model.addAttribute("stored_email",user.get(0).getRegisterEmail());
			     model.addAttribute("stored_dob",user.get(0).getDob());
			     model.addAttribute("userName",user.get(0).getRegisterName());
		
     			return "adminPage";
     		}
     		else {
     			session.setAttribute("message", "Please Enter valid credentials!!!");
     		    return "redirect:/";
     		}
        }
		
		
		
	}
	
	
	
	
	@PostMapping("/cancel_ticket")
	public String cancelTicket(@RequestParam (name="uniqueId") String uid,
			
			RedirectAttributes redirectAttribute,
			Model model) {
		System.out.println("---------------------------");
		System.out.println(uid);
		
		
	    
	    
	    // now use this userids for deleting all passenger list
		
		String flightName = userService.findFlightWithUserId(Integer.parseInt(uid));
		
		
	    
	    int answer = userService.deleteAllPassengers(Integer.parseInt(uid));
	    
	    System.out.println(answer);
	    
	    
	    // now finally delete this uid from currentUserWhoBooked;
	    
	    int deleted = userService.deleteUserBookId(Integer.parseInt(uid));
	    
	    
	    System.out.println(deleted);
	    
	    // Create CurrentUserWhoBooked object
		CurrentlyLogged hereUser = logRepo.select();
		System.out.print(hereUser);
		
        // now get the user id of this user by using his mail id currentUserWhoBooked 
	    
	    List<Integer> userIds = userService.findAllCurrentUserId(hereUser.getRegisterEmail());
	    
	    System.out.println(userIds);
	    
	    
	    // now use this userids for fetching all passenger list
	    
	    List<PassengersBooked> allBookingsPassengers = userService.findAllPassengers(userIds);
	    
	    // Group bookings by currentUserWhoBooked.id
	    Map<Integer, List<PassengersBooked>> bookingsByUserId = allBookingsPassengers.stream()
	            .collect(Collectors.groupingBy(passenger -> passenger.getCurrentUserWhoBooked().getId()));

	    System.out.println(bookingsByUserId);
	    
	    model.addAttribute("isCancelled",true);
	    
	    model.addAttribute("userName",hereUser.getRegisterName());
	    
	    
	    model.addAttribute("previousBookings", bookingsByUserId);
	    
	    //now getting all details
	    
	     List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
	     
	     model.addAttribute("stored_email",user.get(0).getRegisterEmail());
	     model.addAttribute("stored_dob",user.get(0).getDob());

	     model.addAttribute("message","Welcome to FlyHighHub.com!!!");  
	     
	     
	     // Now find all the email ids of the users who had done booking on this flight
	     List<String> emails = new ArrayList<>();
	     emails.add(hereUser.getRegisterEmail());

	     System.out.println("emails found => " + emails);

	     // Notify all recipients about the cancellation of their booking
	     emailByAdmin.sendEmail(emails, "Cancellation of Your Booking for Flight " + flightName,
	             "Hi,<br>We have successfully processed the cancellation of your booking for flight " + flightName + "."
	             + "<br>If you have any questions or need further assistance, please contact our support team."
	             + "<br><br>Thank you for choosing FlyhighHub.com.<br>Best regards,<br>FlyhighHub.com");

			
		
		return "flight_booking";
	}
	
	
	
	@PostMapping("/email")
	public String toEmail(
			@RequestParam(name="userId") String userId, 
			RedirectAttributes redirectAttributes) {
		//sending boarding pass over email for booking
		// Create CurrentUserWhoBooked object
		CurrentlyLogged hereUser = logRepo.select();
	    emailHelper.sendEmail(hereUser.getRegisterEmail(),userId);
	    redirectAttributes.addFlashAttribute("message","Welcome to FlyHighHub.com!!!"); 
	    redirectAttributes.addFlashAttribute("userName",hereUser.getRegisterName());
	    redirectAttributes.addFlashAttribute("msg_email","Boarding Pass Was Sent to Your E-mail Id Successfully!");
	    return "redirect:/";
	}
	
	@PostMapping("/boarding")
	public String tillFoodDetails(
			
			@RequestParam String capturedFlightName,
	        @RequestParam String capturedFlightId,
	        @RequestParam String capturedPrice,
	        @RequestParam String from,
	        @RequestParam String to,
	        @RequestParam String arrivalTime,
	        @RequestParam String departTime,
	        
	        
	        @RequestParam Long travelHours,
	        @RequestParam Long travelDays,
	        @RequestParam String boardingTime,
	        @RequestParam String parsedArrivalDate,
	        @RequestParam String parsedDepartDate,
	        
	        
	        
	        @RequestParam(name = "firstName_1", required = false) String firstName_1,
	        @RequestParam(name = "lastName_1", required = false) String lastName_1,
	        @RequestParam(name = "age_1", required = false) Integer age_1,
	        @RequestParam(name = "nation_1", required = false) String nation_1,

	        @RequestParam(name = "firstName_2", required = false) String firstName_2,
	        @RequestParam(name = "lastName_2", required = false) String lastName_2,
	        @RequestParam(name = "age_2", required = false) Integer age_2,
	        @RequestParam(name = "nation_2", required = false) String nation_2,

	        @RequestParam(name = "firstName_3", required = false) String firstName_3,
	        @RequestParam(name = "lastName_3", required = false) String lastName_3,
	        @RequestParam(name = "age_3", required = false) Integer age_3,
	        @RequestParam(name = "nation_3", required = false) String nation_3,

	        @RequestParam(name = "firstName_4", required = false) String firstName_4,
	        @RequestParam(name = "lastName_4", required = false) String lastName_4,
	        @RequestParam(name = "age_4", required = false) Integer age_4,
	        @RequestParam(name = "nation_4", required = false) String nation_4,
	        
	        @RequestParam(name = "seat1", required = false) String seatIdName1,
	        @RequestParam(name = "seat2", required = false) String seatIdName2,
	        @RequestParam(name = "seat3", required = false) String seatIdName3,
	        @RequestParam(name = "seat4", required = false) String seatIdName4,
	        
	        @RequestParam(name = "seatPrice", required = false) String seatPrice,
	        @RequestParam(name = "totalPrice", required = false) String totalPrice,
	        
	        
	        @RequestParam(name = "foodCost", required = false) String foodCost,
			
	        

	        HttpSession session,
	        Model model,
	        RedirectAttributes redirectAttributes
			) 
	{
		
		//process them accordingly
				System.out.println(capturedFlightName);
				System.out.println(capturedFlightId);
				System.out.println(capturedPrice);
				System.out.println(from);
				System.out.println(to);
				System.out.println(arrivalTime);
				System.out.println(departTime);
				
				System.out.println(travelHours);
			    System.out.println(travelDays);
			    System.out.println(boardingTime);
			    System.out.println(parsedArrivalDate);
			    System.out.println(parsedDepartDate);


			    System.out.println(firstName_1);
			    System.out.println(lastName_1);
			    System.out.println(age_1);
			    System.out.println(nation_1);

			    System.out.println(firstName_2);
			    System.out.println(lastName_2);
			    System.out.println(age_2);
			    System.out.println(nation_2);

			    System.out.println(firstName_3);
			    System.out.println(lastName_3);
			    System.out.println(age_3);
			    System.out.println(nation_3);

			    System.out.println(firstName_4);
			    System.out.println(lastName_4);
			    System.out.println(age_4);
			    System.out.println(nation_4);

			    System.out.println("s1"+seatIdName1);
			    System.out.println("s2"+seatIdName2);
			    System.out.println("s3"+seatIdName3);
			    System.out.println("s4"+seatIdName4);
			    
			    System.out.println("p "+seatPrice);
			    System.out.println("t "+totalPrice);
			    
			    
			    
			  // Add the form data to the model
			    model.addAttribute("capturedFlightName", capturedFlightName);
			    model.addAttribute("capturedFlightId", capturedFlightId);
			    model.addAttribute("capturedPrice", capturedPrice);
			    model.addAttribute("from", from);
			    model.addAttribute("to", to);
			    model.addAttribute("arrivalTime", arrivalTime);
			    model.addAttribute("departTime", departTime);
			    
			    
			    
			    
		    // Create a list to store PassengersBooked objects
		    List<PassengersBooked> bookings = new ArrayList<>();
			
			// Create CurrentUserWhoBooked object
			CurrentUserWhoBooked currentUser = new CurrentUserWhoBooked();
			CurrentlyLogged hereUser = logRepo.select();
			System.out.print(hereUser);
			currentUser.setRegisterName(hereUser.getRegisterName());
			currentUser.setRegisterEmail(hereUser.getRegisterEmail());
			
			repoBook.save(currentUser);
			
			
			int countOfPassengers = 0;
			    

				
			if (firstName_1 != null && lastName_1 != null  && age_1 != null && nation_1!=null) {
		    	
		    	PassengersBooked passenger1 = new PassengersBooked(firstName_1, lastName_1, age_1, nation_1, seatIdName1, 
		    			capturedFlightName,capturedFlightId,parsedArrivalDate,parsedDepartDate,arrivalTime,departTime,from,to,totalPrice,currentUser);
			        bookings.add(passenger1);
			        repoBookingsStore.save(passenger1);
			        
			        model.addAttribute("firstName_1", firstName_1);
			        model.addAttribute("lastName_1", lastName_1);
			        model.addAttribute("age_1", age_1);
			        model.addAttribute("nation_1", nation_1);
			        countOfPassengers++;
			        
			     }
			    
                  if (firstName_2 != null && lastName_2 != null && age_2 != null && nation_2!=null) {
			    	
			    	PassengersBooked passenger1 = new PassengersBooked(firstName_2, lastName_2, age_2, nation_2, seatIdName2,
			    			capturedFlightName,capturedFlightId,parsedArrivalDate,parsedDepartDate,arrivalTime,departTime,from,to,totalPrice,currentUser);

			    	bookings.add(passenger1);
			        repoBookingsStore.save(passenger1);
			        
			    	model.addAttribute("firstName_2", firstName_2);
			        model.addAttribute("lastName_2", lastName_2);
			        model.addAttribute("age_2", age_2);
			        model.addAttribute("nation_2", nation_2);
			        countOfPassengers++;
			    }
			    
                  if (firstName_3 != null && lastName_3 != null  && age_3 != null && nation_3!=null ) {
  			    	
  			    	PassengersBooked passenger1 = new PassengersBooked(firstName_3, lastName_3, age_3, nation_3,seatIdName3, 
  			    			capturedFlightName,capturedFlightId,parsedArrivalDate,parsedDepartDate,arrivalTime,departTime,from,to,totalPrice,currentUser);
  			        bookings.add(passenger1);
  			        repoBookingsStore.save(passenger1);
			        
			        model.addAttribute("firstName_3", firstName_3);
			        model.addAttribute("lastName_3", lastName_3);
			        model.addAttribute("age_3", age_3);
			        model.addAttribute("nation_3", nation_3);
			        countOfPassengers++;
			        
			    }
			    
                  if (firstName_4 != null && lastName_4 != null  && age_4 != null && nation_4!=null) {
  			    	
  			    	PassengersBooked passenger1 = new PassengersBooked(firstName_4, lastName_4, age_4, nation_4, seatIdName4,
  			    			capturedFlightName,capturedFlightId,parsedArrivalDate,parsedDepartDate,arrivalTime,departTime,from,to,totalPrice,currentUser);
  			        bookings.add(passenger1);
  			        repoBookingsStore.save(passenger1);
			        
			        model.addAttribute("firstName_4", firstName_4);
			        model.addAttribute("lastName_4", lastName_4);
			        model.addAttribute("age_4", age_4);
			        model.addAttribute("nation_4", nation_4);
			        countOfPassengers++;
			        
			    }
			    
		        model.addAttribute("travelHours", travelHours);
			    
			    model.addAttribute("travelDays", travelDays);
			    
		        model.addAttribute("boardingTime",boardingTime);
		        
		        
		        model.addAttribute("parsedArrivalDate",parsedArrivalDate);
		        
		        model.addAttribute("parsedDepartDate",parsedDepartDate);
		        
		        
		        model.addAttribute("seatIdName1",seatIdName1);
		        model.addAttribute("seatIdName2",seatIdName2);
		        model.addAttribute("seatIdName3",seatIdName3);
		        model.addAttribute("seatIdName4",seatIdName4);
		        
		        
		        
		        
		        model.addAttribute("seatPrice", seatPrice);
		        
		        
		        model.addAttribute("totalPrice", totalPrice);
		        
		        
		        
               //updating seats
			    
			    String fullFlight =  capturedFlightId + " " + capturedFlightName ;
			    
			    boolean found = userService.isFlightNameFound(fullFlight);
			    
			    
			    
			    System.out.println(fullFlight);
			    System.out.println(" ---------------------> "+found);
			    
                System.out.println(countOfPassengers);
			    
			    //finding the original seat capacity left
			    
			    String originalSeats = userService.seats(fullFlight);
			    
			    int oc = Integer.parseInt(originalSeats);
			    
			    int ac = oc - countOfPassengers;
			    
			    if(ac < 0) {
			    	model.addAttribute("message","Maximum Capacity of Flight is Reached!");
			    	return "flight_booking";
			    }
			    
			    String updatedSeats = String.valueOf(ac);
			    
			    //reducing the seats by countOfPassengers in table flightAvailable
			    
			    int ans = userService.updateSeats(fullFlight, updatedSeats);
			    
			    
			    
		        
		        model.addAttribute("passengerForms", bookings);
		
		        // Set the bookings list to the currentUser object
			    currentUser.setBookings(bookings);
			    
			    
			    
			    // now get the user id of this user by using his mail id currentUserWhoBooked 
			    
			    List<Integer> userIds = userService.findAllCurrentUserId(hereUser.getRegisterEmail());
			    
			    System.out.println(userIds);
			    
			    
			    // now use this userids for fetching all passenger list
			    
			    List<PassengersBooked> allBookingsPassengers = userService.findAllPassengers(userIds);
			    
			    
			    // Group bookings by currentUserWhoBooked.id
			    Map<Integer, List<PassengersBooked>> bookingsByUserId = allBookingsPassengers.stream()
			            .collect(Collectors.groupingBy(passenger -> passenger.getCurrentUserWhoBooked().getId()));

			    
			    
			    
			    
			    model.addAttribute("previousBookings", allBookingsPassengers);
			    
			    System.out.println(countOfPassengers);
			    
			    
			    //now getting all details
			    
			     List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
			     
			     model.addAttribute("stored_email",user.get(0).getRegisterEmail());
			     model.addAttribute("stored_dob",user.get(0).getDob());
			     model.addAttribute("userName",user.get(0).getRegisterName());
                 
			     model.addAttribute("userId",currentUser.getId());
		
		
		return "boarding_pass"; 
		
	}

	
	
	@PostMapping("/food")
	public String tillSeatDetails(
			@RequestParam String capturedFlightName,
	        @RequestParam String capturedFlightId,
	        @RequestParam String capturedPrice,
	        @RequestParam String from,
	        @RequestParam String to,
	        @RequestParam String arrivalTime,
	        @RequestParam String departTime,
	        
	        
	        @RequestParam Long travelHours,
	        @RequestParam Long travelDays,
	        @RequestParam String boardingTime,
	        @RequestParam String parsedArrivalDate,
	        @RequestParam String parsedDepartDate,
	        
	        
	        
	        @RequestParam(name = "firstName_1", required = false) String firstName_1,
	        @RequestParam(name = "lastName_1", required = false) String lastName_1,
	        @RequestParam(name = "age_1", required = false) Integer age_1,
	        @RequestParam(name = "nation_1", required = false) String nation_1,

	        @RequestParam(name = "firstName_2", required = false) String firstName_2,
	        @RequestParam(name = "lastName_2", required = false) String lastName_2,
	        @RequestParam(name = "age_2", required = false) Integer age_2,
	        @RequestParam(name = "nation_2", required = false) String nation_2,

	        @RequestParam(name = "firstName_3", required = false) String firstName_3,
	        @RequestParam(name = "lastName_3", required = false) String lastName_3,
	        @RequestParam(name = "age_3", required = false) Integer age_3,
	        @RequestParam(name = "nation_3", required = false) String nation_3,

	        @RequestParam(name = "firstName_4", required = false) String firstName_4,
	        @RequestParam(name = "lastName_4", required = false) String lastName_4,
	        @RequestParam(name = "age_4", required = false) Integer age_4,
	        @RequestParam(name = "nation_4", required = false) String nation_4,
	        
	        @RequestParam(name = "seat1", required = false) String seatIdName1,
	        @RequestParam(name = "seat2", required = false) String seatIdName2,
	        @RequestParam(name = "seat3", required = false) String seatIdName3,
	        @RequestParam(name = "seat4", required = false) String seatIdName4,
	        
	        @RequestParam(name = "seatPrice1", required = false) Long seatPrice1,
	        @RequestParam(name = "seatPrice2", required = false) Long seatPrice2,
	        @RequestParam(name = "seatPrice3", required = false) Long seatPrice3,
	        @RequestParam(name = "seatPrice4", required = false) Long seatPrice4,
	        

	        HttpSession session,
	        Model model,
	        RedirectAttributes redirectAttributes
			) 
	{
		
		//process them accordingly
		System.out.println("ppppp");
		System.out.println(capturedFlightName);
		System.out.println(capturedFlightId);
		System.out.println(capturedPrice);
		System.out.println(from);
		System.out.println(to);
		System.out.println(arrivalTime);
		System.out.println(departTime);
		
		System.out.println(travelHours);
	    System.out.println(travelDays);
	    System.out.println(boardingTime);
	    System.out.println(parsedArrivalDate);
	    System.out.println(parsedDepartDate);


	    System.out.println(firstName_1);
	    System.out.println(lastName_1);
	    System.out.println(age_1);
	    System.out.println(nation_1);

	    System.out.println(firstName_2);
	    System.out.println(lastName_2);
	    System.out.println(age_2);
	    System.out.println(nation_2);

	    System.out.println(firstName_3);
	    System.out.println(lastName_3);
	    System.out.println(age_3);
	    System.out.println(nation_3);

	    System.out.println(firstName_4);
	    System.out.println(lastName_4);
	    System.out.println(age_4);
	    System.out.println(nation_4);

	    System.out.println("s1"+seatIdName1);
	    System.out.println("s2"+seatIdName2);
	    System.out.println("s3"+seatIdName3);
	    System.out.println("s4"+seatIdName4);
	    
	    System.out.println("ps1"+seatPrice1);
	    System.out.println("ps2"+seatPrice2);
	    System.out.println("ps3"+seatPrice3);
	    System.out.println("ps4"+seatPrice4);
	    
	    
	  // Add the form data to the model
	    model.addAttribute("capturedFlightName", capturedFlightName);
	    model.addAttribute("capturedFlightId", capturedFlightId);
	    model.addAttribute("capturedPrice", capturedPrice);
	    model.addAttribute("from", from);
	    model.addAttribute("to", to);
	    model.addAttribute("arrivalTime", arrivalTime);
	    model.addAttribute("departTime", departTime);
	    

		
	    if (firstName_1 != null && lastName_1 != null  && age_1 != null && nation_1!=null) {
	        model.addAttribute("firstName_1", firstName_1);
	        model.addAttribute("lastName_1", lastName_1);
	        model.addAttribute("age_1", age_1);
	        model.addAttribute("nation_1", nation_1);
	        
	     }
	    
	    if (firstName_2 != null && lastName_2 != null && age_2 != null && nation_2!=null) {
	    	model.addAttribute("firstName_2", firstName_2);
	        model.addAttribute("lastName_2", lastName_2);
	        model.addAttribute("age_2", age_2);
	        model.addAttribute("nation_2", nation_2);
	    }
	    
	    if (firstName_3 != null && lastName_3 != null  && age_3 != null && nation_3!=null ) {
	        model.addAttribute("firstName_3", firstName_3);
	        model.addAttribute("lastName_3", lastName_3);
	        model.addAttribute("age_3", age_3);
	        model.addAttribute("nation_3", nation_3);
	        
	    }
	    
	    if (firstName_4 != null && lastName_4 != null  && age_4 != null && nation_4!=null) {
	        model.addAttribute("firstName_4", firstName_4);
	        model.addAttribute("lastName_4", lastName_4);
	        model.addAttribute("age_4", age_4);
	        model.addAttribute("nation_4", nation_4);
	        
	    }
	    
        model.addAttribute("travelHours", travelHours);
	    
	    model.addAttribute("travelDays", travelDays);
	    
        model.addAttribute("boardingTime",boardingTime);
        
        
        model.addAttribute("parsedArrivalDate",parsedArrivalDate);
        
        model.addAttribute("parsedDepartDate",parsedDepartDate);
        
        
        model.addAttribute("seatIdName1",seatIdName1);
        model.addAttribute("seatIdName2",seatIdName2);
        model.addAttribute("seatIdName3",seatIdName3);
        model.addAttribute("seatIdName4",seatIdName4);
        
        long ans = 0;
        
        if(seatPrice1 != null) {
        	ans += seatPrice1; 
        }
        
        if(seatPrice2 != null) {
        	ans += seatPrice2; 
        }
        
        if(seatPrice3 != null) {
        	ans += seatPrice3; 
        }
        
        if(seatPrice4 != null) {
        	ans += seatPrice4; 
        }
        
        
        String seatPrice = String.valueOf(ans);
        model.addAttribute("seatPrice", seatPrice);
        
        long res = Long.valueOf(capturedPrice) + ans;
        String totalPrice = String.valueOf(res);
        model.addAttribute("totalPrice", totalPrice);
        
     // Create CurrentUserWhoBooked object
     CurrentlyLogged hereUser = logRepo.select();
     System.out.print(hereUser);
     model.addAttribute("userName",hereUser.getRegisterName());
        
   //now getting all details
    
     List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
     
     model.addAttribute("stored_email",user.get(0).getRegisterEmail());
     model.addAttribute("stored_dob",user.get(0).getDob());
   
		
		
		
		return "shoppingBook";
		
	}
	
	
	
	@PostMapping("/passenger")
	public String passengerDetail(
	        @RequestParam String capturedFlightName,
	        @RequestParam String capturedPrice,
	        @RequestParam String capturedArrival,
	        @RequestParam String capturedDeparture,
	        @RequestParam String capturedSeatsAvailable,
	        
	        @RequestParam(name = "firstName_1", required = false) String firstName_1,
	        @RequestParam(name = "lastName_1", required = false) String lastName_1,
	        @RequestParam(name = "age_1", required = false) Integer age_1,
	        @RequestParam(name = "nation_1", required = false) String nation_1,

	        @RequestParam(name = "firstName_2", required = false) String firstName_2,
	        @RequestParam(name = "lastName_2", required = false) String lastName_2,
	        @RequestParam(name = "age_2", required = false) Integer age_2,
	        @RequestParam(name = "nation_2", required = false) String nation_2,

	        @RequestParam(name = "firstName_3", required = false) String firstName_3,
	        @RequestParam(name = "lastName_3", required = false) String lastName_3,
	        @RequestParam(name = "age_3", required = false) Integer age_3,
	        @RequestParam(name = "nation_3", required = false) String nation_3,

	        @RequestParam(name = "firstName_4", required = false) String firstName_4,
	        @RequestParam(name = "lastName_4", required = false) String lastName_4,
	        @RequestParam(name = "age_4", required = false) Integer age_4,
	        @RequestParam(name = "nation_4", required = false) String nation_4,

	        HttpSession session,
	        Model model,
	        RedirectAttributes redirectAttributes) {
		
		
		System.out.println("haaaaaaaaaaaa");
	    
	    System.out.println(capturedFlightName);
	    System.out.println(capturedPrice);
	    System.out.println(capturedArrival);
	    System.out.println(capturedDeparture);
	    System.out.println(capturedSeatsAvailable);
	    
	    // Split the flight name to capture flight ID and name
	    String[] f = capturedFlightName.split(" ");
	    String flightId = f[0];
	    String flightName = f[1];

	    // Split the arrival and departure details
	    String[] arrivalArr = capturedArrival.split(" ");
	    String from = arrivalArr[0];
	    String arrivalDate = arrivalArr[1];
	    String arrivalTime = arrivalArr[2];

	    String[] departArr = capturedDeparture.split(" ");
	    String to = departArr[0];
	    String departDate = departArr[1];
	    String departTime = departArr[2];

	    // Add the form data to the model
	    model.addAttribute("capturedFlightName", flightName);
	    model.addAttribute("capturedFlightId", flightId);
	    model.addAttribute("capturedPrice", capturedPrice);
	    model.addAttribute("from", from);
	    model.addAttribute("to", to);
	    model.addAttribute("arrivalTime", arrivalTime);
	    model.addAttribute("departTime", departTime);
	    model.addAttribute("arrivalDate", arrivalDate);
	    model.addAttribute("departDate", departDate);
	    
	    
	    
	    

	    
	    
	    
	    if (firstName_1 != null && lastName_1 != null  && age_1 != null && nation_1!=null) {
	        model.addAttribute("firstName_1", firstName_1);
	        model.addAttribute("lastName_1", lastName_1);
	        model.addAttribute("age_1", age_1);
	        model.addAttribute("nation_1", nation_1);
	        
	        System.out.println("--------------");
	        System.out.println(firstName_1);
	        System.out.println(lastName_1);
	        System.out.println(age_1);
	        System.out.println(nation_1);
	        
	        
	    }
	    
	    if (firstName_2 != null && lastName_2 != null && age_2 != null && nation_2!=null) {
	    	model.addAttribute("firstName_2", firstName_2);
	        model.addAttribute("lastName_2", lastName_2);
	        model.addAttribute("age_2", age_2);
	        model.addAttribute("nation_2", nation_2);
	        
	        System.out.println("--------------");
	        System.out.println(firstName_2);
	        System.out.println(lastName_2);
	        System.out.println(age_2);
	        System.out.println(nation_2);
	    }
	    
	    if (firstName_3 != null && lastName_3 != null  && age_3 != null && nation_3!=null ) {
	        model.addAttribute("firstName_3", firstName_3);
	        model.addAttribute("lastName_3", lastName_3);
	        model.addAttribute("age_3", age_3);
	        model.addAttribute("nation_3", nation_3);
	        
	        System.out.println("--------------");
	        System.out.println(firstName_3);
	        System.out.println(lastName_3);
	        System.out.println(age_3);
	        System.out.println(nation_3);
	    }
	    
	    if (firstName_4 != null && lastName_4 != null  && age_4 != null && nation_4!=null) {
	        model.addAttribute("firstName_4", firstName_4);
	        model.addAttribute("lastName_4", lastName_4);
	        model.addAttribute("age_4", age_4);
	        model.addAttribute("nation_4", nation_4);
	        
	        System.out.println("--------------");
	        System.out.println(firstName_4);
	        System.out.println(lastName_4);
	        System.out.println(age_4);
	        System.out.println(nation_4);
	    }
	    
	    // Inside your controller method
	    String[] departDateParts = departDate.split("-");
	    String[] arrivalDateParts = arrivalDate.split("-");

	    int departYear = Integer.parseInt(departDateParts[0]);
	    int departMonth = Integer.parseInt(departDateParts[1]);
	    int departDay = Integer.parseInt(departDateParts[2]);

	    int arrivalYear = Integer.parseInt(arrivalDateParts[0]);
	    int arrivalMonth = Integer.parseInt(arrivalDateParts[1]);
	    int arrivalDay = Integer.parseInt(arrivalDateParts[2]);

	    // Parse the dates
	    LocalDate startDate = LocalDate.of(arrivalYear, arrivalMonth, arrivalDay);
	    LocalDate endDate = LocalDate.of(departYear, departMonth, departDay);

	    // Calculate the difference
	    long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
         
	    //Parsing the time
	    String arrTime[] = arrivalTime.split(":");
	    String depTime[] = departTime.split(":");
	    
	    int ah = Integer.parseInt(arrTime[0]);
	    int am = Integer.parseInt(arrTime[1]);
	    
	    int dh = Integer.parseInt(depTime[0]);
	    int dm = Integer.parseInt(depTime[1]);
	    
	    // Parse the dates
	    LocalDateTime startDateTime = LocalDateTime.of(arrivalYear, arrivalMonth, arrivalDay,ah, am); // Assuming midnight
	    LocalDateTime endDateTime = LocalDateTime.of(departYear, departMonth, departDay, dh, dm); // Assuming midnight

	    // Calculate the difference
	    long hoursDifference = ChronoUnit.HOURS.between(startDateTime, endDateTime);
	    
	 // Calculate the remaining hours after accounting for full days
        long remainingHours = hoursDifference - (daysDifference * 24);
	    
	    model.addAttribute("travelHours", remainingHours);
	    
	    model.addAttribute("travelDays", daysDifference);
	    
	    
	    
	    
	    // Parse the arrival time for boarding time
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime parsedArrivalTime;
        try {
            parsedArrivalTime = LocalTime.parse(arrivalTime, timeFormatter);
        } catch (DateTimeParseException e) {
            // Handle parse exception (e.g., log error, set default time, etc.)
            parsedArrivalTime = LocalTime.of(0, 0);
        }

        // Add 20 minutes to the parsed arrival time
        LocalTime boardingTime = parsedArrivalTime.plusMinutes(20);

        // Format the boarding time back to string
        String boardingTimeString = boardingTime.format(timeFormatter);
        
        
        model.addAttribute("boardingTime",boardingTimeString);
        
        
        //Parsing the Arrival Date for converting in format of 9 Jun
        
        LocalDate date = LocalDate.parse(arrivalDate);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        String formattedDate = date.format(formatter);
        model.addAttribute("parsedArrivalDate",formattedDate);
        
        LocalDate date2 = LocalDate.parse(departDate);
        String formattedDate2 = date2.format(formatter);
        model.addAttribute("parsedDepartDate",formattedDate2);
        
        //ends here

//	    // Set the bookings list to the currentUser object
//	    currentUser.setBookings(bookings);

	    //return "boarding_pass";
        
      //now getting all details
        CurrentlyLogged hereUser = logRepo.select();
        
        List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
        
        model.addAttribute("userName",user.get(0).getRegisterName());
        model.addAttribute("stored_email",user.get(0).getRegisterEmail());
        model.addAttribute("stored_dob",user.get(0).getDob());
	    return "seatBook";
	}

	
	@PostMapping("/book_2")
	public String cap(@RequestParam String flightName, 
            @RequestParam String price, 
            @RequestParam String arrival, 
            @RequestParam String departure, 
            @RequestParam String seatsAvailable, 
            Model model) {
		
		System.out.println(flightName);
		System.out.println(price);
		System.out.println(arrival);
		System.out.println(departure);
		System.out.println(seatsAvailable);
		
		model.addAttribute("capturedFlightName", flightName);
        model.addAttribute("capturedPrice", price);
        model.addAttribute("capturedArrival", arrival);
        model.addAttribute("capturedDeparture", departure);
        model.addAttribute("capturedSeatsAvailable", seatsAvailable);
        
        
        //now getting all details
        CurrentlyLogged hereUser = logRepo.select();
        
        List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
        
        model.addAttribute("userName",user.get(0).getRegisterName());
        model.addAttribute("stored_email",user.get(0).getRegisterEmail());
        model.addAttribute("stored_dob",user.get(0).getDob());
		
		return "passenger_booking";
		
	}
	@PostMapping("/book")
	public String book(@RequestParam String location, @RequestParam String Destlocation, @RequestParam String travel,
			@RequestParam String departure,
			Model model, HttpSession session) {
		System.out.println("Location: " + location);
        System.out.println("Destination Location: " + Destlocation);
        System.out.println("Number of Travelers: " + travel);
        System.out.println("Departure Date: " + departure);
        
        
        

       
        
        
        // fetch from database of admin like perform query operation first
        
        
        int seats_needed=Integer.parseInt(travel);
        List<FlightsAvailable> flights = userService.fetch(location, Destlocation, departure, seats_needed);
        System.out.println(flights);
        
        
        model.addAttribute("flights", flights);
        
        //now getting all details
        CurrentlyLogged hereUser = logRepo.select();
        
        List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
        
        model.addAttribute("userName",user.get(0).getRegisterName());
        model.addAttribute("stored_email",user.get(0).getRegisterEmail());
        model.addAttribute("stored_dob",user.get(0).getDob());
        
        
        
        
		return "flight_booking";
		
	}
	
	@PostMapping("/admin_view_delete_indirectly")
	public String deleteAdminViewIndirect(@RequestParam(name = "flightName") String flightName,
			@RequestParam(name = "arrival") String arrival,
			@RequestParam(name = "departure") String departure,
			Model model,
			RedirectAttributes redirectAttributes) {
		
		
		
		
		
		//now delete that flight with given name
		
		int ans=userService.deleteFlight(flightName);
		
		System.out.println(flightName);
		System.out.println(arrival);
		System.out.println(departure);
		
		
		List<FlightsAvailable> flights = userService.isViewAvailable(arrival, departure);
        System.out.println(flights);
        System.out.println("haaaaaaaa");
        
        
        model.addAttribute("Viewflights", flights);
        model.addAttribute("arrival", arrival);
        
        model.addAttribute("departure", departure);
        
       
		
		model.addAttribute("message","Flight Is Removed Successfully!");
		
		//now getting all details
        CurrentlyLogged hereUser = logRepo.select();
        
        List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
        
        model.addAttribute("userName",user.get(0).getRegisterName());
        model.addAttribute("stored_email",user.get(0).getRegisterEmail());
        model.addAttribute("stored_dob",user.get(0).getDob());
		
		
		return "admin_things";
	}
	
	@PostMapping("/admin_search_delete_indirectly")
	public String deleteAdminSearchIndirect(@RequestParam(name = "flightName") String flightName,
			@RequestParam(name = "arrival") String arrival,
			@RequestParam(name = "departure") String departure,
			@RequestParam(name = "departure_date") String departure_date,
			Model model,
			RedirectAttributes redirectAttributes) {
		
		
		
		
		
		//now delete that flight with given name
		
		int ans=userService.deleteFlight(flightName);
		
		// i need to delete in user profile also about if that user has done ticket in this flight then that ticket gone
		
        // now use this userids for deleting all passenger list
	    
	    //before deleting from passengers return all userIds 
		
		List<Integer> userIdsWhoBooked = userService.findAllUserIdsSpecificToFlight(flightName);
		
		System.out.println("UserIDsBooked  ---> " + userIdsWhoBooked);
		
	    
	    int res = userService.deleteAllPassengersSpecificToFlight(flightName);
	    
	    System.out.println("deleteAllPassengersSpecificToFlight  ---> " + res);
	    
	    //now delete all currentUsersWhoBooked that flight
	    
	    int deletedUsers = userService.deletedBookingCurrentUserIds(userIdsWhoBooked);
	    
	    System.out.println("deletedBookingCurrentUserIds -----> " + deletedUsers);
	    
	    
	    System.out.println(flightName);
		
		
		System.out.println(" ---------------suyash--------------");
		System.out.println(arrival);
		System.out.println(departure);
		System.out.println(departure_date);
		
		model.addAttribute("message","Flight Is Removed Successfully!");
		
		List<FlightsAvailable> flights = userService.isSearchAvailable(arrival, departure,departure_date);
        System.out.println(flights);
        
        
        model.addAttribute("Searchflights", flights);
        model.addAttribute("Viewflights", null);
        
        model.addAttribute("arrival", arrival); 
        
        model.addAttribute("departure", departure);
       
        model.addAttribute("departure_date", departure_date);
        
      //now getting all details
        CurrentlyLogged hereUser = logRepo.select();
        
        List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
        
        model.addAttribute("userName",user.get(0).getRegisterName());
        model.addAttribute("stored_email",user.get(0).getRegisterEmail());
        model.addAttribute("stored_dob",user.get(0).getDob());
        
        // now find all the email ids of the users who had done booking on this flight
    	List<String> emails = userService.findAllEmailIdsWithThisFlight(flightName.split(" ")[1]);
    	
    	System.out.println("emails found => "+emails);
    	
    	// Notify all recipients about the deletion of the flight
    	emailByAdmin.sendEmail(emails, "Deletion of Flight " + flightName,
    	        "Hi,<br>We regret to inform you that the flight " + flightName + " has been deleted from our schedule. We apologize for any inconvenience this may cause."
    	        + "<br>If you have any questions or need further assistance, please contact our support team."
    	        + "<br><br>Thank you for your understanding.<br>Best regards,<br>FlyhighHub.com");
		
		
		return "admin_things";
	}

	
	@PostMapping("/admin_delete")
	public String deleteAdmin(@RequestParam(name = "flightName") String flightName,
			RedirectAttributes redirectAttributes) {
		
		
		
		//search in database of adminFlight
		
		boolean isFlightFound = userService.isFlightNameFound(flightName);
		
		if(isFlightFound == false) {
			System.out.println(flightName);
			redirectAttributes.addFlashAttribute("message","No Flight Found With Given Flight Name");
			return "redirect:/adminPage";
		}
		
		//now delete that flight with given name
		
		int ans=userService.deleteFlight(flightName);
		
		
        // i need to delete in user profile also about if that user has done ticket in this flight then that ticket gone
		
        // now use this userids for deleting all passenger list
	    
	    //before deleting from passengers return all userIds 
		
		List<Integer> userIdsWhoBooked = userService.findAllUserIdsSpecificToFlight(flightName);
		
		System.out.println("UserIDsBooked  ---> " + userIdsWhoBooked);
		
	    
	    int res = userService.deleteAllPassengersSpecificToFlight(flightName);
	    
	    System.out.println("deleteAllPassengersSpecificToFlight  ---> " + res);
	    
	    //now delete all currentUsersWhoBooked that flight
	    
	    int deletedUsers = userService.deletedBookingCurrentUserIds(userIdsWhoBooked);
	    
	    System.out.println("deletedBookingCurrentUserIds -----> " + deletedUsers);
	    
	    
	    System.out.println(flightName);
		
		System.out.println(ans);
		
		redirectAttributes.addFlashAttribute("message","Flight Is Removed Successfully!");
		
		// now find all the email ids of the users who had done booking on this flight
    	List<String> emails = userService.findAllEmailIdsWithThisFlight(flightName.split(" ")[1]);
    	
    	System.out.println("emails found => "+emails);
    	
    	// Notify all recipients about the deletion of the flight
    	emailByAdmin.sendEmail(emails, "Deletion of Flight " + flightName,
    	        "Hi,<br>We regret to inform you that the flight " + flightName + " has been deleted from our schedule. We apologize for any inconvenience this may cause."
    	        + "<br>If you have any questions or need further assistance, please contact our support team."
    	        + "<br><br>Thank you for your understanding.<br>Best regards,<br>FlyhighHub.com");
		
		return "redirect:/adminPage";
	}
	
	@PostMapping("/admin_add")
	public String saveAdmin(@ModelAttribute FlightsAvailable admin, RedirectAttributes redirectAttributes,
			HttpSession session) {
		
		//finding is already exist
		
		boolean ans = userService.isFlightNameFound(admin.getFlightName());
		
		if(ans) {
			redirectAttributes.addFlashAttribute("message","Flight Already Exist!");
			return "redirect:/adminPage";
		}
		repoAdmin.save(admin);
		redirectAttributes.addFlashAttribute("message","Changes Done Successfully");
		return "redirect:/adminPage";
		
		
	}
	
	@PostMapping("/admin_update")
	public String saveAdminUpdate(@RequestParam String flightName, @RequestParam String arrival, @RequestParam String departure,
			@RequestParam String seatsAvailable, @RequestParam String costPrice ,
			RedirectAttributes redirectAttributes,HttpSession session) {
		
        int cost = Integer.parseInt(costPrice);
        boolean result=userService.isUpdateDone(flightName, arrival, departure, seatsAvailable, cost);
        
        
        
        
        if(result==false) {
        	redirectAttributes.addFlashAttribute("message","Error in updating the flight details");
        	return "redirect:/adminPage";
        }
        else {
        	
        	// now find all the email ids of the users who had done booking on this flight
        	List<String> emails = userService.findAllEmailIdsWithThisFlight(flightName.split(" ")[1]);
        	
        	System.out.println("emails found => "+emails);
        	
        	// send all of them an email of updating location
        	emailByAdmin.sendEmail(emails, "Updation in Schedule of Flight " + flightName,
                    "Hi,<br>We have updated the schedule of the flight. Sorry for the inconvenience. Boarding pass will be sent soon."
                    		+ "<br>If you have any questions or need further assistance, please contact our support team."
                    		+ "<br><br>Thank you for your understanding.<br>Best regards,<br>FlyhighHub.com");
        	
        	redirectAttributes.addFlashAttribute("message","Updated Successfully");
        	return "redirect:/adminPage";
        }
		
		
		
	}
	
	
	
	
	//list all flights from arrival to destination
	@PostMapping("/admin_view")
	public String saveAdminView(@RequestParam String arrival, @RequestParam String departure, Model model,
			HttpSession session) {
		
		List<FlightsAvailable> flights = userService.isViewAvailable(arrival, departure);
        System.out.println(flights);
        System.out.println("haaaaaaaa");
        
        
       // model.addAttribute("Viewflights", flights);
        model.addAttribute("arrival", arrival);
        model.addAttribute("departure", departure);
      //now getting all details
        CurrentlyLogged hereUser = logRepo.select();
        
        List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
        
        model.addAttribute("userName",user.get(0).getRegisterName());
        model.addAttribute("stored_email",user.get(0).getRegisterEmail());
        model.addAttribute("stored_dob",user.get(0).getDob());
		
     // 
        // now get the user id of this user by using his mail id currentUserWhoBooked 
	    
	    List<Integer> userIds = userService.findAllCurrentUserId(hereUser.getRegisterEmail());
	    
	    System.out.println(userIds);
	    
     // now use this userids for fetching all passenger list
	    
	    List<PassengersBooked> allBookingsPassengers = userService.findAllPassengersBookings();
	    
	    // Group bookings by currentUserWhoBooked.id
	    Map<Integer, List<PassengersBooked>> bookingsByUserId = allBookingsPassengers.stream()
	            .collect(Collectors.groupingBy(passenger -> passenger.getCurrentUserWhoBooked().getId()));

	    System.out.println(bookingsByUserId);
	    
	    model.addAttribute("previousBookings", bookingsByUserId);
	    
		return "admin_things";
		
		
	}
	
	@PostMapping("/admin_search")
	public String saveAdminSearch(@RequestParam String arrival, @RequestParam String departure,@RequestParam String departure_date, Model model, HttpSession session) {
		
		
		List<FlightsAvailable> flights = userService.isSearchAvailable(arrival, departure,departure_date);
        System.out.println(flights);
        System.out.println("naaaaaaaa");
        
        
        model.addAttribute("Searchflights", flights);
        model.addAttribute("arrival", arrival);
        model.addAttribute("departure", departure);
        model.addAttribute("departure_date", departure_date);
      //now getting all details
        CurrentlyLogged hereUser = logRepo.select();
        
        List<UserDetail> user = repo.findByRegisterEmail(hereUser.getRegisterEmail());
        
        model.addAttribute("userName",user.get(0).getRegisterName());
        model.addAttribute("stored_email",user.get(0).getRegisterEmail());
        model.addAttribute("stored_dob",user.get(0).getDob());
		
		return "admin_things";
		
		
	}
	
	
}
