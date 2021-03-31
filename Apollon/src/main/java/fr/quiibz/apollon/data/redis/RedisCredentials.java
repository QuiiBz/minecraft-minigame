package fr.quiibz.apollon.data.redis;

public class RedisCredentials {

    /*
     *  FIELDS
     */

    private String ip;
    private String password;
    private int port;
    private String client;

    /*
     *  CONSTRUCTOR
     */

    public RedisCredentials(String ip, String password, int port) {

        this(ip, password, port, "Apollon");
    }

    public RedisCredentials(String ip, String password, int port, String client) {

        this.ip = ip;
        this.password = password;
        this.port = port;
        this.client = client;
    }

    /*
     *  METHODS
     */

    public String toURI() {

        StringBuilder stringBuilder = new StringBuilder()
                .append(this.ip)
                .append(":")
                .append(this.port);

        return stringBuilder.toString();
    }

    public String getIp() {

        return this.ip;
    }

    public String getPassword() {

        return this.password;
    }

    public int getPort() {

        return this.port;
    }

    public String getClient() {

        return this.client;
    }
}
