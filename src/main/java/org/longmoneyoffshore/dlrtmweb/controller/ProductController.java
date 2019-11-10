package org.longmoneyoffshore.dlrtmweb.controller;

import org.longmoneyoffshore.dlrtmweb.entities.models.entity.Product;
import org.longmoneyoffshore.dlrtmweb.service.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ProductController  {

    @Qualifier("productService")
    private ProductService productService;

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    //todo better version
    @GetMapping("/products")
    public List<Product> getAllProducts() {

        return productService.getAllProducts();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/test")
    public String printTest() {
        return "Testing.";
    }

    @RequestMapping("/products/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addProduct(@RequestBody Product product) {

        productService.insertProduct(product);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/products/{id}")
    public void updateProduct(@RequestBody Product product, @PathVariable String id) {
        System.out.println("REQUEST / UPDATE PRODUCT");

        productService.updateProduct(product);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "products/{id}")
    public void deleteProduct(@PathVariable String id) {

        System.out.println("REQUEST /DELETE PRODUCT");

        productService.deleteProductById(id);

    }

}
