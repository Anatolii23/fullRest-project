package com.example.payroll.rest.controller;


import com.example.payroll.rest.dto.EmployeeDto;
import com.example.payroll.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public CollectionModel<EntityModel<EmployeeDto>> getAllEmployee() {
        List<EntityModel<EmployeeDto>> employees = employeeService.getAllEntityModelsEmployees();
        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAllEmployee()).withSelfRel());
    }


    @PostMapping("/employees")
    public ResponseEntity<?> createNewEmployee(@RequestBody EmployeeDto newEmployee) {
        EntityModel<EmployeeDto> entityModel = employeeService.getEmployeeDtoEntityModel(newEmployee);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<EmployeeDto> getOneEmployee(@PathVariable Long id) {

        return employeeService.getEntityModel(id);
    }


    @PutMapping("/employees/{id}")
    public ResponseEntity<?> replaceEmployee(@RequestBody EmployeeDto newEmployee, @PathVariable Long id) {
        EntityModel<EmployeeDto> entityModel = employeeService.getUpdatedEmployee(newEmployee, id);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }
}
