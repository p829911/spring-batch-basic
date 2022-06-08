package me.p829911.spring.batch.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.p829911.spring.batch.job.validator.LocalDateParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AllArgsConstructor
public class AdvancedJobConfig {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job advancedJob(Step advancedStep) {
    return jobBuilderFactory.get("advancedJob")
        .incrementer(new RunIdIncrementer())
        .validator(new LocalDateParameterValidator("targetDate"))
        .start(advancedStep)
        .build();
  }

  @JobScope
  @Bean
  public Step advancedStep(Tasklet advancedTasklet) {
    return stepBuilderFactory.get("advancedStep")
        .tasklet(advancedTasklet)
        .build();
  }

  @StepScope
  @Bean
  public Tasklet advancedTasklet(@Value("#{jobParameters['targetDate']}") String targetDate) {
    return ((contribution, chunkContext) -> {
      log.info("[AdvancedJobConfig] JobParameter - targetDate = {}", targetDate);
      log.info("[AdvancedJobConfig] executed advancedTasklet");
      return RepeatStatus.FINISHED;
    });
  }
}
