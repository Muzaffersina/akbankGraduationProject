package com.msa.bankingsystem.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListLoginDto {

	private boolean status;
	private String message;
	private String token;
}
