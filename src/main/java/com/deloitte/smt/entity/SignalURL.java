package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "sm_signal_url")
public class SignalURL  implements Serializable {

	private static final long serialVersionUID = -3652414302035277522L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String description;
	private String url;
	private Long topicId;
	private String createdBy;
	private Date createdDate;
	private Date modifiedDate;
	private String modifiedBy;
}
