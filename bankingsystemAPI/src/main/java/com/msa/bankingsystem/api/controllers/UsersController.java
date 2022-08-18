package com.msa.bankingsystem.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msa.bankingsystem.core.mapper.UserEntityDtoMapper;
import com.msa.bankingsystem.core.results.DataResult;
import com.msa.bankingsystem.core.results.Result;
import com.msa.bankingsystem.models.UserEntity;
import com.msa.bankingsystem.services.dtos.GetListLoginDto;
import com.msa.bankingsystem.services.dtos.GetListUserEntityDto;
import com.msa.bankingsystem.services.message.Messages;
import com.msa.bankingsystem.services.requests.CreateChangeUserActivationRequest;
import com.msa.bankingsystem.services.requests.CreateLoginRequest;
import com.msa.bankingsystem.services.requests.CreateRegisterRequest;
import com.msa.bankingsystem.services.user.IUserEntityService;
import com.msa.bankingsystem.services.webSecurityConfig.AuthenticationFilter;
import com.msa.bankingsystem.services.webSecurityConfig.MyCustomUser;

@CrossOrigin(origins = "http://localhost:4200" )
@RestController
@RequestMapping(path = "/api")
public class UsersController {

	private AuthenticationFilter authenticationFilter;
	private IUserEntityService iUserEntityService;
	private UserEntityDtoMapper userEntityDtoMapper;

	@Autowired
	public UsersController(AuthenticationFilter authenticationFilter, IUserEntityService iUserEntityService,
			UserEntityDtoMapper userEntityDtoMapper) {
		this.authenticationFilter = authenticationFilter;
		this.iUserEntityService = iUserEntityService;
		this.userEntityDtoMapper = userEntityDtoMapper;
	}

	@PostMapping("/auth")
	public ResponseEntity<GetListLoginDto> login(@RequestBody @Valid CreateLoginRequest request) {
		
		GetListLoginDto dto = this.authenticationFilter.login(request);
		
		if (dto == null) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GetListLoginDto(false, Messages.LOGINBADCREDENTIALS, null));

		} else if (dto.isStatus() == false) {	
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
		}
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping(path = "/register")
	private ResponseEntity<DataResult<GetListUserEntityDto>> register(
			@RequestBody @Valid CreateRegisterRequest createRegisterRequest) {

		DataResult<GetListUserEntityDto> userDto;

		DataResult<UserEntity> userEntity = this.iUserEntityService.register(createRegisterRequest);

		userDto = new DataResult<GetListUserEntityDto>(this.userEntityDtoMapper.userToUserDto(userEntity.getData()),
				userEntity.isSuccess(), userEntity.getMessage());

		return ResponseEntity.created(null).body(userDto);
	}

	@PatchMapping(path = "/user/{id}")
	private ResponseEntity<Result> changeUserActivation(@PathVariable(name = "id") int id,
			@RequestBody @Valid CreateChangeUserActivationRequest createChangeUserActivationRequest) {

		return ResponseEntity.ok()
				.body(this.iUserEntityService.changeUserActivation(id, createChangeUserActivationRequest));
	}

	@GetMapping(path = "/user")
	private ResponseEntity<UserDetails> getUserDetailsByUserId() {

		MyCustomUser authUser = (MyCustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		UserDetails userDetails = this.iUserEntityService.loadUserByUsername(authUser.getUsername());
		return ResponseEntity.ok().body(userDetails);
	}
}
