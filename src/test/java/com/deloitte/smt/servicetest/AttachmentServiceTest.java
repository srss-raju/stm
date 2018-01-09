package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.service.AttachmentService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class AttachmentServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private AttachmentService attachmentService;
	
	@MockBean
	AttachmentRepository attachmentRepository;
	
		
	@Test
	public void testSave() throws Exception{
		Attachment attachment = new Attachment();
		attachmentService.save(attachment);
	}
	
	@Test
	public void testDelete() throws Exception{
			Attachment attachment = new Attachment();
			attachment.setId(1l);
			given(this.attachmentRepository.findOne(1l)).willReturn(attachment);
			attachmentService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			Attachment attachment = new Attachment();
			given(this.attachmentRepository.findOne(11l)).willReturn(attachment);
			attachmentService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindByResourceIdAndAttachmentType() throws Exception{
		attachmentService.findByResourceIdAndAttachmentType(null, null);
	}
	
	@Test
	public void testFindById() throws Exception{
		Attachment attachment = new Attachment();
		given(this.attachmentRepository.findOne(11l)).willReturn(attachment);
		attachmentService.findById(11l);
	}
	
	@Test
	public void testFindByIdWithNull() {
		try{
			attachmentService.findById(11l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testAddAttachments() throws Exception{
		try{
			 File file = new File("src/test/resources/test.properties");
			 FileInputStream input = new FileInputStream(file);
			 MultipartFile multipartFile = new MockMultipartFile("test", file.getName(), "text/plain", IOUtils.toByteArray(input));
			 Long attachmentResourceId = 1l; 
			 MultipartFile[] attachments = new MultipartFile[1];
			 attachments[0] = multipartFile;
			 List<Long> deletedAttachmentIds = new ArrayList<>(); 
			 deletedAttachmentIds.add(attachmentResourceId);
			 Map<String, Attachment> metaData = new HashMap<>();
			 Attachment attachment = new Attachment();
			 attachment.setAttachmentsURL("a@b.com");
			 attachment.setAttachmentResourceId(1l);
			 attachment.setAttachmentType(AttachmentType.TOPIC_ATTACHMENT);
			 attachment.setContent(multipartFile.getBytes());
			 attachment.setFileName("test");
			 attachment.setDescription("test");
			 metaData.put("test.properties", attachment);
			 given(this.attachmentRepository.save(attachment)).willReturn(attachment);
			 attachmentService.addAttachments(attachmentResourceId, attachments, AttachmentType.TOPIC_ATTACHMENT, deletedAttachmentIds, metaData,"Rajesh");
			 logger.info(multipartFile);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
}
