package com.msa.bankingsystem.services.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.msa.bankingsystem.core.results.DataResult;
import com.msa.bankingsystem.core.results.Result;
import com.msa.bankingsystem.models.UserEntity;
import com.msa.bankingsystem.services.requests.CreateChangeUserActivationRequest;
import com.msa.bankingsystem.services.requests.CreateRegisterRequest;

public interface IUserEntityService extends UserDetailsService {
	
	DataResult<UserEntity> register(CreateRegisterRequest createRegisterRequest);
	
	Result changeUserActivation(int id, CreateChangeUserActivationRequest changeUserActivationRequest);
}
