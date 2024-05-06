package de.alphaomegait.wooeconomy.wooeconomy.commands.deposit;

import de.alphaomegait.ao18n.I18n;
import de.alphaomegait.wooeconomy.wooeconomy.WooEconomy;
import me.blvckbytes.bukkitcommands.PlayerCommand;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import me.blvckbytes.bukkitevaluable.section.PermissionsSection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Deposit extends PlayerCommand {

	private final WooEconomy wooEconomy;
	private final PermissionsSection permissionsSection;

	public Deposit(
			final @NotNull ConfigManager configManager,
			final @NotNull WooEconomy wooEconomy,
			final @NotNull Logger logger
	) throws Exception {
		super(
				configManager
						.getMapper(
						"commands/deposit-config.yml"
						)
						.mapSection(
								"commands.deposit",
								DepositCommandSection.class
						),
				logger
		);
		
		this.wooEconomy = wooEconomy;
		this.permissionsSection = configManager
				                          .getMapper(
																			"commands/deposit-config.yml"
				                          )
				                          .mapSection(
																			"commands.deposit",
						                          PermissionsSection.class
				                          );
	}

	@Override
	protected void onPlayerInvocation(
		final @NotNull Player player,
		final @NotNull String label,
		final @NotNull String[] args
	) {
		if (
				! this.permissionsSection.hasPermission(
						player,
						EDepositPermissionNode.DEPOSIT
				)
		) {
			this.permissionsSection.sendMissingMessage(
					player,
					EDepositPermissionNode.DEPOSIT
			);
			return;
		}
		
		final Player targetPlayer = this.playerParameter(
				args,
				0
		);
		
		final double depositAmount = this.doubleParameter(
				args,
				1
		);
		
		this.wooEconomy.getEconomyAdapter().depositPlayer(
				targetPlayer,
				depositAmount
		);
		
		new I18n.Builder(
				"deposit.increased_amount_by",
				player
		).hasPrefix(true).setArgs(depositAmount).build().sendMessageAsComponent();
	}

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
				(args.length != 1 && args.length != 2) ||
				! this.permissionsSection.hasPermission(
						player,
						EDepositPermissionNode.DEPOSIT
				)
		) return new ArrayList<>();
		
		if (
				args.length == 1
		) return StringUtil.copyPartialMatches(
				args[0].toLowerCase(),
				Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(),
				new ArrayList<>()
		);
		
		return List.of("0", "500", "1000", "2500", "5000", "10000");
	}
}
