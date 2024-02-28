package tn.dksoft.authentification.service;

import java.util.List;

import tn.dksoft.authentification.dto.AppRoleDto;
import tn.dksoft.authentification.entity.AppRole;

public interface RoleService {
	List<AppRoleDto> listRoles();

	List<AppRoleDto> addNewRole(AppRole appRole);

	AppRoleDto findRoleById(Long idRole);

	void deleteById(Long idRole);

	void deleteAll();

	AppRoleDto findRoleByRoleName(String roleName);
}
