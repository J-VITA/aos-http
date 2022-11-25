package m.vita.module.http.params;

import m.vita.module.http.annotation.ThreadSafe;
import m.vita.module.http.header.BasicHttpParams;
import m.vita.module.http.header.HttpParams;

@ThreadSafe
public class SyncBasicHttpParams extends BasicHttpParams {

    private static final long serialVersionUID = 5387834869062660642L;

    public SyncBasicHttpParams() {
        super();
    }

    @Override
    public synchronized boolean removeParameter(final String name) {
        return super.removeParameter(name);
    }

    @Override
    public synchronized HttpParams setParameter(final String name, final Object value) {
        return super.setParameter(name, value);
    }

    @Override
    public synchronized Object getParameter(final String name) {
        return super.getParameter(name);
    }

    @Override
    public synchronized boolean isParameterSet(final String name) {
        return super.isParameterSet(name);
    }

    @Override
    public synchronized boolean isParameterSetLocally(final String name) {
        return super.isParameterSetLocally(name);
    }

    @Override
    public synchronized void setParameters(final String[] names, final Object value) {
        super.setParameters(names, value);
    }

    @Override
    public synchronized void clear() {
        super.clear();
    }

    @Override
    public synchronized Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
