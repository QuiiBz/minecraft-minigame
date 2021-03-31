package fr.quiibz.commons.accounts;

public enum Rank {

    ADMIN(100, 0,"ADMIN", "§c", "§f"),
    RESP(90, 1,"RESP", "§6", "§f"),
    SMOD(80, 2,"S-MOD", "§9", "§f"),
    MOD(70, 3,"MOD", "§9", "§f"),
    HELPER(60, 4, "HELPER", "§b", "§f"),
    PLAYER(0, 5, null, "§7", "§7"),
    ;

    /*
     *  FIELDS
     */

    private int power;
    private int order;
    private String name;
    private String color;
    private String chatColor;

    /*
     *  CONSTRUCTOR
     */

    Rank(int power, int order, String name, String color, String chatColor) {

        this.order = order;
        this.power = power;
        this.name = name;
        this.color = color;
        this.chatColor = chatColor;
    }

    /*
     *  METHODS
     */

    public int getPower() {

        return this.power;
    }

    public String getOrder() {

        return String.valueOf(this.order);
    }

    public String getColor() {

        return this.color;
    }

    public String getChatColor() {

        return this.chatColor;
    }

    public String getPrefix() {

        return this == Rank.PLAYER ? this.color : this.color + "§l" + this.name + " ";
    }

    public String getName() {

        return this.name;
    }

    public static Rank getRank(int power) {

        for(Rank current : values()) {

            if(current.getPower() == power)
                return current;
        }

        return Rank.PLAYER;
    }
}
