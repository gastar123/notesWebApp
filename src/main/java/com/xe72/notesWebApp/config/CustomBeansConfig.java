package com.xe72.notesWebApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.PostgresSequenceMaxValueIncrementer;

import javax.sql.DataSource;

@Configuration
public class CustomBeansConfig {

    @Bean
    public AbstractSequenceMaxValueIncrementer getNoteVersionSequence(DataSource dataSource) {
        return new PostgresSequenceMaxValueIncrementer(dataSource, "note_version_seq");
    }
}
