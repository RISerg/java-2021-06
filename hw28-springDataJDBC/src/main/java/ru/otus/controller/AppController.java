package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
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

        return "client_form";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable(name = "id") String id) {
        var client = clientRepository.findById(id).orElseThrow();
        var model = new ModelAndView("client_form");
        model.addObject("client", client);

        return model;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("client") Client client) {
        clientRepository.save(client);

        return "redirect:/";
    }

    @RequestMapping("/delete/{id}")
    public String remove(@PathVariable(name = "id") String id) {
        var client = clientRepository.findById(id).orElseThrow();
        clientRepository.delete(client);

        return "redirect:/";
    }

}
