package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;

@Controller
public class AppController {

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/")
    public String viewHomePage(Model model) {
        var listClient = clientRepository.findAll();
        model.addAttribute("listClient", listClient);
        return "index";
    }

    @RequestMapping("/new")
    public String showNewForm(Model model) {
        var newClient = new Client();
        model.addAttribute("client", newClient);

        return "new_form";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("client") Client client) {
        clientRepository.save(client);

        return "redirect:/";
    }

}
