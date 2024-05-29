package graphql.kickstart.spring.cache;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.dataloader.ValueCache;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

/**
 * A {@link ValueCache} which uses a Spring {@link Cache} for caching.
 *
 * @see <a
 * href="https://www.graphql-java.com/documentation/batching/#per-request-data-loaders">GraphQL Java
 * docs</a>
 */
@RequiredArgsConstructor
public class SpringValueCache<K, V> implements ValueCache<K, V> {

  private final Cache cache;
  private Function<K, ?> keyTransformer;

  @Override
  public CompletableFuture<V> get(K key) {
    return supplyAsync(() -> {
      Object finalKey = this.getKey(key);
      ValueWrapper valueWrapper = this.cache.get(finalKey);

      if (valueWrapper == null) {
        throw new CacheEntryNotFoundException(this.cache.getName(), finalKey);
      }

      return (V) valueWrapper.get();
    });
  }

  @Override
  public CompletableFuture<V> set(K key, V value) {
    return supplyAsync(() -> {
      this.cache.put(this.getKey(key), value);
      return value;
    });
  }

  @Override
  public CompletableFuture<Void> delete(K key) {
    return runAsync(() -> this.cache.evictIfPresent(this.getKey(key)));
  }

  @Override
  public CompletableFuture<Void> clear() {
    return runAsync(this.cache::invalidate);
  }

  public <KFinal> SpringValueCache<K, V> setKeyTransformer(Function<K, KFinal> transformer) {
    this.keyTransformer = transformer;
    return this;
  }

  private Object getKey(K key) {
    return (
      this.keyTransformer == null
        ? key
        : this.keyTransformer.apply(key)
    );
  }

  public static class CacheEntryNotFoundException extends RuntimeException {
    public CacheEntryNotFoundException(String cacheName, Object key) {
      super("Entry could not be found in cache named \"" + cacheName + "\" for key: " + key);
    }
  }
}
