package tn.dksoft.authentification.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tn.dksoft.authentification.dto.AppUserDto;
import tn.dksoft.authentification.entity.AppUser;
import tn.dksoft.authentification.mappers.AppRoleMapper;
import tn.dksoft.authentification.mappers.AppUserMapper;
import tn.dksoft.authentification.service.RoleServiceImpl;
import tn.dksoft.authentification.service.UserServiceImpl;

@RestController
@RequestMapping(value = "/api/user", method = { RequestMethod.GET, RequestMethod.POST })
public class UserRestController {
	@Autowired
	private final UserServiceImpl userServiceImpl;
	private final RoleServiceImpl roleServiceImpl;
	private final AppRoleMapper appRoleMapper;
	private final AppUserMapper appUserMapper;

	public UserRestController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl,
			AppRoleMapper appRoleMapper, AppUserMapper appUserMapper) {
		super();
		this.userServiceImpl = userServiceImpl;
		this.roleServiceImpl = roleServiceImpl;
		this.appRoleMapper = appRoleMapper;
		this.appUserMapper = appUserMapper;
	}

	@PostMapping("/add/{idRole}")
	public AppUserDto add(@RequestBody AppUser user, @PathVariable("idRole") Long idRole) {
		return userServiceImpl.add(user, idRole);
	}

	@PostMapping("/modifier/{idRole}")
	public AppUserDto midifier(@RequestBody AppUser user, @PathVariable("idRole") Long idRole) {
		return userServiceImpl.modif(user, idRole);
	}

	@DeleteMapping("/supp/{id}")
	public void supprimer(@PathVariable("id") Long id) {
		userServiceImpl.deleteById(id);
	}

	@GetMapping("/search")
	public List<AppUserDto> findUsers(@RequestParam(name = "search", defaultValue = "") String search) {
		return userServiceImpl.searchUsers(search);
	}

	@GetMapping("/list")
	public List<AppUserDto> userJson() {
		return userServiceImpl.listUsers();
	}

	@PostMapping("/addrole/{idRole}")
	public AppUserDto addRole(@RequestBody AppUserDto user, @PathVariable("idRole") Long idRole) {
		userServiceImpl.addRoleToUser(appUserMapper.fromAppUserDto(user), idRole);
		return user;
	}

	@GetMapping(path = "/profile")
	public AppUser profile(Principal principal) {
		return userServiceImpl.loadUserByUserName(principal.getName());
	}

}