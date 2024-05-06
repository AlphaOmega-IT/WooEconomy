package de.alphaomegait.wooeconomy.wooeconomy.commands.pay;

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

public class Pay extends PlayerCommand {
	
	private final WooEconomy         wooEconomy;
	private final PermissionsSection permissionsSection;
	
	public Pay(
			final @NotNull ConfigManager configManager,
			final @NotNull WooEconomy wooEconomy,
			final @NotNull Logger logger
	) throws Exception {
		super(
				configManager
						.getMapper(
								"commands/pay-config.yml"
						)
						.mapSection(
								"commands.pay",
								PayCommandSection.class
						),
				logger
		);
		
		this.wooEconomy = wooEconomy;
		this.permissionsSection = configManager
				                          .getMapper(
						                          "commands/pay-config.yml"
				                          )
				                          .mapSection(
						                          "commands.pay",
						                          PermissionsSection.class
				                          );
	}
	
	@Override
	protected void onPlayerInvocation(
			final Player player,
			final String label,
			final String[] args
	) {
		if (
				! this.permissionsSection.hasPermission(
						player,
						EPayPermissionNode.PAY
				)
		) {
			this.permissionsSection.sendMissingMessage(
					player,
					EPayPermissionNode.PAY
			);
			return;
		}
		
		final Player targetPlayer = this.playerParameter(
				args,
				0
		);
		
		if (
				targetPlayer.getUniqueId() == player.getUniqueId()
		) {
			new I18n.Builder(
					"pay.no_self_payment",
					player
			).hasPrefix(true).build().sendMessageAsComponent();
			return;
		}
		
		final double payAmount = this.doubleParameter(
				args,
				1
		);
		
		if (
				! this.wooEconomy.getEconomyAdapter().withdrawPlayer(
					player,
					payAmount
				).transactionSuccess()
		) {
			new I18n.Builder(
					"pay.sufficient_amount",
					player
			).hasPrefix(true).build().sendMessageAsComponent();
			return;
		}
		
		if (
				this.wooEconomy.getEconomyAdapter().depositPlayer(
				targetPlayer,
				payAmount
			).transactionSuccess()
		) {
			new I18n.Builder(
					"pay.sent_money",
					player
			).hasPrefix(true).setArgs(targetPlayer.getName(), payAmount).build().sendMessageAsComponent();
			
			new I18n.Builder(
					"pay.received_money_by",
					targetPlayer
			).hasPrefix(true).setArgs(player.getName(), payAmount).build().sendMessageAsComponent();
		}
	}
	
	@Override
	protected List<String> onTabComplete(
			final CommandSender commandSender,
			final String label,
			final String[] args
	) {
		if (
				args.length != 1
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
