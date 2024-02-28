package tn.dksoft.authentification.service;

import java.util.List;

import tn.dksoft.authentification.dto.AppUserDto;
import tn.dksoft.authentification.entity.AppUser;

public interface UserService {
	AppUserDto findById(Long id); // lorsque l'id null retourne list vide

	void deleteAll();

	List<AppUserDto> addNewUser(AppUser appUser);

	AppUser loadUserByUserName(String UserName);

	List<AppUserDto> searchUsers(String search);

	List<AppUserDto> listUsers();

	void deleteById(Long id);

	void addRoleToUser(AppUser user, Long idRole);

	// String getRoleUser(List<AppRole> appRole);

	// Long getIdRole(List<AppRole> appRole);

}
