package fr.quiibz.zeus.feeders;

import fr.quiibz.apollon.data.redis.RedisAccess;
import org.redisson.RedissonShutdownException;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

public abstract class AbstractFeeder<T> implements IFeeder<T> {

    /*
     *  FIELDS
     */

    private String name;
    private RTopic<T> topic;

    /*
     *  CONSTRUCTOR
     */

    public AbstractFeeder() {

        this.name = this.getName() + "Feeder";

        RedisAccess redisAccess = RedisAccess.getInstance();
        RedissonClient redissonClient = redisAccess.getClient();

        this.topic = redissonClient.getTopic(this.name);
    }

    /*
     *  METHODS
     */

    @Override
    public void publish() {

        try {

            this.topic.publish(this.feed());

        } catch (RedissonShutdownException e) { }
    }
}
