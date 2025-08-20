package com.smartbin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AreaDTO {

    private Long id;

    @NotBlank(message = "Area name is required")
    @Size(max = 100, message = "Area name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    private Integer binCount;

    // Constructors
    public AreaDTO() {
    }

    public AreaDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public AreaDTO(Long id, String name, String description, Integer binCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.binCount = binCount;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBinCount() {
        return binCount;
    }

    public void setBinCount(Integer binCount) {
        this.binCount = binCount;
    }
}
