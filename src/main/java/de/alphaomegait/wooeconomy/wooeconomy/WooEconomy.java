package de.alphaomegait.wooeconomy.wooeconomy;

import de.alphaomegait.ao18n.AO18n;
import de.alphaomegait.woocore.WooCore;
import de.alphaomegait.woocore.dependencies.LibraryLoader;
import de.alphaomegait.woocore.enums.GPADependency;
import de.alphaomegait.woocore.enums.LicenseType;
import me.blvckbytes.autowirer.AutoWirer;
import me.blvckbytes.bukkitboilerplate.PluginFileHandler;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import me.blvckbytes.bukkitevaluable.IConfigPathsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;
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

	private WooCore wooCore;

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

		// Create config files
		Arrays.stream(this.getConfigPaths()).toList().forEach(
			configPath -> {
				this.saveResource(
					configPath,
					false
				);
				this.logger.info("Config file created: " + configPath);
			});

		this.wooCore = new WooCore(
			this,
			LicenseType.FREE,
			dependencies,
			false
		);

		new AO18n(
			this,
			false
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
			});
	}

	@Override
	public void onDisable() {
		this.wooCore.onDisable();
		// Perform cleanup
		this.autoWirer.cleanup();
	}

	public static WooEconomy getInstance() {
		return instance;
	}

	@Override
	public String[] getConfigPaths() {
		return new String[] {
			"translations/i18n.yml",
			"license-config.yml",
			"database-config.yml"
		};
	}
}