package tn.dksoft.authentification.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;
import tn.dksoft.authentification.exception.AppUserNotFoundException;

@Slf4j
@ControllerAdvice
@CrossOrigin("*")
public class AdviceController {

	@ExceptionHandler(AppUserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String userNotFoundException(final AppUserNotFoundException cnfe, final Model model) {
		log.error("Exception during retrieving entity", cnfe);
		String errorMessage = (cnfe != null ? cnfe.getMessage() : "Unknown error");
		model.addAttribute("errorMessage", errorMessage);
		return "error";
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String exceptionGenerique(final Throwable th, final Model model) {
		log.error("Exception during retrieving entity", th);
		String errorMessage = (th != null ? th.getMessage() : "Unknown error");
		model.addAttribute("errorMessage", errorMessage);
		return "error";
	}

}