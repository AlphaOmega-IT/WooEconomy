package de.alphaomegait.wooeconomy.wooeconomy.hooks;

import de.alphaomegait.wooeconomy.wooeconomy.WooEconomy;
import de.alphaomegait.wooeconomy.wooeconomy.economy.hookadapters.ShopGUIPlusProvider;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.event.ShopGUIPlusPostEnableEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class ShopGUIPlusHook implements Listener {

	private final WooEconomy wooEconomy;

	public ShopGUIPlusHook(
		final @NotNull WooEconomy wooEconomy
	) {
		this.wooEconomy = wooEconomy;
	}

	@EventHandler
	public void onShopGUIPlusPostEnable(
		final @NotNull ShopGUIPlusPostEnableEvent event
	) {
		ShopGuiPlusApi.registerEconomyProvider(new ShopGUIPlusProvider(this.wooEconomy));
		this.wooEconomy.getLogger().info("Registered ShopGUIPlus economy provider");
	}
}