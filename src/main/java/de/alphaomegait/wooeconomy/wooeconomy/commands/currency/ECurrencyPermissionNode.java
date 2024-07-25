package de.alphaomegait.wooeconomy.wooeconomy.commands.currency;

import me.blvckbytes.bukkitevaluable.section.IPermissionNode;
import org.jetbrains.annotations.NotNull;

public enum ECurrencyPermissionNode implements IPermissionNode {
	
	CURRENCY(
			"currency",
			"wooeconomy.currency"
	),
	CURRENCY_OTHER(
			"currencyOther",
			"wooeconomy.currency_other"
	)
	;
	
	private final String internalName;
	private final String fallbackNode;
	
	ECurrencyPermissionNode(
			final @NotNull String internalName,
			final @NotNull String fallbackNode
	) {
		this.internalName = internalName;
		this.fallbackNode = fallbackNode;
	}

	@Override
	public String getInternalName() {
		return this.internalName;
	}

	@Override
	public String getFallbackNode() {
		return this.fallbackNode;
	}
}