package com.projects.springboot.crm.models.service;

import com.projects.springboot.crm.models.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {

    public List<Customer> findAll();

    public void save(Customer customer);

    public Customer findById(Integer id);

    public void deleteById(Integer id);

}
