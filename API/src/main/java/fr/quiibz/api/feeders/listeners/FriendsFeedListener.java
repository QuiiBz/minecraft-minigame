package fr.quiibz.api.feeders.listeners;

import fr.quiibz.api.accounts.AccountManager;
import fr.quiibz.api.utils.PlayerUtils;
import fr.quiibz.commons.accounts.Rank;
import fr.quiibz.commons.topic.FriendsTopic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FriendsFeedListener extends AbstractFeedListener<FriendsTopic> {

    /*
     *  METHODS
     */

    @Override
    public String getName() {

        return "friends";
    }

    @Override
    public void listen(FriendsTopic topic) {

        Player player = Bukkit.getPlayer(topic.getUUID());
        Player target = Bukkit.getPlayer(topic.getTarget());

        if(PlayerUtils.exist(player))
            this.process(player, topic.getTarget(), topic.getTargetName(), topic.getAction());

        if(PlayerUtils.exist(target))
            this.process(target, topic.getUUID(), topic.getName(), topic.getAction());
    }

    private void process(Player player, UUID target, String targetName, int action) {

        if(action == 0)
            AccountManager.get().getAccount(player).getFriends().put(targetName, target);
        else if(action == 1)
            AccountManager.get().getAccount(player).getFriends().remove(targetName);
    }
}
