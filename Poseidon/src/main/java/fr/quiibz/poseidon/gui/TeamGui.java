package fr.quiibz.poseidon.gui;

import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.poseidon.game.AbstractTeamGameManager;
import fr.quiibz.poseidon.game.GameTeam;
import fr.quiibz.poseidon.players.AbstractTeamPlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.redisson.api.RBucket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TeamGui extends AbstractGuiBuilder<AbstractTeamGameManager> {

    /*
     *  CONSTRUCTOR
     */

    public TeamGui(AbstractTeamGameManager gameManager) {

        super(gameManager);
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Equipe";
    }

    @Override
    public int getSize() {

        return 4;
    }

    @Override
    public boolean update() {

        return true;
    }

    @Override
    public byte getGlassMeta() {

        return 8;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        Map<GameTeam, List<String>> teams = this.getSettings().toTeams();

        AtomicInteger slot = new AtomicInteger(11);

        teams.entrySet().forEach(entry -> {

            GameTeam team = entry.getKey();
            List<String> players = entry.getValue();
            List<String> lore = new ArrayList<String>();
            lore.add(" ");
            players.forEach(current -> lore.add(Constants.ROUND + "§f" + current));
            lore.add(" ");
            lore.add(players.contains(player.getName()) ? " §f§l» §aSélectionné" : " §f§l» §eClic pour sélectionner");

            inv.setItem(slot.getAndIncrement(), new ItemBuilder(Material.BANNER, players.size(), (byte) team.getColor()).setName("§7Equipe " + team.getName()).setLore(lore).toItemStack());
        });

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.startsWith("§7Equipe ")) {

            GameTeam team = GameTeam.resolve(name.replace("§7Equipe ", ""));
            AbstractTeamPlayerData playerData = (AbstractTeamPlayerData) this.getSettings().getData(player);

            playerData.setTeam(team);

            player.closeInventory();
            player.sendMessage(Constants.PREFIX + "§eVous avez sélectionné l'équipé " + team.getName());
        }

        super.onClick(player, inv, item, click, slot);
    }
}
