package com.project.comment_service;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @PostMapping("/{postId}")
    public String addComment(@PathVariable Long postId) {
        return "Commented on post " + postId;
    }

    @GetMapping("/test")
    public String fetchComment() {
        return "Comment fetched";
    }
}
