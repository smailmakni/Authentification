package tn.dksoft.authentification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tn.dksoft.authentification.dto.AppRoleDto;
import tn.dksoft.authentification.entity.AppRole;
import tn.dksoft.authentification.service.RoleServiceImpl;

@RestController
@RequestMapping(value = "/api/role", method = { RequestMethod.GET, RequestMethod.POST })
public class RoleRestController {
	private final RoleServiceImpl roleServiceImpl;

	public RoleRestController(RoleServiceImpl roleServiceImpl) {
		super();
		this.roleServiceImpl = roleServiceImpl;
	}

	@GetMapping("/list")
	public List<AppRoleDto> roleJson() {
		return roleServiceImpl.listRoles();
	}

	@PostMapping("/add")
	public AppRoleDto add(@RequestBody AppRole role) {

		return roleServiceImpl.add(role);
	}

	@DeleteMapping("/supp/{idRole}")
	public void supprimer(@PathVariable("idRole") Long idRole) {
		roleServiceImpl.deleteById(idRole);
	}

}
