package com.greenhills.fuel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "invalid fuel type")
public class IllegalFuelTypeException extends IllegalArgumentException {
}
