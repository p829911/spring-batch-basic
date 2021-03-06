package me.p829911.spring.batch.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.p829911.spring.batch.job.validator.LocalDateParameterValidator;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
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
    return jobBuilderFactory
        .get("advancedJob")
        .incrementer(new RunIdIncrementer())
        .validator(new LocalDateParameterValidator("targetDate"))
        .listener(jobExecutionListener())
        .start(advancedStep)
        .build();
  }

  @JobScope
  @Bean
  public JobExecutionListener jobExecutionListener() {
    return new JobExecutionListener() {
      @Override
      public void beforeJob(JobExecution jobExecution) {
        log.info("[JobExecutionListener#beforeJob] jobExecution is " + jobExecution.getStatus());
      }

      @Override
      public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
          log.error("[JobExecutionListener#beforeJob] jobExecution is FAILED!!! RECOVER ASAP");
        }
      }
    };
  }

  @JobScope
  @Bean
  public Step advancedStep(StepExecutionListener stepExecutionListener, Tasklet advancedTasklet) {
    return stepBuilderFactory
        .get("advancedStep")
        .listener(stepExecutionListener)
        .tasklet(advancedTasklet)
        .build();
  }

  @StepScope
  @Bean
  public StepExecutionListener stepExecutionListener() {
    return new StepExecutionListener() {
      @Override
      public void beforeStep(StepExecution stepExecution) {
        log.info(
            "[StepExecutionListener#beforeStep] stepExecution is " + stepExecution.getStatus());
      }

      @Override
      public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("[StepExecutionListener#afterStep] stepExecution is " + stepExecution.getStatus());
        return stepExecution.getExitStatus();
      }
    };
  }

  @StepScope
  @Bean
  public Tasklet advancedTasklet(@Value("#{jobParameters['targetDate']}") String targetDate) {
    return ((contribution, chunkContext) -> {
      log.info("[AdvancedJobConfig] JobParameter - targetDate = {}", targetDate);
      log.info("[AdvancedJobConfig] executed advancedTasklet");
//      throw new RuntimeException("ERROR!!!");
            return RepeatStatus.FINISHED;
    });
  }
}
