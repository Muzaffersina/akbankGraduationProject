package com.msa.bankingsystem.services.requests;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.msa.bankingsystem.services.message.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

	@NotNull(message = "bank_id" + Messages.VALIDATIONNOTNULLERRORS)
	@Positive
	private int bank_id;

	@NotNull(message = "type" + Messages.VALIDATIONNOTNULLERRORS)
	private String type;

}
