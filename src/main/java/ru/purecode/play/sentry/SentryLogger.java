package ru.purecode.play.sentry;

import net.kencochrane.raven.event.Event.Level;
import net.kencochrane.raven.event.EventBuilder;
import net.kencochrane.raven.event.interfaces.ExceptionInterface;
import net.kencochrane.raven.event.interfaces.SentryInterface;
import play.Logger;
import play.Play;
import play.mvc.Http.Request;
import ru.purecode.play.sentry.interfaces.PlayHttpRequestInterface;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class SentryLogger {
	private static String format(String str, Object... args) {
		try {
            if(args != null && args.length > 0) {
                return String.format(str, args);
            }
            return str;
        } catch (Exception e) {
            return str;
        }
	}
	
	private static String determineCulprit(Throwable throwable) {
		Throwable currentThrowable = throwable;
        String culprit = null;
        
        while (currentThrowable != null) {
            StackTraceElement[] elements = currentThrowable.getStackTrace();
            if (elements.length > 0) {
                StackTraceElement trace = elements[0];
                culprit = trace.getClassName() + "." + trace.getMethodName();
            }
            currentThrowable = currentThrowable.getCause();
        }
        
        return culprit;
	}
	
	private EventBuilder builder;
	
	public SentryLogger() {
		builder = new EventBuilder();
	}
	
	public SentryLogger setLevel(Level level) {
		builder.withLevel(level);
		return this;
	}
	
	public SentryLogger setMessage(String message, Object... args) {
		builder.withMessage(format(message, args));
		return this;
	}
	
	public SentryLogger setCulprit(String culprit) {
		builder.withCulprit(culprit);
		return this;
	}
	
	public SentryLogger setLogger(String logger) {
		builder.withLogger(logger);
		return this;
	}
	
	public SentryLogger withException(Throwable t) {
		if(t != null)
			builder
				.withMessage(t.getMessage())
				.withCulprit(determineCulprit(t))
				.withSentryInterface(new ExceptionInterface(t));
		
		return this;
	}
	
	public SentryLogger withRequest(Request request) {
		builder.withSentryInterface(new PlayHttpRequestInterface(request));
		return this;
	}

	public SentryLogger withUser(Object user) {
		if(user != null && user instanceof net.kencochrane.raven.event.interfaces.UserInterface)
			builder.withSentryInterface((SentryInterface) user);

		return this;
	}
	
	public SentryLogger addTags(Map<String, String> tags) {
		if(tags != null) {
			for(String tag : tags.keySet()) {
				addTag(tag, tags.get(tag));
			}
		}
		
		return this;
	}
	
	public SentryLogger addTag(String tag, String value) {
		if(tag != null && value != null)
			builder.withTag(tag, value);
		
		return this;
	}
	
	public SentryLogger addExtras(Map<String, Object> extras) {
		if(extras != null) {
			for(String extra : extras.keySet()) {
				builder.withExtra(extra, extras.get(extra));
			}
		}
		
		return this;
	}
	
	public SentryLogger addExtra(String extra, Object value) {
		if(extra != null && value != null)
			builder.withExtra(extra, value);
		
		return this;
	}
	
	public void log() {
		try {
			builder.withServerName(InetAddress.getLocalHost().getHostName());
		} catch(UnknownHostException e) {
			Logger.warn("Can not set SERVER_NAME for Raven client", e);
		}
		
		SentryPlugin plugin = Play.application().plugin(SentryPlugin.class);
		
		if(plugin != null && plugin.enabled())
			plugin.raven().sendEvent(builder.build());
	}
}
