package ru.purecode.play.sentry.utils;

import net.kencochrane.raven.event.interfaces.UserInterface;
import play.mvc.Http.Context;

/**
 *
 * Created by xolvo on 28.01.15.
 */
public interface UserInterfaceLoader {

    UserInterface load(Context ctx);

}
