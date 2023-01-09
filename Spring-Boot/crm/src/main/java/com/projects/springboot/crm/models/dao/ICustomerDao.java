package com.projects.springboot.crm.models.dao;

import com.projects.springboot.crm.models.entity.Customer;
import org.springframework.data.repository.CrudRepository;


public interface ICustomerDao extends CrudRepository<Customer, Integer> {

}
