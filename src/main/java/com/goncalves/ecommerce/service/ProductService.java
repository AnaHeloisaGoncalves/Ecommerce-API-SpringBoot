package com.goncalves.ecommerce.service;

import com.goncalves.ecommerce.entity.Product;
import com.goncalves.ecommerce.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    //Dependency injection via the constructor
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Create product
    public Product save(Product product){
        return productRepository.save(product);
    }

    //List all products
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    //Get product by id
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    }

    //Partial update product by id
    public Product partialUpdateById(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (updatedProduct.getName() != null) {
            existingProduct.setName(updatedProduct.getName());
        }
        if (updatedProduct.getDescription() != null) {
            existingProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getPrice() != null) {
            existingProduct.setPrice((updatedProduct.getPrice()));
        }
        if (updatedProduct.getStockQuantity() != null) {
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
        }

        return productRepository.save(existingProduct);
    }

    //Delete product by id
    public void deleteById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        product.setActive(false);
        productRepository.save(product);
    }
}
