package me.p829911.spring.batch.dto;

import lombok.Data;

@Data
public class PlayerSalaryDto {
  private String ID;
  private String lastName;
  private String firstName;
  private String position;
  private Integer birthYear;
  private Integer debutYear;
  private Integer salary;

  public static PlayerSalaryDto of(PlayerDto player, Integer salary) {
    PlayerSalaryDto playerSalary = new PlayerSalaryDto();
    playerSalary.setID(player.getID());
    playerSalary.setLastName(player.getLastName());
    playerSalary.setFirstName(player.getFirstName());
    playerSalary.setPosition(player.getPosition());
    playerSalary.setBirthYear(player.getBirthYear());
    playerSalary.setDebutYear(player.getDebutYear());
    playerSalary.setSalary(salary);
    return playerSalary;
  }
}
