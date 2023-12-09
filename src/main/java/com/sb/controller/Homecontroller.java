package com.sb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sb.entity.Users;
import com.sb.repository.UserRepository;
import com.sb.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class Homecontroller {

	@Autowired
	private UserService us;
	@Autowired
	private UserRepository ur;
	
@GetMapping("/")
public String indexpage() {
return "index";	
}
@GetMapping("home")
public String home() {
return "index";	
}
@GetMapping("base")
public String basepage() {
	return "base";
}

@GetMapping("loginpage")
public String loginpage() {
	return "login";
}
@GetMapping("register")
public String registerpage() {
	return "register";
}

@PostMapping("userregister")
public String userregister(@ModelAttribute Users u,HttpSession hs) {
	if(us.existsEmail(u.getEmail())) {
		hs.setAttribute("msg", "Email Allready Existed !");
	}else {
	 Users ru=us.saveUser(u);	
	 if(ru!=null) {
		   hs.setAttribute("msg", "User Registered");
	   }else {
		   hs.setAttribute("msg", "Something went wrong"); 
	   }
	}
	return "redirect:/register";
   }

@GetMapping("loadforgotpassword")
public String loadforgotpassword() {
	return "forgotpassword";
}
@GetMapping("loadresetpassword/{id}")
public String resetpassword(@PathVariable("id")int id,Model m) {
	m.addAttribute("id", id);
	return "resetpassword";
}
@PostMapping("resetpassword")
public String loadresetpassword(@RequestParam("email")String em,@RequestParam("phoneno")String ph,HttpSession hs) {
	Users user=ur.findByEmailAndPhoneno(em, ph);
	if(user!=null) {
		return "redirect:/loadresetpassword/"+user.getId();
	}else {
		hs.setAttribute("msg", "Wrong Email/Phone Number");
		return "redirect:/loadforgotpassword";
	}
	
}
@PostMapping("reset")
public String reset(@RequestParam("id")int id,@RequestParam("newpassword")String np,@RequestParam("confirmpassword")String cp,HttpSession hs) {
	Users user=ur.findById(id).get();
	if(np.equals(cp)) {
		user.setPassword(cp);
		us.saveUser(user);
		hs.setAttribute("msg", "Password Changed");
		return "redirect:/loginpage";
	}else {
		hs.setAttribute("msg", "Enter New Password / Confirm Password Correctly");
		return "redirect:/loadresetpassword/"+id;

	}
}


}
