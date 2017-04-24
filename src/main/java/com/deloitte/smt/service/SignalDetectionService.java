package com.deloitte.smt.service;

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
import com.deloitte.smt.exception.UpdateFailedException;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public String createSignalDetection(SignalDetection signalDetection) {
		
		Calendar c = Calendar.getInstance();
		signalDetection.setCreatedDate(c.getTime());
		signalDetection.setLastModifiedDate(c.getTime());
		signalDetection = signalDetectionRepository.save(signalDetection);
        
		Ingredient ingredient = signalDetection.getIngredient();
        if(ingredient != null) {
            List<Product> products = ingredient.getProducts();
            List<License> licenses = ingredient.getLicenses();
            ingredient.setTopicId(signalDetection.getId()); // Topic ID is same as Signal Detection Id
            ingredient = ingredientRepository.save(ingredient);

            if(!CollectionUtils.isEmpty(products)){
                for (Product singleProduct : products) {
                    singleProduct.setIngredientId(ingredient.getId());
                    singleProduct.setTopicId(signalDetection.getId());
                }
                productRepository.save(products);
            }
            if(!CollectionUtils.isEmpty(licenses)) {
                for (License singleLicense : licenses) {
                    singleLicense.setIngredientId(ingredient.getId());
                    singleLicense.setTopicId(signalDetection.getId());
                }
                licenseRepository.save(licenses);
            }
        }
        
        List<Soc> socs  = signalDetection.getSocs();
    	for(Soc soc:socs){
    		soc.setTopicId(signalDetection.getId());
    	}
    	socs = socRepository.save(socs);
    	List<Hlgt> hlgts;
    	List<Hlt> hlts;
    	List<Pt> pts;
    	
    	for(Soc soc:socs){
    		hlgts = soc.getHlgts();
    		hlts = soc.getHlts();
    		pts = soc.getPts();
    		if(!CollectionUtils.isEmpty(hlgts)){
    			for(Hlgt hlgt:hlgts){
    				hlgt.setSocId(soc.getId());
    				hlgt.setTopicId(signalDetection.getId());
    			}
    			hlgtRepository.save(hlgts);
    		}
    		if(!CollectionUtils.isEmpty(hlts)){
    			for(Hlt hlt:hlts){
    				hlt.setSocId(soc.getId());
    				hlt.setTopicId(signalDetection.getId());
    			}
    			hltRepository.save(hlts);
    		}
    		if(!CollectionUtils.isEmpty(pts)){
    			for(Pt pt:pts){
    				pt.setSocId(soc.getId());
    				pt.setTopicId(signalDetection.getId());
    			}
    			ptRepository.save(pts);
    		}
    	}
    	
    	List<IncludeAE> includeAEs  = signalDetection.getIncludeAEs();
    	List<DenominatorForPoisson> denominatorForPoissons  = signalDetection.getDenominatorForPoisson();
    	
    	if(!CollectionUtils.isEmpty(includeAEs)){
    		for(IncludeAE ae:includeAEs){
    			ae.setDetectionId(signalDetection.getId());
    		}
    		includeAERepository.save(includeAEs);
    	}
    	if(!CollectionUtils.isEmpty(denominatorForPoissons)){
    		for(DenominatorForPoisson dfp:denominatorForPoissons){
    			dfp.setDetectionId(signalDetection.getId());
    		}
    		denominatorForPoissonRepository.save(denominatorForPoissons);
    	}
		return "Saved Successfully";
	}
	
	public String updateSignalDetection(SignalDetection signalDetection) throws UpdateFailedException, IOException {
        if(signalDetection.getId() == null) {
            throw new UpdateFailedException("Update failed for Topic, since it does not have any valid Id field.");
        }
        signalDetection.setLastModifiedDate(new Date());
        signalDetectionRepository.save(signalDetection);
        return "Update Success";
    }
	
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
		Ingredient ingredient = ingredientRepository.findByTopicId(signalDetection.getId());
        List<Product> products = productRepository.findByTopicId(signalDetection.getId());
        List<License> licenses = licenseRepository.findByTopicId(signalDetection.getId());
        if(ingredient != null) {
            ingredient.setProducts(products);
            ingredient.setLicenses(licenses);
            signalDetection.setIngredient(ingredient);
        }
        List<Soc> socs  = socRepository.findByTopicId(signalDetection.getId());
        if(!CollectionUtils.isEmpty(socs)) {
        	for(Soc soc:socs){
        		soc.setHlgts(hlgtRepository.findBySocId(soc.getId()));
        		soc.setHlts(hltRepository.findBySocId(soc.getId()));
        		soc.setPts(ptRepository.findBySocId(soc.getId()));
        	}
        }
        signalDetection.setSocs(socs);
        
        List<DenominatorForPoisson> denominatorForPoissonList =  denominatorForPoissonRepository.findByDetectionId(signalDetection.getId());
        List<IncludeAE> includeAEList = includeAERepository.findByDetectionId(signalDetection.getId());
        signalDetection.setDenominatorForPoisson(denominatorForPoissonList);
        signalDetection.setIncludeAEs(includeAEList);
        
        return signalDetection;
    }
	
	 public List<SignalDetection> findAllForSearch(SearchDto searchDto) {
	        List<SignalDetection> signalDetections;
	        Set<Long> signalDetectionIds = new HashSet<>();
	        if(!CollectionUtils.isEmpty(searchDto.getProducts())) {
	            List<Product> products = productRepository.findAllByProductNameIn(searchDto.getProducts());
	            products.parallelStream().forEach(product -> signalDetectionIds.add(product.getTopicId()));
	        }
	        if(!CollectionUtils.isEmpty(searchDto.getLicenses())) {
	            List<License> licenses = licenseRepository.findAllByLicenseNameIn(searchDto.getLicenses());
	            licenses.parallelStream().forEach(product -> signalDetectionIds.add(product.getTopicId()));
	        }
	        if(!CollectionUtils.isEmpty(searchDto.getSocs())) {
	            List<Ingredient> socs = socRepository.findAllBySocNameIn(searchDto.getSocs());
	            socs.parallelStream().forEach(product -> signalDetectionIds.add(product.getTopicId()));
	        }
	        if(!CollectionUtils.isEmpty(searchDto.getSocs())) {
	            List<Ingredient> socs = socRepository.findAllBySocNameIn(searchDto.getSocs());
	            socs.parallelStream().forEach(soc -> signalDetectionIds.add(soc.getTopicId()));
	        }
	        
	        if(!CollectionUtils.isEmpty(searchDto.getHlgts())) {
	            List<Hlgt> hlgts = hlgtRepository.findAllByhlgtNameIn(searchDto.getHlgts());
	            hlgts.parallelStream().forEach(hlgt -> signalDetectionIds.add(hlgt.getTopicId()));
	        }
	        
	        if(!CollectionUtils.isEmpty(searchDto.getHlts())) {
	            List<Hlt> hlts = hltRepository.findAllByhltNameIn(searchDto.getHlts());
	            hlts.parallelStream().forEach(hlt -> signalDetectionIds.add(hlt.getTopicId()));
	        }
	        
	        if(!CollectionUtils.isEmpty(searchDto.getPts())) {
	            List<Pt> pts = ptRepository.findAllByPtNameIn(searchDto.getPts());
	            pts.parallelStream().forEach(pt -> signalDetectionIds.add(pt.getTopicId()));
	        }

	        StringBuilder queryString = new StringBuilder("SELECT o FROM SignalDetection o WHERE 1=1 ");
	        if(!CollectionUtils.isEmpty(searchDto.getProducts()) || !CollectionUtils.isEmpty(searchDto.getLicenses()) || !CollectionUtils.isEmpty(searchDto.getIngredients())){
	            queryString.append(" AND id IN :ids ");
	        }
	        
	        queryString.append(" ORDER BY createdDate DESC");
	        Query q = entityManager.createQuery(queryString.toString(), SignalDetection.class);

	        if(queryString.toString().contains(":ids")){
	            if(CollectionUtils.isEmpty(signalDetectionIds)) {
	                q.setParameter("ids", null);
	            } else {
	                q.setParameter("ids", signalDetectionIds);
	            }
	        }
	        signalDetections = q.getResultList();
	        return signalDetections;
	    }
}
