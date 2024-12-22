package com.restaurent.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserWrapper {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String status;
}
