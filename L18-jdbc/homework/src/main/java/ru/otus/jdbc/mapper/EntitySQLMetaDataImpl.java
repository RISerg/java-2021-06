package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> classMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> classMetaData) {
        this.classMetaData = classMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select %s from %s".formatted(getAllFieldsString(), classMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return "%s \nwhere %s = ?".formatted(
                getSelectAllSql(),
                classMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        return "insert into %s (%s) \nvalues (%s)".formatted(
                classMetaData.getName(),
                getFieldsWithoutIdString(),
                getFieldsWithoutIdParamString());
    }

    @Override
    public String getUpdateSql() {
        return "update %s \nset %s \nwhere %s = ?".formatted(
                classMetaData.getName(),
                getAllFieldsUpdateString(),
                classMetaData.getIdField().getName());
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
