package usst.spm.service;


import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;
    private final RedissonClient redissonClient;
    private static final Logger logger = Logger.getLogger(RedisService.class.getName());

    @Autowired
    public RedisService(
            RedisTemplate<String, Object> redisTemplate,
            RedisTemplate<String, String> stringRedisTemplate, RedissonClient redissonClient) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
    }

    // redis设置值
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // redis设置值并设置过期时间
    public void setValue(String key, Object value, long timeout) {
        Duration duration = Duration.ofSeconds(timeout);
        redisTemplate.opsForValue().set(key, value, duration);
    }

    // redis获取值
    public Object getValue(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.warning("Failed to get value from redis: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // 获取键的剩余过期时间
    public Long getKeyExpiry(String key) {
        try {
            return redisTemplate.getExpire(key); // 返回键值的剩余过期时间，单位为秒
        } catch (Exception e) {
            logger.warning("Failed to get key expiry from redis: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // 设置值并在键不存在时返回 true，否则返回 false
    public boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
        return Boolean.TRUE.equals(result);
    }

    // 删除值
    public void removeValue(String key) {
        redisTemplate.delete(key);
    }

    // 获取哈希值
    public Object getHashValue(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    // 设置哈希值
    public void setHashValue(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    // 删除哈希值
    public void removeHashValue(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    // 判断键是否存在
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // 发布消息
    public void publish(String channel, String message) {
        try {
            stringRedisTemplate.convertAndSend(channel, message);
        } catch (Exception e) {
            logger.warning("Failed to publish message to redis: " + e.getMessage());
        }
    }

    // 创建布隆过滤器
    public void createBloomFilter(String bloomFilterName, long expectedInsertions, double falseProbability) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
        // 初始化布隆过滤器
        bloomFilter.tryInit(expectedInsertions, falseProbability);
    }

    // 创建或获取布隆过滤器
    public RBloomFilter<String> getBloomFilter(String filterName) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(filterName);
        if (!bloomFilter.isExists()) {
            bloomFilter.tryInit(10000L, 0.01); // 初始化布隆过滤器，指定期望插入的数量和误报率
        }
        return bloomFilter;
    }

    // 向布隆过滤器添加元素
    public void addToBloomFilter(String filterName, String value) {
        RBloomFilter<String> bloomFilter = getBloomFilter(filterName);
        bloomFilter.add(value);
    }

    // 检查布隆过滤器中是否存在元素
    public boolean checkBloomFilter(String filterName, String value) {
        RBloomFilter<String> bloomFilter = getBloomFilter(filterName);
        return bloomFilter.contains(value);
    }

    // 示例：布隆锁的实现，防止并发操作
    public void performActionWithBloomLock(String filterName, String value) {
        if (checkBloomFilter(filterName, value)) {
            logger.info("Value already exists in Bloom Filter. Skipping action.");
            return;
        }

        synchronized (this) {
            if (!checkBloomFilter(filterName, value)) {
                // 添加到布隆过滤器
                addToBloomFilter(filterName, value);

                // 执行某个操作
                logger.info("Executing action for value: " + value);
            }
        }
    }
}