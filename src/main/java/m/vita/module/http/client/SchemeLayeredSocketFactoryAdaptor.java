package m.vita.module.http.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import m.vita.module.http.factory.LayeredSocketFactory;
import m.vita.module.http.factory.SchemeLayeredSocketFactory;
import m.vita.module.http.header.HttpParams;

@Deprecated
class SchemeLayeredSocketFactoryAdaptor extends SchemeSocketFactoryAdaptor
        implements SchemeLayeredSocketFactory {

    private final LayeredSocketFactory factory;

    SchemeLayeredSocketFactoryAdaptor(final LayeredSocketFactory factory) {
        super(factory);
        this.factory = factory;
    }

    public Socket createLayeredSocket(
            final Socket socket,
            final String target, final int port,
            final HttpParams params) throws IOException, UnknownHostException {
        return this.factory.createSocket(socket, target, port, true);
    }

}
