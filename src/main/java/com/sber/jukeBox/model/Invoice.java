package com.sber.jukeBox.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Invoice {

    private String email;
    private int userId;
    private Status paymentStatus;
    private int paymentCode;

    public enum Status {
        Success, Fail, Wait;
    }
}
