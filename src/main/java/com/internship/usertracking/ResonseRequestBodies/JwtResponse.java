package com.internship.usertracking.ResonseRequestBodies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtResponse {
    private final String token;
}
