package ru.practicum.model.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import static ru.practicum.model.DateTimeConstants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime updatedOn;
    private Long authorId;
    private Long eventId;
}
