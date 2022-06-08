package me.p829911.spring.batch.job;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import me.p829911.spring.batch.core.domain.PlainText;
import me.p829911.spring.batch.core.domain.ResultText;
import me.p829911.spring.batch.core.repository.PlainTextRepository;
import me.p829911.spring.batch.core.repository.ResultTextRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

@Configuration
@RequiredArgsConstructor
public class PlainTextJobConfig {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final PlainTextRepository plainTextRepository;
  private final ResultTextRepository resultTextRepository;

  @Bean("plainTextJob")
  public Job plainTextJob(Step plainTextStep) {
    return jobBuilderFactory
        .get("plainTextJob")
        .incrementer(new RunIdIncrementer())
        .start(plainTextStep)
        .build();
  }

  @JobScope
  @Bean("plainTextStep")
  public Step plainTextStep(
      ItemReader<PlainText> plainTextReader,
      ItemProcessor<PlainText, String> plainTextProcessor,
      ItemWriter<String> plainTextWriter) {
    return stepBuilderFactory
        .get("plainTextStep")
        .<PlainText, String>chunk(5)
        .reader(plainTextReader)
        .processor(plainTextProcessor)
        .writer(plainTextWriter)
        .build();
  }

  @StepScope
  @Bean
  public RepositoryItemReader<PlainText> plainTextReader() {
    return new RepositoryItemReaderBuilder<PlainText>()
        .name("plainTextReader")
        .repository(plainTextRepository)
        .methodName("findBy")
        .pageSize(5)
        .arguments(Collections.emptyList())
        .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
        .build();
  }

  @StepScope
  @Bean
  public ItemProcessor<PlainText, String> plainTextProcessor() {
    return item -> "processed " + item.getText();
  }

  @StepScope
  @Bean
  public ItemWriter<String> plainTextWriter() {
    return items -> {
      items.forEach(item -> resultTextRepository.save(new ResultText(null, item)));
      System.out.println("==== chunk is finished ====");
    };
  }
}
