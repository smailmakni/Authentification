package tn.dksoft.authentification.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import tn.dksoft.authentification.dto.AppUserDto;
import tn.dksoft.authentification.entity.AppUser;

@Service
public class AppUserMapper {

	public AppUserDto fromAppUser(AppUser appUser) {
		AppUserDto appUserDto = new AppUserDto();
		BeanUtils.copyProperties(appUser, appUserDto);
		return appUserDto;
	}

	public AppUser fromAppUserDto(AppUserDto appUserDto) {
		AppUser appUser = new AppUser();
		BeanUtils.copyProperties(appUserDto, appUser);
		return appUser;
	}
}
