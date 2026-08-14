package com.quickdrop.backend.dto;

import lombok.Data;

@Data
public class JoinRequest {
    // this variable name must exactly match the json key from the frontend
    private String code;
}
