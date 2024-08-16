package de.alphaomegait.wooeconomy.wooeconomy.placeholder;

import de.alphaomegait.wooeconomy.wooeconomy.WooEconomy;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EconomyPlaceholderExpansion extends PlaceholderExpansion {

	private final WooEconomy wooEconomy;

	public EconomyPlaceholderExpansion(
		final @NotNull WooEconomy wooEconomy
	) {
		this.wooEconomy = wooEconomy;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "wooeconomy";
	}

	@Override
	public @NotNull String getAuthor() {
		return String.join(
			", ",
			this.wooEconomy.getPluginMeta().getAuthors()
		);
	}

	@Override
	public @NotNull String getVersion() {
		return this.wooEconomy.getPluginMeta().getVersion();
	}

	@Override
	public @NotNull List<String> getPlaceholders() {
		return List.of(
			"%wooeconomy_player_currency%",
			"%wooeconomy_player_uuid%",
			"%wooeconomy_player_name%"
		);
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public @Nullable String onPlaceholderRequest(
		final Player player,
		final @NotNull String params
	) {
		if (
			player == null
		) {
			return "";
		}

		if (
			params.equalsIgnoreCase("player_currency")
		) return String.valueOf(this.wooEconomy.getEconomyAdapter().getBalance(player));

		if (
			params.equalsIgnoreCase("player_uuid")
		) return String.valueOf(player.getUniqueId());

		if (
			params.equalsIgnoreCase("player_name")
		) return player.getName();

		return "";
	}
}