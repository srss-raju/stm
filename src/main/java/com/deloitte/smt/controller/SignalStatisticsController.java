package com.deloitte.smt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.exception.EntityAlreadyExistsException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.service.SignalStatisticsService;

@RestController
@RequestMapping(value = "/camunda/api/signal/statistics")
public class SignalStatisticsController {
	@Autowired
	SignalStatisticsService signalStatisticsService;
	
	@PostMapping
    public List<SignalStatistics> createSignalStatistics(@RequestBody List<SignalStatistics> signalStatistics) throws EntityAlreadyExistsException {
        return signalStatisticsService.insert(signalStatistics);
    }

    @GetMapping(value = "/{runInstanceId}")
    public List<SignalStatistics> findSignalStatisticsByRunInstanceId(@PathVariable Long runInstanceId) throws EntityNotFoundException {
        return signalStatisticsService.findSignalStatisticsByRunInstanceId(runInstanceId);
    }
}
