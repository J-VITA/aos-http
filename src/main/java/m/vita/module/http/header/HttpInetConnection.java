package m.vita.module.http.header;

import java.net.InetAddress;

import m.vita.module.http.connect.HttpConnection;

public interface HttpInetConnection extends HttpConnection {

    InetAddress getLocalAddress();

    int getLocalPort();

    InetAddress getRemoteAddress();

    int getRemotePort();

}
