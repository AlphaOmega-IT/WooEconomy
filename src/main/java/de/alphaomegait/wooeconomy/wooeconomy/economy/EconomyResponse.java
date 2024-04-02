package de.alphaomegait.wooeconomy.wooeconomy.economy;

import java.util.Objects;

/**
 * Indicates a typical Return for an Economy method. It includes a {@link de.alphaomegait.wooeconomy.wooeconomy.economy.EconomyResponse.ResponseType} indicating whether the plugin currently being used for Economy
 * actually allows the method, or if the operation was a success or failure.
 *
 * @param amount       Amount modified by calling method
 * @param balance      New balance of account
 * @param type         Success or failure of call. Using Enum of ResponseType to determine valid outcomes
 * @param errorMessage Error message if the variable 'type' is ResponseType.FAILURE
 */
public record EconomyResponse(
	double amount,
	double balance,
	ResponseType type,
	String errorMessage
) {

	/**
	 * Enum for types of Responses indicating the status of a method call.
	 */
	public enum ResponseType {
		SUCCESS,
		FAILURE,
		NOT_IMPLEMENTED
	}

	/**
	 * Constructor for EconomyResponse
	 *
	 * @param amount       Amount modified during operation
	 * @param balance      New balance of account
	 * @param type         Success or failure type of the operation
	 * @param errorMessage Error message if necessary (commonly null)
	 */
	public EconomyResponse {}

	/**
	 * Checks if an operation was successful
	 *
	 * @return Value
	 */
	public boolean transactionSuccess() {
		return Objects.requireNonNull(type) == ResponseType.SUCCESS;
	}
}