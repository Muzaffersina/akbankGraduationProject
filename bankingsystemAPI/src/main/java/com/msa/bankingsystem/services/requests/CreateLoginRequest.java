package com.msa.bankingsystem.services.requests;

import javax.validation.constraints.NotNull;

import com.msa.bankingsystem.services.message.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoginRequest {

	@NotNull(message = "username" + Messages.VALIDATIONNOTNULLERRORS)
	private String username;
	@NotNull(message = "password" + Messages.VALIDATIONNOTNULLERRORS)
	private String password;

}
