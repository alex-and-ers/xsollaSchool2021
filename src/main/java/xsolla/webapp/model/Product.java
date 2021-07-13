package xsolla.webapp.model;


import xsolla.webapp.model.enums.ProductType;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private double price;

    public Product(String name, ProductType type, double price)
    {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public Product()
    {
    }

    public Product(int id, String name, ProductType type, double price)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public ProductType getType()
    {
        return type;
    }

    public double getPrice()
    {
        return price;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setType(ProductType type)
    {
        this.type = type;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }
}
