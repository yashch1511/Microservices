package com.ecommerce.product_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public Product updateProductQuantity(Long id, Integer quantity) {
    Product product = productRepository.findById(id).orElse(null);

    if (product == null) {
        return null;
    }

    product.setQuantity(quantity);
    return productRepository.save(product);
}

    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());

            return productRepository.save(product);
        }

        return null;
    }

    public Product patchProduct(Long id, Product partialProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

            if (partialProduct.getName() != null) {
                product.setName(partialProduct.getName());
            }
            if (partialProduct.getDescription() != null) {
                product.setDescription(partialProduct.getDescription());
            }
            if (partialProduct.getPrice() != null) {
                product.setPrice(partialProduct.getPrice());
            }
            if (partialProduct.getQuantity() != null) {
                product.setQuantity(partialProduct.getQuantity());
            }

            return productRepository.save(product);
        }

        return null;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
