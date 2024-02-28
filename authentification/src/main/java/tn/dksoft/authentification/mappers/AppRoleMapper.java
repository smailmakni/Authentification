package tn.dksoft.authentification.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import tn.dksoft.authentification.dto.AppRoleDto;
import tn.dksoft.authentification.entity.AppRole;

@Service
public class AppRoleMapper {

	public AppRoleDto fromAppRole(AppRole appRole) {
		AppRoleDto appRoleDto = new AppRoleDto();
		BeanUtils.copyProperties(appRole, appRoleDto);
		return appRoleDto;
	}

	public AppRole fromAppRoleDto(AppRoleDto appRoleDto) {
		AppRole appRole = new AppRole();
		BeanUtils.copyProperties(appRoleDto, appRole);
		return appRole;
	}
}
