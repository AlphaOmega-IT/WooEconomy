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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
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
		
		if (
				args.length == 2 &&
				args[1].equals("-all")
		) {
			final double payAmount = this.doubleParameter(
					args,
					1
			);
			
			final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
			final double amountToPay = payAmount * onlinePlayers.size();
			
			if (
					this.wooEconomy.getEconomyAdapter().getBalance(player) < amountToPay
			) {
				new I18n.Builder(
						"pay_sufficient_amount",
						player
				).hasPrefix(true).build().sendMessageAsComponent();
				return;
			}
			
			final double calculatedPayment = BigDecimal.valueOf(amountToPay / onlinePlayers.size()).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
			
			onlinePlayers.forEach(onlinePlayer -> this.handlePayment(player, onlinePlayer, calculatedPayment, true));
			
			new I18n.Builder(
					"pay_paid_all_people",
					player
			).hasPrefix(true).setArgs(onlinePlayers.size(), amountToPay, calculatedPayment).build().sendMessageAsComponent();
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
					"pay_no_self_payment",
					player
			).hasPrefix(true).build().sendMessageAsComponent();
			return;
		}
		
		final double payAmount = this.doubleParameter(
				args,
				1
		);
		
		this.handlePayment(
				player,
				targetPlayer,
				payAmount,
				false
		);
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
				List.of("--all")
		);
	}
	
	private void handlePayment(
			final @NotNull Player player,
			final @NotNull Player receiver,
			final double amountToReceive,
			final boolean isPayAll
	) {
		
		if (
				! this.wooEconomy.getEconomyAdapter().withdrawPlayer(
						player,
						amountToReceive
				).transactionSuccess()
		) {
			if (
					! isPayAll
			) new I18n.Builder(
					"pay_sufficient_amount",
					player
			).hasPrefix(true).build().sendMessageAsComponent();
			return;
		}
		
		if (
				this.wooEconomy.getEconomyAdapter().depositPlayer(
						receiver,
						amountToReceive
				).transactionSuccess()
		) {
			if (
					! isPayAll
			) new I18n.Builder(
					"pay_sent_money",
					player
			).hasPrefix(true).setArgs(receiver.getName(), amountToReceive).build().sendMessageAsComponent();
			
			if (
					! receiver.isOnline()
			) return;
			
			new I18n.Builder(
					"pay_received_money_by",
					receiver
			).hasPrefix(true).setArgs(receiver.getName(), amountToReceive).build().sendMessageAsComponent();
		}
	}
}