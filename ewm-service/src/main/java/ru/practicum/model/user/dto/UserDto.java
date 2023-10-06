package ru.practicum.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty
    public String name;
    @NotEmpty
    private String email;
}
