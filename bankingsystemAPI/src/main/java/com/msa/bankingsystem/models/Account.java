package com.msa.bankingsystem.models;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("Account")
public class Account {
	private int id;
	private UserEntity user; 
	private Bank bank;
	private String accountNumber;
	private String type;
	private double balance;
	private LocalDateTime lastUpdateDate;
	private LocalDateTime creationDate;
	private boolean isDeleted;
}
