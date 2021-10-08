package ru.otus.aop.proxy;

import ru.otus.aop.proxy.annotations.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

class Ioc {

    private Ioc() {
    }

    static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        private final Map<String, Set<Class>> implMethods;

        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;

            var methodsArray = myClass.getClass().getDeclaredMethods();
            this.implMethods = Arrays.stream(methodsArray).collect(Collectors.toMap(
                    this::getMethodSignature,
                    method -> Arrays.stream(method.getDeclaredAnnotations()).map(Annotation::annotationType).collect(Collectors.toSet())));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            var methodSignature = this.getMethodSignature(method);
            var implMethodAnnotations = this.implMethods.get(methodSignature);

            if (implMethodAnnotations.contains(Log.class))
                System.out.println("executed method: " + method.getName() + ", params: " + Arrays.toString(args));

            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }

        String getMethodSignature(Method method) {
            return method.getName() + Arrays.toString(method.getParameterTypes());
        }
    }
}
