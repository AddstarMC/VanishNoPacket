package org.kitteh.vanish;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.metadata.LazyMetadataValue.CacheStrategy;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.hooks.HookManager;
import org.kitteh.vanish.hooks.HookManager.HookType;
import org.kitteh.vanish.listeners.ListenEntity;
import org.kitteh.vanish.listeners.ListenInventory;
import org.kitteh.vanish.listeners.ListenPlayerJoin;
import org.kitteh.vanish.listeners.ListenPlayerMessages;
import org.kitteh.vanish.listeners.ListenPlayerOther;
import org.kitteh.vanish.listeners.ListenServerPing;
import org.kitteh.vanish.listeners.ListenToYourHeart;

import au.com.addstar.bc.BungeeChat;

import java.io.File;
import java.util.HashSet;

public final class VanishPlugin extends JavaPlugin {
    private final HashSet<String> haveInventoriesOpen = new HashSet<String>();
    private final String latestVersion = null;
    private VanishManager manager;
    private final HookManager hookManager = new HookManager(this);

    /**
     * Informs VNP that a user has closed their fake chest
     *
     * @param name user's name
     */
    public void chestFakeClose(String name) {
        synchronized (this.haveInventoriesOpen) {
            this.haveInventoriesOpen.remove(name);
        }
    }

    /**
     * Queries if a user is currently using a fake chest
     *
     * @param name the user's name
     * @return true if currently using a fake chest
     */
    public boolean chestFakeInUse(String name) {
        synchronized (this.haveInventoriesOpen) {
            return this.haveInventoriesOpen.contains(name);
        }
    }

    /**
     * Informs VNP that a user has opened their fake chest
     *
     * @param name user's name
     */
    public void chestFakeOpen(String name) {
        synchronized (this.haveInventoriesOpen) {
            this.haveInventoriesOpen.add(name);
        }
    }

    /**
     * Gets the current version
     *
     * @return version of VanishNoPacket in use
     */
    public String getCurrentVersion() {
        return this.getDescription().getVersion();
    }

    /**
     * Gets the hook manager
     *
     * @return the hook manager
     */
    public HookManager getHookManager() {
        return this.hookManager;
    }

    /**
     * Gets the latest found version
     * Will show this version, if update checks are disabled
     *
     * @return the latest found version of VanishNoPacket
     */
    public String getLatestKnownVersion() {
        return this.latestVersion;
    }

    /**
     * Gets the vanish manager
     *
     * @return the VanishManager
     */
    public VanishManager getManager() {
        return this.manager;
    }

    /**
     * Indicates a player has just joined the server.
     * Internal use only please
     *
     * @param player player who has joined the server
     */
    public void hooksJoin(Player player) {
        this.hookManager.onJoin(player);
    }

    /**
     * Indicates a player has left the server
     * Internal use only please
     *
     * @param player player who has left the server
     */
    public void hooksQuit(Player player) {
        this.hookManager.onQuit(player);
        this.hookManager.onUnvanish(player);
    }

    /**
     * Calls hooks for when a player has unvanished
     * Internal use only please
     *
     * @param player the un-vanishing user
     */
    public void hooksUnvanish(Player player) {
        this.hookManager.onUnvanish(player);
    }

    /**
     * Calls hooks for when player has vanished
     * Internal use only please.
     *
     * @param player the vanishing player
     */
    public void hooksVanish(Player player) {
        this.hookManager.onVanish(player);
    }

    /**
     * Sends a message to all players with vanish.statusupdates permission
     *
     * @param message the message to send
     */
    public void messageStatusUpdate(String message) {
        this.messageStatusUpdate(message, null);
    }

    /**
     * Sends a message to all players with vanish.statusupdates but one
     *
     * @param message the message to send
     * @param avoid player to not send the message to
     */
    public void messageStatusUpdate(String message, Player avoid) {
        for (final Player player : this.getServer().getOnlinePlayers()) {
            if ((player != null) && !player.equals(avoid) && VanishPerms.canSeeStatusUpdates(player)) {
                player.sendMessage(message);
            }
        }
        
        BungeeChat.mirrorChat(message, Settings.getChatChannel());
    }

    @Override
    public void onDisable() {
        this.setInstance(null);
        Debuggle.nah();
        for (final Player player : VanishPlugin.this.getServer().getOnlinePlayers()) {
            if (player != null) {
                if (this.manager.isVanished(player)) {
                    player.sendMessage(ChatColor.DARK_AQUA + "[Vanish] You have been forced visible by a reload.");
                }
            }
        }
        this.hookManager.onDisable();
        this.manager.onPluginDisable();
        this.getLogger().info(this.getCurrentVersion() + " unloaded.");
    }

    @Override
    public void onEnable() {
        this.setInstance(this);
        BungeeHelper.initialize(this);

        final File check = new File(this.getDataFolder(), "config.yml");
        if (!check.exists()) {
            this.saveDefaultConfig();
            this.reloadConfig();
            if (this.getServer().getPluginManager().isPluginEnabled("Essentials")) {
                this.getConfig().set("hooks.essentials", true);
                this.getLogger().info("Detected Essentials. Enabling Essentials hook.");
                this.saveConfig();
            }
        }

        Settings.freshStart(this);

        if (this.getConfig().getBoolean("hooks.essentials", false)) {
            this.hookManager.getHook(HookType.Essentials).onEnable();
        }
        if (this.getConfig().getBoolean("hooks.dynmap", false)) {
            this.hookManager.getHook(HookType.Dynmap).onEnable();
        }
        this.hookManager.getHook(HookType.BungeeChat).onEnable();
        //if (this.getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
        //    this.hookManager.getHook(HookType.ProtocolLib).onEnable();
        //}

        final VanishPlugin self = this;
        //Post-load stuff

        this.manager = new VanishManager(this);

        for (final Player player : this.getServer().getOnlinePlayers()) {
            player.setMetadata("vanished", new LazyMetadataValue(this, CacheStrategy.NEVER_CACHE, new VanishCheck(this.manager, player.getName())));
        }


        this.getCommand("vanish").setExecutor(new VanishCommand(this));
        this.getServer().getPluginManager().registerEvents(new ListenEntity(this), this);
        this.getServer().getPluginManager().registerEvents(new ListenPlayerMessages(this), this);
        this.getServer().getPluginManager().registerEvents(new ListenPlayerJoin(this), this);
        this.getServer().getPluginManager().registerEvents(new ListenPlayerOther(this), this);
        this.getServer().getPluginManager().registerEvents(new ListenToYourHeart(this), this);
        this.getServer().getPluginManager().registerEvents(new ListenInventory(this), this);
        this.getServer().getPluginManager().registerEvents(new ListenServerPing(this.manager), this);

        this.getLogger().info(this.getCurrentVersion()+" loaded.");
    }

    /**
     * Reloads the VNP config
     */
    public void reload() {
        this.reloadConfig();
        Settings.freshStart(this);
    }

    /**
     * Gets if there is a difference in versions between this and latest
     * Will always be false if update checks are disabled
     *
     * @return whether or not there's a new version available
     */
    public boolean versionDifference() {
        boolean versionDiff = false;
        return versionDiff;
    }

    @SuppressWarnings("deprecation")
    private void setInstance(VanishPlugin plugin) {
        org.kitteh.vanish.staticaccess.VanishNoPacket.setInstance(plugin);
    }
}