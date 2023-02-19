package xyz.jxzou.zblog.common.util.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maps
 *
 * @author Jx
 **/
public class Maps<K, V> {

    private final Map<K, V> params = new HashMap<>(4);

    public Map<K, V> hashMap() {
        return params;
    }

    public Map<K, V> currentHashMap() {
        return new ConcurrentHashMap<>(params);
    }

    public Maps<K, V> put(K k, V v) {
        params.put(k, v);
        return this;
    }
}
