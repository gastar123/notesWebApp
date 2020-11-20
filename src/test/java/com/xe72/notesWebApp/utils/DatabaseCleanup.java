package com.xe72.notesWebApp.utils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseCleanup {

    // TODO: Что делает @PersistenceContext ???
    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Transactional
    public void execute() {
        entityManager.flush();

        // Disable foreign keys
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (final String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @PostConstruct
    private void afterPropertiesSet() throws Exception {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Table.class) != null)
                .map(e -> e.getJavaType().getAnnotation(Table.class).name())
                .collect(Collectors.toList());
    }
}
