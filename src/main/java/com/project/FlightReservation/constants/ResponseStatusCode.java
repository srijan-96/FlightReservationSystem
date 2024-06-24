package com.project.FlightReservation.constants;

public enum ResponseStatusCode
{

	SUCCESS(1),


	ERROR(-1);

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
