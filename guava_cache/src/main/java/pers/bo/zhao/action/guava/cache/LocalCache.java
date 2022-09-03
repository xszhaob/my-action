package pers.bo.zhao.action.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class LocalCache {

    private LoadingCache<Long, Optional<Long>> cache = CacheBuilder
            .newBuilder()
            .maximumSize(10240)
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, Optional<Long>>() {
                @Override
                public Optional<Long> load(Long key) throws Exception {
                    System.out.println("load value of key " + key);
                    return Optional.of(key);
                }
            });


    public Long getValue(Long key) throws ExecutionException {
        return cache.get(key).orElse(-1L);
    }
}
