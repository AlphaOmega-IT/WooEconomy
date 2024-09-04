package de.alphaomegait.wooeconomy.wooeconomy.commands.player.currency;

import de.alphaomegait.ao18n.I18n;
import de.alphaomegait.wooeconomy.wooeconomy.WooEconomy;
import me.blvckbytes.bukkitcommands.PlayerCommand;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import me.blvckbytes.bukkitevaluable.section.PermissionsSection;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Currency extends PlayerCommand {
	
	private final WooEconomy         wooEconomy;
	private final PermissionsSection permissionsSection;
	
	public Currency(
			final @NotNull ConfigManager configManager,
			final @NotNull WooEconomy wooEconomy,
			final @NotNull Logger logger
	) throws Exception {
		super(
				configManager
						.getMapper(
								"commands/currency-config.yml"
						)
						.mapSection(
								"commands.currency",
								CurrencyCommandSection.class
						),
				logger
		);
		this.wooEconomy        = wooEconomy;
		this.permissionsSection = configManager
				                          .getMapper(
						                          "commands/currency-config.yml"
				                          )
				                          .mapSection(
						                          "permissions.currency",
						                          PermissionsSection.class
				                          );
	}
	
	/**
	 * Overrides the onPlayerInvocation method from the superclass.
	 *
	 * @param player the player invoking the method
	 * @param label  the label of the command
	 * @param args   an array of command arguments
	 */
	@Override
	protected void onPlayerInvocation(
			final Player player,
			final String label,
			final String[] args
	) {
		if (! this.permissionsSection.hasPermission(
				player,
				ECurrencyPermissionNode.CURRENCY
		)) {
			this.permissionsSection.sendMissingMessage(
					player,
					ECurrencyPermissionNode.CURRENCY
			);
			return;
		}
		
		if (
				args.length == 0
		) {
			new I18n.Builder(
					"currency_current_balance",
					player
			).hasPrefix(true)
					.setArgs(this.wooEconomy.getEconomyAdapter().getBalance(player))
					.build()
					.sendMessageAsComponent();
			return;
		}
		
		if (! this.permissionsSection.hasPermission(
				player,
				ECurrencyPermissionNode.CURRENCY_OTHER
		)) {
			this.permissionsSection.sendMissingMessage(
					player,
					ECurrencyPermissionNode.CURRENCY_OTHER
			);
			return;
		}
		
		final OfflinePlayer targetPlayer = this.offlinePlayerParameter(
				args,
				0,
				true
		);
		
		new I18n.Builder(
				"currency_current_balance_of",
				player
		).hasPrefix(true).setArgs(
						targetPlayer.getName(),
						this.wooEconomy.getEconomyAdapter().getBalance(targetPlayer)
				)
				.build()
				.sendMessageAsComponent();
	}
	
	/**
	 * Generates a list of tab completions for the given command.
	 *
	 * @param commandSender the sender of the command
	 * @param label         the label of the command
	 * @param args          the arguments of the command
	 *
	 * @return a list of tab completions
	 */
	@Override
	protected List<String> onTabComplete(
			final CommandSender commandSender,
			final String label,
			final String[] args
	) {
		if (
				! (commandSender instanceof Player player)
		) return new ArrayList<>();
		
		if (
				args.length != 1 ||
				! this.permissionsSection.hasPermission(
						player,
						ECurrencyPermissionNode.CURRENCY_OTHER
				)
		) {
			return new ArrayList<>();
		}
		return StringUtil.copyPartialMatches(
				args[0].toLowerCase(),
				Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(),
				new ArrayList<>()
		);
	}
}