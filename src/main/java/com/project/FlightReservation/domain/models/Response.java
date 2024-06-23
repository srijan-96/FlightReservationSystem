package com.project.FlightReservation.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.FlightReservation.constants.ResponseStatusCode;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class Response<T>
{
	private int statusCode;
	private String status;
	private T message;
	private T body;


	public Response()
	{
	}

	public Response(int statusCodeValue, T message, T body)
	{
		if(ResponseStatusCode.isSuccess(statusCodeValue))
			getResponse(statusCodeValue, "Success", message, body);
		else
			getResponse(statusCodeValue, "Error", message, body);
	}

	public Response(ResponseStatusCode statusCode, T message, T body)
	{
		if(statusCode.isSuccess())
			getResponse(statusCode.getValue(), "Success", message, body);
		else
			getResponse(statusCode.getValue(), "Error", message, body);
	}

	@JsonIgnore
	public boolean isSuccess()
	{
		return ResponseStatusCode.isSuccess(this.statusCode);
	}

	@JsonIgnore
	public boolean isError()
	{
		return ResponseStatusCode.isError(this.statusCode);
	}

	private void getResponse(int statusCode, String status, T message, T body)
	{
		this.statusCode = statusCode;
		this.status = status;
		this.message = message;
		this.body = body;
	}

}
