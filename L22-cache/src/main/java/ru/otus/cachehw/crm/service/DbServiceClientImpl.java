package ru.otus.cachehw.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.cache.HwCache;
import ru.otus.cachehw.core.repository.DataTemplate;
import ru.otus.cachehw.crm.model.Client;
import ru.otus.cachehw.core.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<Long, Client> cache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate, HwCache<Long, Client> cache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();

            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
            } else {
                clientDataTemplate.update(session, clientCloned);
                log.info("updated client: {}", clientCloned);
            }

            // кэшируем клиента
            cache.put(clientCloned.getId(), clientCloned);

            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Optional<Client> client = Optional.ofNullable(cache.get(id));
        // если нет в кэше - идем в базу
        if (client.isEmpty()) {
            client = transactionManager.doInTransaction(session -> {
                var clientOptional = clientDataTemplate.findById(session, id);
                log.info("client: {}", clientOptional);
                return clientOptional;
            });
        }
        return client;
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);

            // кэшируем
            cache.removeAll();
            clientList.forEach(client -> cache.put(client.getId(), client));

            return clientList;
        });
    }
}
