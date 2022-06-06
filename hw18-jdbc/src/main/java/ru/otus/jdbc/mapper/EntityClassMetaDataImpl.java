package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> fields;
    private List<Method> methods;

    private static final Logger log = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.name = clazz.getSimpleName().toLowerCase();
        try {
            this.constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Не найден конструктор класса %s".formatted(clazz.getSimpleName()));
        }
        this.fields = Arrays.asList(clazz.getDeclaredFields());
        this.idField = fields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow();

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> fieldsWithoutId = new ArrayList<>(fields);
        fieldsWithoutId.removeIf(field -> field.getName().equals(idField.getName()));
        return fieldsWithoutId;
    }

    public List<Object> getFieldValues(T obj, List<Field> fields) {
        List<Object> values = new ArrayList<>();
        for (var field : fields) {
            field.setAccessible(true);
            try {
                values.add(field.get(obj));
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
        return values;
    }

    public List<T> makeObjectsFromRS(ResultSet resultSet) {
        List<T> listObj = new ArrayList<>();
        try {
            while (resultSet.next()) {
                try {
                    var obj = getConstructor().newInstance();
                    for (var field : fields) {
                        field.setAccessible(true);
                        field.set(obj, resultSet.getObject(field.getName()));
                    }
                    listObj.add(obj);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    log.error(e.getMessage(), e);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listObj;
    }

    private Method getMethod(String methodName, Class<?>[] paramTypes) {
        return methods.stream()
                .filter(method -> method.getName().equalsIgnoreCase(methodName) &&
                        Arrays.equals(method.getParameterTypes(), paramTypes))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Не найден метод %s с параметрами %s"
                        .formatted(methodName, Arrays.toString(paramTypes))));
    }
}
