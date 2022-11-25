package m.vita.module.http.io;

import m.vita.module.http.HttpMessage;
import m.vita.module.http.util.args.MessageConstraints;

public interface HttpMessageParserFactory<T extends HttpMessage> {

    HttpMessageParser<T> create(SessionInputBuffer buffer, MessageConstraints constraints);

}
