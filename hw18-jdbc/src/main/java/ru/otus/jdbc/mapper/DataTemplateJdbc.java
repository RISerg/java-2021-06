package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    private static final Logger log = LoggerFactory.getLogger(DataTemplateJdbc.class);

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                resultSet -> entityClassMetaData.makeObjectsFromRS(resultSet).stream().findFirst().orElse(null));
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                        connection,
                        entitySQLMetaData.getSelectAllSql(),
                        Collections.emptyList(),
                        entityClassMetaData::makeObjectsFromRS)
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getInsertSql(),
                entityClassMetaData.getFieldValues(client, entityClassMetaData.getFieldsWithoutId()));
    }

    @Override
    public void update(Connection connection, T client) {
        var fields = entityClassMetaData.getFieldsWithoutId();
        fields.add(entityClassMetaData.getIdField());

        dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getUpdateSql(),
                entityClassMetaData.getFieldValues(client, fields));
    }
}
