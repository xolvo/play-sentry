package ru.purecode.play.sentry.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.kencochrane.raven.event.interfaces.UserInterface;
import play.Logger;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import ru.purecode.play.sentry.SentryLogger;

import java.util.Arrays;

public class SentryExceptionsLogger extends Action<LogExceptions> {

	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		try {
			return delegate.call(ctx);
		} catch(final Throwable t) {
			doLogging(t, ctx);
			throw t;
		}
	}

	private void doLogging(Throwable t, Context ctx) {
		if(!shouldLogThisException(t))
			return;

		new SentryLogger()
			.withException(t)
			.withRequest(ctx.request())
			.withUser(getUser(ctx))
			.log();
	}

	private boolean shouldLogThisException(final Throwable t) {
		Class<? extends Throwable>[] exclude = configuration.exclude();

		return exclude.length == 0 || Iterables.any(Arrays.asList(exclude),
			new Predicate<Class<? extends Throwable>>() {
				@Override
				public boolean apply(Class<? extends Throwable> ex) {
					return !ex.isAssignableFrom(t.getClass());
				}
			});
	}

	private UserInterface getUser(Context ctx) {
		UserInterface ui = null;
		Class<? extends UserInterfaceLoader> implClass = configuration.userloader();

		try {
			ui = implClass.newInstance().load(ctx);
		} catch (InstantiationException | IllegalAccessException e) {
			Logger.warn("Could not operate on UserInterfaceStore to log user", e);
		}

		return ui;
	}

}
