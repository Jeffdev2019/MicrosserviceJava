package br.com.victor.awsproject1.controller;

import br.com.victor.awsproject1.Enums.EventType;
import br.com.victor.awsproject1.model.Product;
import br.com.victor.awsproject1.repository.IProductRepository;
import br.com.victor.awsproject1.service.ProductPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/products")
public class ProductController {

    private IProductRepository productRepository;
    private ProductPublisher productPublisher;

    @Autowired
    public ProductController(IProductRepository productRepository, ProductPublisher productPublisher) {

        this.productRepository = productRepository;
        this.productPublisher = productPublisher;
    }

    @GetMapping
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id) {
        Optional<Product> optProduct = productRepository.findById(id);

        if(optProduct.isPresent()){
            return new ResponseEntity<Product>(optProduct.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //return optProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
        // .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(
            @RequestBody Product product) {

       Product productCreated = productRepository.save(product);

       productPublisher.publishProductEvent(productCreated, EventType.PRODUCT_CREATED, "Jefferson");

       return new ResponseEntity<Product>(productCreated,
               HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Product> updateProduct(
            @RequestBody Product product, @PathVariable("id") long id) {

        if(productRepository.existsById(id)){
            product.setID(id);

            Product productUpdated = productRepository.save(product);

            productPublisher.publishProductEvent(productUpdated, EventType.PRODUCT_UPDATE, "Victor");

            return new ResponseEntity<Product>(productUpdated,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();

            productRepository.delete(product);

            productPublisher.publishProductEvent(product, EventType.PRODUCT_DELETED, "Da Silva");

            return new ResponseEntity<Product>(product, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/bycode")
    public ResponseEntity<Product> findByCode(@RequestParam String code) {
        Optional<Product> optProduct = productRepository.findByCode(code);
        if(optProduct.isPresent()){
            return new ResponseEntity<Product>(optProduct.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //return optProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK))
        // .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }





}
