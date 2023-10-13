package ru.practicum.model.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateUpdateDto {
    @NotBlank
    @Size(max = 500, min = 1)
    private String text;
}