package com.divsis.money_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FilterDTO {
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyword;
    private String sortField; //date amount name
    private String sortOrder; //ascen  descen
}
