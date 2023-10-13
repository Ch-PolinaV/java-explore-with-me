package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByAuthorId(Long authorId, Pageable pageable);

    List<Comment> findCommentsByEventId(Long eventId, Pageable pageable);
}