package ru.otus;

import testfw.annotations.*;

public class TestClass {

    @Before
    public void before1() {
        System.out.println("before1 invoked");
    }
    @Before
    public void before2() {
        System.out.println("before2 invoked");
    }

    @Test
    public void test1() {
        System.out.println("test1 invoked");
    }
    @Test
    public void test2() throws Exception {
        System.out.println("test2 invoked");
        throw new Exception("Exception in test2");
    }

    @After
    public void after() {
        System.out.println("after invoked");
    }
}
