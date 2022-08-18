package com.msa.bankingsystem.services.webSecurityConfig;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MyCustomUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String email;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public MyCustomUser(int id, String email, String username, String password, boolean isEnabled,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, isEnabled, true, true, true, authorities);
		this.id = id;
		this.email = email;
	}

}
