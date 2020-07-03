package com.azkz.infrastructure.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the view_tag_map_list database table.
 *
 */
@Entity
@Data
@Table(name = "view_tag_map_list")
public class ViewTagMapList implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "tag_id")
	private long tagId;

	@Column(name = "tag_map_id")
	private long tagMapId;

	@Column(name = "tag_name")
	private String tagName;

	public ViewTagMapList() {
	}

	public long getTagId() {
		return this.tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public long getTagMapId() {
		return this.tagMapId;
	}

	public void setTagMapId(long tagMapId) {
		this.tagMapId = tagMapId;
	}

	public String getTagName() {
		return this.tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
