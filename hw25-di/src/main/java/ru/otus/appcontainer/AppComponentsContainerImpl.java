package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        try {
            Object configObj = configClass.getDeclaredConstructor().newInstance();
            var methods = Arrays.stream(configClass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(AppComponent.class))
                    .sorted(Comparator.comparingInt(m -> m.getDeclaredAnnotation(AppComponent.class).order())).toList();
            for (var method : methods) {
                var params = Arrays.stream(method.getParameterTypes()).map(this::getAppComponent).toArray();
                var obj = method.invoke(configObj, params);
                var objName = method.getDeclaredAnnotation(AppComponent.class).name();

                if (appComponents.contains(obj) || appComponentsByName.containsKey(objName)) {
                    throw new RuntimeException("Объект с таки именем уже инициализирован: " + objName);
                }

                appComponents.add(obj);
                appComponentsByName.put(objName, obj);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Ошибка обработки конфига", e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(componentClass::isInstance)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var component = Optional.of((C) appComponentsByName.get(componentName));
        return component.orElseThrow();
    }
}
