package uk.gov.dwp.uc.pairtest.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Jesus GlobalExceptionHandlerTest.java
 * <p>
 * Unit tests for {@link GlobalExceptionHandler}. Tests exception handling for argument type mismatches and
 * malformed HTTP message bodies, ensuring meaningful error messages for invalid inputs.
 * </p>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-08-21
 *
 * Copyright: (c) 2025, UK Government
 */
public class GlobalExceptionHandlerTest {
	@Test
	void testHandleTypeMismatch_ValuePresent() throws Exception {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		Method setter = DummyInput.class.getMethod("setAccountId", Long.class);
		MethodParameter methodParam = new MethodParameter(setter, 0);
		MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException("abc", Long.class, "accountId",
				methodParam, new NumberFormatException("For input string: \"abc\""));
		String result = handler.handleTypeMismatch(ex);
		assertTrue(result.contains("must be an Number"));
		assertTrue(result.contains("accountId"));
		assertTrue(result.contains("abc"));
	}

	@Test
	void testHandleTypeMismatch_ValueNull() throws Exception {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		Method setter = DummyInput.class.getMethod("setAccountId", Long.class);
		MethodParameter methodParam = new MethodParameter(setter, 0);
		MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(null, Long.class, "accountId",
				methodParam, new NumberFormatException("fail"));
		String result = handler.handleTypeMismatch(ex);
		assertTrue(result.contains("must be an Number"));
		assertTrue(result.contains("accountId"));
		assertTrue(result.contains("''") || result.contains("Invalid value ''"));
	}

	@Test
	void testHandleHttpMessageNotReadable_WithRootCauseAndMessage() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		HttpMessageNotReadableException ex = new HttpMessageNotReadableException("fail",
				new NumberFormatException("bad stuff"));
		String result = handler.handleHttpMessageNotReadable(ex);
		assertTrue(result.contains("bad stuff"));
	}

	@Test
	void testHandleHttpMessageNotReadable_RootCauseNotNullMessageNotNull() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		HttpMessageNotReadableException ex =
		    new HttpMessageNotReadableException("fail", new NumberFormatException("bad stuff"), null);
		String result = handler.handleHttpMessageNotReadable(ex);
		assertTrue(result.contains("bad stuff"));
	}

	@Test
	void testHandleHttpMessageNotReadable_RootCauseNull() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		HttpMessageNotReadableException ex = new HttpMessageNotReadableException("fail", null, null);
		String result = handler.handleHttpMessageNotReadable(ex);
		assertTrue(result.equals("Malformed request or invalid input."));
	}

	@Test
	void testHandleHttpMessageNotReadable_RootCauseMessageNull() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		Throwable root = new Throwable() { @Override public String getMessage() { return null; } };
		HttpMessageNotReadableException ex = new HttpMessageNotReadableException("fail", root, null);
		String result = handler.handleHttpMessageNotReadable(ex);
		assertTrue(result.equals("Malformed request or invalid input."));
	}

	public static class DummyInput {
		public void setAccountId(Long accountId) {
		}
	}
}
