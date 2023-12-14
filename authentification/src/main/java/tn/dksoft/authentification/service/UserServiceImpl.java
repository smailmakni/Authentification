package tn.dksoft.authentification.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.dksoft.authentification.entity.AppRole;
import tn.dksoft.authentification.entity.AppUser;
import tn.dksoft.authentification.exception.AppUserNotFoundException;
import tn.dksoft.authentification.repository.AppRoleRepository;
import tn.dksoft.authentification.repository.AppUserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
			PasswordEncoder passwordEncoder) {
		super();
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<AppUser> addNewUser(AppUser appUser) {

		if (appUser.getPassword() != null) {
			String pwd = appUser.getPassword();
			appUser.setPassword(passwordEncoder.encode(pwd));
			appUserRepository.save(appUser);
		}
		return listUsers();
	}

	@Override
	public void addRoleToUser(String userName, String roleName) {
		AppUser appUser = appUserRepository.findByUserName(userName);
		AppRole appRole = appRoleRepository.findByRoleName(roleName);
		appUser.getAppRole().add(appRole);
	}

	@Override
	public AppUser loadUserByUserName(String userName) {
		return appUserRepository.findByUserName(userName);

	}

	@Override
	public List<AppUser> listUsers() {
		return appUserRepository.findAll();
	}

	@Override
	public AppUser findById(Long id) {
		Optional<AppUser> userOptional = this.appUserRepository.findById(id);
		return (AppUser) userOptional.orElseThrow(() -> {
			return new AppUserNotFoundException("Compte Not Found with id=" + id.toString());
		});

	}

	@Override
	public void deleteById(Long id) {
		appUserRepository.deleteById(id);

	}

	@Override
	public void deleteAll() {
		appUserRepository.deleteAll();
	}

	/*
	 * @Override // retourne le 1er role d'une liste des roles public String
	 * getRoleUser(List<AppRole> appRole) { return appRole.get(0).getRoleName(); }
	 * 
	 * 
	 * @Override public Long getIdRole(List<AppRole> appRole) { Long index =(long)
	 * 0; for (AppRole role : roleServiceImpl.listRoles()) {
	 * if(getRoleUser(appRole).equals(role.getRoleName())){ index =
	 * role.getIdRole(); }
	 * 
	 * return index ; }
	 */

}
