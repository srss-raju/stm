package com.deloitte.smt.util;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.SearchDto;

@Component
public class SearchFilters {
	
	
	public boolean ifUserGroupKey(SearchDto searchDto){
    	return !CollectionUtils.isEmpty(searchDto.getUserGroupKeys());
    }
    
    public boolean ifUserKey(SearchDto searchDto){
    	return !CollectionUtils.isEmpty(searchDto.getUserKeys());
    }
    
    public boolean ifOwner(SearchDto searchDto){
    	return !CollectionUtils.isEmpty(searchDto.getOwners());
    }
    public boolean isOwnerUserKey(SearchDto searchDto){
    	return !CollectionUtils.isEmpty(searchDto.getUserKeys()) && !CollectionUtils.isEmpty(searchDto.getOwners());
    }
    public boolean isOwnerUserGRoupKey(SearchDto searchDto){
    	return !CollectionUtils.isEmpty(searchDto.getUserGroupKeys()) && !CollectionUtils.isEmpty(searchDto.getOwners());
    }
    public boolean isGroupKeyUserKey(SearchDto searchDto){
    	return !CollectionUtils.isEmpty(searchDto.getUserGroupKeys()) && !CollectionUtils.isEmpty(searchDto.getUserKeys());
    }

}
