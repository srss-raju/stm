package com.deloitte.smt.dto;

import java.io.Serializable;

public class SignalAlgorithmDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6678141117765723101L;
	
	private String familyDescription;
	
	private String socDescription;
	
	private String ptDescription;
	
	private float cases;
	
	private float outcomeDE;
	
	private float outcomeHO;
	
	private float outcomeLT;
	
	private float outcomeCA;
	
	private float outcomeRI;
	
	private float outcomeOT;
	
	private float algorithmPRRScore;
	
	private float algorithmPRRLB;
	
	private float algorithmPRRUB;
	
	private float algorithmRORScore;
	
	private float algorithmRORLB;
	
	private float algorithmRORUB;
	
	private float algorithmRRRScore;
	
	private float algorithmRRRLB;
	
	private float algorithmRRRUB;
	
	private float algorithmEBScore;
	
	private float algorithmBCPNNScore;
	
	private float pp;
	
	private String signal;
	
	private String algorithmPRRSQ;
	
	private String signalId;	
	
	private String algorithmEBLB;
	
	private String algorithmEBUB;
	
	private String algorithmRRRInfoComp;
	
	private String algorithmBCPNNLB;
	
	private String algorithmBCPNNUB;
	
	private String algorithmRRRSTDev;
	
	private String algorithmRORSTDev;
	
	private String algorithmPRRSTDev;

	public String getFamilyDescription() {
		return familyDescription;
	}

	public void setFamilyDescription(String familyDescription) {
		this.familyDescription = familyDescription;
	}

	public String getSocDescription() {
		return socDescription;
	}

	public void setSocDescription(String socDescription) {
		this.socDescription = socDescription;
	}

	public String getPtDescription() {
		return ptDescription;
	}

	public void setPtDescription(String ptDescription) {
		this.ptDescription = ptDescription;
	}

	public String getAlgorithmPRRSTDev() {
		return algorithmPRRSTDev;
	}

	public void setAlgorithmPRRSTDev(String algorithmPRRSTDev) {
		this.algorithmPRRSTDev = algorithmPRRSTDev;
	}

	public String getAlgorithmRORSTDev() {
		return algorithmRORSTDev;
	}

	public void setAlgorithmRORSTDev(String algorithmRORSTDev) {
		this.algorithmRORSTDev = algorithmRORSTDev;
	}

	public String getAlgorithmRRRSTDev() {
		return algorithmRRRSTDev;
	}

	public void setAlgorithmRRRSTDev(String algorithmRRRSTDev) {
		this.algorithmRRRSTDev = algorithmRRRSTDev;
	}

	public String getAlgorithmRRRInfoComp() {
		return algorithmRRRInfoComp;
	}

	public void setAlgorithmRRRInfoComp(String algorithmRRRInfoComp) {
		this.algorithmRRRInfoComp = algorithmRRRInfoComp;
	}
	public String getAlgorithmEBLB() {
		return algorithmEBLB;
	}

	public void setAlgorithmEBLB(String algorithmEBLB) {
		this.algorithmEBLB = algorithmEBLB;
	}

	public String getAlgorithmEBUB() {
		return algorithmEBUB;
	}

	public void setAlgorithmEBUB(String algorithmEBUB) {
		this.algorithmEBUB = algorithmEBUB;
	}
	public String getAlgorithmBCPNNLB() {
		return algorithmBCPNNLB;
	}

	public void setAlgorithmBCPNNLB(String algorithmBCPNNLB) {
		this.algorithmBCPNNLB = algorithmBCPNNLB;
	}

	public String getAlgorithmBCPNNUB() {
		return algorithmBCPNNUB;
	}

	public void setAlgorithmBCPNNUB(String algorithmBCPNNUB) {
		this.algorithmBCPNNUB = algorithmBCPNNUB;
	}
	public String getSignal() {
		return signal;
	}

	public void setSignal(String signal) {
		this.signal = signal;
	}

	public String getSignalId() {
		return signalId;
	}

	public void setSignalId(String signalId) {
		this.signalId = signalId;
	}

	public float getCases() {
		return cases;
	}

	public void setCases(float cases) {
		this.cases = cases;
	}

	public float getOutcomeDE() {
		return outcomeDE;
	}

	public void setOutcomeDE(float outcomeDE) {
		this.outcomeDE = outcomeDE;
	}

	public float getOutcomeHO() {
		return outcomeHO;
	}

	public void setOutcomeHO(float outcomeHO) {
		this.outcomeHO = outcomeHO;
	}

	public float getOutcomeLT() {
		return outcomeLT;
	}

	public void setOutcomeLT(float outcomeLT) {
		this.outcomeLT = outcomeLT;
	}

	public float getOutcomeCA() {
		return outcomeCA;
	}

	public void setOutcomeCA(float outcomeCA) {
		this.outcomeCA = outcomeCA;
	}

	public float getOutcomeRI() {
		return outcomeRI;
	}

	public void setOutcomeRI(float outcomeRI) {
		this.outcomeRI = outcomeRI;
	}

	public float getOutcomeOT() {
		return outcomeOT;
	}

	public void setOutcomeOT(float outcomeOT) {
		this.outcomeOT = outcomeOT;
	}

	public float getAlgorithmPRRScore() {
		return algorithmPRRScore;
	}

	public void setAlgorithmPRRScore(float algorithmPRRScore) {
		this.algorithmPRRScore = algorithmPRRScore;
	}

	public float getAlgorithmPRRLB() {
		return algorithmPRRLB;
	}

	public void setAlgorithmPRRLB(float algorithmPRRLB) {
		this.algorithmPRRLB = algorithmPRRLB;
	}

	public float getAlgorithmPRRUB() {
		return algorithmPRRUB;
	}

	public void setAlgorithmPRRUB(float algorithmPRRUB) {
		this.algorithmPRRUB = algorithmPRRUB;
	}
	public float getAlgorithmRORScore() {
		return algorithmRORScore;
	}

	public void setAlgorithmRORScore(float algorithmRORScore) {
		this.algorithmRORScore = algorithmRORScore;
	}

	public float getAlgorithmRORLB() {
		return algorithmRORLB;
	}

	public void setAlgorithmRORLB(float algorithmRORLB) {
		this.algorithmRORLB = algorithmRORLB;
	}

	public float getAlgorithmRORUB() {
		return algorithmRORUB;
	}

	public void setAlgorithmRORUB(float algorithmRORUB) {
		this.algorithmRORUB = algorithmRORUB;
	}

	public float getAlgorithmRRRScore() {
		return algorithmRRRScore;
	}

	public void setAlgorithmRRRScore(float algorithmRRRScore) {
		this.algorithmRRRScore = algorithmRRRScore;
	}

	public float getAlgorithmRRRLB() {
		return algorithmRRRLB;
	}

	public void setAlgorithmRRRLB(float algorithmRRRLB) {
		this.algorithmRRRLB = algorithmRRRLB;
	}

	public float getAlgorithmRRRUB() {
		return algorithmRRRUB;
	}

	public void setAlgorithmRRRUB(float algorithmRRRUB) {
		this.algorithmRRRUB = algorithmRRRUB;
	}

	public float getAlgorithmEBScore() {
		return algorithmEBScore;
	}

	public void setAlgorithmEBScore(float algorithmEBScore) {
		this.algorithmEBScore = algorithmEBScore;
	}

	public float getAlgorithmBCPNNScore() {
		return algorithmBCPNNScore;
	}

	public void setAlgorithmBCPNNScore(float algorithmBCPNNScore) {
		this.algorithmBCPNNScore = algorithmBCPNNScore;
	}

	public float getPp() {
		return pp;
	}

	public void setPp(float pp) {
		this.pp = pp;
	}

	public String getAlgorithmPRRSQ() {
		return algorithmPRRSQ;
	}

	public void setAlgorithmPRRSQ(String algorithmPRRSQ) {
		this.algorithmPRRSQ = algorithmPRRSQ;
	}
}
