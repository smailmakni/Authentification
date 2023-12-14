package tn.dksoft.authentification.controller;

import java.util.ArrayList;
import java.util.List;

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

import tn.dksoft.authentification.entity.AppRole;
import tn.dksoft.authentification.service.RoleServiceImpl;

@Controller
@RequestMapping(value = "/role", method = { RequestMethod.GET, RequestMethod.POST })
public class AppRoleController {
	private final RoleServiceImpl roleServiceImpl;

	public AppRoleController(RoleServiceImpl roleServiceImpl) {
		super();
		this.roleServiceImpl = roleServiceImpl;
	}

	@GetMapping
	public ModelAndView listRoles() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("roles", roleServiceImpl.listRoles());
		modelAndView.setViewName("listroles");
		return modelAndView;
	}

	@PostMapping("/save")
	public String save(@ModelAttribute AppRole appRole) {
		roleServiceImpl.addNewRole(appRole);
		return "addrole";
	}

	@GetMapping("/delete/{idRole}")
	public String delete(@PathVariable("idRole") Long idRole) {
		roleServiceImpl.deleteById(idRole);
		return "redirect:/role";
	}

	@GetMapping("/delete")
	public String deleteAll() {
		roleServiceImpl.deleteAll();
		return "redirect:/role";
	}

	@GetMapping("/find")
	public String search(@RequestParam String search, Model model) {
		List<AppRole> searchRole = new ArrayList<>();
		for (AppRole appRole : roleServiceImpl.listRoles()) {
			if (appRole.getRoleName().equalsIgnoreCase(search)) {
				searchRole.add(appRole);
			}
		}
		model.addAttribute("rolefound", searchRole);
		return "searchroles";
	}

	@ResponseBody
	@GetMapping("/json")
	public List<AppRole> roleJson() {
		return roleServiceImpl.listRoles();
	}

	@GetMapping("/edit")
	public String edit(@RequestParam("idRole") Long idRole, Model model) {
		AppRole appRole = roleServiceImpl.findRoleById(idRole);
		model.addAttribute("role", appRole);
		return "editrole";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute AppRole appRole) {
		roleServiceImpl.addNewRole(appRole);
		return "redirect:/role";
	}

}
