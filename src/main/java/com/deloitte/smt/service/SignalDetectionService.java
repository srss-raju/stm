package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.DenominatorForPoisson;
import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.IncludeAE;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.repository.DenominatorForPoissonRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IncludeAERepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.util.SignalUtil;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Service
public class SignalDetectionService {

	private static final Logger LOG = Logger.getLogger(SignalDetectionService.class);

	@Autowired
	private IngredientRepository ingredientRepository;
	
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	private SocRepository socRepository;

	@Autowired
	private HlgtRepository hlgtRepository;

	@Autowired
	private HltRepository hltRepository;

	@Autowired
	private PtRepository ptRepository;
	
	@Autowired
	private SignalDetectionRepository signalDetectionRepository;
	
	@Autowired
	private DenominatorForPoissonRepository denominatorForPoissonRepository;
	
	@Autowired
	private IncludeAERepository includeAERepository;
	
	@PersistenceContext
    private EntityManager entityManager;

	public SignalDetection createOrUpdateSignalDetection(SignalDetection signalDetection) {
		
		Calendar c = Calendar.getInstance();
		if(signalDetection.getId() == null) {
			signalDetection.setCreatedDate(c.getTime());
			signalDetection.setLastModifiedDate(c.getTime());
		} else {
			signalDetection.setLastModifiedDate(c.getTime());
		}
		
		signalDetection.setNextRunDate(SignalUtil.getNextRunDate(signalDetection.getRunFrequency(), signalDetection.getCreatedDate()));
		SignalDetection clone = signalDetection;
		clone = signalDetectionRepository.save(clone);
		signalDetection.setId(clone.getId());
		Ingredient ingredient = signalDetection.getIngredient();
        if(ingredient != null) {
            List<Product> products = ingredient.getProducts();
            List<License> licenses = ingredient.getLicenses();
            ingredient.setDetectionId(signalDetection.getId());
			ingredientRepository.deleteByDetectionId(signalDetection.getId());
            ingredient = ingredientRepository.save(ingredient);

            if(!CollectionUtils.isEmpty(products)){
                for (Product singleProduct : products) {
                    singleProduct.setIngredientId(ingredient.getId());
                    singleProduct.setDetectionId(signalDetection.getId());
                }
				productRepository.deleteByDetectionId(signalDetection.getId());
                productRepository.save(products);
            }
            if(!CollectionUtils.isEmpty(licenses)) {
                for (License singleLicense : licenses) {
                    singleLicense.setIngredientId(ingredient.getId());
                    singleLicense.setDetectionId(signalDetection.getId());
                }
				licenseRepository.deleteByDetectionId(signalDetection.getId());
                licenseRepository.save(licenses);
            }
        }
        
        List<Soc> socs  = signalDetection.getSocs();
		if(!CollectionUtils.isEmpty(socs)) {
			for (Soc soc : socs) {
				soc.setDetectionId(signalDetection.getId());
			}
			socRepository.deleteByDetectionId(signalDetection.getId());
			socs = socRepository.save(socs);
			List<Hlgt> hlgts;
			List<Hlt> hlts;
			List<Pt> pts;

			for (Soc soc : socs) {
				hlgts = soc.getHlgts();
				hlts = soc.getHlts();
				pts = soc.getPts();
				if (!CollectionUtils.isEmpty(hlgts)) {
					for (Hlgt hlgt : hlgts) {
						hlgt.setSocId(soc.getId());
						hlgt.setDetectionId(signalDetection.getId());
					}
					hlgtRepository.deleteByDetectionId(signalDetection.getId());
					hlgtRepository.save(hlgts);
				}
				if (!CollectionUtils.isEmpty(hlts)) {
					for (Hlt hlt : hlts) {
						hlt.setSocId(soc.getId());
						hlt.setDetectionId(signalDetection.getId());
					}
					hltRepository.deleteByDetectionId(signalDetection.getId());
					hltRepository.save(hlts);
				}
				if (!CollectionUtils.isEmpty(pts)) {
					for (Pt pt : pts) {
						pt.setSocId(soc.getId());
						pt.setDetectionId(signalDetection.getId());
					}
					ptRepository.deleteByDetectionId(signalDetection.getId());
					ptRepository.save(pts);
				}
			}
		}
    	
    	List<IncludeAE> includeAEs  = signalDetection.getIncludeAEs();
    	List<DenominatorForPoisson> denominatorForPoissons  = signalDetection.getDenominatorForPoisson();
    	
    	if(!CollectionUtils.isEmpty(includeAEs)){
    		for(IncludeAE ae:includeAEs){
    			ae.setDetectionId(signalDetection.getId());
    		}
			includeAERepository.deleteByDetectionId(signalDetection.getId());
    		includeAERepository.save(includeAEs);
    	}
    	if(!CollectionUtils.isEmpty(denominatorForPoissons)){
    		for(DenominatorForPoisson dfp:denominatorForPoissons){
    			dfp.setDetectionId(signalDetection.getId());
    		}
			denominatorForPoissonRepository.deleteByDetectionId(signalDetection.getId());
    		denominatorForPoissonRepository.save(denominatorForPoissons);
    	}
		return signalDetection;
	}
	
	/*public String updateSignalDetection(SignalDetection signalDetection) throws UpdateFailedException, IOException {
        if(signalDetection.getId() == null) {
            throw new UpdateFailedException("Update failed for Topic, since it does not have any valid Id field.");
        }
        signalDetection.setLastModifiedDate(new Date());
        signalDetectionRepository.save(signalDetection);
        return "Update Success";
    }*/
	
	public void delete(Long signalDetectionId) throws DeleteFailedException {
		SignalDetection signalDetection = signalDetectionRepository.findOne(signalDetectionId);
        if(signalDetection == null) {
            throw new DeleteFailedException("Failed to delete Action. Invalid Id received");
        }
        signalDetectionRepository.delete(signalDetection);
    }
	
	public SignalDetection findById(Long id) throws EntityNotFoundException {
		SignalDetection signalDetection = signalDetectionRepository.findOne(id);
		if(null == signalDetection) {
            throw new EntityNotFoundException("Signal Detection not found with given Id :"+id);
        }
        addOtherInfoToSignalDetection(Arrays.asList(signalDetection));
		return signalDetection;
    }
	
	 public List<SignalDetection> findAllForSearch(SearchDto searchDto) {
	        List<SignalDetection> signalDetections;
	        Set<Long> signalDetectionIds = new HashSet<>();
		 List<Ingredient> ingredients;
		List<Product> products = new ArrayList<Product>();
		List<License> licenses = new ArrayList<License>();
		List<Soc> socs;
		List<Hlgt> hlgts;
		List<Hlt> hlts;
		List<Pt> pts;

		StringBuilder queryString = new StringBuilder("SELECT o FROM SignalDetection o WHERE 1=1 ");

		if (searchDto != null) {

			if (!CollectionUtils.isEmpty(searchDto.getProducts())) {
				products = productRepository.findAllByProductNameIn(searchDto.getProducts());
				products.parallelStream().forEach(product -> {
					if (product.getDetectionId() != null) {
						signalDetectionIds.add(product.getDetectionId());
					}
				});
			}

			if (!CollectionUtils.isEmpty(searchDto.getIngredients())) {
				ingredients = ingredientRepository.findAllByIngredientNameIn(searchDto.getIngredients());
				ingredients.parallelStream().forEach(ingredient -> {
					if (ingredient.getDetectionId() != null) {
						signalDetectionIds.add(ingredient.getDetectionId());
					}
				});
			}

			if (!CollectionUtils.isEmpty(searchDto.getLicenses())) {
				licenses = licenseRepository.findAllByLicenseNameIn(searchDto.getLicenses());
				licenses.parallelStream().forEach(license -> {
					if (license.getDetectionId() != null) {
						signalDetectionIds.add(license.getDetectionId());
					}
				});
			}
			if (!CollectionUtils.isEmpty(searchDto.getSocs())) {
				socs = socRepository.findAllBySocNameIn(searchDto.getSocs());
				socs.parallelStream().forEach(soc -> {
					if (soc.getDetectionId() != null) {
						signalDetectionIds.add(soc.getDetectionId());
					}
				});
			}
			if (!CollectionUtils.isEmpty(searchDto.getHlgts())) {
				hlgts = hlgtRepository.findAllByHlgtNameIn(searchDto.getHlgts());
				hlgts.parallelStream().forEach(hlgt -> {
					if (hlgt.getDetectionId() != null) {
						signalDetectionIds.add(hlgt.getDetectionId());
					}
				});
			}

			if (!CollectionUtils.isEmpty(searchDto.getHlts())) {
				hlts = hltRepository.findAllByHltNameIn(searchDto.getHlts());
				hlts.parallelStream().forEach(hlt -> {
					if (hlt.getDetectionId() != null) {
						signalDetectionIds.add(hlt.getDetectionId());
					}
				});
			}

			if (!CollectionUtils.isEmpty(searchDto.getPts())) {
				pts = ptRepository.findAllByPtNameIn(searchDto.getPts());
				pts.parallelStream().forEach(pt -> {
					if (pt.getDetectionId() != null) {
						signalDetectionIds.add(pt.getDetectionId());
					}
				});
			}

			if (signalDetectionIds.size() > 0) {
				queryString.append(" AND id IN :ids ");
			}

			if (StringUtils.isNotBlank(searchDto.getDescription())) {
				queryString.append(" AND description IN :description ");
			}

			if (!CollectionUtils.isEmpty(searchDto.getFrequency())) {
				queryString.append(" AND runFrequency IN :runFrequency");
			}

		} 

		queryString.append(" ORDER BY createdDate DESC");
		Query q = entityManager.createQuery(queryString.toString(), SignalDetection.class);

		if (null != searchDto) {
			if (queryString.toString().contains(":ids")) {
				if (CollectionUtils.isEmpty(signalDetectionIds)) {
					q.setParameter("ids", null);
				} else {
					q.setParameter("ids", signalDetectionIds);
				}
			}
			if (queryString.toString().contains(":description")) {
				q.setParameter("description", searchDto.getDescription());
			}
			if (queryString.toString().contains(":runFrequency")) {
				q.setParameter("runFrequency", searchDto.getFrequency());
			}
		}
		
		signalDetections = q.getResultList();

		if (!CollectionUtils.isEmpty(signalDetections)) {
			addOtherInfoToSignalDetection(signalDetections);
		}

		// Set<String> signalDetectedProductNames =
		// products.stream().filter(product -> null != product.getDetectionId())
		// .map(Product::getProductName).collect(Collectors.toSet());
		//
		// Set<String> signalDetectedLicenseNames =
		// licenses.stream().filter(license -> null != license.getDetectionId())
		// .map(License::getLicenseName).collect(Collectors.toSet());

		// signalDetections.parallelStream().forEach(signalDetection -> {
		// List<Product> matchedProducts =
		// signalDetection.getIngredient().getProducts().stream()
		// .filter(product ->
		// signalDetectedProductNames.contains(product.getProductName())).collect(Collectors.toList());
		// signalDetection.getIngredient().setProducts(matchedProducts);
		//
		// List<License> matchedLicenses =
		// signalDetection.getIngredient().getLicenses().stream()
		// .filter(product ->
		// signalDetectedLicenseNames.contains(product.getLicenseName())).collect(Collectors.toList());
		// signalDetection.getIngredient().setLicenses(matchedLicenses);
		//
		// });
		//
		// List<SignalDetection> filtered=signalDetections.stream()
		// .filter(signalDetection ->
		// !CollectionUtils.isEmpty(signalDetection.getIngredient().getProducts())
		// ||
		// !CollectionUtils.isEmpty(signalDetection.getIngredient().getLicenses()))
		// .collect(Collectors.toList());

		return signalDetections;
	}

	private void addOtherInfoToSignalDetection(List<SignalDetection> signalDetections) {
		signalDetections.parallelStream().forEach((signalDetection) -> {
			Ingredient ingredient;
			ingredient = ingredientRepository.findByDetectionId(signalDetection.getId());
			List<Product> products;
				products = productRepository.findByDetectionId(signalDetection.getId());

			List<License> licenses;
				licenses = licenseRepository.findByDetectionId(signalDetection.getId());

			if(ingredient != null) {
				ingredient.setProducts(products);
				ingredient.setLicenses(licenses);
				signalDetection.setIngredient(ingredient);
			}

			List<Soc> socs;
				socs = socRepository.findByDetectionId(signalDetection.getId());
			if (!CollectionUtils.isEmpty(socs)) {
				for (Soc soc : socs) {
					soc.setHlgts(hlgtRepository.findByDetectionId(signalDetection.getId()));

					soc.setHlts(hltRepository.findByDetectionId(signalDetection.getId()));
					soc.setPts(ptRepository.findByDetectionId(signalDetection.getId()));
				}
			}
			signalDetection.setSocs(socs);

			List<DenominatorForPoisson> denominatorForPoissonList = denominatorForPoissonRepository.findByDetectionId(signalDetection.getId());
			List<IncludeAE> includeAEList = includeAERepository.findByDetectionId(signalDetection.getId());
			signalDetection.setDenominatorForPoisson(denominatorForPoissonList);
			signalDetection.setIncludeAEs(includeAEList);
		});
	}
	
	public List<SignalDetection> ganttDetections(List<SignalDetection> detections) {
		if (!CollectionUtils.isEmpty(detections)) {
			for(SignalDetection signalDetection:detections){
				createGanttSignalDetections(signalDetection);
			}
		}
		return detections;
    }

	private void createGanttSignalDetections(SignalDetection signalDetection) {
		List<Date> nextRunDates = new ArrayList<>();
		Date createdDate = signalDetection.getCreatedDate();
		for(int i=0; i<Integer.parseInt(signalDetection.getWindowType()); i++){
			createdDate = SignalUtil.getNextRunDate(signalDetection.getRunFrequency(), createdDate);
			nextRunDates.add(createdDate);
			LOG.info("Next Run Date  -->> "+createdDate);
		}
		signalDetection.setNextRunDates(nextRunDates);
	}
}
