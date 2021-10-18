package ru.otus.aop.proxy;


import ru.otus.aop.proxy.annotations.Log;

public class TestLoggingImpl implements TestLoggingInterface {

    @Log
    @Override
    public void calculation(int param) {
        System.out.println(String.format("doing calculations(%d)...", param));
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.println(String.format("doing calculations(%d, %d)...", param1, param2));
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println(String.format("doing calculations(%d, %d, %s)...", param1, param2, param3));
    }

    @Override
    public String toString() {
        return "TestLoggingImpl{}";
    }
}
