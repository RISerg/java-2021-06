package ru.otus.aop.proxy;

public class ProxyDemo {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.createMyClass();
        myClass.calculation(1);
        myClass.calculation(1, 2);
        myClass.calculation(1, 2, "3");
    }
}



