package xsolla.webapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xsolla.webapp.model.enums.ProductType;
import xsolla.webapp.response.ProductResponse;
import xsolla.webapp.service.ProductService;

import java.util.Map;

@RestController
@RequestMapping("/webapp/product")
public class WebappController
{
    @Autowired
    private final ProductService productService;

    public WebappController(ProductService productService)
    {
        this.productService = productService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> saveProduct(@RequestParam(value = "name", defaultValue = "") String name,
                                                       @RequestParam(value = "type", defaultValue = "") String type,
                                                       @RequestParam(value = "price", defaultValue = "0.0") double price)
    {
        try
        {
            ProductType.valueOf(type.toUpperCase());
        }
        catch (EnumConstantNotPresentException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return productService.createProduct(name, ProductType.valueOf(type.toUpperCase()), price);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> getProduct(@RequestParam(value = "id") int id)
    {
        return productService.getProduct(id);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> patchProduct(@RequestParam(value = "id") int id,
                                                        @RequestParam(value = "name", defaultValue = "") String name,
                                                        @RequestParam(value = "type", defaultValue = "") String type,
                                                        @RequestParam(value = "price", defaultValue = "-1.0") double price)
    {
        return productService.patchProduct(id, name, type, price);
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.OK)
    public HttpStatus deleteProduct(@RequestParam(value = "id") int id)
    {
        return productService.deleteProduct(id);
    }

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProducts(@RequestParam(required = false) String type,
                                                              @RequestParam(required = false, defaultValue = "-1.0") double price,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                              @RequestParam(value = "sort", defaultValue = "id,desc") String[] sort)
    {
        return productService.showProducts(type, price, page, size, sort);
    }
}
