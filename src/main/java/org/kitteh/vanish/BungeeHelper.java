package org.kitteh.vanish;

import org.bukkit.Bukkit;
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
		BungeeChat.getSyncManager().setPlayerProperty(player.getName(), "VNP:vanished", vanished);
		setTabGroup(player, vanished);
	}
	
	public static void setTabGroup(Player player, boolean vanished)
	{
		if(vanished)
			BungeeChat.getSyncManager().setPlayerProperty(player.getName(), "TL:group:vanish", true);
		else
			BungeeChat.getSyncManager().setPlayerProperty(player.getName(), "TL:group:vanish", null);
	}
	
	public static void setOnlineState(Player player, boolean online)
	{
		BungeeChat.getSyncManager().setPlayerProperty(player.getName(), "VNP:online", online);
		BungeeChat.getSyncManager().setPlayerProperty(player.getName(), "hasQuitMessage", online);
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
	}
	
	private static void loadVanishStatus(final Player player)
	{
		BungeeChat.getSyncManager().getPlayerPropertyAsync(player.getName(), "VNP:vanished", new IMethodCallback<Object>()
		{
			@Override
			public void onFinished( Object isVanished )
			{
				if(!(isVanished instanceof Byte) || ((Byte)isVanished) == 0)
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
		BungeeChat.getSyncManager().getPlayerPropertyAsync(player.getName(), "VNP:online", new IMethodCallback<Object>()
		{
			@Override
			public void onFinished( Object status )
			{
				boolean online = (status instanceof Byte) && ((Byte)status) == 1;
				
				mPlugin.getManager().getAnnounceManipulator().setFakeOnlineStatus(player.getName(), online);
				
				if(!online && VanishPerms.joinWithoutAnnounce(player))
					BungeeChat.getSyncManager().setPlayerProperty(player.getName(), "hasQuitMessage", false);
			}
			
			@Override
			public void onError( String type, String message )
			{
				mPlugin.getLogger().severe("Remote call exception while grabbing online status. " + type + ": " + message);
			}
		});
	}
}
