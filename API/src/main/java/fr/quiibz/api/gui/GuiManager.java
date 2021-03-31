package fr.quiibz.api.gui;

import fr.quiibz.api.API;
import fr.quiibz.api.listeners.IListener;
import fr.quiibz.api.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GuiManager extends IListener {

    /*
     *  FIELDS
     */

    private static Map<UUID, AbstractGuiBuilder<?>> openedGuis;

    /*
     *  CONSTRUCTOR
     */

    public GuiManager() {

        openedGuis = new HashMap<UUID, AbstractGuiBuilder<?>>();

        Bukkit.getScheduler().runTaskTimerAsynchronously(API.get(), () -> {

            openedGuis.forEach((uuid, gui) -> {

                if(gui.update()) {

                    Inventory inv = Bukkit.createInventory(null, 9 * gui.getSize(), gui.getName());
                    Player player = Bukkit.getPlayer(uuid);
                    gui.setContent(player, inv);

                    for(int i = 0; i < inv.getSize(); i++)
                        player.getOpenInventory().setItem(i, inv.getItem(i));
                }
            });

        }, 20, 20);
    }

    /*
     *  METHODS
     */

    @EventHandler
    public void onClick(InventoryClickEvent event){

        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack item = event.getCurrentItem();

        if(ItemUtils.notNull(item)) {

            AbstractGuiBuilder<?> gui = openedGuis.get(player.getUniqueId());

            if(gui != null && gui.getName().equals(inv.getName())) {

                event.setCancelled(true);

                gui.onClick(player, inv, item, event.getClick(), event.getSlot());
            }
        }
    }

    public static void open(AbstractGuiBuilder<?> gui, Player player) {

        open(gui, player, null);
    }

    public static void open(AbstractGuiBuilder<?> gui, Player player, int page) {

        open(gui, player, null, page);
    }

    public static void open(AbstractGuiBuilder<?> gui, Player player, AbstractGuiBuilder<?> lastGui) {

        open(gui, player, lastGui, 1);
    }

    public static void open(AbstractGuiBuilder<?> gui, Player player, AbstractGuiBuilder<?> lastGui, int page) {

        if(lastGui != null)
            gui.setLastGui(lastGui);

        if(gui instanceof AbstractListGuiBuilder)
            ((AbstractListGuiBuilder) gui).setPage(page);

        Inventory inv = Bukkit.createInventory(null, 9 * gui.getSize(), gui.getName());
        gui.setContent(player, inv);

        player.openInventory(inv);

        openedGuis.put(player.getUniqueId(), gui);
    }

    public static void onClose(Player player) {

        openedGuis.remove(player.getUniqueId());
    }
}