package com.orenn.coupons.logic;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.orenn.coupons.dao.IFilesDao;
import com.orenn.coupons.entities.FileEntity;
import com.orenn.coupons.exceptions.ApplicationException;

@Controller
public class FilesController {
	
	@Autowired
	private IFilesDao filesDao;
	
	public FileEntity store(MultipartFile file) throws ApplicationException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileEntity fileEntity = new FileEntity();
		fileEntity.setFileName(fileName);
		fileEntity.setFileType(file.getContentType());
		try {
			fileEntity.setData(file.getBytes());
		} catch (IOException e) {
			throw new ApplicationException();
		}
		
		return filesDao.save(fileEntity);
	}
	
	public FileEntity getFile(String id) throws ApplicationException {
		try {
			return filesDao.findById(id).get();
		} catch (Exception e) {
			throw new ApplicationException();
		}
	}
	
	public Stream<FileEntity> getAllFiles() throws ApplicationException {
		try {
			return filesDao.findAll().stream();
		} catch (Exception e) {
			throw new ApplicationException();
		}
	}

}
