package fr.quiibz.api.npc;

import com.mojang.authlib.GameProfile;
import fr.quiibz.api.npc.nms.VoidPlayerConnection;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class NPC extends EntityPlayer {

    /*
     *  FIELDS
     */

    private NPCCallback callback;

    /*
     *  CONSTRUCTOR
     */

    public NPC(World world, GameProfile profile) {

        super(world.getServer().getServer(), (WorldServer) world, profile, new PlayerInteractManager(world));

        playerInteractManager.b(WorldSettings.EnumGamemode.SURVIVAL);
        this.playerConnection = new VoidPlayerConnection(world.getServer().getServer(), new VoidPlayerConnection.NPCNetworkManager(), this);
    }

    /*
     *  METHODS
     */

    public void setCallback(NPCCallback callback) {

        this.callback = callback;
    }

    public void runCallback(Player player) {

        this.callback.call(player);
    }

    @Override
    public void attack(Entity entity) {

    }

    @Override
    public void d(Entity entity) {

    }

    @Override
    public void setPlayerWeather(WeatherType type, boolean plugin) {

    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {

        if(damagesource.getEntity() == null || !(damagesource.getEntity() instanceof EntityPlayer))
            return true;

        EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(damagesource.getEntity().getBukkitEntity(), this.getBukkitEntity(), EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0);
        entityDamageByEntityEvent.setCancelled(true);
        Bukkit.getPluginManager().callEvent(entityDamageByEntityEvent);

        return true;
    }
}
