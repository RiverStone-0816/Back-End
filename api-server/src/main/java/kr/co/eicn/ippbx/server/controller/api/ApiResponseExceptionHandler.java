package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.exception.StorageFileNotFoundException;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.UncheckedIOException;
import java.sql.SQLException;
import java.sql.SQLNonTransientException;
import java.util.Arrays;

import static kr.co.eicn.ippbx.util.JsonResult.Result.failure;
import static kr.co.eicn.ippbx.util.JsonResult.create;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ApiResponseExceptionHandler {
	protected final Logger logger = LoggerFactory.getLogger(ApiResponseExceptionHandler.class);
	@Qualifier
	private final RequestMessage message;

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<JsonResult<?>> httpMediaTypeNotSupportedException(Exception e) {
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(writeResponse(e, message.getText("error.exception.cause.httpMediaTypeNotSupportedException")));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<JsonResult<?>> httpRequestMethodNotSupportedException(Exception e) {
		return ResponseEntity.status(HttpServletResponse.SC_METHOD_NOT_ALLOWED).body(writeResponse(e, message.getText("error.exception.cause.httpMediaTypeNotSupportedException")));
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<JsonResult<?>> validationException(ValidationException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(create(failure, message.getText("error.exception.cause.IllegalArgumentException"), e.getBindingResult()));
	}

	@ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
	public ResponseEntity<JsonResult<?>> authenticationException(Exception e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(writeResponse(e, message.getText("error.access.denied")));
	}

	@ExceptionHandler({EntityNotFoundException.class})
	public ResponseEntity<JsonResult<?>> entityNotFoundException(Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(writeResponse(e, message.getText("error.exception.cause.EntityNotFoundException")));
	}

	@ExceptionHandler({StorageFileNotFoundException.class})
	public ResponseEntity<JsonResult<?>> storageFileNotFoundException(Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(writeResponse(e, message.getText("error.exception.cause.StorageFileNotFoundException")));
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<JsonResult<?>> usernameNotFoundException(Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(writeResponse(e, e.getMessage()));
	}

	@ExceptionHandler({DataAccessException.class, org.springframework.dao.DataAccessException.class, SQLException.class})
	public ResponseEntity<JsonResult<?>> sqlException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(writeResponseCustomMessage(e, message.getText("error.exception.cause.SQLException")));
	}

	@ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
	public ResponseEntity<JsonResult<?>> illegalArgumentException(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(writeResponse(e, message.getText("error.exception.cause.IllegalArgumentException")));
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<JsonResult<?>> duplicateKeyException(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(writeResponse(e, message.getText("error.exception.cause.DuplicateKeyException")));
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<JsonResult<?>> nullPointerException(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(writeResponse(e, message.getText("error.exception.cause.NullPointerException")));
	}

	@ExceptionHandler(UnsupportedOperationException.class)
	public ResponseEntity<JsonResult<?>> unsupportedOperationException(Exception e) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(SQLNonTransientException.class)
	public ResponseEntity<JsonResult<?>> sqlNonTransientException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(writeResponse(new Exception(message.getText("error.exception.cause.SQLNonTransientException"), e),
				message.getText("error.exception.cause.SQLNonTransientException")));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<JsonResult<?>> exception(HttpServletResponse response, Exception e) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(writeResponse(e, message.getText("error.exception.cause.Exception")));
	}

	@ExceptionHandler(UncheckedIOException.class)
	public ResponseEntity<JsonResult<?>> uncheckedIOException(HttpServletResponse response, Exception e) {
		if (StringUtils.isEmpty(e.getMessage()))
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(writeResponse(e, message.getText("error.exception.cause.UncheckedIOException")));

		final String exceptionString = "Exception:";
		final int exceptionIndex = e.getMessage().indexOf(exceptionString);

		if (exceptionIndex >= 0) {
			printException(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(writeResponse(new Exception(e.getMessage().substring(exceptionIndex + exceptionString.length() + 1)), message.getText("error.exception.status.500")));
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(writeResponse(e, message.getText("error.exception.status.500")));
		}
	}

	private JsonResult<?> writeResponse(Exception e, String defaultMessage) {
		printException(e);

		final String message = e.getMessage();
		return create(StringUtils.isEmpty(message) ? defaultMessage : message);
	}

	private JsonResult<?> writeResponseCustomMessage(Exception e, String defaultMessage) {
		printException(e);

		return create(defaultMessage);
	}

	private void printException(Exception e) {
		logger.info(e.getMessage());
		e.printStackTrace();
		Arrays.stream(e.getStackTrace())
				.filter(exception -> exception.getClassName().startsWith(Constants.BASE_PACKAGE)
						&& StringUtils.isNotEmpty(exception.getFileName())
						&& !exception.getFileName().startsWith("<"))
				.forEach(exception -> logger.info("\t" + exception.toString()));
	}
}
