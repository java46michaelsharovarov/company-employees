package telran.company.exceptions;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionsHandler {
	
	static Logger LOG = LoggerFactory.getLogger(GlobalExceptionsHandler.class);
	public static final String RESPONSE_NOT_FOUND = "responce status - NOT FOUND";
	public static final String RESPONSE_BAD_REQUEST = "responce status - BAD REQUEST";

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<String> handlerMethodArgument(MethodArgumentNotValidException e) {
		List<ObjectError> errors = e.getAllErrors();
		String body = errors.stream().map(err -> err.getDefaultMessage()).collect(Collectors.joining("; \n"));
		LOG.error(RESPONSE_BAD_REQUEST);
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<String> handlerConstraintViolation(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> constraints = e.getConstraintViolations();
		String body = constraints.stream().map(constraint -> constraint.getMessage()).collect(Collectors.joining("; \n"));
		LOG.error(RESPONSE_BAD_REQUEST);
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST); 
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<String> handlerIllegalArgument(IllegalArgumentException e) {
		LOG.error(RESPONSE_BAD_REQUEST);
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	ResponseEntity<String> handlerNoSuchElement(NoSuchElementException e) {
		LOG.error(RESPONSE_NOT_FOUND);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
}
