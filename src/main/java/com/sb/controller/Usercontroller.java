package com.sb.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sb.entity.Note;
import com.sb.entity.Users;
import com.sb.repository.NoteRepository;
import com.sb.repository.UserRepository;
import com.sb.service.NoteService;
import com.sb.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class Usercontroller {
	@Autowired
	private UserRepository ur;
	@Autowired
	private UserService us;
	@Autowired
	private NoteService ns;
	@Autowired
	private NoteRepository nr;
	@Autowired
	private BCryptPasswordEncoder bpe;
	@ModelAttribute
	public Users logeedUser(Principal p,Model m) {
		String email=p.getName();
		Users us=ur.findByEmail(email);
		m.addAttribute("loguser", us);
		return us;
	}

	@GetMapping("home")
	public String home() {
	return "index";	
	}
	@GetMapping("add")
	public String addpage() {
		return "add";
	}
	@GetMapping("edit/{id}")
	public String editpage(@PathVariable("id") int id,Model m) {
		Note note=ns.getNotesById(id);
		m.addAttribute("upnote", note);
		return "edit";
	}
	@PostMapping("editnote")
	public String editnote(@ModelAttribute Note un,Principal p,Model m,HttpSession hs) {
		un.setDate(LocalDate.now());
		Users u=logeedUser(p, m);
		un.setUser(u);
		Note n= ns.saveNote(un);
		if(n!=null) {
			hs.setAttribute("msg", "Your Note has been Updated");
	    }else {
	    	hs.setAttribute("msg", "Error During Updating");
	    }
		return "redirect:/user/view";
	}
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") int id,HttpSession hs) {
	    if(ns.deleteNote(id)) {
	    	hs.setAttribute("msg", "The note successfully deleted");
	    }else{
	    	hs.setAttribute("msg", "Error During Deleting");
	    }
		return "redirect:/user/view";
	}
	@GetMapping("view")
	public String viewpage(Principal p,Model m) {
//		Users user= logeedUser(p, m);
//		List<Note> notelist=ns.getNotesByUser(user);
//		m.addAttribute("notelist", notelist);
		return paginationAndSorting(0, m, p, "date", "dsc");
	}
	@GetMapping("/page/{pageno}")
    public String paginationAndSorting(@PathVariable("pageno")int pageno,Model m,Principal p,
    @RequestParam("sortfield")String sf,@RequestParam("sortdir")String sd) {
		
		Users user= logeedUser(p, m);
		
		Sort sort=sd.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sf).ascending():Sort.by(sf).descending();
		Pageable pga=PageRequest.of(pageno, 3,sort);
		Page<Note>np=nr.findAllByUser(user, pga);
		
		List<Note> nl=np.getContent();
		
		m.addAttribute("pageno",pageno);
		m.addAttribute("totalelements",np.getTotalElements());
		m.addAttribute("totalpages",np.getTotalPages());
		m.addAttribute("notelist",nl);
		
		m.addAttribute("sf",sf);
		m.addAttribute("sd",sd);
		
		return "view";
	}
	@PostMapping("addnote")
	public String addNote(@ModelAttribute Note note,HttpSession hs,Principal p,Model m) {
	    note.setDate(LocalDate.now());
	    note.setUser(logeedUser(p, m));
	    
	    Note sn=ns.saveNote(note);
	    if(sn!=null) {
	    	hs.setAttribute("msg", "Your Note has been Saved");
	    }else {
	    	hs.setAttribute("msg", "Error During saving");
	    }
		return "redirect:/user/add";
	}
	@PostMapping("searchitem")
	public String search(@RequestParam("searchString") String ss,Principal p, Model m,HttpSession hs) {
	    Users user=logeedUser(p, m);
		List<Note> ansl=ns.getSearches(ss,user);
	    if(ansl.isEmpty()) {
	    	hs.setAttribute("msg", "No Notes Found");
	    	return "redirect:/user/view";
	    }
	    else m.addAttribute("ll", ansl);
		return "search";
	}
	@PostMapping("sort")
	public String sort(@RequestParam("category")String c,@RequestParam("notedate")String nd,HttpSession hs,Model m,Principal p) {
		Users user= logeedUser(p, m);
		if(c.equalsIgnoreCase("default") && nd.equals("")) {
			hs.setAttribute("msg", "nothing to be sort");
			return "redirect:/user/view";
		}
		else if(c.equalsIgnoreCase("default") || nd.equals("")) {
			if(nd.equals("")) {
				List<Note> cl=ns.sortByCategory(c,user);
				if(cl.isEmpty()) {
					hs.setAttribute("msg", "nothing to be sort");
					return "redirect:/user/view";
				}else {
					m.addAttribute("list", cl);
				}
			}
			else if(c.equalsIgnoreCase("default")) {
				List<Note> dl=ns.sortByDate(nd,user);
				
				if(dl.isEmpty()) {
					hs.setAttribute("msg", "nothing to be sort");
					return "redirect:/user/view";
				}else {
					m.addAttribute("list", dl);
				}
			}
		}
		else {
			List<Note> cdl=ns.sortByCategoryandDate(c, nd,user);
			
			if(cdl.isEmpty()) {
				hs.setAttribute("msg", "nothing to be sort");
				return "redirect:/user/view";
			}else {
				m.addAttribute("list", cdl);
			}
	
		}
		return "sort";
	}
	@GetMapping("changepassword")
	public String passwordChange() {
		return "changepassword";
	}
	@PostMapping("change")
	public String change(@RequestParam("oldpassword")String op,@RequestParam("newpassword")String np
			,Principal p,HttpSession hs) {
		String email=p.getName();
		Users user=ur.findByEmail(email);
				
		if(bpe.matches(op, user.getPassword())) {
			user.setPassword(np);
		   Users su=us.saveUser(user);
		   if(su!=null) {
			   hs.setAttribute("msg", "Password Changed");
		   }else {
			   hs.setAttribute("msg", "Something went wrong"); 
		   }
		}else {
			hs.setAttribute("msg", "Please Enter the Correct Password");
		}
		return "redirect:/user/changepassword";
	}

	
}
