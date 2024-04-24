package de.alphaomegait.wooeconomy.wooeconomy.economy.hookadapters;

import de.alphaomegait.wooeconomy.wooeconomy.WooEconomy;
import net.brcdev.shopgui.provider.economy.EconomyProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShopGUIPlusProvider extends EconomyProvider {

	private final WooEconomy wooEconomy;

	public ShopGUIPlusProvider(
		final @NotNull WooEconomy wooEconomy
	) {
		this.wooEconomy = wooEconomy;

		this.currencyPrefix = this.wooEconomy.getEconomyAdapter().getCurrencyPrefix();
		this.currencySuffix = "";
	}

	@Override
	public String getName() {
		return this.wooEconomy.getEconomyAdapter().getClass().getName();
	}

	@Override
	public double getBalance(final Player player) {
		return this.wooEconomy.getEconomyAdapter().getBalance(player);
	}

	@Override
	public void deposit(
		final Player player,
		final double v
	) {
		this.wooEconomy.getEconomyAdapter().depositPlayer(player, v);
	}

	@Override
	public void withdraw(
		final Player player,
		final double v
	) {
		this.wooEconomy.getEconomyAdapter().withdrawPlayer(player, v);
	}
}