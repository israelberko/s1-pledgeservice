package org.ssm.demo.pledgeservice.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.ssm.demo.pledgeservice.shared.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@ToString
@Entity
public class Pledge implements BaseEntity{
	@Id @GeneratedValue(strategy = GenerationType.AUTO) UUID id;
	String state;
	Integer actual_pledged_amount = 0;
	Integer requested_pledged_amount = 0;
	@CreatedDate @Temporal(TemporalType.TIMESTAMP) Date created_at;
	@LastModifiedDate @Temporal(TemporalType.TIMESTAMP) Date updated_at;
	
	public static Pledge of(Map<?,?> data) {
		return new Pledge().buildFrom(data, Pledge.class);
	}
}
