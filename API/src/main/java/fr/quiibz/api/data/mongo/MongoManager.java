package fr.quiibz.api.data.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoManager {

    private static MongoDatabase mongoDatabase;

    public MongoManager(Class... clazz) {

        this.loadConnection(clazz);
    }

    private void loadConnection(Class... clazz) {

        MongoCredentials mongoCredentials = new MongoCredentials("kerion", "kerion", "", "network", "", 27017);

        mongoDatabase = new MongoConnection(mongoCredentials).getMongoClient().getDatabase(mongoCredentials.getDatabase()).withCodecRegistry(
                CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().register(clazz).build())));
    }

    public static MongoDatabase get() {

        return mongoDatabase;
    }
}
