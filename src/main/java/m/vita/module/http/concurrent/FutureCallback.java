package m.vita.module.http.concurrent;

public interface FutureCallback<T> {

    void completed(T result);

    void failed(Exception ex);

    void cancelled();

}
