package com.projects.springboot.crm.controllers;

import com.projects.springboot.crm.models.entity.Customer;
import com.projects.springboot.crm.models.service.ICustomerService;
import com.projects.springboot.crm.models.service.UploadsFilesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@Controller
@SessionAttributes("customer")
public class AppController {

    @Autowired
    public ICustomerService customerService;

    @GetMapping("")
    public String dashboard(Model model) {
        model.addAttribute("title", "Clientes");
        model.addAttribute("customers", customerService.findAll());

        return "dashboard";
    }

    @GetMapping("/customer/new")
    public String create(Model model) {

        Customer customer = new Customer();

        model.addAttribute("title", "Nuevo Cliente");
        model.addAttribute("customer", customer);
        return "form";
    }

    @GetMapping("/customer/update/{id}")
    public String update(@PathVariable(value = "id") Integer id, Model model) {
        Customer customer = null;

        if(id == 0) {
            return "redirect:/";
        }

        customer = customerService.findById(id);
        if(customer == null) {
            return "redirect:/";
        }

        model.addAttribute("title", "Editar Cliente");
        model.addAttribute("customer", customer);
        return "form";
    }

    @PostMapping("/customer/new")
    public String postCreate(@Valid Customer customer, BindingResult result, Model model, @RequestParam("file") MultipartFile image) {
        if(result.hasErrors()) {
            model.addAttribute("title", "Nuevo Cliente");
            return "form";
        }

        if(!image.isEmpty()) {
            UploadsFilesImpl upload = new UploadsFilesImpl();
            String filename = upload.copy(image);
            customer.setImage(filename);
        }

        customerService.save(customer);
        return "redirect:/";
    }

    @PostMapping("/customer/update/{id}")
    public String postUpdate(@PathVariable(value = "id") Integer id, @Valid Customer customer, BindingResult result, Model model, @RequestParam("file") MultipartFile image) {
        if(result.hasErrors()) {
            model.addAttribute("title", "Editar Cliente");
            return "form";
        }

        UploadsFilesImpl upload = new UploadsFilesImpl();

        if(!image.isEmpty() && customer.getImage() == "") {
            String filename = upload.copy(image);
            customer.setImage(filename);

        } else if (!image.isEmpty() && customer.getImage() != "") {
            upload.delete(customer.getImage());
            String filename = upload.copy(image);
            customer.setImage(filename);
        }

        customerService.save(customer);
        return "redirect:/";
    }

    @GetMapping("/customer/delete/{id}")
    public String deleteCustomer(@PathVariable(value = "id") Integer id) {
        Customer customer = null;
        UploadsFilesImpl uploadsFiles = new UploadsFilesImpl();

        if(id == 0) {
            return "redirect:/";
        }

        customer = customerService.findById(id);
        if(customer == null) {
            return "redirect:/";
        }

        uploadsFiles.delete(customer.getImage());
        customerService.deleteById(id);

        return "redirect:/";
    }
}
