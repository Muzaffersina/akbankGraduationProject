package com.msa.bankingsystem.core.mapper;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class GoldPriceServisResponseMapper {
	
	private boolean success;
	private GoldPriceServiceResponseResultMapper[] result;

}
