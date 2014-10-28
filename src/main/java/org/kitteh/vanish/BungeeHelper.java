package org.kitteh.vanish;

import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.addstar.bc.BungeeChat;
import au.com.addstar.bc.sync.IMethodCallback;

public class BungeeHelper
{
	private static VanishPlugin mPlugin;
	
	public static void initialize(VanishPlugin plugin)
	{
		mPlugin = plugin;
	}
	
	public static void setVanishState(Player player, boolean vanished)
	{
		BungeeChat.getSyncManager().setPlayerProperty(player.getUniqueId(), "VNP:vanished", vanished);
		setTabGroup(player, vanished);
	}
	
	public static void setTabGroup(Player player, boolean vanished)
	{
		if(vanished)
			BungeeChat.getSyncManager().setPlayerProperty(player.getUniqueId(), "TL:group:vanish", true);
		else
			BungeeChat.getSyncManager().setPlayerProperty(player.getUniqueId(), "TL:group:vanish", null);
	}
	
	public static void setOnlineState(Player player, boolean online)
	{
		BungeeChat.getSyncManager().setPlayerProperty(player.getUniqueId(), "VNP:online", online);
		BungeeChat.getSyncManager().setPlayerProperty(player.getUniqueId(), "hasQuitMessage", online);
	}
	
	public static void setSeeState(Player player, boolean canSee)
	{
		if(canSee)
			BungeeChat.getSyncManager().setPlayerProperty(player.getUniqueId(), "TL:see:vanish", true);
		else
			BungeeChat.getSyncManager().setPlayerProperty(player.getUniqueId(), "TL:see:vanish", null);
	}
	
	public static void setToggleState(Player player)
	{
		VanishUser user = VanishPerms.getUser(player);
		int state = user.getState();
		
		BungeeChat.getSyncManager().setPlayerProperty(player.getUniqueId(), "VNP:toggles", state);
	}
	
	public static void broadcastMessage(String message)
	{
		Bukkit.broadcastMessage(message);
		BungeeChat.mirrorChat(message, "~BC");
	}
	
	public static void loadStateFromProxy(Player player)
	{
		if(VanishPerms.joinVanished(player))
			loadOnlineStatus(player);
		
		loadVanishStatus(player);
		loadToggles(player);
	}
	
	private static void loadVanishStatus(final Player player)
	{
		BungeeChat.getSyncManager().getPlayerPropertyAsync(player.getUniqueId(), "VNP:vanished", new IMethodCallback<Object>()
		{
			@Override
			public void onFinished( Object isVanished )
			{
				if(!(isVanished instanceof Boolean) || !(Boolean)isVanished)
				{
					if(mPlugin.getManager().isVanished(player))
						mPlugin.getManager().toggleVanishQuiet(player, false, false);
					
					setTabGroup(player, false);
				}
				else
				{
					if(!mPlugin.getManager().isVanished(player))
						mPlugin.getManager().toggleVanishQuiet(player, false, false);
					
					setTabGroup(player, true);
				}
			}
			
			@Override
			public void onError( String type, String message )
			{
				mPlugin.getLogger().severe("Remote call exception while grabbing vanish status. " + type + ": " + message);
			}
		});
	}
	
	private static void loadOnlineStatus(final Player player)
	{
		BungeeChat.getSyncManager().getPlayerPropertyAsync(player.getUniqueId(), "VNP:online", new IMethodCallback<Object>()
		{
			@Override
			public void onFinished( Object status )
			{
				boolean online = (status instanceof Boolean) && (Boolean)status;
				
				mPlugin.getManager().getAnnounceManipulator().setFakeOnlineStatus(player.getName(), online);
				
				if(!online && VanishPerms.joinWithoutAnnounce(player))
					BungeeChat.getSyncManager().setPlayerProperty(player.getUniqueId(), "hasQuitMessage", false);
			}
			
			@Override
			public void onError( String type, String message )
			{
				mPlugin.getLogger().severe("Remote call exception while grabbing online status. " + type + ": " + message);
			}
		});
	}
	
	private static void loadToggles(final Player player)
	{
		BungeeChat.getSyncManager().getPlayerPropertyAsync(player.getUniqueId(), "VNP:toggles", new IMethodCallback<Object>()
		{
			@Override
			public void onFinished( Object status )
			{
				int state = (status instanceof Number ? ((Number)status).intValue() : 0);
				
				VanishUser user = VanishPerms.getUser(player);
				user.loadState(state);
			}
			
			@Override
			public void onError( String type, String message )
			{
				mPlugin.getLogger().severe("Remote call exception while grabbing toggles. " + type + ": " + message);
			}
		});
	}
	
	public static void printVanishedPlayers(final CommandSender sender)
	{
		BungeeChat.getSyncManager().getPropertiesAsync("VNP:vanished", new IMethodCallback<Map<String,Object>>()
		{
			@Override
			public void onFinished( Map<String, Object> data )
			{
				StringBuilder list = new StringBuilder();
				for(Entry<String, Object> entry : data.entrySet())
				{
					OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
					boolean vanished = (entry.getValue() instanceof Boolean && (Boolean)entry.getValue());
					if(vanished)
					{
						if (list.length() > 0) 
						{
		                    list.append(ChatColor.DARK_AQUA);
		                    list.append(',');
		                }
		                
						list.append(ChatColor.AQUA);
		                list.append(player.getName());
					}
				}
				
				list.insert(0, "Vanished: ");
		        list.insert(0, ChatColor.DARK_AQUA);
		        sender.sendMessage(list.toString());
			}
			
			@Override
			public void onError( String type, String message )
			{
				sender.sendMessage(ChatColor.RED + "An internal server error occured.");
				mPlugin.getLogger().severe("Remote call exception while grabbing vanish list. " + type + ": " + message);
			}
		});
	}
}
