package fr.quiibz.lobby;

import fr.quiibz.api.API;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.npc.NPCCallback;
import fr.quiibz.commons.instances.ServerType;
import fr.quiibz.lobby.commands.SpawnCommand;
import fr.quiibz.lobby.feeders.listener.TotalPlayersFeedListener;
import fr.quiibz.lobby.gui.*;
import fr.quiibz.lobby.listeners.ChatListener;
import fr.quiibz.lobby.listeners.InteractListener;
import fr.quiibz.lobby.listeners.PetsListener;
import fr.quiibz.lobby.listeners.PlayerListener;
import fr.quiibz.lobby.pets.PetsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Lobby extends JavaPlugin {

    /*
     *  FIELDS
     */

    private static Lobby instance;

    public int totalPlayers;

    /*
     *  METHODS
     */

    @Override
    public void onEnable() {

        instance = this;

        new PetsManager();

        API api = API.get();
        api.register(this, new SpawnCommand());
        api.register(this, new PlayerListener());
        api.register(this, new InteractListener());
        api.register(this, new ChatListener());
        api.register(this, new PetsListener());

        api.getNPCManager().addNPC(new Location(Bukkit.getWorld("world"), -8.5, 6, 6.5, -90, 0),
                "§6§lDuels",
                UUID.fromString("8882cee6-a8e5-407e-9992-a44bbcb48da6"),
                "ewogICJ0aW1lc3RhbXAiIDogMTU4ODgzNzU2MDEwMywKICAicHJvZmlsZUlkIiA6ICI3ZGEyYWIzYTkzY2E0OGVlODMwNDhhZmMzYjgwZTY4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHb2xkYXBmZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODVhMjVkNDUzODNiYTM2MTRiZmQ2YzJjMDJmZGJhZTYwMTA2NmQ0Njk3MzZiNjVkNTg5YmJjOTQ3MGVhYmY1OSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                "GYN8O5ivfR/bafc+cveWUwCOONU9eBef/zMofNNi5DyC67Dacn+qpccdi/r/u3KhPwl1QZSDixiYbqDaqGJB9r6l9XbQVtqe6r73LzyWCmwfAh17b/qmdfzBAWPa8ua522dR8AO+l1a7Y10fwzgYGwgLMfXig/lkCztxWxHWJK4lMNEjF+KnTs5tYG0LPURm3GP6cSxD/tBh3XqfFC7wp3JkNcXv05h/VWgiFj9PrcGmfTZLurUs0u3eScUMPls01wQo/pPH3Q4hAa/xrYBOlXslXRcSbBbCheE4lZfVLTyzCC0Zn3IrnFv7PtTu34NwKlmofqMipJwDGcUxD7s0OEmAqGexI4R8LGWUp9RF3iwMjrxwX5ZOq7wSOqD/YBybLCg86CcE+FS10JZrsX2fbt/UKFvTh4EnGVQhZfaELJ5U1gKqTE+/KiFHjQsOQZhsbIu7E+b36l2BKoF10TKA64VYh3LvkQx7hHW2S0rPCnUyH/PjcwvHJFOpoOLXRZ3TNJU0KwmPHnM916nWM3+X8wXTnt7spPFGsbMiQ4U2ZOIHZqy+ARa75ZJE5KB+z9uzUmR1omLJxPV216j07sqpPbY9yG5aNPXQAl5/NCLegqEwzjYqOtVRVVCL4rhqJA2d7irln5SBWYOGz6famN2eZKW7w4cs85XVcMEsOQsPbuw=")
                .setCallback(player -> GuiManager.open(new PlayGui(ServerType.DUELS), player));

        api.getNPCManager().addNPC(new Location(Bukkit.getWorld("world"), -7.5, 6, 2.5, -90, 0),
                "§6§lShooter",
                UUID.fromString("fb255fe6-ad12-4644-9fcb-a34a3ad19b08"),
                "ewogICJ0aW1lc3RhbXAiIDogMTU4ODgzNTkzMjQ3MSwKICAicHJvZmlsZUlkIiA6ICIyYzEwNjRmY2Q5MTc0MjgyODRlM2JmN2ZhYTdlM2UxYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOYWVtZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83MjlkMjBkYTY1ZTFmMjQyY2MxM2NmYzM0NmFkYjAyODM2NTY5NDZjNGZiOTUzN2M2MjE2MjMyY2Q3Yjc3OGY2IgogICAgfQogIH0KfQ==",
                "p2rqvf6susokjmNVb7BwRKg/CWUqrtSGq1Ss4rPW8y6cI+9zm2qwLoI1Id+d/roibwMs6MwTpLFGAqNqxtc/0EhbOkmVd3eoctKE/EQxphSLo8RtrDgUq2lTJVWw4C6CZXuCN0ZIY4W/NvJkKjvDwXloR+LJ6wdCJFj+sv/59kVppzlHYBseK3CEYfxaQlCfyMkisU7LnzGuPcEs9Eo2DV0esvPrtuFAV3ZDQaXJ19M4MNKEx0ryjq4ewuhcoNN88tvLWsSoUbGcvrw06Ds0XgqntE2szw6lQqkMY87XEsjtXg2o5yTvndrEbRnA+a4qUJCz8x3yfVyMHUErPsdcjWLrXRskcA2fNwT1EhRigGm3589xMw3m0djXsUmDoI0dxfHOgVyh5fLCQRJX3SvK++T8gWd2yu9GaXmtXDexS8TBz1VgfI0UzJUfB2hlC3Hih3CHQx7ss3aSNUYeLdDn53OYcj6id68onHGf6k0RAI9lEip2k0v7tD6f4JioVs9TkXyMKXy3+z2D1g740/1w44jXHdqXdZhRnHgSdnHp7YG9Ycn6lhEXOnakWyY6kws2X42ZOaBkmcRUJopv+jocSFe7MkD2GN+cafmkzTjKJIq46HKD0rkByxK/MspBLg0zMRFNkBrRHHM2buLpwf3VeuHEcv7MhvnbEQI9EHLWmlI=")
                .setCallback(player -> GuiManager.open(new PlayGui(ServerType.SHOOTER), player));

        api.getNPCManager().addNPC(new Location(Bukkit.getWorld("world"), -8.5, 6, -1.5, -90, 0),
                "§6§lCTF",
                UUID.fromString("ca1c27c9-c465-48f0-a769-7842334ba31d"),
                "ewogICJ0aW1lc3RhbXAiIDogMTU4ODgzMjc1OTgxOSwKICAicHJvZmlsZUlkIiA6ICI0NDAzZGM1NDc1YmM0YjE1YTU0OGNmZGE2YjBlYjdkOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJGbGF3Q3JhQm90MDEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA5ODkyOTRlYmQ2OWY1YjEyOTcxMmM4MjJmMDAxYzlmZDkzZjc0NWYyODJmYmE5YmZhOTgxMGFhN2JkNmEyYiIKICAgIH0KICB9Cn0=",
                "r+UNuEr+Ks/tXzvRhal+1xnMNLFlvv8Ja8jllZKpYhDk9NiAXaS+ba9A5UUcao04nBzNsqH+k7C/UAT/+8fAhtc7yIzNFQh+kLNchL/uEVPwjdB+M+wDJ7dUNNJvp+O+h8ZVzR9hrMtag1smbI3TOZbDsG6Bl6ptTrpPCQjUQDjPKjocfJHJuOsUA65mP+FIc61sLDZuVs/tLfqQZGKElaTpX4b/5Fe9SHJoXOUiXKQd399ExvrAFE+Xq7v+brfnrNwHOhjvLbeuyommRzu1H0TPwmkWjJFaudGyWK8LASBjOls6Jh0hCxjem8R+K+52AsSkY133iYf78tK4l0BIceJmcuahBmxQag/22Yx1p0p1iRhPXNQ6HJxArbTfve4RKXHYSJESx4DVapu9ReRibX2n2rgVfPgc9EDdhskbX9+3YkXGgmxSCJw6kcODYHk53D3AhBMf+JulbGIwjBRkmjaD0MGf/cSIJj3PX/wZpgaoKKkI0qDA3egJVXoVaBuadHhINpq7OJR04h4Xy9qKybltUW0GQwBTXvQFwc/NC60xy/c/qSdLmpHrEoMPgFxsuHUAwIMY8MkUWP2EJgiWBGqiGbktd5P1ZjK4Qw5x3BZarDSBdUuzbuiI7uS/mBnuyW7LMMtpfJRKfYM4Aqd3smRVE2fhl62DI3YwPemE1jM=")
                .setCallback(player -> GuiManager.open(new PlayGui(ServerType.CTF), player));

        new TotalPlayersFeedListener(this);
    }

    public void setTotalPlayers(int totalPlayers) {

        this.totalPlayers = totalPlayers;
    }

    public int getTotalPlayers() {

        return this.totalPlayers;
    }

    public static Lobby get() {

        return instance;
    }
}
