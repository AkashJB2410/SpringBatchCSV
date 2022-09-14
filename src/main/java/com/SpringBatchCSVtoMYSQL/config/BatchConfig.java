package com.SpringBatchCSVtoMYSQL.config;

import com.SpringBatchCSVtoMYSQL.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<User> reader()  {
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("studentsheet1.csv"));
        reader.setLineMapper(getLineMapper());
        return reader;
    }

    private LineMapper<User> getLineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String []{"id", "prename", "firstname", "lastname"});
        lineTokenizer.setIncludedFields(new int[]{0, 1, 2, 3});
        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<User> writer() {
        JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("insert into (id,prename,firstname,lastname,) value (:id,:prename,:firstname,:lastname,)");
        writer.setDataSource(this.dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob() throws ParseException {
        return this.jobBuilderFactory.get("IMPOPRT-USER-JOB")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() throws ParseException {
        return this.stepBuilderFactory.get("step")
                .<User, User>chunk(1000)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}
