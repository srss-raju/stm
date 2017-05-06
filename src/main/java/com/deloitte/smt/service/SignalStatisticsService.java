package com.deloitte.smt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.repository.SignalStatisticsRepository;

@Service
public class SignalStatisticsService {
	
	@Autowired
	SignalStatisticsRepository signalStatisticsRepository;

	public List<SignalStatistics> findSignalStatisticsByRunInstanceId(Long runInstanceId) {
		return signalStatisticsRepository.findByRunInstanceId(runInstanceId);
	}

	public List<SignalStatistics> insert(List<SignalStatistics> signalStatistics) {
		return signalStatisticsRepository.save(signalStatistics);
	}

}
