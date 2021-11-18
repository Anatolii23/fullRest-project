package com.example.payroll.until;

import com.example.payroll.entity.Customer;
import com.example.payroll.entity.Employee;
import com.example.payroll.entity.Order;
import com.example.payroll.rest.dto.CustomerDto;
import com.example.payroll.rest.dto.EmployeeDto;
import com.example.payroll.rest.dto.OrderDto;
import org.springframework.beans.BeanUtils;

public class EntityDtoMapper {
    public static EmployeeDto mappedToEmployeeDto(Employee employee){
        EmployeeDto employeeDto = EmployeeDto.builder().build();
        BeanUtils.copyProperties(employee,employeeDto);
        return employeeDto;
    }
    public static Employee mappedToEmployeeEntity(EmployeeDto employeeDto){
        Employee employee = Employee.builder().build();
        BeanUtils.copyProperties(employeeDto,employee);
        return employee;
    }
    public static CustomerDto mappedToCustomerDto(Customer customer){
        CustomerDto customerDto = CustomerDto.builder().build();
        BeanUtils.copyProperties(customer,customerDto);
        return customerDto;
    }
    public static Customer mappedToCustomerEntity(CustomerDto customerDto){
        Customer customer = Customer.builder().build();
        BeanUtils.copyProperties(customerDto,customer);
        return customer;
    }
    public static OrderDto mappedToOrderDto(Order order){
        OrderDto orderDto = OrderDto.builder().build();
        BeanUtils.copyProperties(order,orderDto);
        return orderDto;
    }
    public static Order mappedToOrderEntity(OrderDto orderDto){
        Order order = Order.builder().build();
        BeanUtils.copyProperties(orderDto,order);
        return order;
    }
}
