package tn.dksoft.authentification.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.dksoft.authentification.dto.AppRoleDto;
import tn.dksoft.authentification.entity.AppRole;
import tn.dksoft.authentification.exception.AppUserNotFoundException;
import tn.dksoft.authentification.mappers.AppRoleMapper;
import tn.dksoft.authentification.repository.AppRoleRepository;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	private AppRoleRepository appRoleRepository;

	private AppRoleMapper appRoleMapper;

	public RoleServiceImpl(AppRoleRepository appRoleRepository, AppRoleMapper appRoleMapper) {
		super();
		this.appRoleRepository = appRoleRepository;
		this.appRoleMapper = appRoleMapper;
	}

	@Override
	public List<AppRoleDto> listRoles() {
		List<AppRole> roles = appRoleRepository.findAll();
		List<AppRoleDto> roleDto = roles.stream().map(role -> appRoleMapper.fromAppRole(role))
				.collect(Collectors.toList());
		return roleDto;
	}

	@Override
	public List<AppRoleDto> addNewRole(AppRole appRole) {
		if (appRole.getRoleName() != null) {
			appRoleRepository.save(appRole);
		}
		return listRoles();
	}

	@Override
	public AppRoleDto findRoleById(Long idRole) {
		AppRole role = this.appRoleRepository.findById(idRole)
				.orElseThrow(() -> new AppUserNotFoundException("Role Not Found with id=" + idRole.toString()));
		return appRoleMapper.fromAppRole(role);
	}

	@Override
	public void deleteById(Long idRole) {
		appRoleRepository.deleteById(idRole);
	}

	@Override
	public void deleteAll() {
		appRoleRepository.deleteAll();
	}

	@Override
	public AppRoleDto findRoleByRoleName(String roleName) {
		AppRole appRole = appRoleRepository.findByRoleName(roleName);
		return appRoleMapper.fromAppRole(appRole);
	}

	public AppRoleDto add(AppRole role) {

		appRoleRepository.saveAndFlush(role);
		AppRoleDto roleDto = appRoleMapper.fromAppRole(role);
		return roleDto;
	}

}
