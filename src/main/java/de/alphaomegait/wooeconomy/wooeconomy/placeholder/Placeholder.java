package de.alphaomegait.wooeconomy.wooeconomy.placeholder;

import de.alphaomegait.aocore.placeholder.AOPlaceholder;
import de.alphaomegait.wooeconomy.wooeconomy.WooEconomy;
import de.alphaomegait.wooeconomy.wooeconomy.database.entities.WooEconomyPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Represents a custom placeholder class that extends AOPlaceholder for handling placeholders related to currencies and player currencies.
 */
public class Placeholder extends AOPlaceholder {

    private static final Pattern PARAMS_PATTERN = Pattern.compile("_");
    private final WooEconomy wooEconomy;

    /**
     * Constructs a new Placeholder instance.
     *
     * @param wooEconomy The AOPayments instance associated with this placeholder.
     */
    public Placeholder(final @NotNull WooEconomy wooEconomy) {
        super(wooEconomy.getAoCore());
        this.wooEconomy = wooEconomy;
    }

    /**
     * Sets the list of custom placeholders provided by this placeholder.
     *
     * @return A list of custom placeholders.
     */
    @Override
    public @NotNull List<String> setPlaceholders() {
        return List.of(
                "player_currency",
                "player_uuid",
                "player_name"
        );
    }

    /**
     * Processes the placeholder based on the player and parameters provided.
     *
     * @param player The player associated with the placeholder.
     * @param params The parameters for the placeholder.
     * @return The result of the placeholder processing.
     */
    @Override
    public @Nullable String onPlaceholder(Player player, @NotNull String params) {
        if (player == null || (!params.startsWith("player_"))) {
            return "";
        }
        return processCurrency(player, params);
    }

    /**
     * Processes the currency-related placeholder based on the parameters provided.
     *
     * @param player The player associated with the placeholder.
     * @param params The parameters for the currency placeholder.
     * @return The processed currency placeholder result.
     */
    private String processCurrency(final @NotNull Player player, final @NotNull String params) {
        String[] paramsSplit = PARAMS_PATTERN.split(params);

        if (!Pattern.matches("player_(currency|name|uuid)", params)) {
            return "";
        }

        String playerDetail = paramsSplit[1];

        Optional<WooEconomyPlayer> economyPlayer = this.wooEconomy.getWooEconomyDao().findByPlayerUUID(player.getUniqueId());

        return economyPlayer.map(playerData -> switch (playerDetail) {
            case "currency" -> playerData.getBalance().toString();
            case "name" -> player.getName();
            case "uuid" -> String.valueOf(playerData.getPlayerUUID());
            default -> "";
        }).orElse("");
    }
}