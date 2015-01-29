package ru.purecode.play.sentry.utils;

import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@With(SentryExceptionsLogger.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExceptions {

	/**
	 *
	 * @return Array of excluded exceptions for logging
	 */
	Class<? extends Throwable>[] exclude() default {};

	/**
	 * Developer defined implementation to load current user for HTTP request.
	 * Useful when you have your own auth mechanism.
	 *
	 * Default implementation uses only username and IP address supported by Play.
	 *
	 * @return ref to implementation of {@link ru.purecode.play.sentry.utils.UserInterfaceLoader}
	 */
	Class<? extends UserInterfaceLoader> userloader() default DefaultUserInterfaceLoader.class;

}
