package m.vita.module.http.header;

import m.vita.module.http.util.ProtocolVersion;

public interface RequestLine {
    String getMethod();

    ProtocolVersion getProtocolVersion();

    String getUri();
}
