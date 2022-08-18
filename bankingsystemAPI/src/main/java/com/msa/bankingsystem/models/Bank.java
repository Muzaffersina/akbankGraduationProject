package com.msa.bankingsystem.models;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("Bank")
public class Bank {
	private int id;
	private String name;
	private List<Account> accounts;
}
