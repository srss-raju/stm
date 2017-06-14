package com.deloitte.smt.controller;

import com.deloitte.smt.entity.SMUser;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.service.SMUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by myelleswarapu on 04-05-2017.
 */
@RestController
@RequestMapping("/camunda/api/signal/user")
public class SMUserController {

    @Autowired
    SMUserService smUserService;

    @PostMapping
    public SMUser createNewUser(@RequestBody SMUser smUser) {
        return smUserService.insert(smUser);
    }

    @PutMapping
    public SMUser updateUser(@RequestBody SMUser smUser) throws UpdateFailedException {
        return smUserService.update(smUser);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws EntityNotFoundException {
        smUserService.delete(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}")
    public SMUser findAssignmentConfigurationById(@PathVariable Long userId) throws EntityNotFoundException {
        return smUserService.findById(userId);
    }

    @GetMapping
    public List<SMUser> findAll() {
        return smUserService.findAll();
    }

}
