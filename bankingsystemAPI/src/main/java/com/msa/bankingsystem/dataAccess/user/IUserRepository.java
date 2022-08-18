package com.msa.bankingsystem.dataAccess.user;

import org.apache.ibatis.annotations.Mapper;

import com.msa.bankingsystem.models.UserEntity;

@Mapper
public interface IUserRepository {

	
	UserEntity loadUserByUsername(String username);

	void save(UserEntity user);
	
	UserEntity getByEmail(String email);
	
	UserEntity getById(int id);

	void updateEnabled(int id,boolean enabled);	

}
