package de.alphaomegait.wooeconomy.wooeconomy.commands.console.deposit;

import de.alphaomegait.ao18n.I18n;
import de.alphaomegait.wooeconomy.wooeconomy.WooEconomy;
import me.blvckbytes.bukkitcommands.ServerCommand;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ADeposit extends ServerCommand {

	private final WooEconomy wooEconomy;

	public ADeposit(
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
								"commands.adeposit",
								ADepositCommandSection.class
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
		
		final double depositAmount = this.doubleParameter(
				args,
				1
		);
		
		this.wooEconomy.getEconomyAdapter().depositPlayer(
				targetPlayer,
				depositAmount
		);

		if (
			targetPlayer.isOnline()
		) {
			new I18n.Builder(
				"deposit_increased_amount_by",
				targetPlayer
			).hasPrefix(true).setArgs(depositAmount).build().sendMessageAsComponent();
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