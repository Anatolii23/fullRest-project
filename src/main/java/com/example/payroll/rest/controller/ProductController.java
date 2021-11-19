package com.example.payroll.rest.controller;

import com.example.payroll.repository.ProductRepository;
import com.example.payroll.rest.dto.CustomerDto;
import com.example.payroll.rest.dto.ProductDto;
import com.example.payroll.services.ProductService;
import com.example.payroll.until.ProductModelAssembler;
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
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProductModelAssembler productModelAssembler;

    @GetMapping("/products")
    public CollectionModel<EntityModel<ProductDto>> getAllProducts() {
        List<EntityModel<ProductDto>> products = productService.getAllProductsModel();
        return CollectionModel.of(products, linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
    }
    @GetMapping("/products/{id}")
    public EntityModel<ProductDto> getOneProductById(@PathVariable Long id) {
        return productService.getProductDto(id);
    }
    @PostMapping("/products")
    public ResponseEntity<?> createNewProduct(@RequestBody ProductDto productDto) {
        EntityModel<ProductDto> entityModel = productService.getProductDtoEntityModel(productDto);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id) {
        productService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/products/{id}")
    public ResponseEntity<?> replaceCustomer(@RequestBody ProductDto newProduct, @PathVariable Long id) {
        EntityModel<ProductDto> entityModel = productService.getUpdateProduct(newProduct, id);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);


    }
}
