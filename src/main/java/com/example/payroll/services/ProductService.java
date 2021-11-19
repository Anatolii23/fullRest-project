package com.example.payroll.services;

import com.example.payroll.entity.Product;
import com.example.payroll.errors.EmployeeNotFoundException;
import com.example.payroll.errors.ProductNotFoundException;
import com.example.payroll.repository.ProductRepository;
import com.example.payroll.rest.dto.ProductDto;
import com.example.payroll.until.EntityDtoMapper;
import com.example.payroll.until.ProductModelAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductModelAssembler productModelAssembler;

    public List<EntityModel<ProductDto>> getAllProductsModel() {
        return productRepository.findAll().stream()
                .map(EntityDtoMapper::mappedToProductDto)
                .map(productModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<ProductDto> getProductDto(Long id) {
        ProductDto productDto = productRepository.findById(id)
                .map(EntityDtoMapper::mappedToProductDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productModelAssembler.toModel(productDto);
    }

    public EntityModel<ProductDto> getProductDtoEntityModel(ProductDto newProduct) {
        Product product = EntityDtoMapper.mappedToProductEntity(newProduct);
        productRepository.save(product);
        ProductDto productDto = EntityDtoMapper.mappedToProductDto(product);
        return productModelAssembler.toModel(productDto);
    }

    public void deleteCustomerById(Long id) {
        productRepository.deleteById(id);
    }

    public EntityModel<ProductDto> getUpdateProduct(ProductDto newProduct, Long id) {
        newProduct.setId(id);
        productRepository.findById(id)
                .map(product -> {
                    BeanUtils.copyProperties(newProduct, product);
                    return productRepository.save(product);
                })
                .orElseGet(() -> {
                    newProduct.setId(id);
                    return productRepository.save(EntityDtoMapper.mappedToProductEntity(newProduct));
                });
        return productModelAssembler.toModel(newProduct);
    }
}
