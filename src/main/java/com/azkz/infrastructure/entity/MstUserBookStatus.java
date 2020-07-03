package com.azkz.infrastructure.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the mst_book database table.
 *
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "mst_user_book_status")
public class MstUserBookStatus extends BaseTableEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private char status;

	private String name;

	private String description;

	public MstUserBookStatus() {

	}
}
