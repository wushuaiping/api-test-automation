package io.idwangmo.testing.util;

import io.idwangmo.testing.model.config.RedisConfig;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.Objects;

/**
 * @author Chengmingyuan
 * @since 1.0
 **/
public class RedisClientUtil {
    private static RedisClientUtil ourInstance;
    private static volatile StatefulRedisConnection<String, String> redisClient;

    private RedisClientUtil() {
    }

    public static RedisClientUtil getInstance() {
        if (Objects.isNull(ourInstance)) {
            synchronized (RedisClientUtil.class) {
                if (Objects.isNull(ourInstance)) {
                    ourInstance = new RedisClientUtil();
                }
            }
        }
        return ourInstance;

    }


    /**
     * redis 连接配置
     */
    static {
        RedisConfig redisConfig = TestingUtil.getTestingConfig().getRedis();

        RedisURI redisURI = RedisURI.create(redisConfig.getHost(), redisConfig.getPort());
        redisClient = RedisClient.create(redisURI).connect();
    }

    public RedisCommands<String, String> getRedisClient() {

        return redisClient.sync();

    }

}
