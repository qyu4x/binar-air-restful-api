package com.binarair.binarairrestapi.model.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftRequest {

    @NotEmpty(message = "modelis required.")
    private String model;

    @Min(value = 0L, message = "passenger be positive")
    private Integer passengerCapacity;

    @Min(value = 0L, message = "total unit be positive")
    private Integer totalUnit;

    @NotEmpty(message = "seat arrangementis required.")
    private String seatArrangement;

    @Min(value = 0L, message = "distance between seat must be positive")
    private Integer distanceBetweenSeat;

    @NotEmpty(message = "seat length unit is required.")
    private String seatLengthUnit;

    @NotEmpty(message = "aircraft manufactureId is required.")
    private String airCraftManufactureId;

    @NotEmpty(message = "travel class id is required.")
    private String travelClassId;

    @NotEmpty(message = "airlines di is required.")
    private String airlinesId;

}
