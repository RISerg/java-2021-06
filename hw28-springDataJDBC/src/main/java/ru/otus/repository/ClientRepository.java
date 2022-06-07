package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.model.Client;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, String> {

//    @Override
//    @Query(value = """
//            select  c.id as client_id,
//                    c.name as client_name,
//                    p.id as phone_id,
//                    p.number as phone_number,
//                    a.id as address_id,
//                    a.street as address_street
//                        from client c
//                        left join phone p on p.id = c.phone_id
//                        left join address a on a.client_id = c.id
//            order by c.id
//            """,
//            resultSetExtractorClass = ClientResultSetExtractorClass.class)
//    List<Client> findAll();
}