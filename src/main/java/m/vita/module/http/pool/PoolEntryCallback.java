package m.vita.module.http.pool;

import m.vita.module.http.connect.PoolEntry;

public interface PoolEntryCallback<T, C> {

    void process(PoolEntry<T, C> entry);

}
