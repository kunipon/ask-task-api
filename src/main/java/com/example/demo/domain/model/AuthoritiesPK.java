package com.example.demo.domain.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Data;

@Data
@Embeddable
public class AuthoritiesPK implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String userId;
	@Enumerated(EnumType.STRING)
	private RoleType authority;
}
