package com.example.payroll.services;

import com.example.payroll.entity.Customer;
import com.example.payroll.errors.EmployeeNotFoundException;
import com.example.payroll.repository.CustomerRepository;
import com.example.payroll.rest.dto.CustomerDto;
import com.example.payroll.until.CustomerModelAssembler;
import com.example.payroll.until.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomersService {
    private final CustomerRepository customerRepository;
    private final CustomerModelAssembler customerModelAssembler;

    public EntityModel<CustomerDto> getUpdateCustomer(CustomerDto newCustomer, Long id) {
        newCustomer.setId(id);
        customerRepository.findById(id)
                .map(customer -> {
                    BeanUtils.copyProperties(newCustomer,customer);
                    return customerRepository.save(customer);
                })
                .orElseGet(() -> {
                    newCustomer.setId(id);
                    return customerRepository.save(EntityDtoMapper.mappedToCustomerEntity(newCustomer));
                });
       return customerModelAssembler.toModel(newCustomer);
    }

    public List<EntityModel<CustomerDto>> getAllEntityModelCustomers() {
        return customerRepository.findAll().stream()
                .map(EntityDtoMapper::mappedToCustomerDto)
                .map(customerModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<CustomerDto> getCustomerDto(Long id) {
        CustomerDto customerDto = customerRepository.findById(id)
                .map(EntityDtoMapper::mappedToCustomerDto)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return customerModelAssembler.toModel(customerDto);
    }

    public EntityModel<CustomerDto> getCustomerDtoEntityModel(CustomerDto newCustomer) {
        Customer customer =EntityDtoMapper.mappedToCustomerEntity(newCustomer);
        customerRepository.save(customer);
        CustomerDto customerDto = EntityDtoMapper.mappedToCustomerDto(customer);
        return customerModelAssembler.toModel(customerDto);
    }
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }
}
