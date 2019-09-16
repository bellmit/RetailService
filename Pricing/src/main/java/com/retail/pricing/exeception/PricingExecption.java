package com.retail.pricing.execption;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@ControllerAdvice
@RestController
public class PricingExecption {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value={TypeMismatchException.class,HttpMessageNotReadableException.class})
	public ResponseEntity<?> handleException() {
		return new ResponseEntity<>(new Gson().toJson("Bad Request"), HttpStatus.BAD_REQUEST);
	}
}
