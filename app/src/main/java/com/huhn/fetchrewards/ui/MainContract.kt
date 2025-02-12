package com.huhn.fetchrewards.ui

import com.huhn.fetchrewards.datasource.remotedatasource.networkModel.User



data class UserUIState(
    val isRefreshing: Boolean = false,
    var isInitialized: Boolean = false,
    val userIdList: ArrayList<User> = ArrayList(),
    val listIdCount: Pair<Int, Int> = Pair(0, 0),
    val user : User? = null,
)

sealed interface UserEvent {
    data object OnInitializeEvent : UserEvent
    data object OnGetUsersEvent : UserEvent
    data class OnUpdateListIdCountEvent(val listId: Int) : UserEvent
}