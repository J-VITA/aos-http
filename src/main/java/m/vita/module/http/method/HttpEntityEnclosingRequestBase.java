package m.vita.module.http.method;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HttpEntityEnclosingRequest;
import m.vita.module.http.util.CloneUtils;
import m.vita.module.http.util.HTTP;

@NotThreadSafe // HttpRequestBase is @NotThreadSafe
public abstract class HttpEntityEnclosingRequestBase
        extends HttpRequestBase implements HttpEntityEnclosingRequest {

    private HttpEntity entity;

    public HttpEntityEnclosingRequestBase() {
        super();
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public void setEntity(final HttpEntity entity) {
        this.entity = entity;
    }

    public boolean expectContinue() {
        final Header expect = getFirstHeader(HTTP.EXPECT_DIRECTIVE);
        return expect != null && HTTP.EXPECT_CONTINUE.equalsIgnoreCase(expect.getValue());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final HttpEntityEnclosingRequestBase clone =
                (HttpEntityEnclosingRequestBase) super.clone();
        if (this.entity != null) {
            clone.entity = CloneUtils.cloneObject(this.entity);
        }
        return clone;
    }

}
