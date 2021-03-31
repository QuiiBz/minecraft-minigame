package fr.quiibz.poseidon.game;

public enum GameTeam {

    NONE("Aucune", 15),
    BLUE("§9Bleu", 4),
    RED("§cRouge", 1),
    ;

    /*
     *  FIELDS
     */

    private String name;
    private int color;

    /*
     *  CONSTRUCTOR
     */

    GameTeam(String name, int color) {

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

    public GameTeam inverse() {

        return (this.equals(GameTeam.RED) ? GameTeam.BLUE : GameTeam.RED);
    }

    public static GameTeam resolve(String name) {

        for(GameTeam team : values()) {

            if(team.getName().equals(name))
                return team;
        }

        return null;
    }
}
