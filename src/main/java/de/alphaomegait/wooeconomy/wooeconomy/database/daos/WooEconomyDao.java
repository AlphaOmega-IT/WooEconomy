package de.alphaomegait.wooeconomy.wooeconomy.database.daos;

import de.alphaomegait.woocore.WooCore;
import de.alphaomegait.woocore.database.daos.BaseDao;
import de.alphaomegait.wooeconomy.wooeconomy.database.entities.WooEconomyPlayer;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WooEconomyDao extends BaseDao<WooEconomyPlayer> {

	private final Logger logger;

	public WooEconomyDao(
		final @NotNull WooCore wooCore,
		final @NotNull Logger logger
	) {
		super(
			wooCore,
			logger
		);
		this.logger = logger;
	}

	public Optional<WooEconomyPlayer> findByPlayerUUID(
		final @NotNull UUID playerUUID
	) {
		final Query<WooEconomyPlayer> query = this.createNamedQuery("WooEconomyPlayer.findByPlayerUUID");
		query.setParameter("playerUUID", playerUUID);
		return Optional.ofNullable(query.getSingleResultOrNull());
	}

	public Optional<WooEconomyPlayer> findById(
		final @NotNull Long id
	) {
		final Query<WooEconomyPlayer> query = this.createNamedQuery("WooEconomyPlayer.findById");
		query.setParameter("id", id);
		return Optional.ofNullable(query.getSingleResultOrNull());
	}

	/**
	 * Update the balance of a WooEconomyPlayer with the given ID.
	 *
	 * @param  player  the WooEconomyPlayer to update
	 * @param  id      the ID of the player to update
	 */
	public void update(
		final @NotNull WooEconomyPlayer player,
		final @NotNull Long id
	) {
		try (
			final Session session = this.getOrCreateSession()
		) {
			final String hql = "UPDATE WooEconomyPlayer SET balance = :balance WHERE id = :id";
			session.beginTransaction();
			session.createMutationQuery(hql)
				.setParameter("balance", player.getBalance())
				.setParameter("id", id)
				.executeUpdate();
			session.getTransaction().commit();
		} catch (
			final Exception exception
		) {
			this.logger.log(
				Level.SEVERE,
				"Failed to update economy player with id: " + id,
				exception
			);
		}
	}

	public List<WooEconomyPlayer> findPlayersByTopBalance() {
		return this.createNamedQuery("WooEconomyPlayer.findPlayersByTopBalance").getResultList();
	}

	public List<WooEconomyPlayer> findPlayersByTopBalanceAndLimit() {
		return this.createNamedQuery("WooEconomyPlayer.findPlayersByTopBalanceAndLimit").getResultList();
	}

	public List<WooEconomyPlayer> findAll() {
		return this.createNamedQuery("WooEconomyPlayer.findAll").getResultList();
	}

	@Override
	protected Class<WooEconomyPlayer> getClazzType() {
		return WooEconomyPlayer.class;
	}
}