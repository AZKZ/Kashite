package com.azkz.infrastructure.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseTableEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	private Date creationDate;

	@Column(name = "created_user")
	private String createdUser;

	@Column(name = "created_program")
	private String createdProgram;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date")
	private Date updateDate;

	@Column(name = "update_user")
	private String updateUser;

	@Column(name = "update_program")
	private String updateProgram;

	@Version
	private Integer version;

	@PrePersist
	public void onPrePersist() {
		setUpdateDate(getCreationDate());
		setUpdateUser(getCreatedUser());
		setUpdateProgram(getCreatedProgram());
		setVersion(0);
	}

	@PreUpdate
	public void onPreUpdate() {
		setUpdateDate(new Date());
		setVersion(getVersion() + 1);
	}

}
