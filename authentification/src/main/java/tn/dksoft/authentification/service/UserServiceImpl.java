package tn.dksoft.authentification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.dksoft.authentification.dto.AppUserDto;
import tn.dksoft.authentification.entity.AppRole;
import tn.dksoft.authentification.entity.AppUser;
import tn.dksoft.authentification.exception.AppUserNotFoundException;
import tn.dksoft.authentification.mappers.AppRoleMapper;
import tn.dksoft.authentification.mappers.AppUserMapper;
import tn.dksoft.authentification.repository.AppRoleRepository;
import tn.dksoft.authentification.repository.AppUserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	private PasswordEncoder passwordEncoder;
	private AppUserMapper appUserMapper;
	private RoleServiceImpl roleServiceImpl;
	private AppRoleMapper appRoleMapper;

	public UserServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
			AppUserMapper appUserMapper, RoleServiceImpl roleServiceImpl, AppRoleMapper appRoleMapper) {
		super();
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.appUserMapper = appUserMapper;
		this.roleServiceImpl = roleServiceImpl;
		this.appRoleMapper = appRoleMapper;
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<AppUserDto> addNewUser(AppUser appUser) {
		if (appUser.getPassword() != null) {
			String pwd = appUser.getPassword();
			appUser.setPassword(passwordEncoder.encode(pwd));
			appUserRepository.save(appUser);
		}
		return listUsers();
	}

	@Override
	public void addRoleToUser(AppUser user, Long idRole) {
		AppRole role = appRoleMapper.fromAppRoleDto(roleServiceImpl.findRoleById(idRole));
		user.getAppRole().add(role);
		appUserMapper.fromAppUser(user);
	}

	@Override
	public AppUser loadUserByUserName(String userName) {
		return appUserRepository.findByUserName(userName);

	}

	@Override
	public List<AppUserDto> listUsers() {
		List<AppUser> users = appUserRepository.findAll();
		List<AppUserDto> usersDto = users.stream().map(user -> appUserMapper.fromAppUser(user))
				.collect(Collectors.toList());
		return usersDto;
	}

	@Override
	public AppUserDto findById(Long id) {
		AppUser user = this.appUserRepository.findById(id)
				.orElseThrow(() -> new AppUserNotFoundException("Compte Not Found with id=" + id.toString()));
		return appUserMapper.fromAppUser(user);
	}

	@Override
	public void deleteById(Long id) {
		appUserRepository.deleteById(id);

	}

	@Override
	public void deleteAll() {
		appUserRepository.deleteAll();
	}

	public List<AppUserDto> searchUsers(String search) {
		List<AppUser> users = appUserRepository.findByUserNameContaining(search);
		List<AppUserDto> userDto = users.stream().map(user -> appUserMapper.fromAppUser(user))
				.collect(Collectors.toList());
		return userDto;
	}

	public AppUserDto modif(AppUser user, Long idRole) {
		AppRole role = appRoleMapper.fromAppRoleDto(roleServiceImpl.findRoleById(idRole));
		List<AppRole> roles = new ArrayList<>();
		roles.add(role);
		user.setAppRole(roles);
		user.setUserName(user.getUserName());
		user.setEmail(user.getEmail());
		AppUserDto userDto = appUserMapper.fromAppUser(user);
		return userDto;
	}

	public AppUserDto add(AppUser user, Long idRole) {

		if (user.getPassword() != null) {
			String pwd = user.getPassword();
			user.setPassword(passwordEncoder.encode(pwd));
			appUserRepository.save(user);
		}

		AppRole role = appRoleMapper.fromAppRoleDto(roleServiceImpl.findRoleById(idRole));
		List<AppRole> roles = new ArrayList<>();
		roles.add(role);
		user.setAppRole(roles);
		appUserRepository.save(user);
		AppUserDto userDto = appUserMapper.fromAppUser(user);
		return userDto;
	}

	/*
	 * @Override public Page<List<AppUser>> searchUsers(String search, Pageable
	 * pageable) { Page<List<AppUser>> users =
	 * appUserRepository.findByUserName(search, pageable); return users; }
	 */

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
