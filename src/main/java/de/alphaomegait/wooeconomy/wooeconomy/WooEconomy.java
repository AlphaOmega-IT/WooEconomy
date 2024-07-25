package de.alphaomegait.wooeconomy.wooeconomy;

import de.alphaomegait.ao18n.AO18n;
import de.alphaomegait.woocore.WooCore;
import de.alphaomegait.woocore.dependencies.LibraryLoader;
import de.alphaomegait.woocore.enums.GPADependency;
import de.alphaomegait.woocore.enums.LicenseType;
import de.alphaomegait.wooeconomy.wooeconomy.commands.currency.Currency;
import de.alphaomegait.wooeconomy.wooeconomy.commands.deposit.Deposit;
import de.alphaomegait.wooeconomy.wooeconomy.commands.pay.Pay;
import de.alphaomegait.wooeconomy.wooeconomy.commands.setcurrency.SetCurrency;
import de.alphaomegait.wooeconomy.wooeconomy.commands.withdraw.Withdraw;
import de.alphaomegait.wooeconomy.wooeconomy.economy.EconomyAdapter;
import de.alphaomegait.wooeconomy.wooeconomy.hooks.ShopGUIPlusHook;
import me.blvckbytes.autowirer.AutoWirer;
import me.blvckbytes.bukkitboilerplate.PluginFileHandler;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import me.blvckbytes.bukkitevaluable.IConfigPathsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class WooEconomy extends JavaPlugin implements IConfigPathsProvider {

	private final Logger logger = Logger.getLogger("WooEconomy");
	private static WooEconomy instance;

	private AutoWirer autoWirer;

	private EconomyAdapter economyAdapter;

	@Override
	public void onLoad() {
		instance = this;

		// Create the plugin folder if it doesn't exist
		if (
			this.getDataFolder().mkdir()
		) {
			this.logger.info("Plugin folder created.");
		}

		List<LibraryLoader.Dependency> dependencies = new ArrayList<>();
		dependencies.addAll(GPADependency.HIBERNATE_ORM.getDependencies());
		dependencies.addAll(GPADependency.MYSQL.getDependencies());
		dependencies.addAll(GPADependency.H2.getDependencies());

		// Create config files
		Arrays.stream(this.getConfigPaths()).toList().forEach(
			configPath -> {
				this.saveResource(
					configPath,
					false
				);
				this.logger.info("Config file created: " + configPath);
			});

		final WooCore wooCore = new WooCore(
			this,
			LicenseType.FREE,
			dependencies,
			false, true
		);

		new AO18n(
			this,
			false
		);

		this.economyAdapter = new EconomyAdapter(
			this.logger,
			wooCore
		);

		this.getServer().getServicesManager().register(
			EconomyAdapter.class,
			this.economyAdapter,
			this,
			ServicePriority.Highest
		);
	}

	@Override
	public void onEnable() {
		final long beginTimestamp = System.nanoTime();

		// Create an instance of AutoWirer
		this.autoWirer = new AutoWirer();

		this.autoWirer
			.addExistingSingleton(this)
			.addExistingSingleton(this.logger)
			.addSingleton(ConfigManager.class)
			.addSingleton(PluginFileHandler.class)
			.addSingleton(Deposit.class)
			.addSingleton(Withdraw.class)
			.addSingleton(Pay.class)
			.addSingleton(Currency.class)
			.addSingleton(SetCurrency.class)
			.addInstantiationListener(
				Listener.class,
				(listener, dependencies) -> {
					Bukkit.getPluginManager().registerEvents(
						listener,
						this
					);
				}
			)
			.addInstantiationListener(
				Command.class,
				(command, dependencies) -> {
					Bukkit.getCommandMap()
								.register(
									command.getName(),
									command
								);
				}
			)
			.onException(exception -> {
				this.logger.log(
					Level.SEVERE,
					"An exception occurred while loading the plugin: " + exception,
					exception
				);
				this.getServer().getScheduler().cancelTasks(this);
				this.getServer().getPluginManager().disablePlugin(this);
				this.onDisable();
			})
			.wire(wirer -> {
				this.logger.info(
					"Successfully loaded " + wirer.getInstancesCount() + " classes (" + ((System.nanoTime() - beginTimestamp) / 1000 / 1000) + "ms)"
				);

				if (
					Arrays.stream(this.getServer().getPluginManager().getPlugins()).anyMatch(plugin -> plugin.getName().equals("ShopGUIPlus"))
				) {
					this.autoWirer.addSingleton(ShopGUIPlusHook.class).wire(wire -> {});
					this.getServer().getPluginManager().registerEvents(
						new ShopGUIPlusHook(this), this
					);
				}
			});
	}

	@Override
	public void onDisable() {
		// Perform cleanup
		this.autoWirer.cleanup();
	}

	/**
	 * Returns the instance of the plugin
	 *
	 * @return  the instance of the plugin
	 */
	public static WooEconomy getInstance() {
		return instance;
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
			"commands/setcurrency-config.yml"
		};
	}

	public EconomyAdapter getEconomyAdapter() {
		return this.economyAdapter;
	}
}