package com.msa.bankingsystem.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetListAccountDto {

	private String accountNumber;
	private int userId;
	private int bankId;
	private String bankName;
	private String type;
	private double balance;
}
