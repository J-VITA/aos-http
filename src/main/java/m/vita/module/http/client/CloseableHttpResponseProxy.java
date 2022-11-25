package m.vita.module.http.client;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.HttpResponse;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.execute.CloseableHttpResponse;
import m.vita.module.http.util.EntityUtils;

@NotThreadSafe
class CloseableHttpResponseProxy implements InvocationHandler {

    private final static Constructor<?> CONSTRUCTOR;

    static {
        try {
            CONSTRUCTOR = Proxy.getProxyClass(CloseableHttpResponseProxy.class.getClassLoader(),
                    new Class<?>[] { CloseableHttpResponse.class }).getConstructor(new Class[] { InvocationHandler.class });
        } catch (NoSuchMethodException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private final HttpResponse original;

    CloseableHttpResponseProxy(final HttpResponse original) {
        super();
        this.original = original;
    }

    public void close() throws IOException {
        final HttpEntity entity = this.original.getEntity();
        EntityUtils.consume(entity);
    }

    public Object invoke(
            final Object proxy, final Method method, final Object[] args) throws Throwable {
        final String mname = method.getName();
        if (mname.equals("close")) {
            close();
            return null;
        } else {
            try {
                return method.invoke(original, args);
            } catch (final InvocationTargetException ex) {
                final Throwable cause = ex.getCause();
                if (cause != null) {
                    throw cause;
                } else {
                    throw ex;
                }
            }
        }
    }

    public static CloseableHttpResponse newProxy(final HttpResponse original) {
        try {
            return (CloseableHttpResponse) CONSTRUCTOR.newInstance(new CloseableHttpResponseProxy(original));
        } catch (InstantiationException ex) {
            throw new IllegalStateException(ex);
        } catch (InvocationTargetException ex) {
            throw new IllegalStateException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
