package m.vita.module.http.factory;

import m.vita.module.http.HttpMessage;
import m.vita.module.http.io.HttpMessageWriter;
import m.vita.module.http.io.SessionOutputBuffer;

public interface HttpMessageWriterFactory<T extends HttpMessage> {

    HttpMessageWriter<T> create(SessionOutputBuffer buffer);

}
