package m.vita.module.http.config;

/**
 * Generic lookup by low-case string ID.
 *
 * @since 4.3
 */
public interface Lookup<I> {

    I lookup(String name);

}
