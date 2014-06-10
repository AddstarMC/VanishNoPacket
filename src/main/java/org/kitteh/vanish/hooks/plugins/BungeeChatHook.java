package org.kitteh.vanish.hooks.plugins;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.vanish.VanishPlugin;
import org.kitteh.vanish.hooks.Hook;

import au.com.addstar.bc.AFKChangeEvent;

public final class BungeeChatHook extends Hook implements Listener 
{
    public BungeeChatHook(VanishPlugin plugin) 
    {
        super(plugin);
    }

    @Override
    public void onEnable() 
    {
        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    private void onAFKChange(AFKChangeEvent event)
    {
    	if(plugin.getManager().isVanished(event.getPlayer()))
    		event.setCancelled(true);
    }
}