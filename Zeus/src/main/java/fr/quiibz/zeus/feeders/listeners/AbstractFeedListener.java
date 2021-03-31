package fr.quiibz.zeus.feeders.listeners;

import fr.quiibz.apollon.data.redis.RedisAccess;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

public abstract class AbstractFeedListener<T> implements IFeedListener<T> {

    /*
     *  FIELDS
     */

    private String name;
    private RTopic<T> topic;

    /*
     *  CONSTRUCTOR
     */

    public AbstractFeedListener() {

        this.name = this.getName() + "Feeder";

        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();

        this.topic = redissonClient.getTopic(this.name);

        this.topic.addListener((message, object) -> this.listen(object));
    }
}
