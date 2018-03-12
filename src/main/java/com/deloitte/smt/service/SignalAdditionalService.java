package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.dto.DetectionTopicDTO;
import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.util.DateUtil;

@Service
public class SignalAdditionalService {
	
	@Autowired
	SignalService signalService;

	public void saveSignalsAndNonSignals(List<DetectionTopicDTO> detectionTopicDTOs) throws ApplicationException{
		if(!CollectionUtils.isEmpty(detectionTopicDTOs)){
			for(DetectionTopicDTO dto:detectionTopicDTOs){
				if(dto.getISsignal() >= 1){
					Topic topic = new Topic();
					StringBuilder nameBuilder = new StringBuilder(dto.getProductName()).append("_").append(dto.getPtDesc()).append("_").append(DateUtil.convertDateToString(new Date()));
					topic.setName(nameBuilder.toString());
					topic.setSourceName("Case");
					topic.setRunInstanceId(Long.parseLong(dto.getRunInstanceId()));
					topic.setProductKey(String.valueOf(dto.getProductKey()));
					topic.setCases(dto.getNumDrugPt());
					setSocsAndPts(dto, topic);
					setStatistics(dto, topic);
					signalService.createTopic(topic, null);
				}else{
					NonSignal nonSignal = new NonSignal();
					StringBuilder nameBuilder = new StringBuilder(dto.getProductName()).append(dto.getPtDesc());
					nonSignal.setName(nameBuilder.toString());
					nonSignal.setRunInstanceId(Long.parseLong(dto.getRunInstanceId()));
					nonSignal.setCases(dto.getNumDrugPt());
					signalService.createOrupdateNonSignal(nonSignal);
				}
				
			}
		}
	}

	private void setSocsAndPts(DetectionTopicDTO dto, Topic topic) {
		List<Soc> socs = new ArrayList<>();
		Soc soc = new Soc();
		soc.setSocName(dto.getSocDesc());
		List<Pt> pts = new ArrayList<>();
		Pt pt = new Pt();
		pt.setPtName(dto.getPtDesc());
		pts.add(pt);
		soc.setPts(pts);
		socs.add(soc);
		topic.setSocs(socs);
	}

	private void setStatistics(DetectionTopicDTO dto, Topic topic) {
		Set<SignalStatistics> statistics;
		statistics = new HashSet<>();
		
		
		setPrr(dto, statistics);
		setRor(dto, statistics);
		setRrr(dto, statistics);
		setEbgm(dto, statistics);
		setBcpnn(dto, statistics);
		
		topic.setSignalStatistics(statistics);
	}

	private void setPrr(DetectionTopicDTO dto, Set<SignalStatistics> statistics) {
		SignalStatistics signalStatistics = new SignalStatistics();
		signalStatistics.setAlgorithm("PRR");
		signalStatistics.setRunInstanceId(Long.parseLong(dto.getRunInstanceId()));
		if(dto.getPrrScore() != null){
			signalStatistics.setScore(Double.parseDouble(dto.getPrrScore()));
		}else{
			signalStatistics.setScore(0.0);
		}
		if(dto.getPrrCi95Lb() != null){
			signalStatistics.setLb(Double.parseDouble(dto.getPrrCi95Lb()));
		}else{
			signalStatistics.setLb(Double.parseDouble(dto.getRorCi95Lb()));
		}
		if(dto.getPrrCi95Ub() != null){
			signalStatistics.setUb(Double.parseDouble(dto.getPrrCi95Ub()));
		}else{
			signalStatistics.setUb(0.0);
		}
		if(dto.getPrrStDev() != null){
			signalStatistics.setSe(Double.parseDouble(dto.getPrrStDev()));
		}else{
			signalStatistics.setSe(0.0);
		}
		
		statistics.add(signalStatistics);
	}

	private void setRor(DetectionTopicDTO dto, Set<SignalStatistics> statistics) {
		SignalStatistics signalStatistics = new SignalStatistics();
		signalStatistics.setAlgorithm("ROR");
		signalStatistics.setRunInstanceId(Long.parseLong(dto.getRunInstanceId()));
		if(dto.getRorScore() != null){
			signalStatistics.setScore(Double.parseDouble(dto.getRorScore()));
		}else{
			signalStatistics.setScore(0.0);
		}
		if(dto.getRorCi95Lb() != null){
			signalStatistics.setLb(Double.parseDouble(dto.getRorCi95Lb()));
		}else{
			signalStatistics.setLb(0.0);
		}
		if(dto.getRorCi95Ub() != null){
			signalStatistics.setUb(Double.parseDouble(dto.getRorCi95Ub()));
		}else{
			signalStatistics.setUb(0.0);
		}
		if(dto.getRorStDev() != null){
			signalStatistics.setSe(Double.parseDouble(dto.getRorStDev()));
		}else{
			signalStatistics.setSe(0.0);
		}
		statistics.add(signalStatistics);
	}

	private void setRrr(DetectionTopicDTO dto, Set<SignalStatistics> statistics) {
		SignalStatistics signalStatistics = new SignalStatistics();
		signalStatistics.setAlgorithm("RRR");
		signalStatistics.setRunInstanceId(Long.parseLong(dto.getRunInstanceId()));
		if(dto.getRrrScore() != null){
			signalStatistics.setScore(Double.parseDouble(dto.getRrrScore()));
		}else{
			signalStatistics.setScore(0.0);
		}
		if(dto.getRrrCi95Lb() != null){
			signalStatistics.setLb(Double.parseDouble(dto.getRrrCi95Lb()));
		}else{
			signalStatistics.setLb(0.0);
		}
		if(dto.getRrrCi95Ub() != null){
			signalStatistics.setUb(Double.parseDouble(dto.getRrrCi95Ub()));
		}else{
			signalStatistics.setUb(0.0);
		}
		if(dto.getRrrStDev() != null){
			signalStatistics.setSe(Double.parseDouble(dto.getRrrStDev()));
		}else{
			signalStatistics.setSe(0.0);
		}
		statistics.add(signalStatistics);
	}

	private void setEbgm(DetectionTopicDTO dto, Set<SignalStatistics> statistics) {
		SignalStatistics signalStatistics = new SignalStatistics();
		signalStatistics.setAlgorithm("EBGM");
		signalStatistics.setRunInstanceId(Long.parseLong(dto.getRunInstanceId()));
		if(dto.getMgpsScore() != null){
			signalStatistics.setScore(Double.parseDouble(dto.getMgpsScore()));
		}else{
			signalStatistics.setScore(0.0);
		}
		if(dto.getMgpsCi95Lb() != null){
			signalStatistics.setLb(Double.parseDouble(dto.getMgpsCi95Lb()));
		}else{
			signalStatistics.setLb(0.0);
		}
		if(dto.getMgpsCi95Ub() != null){
			signalStatistics.setUb(Double.parseDouble(dto.getMgpsCi95Ub()));
		}else{
			signalStatistics.setUb(0.0);
		}
		statistics.add(signalStatistics);
	}

	private void setBcpnn(DetectionTopicDTO dto, Set<SignalStatistics> statistics) {
		SignalStatistics signalStatistics = new SignalStatistics();
		signalStatistics.setAlgorithm("BCPNN");
		signalStatistics.setRunInstanceId(Long.parseLong(dto.getRunInstanceId()));
		if(dto.getBcpnnScore() != null){
			signalStatistics.setScore(Double.parseDouble(dto.getBcpnnScore()));
		}else{
			signalStatistics.setScore(0.0);
		}
		if(dto.getBcpnnCi95Lb() != null){
			signalStatistics.setLb(Double.parseDouble(dto.getBcpnnCi95Lb()));
		}else{
			signalStatistics.setLb(0.0);
		}
		if(dto.getBcpnnCi95Ub() != null){
			signalStatistics.setUb(Double.parseDouble(dto.getBcpnnCi95Ub()));
		}else{
			signalStatistics.setUb(0.0);
		}
		statistics.add(signalStatistics);
	}
	
}
