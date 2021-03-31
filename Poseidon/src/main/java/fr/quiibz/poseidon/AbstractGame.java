package fr.quiibz.poseidon;

import fr.quiibz.api.API;
import fr.quiibz.api.data.mongo.MongoManager;
import fr.quiibz.api.scoreboards.IBoardManager;
import fr.quiibz.poseidon.commands.ForceStartCommand;
import fr.quiibz.poseidon.commands.ReCommand;
import fr.quiibz.poseidon.config.AbstractConfig;
import fr.quiibz.poseidon.config.GameConfig;
import fr.quiibz.poseidon.game.IGameManager;
import fr.quiibz.poseidon.gui.ConfigGui;
import fr.quiibz.poseidon.listeners.GameListener;
import fr.quiibz.poseidon.listeners.InteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractGame extends JavaPlugin {

    /*
     *  FIELDS
     */

    private AbstractGame instance;
    protected IGameManager gameManager;
    protected IBoardManager boardManager;

    /*
     *  METHODS
     */

    @Override
    public void onEnable() {

        instance = this;

        new MongoManager(GameConfig.class);

        this.gameManager = this.getGameManager();
        this.boardManager = this.getBoardManager();

        API api = API.get();
        api.register(this, new GameListener(this.gameManager, this.boardManager));
        api.register(this, new InteractListener(this.gameManager, this.getConfigGui()));
        api.register(Poseidon.get(), new ReCommand());
        api.register(Poseidon.get(), new ForceStartCommand(this.gameManager));

        api.setMaxPlayers(this.gameManager.getMaxPlayers());

        this.enable(api);
    }

    public abstract void enable(API api);
    public abstract IGameManager getGameManager();
    public abstract IBoardManager getBoardManager();
    public abstract ConfigGui getConfigGui();

    public AbstractGame getInstance() {

        return this.instance;
    }
}
