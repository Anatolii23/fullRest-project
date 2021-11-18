package com.example.payroll.rest.controller;

import com.example.payroll.rest.dto.OrderDto;
import com.example.payroll.services.OrderServices;
import com.example.payroll.until.OrderModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderModelAssembler assembler;
    private final OrderServices orderServices;


    @GetMapping("/orders")
    public CollectionModel<EntityModel<OrderDto>> getAllOrders() {
        List<EntityModel<OrderDto>> orders = orderServices.getAllEntityModelsOrders();
        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel());
    }


    @GetMapping("/orders/{id}")
    public EntityModel<OrderDto> getOneOrder(@PathVariable Long id) {
        return orderServices.getOrderDto(id);
    }

    @PostMapping("/orders")
    public ResponseEntity<EntityModel<OrderDto>> newOrder(@RequestBody OrderDto orderDto,
                                                          @RequestParam Long customerId,
                                                          @RequestParam Long employeeId) {
        OrderDto newOrder = orderServices.getNewOrder(orderDto, customerId, employeeId);
        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).getOneOrder(newOrder.getId())).toUri())
                .body(assembler.toModel(newOrder));
    }


    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        return orderServices.getResponseEntityForDeleteMapping(id);
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long id) {
        return orderServices.getResponseEntityForPutMappingOrder(id);
    }

}
