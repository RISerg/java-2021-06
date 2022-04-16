package ru.otus.cachehw.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.cache.HwListener;
import ru.otus.cachehw.cache.MyCache;
import ru.otus.cachehw.core.repository.DataTemplateHibernate;
import ru.otus.cachehw.core.repository.HibernateUtils;
import ru.otus.cachehw.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.cachehw.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.cachehw.crm.model.Address;
import ru.otus.cachehw.crm.model.Client;
import ru.otus.cachehw.crm.model.Phone;
import ru.otus.cachehw.crm.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var cache = new MyCache<Long, Client>();
        HwListener<Long, Client> listener = new HwListener<>() {
            @Override
            public void notify(Long key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, cache);
        var phone = new Phone("123");
        var address = new Address("abc");
        dbServiceClient.saveClient(new Client("dbServiceFirst", phone, List.of(address)));

        phone = new Phone("456");
        address = new Address("def");
        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond", phone, List.of(address)));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
///

        phone = new Phone("789");
        address = new Address("ghi");
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "dbServiceSecondUpdated", phone, List.of(address)));
        var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));

        cache.removeListener(listener);
    }
}
