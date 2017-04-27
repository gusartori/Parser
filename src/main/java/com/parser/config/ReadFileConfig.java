package com.parser.config;

import com.parser.pojo.Webhook;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gustavo on 26/04/17.
 */
@Configuration
public class ReadFileConfig {

    @Bean
    public ItemReader fileReader(LineMapper<Webhook> patternMapper) {
        FlatFileItemReader<Webhook> infoFlatFileItemReader = new FlatFileItemReader<>();
        infoFlatFileItemReader.setEncoding("UTF-8");
        infoFlatFileItemReader.setLinesToSkip(2);
        infoFlatFileItemReader.setLineMapper(patternMapper);
        infoFlatFileItemReader.setResource(new FileSystemResource("./input/log.txt"));
        return infoFlatFileItemReader;
    }

    @Bean
    public LineMapper<Webhook> patternMapper() {
        return new LineMapper<Webhook>() {
            Pattern p = Pattern.compile(".*request_to=\"(.*)\".*response_status=\"(.*)\"");

            @Override
            public Webhook mapLine(String line, int i) throws Exception {
                Matcher matcher = p.matcher(line);
                if (matcher.matches()) {
                    return new Webhook(matcher.group(1), new Integer(matcher.group(2)));
                } else return null;
            }
        };
    }

    @Bean
    public JdbcBatchItemWriter<Webhook> jdbcWriter(@Qualifier("dataSource") DataSource ds) {
        return new JdbcBatchItemWriterBuilder<Webhook>()
                .dataSource(ds)
                .sql("INSERT into WEBHOOK (url, code) values (:url, :code)")
                .beanMapped()
                .build();
    }
}
