package ru.purecode.play.sentry.utils;

import net.kencochrane.raven.event.interfaces.UserInterface;
import play.mvc.Http.Context;

/**
 *
 * Created by xolvo on 28.01.15.
 */
public class DefaultUserInterfaceLoader implements UserInterfaceLoader {

    @Override
    public UserInterface load(Context ctx) {
        return new UserInterface(null, ctx.request().username(), ctx.request().remoteAddress(), null);
    }

}
