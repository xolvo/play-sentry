package ru.purecode.play.sentry;

import net.kencochrane.raven.Raven;
import net.kencochrane.raven.RavenFactory;
import net.kencochrane.raven.dsn.Dsn;
import play.Application;
import play.Logger;
import play.Plugin;

public class SentryPlugin extends Plugin {

    private final Application application;

    private Raven raven;

    public SentryPlugin(Application application) {
        this.application = application;
    }

    @Override
    public void onStart() {
        String dsn_string = application.configuration().getString("sentry.dsn");
        if(dsn_string == null) {
            Logger.error("Can not load Sentry plugin. Define `sentry.dsn` in configuration!");
            return;
        }

        RavenFactory.registerFactory(new CustomRavenFactory());

        Dsn dsn = new Dsn(dsn_string);
        raven = RavenFactory.ravenInstance(dsn);
    }

    @Override
    public void onStop() {
        if(raven != null) {
            raven.closeConnection();
            raven = null;
        }
    }

    @Override
    public boolean enabled() {
        return application.configuration().getBoolean("sentry.enabled", true);
    }

    public Raven raven() {
        return raven;
    }

}
