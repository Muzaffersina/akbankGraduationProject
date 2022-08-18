package com.msa.bankingsystem.core.mapper;

import org.springframework.stereotype.Component;

import com.msa.bankingsystem.models.UserEntity;
import com.msa.bankingsystem.services.dtos.GetListUserEntityDto;

@Component
public class UserEntityDtoMapper {

	public GetListUserEntityDto userToUserDto(UserEntity userEntity) {
		return new GetListUserEntityDto().builder()
				.username(userEntity.getUsername())
				.email(userEntity.getEmail())
				.build();
	}
}
