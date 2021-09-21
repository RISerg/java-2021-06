package testfw;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import ru.otus.TestClass;
import testfw.annotations.*;

public class TestRunner {

    private static class Result {
        int total = 0;
        int success = 0;
        int fail = 0;

        public Result(int total) {
            this.total = total;
        }
    }

    private static Class testClass;

    public static void main(String[] args) {
        try {
            run("ru.otus.TestClass");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void run(String testClassName) throws ClassNotFoundException {
        testClass = Class.forName(testClassName);
        System.out.println("Test class name: " + testClass.getName());
        Result result;

        Method[] methods = testClass.getDeclaredMethods();

        Method[] beforeMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(testfw.annotations.Before.class))
                .toArray(Method[]::new);

        Method[] afterMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(testfw.annotations.After.class))
                .toArray(Method[]::new);

        Method[] testMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(testfw.annotations.Test.class))
                .toArray(Method[]::new);

        result = runTests(testMethods, beforeMethods, afterMethods);
        printResult(result);
    }

    private static Result runTests(Method[] testMethods, Method[] beforeMethods, Method[] afterMethods) {
        Result result = new Result(testMethods.length);

        for (Method method : testMethods) {
            try {
                Object instance = testClass.getDeclaredConstructor().newInstance();
                System.out.println(String.format("%s > %s > instance %s", testClass.getSimpleName(), method.getName(), instance.hashCode()));
                boolean success = true;
                try {
                    runBefore(beforeMethods, instance);
                    runTest(method, instance);
                } catch (Exception e) {
                    System.out.println(e.getCause().getMessage());
                    success = false;
                } finally {
                    try {
                        runAfter(afterMethods, instance);
                    } catch (Exception e) {
                        System.out.println(e.getCause().getMessage());
                        success = false;
                    }
                }
                System.out.println(String.format("%s > %s %s", testClass.getSimpleName(), method.getName(), success ? "PASSED" : "FAILED"));
                if (success) {
                    result.success += 1;
                } else {
                    result.fail += 1;
                }
            } catch (Exception e) {
                System.out.println(e.getCause().getMessage());
            }
        }

        return result;
    }

    private static void runBefore(Method[] beforeMethods, Object instance) throws Exception {
        for (Method method : beforeMethods) {
            method.invoke(instance);
        }
    }

    private static void runTest(Method method, Object instance) throws Exception {
        method.invoke(instance);
    }

    private static void runAfter(Method[] afterMethods, Object instance) throws Exception {
        for (Method method : afterMethods) {
            method.invoke(instance);
        }
    }

    private static void printResult(Result result) {
        System.out.println(String.format("TOTAL: %d; SUCCESS: %d; FAIL: %d", result.total, result.success, result.fail));
    }
}
