package com.mdm.mdm.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class MasterDataDTO {

    @Id
    @NotBlank(message = "Code cannot be blank")
    @Size(min = 2, max = 5, message = "Code must be between 2 and 5 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Code must be alphanumeric")
    private String countryID;

    @NotBlank(message = "Name cannot be blank")
    private String countryName;

    @NotNull(message = "Numeric code cannot be null")
    @Min(value = 0, message = "Numeric code must be a non-negative integer")
    private Integer numericCode;

    @NotBlank(message = "Capital city cannot be blank")
    private String capitalCity;

    @NotNull(message = "Population cannot be null")
    @Min(value = 0, message = "Population must be a non-negative integer")
    private Integer population;

    @NotNull(message = "Area cannot be null")
    @Positive(message = "Area must be a positive number")
    private long area;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
