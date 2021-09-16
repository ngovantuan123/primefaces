package com.tuangh.controller;

import com.tuangh.Entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
@ManagedBean(name="productController")
@RequestScoped
@Getter
@Setter
public class ProductController {
    private List<Product> allProduct = Arrays.asList(Product.builder().productId("0")
                    .productCategory("samsung").createdDate(new Date()).price("1560000").productName("product name").build(),
            Product.builder().productId("1")
                    .productCategory("samsung1").createdDate(new Date()).price("15630000").productName("product name").build());
    public Product product = new Product();
    @RequiresPermissions(value = {"product:view"})
    public List<Product> getAll(){
        return this.allProduct;
    }
    @RequiresPermissions(value = {"product:add"})
    public void add() throws IOException {
        if(SecurityUtils.getSubject().isPermitted("product:add")){
            System.out.println("ok");
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/admin/product/allproduct.htm");
        }else{
            //logger
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Permission denied!", "Your don't have permission"));
        }
    }
    public String edit(){
        System.out.println("ok");
        return "edit";
    }
    public void delete(){

    }
}
