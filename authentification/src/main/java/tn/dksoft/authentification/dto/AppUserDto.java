package tn.dksoft.authentification.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import tn.dksoft.authentification.entity.AppRole;

@Data
public class AppUserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String userName;

	private String email;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	private List<AppRole> appRole = new ArrayList<>();

}