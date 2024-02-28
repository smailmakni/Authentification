package tn.dksoft.authentification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tn.dksoft.authentification.service.RoleServiceImpl;
import tn.dksoft.authentification.service.UserServiceImpl;

@Controller
@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
@CrossOrigin("*")
public class HomeController {
	@Autowired
	private final UserServiceImpl userServiceImpl;
	private final RoleServiceImpl roleServiceImpl;

	public HomeController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
		super();
		this.userServiceImpl = userServiceImpl;
		this.roleServiceImpl = roleServiceImpl;
	}

	@GetMapping
	public String home() {
		return "home";
	}
}