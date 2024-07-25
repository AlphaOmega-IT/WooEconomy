package de.alphaomegait.wooeconomy.wooeconomy.economy.hookadapters;

import net.brcdev.shopgui.provider.economy.EconomyProvider;
import org.bukkit.entity.Player;

public class VaultProvider extends EconomyProvider {

	public VaultProvider() {

	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public double getBalance(final Player player) {
		return 0;
	}

	@Override
	public void deposit(
		final Player player,
		final double amount
	) {

	}

	@Override
	public void withdraw(
		final Player player,
		final double amount
	) {

	}
}