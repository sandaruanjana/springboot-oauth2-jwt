package com.example.resourceserverjwtauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private String field;
    private String errorMessage;
}
