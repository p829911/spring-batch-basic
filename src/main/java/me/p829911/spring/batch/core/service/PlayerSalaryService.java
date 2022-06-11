package me.p829911.spring.batch.core.service;

import java.time.Year;
import me.p829911.spring.batch.dto.PlayerDto;
import me.p829911.spring.batch.dto.PlayerSalaryDto;
import org.springframework.stereotype.Service;

@Service
public class PlayerSalaryService {

  public PlayerSalaryDto calcSalary(PlayerDto player) {
    Integer salary = (Year.now().getValue() - player.getBirthYear()) * 1000000;
    return PlayerSalaryDto.of(player, salary);
  }
}
