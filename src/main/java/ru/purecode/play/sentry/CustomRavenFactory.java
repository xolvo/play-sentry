package ru.purecode.play.sentry;

import net.kencochrane.raven.DefaultRavenFactory;
import net.kencochrane.raven.Raven;
import net.kencochrane.raven.dsn.Dsn;
import net.kencochrane.raven.marshaller.Marshaller;
import net.kencochrane.raven.marshaller.json.JsonMarshaller;
import ru.purecode.play.sentry.bindings.PlayHttpRequestInterfaceBinding;
import ru.purecode.play.sentry.interfaces.PlayHttpRequestInterface;

public class CustomRavenFactory extends DefaultRavenFactory {
    @Override
    public Raven createRavenInstance(Dsn dsn) {
        Raven raven = new Raven();
        raven.setConnection(createConnection(dsn));

        return raven;
    }

    @Override
    protected Marshaller createMarshaller(Dsn dsn) {
        JsonMarshaller marshaller = (JsonMarshaller) super.createMarshaller(dsn);
        marshaller.addInterfaceBinding(PlayHttpRequestInterface.class, new PlayHttpRequestInterfaceBinding());

        return marshaller;
    }
}