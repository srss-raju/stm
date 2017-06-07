package com.deloitte.smt.controller;

import com.deloitte.smt.dto.AppDataDTO;
import com.deloitte.smt.dto.FileMetaDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cavula
 */
@RestController
@RequestMapping("/camunda/api/appData")
public class AppDataController    {

    @Autowired
    Environment environment;

    @PostMapping(value="/data")
    public AppDataDTO getAppData(){
    	AppDataDTO appData=new AppDataDTO();
    	FileMetaDataDTO fileMetadata=new FileMetaDataDTO();
    	fileMetadata.setMaxFileUploadSize(environment.getProperty("spring.http.multipart.max-file-size"));
    	appData.setFileMetaData(fileMetadata);
    	return appData;
    }

    
}
