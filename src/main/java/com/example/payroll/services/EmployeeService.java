package com.example.payroll.services;

import com.example.payroll.entity.Employee;
import com.example.payroll.errors.EmployeeNotFoundException;
import com.example.payroll.repository.EmployeeRepository;
import com.example.payroll.rest.dto.EmployeeDto;
import com.example.payroll.until.EmployeeModelAssembler;
import com.example.payroll.until.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeModelAssembler assembler;

    public EntityModel<EmployeeDto> getUpdatedEmployee(EmployeeDto newEmployee, Long id) {
        newEmployee.setId(id);
        employeeRepository.findById(id)
                .map(employee -> {
                    BeanUtils.copyProperties(newEmployee,employee);
                    return employeeRepository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return employeeRepository.save(EntityDtoMapper.mappedToEmployeeEntity(newEmployee));
                });
        return assembler.toModel(newEmployee);
    }

    public List<EntityModel<EmployeeDto>> getAllEntityModelsEmployees() {
        return employeeRepository.findAll().stream()
                .map(EntityDtoMapper::mappedToEmployeeDto)
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<EmployeeDto> getEmployeeDtoEntityModel(EmployeeDto newEmployee) {
        Employee employee = EntityDtoMapper.mappedToEmployeeEntity(newEmployee);
        employeeRepository.save(employee);
        EmployeeDto employeeDto = EntityDtoMapper.mappedToEmployeeDto(employee);
        return assembler.toModel(employeeDto);
    }

    public EntityModel<EmployeeDto> getEntityModel(Long id) {
        EmployeeDto employeeDto = employeeRepository.findById(id).map(EntityDtoMapper::mappedToEmployeeDto)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return assembler.toModel(employeeDto);
    }
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }
}
