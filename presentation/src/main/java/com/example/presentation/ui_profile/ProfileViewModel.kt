package com.example.presentation.ui_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.presentation.R
import com.example.presentation.ui_home.Post
import com.example.presentation.ui_home.listOfPosts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

) : ViewModel() {

//    private val _content = MutableStateFlow<PagingData<Post>>(PagingData.empty())
//    val content = _content.asStateFlow()

    private val _content = MutableStateFlow<List<Post>>(emptyList())
    val content = _content.asStateFlow()

    private val _selectedTab = MutableStateFlow<TabBarItem>(TabBarItem.Grid)
    val selectedTab = _selectedTab.asStateFlow()

    init {
        postItems()
    }

    //Устанавливаем состояние таба, после чего запускаем метод получения контента
    fun setTabBarState(state: TabBarItem) {
        _selectedTab.value = state
        postItems()
    }

    //Проверяем выбранный таб и запускаем метод получения контента
    private fun postItems() {
        when (_selectedTab.value) {
            TabBarItem.Grid -> getContent()
            TabBarItem.Reels -> { _content.value = emptyList() }
            TabBarItem.Tag -> { _content.value = emptyList() }
        }
    }

    private fun getContent() = viewModelScope.launch {
        _content.value = listOfPosts
    }

    private fun getReelsContent() = viewModelScope.launch {
        TODO("Получение контента для таба Reels")
    }

    private fun getTagContent() = viewModelScope.launch {
        TODO("Получение контента для таба Tag")
    }

}

enum class TabBarItem(val icon: Int, val title: String) {
    Grid(icon = R.drawable.grid_on_24px, title = "Grid"),
    Reels(icon = R.drawable.instagram_reels_icon, title = "Reels"),
    Tag(icon = R.drawable.instagram_tag_icon, title = "Tag")
}