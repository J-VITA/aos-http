package m.vita.module.http.client.execchain;

import m.vita.module.http.HttpResponse;
import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.HttpException;

@Immutable
public class TunnelRefusedException extends HttpException {

    private static final long serialVersionUID = -8646722842745617323L;

    private final HttpResponse response;

    public TunnelRefusedException(final String message, final HttpResponse response) {
        super(message);
        this.response = response;
    }

    public HttpResponse getResponse() {
        return this.response;
    }

}
