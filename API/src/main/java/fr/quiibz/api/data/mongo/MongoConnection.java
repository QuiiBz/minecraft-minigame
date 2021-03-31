package fr.quiibz.api.data.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;

public class MongoConnection {

    private String name;
    private MongoClient mongoClient;

    public MongoConnection(MongoCredentials mongoCredentials) {

        this.name = mongoCredentials.getName();

        MongoCredential credential = MongoCredential.createCredential(mongoCredentials.getUser(), mongoCredentials.getDatabase(), mongoCredentials.getPassword().toCharArray());
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(new CodecRegistry[]{MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromProviders(new CodecProvider[]{PojoCodecProvider.builder().automatic(true).build()})});
        MongoClientSettings.builder().codecRegistry(pojoCodecRegistry).build();

        this.mongoClient = new MongoClient(new ServerAddress(mongoCredentials.getHost(), mongoCredentials.getPort()), Arrays.asList(credential));
    }

    public String getName() {
        return this.name;
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }
}
