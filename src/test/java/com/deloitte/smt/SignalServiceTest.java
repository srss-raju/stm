package com.deloitte.smt;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.service.SignalService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	  DbUnitTestExecutionListener.class })
// @TransactionConfiguration(transactionManager="transactionManager",
// defaultRollback=false)
//@TransactionConfiguration(defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SignalManagementApplication.class)
//@DbUnitConfiguration(databaseConnection = { "dataSource" })
public class SignalServiceTest {
	@Autowired
	SignalService signalService;
	
	@Autowired
	DataSource dataSource;
	
	
	//@DatabaseSetup(value = { "/sm_assignment_configuration.xml","/sm_signal_configuration.xml" })
	@DatabaseSetup(value = { "/sm_assignment_configuration.xml"})
	@DatabaseTearDown(value = { "/sm_assignment_configuration.xml" })
	@Test
	public void testCreateTopic() throws IOException {
		Topic createdTopic = signalService.createTopic(createTopic(), null);
		assertNotNull(createdTopic);
		assertTrue(createdTopic.getId() > 0);
	}

	private Topic createTopic() {
		Topic topic = new Topic();
		topic.setAssignTo("Safety Analyst");
		topic.setCaselistId("0");
		topic.setCases(null);
		topic.setCohortPercentage(10l);
		topic.setConfidenceIndex(10l);
		topic.setCreatedBy(null);
		topic.setDescription(
				"Holt EM, Steiner R, et al. Six cases of myocardial infarction in young males taking ibuprofen. New England Journal of Medicine. 2017 Mar 18; 43(1):89-96.");
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, 5, 1);
		topic.setEndDate(calendar.getTime());
		topic.setIngredient(createIngredient());
		topic.setSocs(addSoc());
		topic.setSourceName("Literature");
		topic.setSignalUrls(addUrls());
		return topic;

	}

	private List<SignalURL> addUrls() {
		List<SignalURL> urls = new ArrayList<SignalURL>();
		SignalURL signalUrl = new SignalURL();
		signalUrl.setUrl("www.nejm.com/casestudies/ibuprofen/myocardialinfarction");
		urls.add(signalUrl);
		return urls;
	}

	private Ingredient createIngredient() {
		Ingredient ingredient = new Ingredient();
		ingredient.setIngredientName("IBUPROFEN");
		ingredient.setProducts(addProducts());
		return ingredient;
	}

	private List<Product> addProducts() {
		Product product1 = new Product();
		product1.setProductName("IBUPROFEN ( Oral Suspension )");

		List<Product> products = new ArrayList<Product>();
		products.add(product1);
		return products;
	}

	private List<Soc> addSoc() {
		Soc soc1 = new Soc();
		soc1.setSocName("CARDIAC DISORDERS");
		soc1.setPts(addPt());

		List<Soc> socList = new ArrayList<Soc>();
		socList.add(soc1);
		return socList;

	}

	private List<Pt> addPt() {
		Pt pt1 = new Pt();
		pt1.setPtName("MYOCARDIAL INFARCTION");

		List<Pt> pts = new ArrayList<Pt>();
		pts.add(pt1);

		return pts;
	}

}
