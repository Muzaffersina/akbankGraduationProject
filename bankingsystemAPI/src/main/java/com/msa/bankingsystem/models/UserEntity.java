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
@Alias("UserEntity")
public class UserEntity {

	private int id;
	private String username;
	private String email;
	private String password;
	private boolean isEnabled;
	private String authorities;
	private List<Account> accounts;
}
