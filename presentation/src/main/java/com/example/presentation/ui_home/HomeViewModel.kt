package com.example.presentation.ui_home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {
    private val _posts = MutableStateFlow<PagingData<Post>>(PagingData.empty())
    val posts = _posts.asStateFlow()

    val currentlyPlayingIndex = MutableStateFlow<Int?>(null)


}

sealed class PostContent() {
    data class Image(val imageUrl: String) : PostContent()
    data class Video(val videoUrl: String) : PostContent()
    data class CarouselContent(val imageUrls: List<String>) : PostContent()
}

data class Post(
    val id: String,
    val userId: String,
    val userName: String,
    val avatarUrl: String,
    val content: PostContent,
    val previewUrl: String? = null,
    val contentDescription: String? = null,
    val caption: String?,
    val isLiked: Boolean = false,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val timestamp: Long,
)