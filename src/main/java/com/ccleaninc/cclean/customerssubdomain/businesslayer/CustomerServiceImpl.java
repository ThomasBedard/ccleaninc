package com.ccleaninc.cclean.customerssubdomain.businesslayer;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ccleaninc.cclean.customerssubdomain.datalayer.CustomerRepository;
import com.ccleaninc.cclean.customerssubdomain.datamapperlayer.CustomerRequestMapper;
import com.ccleaninc.cclean.customerssubdomain.datamapperlayer.CustomerResponseMapper;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.*;;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;
    private final CustomerResponseMapper customerResponseMapper;
    private final CustomerRequestMapper customerRequestMapper;

        public CustomerServiceImpl(CustomerRepository customerRepository, CustomerResponseMapper customerResponseMapper, CustomerRequestMapper customerRequestMapper) {
        this.customerRepository = customerRepository;
        this.customerResponseMapper = customerResponseMapper;
        this.customerRequestMapper = customerRequestMapper;
    }

    @Override
    public List<CustomerResponseModel> getAllCustomers() {
        return customerResponseMapper.entityToResponseModelList(customerRepository.findAll());
    }

}
