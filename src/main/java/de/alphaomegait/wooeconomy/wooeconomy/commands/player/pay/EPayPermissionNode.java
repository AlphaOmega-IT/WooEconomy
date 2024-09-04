package de.alphaomegait.wooeconomy.wooeconomy.commands.player.pay;

import me.blvckbytes.bukkitevaluable.section.IPermissionNode;
import org.jetbrains.annotations.NotNull;

public enum EPayPermissionNode implements IPermissionNode {
	
	PAY(
			"pay",
			"wooeconomy.pay"
	),
	PAY_ALL(
			"pay",
			"wooeconomy.pay_all"
	)
	;
	
	private final String internalName;
	private final String fallbackNode;
	
	EPayPermissionNode(
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
