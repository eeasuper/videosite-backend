package com.nano.videosite.repositories;

import org.springframework.data.repository.CrudRepository;

import com.nano.videosite.models.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long>{

}
