package de.alphaomegait.wooeconomy.wooeconomy.commands.withdraw;

import me.blvckbytes.bukkitevaluable.section.IPermissionNode;
import org.jetbrains.annotations.NotNull;

public enum EWithdrawPermissionNode implements IPermissionNode {
	
	WITHDRAW(
			"withdraw",
			"wooeconomy.withdraw"
	),
	WITHDRAW_OTHER(
			"withdraw",
			"wooeconomy.withdraw_other"
	)
	;
	
	private final String internalName;
	private final String fallbackNode;
	
	EWithdrawPermissionNode(
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
