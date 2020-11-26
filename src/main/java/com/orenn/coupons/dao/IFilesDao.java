package com.orenn.coupons.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orenn.coupons.entities.FileEntity;

@Repository
public interface IFilesDao extends JpaRepository<FileEntity, String>{

}
