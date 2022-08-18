package com.msa.bankingsystem.services.requests;

import javax.validation.constraints.NotNull;

import com.msa.bankingsystem.services.message.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChangeUserActivationRequest {

	@NotNull(message = "enabled" + Messages.VALIDATIONNOTNULLERRORS)
	private boolean enabled;
}
