package ru.otus.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();
        var addressList = new ArrayList<Address>();
        Long prevClientId = null;
        Long prevAddressId = null;
        while (rs.next()) {
            var clientId = (Long) rs.getObject("client_id");
            var addressId = (Long) rs.getObject("address_id");
            if (prevClientId == null || !prevClientId.equals(clientId)) {
                addressList = new ArrayList<>();
                var phone = new Phone(rs.getLong("phone_id"), rs.getString("phone_number"));

                if (addressId != null) {
                    addressList.add(new Address(addressId, rs.getString("address_street")));
                    prevAddressId = addressId;
                }

                var client = new Client(clientId, rs.getString("client_name"), phone, addressList);

                clientList.add(client);
                prevClientId = clientId;
            } else if (!addressId.equals(prevAddressId)) {
                addressList.add(new Address(addressId, rs.getString("address_street")));
            }
        }
        return clientList;
    }
}
