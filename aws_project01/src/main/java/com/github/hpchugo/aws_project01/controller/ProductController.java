package com.github.hpchugo.aws_project01.controller;

import com.github.hpchugo.aws_project01.enums.EventType;
import com.github.hpchugo.aws_project01.model.Product;
import com.github.hpchugo.aws_project01.repository.ProductRepository;
import com.github.hpchugo.aws_project01.service.ProductPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final ProductRepository productRepository;
    private final ProductPublisher productPublisher;

    @Autowired
    public ProductController(ProductRepository productRepository, ProductPublisher productPublisher) {
        this.productRepository = productRepository;
        this.productPublisher = productPublisher;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(product -> products.add(product));
        return ResponseEntity.ok(products);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        if(isPresentProduct(id) != null) {
            var product = isPresentProduct(id);
            return ResponseEntity.ok(product);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/byCode/{code}")
    public ResponseEntity<?> getProductByCode(@PathVariable String code){
        Optional<Product> product = productRepository.findByCode(code);
        if(product.isPresent()) {
            return ResponseEntity.ok(product.get());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> saveProduct(@RequestBody Product product){
        var productCreated = productRepository.save(product);
        productPublisher.publishProductEvent(productCreated, EventType.PRODUCT_CREATED, "Madruguinha");
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Product product){
        if(isPresentProduct(id) != null){
            var productInDB = isPresentProduct(id);
            productInDB.setName(product.getName());
            productInDB.setModel(product.getModel());
            productInDB.setPrice(product.getPrice());
            var productUpdated = productRepository.save(productInDB);
            productPublisher.publishProductEvent(productUpdated, EventType.PRODUCT_UPDATED, "Kiko");
            return new ResponseEntity<>(productUpdated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id){
        if(isPresentProduct(id) != null){
            var product = isPresentProduct(id);
            productRepository.delete(product);
            productPublisher.publishProductEvent(product, EventType.PRODUCT_DELETED, "Chiquinha");
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Product isPresentProduct(long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent())
            return product.get();
        return null;
    }
}
