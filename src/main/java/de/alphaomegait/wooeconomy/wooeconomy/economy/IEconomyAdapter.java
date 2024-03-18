package de.alphaomegait.wooeconomy.wooeconomy.economy;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

/**
 * This interface extends the Economy interface and defines methods for interacting with the economy system, such as
 * checking player balance, depositing, and withdrawing funds. It also includes a method to create a player account
 * and determine if the adapter is available.
 */
public interface IEconomyAdapter {
	
	double getBalance(
		final OfflinePlayer player
	);

	/*
	 * @return null on success, the error-message if the balance could not be added.
	 */
	EconomyResponse depositPlayer(
		final OfflinePlayer player,
		final double amount
	);

	/*
	 * @return null on success, the error-message if the balance could not be removed.
	 */
	EconomyResponse withdrawPlayer(
		final OfflinePlayer player,
		final double amount
	);

	int fractionalDigits();
	
	boolean createPlayerAccount(
		final OfflinePlayer player
	);
}