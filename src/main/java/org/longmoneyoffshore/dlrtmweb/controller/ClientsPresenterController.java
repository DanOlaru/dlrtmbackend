package org.longmoneyoffshore.dlrtmweb.controller;

import org.longmoneyoffshore.dlrtmweb.entities.models.entity.Client;
import org.longmoneyoffshore.dlrtmweb.service.ClientService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClientsPresenterController {

    private ClientService clientService;

    @GetMapping("/presentClients")
    public String listClients(Model model) {

        model.addAttribute("clients", clientService.getAllClients());

        return "clientsThyme";
    }


    @GetMapping("/presentClientsEx")
    public ModelAndView selectClient() {

        ModelAndView mav = new ModelAndView("clientsThyme");
        mav.addObject("clients", clientService.getAllClients());

        return mav;
    }

   /* @Controller
    public class SomeController {
        @RequestMapping("/")
        public String redirect() {
            return "redirect:/query?q=Thymeleaf+Is+Great!";
        }
    }*/

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }
}