package com.apex.eqp.inventory.services;

import com.apex.eqp.inventory.Exceptions.ResourceNotFoundException;
import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.helpers.ProductFilter;
import com.apex.eqp.inventory.repositories.InventoryRepository;
import com.apex.eqp.inventory.repositories.RecalledProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final InventoryRepository inventoryRepository;
    private final RecalledProductRepository recalledProductRepository;

    @Transactional
    public Product save(Product product) {
        return inventoryRepository.save(product);
    }

    public Collection<Product> getAllProduct() {
        Set<String> recalledProducts=recalledProductRepository.findAll().stream().map(rp->rp.getName()).collect(Collectors.toSet());
        ProductFilter filter = new ProductFilter(recalledProducts);
        // we can improve this function by reducing the number of database calls(currently we are doing 2 calls). we can use sub queries or joins and limit this to 1 database call
        return filter.removeRecalledFrom(inventoryRepository.findAll());
    }

    public Optional<Product> findById(Integer id) {
        return inventoryRepository.findById(id);
    }

    public void deleteById(Integer id){
        Optional<Product>product=inventoryRepository.findById(id);
        if(product.isPresent()){
            Product toBeDeleted=product.get();
            inventoryRepository.delete(toBeDeleted);
        }else{
            throw new ResourceNotFoundException("Product not found");
        }
    }

}
