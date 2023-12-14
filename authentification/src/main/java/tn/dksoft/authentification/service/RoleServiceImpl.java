package tn.dksoft.authentification.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.dksoft.authentification.entity.AppRole;
import tn.dksoft.authentification.exception.AppRoleNotFoundException;
import tn.dksoft.authentification.repository.AppRoleRepository;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	private AppRoleRepository appRoleRepository;

	public RoleServiceImpl(AppRoleRepository appRoleRepository) {
		super();
		this.appRoleRepository = appRoleRepository;
	}

	@Override
	public List<AppRole> listRoles() {
		return appRoleRepository.findAll();
	}

	@Override
	public List<AppRole> addNewRole(AppRole appRole) {
		if (appRole.getRoleName() != null) {
			appRoleRepository.save(appRole);
		}
		return listRoles();
	}

	@Override
	public AppRole findRoleById(Long idRole) {
		Optional<AppRole> roleOptional = this.appRoleRepository.findById(idRole);
		return (AppRole) roleOptional.orElseThrow(() -> {
			return new AppRoleNotFoundException("role Not Found with id=" + idRole.toString());
		});

	}

	@Override
	public List<AppRole> deleteById(Long idRole) {
		appRoleRepository.deleteById(idRole);
		return listRoles();
	}

	@Override
	public void deleteAll() {
		appRoleRepository.deleteAll();
	}

	@Override
	public AppRole findRoleByRoleName(String roleName) {
		return appRoleRepository.findByRoleName(roleName);
	}

}
