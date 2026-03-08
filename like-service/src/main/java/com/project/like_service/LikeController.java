package com.project.like_service;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @PostMapping("/{postId}")
    public String likePost(@PathVariable Long postId) {
        return "You liked the post " + postId;
    }

    @GetMapping("/test")
    public String fetchLikeCount() {
        return "Like count fetched";
    }
}
