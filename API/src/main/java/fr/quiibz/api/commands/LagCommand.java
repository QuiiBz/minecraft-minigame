package fr.quiibz.api.commands;

import fr.quiibz.api.API;
import fr.quiibz.api.utils.Constants;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class LagCommand extends ICommand {

    /*
     *  METHODS
     */

    @Override
    public void perform(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        API api = API.get();

        double[] tps = MinecraftServer.getServer().recentTps;
        String[] tpsAvg = new String[tps.length];

        for(int i = 0; i < tps.length; i++)
            tpsAvg[i] = format(tps[i]);

        player.sendMessage(" ");
        player.sendMessage(Constants.PREFIX + "§eInformations du serveur :");
        player.sendMessage(" ");
        player.sendMessage(Constants.ROUND + "§7Date : " + api.getServerInfo().split(" ")[0].replace("§7", "§f"));
        player.sendMessage(Constants.ROUND + "§7Serveur : §b" + api.getServerInstance().getServerType().getName() + " #" + api.getServerInstance().getServerId());
        player.sendMessage(Constants.ROUND + "§7Lag serveur : " + StringUtils.join(tpsAvg, ", "));
        player.sendMessage(Constants.ROUND + "§7Lag client : §a" + ((CraftPlayer) player).getHandle().ping + "ms");
        player.sendMessage(" ");
    }

    @Override
    public String getCommand() {

        return "ping";
    }

    private String format(double tps) {

        return ( ( tps > 18.0 ) ? ChatColor.GREEN : ( tps > 16.0 ) ? ChatColor.YELLOW : ChatColor.RED ).toString()
                + ( ( tps > 20.0 ) ? "*" : "" ) + Math.min( Math.round( tps * 100.0 ) / 100.0, 20.0 );
    }
}
