package fr.quiibz.api.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import fr.quiibz.api.utils.PacketHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class NPCManager {

    /*
     *  FIELDS
     */

    private List<NPC> npc;

    /*
     *  CONSTRUCTOR
     */

    public NPCManager() {

        this.npc = new ArrayList<NPC>();
    }

    /*
     *  METHODS
     */

    public NPC addNPC(Location location, String name, UUID uuid, String value, String signature) {

        World world = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile profile = this.feedProfile(name, uuid, value, signature);

        NPC npc = new NPC(world, profile);
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        npc.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        world.addEntity(npc, CreatureSpawnEvent.SpawnReason.CUSTOM);

        this.npc.add(npc);

        return npc;
    }

    private GameProfile feedProfile(String name, UUID uuid, String value, String signature) {

        GameProfile profile = new GameProfile(uuid, name);
        profile.getProperties().put("textures", new Property("textures", value, signature));

        return profile;
    }

    public void onJoin(Player player) {

        this.npc.forEach(npc -> {

            PacketHelper.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            PacketHelper.sendPacket(player, new PacketPlayOutNamedEntitySpawn(npc));
            PacketHelper.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
        });
    }
}

