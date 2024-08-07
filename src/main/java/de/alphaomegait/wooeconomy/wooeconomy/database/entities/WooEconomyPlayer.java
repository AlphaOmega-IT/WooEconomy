package de.alphaomegait.wooeconomy.wooeconomy.database.entities;

import de.alphaomegait.woocore.database.converter.UUIDConverter;
import de.alphaomegait.woocore.database.entities.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.NamedQuery;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity
@NamedQuery(
	name = "WooEconomy.findAll",
	query = "SELECT E FROM WooEconomyPlayer E"
)
@NamedQuery(
	name = "WooEconomyPlayer.findById",
	query = "SELECT E FROM WooEconomyPlayer E WHERE E.id = :id"
)
@NamedQuery(
	name = "WooEconomyPlayer.findByPlayerUUID",
	query = "SELECT E FROM WooEconomyPlayer E WHERE E.playerUUID = :playerUUID"
)
@NamedQuery(
	name = "WooEconomyPlayer.findPlayersByTopBalance",
	query = "SELECT E FROM WooEconomyPlayer E ORDER BY E.balance DESC"
)
@NamedQuery(
	name = "WooEconomyPlayer.findPlayersByTopBalanceAndLimit",
	query = "SELECT E FROM WooEconomyPlayer E ORDER BY E.balance DESC LIMIT 10"
)
@Table(name = "woo_economy_player")
@SequenceGenerator(
	allocationSize = 1,
	name = "SQ_GEN_HIBERNATE",
	sequenceName = "SQ_GEN_HIBERNATE"
)
public class WooEconomyPlayer extends BaseEntity<WooEconomyPlayer> {

	@Column(
		name = "player_uuid",
		unique = true,
		nullable = false
	)
	@Convert(
		converter = UUIDConverter.class
	)
	private UUID playerUUID;

	@Column(
		name = "balance",
		nullable = false
	)
	private Double balance = 0.00;

	public WooEconomyPlayer() {}

	public WooEconomyPlayer(
		final @NotNull UUID playerUUID,
		final double balance
	) {
		this.playerUUID = playerUUID;
		this.balance = balance;
	}

	@Override
	public WooEconomyPlayer getSelf() {
		return this;
	}

	public UUID getPlayerUUID() {
		return this.playerUUID;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(
		final Double balance
	) {
		this.balance = balance;
	}

	/**
	 *
	 * @param amount the amount to deposit
	 * @return true if deposit was successful
	 */
	public boolean deposit(
		final Double amount
	) {
		try {
			this.balance = this.balance + amount;
			return true;
		} catch (
			final Exception ignored
		) {
			return false;
		}
	}

	/**
	 *
	 * @param amount the amount to withdraw
	 * @return true if withdraw was successful
	 */
	public boolean withdraw(
		final double amount
	) {
		try {
			if (
				this.balance < amount
			) return false;

			this.balance = this.balance - amount;
			return true;
		} catch (
			final Exception ignored
		) {
			return false;
		}
	}
}