package de.alphaomegait.wooeconomy.wooeconomy.economy;

import de.alphaomegait.woocore.WooCore;
import de.alphaomegait.wooeconomy.wooeconomy.database.daos.WooEconomyDao;
import de.alphaomegait.wooeconomy.wooeconomy.database.entities.WooEconomyPlayer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * This abstract class represents an economy adapter that provides methods for interacting with the economy system,
 * such as checking player balance, depositing, and withdrawing funds. It also provides a method to determine if the
 * adapter is available and to create a player account. Subclasses must implement the methods for depositing and
 * withdrawing funds based on the specific economy system being adapted.
 */
public class EconomyAdapter implements IEconomyAdapter {

	private final WooEconomyDao wooEconomyDao;

	public EconomyAdapter(
		final @NotNull Logger logger,
		final @NotNull WooCore wooCore
	) {
		this.wooEconomyDao = new WooEconomyDao(wooCore, logger);
	}

	/**
	 * Retrieves the balance of the specified player.
	 *
	 * @param  player  the player whose balance to retrieve
	 * @return         the balance of the player, or 0.00 if the adapter is not available
	 */
	@Override
	public double getBalance(final OfflinePlayer player) {
		final Optional<WooEconomyPlayer> wooEconomy = this.getWooEconomyPlayer(player);
		if (wooEconomy.isEmpty())
			return 0.00;

		return wooEconomy.get().getBalance();
	}

	public Optional<WooEconomyPlayer> getWooEconomyPlayer(final @NotNull OfflinePlayer player) {
 		return this.wooEconomyDao.findByPlayerUUID(player.getUniqueId());
	}

	/**
	 * Deposits the specified amount to the player's balance.
	 *
	 * @param  player  the player whose balance will be deposited
	 * @param  amount  the amount to be deposited
	 * @return         returns null if the deposit is successful, otherwise returns an error message
	 */
	@Override
	public EconomyResponse depositPlayer(
		final OfflinePlayer player,
		final double amount
	) {
		final Optional<WooEconomyPlayer> wooEconomy = this.getWooEconomyPlayer(player);
		if (wooEconomy.isEmpty())
			return new EconomyResponse(amount, this.getBalance(player), EconomyResponse.ResponseType.FAILURE, "Player not found");

		wooEconomy.get().deposit(amount);
		this.wooEconomyDao.update(wooEconomy.get(), wooEconomy.get().getId());
		return new EconomyResponse(amount, this.getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
	}

	/**
	 * Withdraws the specified amount from the player's balance.
	 *
	 * @param  player  the player whose balance will be withdrawn from
	 * @param  amount  the amount to be withdrawn
	 * @return         the error message if the withdrawal failed, otherwise null
	 */
	@Override
	public EconomyResponse withdrawPlayer(
		final OfflinePlayer player,
		final double amount
	) {
		final Optional<WooEconomyPlayer> wooEconomy = this.getWooEconomyPlayer(player);
		if (wooEconomy.isEmpty())
			return new EconomyResponse(amount, this.getBalance(player), EconomyResponse.ResponseType.FAILURE, "Player not found");

		if (! wooEconomy.get().withdraw(amount))
			return new EconomyResponse(amount, this.getBalance(player), EconomyResponse.ResponseType.FAILURE, "Player has not enough money");
		
		this.wooEconomyDao.update(wooEconomy.get(), wooEconomy.get().getId());
		return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
	}

	@Override
	public int fractionalDigits() {
		return 3;
	}

	@Override
	public boolean createPlayerAccount(final OfflinePlayer player) {
		return this.createPlayerAccount(
			player,
			0.00
		);
	}

	public boolean createPlayerAccount(
		final OfflinePlayer player,
		final double balance
	) {
		if (this.getWooEconomyPlayer(player).isPresent())
			return false;

		this.wooEconomyDao.persistEntity(
			new WooEconomyPlayer(player.getUniqueId(), balance)
		);

		return true;
	}
}