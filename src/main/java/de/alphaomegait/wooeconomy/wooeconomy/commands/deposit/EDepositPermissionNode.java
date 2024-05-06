package de.alphaomegait.wooeconomy.wooeconomy.commands.deposit;

import me.blvckbytes.bukkitevaluable.section.IPermissionNode;
import org.jetbrains.annotations.NotNull;

public enum EDepositPermissionNode implements IPermissionNode {
	
	DEPOSIT(
			"deposit",
			"wooeconomy.deposit"
	),
	DEPOSIT_OTHER(
			"deposit",
			"wooeconomy.deposit_other"
	)
	;
	
	private final String internalName;
	private final String fallbackNode;
	
	EDepositPermissionNode(
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
