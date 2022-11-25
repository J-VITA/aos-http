package m.vita.module.http.factory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import m.vita.module.http.header.HttpContext;

public interface LayeredConnectionSocketFactory extends ConnectionSocketFactory {

    /**
     * Returns a socket connected to the given host that is layered over an
     * existing socket.  Used primarily for creating secure sockets through
     * proxies.
     *
     * @param socket the existing socket
     * @param target the name of the target host.
     * @param port the port to connect to on the target host.
     * @param context the actual HTTP context.
     *
     * @return Socket a new socket
     *
     * @throws IOException if an I/O error occurs while creating the socket
     */
    Socket createLayeredSocket(
            Socket socket,
            String target,
            int port,
            HttpContext context) throws IOException, UnknownHostException;

}
