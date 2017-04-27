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
public class MostCalledURLConfig {

    @Bean
    public ItemReader<Map<String,Integer>> jdbcReaderUrl(@Qualifier("dataSource") DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<Map<String,Integer>>()
                .dataSource(dataSource)
                .name("jdbc-reader-url")
                .sql("SELECT * FROM (SELECT COUNT(url) c, url u FROM WEBHOOK GROUP BY url ORDER BY c DESC) WHERE rownum <= 3")
                .rowMapper((resultSet, i) -> Collections.singletonMap(resultSet.getString("u"), resultSet.getInt("c")))
                .build();
    }

    @Bean
    public ItemWriter<Map<String,Integer>> fileWriterUrl(){
        return new FlatFileItemWriterBuilder<Map<String,Integer>>()
                .name("file-writer-url")
                .resource(new FileSystemResource("./output/mostCalledURL.txt"))
                .lineAggregator(new DelimitedLineAggregator<Map<String,Integer>>(){
                    {
                        setDelimiter(" - ");
                        setFieldExtractor(stringIntegerMap -> {
                            Map.Entry<String, Integer> next = stringIntegerMap.entrySet().iterator().next();
                            return new Object[]{next.getKey(), next.getValue()};
                        });
                    }
                })
                .build();

    }
}
