package com.parser;

import com.parser.config.MostCalledURLConfig;
import com.parser.config.ReadFileConfig;
import com.parser.config.WebhooksByStatusConfig;
import com.parser.pojo.Webhook;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@EnableBatchProcessing
@SpringBootApplication
public class ParserApplication {

    @Autowired
    ReadFileConfig readFileConfig;

    @Autowired
    MostCalledURLConfig mostCalledURLConfig;

    @Autowired
    WebhooksByStatusConfig webhooksByStatusConfig;

    @Bean
    Job job(JobBuilderFactory jbf, StepBuilderFactory sbf) throws Exception {
        Step s1 = sbf.get("file-db")
                .<Webhook,Webhook> chunk(100)
                .reader(readFileConfig.fileReader(null))
                .writer(readFileConfig.jdbcWriter(null))
                .build();

        Step s2 = sbf.get("db-file")
                .<Map<String,Integer>,Map<String,Integer>>chunk(1000)
                .reader(mostCalledURLConfig.jdbcReaderUrl(null))
                .writer(mostCalledURLConfig.fileWriterUrl())
                .build();

        Step s3 = sbf.get("db-file2")
                .<Map<Integer,Integer>,Map<Integer,Integer>>chunk(1000)
                .reader(webhooksByStatusConfig.jdbcReaderWebhook(null))
                .writer(webhooksByStatusConfig.fileWriterWebhook())
                .build();

        return jbf.get("etl")
                .incrementer(new RunIdIncrementer())
                .start(s1)
                .next(s3)
                .next(s2)
                .build();
    }

	public static void main(String[] args) {
        SpringApplication.run(ParserApplication.class, args);
	}
}
