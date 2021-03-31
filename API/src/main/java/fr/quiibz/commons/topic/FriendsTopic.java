package fr.quiibz.commons.topic;

import java.util.UUID;

public class FriendsTopic {

    /*
     *  FIELDS
     */

    private String name;
    private UUID uuid;
    private String targetName;
    private UUID target;
    private int action;

    /*
     *  CONSTRUCTOR
     */

    public FriendsTopic() { }

    public FriendsTopic(String name, UUID uuid, String targetName, UUID target, int action) {

        this.name = name;
        this.uuid = uuid;
        this.targetName = targetName;
        this.target = target;
        this.action = action;
    }

    /*
     *  METHODS
     */

    public String getName() {

        return this.name;
    }

    public UUID getUUID() {

        return this.uuid;
    }

    public String getTargetName() {

        return this.targetName;
    }

    public UUID getTarget() {

        return this.target;
    }

    public int getAction() {

        return this.action;
    }
}
