package xsolla.webapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import xsolla.webapp.model.Product;


public class ProductResponse
{
    @JsonProperty("product")
    private final Product product;

    public ProductResponse(Product product)
    {
        this.product = product;
    }

    public Product getProduct()
    {
        return product;
    }
}
