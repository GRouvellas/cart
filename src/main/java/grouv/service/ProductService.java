package grouv.service;

import grouv.entity.Product;
import grouv.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Geo
 */

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    public Iterable<Product> findAll() {

        return productRepo.findAll();
    }

    public Product find(int id) {

        return productRepo.findById(id).get();
    }

}
