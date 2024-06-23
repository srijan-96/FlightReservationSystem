package com.project.FlightReservation.constants;

public enum ResponseStatusCode
{

	SUCCESS_NO_ACTION_NEEDED(2),

	SUCCESS(1),

	SUCCESS_NO_ACTION_PERFORMED(0),

	ERROR(-1),

	ERROR_INVALID_DATA(-2);

	private int value;

	ResponseStatusCode(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public boolean isSuccess()
	{
		return isSuccess(value);
	}

	public boolean isError()
	{
		return !isSuccess(value);
	}

	public static boolean isSuccess(int statusCode)
	{
		return statusCode >= 0;
	}

	public static boolean isError(int statusCode)
	{
		return !isSuccess(statusCode);
	}
}
