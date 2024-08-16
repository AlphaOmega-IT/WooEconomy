package de.alphaomegait.wooeconomy.wooeconomy.commands.withdraw;

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

public class Withdraw extends PlayerCommand {
	
	private final WooEconomy         wooEconomy;
	private final PermissionsSection permissionsSection;
	
	public Withdraw(
			final @NotNull ConfigManager configManager,
			final @NotNull WooEconomy wooEconomy,
			final @NotNull Logger logger
	) throws Exception {
		super(
				configManager
						.getMapper(
								"commands/withdraw-config.yml"
						)
						.mapSection(
								"commands.withdraw",
								WithdrawCommandSection.class
						),
				logger
		);
		
		this.wooEconomy = wooEconomy;
		this.permissionsSection = configManager
				                          .getMapper(
						                          "commands/withdraw-config.yml"
				                          )
				                          .mapSection(
						                          "commands.withdraw",
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
						EWithdrawPermissionNode.WITHDRAW
				)
		) {
			this.permissionsSection.sendMissingMessage(
					player,
					EWithdrawPermissionNode.WITHDRAW
			);
			return;
		}
		
		final OfflinePlayer targetPlayer = this.offlinePlayerParameter(
				args,
				0,
				true
		);
		
		final double withdrawAmount = this.doubleParameter(
				args,
				1
		);
		
		this.wooEconomy.getEconomyAdapter().withdrawPlayer(
				targetPlayer,
				withdrawAmount
		);
		
		new I18n.Builder(
				"withdraw_decreased_amount_by",
				player
		).hasPrefix(true).setArgs(withdrawAmount).build().sendMessageAsComponent();
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
						EWithdrawPermissionNode.WITHDRAW
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