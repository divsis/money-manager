package com.divsis.money_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private Long profileId;
    private LocalDateTime  creationDate;
    private LocalDateTime  updateDate;
    private String type;
    private String icon;
}
