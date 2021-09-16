package com.tuangh.Entity;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private String productId;
    private String productName;
    private String productCategory;
    private String price;
    private Date createdDate;

}