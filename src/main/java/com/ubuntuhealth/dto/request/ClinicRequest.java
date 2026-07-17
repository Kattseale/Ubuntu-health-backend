package com.ubuntuhealth.dto.request;

import lombok.Data;

@Data
public class ClinicRequest {

    private String clinicName;
    private String province;
    private String city;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private String email;

}