package de.alphaomegait.wooeconomy.wooeconomy.commands.setcurrency;

import me.blvckbytes.bukkitevaluable.section.IPermissionNode;
import org.jetbrains.annotations.NotNull;

public enum ESetCurrencyPermissionNode implements IPermissionNode {
	
	SET_CURRENCY(
			"setcurrency",
			"wooeconomy.setcurrency"
	),
	SET_CURRENCY_ALL(
			"setcurrency",
			"wooeconomy.setcurrency_all"
	)
	;
	
	private final String internalName;
	private final String fallbackNode;
	
	ESetCurrencyPermissionNode(
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
