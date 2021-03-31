package fr.quiibz.poseidon.gui;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.quiibz.api.API;
import fr.quiibz.api.data.mongo.MongoManager;
import fr.quiibz.api.gui.AbstractGuiBuilder;
import fr.quiibz.api.gui.GuiManager;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.ItemBuilder;
import fr.quiibz.commons.accounts.Account;
import fr.quiibz.commons.instances.ServerInstance;
import fr.quiibz.commons.instances.ServerStatus;
import fr.quiibz.poseidon.Poseidon;
import fr.quiibz.poseidon.config.AbstractConfig;
import fr.quiibz.poseidon.config.GameConfig;
import fr.quiibz.poseidon.config.IConfig;
import fr.quiibz.poseidon.game.GameState;
import fr.quiibz.poseidon.game.IGameManager;
import javafx.print.PageLayout;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.redisson.api.RBucket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ConfigurationGui extends AbstractGuiBuilder {

    /*
     *  FIELDS
     */

    private IGameManager gameManager;
    private IConfig config;

    /*
     *  METHODS
     */

    public ConfigurationGui(IGameManager gameManager) {

        this.gameManager = gameManager;
        this.config = gameManager.getConfig();
    }

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "§8Menu des configurations";
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

        return 1;
    }

    @Override
    public void setContent(Player player, Inventory inv) {

        MongoCollection<GameConfig> mongoCollection = MongoManager.get().getCollection("configs", GameConfig.class);
        FindIterable<GameConfig> result = mongoCollection.find(Filters.eq("owner", player.getUniqueId()));

        AtomicInteger slot = new AtomicInteger(11);

        result.forEach((Consumer<GameConfig>) config -> {

            List<String> lore = new ArrayList<String>();
            lore.add(" ");
            lore.add(Constants.ROUND + "§7Jeu : §6§l" + config.getServerType().getName());
            lore.add(Constants.ROUND + "§7Nom : §a" + config.getName());
            lore.add(" ");
            lore.add(Constants.PREFIX + "§eConfiguration :");

            config.getItems().forEach((key, value) -> {

                lore.add(Constants.ROUND + "§7" + key + " : §f" + value);
            });

            lore.add(" ");
            lore.add(" §f§l» §eClic-gauche : §aCharger");
            lore.add(" §f§l» §eClic-droit : §cSupprimer");

            inv.setItem(slot.getAndIncrement(), new ItemBuilder(config.getServerType().toItemStack()).setName("§6§l" + config.getName()).setLore(lore).toItemStack());
        });

        inv.setItem(3, new ItemBuilder(Material.STORAGE_MINECART).setName("§6§lMes configurations").setLore(
                " ",
                " §f§l» §aSélectionné").toItemStack());
        inv.setItem(5, new ItemBuilder(Material.SLIME_BALL).setName("§a§lSauvegarder cette configuration").setLore(
                " ",
                " §f§l» §eClic pour sauvegarder").toItemStack());

        super.setContent(player, inv);
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack item, ClickType click, int slot) {

        String name = item.getItemMeta().getDisplayName();

        if(name.equals("§a§lSauvegarder cette configuration")) {

            new AnvilGUI.Builder()
                    .onComplete((p, configName) -> {

                        AbstractConfig config = (AbstractConfig) this.gameManager.getConfig();
                        GameConfig toSave = config.save();
                        toSave.setServerType(API.get().getServerInstance().getServerType());
                        toSave.setName(configName);
                        toSave.setOwner(player.getUniqueId());

                        MongoCollection<GameConfig> mongoCollection = MongoManager.get().getCollection("configs", GameConfig.class);
                        mongoCollection.deleteOne(Filters.eq("name", configName));
                        mongoCollection.insertOne(toSave);

                        player.sendMessage(Constants.PREFIX + "§eLa configuration a bien été sauvegardé.");
                        GuiManager.open(this, player);
                        return AnvilGUI.Response.close();
                    })
                    .text("Ma configuration")
                    .item(new ItemStack(Material.PAPER))
                    .plugin(Poseidon.get())
                    .open(player);

        } else if(name.startsWith("§6§l")) {

            name = name.replace("§6§l", "");

            if(click.equals(ClickType.LEFT)) {

                MongoCollection<GameConfig> mongoCollection = MongoManager.get().getCollection("configs", GameConfig.class);
                GameConfig config = mongoCollection.find(Filters.eq("name", name)).first();

                if(config != null) {

                    if(config.getServerType().equals(API.get().getServerInstance().getServerType())) {

                        this.gameManager.getConfig().load(config);
                        GuiManager.open(new ConfigGui(this.gameManager), player);

                        player.sendMessage(Constants.PREFIX + "§eLa configuration a bien été chargée.");

                    } else
                        player.sendMessage(Constants.PREFIX + "§cCette configuration n'est pas disponible pour ce jeu.");
                }

            } else if(click.equals(ClickType.RIGHT)) {

                MongoCollection<GameConfig> mongoCollection = MongoManager.get().getCollection("configs", GameConfig.class);
                mongoCollection.deleteOne(Filters.eq("name", name));

                // set default config
                this.config.setItems(this.gameManager);
                this.config.setName(null);
                this.config.setOwner(null);

                player.sendMessage(Constants.PREFIX + "§eLa configuration a bien été supprimé.");
            }
        }

        super.onClick(player, inv, item, click, slot);
    }
}
