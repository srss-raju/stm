package com.deloitte.smt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DetectionTopicDTO {
	
	@JsonProperty("ID")
	private String id;
	@JsonProperty("RUN_INSTANCE_ID")
	private String runInstanceId;
	@JsonProperty("PRODUCT_KEY")
	private Long productKey;
	@JsonProperty("PT_DESC")
	private String ptDesc;
	@JsonProperty("NUM_DRUG_PT")
	private String numDrugPt;
	@JsonProperty("NUM_NDRUG_PT")
	private String numNDrugPt;
	@JsonProperty("NUM_DRUG_NPT")
	private String numDrugNpt;
	@JsonProperty("NUM_NDRUG_NPT")
	private String numNdrugNpt;
	
	@JsonProperty("PRR_SCORE")
	private String prrScore;
	@JsonProperty("PRR_STDEV")
	private String prrStDev;
	@JsonProperty("PRR_CI_95_LB")
	private String prrCi95Lb;
	@JsonProperty("PRR_CI_95_UB")
	private String prrCi95Ub;
	@JsonProperty("PRR_CHI_SQ")
	private String prrChiSq;
	@JsonProperty("PRR_IS_SIGNAL")
	private int prrIsSignal;
	
	@JsonProperty("ROR_SCORE")
	private String rorScore;
	@JsonProperty("ROR_STDEV")
	private String rorStDev;
	@JsonProperty("ROR_CI_95_LB")
	private String rorCi95Lb;
	@JsonProperty("ROR_CI_95_UB")
	private String rorCi95Ub;
	@JsonProperty("ROR_IS_SIGNAL")
	private int rorIsSignal;
	
	@JsonProperty("RRR_SCORE")
	private String rrrScore;
	@JsonProperty("RRR_STDEV")
	private String rrrStDev;
	@JsonProperty("RRR_CI_95_LB")
	private String rrrCi95Lb;
	@JsonProperty("RRR_CI_95_UB")
	private String rrrCi95Ub;
	@JsonProperty("RRR_IS_SIGNAL")
	private int rrrIsSignal;
	@JsonProperty("RRR_INFO_COMP")
	private String rrrInfoComp;
	
	@JsonProperty("NUM_DRUG_PT_EXTERNAL")
	private String numDrugPtExternal;
	@JsonProperty("NUM_NDRUG_PT_EXTERNAL")
	private String numNdrugPtExternal;
	@JsonProperty("NUM_DRUG_NPT_EXTERNAL")
	private String numDrugNptExternal;
	@JsonProperty("NUM_NDRUG_NPT_EXTERNAL")
	private String numNdrugNptExternal;
	
	@JsonProperty("PRR_SCORE_EXTERNAL")
	private String prrScoreExternal;
	@JsonProperty("PRR_STDEV_EXTERNAL")
	private String prrStDevExternal;
	@JsonProperty("PRR_CI_95_LB_EXTERNAL")
	private String prrCi95LbExternal;
	@JsonProperty("PRR_CI_95_UB_EXTERNAL")
	private String prrCi95UbExternal;
	@JsonProperty("PRR_CHI_SQ_EXTERNAL")
	private int prrChiSqExternal;
	@JsonProperty("PRR_IS_SIGNAL_EXTERNAL")
	private String prrIsSignalExternal;
	
	@JsonProperty("ROR_SCORE_EXTERNAL")
	private String rorScoreExternal;
	@JsonProperty("ROR_STDEV_EXTERNAL")
	private String rorStDevExternal;
	@JsonProperty("ROR_CI_95_LB_EXTERNAL")
	private String rorCi95LbExternal;
	@JsonProperty("ROR_CI_95_UB_EXTERNAL")
	private String rorCi95UbExternal;
	@JsonProperty("ROR_IS_SIGNAL_EXTERNAL")
	private int rorIsSignalExternal;
	
	@JsonProperty("RRR_SCORE_EXTERNAL")
	private String rrrScoreExternal;
	@JsonProperty("RRR_STDEV_EXTERNAL")
	private String rrrStDevExternal;
	@JsonProperty("RRR_CI_95_LB_EXTERNAL")
	private String rrrCi95LbExternal;
	@JsonProperty("RRR_CI_95_UB_EXTERNAL")
	private String rrrCi95UbExternal;
	@JsonProperty("RRR_IS_SIGNAL_EXTERNAL")
	private int rrrIsSignalExternal;
	@JsonProperty("RRR_INFO_COMP_EXTERNAL")
	private int rrrInfoCompExternal;
	
	@JsonProperty("RXNORM_CUI_KEY")
	private String rxNormCuiKey;
	@JsonProperty("DENOMINATOR_STRING")
	private String denominatorString;
	@JsonProperty("NUMERATOR_STRING")
	private String numeratorString;
	@JsonProperty("MIN_CASES")
	private String minCases;
	
	@JsonProperty("MGPS_CI_95_LB")
	private String mgpsCi95Lb;
	@JsonProperty("MGPS_CI_95_LB_EXTERNAL")
	private String mgpsCi95LbExternal;
	
	@JsonProperty("BCPNN_SCORE")
	private String bcpnnScore;
	@JsonProperty("BCPNN_CI_95_LB")
	private String bcpnnCi95Lb;
	@JsonProperty("BCPNN_CI_95_UB")
	private String bcpnnCi95Ub;
	@JsonProperty("BCPNN_CI_95_LB_EXTERNAL")
	private String bcpnnCi95LbExternal;
	
	@JsonProperty("MGPS_AE_COUNT")
	private String mgpsAeCount;
	@JsonProperty("MGPS_EXP_AE_COUNT")
	private String mgpsExpAeCount;
	@JsonProperty("MGPS_AE_COUNT_EXTERNAL")
	private String mgpsAeCountExternal;
	@JsonProperty("MGPS_EXP_AE_COUNT_EXTERNAL")
	private String mgpsExpAeCountExternal;
	
	@JsonProperty("BCPNN_AE_COUNT")
	private String bcpnnAeCount;
	@JsonProperty("BCPNN_AE_COUNT_EXTERNAL")
	private String bcpnnAeCountExternal;
	@JsonProperty("BCPNN_EXP_AE_COUNT")
	private String bcpnnExpAeCount;
	@JsonProperty("BCPNN_EXP_AE_COUNT_EXTERNAL")
	private String bcpnnExpAeCountExternal;
	
	@JsonProperty("NUM_DE")
	private String numDe;
	@JsonProperty("NUM_LT")
	private String numLt;
	@JsonProperty("NUM_HO")
	private String numHo;
	@JsonProperty("NUM_DS")
	private String numDs;
	@JsonProperty("NUM_CA")
	private String numCa;
	@JsonProperty("NUM_RI")
	private String numRi;
	@JsonProperty("NUM_OT")
	private String numOt;
	
	@JsonProperty("MGPS_CI_95_UB")
	private String mgpsCi95Ub;
	@JsonProperty("MGPS_SCORE")
	private String mgpsScore;
	@JsonProperty("MGPS_IS_SIGNAL")
	private String mgpsIsSignal;
	
	@JsonProperty("BCPNN_IS_SIGNAL")
	private String bcpnnIsSignal;
	@JsonProperty("PRODUCT_NAME")
	private String productName;
	@JsonProperty("RXCUI_KEY")
	private String rxCuiKey;
	@JsonProperty("SOC_ID")
	private String socId;
	@JsonProperty("HLGT_ID")
	private String hlgtId;
	@JsonProperty("HLT_ID")
	private String hltId;
	@JsonProperty("PT_ID")
	private String ptId;
	@JsonProperty("SOC_DESC")
	private String socDesc;
	@JsonProperty("HLGT_DESC")
	private String hlgtDesc;
	@JsonProperty("HLT_DESC")
	private String hltDesc;
	@JsonProperty("IS_SIGNAL")
	private Long iSsignal;
	@JsonProperty("IS_SIGNAL_EXTERNAL")
	private String iSsignalExternal;

}
