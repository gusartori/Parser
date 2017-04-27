package com.parser.config;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

@Configuration
public class WebhooksByStatusConfig {
    @Bean
    public ItemReader<Map<Integer,Integer>> jdbcReaderWebhook(@Qualifier("dataSource") DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<Map<Integer,Integer>>()
                .dataSource(dataSource)
                .name("jdbc-reader-webhook")
                .sql("SELECT COUNT(code) c, code cd FROM WEBHOOK GROUP BY cd ORDER BY c DESC")
                .rowMapper((resultSet, i) -> Collections.singletonMap(resultSet.getInt("cd"), resultSet.getInt("c")))
                .build();
    }

    @Bean
    public ItemWriter<Map<Integer,Integer>> fileWriterWebhook(){
        return new FlatFileItemWriterBuilder<Map<Integer,Integer>>()
                .name("file-writer-webhook")
                .resource(new FileSystemResource("./output/webhooksByStatus.txt"))
                .lineAggregator(new DelimitedLineAggregator<Map<Integer,Integer>>(){
                    {
                        setDelimiter(" - ");
                        setFieldExtractor(integerIntegerMap -> {
                            Map.Entry<Integer, Integer> next = integerIntegerMap.entrySet().iterator().next();
                            return new Object[]{next.getKey(), next.getValue()};
                        });
                    }
                })
                .build();

    }
}
