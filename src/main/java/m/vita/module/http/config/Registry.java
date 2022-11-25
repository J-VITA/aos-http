package m.vita.module.http.config;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import m.vita.module.http.annotation.ThreadSafe;

@ThreadSafe
public final class Registry<I> implements Lookup<I> {

    private final Map<String, I> map;

    Registry(final Map<String, I> map) {
        super();
        this.map = new ConcurrentHashMap<String, I>(map);
    }

    public I lookup(final String key) {
        if (key == null) {
            return null;
        }
        return map.get(key.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public String toString() {
        return map.toString();
    }

}
