package de.alphaomegait.wooeconomy.wooeconomy.commands.console.withdraw;

import de.alphaomegait.ao18n.i18n.I18n;
import de.alphaomegait.wooeconomy.wooeconomy.WooEconomy;
import me.blvckbytes.bukkitcommands.PlayerCommand;
import me.blvckbytes.bukkitcommands.ServerCommand;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import me.blvckbytes.bukkitevaluable.section.PermissionsSection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AWithdraw extends ServerCommand {
	
	private final WooEconomy         wooEconomy;
	
	public AWithdraw(
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
								"commands.awithdraw",
								AWithdrawCommandSection.class
						),
				logger
		);
		
		this.wooEconomy = wooEconomy;
	}
	
	@Override
	protected void onConsoleInvocation(
		final @NotNull ConsoleCommandSender console,
		final @NotNull String label,
		final @NotNull String[] args
	) {
		final Player targetPlayer = this.playerParameter(
				args,
				0
		);
		
		final double withdrawAmount = this.doubleParameter(
				args,
				1
		);
		
		this.wooEconomy.getEconomyAdapter().withdrawPlayer(
				targetPlayer,
				withdrawAmount
		);

		if (
			targetPlayer.isOnline()
		) {
			new I18n.Builder(
				"withdraw_decreased_amount_by",
				targetPlayer
			).hasPrefix(true).setArgs(withdrawAmount).build().sendMessageAsComponent();
		}
	}
	
	@Override
	protected List<String> onTabComplete(
			final CommandSender commandSender,
			final String label,
			final String[] args
	) {
		return new ArrayList<>();
	}
}