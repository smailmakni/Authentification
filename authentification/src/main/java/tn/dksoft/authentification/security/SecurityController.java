/*
 * package tn.dksoft.authentification.security;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestMethod;
 * 
 * import tn.dksoft.authentification.service.RoleServiceImpl; import
 * tn.dksoft.authentification.service.UserServiceImpl;
 * 
 * @Controller
 * 
 * @RequestMapping(value = "/login", method = { RequestMethod.GET,
 * RequestMethod.POST }) public class SecurityController {
 * 
 * @Autowired private final UserServiceImpl userServiceImpl; private final
 * RoleServiceImpl roleServiceImpl;
 * 
 * public SecurityController(UserServiceImpl userServiceImpl, RoleServiceImpl
 * roleServiceImpl) { super(); this.userServiceImpl = userServiceImpl;
 * this.roleServiceImpl = roleServiceImpl; }
 * 
 * @GetMapping(value = "/login") public String login() { return "login"; }
 * 
 * @GetMapping(value = "/") public String home() { return "redirect:/home"; }
 * 
 * }
 */