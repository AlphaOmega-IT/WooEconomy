package de.alphaomegait.wooeconomy.wooeconomy.economy;

import de.alphaomegait.wooeconomy.wooeconomy.database.entities.WooEconomyPlayer;
import org.bukkit.OfflinePlayer;

/**
 * This interface extends the Economy interface and defines methods for interacting with the economy system, such as
 * checking player balance, depositing, and withdrawing funds. It also includes a method to create a player account
 * and determine if the adapter is available.
 */
public interface IEconomyAdapter {

	/**
	 * Retrieves the balance of the specified player.
	 *
	 * @param  player  the player whose balance is to be retrieved
	 * @return         the balance of the player
	 */
	double getBalance(
		final OfflinePlayer player
	);

	double getBalance(
		final WooEconomyPlayer economyPlayer
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

	/**
	 * Get the number of fractional digits in the result.
	 *
	 * @return         the number of fractional digits
	 */
	int fractionalDigits();

	/**
	 * Get the currency prefix.
	 *
	 * @return         the currency prefix
	 */
	String getCurrencyPrefix();

	/**
	 * Creates a player account for the given offline player.
	 *
	 * @param  player	The offline player for whom the account is to be created
	 * @return         	true if the player account is successfully created, false otherwise
	 */
	boolean createPlayerAccount(
		final OfflinePlayer player
	);
}