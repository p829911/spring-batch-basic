package me.p829911.spring.batch.job;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import me.p829911.spring.batch.BatchTestConfig;
import me.p829911.spring.batch.core.domain.PlainText;
import me.p829911.spring.batch.core.repository.PlainTextRepository;
import me.p829911.spring.batch.core.repository.ResultTextRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {PlainTextJobConfig.class, BatchTestConfig.class})
class PlainTextJobConfigTest {
  @Autowired private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired private PlainTextRepository plainTextRepository;
  @Autowired private ResultTextRepository resultTextRepository;

  @AfterEach
  public void tearDown() {
    plainTextRepository.deleteAll();
    resultTextRepository.deleteAll();
  }

  @Test
  public void success_givenNoPlainText() throws Exception {
    // given
    // no plainText

    // when
    JobExecution execution = jobLauncherTestUtils.launchJob();

    // then
    assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    assertThat(resultTextRepository.count()).isEqualTo(0);
  }

  @Test
  public void success_givenPlainText() throws Exception {
    // given
    givenPlainTexts(12);

    // when
    JobExecution execution = jobLauncherTestUtils.launchJob();

    // then
    assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    assertThat(resultTextRepository.count()).isEqualTo(12);
  }

  private void givenPlainTexts(Integer count) {
    IntStream.range(0, count)
        .forEach(num -> plainTextRepository.save(new PlainText(null, "text" + num)));
  }
}
