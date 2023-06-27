package com.example.storyappdicoding.model

data class ResponseDetailStory(
    val error: Boolean,
    val message: String,
    val story: Story
)