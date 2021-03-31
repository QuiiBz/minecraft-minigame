package fr.quiibz.commons.accounts;

public enum PetsType {

    NONE("Aucun", null),
    SLIME("Slime", "kobyjo"),
    PIG("Cochon", "Pork"),
    SHEEP("Mouton", "Kolish"),
    SHARK("Requin", "LeftShark"),
    CREEPER("Creeper", "Fej"),
    MONEY("Sac de coins", "MrSnowDK"),
    ;

    /*
     *  FIELDS
     */

    private String name;
    private String owner;

    /*
     *  CONSTRUCTOR
     */

    PetsType(String name, String owner) {

        this.name = name;
        this.owner = owner;
    }

    /*
     *  METHODS
     */

    public String getName() {

        return this.name;
    }

    public String getOwner() {

        return this.owner;
    }

    public static PetsType resolve(String name) {

        for(PetsType petsType : values()) {

            if(petsType.getName().equals(name))
                return petsType;
        }

        return null;
    }
}
