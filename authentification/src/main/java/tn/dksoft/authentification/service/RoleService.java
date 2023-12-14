package tn.dksoft.authentification.service;

import java.util.List;

import tn.dksoft.authentification.entity.AppRole;

public interface RoleService {
	List<AppRole> listRoles();

	List<AppRole> addNewRole(AppRole appRole);

	AppRole findRoleById(Long idRole);

	List<AppRole> deleteById(Long idRole);

	void deleteAll();

	AppRole findRoleByRoleName(String roleName);
}
