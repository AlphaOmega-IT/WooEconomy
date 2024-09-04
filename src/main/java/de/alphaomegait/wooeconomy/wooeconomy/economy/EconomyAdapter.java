package de.alphaomegait.wooeconomy.wooeconomy.economy;

import de.alphaomegait.wooeconomy.wooeconomy.WooEconomy;
import de.alphaomegait.wooeconomy.wooeconomy.database.entities.WooEconomyPlayer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * This abstract class represents an economy adapter that provides methods for interacting with the economy system,
 * such as checking player balance, depositing, and withdrawing funds. It also provides a method to determine if the
 * adapter is available and to create a player account. Subclasses must implement the methods for depositing and
 * withdrawing funds based on the specific economy system being adapted.
 */
public class EconomyAdapter implements IEconomyAdapter {

	private final WooEconomy wooEconomy;

	public EconomyAdapter(
		final @NotNull WooEconomy wooEconomy
	) {
		this.wooEconomy = wooEconomy;
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

	@Override
	public double getBalance(final WooEconomyPlayer economyPlayer) {
		return economyPlayer.getBalance();
	}

	public Optional<WooEconomyPlayer> getWooEconomyPlayer(final @NotNull OfflinePlayer player) {
 		return this.wooEconomy.getWooEconomyDao().findByPlayerUUID(player.getUniqueId());
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
		Optional<WooEconomyPlayer> oWooEconomyPlayer = this.wooEconomy.getWooEconomyDao().findByPlayerUUID(player.getUniqueId());
		WooEconomyPlayer economyPlayer = oWooEconomyPlayer.orElseThrow(() -> new RuntimeException("Missing Player Account for uuid (" + player.getUniqueId() + ")"));

		if (economyPlayer.deposit(amount)) {
			this.wooEconomy.getWooEconomyDao().update(economyPlayer);
			EconomyResponse response = new EconomyResponse(amount, this.getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Successfully deposited " + amount + " to " + economyPlayer.getId());
			this.wooEconomy.getAoCore().getLogger().logDebug(response.errorMessage());
			return response;
		}

		EconomyResponse response = new EconomyResponse(amount, this.getBalance(economyPlayer), EconomyResponse.ResponseType.FAILURE, "Failed to deposit " + amount + " to " + economyPlayer.getId());
		this.wooEconomy.getAoCore().getLogger().logWarning(response.errorMessage());
		return response;
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
		Optional<WooEconomyPlayer> oEconomyPlayer = this.wooEconomy.getWooEconomyDao().findByPlayerUUID(player.getUniqueId());
		if (oEconomyPlayer.isEmpty()) {
			return new EconomyResponse(amount, this.getBalance(player), EconomyResponse.ResponseType.FAILURE, "Missing Player Account for uuid (" + player.getUniqueId() + ")");
		}

		if (oEconomyPlayer.get().withdraw(amount)) {
			this.wooEconomy.getWooEconomyDao().update(oEconomyPlayer.get());
			return new EconomyResponse(amount, oEconomyPlayer.get().getBalance(), EconomyResponse.ResponseType.SUCCESS, "Successfully withdrawn " + amount + " to " + oEconomyPlayer.get().getId());
		}

		return new EconomyResponse(amount, oEconomyPlayer.get().getBalance(), EconomyResponse.ResponseType.FAILURE, "Failed to withdraw " + amount + " to " + oEconomyPlayer.get().getId());
	}

	/**
	 * Returns the number of fractional digits.
	 *
	 * @return the number of fractional digits
	 */
	@Override
	public int fractionalDigits() {
		return 3;
	}

	@Override
	public String getCurrencyPrefix() {
		return "$";
	}

	/**
	 * Creates a player account for the given OfflinePlayer.
	 *
	 * @param  player  the OfflinePlayer for which the account is created
	 * @return        true if the player account is successfully created, false otherwise
	 */
	@Override
	public boolean createPlayerAccount(
		final @NotNull OfflinePlayer player
	) {
		return this.createPlayerAccount(
			player,
			0.00
		);
	}

	/**
	 * Create a player account for the given player with the specified balance.
	 *
	 * @param  player   the player for whom the account is to be created
	 * @param  balance  the initial balance for the player's account
	 * @return          true if the player account is successfully created, false otherwise
	 */
	public boolean createPlayerAccount(
		final @NotNull OfflinePlayer player,
		final double balance
	) {
		if (
			this.getWooEconomyPlayer(player).isPresent()
		) return false;

		this.wooEconomy.getWooEconomyDao().create(
			new WooEconomyPlayer(player.getUniqueId(), balance)
		);

		return true;
	}
}