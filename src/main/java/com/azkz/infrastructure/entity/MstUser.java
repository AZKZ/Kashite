package com.azkz.infrastructure.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the mst_user database table.
 *
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "mst_user")
public class MstUser extends BaseTableEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String subject;

	private String issuer;

	@Column(name = "enable_flg")
	private String enableFlg;

	private String name;

	public MstUser(String subject, String issuer, String enableFlg, String name, String createdUser,
			String createdProgram) {
		this.setSubject(subject);
		this.setIssuer(issuer);
		this.setEnableFlg(enableFlg);
		this.setName(name);
		this.setCreationDate(new Date());
		this.setCreatedUser(createdUser);
		this.setCreatedProgram(createdProgram);
	}

	public MstUser() {

	}
}
