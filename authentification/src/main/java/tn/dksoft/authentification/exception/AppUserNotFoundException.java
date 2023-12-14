package tn.dksoft.authentification.exception;

public class AppUserNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public AppUserNotFoundException(String msg) {
		super(msg);
	}

}

