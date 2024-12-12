package com.ccleaninc.cclean.customerssubdomain.businesslayer;

import java.util.List;

import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import com.ccleaninc.cclean.customerssubdomain.datalayer.Customer;
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

    @Override
    public List<CustomerResponseModel> searchCustomers(String searchTerm) {
        return customerResponseMapper.entityToResponseModelList(
            customerRepository.searchByNameOrCompany(searchTerm)
        );
    }

    @Override
    public CustomerResponseModel getCustomerByCustomerId(String customerId) {
        return customerResponseMapper.entityToResponseModel(
            customerRepository.findByCustomerIdentifier_CustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"))
        );
    }

    @Override
    public void deleteCustomerByCustomerId(String customerId) {
        Customer customer = customerRepository.findByCustomerIdentifier_CustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    @Override
    public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel) {
        Customer customer = customerRequestMapper.customerModelToEntity(customerRequestModel);
        return customerResponseMapper.entityToResponseModel(customerRepository.save(customer));
    }

    @Override
    public CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel) {
        Customer existingCustomer = customerRepository.findByCustomerIdentifier_CustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        // Update fields
        existingCustomer.setFirstName(customerRequestModel.getFirstName());
        existingCustomer.setLastName(customerRequestModel.getLastName());
        existingCustomer.setCompanyName(customerRequestModel.getCompanyName());
        existingCustomer.setEmail(customerRequestModel.getEmail());
        existingCustomer.setPhoneNumber(customerRequestModel.getPhoneNumber());

        return customerResponseMapper.entityToResponseModel(customerRepository.save(existingCustomer));
    }

}
