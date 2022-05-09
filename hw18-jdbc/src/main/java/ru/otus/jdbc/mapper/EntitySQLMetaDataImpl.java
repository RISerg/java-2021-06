package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> classMetaData;
    private final String selectAllSQL;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> classMetaData) {
        this.classMetaData = classMetaData;
        this.selectAllSQL = "select %s from %s".formatted(getAllFieldsString(), classMetaData.getName());
        this.selectByIdSql = selectAllSQL + "\nwhere %s = ?".formatted(classMetaData.getIdField().getName());
        this.insertSql = "insert into %s (%s) \nvalues (%s)".formatted(
                classMetaData.getName(),
                getFieldsWithoutIdString(),
                getFieldsWithoutIdParamString());
        this.updateSql = "update %s \nset %s \nwhere %s = ?".formatted(
                classMetaData.getName(),
                getAllFieldsUpdateString(),
                classMetaData.getIdField().getName());
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSQL;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

    private String getAllFieldsString() {
        return classMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(", "));
    }

    private String getFieldsWithoutIdString() {
        return classMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(", "));
    }

    private String getFieldsWithoutIdParamString() {
        var count = classMetaData.getFieldsWithoutId().size();
        return Stream.generate(() -> "?").limit(count).collect(Collectors.joining(", "));
    }

    private String getAllFieldsUpdateString() {
        var fields = classMetaData.getFieldsWithoutId();
        return fields.stream().map(field -> "%s = ?".formatted(field.getName())).collect(Collectors.joining(",\n"));
    }
}
