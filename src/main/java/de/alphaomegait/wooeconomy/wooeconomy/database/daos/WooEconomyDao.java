package de.alphaomegait.wooeconomy.wooeconomy.database.daos;

import de.alphaomegait.aocore.AOCore;
import de.alphaomegait.aocore.database.daos.BaseDao;
import de.alphaomegait.wooeconomy.wooeconomy.database.entities.WooEconomyPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Data Access Object for interacting with WooEconomyPlayer entities.
 */
public class WooEconomyDao extends BaseDao<WooEconomyPlayer> {

	/**
	 * Constructor for WooEconomyDao.
	 *
	 * @param aoCore The AOCore instance.
	 */
	public WooEconomyDao(final @NotNull AOCore aoCore) {
		super(aoCore, WooEconomyPlayer.class);
	}

	/**
	 * Find a WooEconomyPlayer by player UUID.
	 *
	 * @param playerUUID The UUID of the player.
	 * @return An Optional containing the WooEconomyPlayer if found, otherwise empty.
	 */
	public Optional<WooEconomyPlayer> findByPlayerUUID(final @NotNull UUID playerUUID) {
		return this.createNamedQuery("WooEconomyPlayer.findByPlayerUUID")
			.setParameter("playerUUID", playerUUID)
			.getResultStream().findFirst();
	}

	/**
	 * Find a WooEconomyPlayer by ID.
	 *
	 * @param id The ID of the player.
	 * @return An Optional containing the WooEconomyPlayer if found, otherwise empty.
	 */
	public Optional<WooEconomyPlayer> findById(final @NotNull Long id) {
		return this.createNamedQuery("WooEconomyPlayer.findById")
			.setParameter("id", id)
			.getResultStream().findFirst();
	}

	/**
	 * Find all WooEconomyPlayers ordered by top balance.
	 *
	 * @return A list of WooEconomyPlayers ordered by balance.
	 */
	public List<WooEconomyPlayer> findPlayersByTopBalance() {
		return this.createNamedQuery("WooEconomyPlayer.findPlayersByTopBalance")
			.getResultList();
	}

	/**
	 * Find top WooEconomyPlayers by balance with a specified limit.
	 *
	 * @return A list of WooEconomyPlayers with a limit on the number of results.
	 */
	public List<WooEconomyPlayer> findPlayersByTopBalanceAndLimit() {
		return this.createNamedQuery("WooEconomyPlayer.findPlayersByTopBalanceAndLimit")
			.setMaxResults(10) // Example limit of 10, can be adjusted as needed
			.getResultList();
	}

	/**
	 * Find all WooEconomyPlayers.
	 *
	 * @return A list of all WooEconomyPlayers.
	 */
	public List<WooEconomyPlayer> findAll() {
		return this.createNamedQuery("WooEconomyPlayer.findAll")
			.getResultList();
	}
}