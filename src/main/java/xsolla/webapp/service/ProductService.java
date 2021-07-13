package xsolla.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xsolla.webapp.model.Product;
import xsolla.webapp.model.enums.ProductType;
import xsolla.webapp.repository.ProductRepo;
import xsolla.webapp.response.ProductResponse;

import java.util.*;

@Service
public class ProductService
{
    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo)
    {
        this.productRepo = productRepo;
    }

    public ResponseEntity<ProductResponse> createProduct(String name, ProductType type, double price)
    {
        if (name.equals("") || price < 0.0)
        {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Product product = new Product();
        product.setType(type);

        if (!name.equals(""))
        {
            product.setName(name);
        }
        if (price >= 0.0)
        {
            product.setPrice(price);
        }

        productRepo.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponse(product));
    }

    public ResponseEntity<ProductResponse> getProduct(int id)
    {
        Optional<Product> product = productRepo.findById(id);

        if (product.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ProductResponse productResponse = new ProductResponse(new Product(product.get().getId(), product.get().getName(), product.get().getType(), product.get().getPrice()));
        return ResponseEntity.ok(productResponse);
    }

    public ResponseEntity<ProductResponse> patchProduct(int id, String name, String type, double price)
    {
        Optional<Product> product = productRepo.findById(id);

        if (product.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (name.equals("") || type.equals("") || price < 0.0)
        {
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        Product patchedProduct = product.get();

        if (!name.equals(""))
        {
            patchedProduct.setName(name);
        }
        if (!type.equals(""))
        {
            patchedProduct.setType(ProductType.valueOf(type));
        }
        if (price >= 0.0)
        {
            patchedProduct.setPrice(price);
        }

        productRepo.save(patchedProduct);

        return ResponseEntity.ok(new ProductResponse(patchedProduct));
    }

    public HttpStatus deleteProduct(int id)
    {
        Optional<Product> product = productRepo.findById(id);

        if (product.isEmpty())
        {
            return HttpStatus.NOT_FOUND;
        }

        productRepo.deleteById(product.get().getId());

        return HttpStatus.OK;
    }

    public ResponseEntity<Map<String, Object>> showProducts(String type, double price, int page, int size, String[] sort)
    {
        try
        {
            List<Sort.Order> orders = new ArrayList<>();

            if (sort[0].contains(","))
            {
                for (String sortOrder : sort)
                {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
                }
            }
            else {
                orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
            }

            List<Product> products;
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<Product> pageProds;

            pageProds = productRepo.findAll(pagingSort);

            if (type != null & price < 0.0)
            {
                pageProds = productRepo.findByType(ProductType.valueOf(type.toUpperCase()), pagingSort);
            }
            if (price >= 0 & type == null)
            {
                pageProds = productRepo.findByPrice(price, pagingSort);
            }
            if (price >= 0.0 && type != null)
            {
                pageProds = productRepo.findByTypeAndPrice(ProductType.valueOf(type.toUpperCase()), price, pagingSort);
            }

            products = pageProds.getContent();

            if (products.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("products", products);
            response.put("currentPage", pageProds.getNumber());
            response.put("totalItems", pageProds.getTotalElements());
            response.put("totalPages", pageProds.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Sort.Direction getSortDirection(String direction)
    {
        if (direction.equals("asc"))
        {
            return Sort.Direction.ASC;
        }
        else if (direction.equals("desc"))
        {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }
}
