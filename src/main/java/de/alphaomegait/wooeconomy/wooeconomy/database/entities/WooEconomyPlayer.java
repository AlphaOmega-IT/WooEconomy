package de.alphaomegait.wooeconomy.wooeconomy.database.entities;

import de.alphaomegait.aocore.database.converter.UUIDConverter;
import de.alphaomegait.aocore.database.entities.BaseEntity;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Represents a player in the WooEconomy system.
 */
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
	query = "SELECT E FROM WooEconomyPlayer E ORDER BY E.balance DESC"
)
@Table(name = "woo_economy_player")
@SequenceGenerator(
	allocationSize = 1,
	name = "SQ_GEN_HIBERNATE",
	sequenceName = "SQ_GEN_HIBERNATE"
)
public class WooEconomyPlayer extends BaseEntity<WooEconomyPlayer> {

	/**
	 * The unique identifier for the player.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_GEN_HIBERNATE")
	private Long id;

	/**
	 * The UUID of the player.
	 */
	@Column(
		name = "player_uuid",
		unique = true,
		nullable = false
	)
	@Convert(
		converter = UUIDConverter.class
	)
	private UUID playerUUID;

	/**
	 * The balance of the player.
	 */
	@Column(
		name = "balance",
		nullable = false
	)
	private Double balance = 0.00;

	/**
	 * Default constructor for WooEconomyPlayer.
	 */
	public WooEconomyPlayer() {}

	/**
	 * Constructor for WooEconomyPlayer with player UUID and initial balance.
	 *
	 * @param playerUUID The UUID of the player.
	 * @param balance The initial balance of the player.
	 */
	public WooEconomyPlayer(UUID playerUUID, double balance) {
		this.playerUUID = playerUUID;
		this.balance = balance;
	}

	/**
	 * Get the UUID of the player.
	 *
	 * @return The UUID of the player.
	 */
	public UUID getPlayerUUID() {
		return this.playerUUID;
	}

	/**
	 * Get the balance of the player.
	 *
	 * @return The balance of the player.
	 */
	public Double getBalance() {
		return this.balance;
	}

	/**
	 * Set the balance of the player.
	 *
	 * @param balance The new balance to set.
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	/**
	 * Deposit a specified amount to the player's balance.
	 *
	 * @param amount The amount to deposit.
	 * @return True if the deposit was successful, false otherwise.
	 */
	public boolean deposit(Double amount) {
		if (amount < 0) {
			return false;
		}
		this.balance += amount;
		return true;
	}

	/**
	 * Withdraw a specified amount from the player's balance.
	 *
	 * @param amount The amount to withdraw.
	 * @return True if the withdrawal was successful, false otherwise.
	 */
	public boolean withdraw(Double amount) {
		if (amount < 0 || this.balance < amount) {
			return false;
		}
		this.balance -= amount;
		return true;
	}

	@Override
	public WooEconomyPlayer getSelf() {
		return this;
	}
}