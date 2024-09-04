package de.alphaomegait.wooeconomy.wooeconomy;

import de.alphaomegait.aocore.AOCore;
import de.alphaomegait.aocore.licensing.ELicenseType;
import de.alphaomegait.wooeconomy.wooeconomy.commands.console.deposit.ADeposit;
import de.alphaomegait.wooeconomy.wooeconomy.commands.console.withdraw.AWithdraw;
import de.alphaomegait.wooeconomy.wooeconomy.commands.player.currency.Currency;
import de.alphaomegait.wooeconomy.wooeconomy.commands.player.deposit.Deposit;
import de.alphaomegait.wooeconomy.wooeconomy.commands.player.pay.Pay;
import de.alphaomegait.wooeconomy.wooeconomy.commands.player.setcurrency.SetCurrency;
import de.alphaomegait.wooeconomy.wooeconomy.commands.player.withdraw.Withdraw;
import de.alphaomegait.wooeconomy.wooeconomy.database.daos.WooEconomyDao;
import de.alphaomegait.wooeconomy.wooeconomy.economy.EconomyAdapter;
import de.alphaomegait.wooeconomy.wooeconomy.hooks.ShopGUIPlusHook;
import de.alphaomegait.wooeconomy.wooeconomy.placeholder.Placeholder;
import me.blvckbytes.bukkitevaluable.IConfigPathsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class WooEconomy extends JavaPlugin implements IConfigPathsProvider {

	private AOCore aoCore;
	private EconomyAdapter economyAdapter;

	private WooEconomyDao wooEconomyDao;

	@Override
	public void onLoad() {

	}

	@Override
	public void onEnable() {
		final long beginTimestamp = System.nanoTime();
		this.registerEconomyAdapter();

		this.aoCore = new AOCore(this, ELicenseType.FREE, true, false, this.getConfigPaths());
		this.aoCore.getAutoWirer()
			.addSingleton(Deposit.class)
			.addSingleton(Withdraw.class)
			.addSingleton(Pay.class)
			.addSingleton(Currency.class)
			.addSingleton(SetCurrency.class)
			.addSingleton(AWithdraw.class)
			.addSingleton(ADeposit.class)
			.addInstantiationListener(
				Listener.class,
				(listener, dependencies) -> Bukkit.getPluginManager().registerEvents(
					listener,
					this
				)
			)
			.addInstantiationListener(
				Command.class,
				(command, dependencies) -> Bukkit.getCommandMap()
					.register(
						command.getName(),
						command
					)
			)
			.onException(exception -> {
				this.aoCore.getLogger().logError("An error occurred while setting up the plugin: ", exception);
				this.getServer().getPluginManager().disablePlugin(this);
			})
			.wire(wirer -> {
				this.aoCore.getLogger().logInfo(
					"Successfully loaded " + wirer.getInstancesCount() + " classes (" + ((System.nanoTime() - beginTimestamp) / 1000 / 1000) + "ms)"
				);

				if (
					Arrays.stream(this.getServer().getPluginManager().getPlugins()).anyMatch(plugin -> plugin.getName().equals("ShopGUIPlus"))
				) {
					wirer.addSingleton(ShopGUIPlusHook.class).wire(wire -> {
					});
					this.getServer().getPluginManager().registerEvents(
						new ShopGUIPlusHook(this), this
					);
				}

				this.wooEconomyDao = new WooEconomyDao(this.aoCore);

				if (
					this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null
				) this.aoCore.initPlaceholderAPI(new Placeholder(this));
			});
	}

	@Override
	public void onDisable() {
		if (
			this.aoCore != null
		) this.aoCore.disable();
	}

	/**
	 * Returns an array of strings representing the paths of the configuration files.
	 *
	 * @return  an array of strings representing the paths of the configuration files
	 */
	@Override
	public String[] getConfigPaths() {
		return new String[] {
			"database-config.yml",
			"commands/currency-config.yml",
			"commands/pay-config.yml",
			"commands/withdraw-config.yml",
			"commands/deposit-config.yml",
			"commands/setcurrency-config.yml",
			"translations/i18n.yml",
			"database/database-config.yml"
		};
	}

	public EconomyAdapter getEconomyAdapter() {
		return this.economyAdapter;
	}

	public AOCore getAoCore() {
		return this.aoCore;
	}

	public WooEconomyDao getWooEconomyDao() {
		return this.wooEconomyDao;
	}

	private void registerEconomyAdapter() {
		this.economyAdapter = new EconomyAdapter(this);

		this.getServer().getServicesManager().register(
			EconomyAdapter.class,
			this.economyAdapter,
			this,
			ServicePriority.Highest
		);
	}
}