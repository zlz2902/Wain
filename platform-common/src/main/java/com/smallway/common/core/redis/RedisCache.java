package com.smallway.common.core.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/**
 * 缓存工具类（去除 Redis 依赖，改为内存实现）
 *
 * @author horzits
 **/
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    private static final class CacheEntry
    {
        private final Object value;
        /** 0 表示不过期 */
        private final long expireAtMs;

        private CacheEntry(Object value, long expireAtMs)
        {
            this.value = value;
            this.expireAtMs = expireAtMs;
        }
    }

    private final ConcurrentMap<String, CacheEntry> store = new ConcurrentHashMap<>();

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        store.put(key, new CacheEntry(value, 0));
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        long ttlMs = timeout == null ? 0 : timeUnit.toMillis(timeout.longValue());
        long expireAt = ttlMs <= 0 ? 0 : System.currentTimeMillis() + ttlMs;
        store.put(key, new CacheEntry(value, expireAt));
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        CacheEntry entry = store.get(key);
        if (entry == null || isExpired(entry))
        {
            store.remove(key);
            return false;
        }
        long ttlMs = unit.toMillis(timeout);
        long expireAt = ttlMs <= 0 ? 0 : System.currentTimeMillis() + ttlMs;
        store.put(key, new CacheEntry(entry.value, expireAt));
        return true;
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key)
    {
        CacheEntry entry = store.get(key);
        if (entry == null || isExpired(entry))
        {
            store.remove(key);
            return -1;
        }
        if (entry.expireAtMs == 0)
        {
            return -1;
        }
        long leftMs = entry.expireAtMs - System.currentTimeMillis();
        return leftMs <= 0 ? -1 : TimeUnit.MILLISECONDS.toSeconds(leftMs);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key)
    {
        CacheEntry entry = store.get(key);
        if (entry == null)
        {
            return false;
        }
        if (isExpired(entry))
        {
            store.remove(key);
            return false;
        }
        return true;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        CacheEntry entry = store.get(key);
        if (entry == null)
        {
            return null;
        }
        if (isExpired(entry))
        {
            store.remove(key);
            return null;
        }
        return (T) entry.value;
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        return store.remove(key) != null;
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public boolean deleteObject(final Collection collection)
    {
        if (collection == null || collection.isEmpty())
        {
            return false;
        }
        boolean removed = false;
        for (Object k : collection)
        {
            if (k != null)
            {
                removed |= (store.remove(String.valueOf(k)) != null);
            }
        }
        return removed;
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        setCacheObject(key, dataList);
        return dataList == null ? 0 : dataList.size();
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key)
    {
        List<T> list = getCacheObject(key);
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> Set<T> setCacheSet(final String key, final Set<T> dataSet)
    {
        // 内存实现不支持 BoundSetOperations，按对象整体存储即可
        setCacheObject(key, dataSet);
        return dataSet;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        return getCacheObject(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        setCacheObject(key, dataMap);
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        return getCacheObject(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        Map<String, Object> map = getCacheObject(key);
        if (map == null)
        {
            map = new ConcurrentHashMap<>();
        }
        map.put(hKey, value);
        setCacheObject(key, map);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        Map<String, T> map = getCacheObject(key);
        if (map == null)
        {
            return null;
        }
        return map.get(hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        Map<String, T> map = getCacheObject(key);
        if (map == null || hKeys == null || hKeys.isEmpty())
        {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        for (Object hk : hKeys)
        {
            if (hk != null)
            {
                result.add(map.get(String.valueOf(hk)));
            }
        }
        return result;
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey)
    {
        Map<String, Object> map = getCacheObject(key);
        if (map == null)
        {
            return false;
        }
        return map.remove(hKey) != null;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern)
    {
        if (pattern == null)
        {
            return Collections.emptyList();
        }
        // 支持最常见的前缀匹配：xxx*；以及全量匹配：*
        if ("*".equals(pattern))
        {
            cleanupExpired();
            return new ArrayList<>(store.keySet());
        }
        if (pattern.endsWith("*"))
        {
            String prefix = pattern.substring(0, pattern.length() - 1);
            cleanupExpired();
            List<String> result = new ArrayList<>();
            for (String k : store.keySet())
            {
                if (k != null && k.startsWith(prefix))
                {
                    result.add(k);
                }
            }
            return result;
        }
        // 兜底：按精确 key 判断
        return hasKey(pattern) ? Collections.singletonList(pattern) : Collections.emptyList();
    }

    private boolean isExpired(CacheEntry entry)
    {
        return entry != null && entry.expireAtMs > 0 && entry.expireAtMs <= System.currentTimeMillis();
    }

    private void cleanupExpired()
    {
        for (Map.Entry<String, CacheEntry> e : store.entrySet())
        {
            if (isExpired(e.getValue()))
            {
                store.remove(e.getKey(), e.getValue());
            }
        }
    }
}
