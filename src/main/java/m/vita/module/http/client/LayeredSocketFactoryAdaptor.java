package m.vita.module.http.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import m.vita.module.http.factory.LayeredSchemeSocketFactory;
import m.vita.module.http.factory.LayeredSocketFactory;


class LayeredSocketFactoryAdaptor extends SocketFactoryAdaptor implements LayeredSocketFactory {

    private final LayeredSchemeSocketFactory factory;

    LayeredSocketFactoryAdaptor(final LayeredSchemeSocketFactory factory) {
        super(factory);
        this.factory = factory;
    }

    public Socket createSocket(
            final Socket socket,
            final String host, final int port, final boolean autoClose) throws IOException, UnknownHostException {
        return this.factory.createLayeredSocket(socket, host, port, autoClose);
    }

}