package fr.quiibz.apollon.data.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

public class RedisAccess {

    /*
     *  FIELDS
     */

    private static RedisAccess instance;

    private RedissonClient client;

    /*
     *  CONSTRUCTOR
     */

    public RedisAccess(RedisCredentials credentials) {

        this.client = this.initRedisson(credentials);

        instance = this;
    }

    /*
     *  METHODS
     */

    public RedissonClient initRedisson(RedisCredentials credentials) {

        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.setUseLinuxNativeEpoll(true);
        config.setThreads(4);
        config.setNettyThreads(4);
        config.useSingleServer()
                .setConnectionPoolSize(5)
                .setAddress(credentials.toURI())
                .setPassword(credentials.getPassword())
                .setDatabase(1)
                .setClientName(credentials.getClient());

        return Redisson.create(config);
    }

    public static void init() {

        new RedisAccess(new RedisCredentials("", "", 6379));
    }

    public static void close() {

        RedisAccess.getInstance().getClient().shutdown();
    }

    public RedissonClient getClient() {

        return this.client;
    }

    public static RedisAccess getInstance() {

        return instance;
    }
}
