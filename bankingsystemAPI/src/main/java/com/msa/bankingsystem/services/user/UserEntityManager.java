package com.msa.bankingsystem.services.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.msa.bankingsystem.core.exception.BusinessException;
import com.msa.bankingsystem.core.results.DataResult;
import com.msa.bankingsystem.core.results.Result;
import com.msa.bankingsystem.core.results.SuccessResult;
import com.msa.bankingsystem.dataAccess.user.IUserRepository;
import com.msa.bankingsystem.models.UserEntity;
import com.msa.bankingsystem.services.message.Messages;
import com.msa.bankingsystem.services.requests.CreateChangeUserActivationRequest;
import com.msa.bankingsystem.services.requests.CreateRegisterRequest;
import com.msa.bankingsystem.services.webSecurityConfig.MyCustomUser;

@Service
public class UserEntityManager implements IUserEntityService {

	private IUserRepository iUserRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserEntityManager(IUserRepository iUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.iUserRepository = iUserRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = this.iUserRepository.loadUserByUsername(username);
		if (userEntity == null) {
			throw new UsernameNotFoundException(username);
		}
		return mapCustomUser(userEntity);
	}

	private MyCustomUser mapCustomUser(UserEntity userEntity) {

		String[] parsedAuthorities = userEntity.getAuthorities().split(",");
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String auth : parsedAuthorities) {
			authorities.add(new SimpleGrantedAuthority(auth));
		}

		MyCustomUser customUser = new MyCustomUser(userEntity.getId(), userEntity.getEmail(), userEntity.getUsername(),
				userEntity.getPassword(), userEntity.isEnabled(), authorities);
	
		return customUser;
	}

	@Override
	public DataResult<UserEntity> register(CreateRegisterRequest createRegisterRequest) {

		checkUsername(createRegisterRequest.getUsername());
		checkEmail(createRegisterRequest.getEmail());

		UserEntity userEntity = mapper(createRegisterRequest);
		this.iUserRepository.save(userEntity);
		return new DataResult<UserEntity>(userEntity, true, Messages.CREATEDUSER);
	}

	@Override
	public Result changeUserActivation(int id ,CreateChangeUserActivationRequest changeUserActivationRequest) {

		checkUserExistsById(id);
		
		this.iUserRepository.updateEnabled(id,
				changeUserActivationRequest.isEnabled());
		
		return new SuccessResult(Messages.CHANGEUSERACTIVATION + changeUserActivationRequest.isEnabled());
	}

	private UserEntity mapper(CreateRegisterRequest createRegisterRequest) {

		UserEntity userEntity = UserEntity.builder().username(createRegisterRequest.getUsername())
				.email(createRegisterRequest.getEmail())
				.password(this.bCryptPasswordEncoder.encode(createRegisterRequest.getPassword())).isEnabled(false)
				.authorities("CREATE_ACCOUNT").build();
		return userEntity;
	}

	private boolean checkUsername(String username) {

		if (this.iUserRepository.loadUserByUsername(username) == null) {
			return true;
		}
		throw new BusinessException(Messages.USERNAMEALREADYEXİTS + username);

	}

	private boolean checkEmail(String email) {

		if (this.iUserRepository.getByEmail(email) == null) {
			return true;
		}
		throw new BusinessException(Messages.EMAILALREADYEXİTS + email);
	}
	
	private boolean checkUserExistsById(int id) {

		if (this.iUserRepository.getById(id) != null) {
			return true;
		}
		throw new BusinessException(Messages.NOTFOUNDUSERBYID + id);
	}

}
