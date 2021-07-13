package xsolla.webapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xsolla.webapp.model.Product;
import xsolla.webapp.model.enums.ProductType;

import java.util.List;


@Repository
public interface ProductRepo extends JpaRepository<Product, Integer>
{
    Page<Product> findByType(ProductType type, Pageable pageable);

    List<Product> findByType(ProductType type, Sort sort);

    Page<Product> findByPrice(double price, Pageable pageable);

    List<Product> findByPrice(double price, Sort sort);

    Page<Product> findByTypeAndPrice(ProductType type, double price, Pageable pageable);

    List<Product> findByTypeAndPrice(ProductType type, double price, Sort sort);
}
