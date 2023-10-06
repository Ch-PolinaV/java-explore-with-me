package ru.practicum.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserShortDto {
    @NotNull
    private Long id;
    @NotNull
    public String name;
}
