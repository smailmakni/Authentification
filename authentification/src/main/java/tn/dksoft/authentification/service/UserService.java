package tn.dksoft.authentification.service;

import java.util.List;

import tn.dksoft.authentification.entity.AppUser;

public interface UserService {
	AppUser findById(Long id); // lorsque l'id null retourne list vide

	void deleteAll();

	List<AppUser> addNewUser(AppUser appUser);

	AppUser loadUserByUserName(String UserName);

	List<AppUser> listUsers();

	void deleteById(Long id);

	void addRoleToUser(String userName, String roleName);

	// String getRoleUser(List<AppRole> appRole);

	// Long getIdRole(List<AppRole> appRole);

}
