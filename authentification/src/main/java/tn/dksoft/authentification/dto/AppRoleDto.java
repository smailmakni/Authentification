package tn.dksoft.authentification.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AppRoleDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idRole;

	private String roleName;
	
	

}
