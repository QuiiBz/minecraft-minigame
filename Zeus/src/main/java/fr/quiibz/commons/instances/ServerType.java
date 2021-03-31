package fr.quiibz.commons.instances;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum ServerType {

    LOBBY("Lobby", Arrays.asList("default")),
    DUELS("Duels", Arrays.asList("default")),
    SHOOTER("Shooter", Arrays.asList("Temple")),
    CTF("CaptureTheFlag", Arrays.asList("default")),
    ;

    /*
     *  FIELDS
     */

    private String name;
    private List<String> maps;

    /*
     *  CONSTRUCTOR
     */

    ServerType(String name, List<String> maps) {

        this.name = name;
        this.maps = maps;
    }

    /*
     *  METHODS
     */

    public String getName() {

        return this.name;
    }

    public String findMap() {

        return this.maps.get(new Random().nextInt(this.maps.size()));
    }

    public static ServerType getByName(String name) {

        for(ServerType current :values()) {

            if(current.getName().startsWith(name))
                return current;
        }

        return null;
    }
}
