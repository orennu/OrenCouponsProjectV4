package com.orenn.coupons.entities;

import java.util.Arrays;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "files")
public class FileEntity {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	@Column(name = "file_name", nullable = false)
	private String fileName;
	
	@Column(name = "file_type", nullable = false)
	private String fileType;
	
	@Lob
	private byte[] data;
	
	@OneToOne(mappedBy = "image")
	private CouponEntity coupon;

	public FileEntity() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public CouponEntity getCoupon() {
		return coupon;
	}

	public void setCoupon(CouponEntity coupon) {
		this.coupon = coupon;
	}

	@Override
	public String toString() {
		return "FileEntity [id=" + id + ", fileName=" + fileName + ", fileType=" + fileType + ", data="
				+ Arrays.toString(data) + ", coupon=" + coupon + "]";
	}
	
}
