package com.msa.bankingsystem.services.requests;

import javax.validation.constraints.NotNull;

import com.msa.bankingsystem.services.message.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBankRequest {
	
	@NotNull(message = "name" + Messages.VALIDATIONNOTNULLERRORS)
	private String name;
}
