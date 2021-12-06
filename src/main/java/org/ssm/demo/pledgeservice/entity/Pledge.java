package org.ssm.demo.pledgeservice.entity;


import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.ssm.demo.pledgeservice.shared.BaseEntity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Entity
public class Pledge implements BaseEntity{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) UUID id;
	String status;
	Integer actual_pledged_amount = 0;
	Integer requested_pledged_amount = 0;
	@CreatedDate @Temporal(TemporalType.TIMESTAMP) Date created_at;
	@LastModifiedDate @Temporal(TemporalType.TIMESTAMP) Date updated_at;
	
	public static Pledge of(Map<?,?> data) {
		return new Pledge().buildFrom(data, Pledge.class);
	}
}
