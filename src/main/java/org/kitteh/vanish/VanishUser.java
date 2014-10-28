package org.kitteh.vanish;

import org.bukkit.entity.Player;

public final class VanishUser {
    private boolean seeAll;
    private boolean noPickup;
    private boolean noFollow;
    private boolean preventIncomingDamage;
    private boolean preventOutgoingDamage;
    private boolean noInteract;
    private boolean noHunger;
    private boolean noChat;
    private boolean silentChestReads;
    private boolean smoke;
    private boolean flames;
    private boolean explode;
    private boolean lightning;
    private boolean bats;
    private Player player;

    public VanishUser(Player player) {
        this.seeAll = player.hasPermission("vanish.see");
        this.noPickup = player.hasPermission("vanish.nopickup");
        this.noFollow = player.hasPermission("vanish.nofollow");
        this.preventIncomingDamage = player.hasPermission("vanish.preventincomingdamage");
        this.preventOutgoingDamage = player.hasPermission("vanish.preventoutgoingdamage");
        this.noInteract = player.hasPermission("vanish.nointeract");
        this.noHunger = player.hasPermission("vanish.nohunger");
        this.noChat = player.hasPermission("vanish.nochat");
        this.silentChestReads = player.hasPermission("vanish.silentchests");
        this.smoke = player.hasPermission("vanish.effects.smoke");
        this.flames = player.hasPermission("vanish.effects.flames");
        this.explode = player.hasPermission("vanish.effects.explode");
        this.lightning = player.hasPermission("vanish.effects.lightning");
        this.bats = player.hasPermission("vanish.effects.bats");
        this.player = player;
    }

    public boolean getEffectBats() {
        return this.bats;
    }

    public boolean getEffectExplode() {
        return this.explode;
    }

    public boolean getEffectFlames() {
        return this.flames;
    }

    public boolean getEffectLightning() {
        return this.lightning;
    }

    public boolean getEffectSmoke() {
        return this.smoke;
    }

    public boolean getNoChat() {
        return this.noChat;
    }

    public boolean getNoFollow() {
        return this.noFollow;
    }

    public boolean getNoHunger() {
        return this.noHunger;
    }

    public boolean getNoInteract() {
        return this.noInteract;
    }

    public boolean getNoPickup() {
        return this.noPickup;
    }

    public boolean getPreventIncomingDamage() {
        return this.preventIncomingDamage;
    }

    public boolean getPreventOutgoingDamage() {
        return this.preventOutgoingDamage;
    }

    public boolean getReadChestsSilently() {
        return this.silentChestReads;
    }

    public boolean getSeeAll() {
        return this.seeAll;
    }

    public boolean toggleEffectBats() {
        this.bats = !this.bats;
        BungeeHelper.setToggleState(player);
        return this.bats;
    }

    public boolean toggleEffectExplode() {
        this.explode = !this.explode;
        BungeeHelper.setToggleState(player);
        return this.explode;
    }

    public boolean toggleEffectFlames() {
        this.flames = !this.flames;
        BungeeHelper.setToggleState(player);
        return this.flames;
    }

    public boolean toggleEffectLightning() {
        this.lightning = !this.lightning;
        BungeeHelper.setToggleState(player);
        return this.lightning;
    }

    public boolean toggleEffectSmoke() {
        this.smoke = !this.smoke;
        BungeeHelper.setToggleState(player);
        return this.smoke;
    }

    public boolean toggleIncomingDamage() {
        this.preventIncomingDamage = !this.preventIncomingDamage;
        BungeeHelper.setToggleState(player);
        return this.preventIncomingDamage;
    }

    public boolean toggleNoChat() {
        this.noChat = !this.noChat;
        BungeeHelper.setToggleState(player);
        return this.noChat;
    }

    public boolean toggleNoFollow() {
        this.noFollow = !this.noFollow;
        BungeeHelper.setToggleState(player);
        return this.noFollow;
    }

    public boolean toggleNoHunger() {
        this.noHunger = !this.noHunger;
        BungeeHelper.setToggleState(player);
        return this.noHunger;
    }

    public boolean toggleNoInteract() {
        this.noInteract = !this.noInteract;
        BungeeHelper.setToggleState(player);
        return this.noInteract;
    }

    public boolean toggleNoPickup() {
        this.noPickup = !this.noPickup;
        BungeeHelper.setToggleState(player);
        return this.noPickup;
    }

    public boolean toggleOutgoingDamage() {
        this.preventOutgoingDamage = !this.preventOutgoingDamage;
        BungeeHelper.setToggleState(player);
        return this.preventOutgoingDamage;
    }

    public boolean toggleSeeAll() {
        this.seeAll = !this.seeAll;
        BungeeHelper.setToggleState(player);
        return this.seeAll;
    }

    public boolean toggleSilentChestReads() {
        this.silentChestReads = !this.silentChestReads;
        BungeeHelper.setToggleState(player);
        return this.silentChestReads;
    }

    private int set(int index, boolean value)
    {
    	if (value)
    		return 1 << index;
    	return 0;
    }
    
    public int getState()
    {
    	int val = 0;
    	val |= set(0, seeAll);
    	val |= set(1, noPickup);
    	val |= set(2, noFollow);
    	val |= set(3, preventIncomingDamage);
    	val |= set(4, preventOutgoingDamage);
    	val |= set(5, noInteract);
    	val |= set(6, noHunger);
    	val |= set(7, noChat);
    	val |= set(8, silentChestReads);
    	val |= set(9, smoke);
    	val |= set(10, flames);
    	val |= set(11, explode);
    	val |= set(12, lightning);
    	val |= set(13, bats);
    	
    	return val;
    }
    
    public boolean get(int index, int val)
    {
    	return (val & 1 << index) != 0;
    }
    
    public void loadState(int state)
    {
    	seeAll = get(0, state);
    	noPickup = get(1, state);
    	noFollow = get(2, state);
    	preventIncomingDamage = get(3, state);
    	preventOutgoingDamage = get(4, state);
    	noInteract = get(5, state);
    	noHunger = get(6, state);
    	noChat = get(7, state);
    	silentChestReads = get(8, state);
    	smoke = get(9, state);
    	flames = get(10, state);
    	explode = get(11, state);
    	lightning = get(12, state);
    	bats = get(13, state);
    }   
}