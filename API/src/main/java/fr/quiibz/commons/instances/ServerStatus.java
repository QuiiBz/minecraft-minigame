package fr.quiibz.commons.instances;

public enum ServerStatus {

    BOOTING("§cDémarrage...", 1),
    WHITELISED("§dWhitelist", 13),
    ONLINE("§aOuvert", 10),
    PLAYING("§bEn jeu", 12),
    CLOSING("§6Arrêt...", 14),
    ;

    /*
     *  FIELDS
     */

    private String name;
    private int color;

    /*
     *  CONSTRUCTOR
     */

    ServerStatus(String name, int color) {

        this.name = name;
        this.color = color;
    }

    /*
     *  METHODS
     */

    public String getName() {

        return this.name;
    }

    public int getColor() {

        return this.color;
    }
}
