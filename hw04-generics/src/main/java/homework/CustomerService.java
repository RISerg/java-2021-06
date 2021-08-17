package homework;


import java.util.*;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private NavigableMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> firstEntry = map.firstEntry();

        return firstEntry != null ? new AbstractMap.SimpleEntry<>( new Customer(firstEntry.getKey()), firstEntry.getValue()) : null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextEntry = map.higherEntry(customer);

        return nextEntry != null ? new AbstractMap.SimpleEntry<>( new Customer(nextEntry.getKey()), nextEntry.getValue()) : null;
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
