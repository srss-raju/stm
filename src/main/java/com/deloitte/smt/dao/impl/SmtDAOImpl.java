package com.deloitte.smt.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.deloitte.smt.dao.SmtDAO;

public class SmtDAOImpl implements SmtDAO{

	@Autowired
	@Qualifier("primaryJdbcTemplate")
	private JdbcTemplate primaryJdbcTemplate;
	
	@Override
	public String deleteData() {
		int[] result = primaryJdbcTemplate.batchUpdate(new String[] 
				{ "delete from sm_signal_statistics",
				"delete from sm_signal_strength",
				"delete from sm_signal_url",
				"delete from sm_topic_soc_assignment_configuration",
				"delete from sm_topic_product_assignment_configuration",
				"delete from sm_topic_assessment_assignment_assignees",
				"delete from sm_topic_riskplan_assignment_assignees",
				"delete from sm_topic_signal_detection_assignees",
				"delete from sm_topic_signal_validation_assignees",
				"delete from sm_topic_soc_assignment_condition",
				"delete from sm_topic_soc_assignment_product",
				"delete from sm_pt",
				"delete from sm_soc",
				"delete from sm_smq",
				"delete from sm_signal_detection_algorithm",
				"delete from sm_signal_detection_statistics",
				"delete from sm_detection_stratification",
				"delete from sm_detection_run",
				"delete from sm_comments",
				"delete from sm_denominator_for_poisson where detection_id>0",
				"delete from sm_detection_geography",
				"delete from sm_detection_query",
				"update sm_assessment_plan set risk_plan_id = null",
				"delete from sm_signal_detection",
				"delete from sm_signal_audit",
				"delete from sm_signal_attachment_audit",
				"delete from sm_risk_task",
				"delete from sm_risk_plan",
				"delete from sm_product",
				"delete from sm_non_signal",
				"delete from sm_meeting",
				"delete from sm_llt",
				"delete from sm_ingredient",
				"delete from sm_attachment",
				"update sm_topic set assessment_plan_id = null",
				"delete from sm_assessment_plan",
				"delete from sm_topic"
				});
		if(result != null && result.length > 0){
			return "Data Deleted Successfully";
		}
		return "Unable to delete the data";
	}
    
   
}
