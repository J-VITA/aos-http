package m.vita.module.http.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.util.Args;

@NotThreadSafe
public final class RegistryBuilder<I> {

    private final Map<String, I> items;

    public static <I> RegistryBuilder<I> create() {
        return new RegistryBuilder<I>();
    }

    RegistryBuilder() {
        super();
        this.items = new HashMap<String, I>();
    }

    public RegistryBuilder<I> register(final String id, final I item) {
        Args.notEmpty(id, "ID");
        Args.notNull(item, "Item");
        items.put(id.toLowerCase(Locale.ENGLISH), item);
        return this;
    }

    public Registry<I> build() {
        return new Registry<I>(items);
    }

    @Override
    public String toString() {
        return items.toString();
    }

}