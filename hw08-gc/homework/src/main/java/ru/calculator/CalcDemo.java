package ru.calculator;


/*
-Xms512m
-Xmx512m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./hw08-gc/homework/logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./hw08-gc/homework/logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/


import java.time.LocalDateTime;

public class CalcDemo {
    public static void main(String[] args) {
        long counter = 100_000_000;
        var summator = new Summator();
        long startTime = System.currentTimeMillis();

        var data = new Data(0);
        for (var idx = 0; idx < counter; idx++) {
            data.setValue(idx);
            summator.calc(data);

            if (idx % 10_000_000 == 0) {
                System.out.println(LocalDateTime.now() + " current idx:" + idx);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        System.out.println(summator.getPrevValue());
        System.out.println(summator.getPrevPrevValue());
        System.out.println(summator.getSumLastThreeValues());
        System.out.println(summator.getSomeValue());
        System.out.println(summator.getSum());
        System.out.println("spend msec:" + delta + ", sec:" + (delta / 1000));
    }
}
