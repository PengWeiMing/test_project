package com.pwm.dev.po;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name="test_table")
public class TestTablePo {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "user_age")
  private int userAge;

  @Column(name = "remark")
  private String remark;

}
