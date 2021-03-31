package fr.quiibz.apollon.data.mongo;

public class MongoCredentials {

    private String name;
    private String user;
    private String password;
    private String database;
    private String host;
    private int port;

    public MongoCredentials(String name, String user, String password, String database, String host, int port) {

        this.name = name;
        this.user = user;
        this.password = password;
        this.database = database;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return this.name;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public String getDatabase() {
        return this.database;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }
}

