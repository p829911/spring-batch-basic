package me.p829911.spring.batch.dto;

import lombok.Data;

@Data
public class PlayerDto {
  private String ID;
  private String lastName;
  private String firstName;
  private String position;
  private Integer birthYear;
  private Integer debutYear;
}
