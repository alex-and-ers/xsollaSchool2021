import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.MockMvcResultMatchersDsl.*;

import org.springframework.web.context.WebApplicationContext;
import xsolla.webapp.WebApp;
import xsolla.webapp.controller.WebappController;
import xsolla.webapp.model.Product;
import xsolla.webapp.model.enums.ProductType;
import xsolla.webapp.repository.ProductRepo;
import xsolla.webapp.response.ProductResponse;
import xsolla.webapp.service.ProductService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
//@WebMvcTest(WebappController.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes={ WebApp.class })
public class ControllerTest
{
    private MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private ProductRepo productRepo;

    @Before
    public void setUp()
    {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getProductByIdShouldReturnProduct() throws Exception
    {
        Product product= new Product(111, "MGS5", ProductType.GAME, 1500.0);

        when(productRepo.findById(111)).thenReturn(Optional.of(product));

        mvc.perform(get("/webapp/product?id=111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.product.id").value(111))
                .andExpect(jsonPath("$.product.name").value("MGS5"))
                .andExpect(jsonPath("$.product.type").value("GAME"))
                .andExpect(jsonPath("$.product.price").value(1500.0));
    }

    @Test
    public void shouldReturn404WhenProductNotFound() throws Exception
    {
        when(productRepo.findById(111)).thenReturn(Optional.empty());

        mvc.perform(get("/webapp/product?id=12345")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateProductWhenValidRequest() throws Exception
    {
        Product product= new Product(111, "MGS5", ProductType.GAME, 1500.0);

        when(productRepo.save(product)).thenReturn(new Product("MGS5", ProductType.GAME, 1500.0));

        mvc.perform(post("/webapp/product?name=MGS5&type=game&price=1500.0"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.product.name").value("MGS5"))
                .andExpect(jsonPath("$.product.type").value("GAME"))
                .andExpect(jsonPath("$.product.price").value(1500.0));
    }
}
