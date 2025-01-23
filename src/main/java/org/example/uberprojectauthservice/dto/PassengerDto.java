package org.example.uberprojectauthservice.dto;

import lombok.*;
import org.example.uberprojectauthservice.models.Passenger;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {

    private String id;

    private String name;

    private String email;

    private String password; //encrypted

    private String phoneNumber;

    private Date createdAt;

    public static PassengerDto from(Passenger p)
    {
        PassengerDto result=PassengerDto.builder()
                .id(p.getId().toString())
                .name(p.getName())
                .email(p.getEmail())
                .password(p.getPassword())
                .phoneNumber(p.getPhoneNumber())
                .createdAt(p.getCreatedAt())
                .build();

        return result;
    }
}
