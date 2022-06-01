package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.form.ClientForm;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repository.ClientDAO;
import ru.otus.repository.ClientRepository;
import ru.otus.repository.PhoneRepository;

import java.util.List;

@Controller
public class AppController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PhoneRepository phoneRepository;

    @RequestMapping("/")
    public String viewHomePage(Model model) {
        List<Client> listClient = clientRepository.findAll();
        model.addAttribute("listClient", listClient);
        return "index";
    }

    @RequestMapping("/new")
    public String showNewForm(Model model) {
        var form = new ClientForm();
        model.addAttribute("form", form);

        return "new_form";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("form") ClientForm form) {
        var phone = new Phone(form.getPhone());
        var client = new Client();

        client.setName(form.getName());
        client.setPhone(phone);

        clientRepository.save(client);

        return "redirect:/";
    }

}
