package com.deloitte.smt.service;

import com.deloitte.smt.entity.SMUser;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.SMUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 04-05-2017.
 */
@Service
public class SMUserService {

    @Autowired
    SMUserRepository smUserRepository;

    public SMUser insert(SMUser smUser) {
        smUser.setCreatedDate(new Date());
        smUser.setLastModifiedDate(new Date());
        return smUserRepository.save(smUser);
    }

    public SMUser update(SMUser smUser) throws UpdateFailedException {
        if(smUser.getId() == null) {
            throw new UpdateFailedException("Required field Id is no present in the given request.");
        }
        smUser.setLastModifiedDate(new Date());
        smUser = smUserRepository.save(smUser);
        return smUser;
    }

    public void delete(Long smUserId) throws EntityNotFoundException {
        SMUser smUser = smUserRepository.findOne(smUserId);
        if(smUser == null) {
            throw new EntityNotFoundException("User not found with the given Id : "+smUserId);
        }
        smUserRepository.delete(smUser);
    }

    public SMUser findById(Long smUserId) throws EntityNotFoundException {
        SMUser smUser = smUserRepository.findOne(smUserId);
        if(smUser == null) {
            throw new EntityNotFoundException("User not found with the given Id : "+smUserId);
        }
        return smUser;
    }

    public List<SMUser> findAll() {
        return smUserRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
    }
}
