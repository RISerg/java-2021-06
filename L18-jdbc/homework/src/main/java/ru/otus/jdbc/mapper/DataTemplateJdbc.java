package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
        return (Optional<T>) dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            var obj = entityClassMetaData.getConstructor().newInstance();
                            for (var field : entityClassMetaData.getAllFields()) {
                                var methods = obj.getClass().getDeclaredMethods();
                                for (var method : methods) {
                                    if (method.getName().equalsIgnoreCase("set" + field.getName()) &&
                                            method.getParameterTypes()[0].equals((field.getType()))) {
                                        method.invoke(obj, resultSet.getObject(field.getName()));
                                    }
                                }
                            }
                            return obj;
                        }
                    } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        log.error(e.getMessage(), e);
                    }
                    return Optional.empty();
                });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return (List<T>) dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                Collections.emptyList(),
                resultSet -> {
                    try {
                        var listObj = new ArrayList<T>();
                        while (resultSet.next()) {
                            var obj = entityClassMetaData.getConstructor().newInstance();
                            for (var field : entityClassMetaData.getAllFields()) {
                                obj.getClass()
                                        .getDeclaredMethod("set" + field.getName(), field.getType())
                                        .invoke(obj, resultSet.getObject(field.getName()));
                            }
                            listObj.add(obj);
                        }
                        return listObj;
                    } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                        log.error(e.getMessage(), e);
                    }
                    return Collections.emptyList();
                }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        var params = entityClassMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> {
                    try {
                        var methods = client.getClass().getDeclaredMethods();
                        for (var method : methods) {
                            if (method.getName().equalsIgnoreCase("get" + field.getName()))
                                return method.invoke(client);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .toList();
        //
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
    }

    @Override
    public void update(Connection connection, T client) {
        var fields = entityClassMetaData.getFieldsWithoutId();
        fields.add(entityClassMetaData.getIdField());
        var params = fields.stream().map(field -> {
            try {
                return client.getClass().getDeclaredField(field.getName()).get(client);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return null;
        }).toList();
        //
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
    }
}
