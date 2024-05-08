package de.alphaomegait.wooeconomy.wooeconomy.commands.setcurrency;

import de.alphaomegait.ao18n.I18n;
import de.alphaomegait.wooeconomy.wooeconomy.WooEconomy;
import de.alphaomegait.wooeconomy.wooeconomy.commands.deposit.DepositCommandSection;
import de.alphaomegait.wooeconomy.wooeconomy.commands.withdraw.EWithdrawPermissionNode;
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

public class SetCurrency extends PlayerCommand {
	
	private final WooEconomy         wooEconomy;
	private final PermissionsSection permissionsSection;
	
	public SetCurrency(
			final @NotNull ConfigManager configManager,
			final @NotNull WooEconomy wooEconomy,
			final @NotNull Logger logger
	) throws Exception {
		super(
				configManager
						.getMapper(
								"commands/setcurrency-config.yml"
						)
						.mapSection(
								"commands.setcurrency",
								SetCurrencyCommandSection.class
						),
				logger
		);
		
		this.wooEconomy = wooEconomy;
		this.permissionsSection = configManager
				                          .getMapper(
						                          "commands/setcurrency-config.yml"
				                          )
				                          .mapSection(
						                          "commands.setcurrency",
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
						ESetCurrencyPermissionNode.SET_CURRENCY
				)
		) {
			this.permissionsSection.sendMissingMessage(
					player,
					ESetCurrencyPermissionNode.SET_CURRENCY
			);
			return;
		}
		
		//TODO ALL PLAYER - SET CURRENCY ALL
		
		final Player targetPlayer = this.playerParameter(
				args,
				0
		);
		
		final double setCurrencyAmount = this.doubleParameter(
				args,
				1
		);
		
		this.wooEconomy.getEconomyAdapter().withdrawPlayer(
				targetPlayer,
				this.wooEconomy.getEconomyAdapter().getBalance(targetPlayer)
		);
		
		this.wooEconomy.getEconomyAdapter().depositPlayer(
				targetPlayer,
				setCurrencyAmount
		);
		
		new I18n.Builder(
				"setcurrency.new_amount_set_to",
				player
		).hasPrefix(true).setArgs(targetPlayer.getName(), setCurrencyAmount).build().sendMessageAsComponent();
		
		new I18n.Builder(
				"setcurrency.new_amount_received_by",
				targetPlayer
		).hasPrefix(true).setArgs(targetPlayer.getName(), setCurrencyAmount).build().sendMessageAsComponent();
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
						ESetCurrencyPermissionNode.SET_CURRENCY
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
