package tn.dksoft.authentification.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tn.dksoft.authentification.entity.AppRole;
import tn.dksoft.authentification.entity.AppUser;
import tn.dksoft.authentification.security.JWTUtil;
import tn.dksoft.authentification.service.RoleServiceImpl;
import tn.dksoft.authentification.service.UserServiceImpl;

@Controller
@RequestMapping(value = "/user", method = { RequestMethod.GET, RequestMethod.POST })
public class AppUserController {
	@Autowired
	private final UserServiceImpl userServiceImpl;
	private final RoleServiceImpl roleServiceImpl;

	public AppUserController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
		super();
		this.userServiceImpl = userServiceImpl;
		this.roleServiceImpl = roleServiceImpl;
	}

	@GetMapping
	/* @PostAuthorize("hasAuthority('user')") */
	public ModelAndView listUsers() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("users", userServiceImpl.listUsers());
		modelAndView.setViewName("listusers");
		return modelAndView;
	}

	@PostMapping("/save")
	/* @PostAuthorize("hasAuthority('admin')") */
	public String save(@ModelAttribute AppUser appUser, Model model) {
		List<AppRole> appRole = new ArrayList<>();
		for (AppRole aa : roleServiceImpl.listRoles()) {
			appRole.add(aa);
		}
		model.addAttribute("rolefound", appRole);

		userServiceImpl.addNewUser(appUser);

		return "adduser";
	}

	/* @PostAuthorize("hasAuthority('superadmin')") */
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id) {
		userServiceImpl.deleteById(id);
		return "redirect:/user";
	}

	@GetMapping("/delete")
	/* @PostAuthorize("hasAuthority('superadmin')") */
	public String deleteAll() {
		userServiceImpl.deleteAll();
		return "redirect:/user";
	}

	@GetMapping("/find")
	/* @PostAuthorize("hasAuthority('user')") */
	public String search(@RequestParam String search, Model model) {
		List<AppUser> searchUser = new ArrayList<>();
		for (AppUser appUser : userServiceImpl.listUsers()) {
			if (appUser.getUserName().equalsIgnoreCase(search)) {
				searchUser.add(appUser);
			}
		}
		model.addAttribute("userfound", searchUser);
		return "searchusers";
	}

	@ResponseBody
	/* @PostAuthorize("hasAuthority('admin')") */
	@GetMapping("/json")
	public List<AppUser> userJson() {
		return userServiceImpl.listUsers();
	}

	@GetMapping("/edit")
	/* @PostAuthorize("hasAuthority('admin')") */
	public String edit(@RequestParam("id") Long id, Model model) {
		AppUser appuser = userServiceImpl.findById(id);
		List<AppRole> roleUser = new ArrayList<>();
		for (AppRole appRole : roleServiceImpl.listRoles()) {
			roleUser.add(appRole);
		}
		model.addAttribute("rolefound", roleUser);
		model.addAttribute("user", appuser);
		Long roleId = appuser.getAppRole().get(0).getIdRole();
		model.addAttribute("roleid", roleId);
		return "edituser";
	}

	@PostMapping("/update")
	/* @PostAuthorize("hasAuthority('admin')") */
	public String update(@ModelAttribute AppUser appUser) {
		userServiceImpl.addNewUser(appUser);
		return "redirect:/user";
	}

	@GetMapping("/refreshToken")
	public void refreshToken(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String authToken = request.getHeader(JWTUtil.AUTH_HEADER);
		if (authToken != null && authToken.startsWith(JWTUtil.PREFIX)) {
			try {

				String jwt = authToken.substring(JWTUtil.PREFIX.length());
				Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
				String username = decodedJWT.getSubject();
				AppUser appUser = userServiceImpl.loadUserByUserName(username);
				String jwtAccessToken = JWT.create().withSubject(appUser.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.EXPIRE_ACCESS_TOKEN))
						.withIssuer(request.getRequestURI().toString())
						.withClaim("roles",
								appUser.getAppRole().stream().map(r -> r.getRoleName()).collect(Collectors.toList()))
						.sign(algorithm);
				Map<String, String> idToken = new HashMap<>();
				idToken.put("access-token", jwtAccessToken);
				idToken.put("refresh-token", jwt);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), idToken);

			} catch (Exception e) {
				throw e;
			}
		} else {
			throw new RuntimeException("Refresh Token Required !!! ");
		}

	}

	@ResponseBody
	@GetMapping(path = "/profile")
	public AppUser profile(Principal principal) {
		return userServiceImpl.loadUserByUserName(principal.getName());
	}

}