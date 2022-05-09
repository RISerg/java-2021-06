package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.DBService;
import ru.otus.model.client.Address;
import ru.otus.model.client.Client;
import ru.otus.model.client.Phone;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.ftl";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";

    private final DBService<Client> clientDBService;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DBService<Client> clientDBService) {
        this.templateProcessor = templateProcessor;
        this.clientDBService = clientDBService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> clients = clientDBService.findAll();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, clients);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var params = request.getParameterMap();
        var clientName = params.get("name")[0];
        var clientPhone = new Phone(params.get("phone")[0]);
        var clientAddress = List.of(new Address(params.get("address")[0]));

        clientDBService.save(new Client(clientName, clientPhone, clientAddress));

        response.sendRedirect("/clients");
    }
}
