package com.project.share_service;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/share")
public class ShareController {

    @PostMapping("/{postId}")
    public String sharePost(@PathVariable Long postId) {
        return "shared post " + postId;
    }

    @GetMapping("/test")
    public String fetchSharedPost() {
        return "Shared post fetched";
    }
}
