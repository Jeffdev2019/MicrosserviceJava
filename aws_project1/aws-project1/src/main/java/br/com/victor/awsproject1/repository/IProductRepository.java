package br.com.victor.awsproject1.repository;

import br.com.victor.awsproject1.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByCode(String code);

}
