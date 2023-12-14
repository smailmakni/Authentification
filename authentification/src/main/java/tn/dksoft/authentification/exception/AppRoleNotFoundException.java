package tn.dksoft.authentification.exception;

public class AppRoleNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AppRoleNotFoundException(String msg) {
		super(msg);
	}
}