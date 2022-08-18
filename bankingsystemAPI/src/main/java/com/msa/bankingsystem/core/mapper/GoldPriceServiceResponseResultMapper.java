package com.msa.bankingsystem.core.mapper;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class GoldPriceServiceResponseResultMapper {
	private String name;
	private double buying ;
	private String buyingstr ;
	private double selling ;
	private String sellingstr ;	
}
