package com.orenn.coupons.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.orenn.coupons.beans.ResponseFile;
import com.orenn.coupons.beans.ResponseMessage;
import com.orenn.coupons.entities.FileEntity;
import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.FilesController;

@RestController
@RequestMapping("/files")
public class FilesApi {
	
	@Autowired
	private FilesController filesController;
	
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) throws ApplicationException {
		try {
			FileEntity fileEntity = filesController.store(file);
			
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/files/")
					.path(fileEntity.getId())
					.toUriString();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(fileDownloadUri));
		} catch (Exception e) {
			String message = "Could not upload file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) throws ApplicationException {
		FileEntity fileEntity = filesController.getFile(id);
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
				.body(fileEntity.getData());
	}
	
	@GetMapping
	public ResponseEntity<List<ResponseFile>> getFilesList() throws ApplicationException {
		List<ResponseFile> filesList = filesController.getAllFiles().map(fileEntry -> {
			String fileDownloadUri = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/files/")
					.path(fileEntry.getId())
					.toUriString();
			
			return new ResponseFile(fileEntry.getFileName(), fileDownloadUri, fileEntry.getFileType(), fileEntry.getData().length);
		}).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(filesList);
	}

}
