package com.example.storyappdicoding.model

data class ResponseListStories(
    val error: Boolean,
    val listStory: List<Story>,
    val message: String
)