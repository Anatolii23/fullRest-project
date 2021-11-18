package com.example.payroll.services;

import com.example.payroll.entity.Customer;
import com.example.payroll.entity.Employee;
import com.example.payroll.entity.Order;
import com.example.payroll.errors.OrderNotFoundException;
import com.example.payroll.repository.CustomerRepository;
import com.example.payroll.repository.EmployeeRepository;
import com.example.payroll.repository.OrderRepository;
import com.example.payroll.rest.dto.OrderDto;
import com.example.payroll.until.EntityDtoMapper;
import com.example.payroll.until.OrderModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServices {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderModelAssembler assembler;

    public OrderDto getNewOrder(OrderDto orderDto, Long customerId, Long employeeId) {
        Order order = EntityDtoMapper.mappedToOrderEntity(orderDto);
        order.setStatus(Order.Status.IN_PROGRESS);
        order.setDate(new Date());
        Customer customerRepositoryById = customerRepository.getById(customerId);
        order.setCustomer(customerRepositoryById);
        Employee employeeRepositoryById = employeeRepository.getById(employeeId);
        order.setEmployee(employeeRepositoryById);
        List<Order> customerOrdersSet = customerRepositoryById.getCustomerOrdersList();
        customerOrdersSet.add(order);
        List<Order> employeeOrdersSet = employeeRepositoryById.getEmployeeOrdersList();
        employeeOrdersSet.add(order);
        orderRepository.save(order);
        return EntityDtoMapper.mappedToOrderDto(order);
    }

    public List<EntityModel<OrderDto>> getAllEntityModelsOrders() {
        return orderRepository.findAll().stream()
                .map(EntityDtoMapper::mappedToOrderDto)
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<OrderDto> getOrderDto(Long id) {
        OrderDto orderDto = orderRepository.findById(id).map(EntityDtoMapper::mappedToOrderDto)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return assembler.toModel(orderDto);
    }

    public ResponseEntity<?> getResponseEntityForDeleteMapping(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            order.setStatus(Order.Status.CANCELLED);
            orderRepository.save(order);
            return ResponseEntity.ok(assembler.toModel(EntityDtoMapper.mappedToOrderDto(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

    public ResponseEntity<?> getResponseEntityForPutMappingOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            order.setStatus(Order.Status.COMPLETED);
            orderRepository.save(order);
            return ResponseEntity.ok(assembler.toModel(EntityDtoMapper.mappedToOrderDto(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't complete an order that is in the " + order.getStatus() + " status"));
    }
}
