package com.ubuntuhealth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClinicResponse {

    private Long id;
    private String clinicName;
    private String province;
    private String city;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private String email;

}