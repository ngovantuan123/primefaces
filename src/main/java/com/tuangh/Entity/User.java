package com.tuangh.Entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String userName;
    private String password;
    private String salt;
    private String role;
    private List<String> permisstions = new ArrayList<>();

}
