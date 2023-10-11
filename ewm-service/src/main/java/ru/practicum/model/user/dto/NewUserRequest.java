package ru.practicum.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank
    @Size(max = 250, min = 2)
    public String name;
    @NotBlank
    @Email
    @Size(max = 254, min = 6)
    private String email;
}
