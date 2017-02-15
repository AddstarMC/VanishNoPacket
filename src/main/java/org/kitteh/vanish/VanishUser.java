package org.kitteh.vanish;

import org.bukkit.entity.Player;

final class VanishUser {
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
    private final Player player;

    VanishUser(Player player) {
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

    boolean getEffectBats() {
        return this.bats;
    }

    boolean getEffectExplode() {
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
        BungeeHelper.setToggleState(this.player);
        return this.bats;
    }

    public boolean toggleEffectExplode() {
        this.explode = !this.explode;
        BungeeHelper.setToggleState(this.player);
        return this.explode;
    }

    public boolean toggleEffectFlames() {
        this.flames = !this.flames;
        BungeeHelper.setToggleState(this.player);
        return this.flames;
    }

    public boolean toggleEffectLightning() {
        this.lightning = !this.lightning;
        BungeeHelper.setToggleState(this.player);
        return this.lightning;
    }

    public boolean toggleEffectSmoke() {
        this.smoke = !this.smoke;
        BungeeHelper.setToggleState(this.player);
        return this.smoke;
    }

    public boolean toggleIncomingDamage() {
        this.preventIncomingDamage = !this.preventIncomingDamage;
        BungeeHelper.setToggleState(this.player);
        return this.preventIncomingDamage;
    }

    public boolean toggleNoChat() {
        this.noChat = !this.noChat;
        BungeeHelper.setToggleState(this.player);
        return this.noChat;
    }

    public boolean toggleNoFollow() {
        this.noFollow = !this.noFollow;
        BungeeHelper.setToggleState(this.player);
        return this.noFollow;
    }

    public boolean toggleNoHunger() {
        this.noHunger = !this.noHunger;
        BungeeHelper.setToggleState(this.player);
        return this.noHunger;
    }

    public boolean toggleNoInteract() {
        this.noInteract = !this.noInteract;
        BungeeHelper.setToggleState(this.player);
        return this.noInteract;
    }

    public boolean toggleNoPickup() {
        this.noPickup = !this.noPickup;
        BungeeHelper.setToggleState(this.player);
        return this.noPickup;
    }

    public boolean toggleOutgoingDamage() {
        this.preventOutgoingDamage = !this.preventOutgoingDamage;
        BungeeHelper.setToggleState(this.player);
        return this.preventOutgoingDamage;
    }

    public boolean toggleSeeAll() {
        this.seeAll = !this.seeAll;
        BungeeHelper.setToggleState(this.player);
        return this.seeAll;
    }

    public boolean toggleSilentChestReads() {
        this.silentChestReads = !this.silentChestReads;
        BungeeHelper.setToggleState(this.player);
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
    	val |= set(0, this.seeAll);
    	val |= set(1, this.noPickup);
    	val |= set(2, this.noFollow);
    	val |= set(3, this.preventIncomingDamage);
    	val |= set(4, this.preventOutgoingDamage);
    	val |= set(5, this.noInteract);
    	val |= set(6, this.noHunger);
    	val |= set(7, this.noChat);
    	val |= set(8, this.silentChestReads);
    	val |= set(9, this.smoke);
    	val |= set(10, this.flames);
    	val |= set(11, this.explode);
    	val |= set(12, this.lightning);
    	val |= set(13, this.bats);
    	
    	return val;
    }
    
    private boolean get(int index, int val)
    {
    	return (val & 1 << index) != 0;
    }
    
    public void loadState(int state)
    {
        this.seeAll = get(0, state);
        this.noPickup = get(1, state);
        this.noFollow = get(2, state);
        this.preventIncomingDamage = get(3, state);
        this.preventOutgoingDamage = get(4, state);
        this.noInteract = get(5, state);
        this.noHunger = get(6, state);
        this.noChat = get(7, state);
        this.silentChestReads = get(8, state);
        this.smoke = get(9, state);
        this.flames = get(10, state);
        this.explode = get(11, state);
        this.lightning = get(12, state);
        this.bats = get(13, state);
    }   
}