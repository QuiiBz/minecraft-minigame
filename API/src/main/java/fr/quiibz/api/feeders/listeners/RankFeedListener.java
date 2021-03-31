package fr.quiibz.api.feeders.listeners;

import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.utils.Constants;
import fr.quiibz.api.utils.PlayerUtils;
import fr.quiibz.commons.accounts.Rank;
import fr.quiibz.commons.topic.RankTopic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RankFeedListener extends AbstractFeedListener<RankTopic> {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "rank";
    }

    @Override
    public void listen(RankTopic topic) {

        Player player = Bukkit.getPlayer(topic.getUUID());
        Rank rank = topic.getRank();

        if(PlayerUtils.exist(player)) {

            AccountManager.get().getAccount(player).setRank(rank);
            player.sendMessage(" ");
            player.sendMessage(Constants.PREFIX + "§eVous avez reçu le grade " + rank.getColor() + rank.getName());
            player.sendMessage(" ");
        }
    }
}
