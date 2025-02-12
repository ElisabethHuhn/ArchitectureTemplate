package com.huhn.fetchrewards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huhn.fetchrewards.datasource.MainRepositoryImpl
import com.huhn.fetchrewards.datasource.remotedatasource.networkModel.User
import com.huhn.fetchrewards.datasource.remotedatasource.networkModel.UserResponse
import com.huhn.fetchrewards.ui.UserEvent
import com.huhn.fetchrewards.ui.UserUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModelImpl(
    private val repo : MainRepositoryImpl
) : ViewModel()
{
    //Define state for recomposing UI
    private val _userState = MutableStateFlow(UserUIState())
    val userState = _userState.asStateFlow()


    //Define responses to User Trigger Events
    fun onUserEvent (event: UserEvent){
        when(event) {
            is UserEvent.OnInitializeEvent -> onInitialization()
            is UserEvent.OnGetUsersEvent -> onGetUsers()
            is UserEvent.OnUpdateListIdCountEvent -> updateListIdCount(event.listId)
        }
    }

    //region stateUpdate

    //Define functions that update state values

    private  fun onUsersChanged(users: ArrayList<User>) {
        _userState.update { it.copy(userIdList = users) }
    }




    private fun onInitialization() {
        _userState.update { it.copy(
            isInitialized = false,
            isRefreshing = false,
            userIdList = ArrayList(),
            user = null,
        ) }
    }

    //endregion

    //region calculators
    private fun updateListIdCount(listId : Int)  {
        val users = _userState.value.userIdList
        val count = users.count { it.listId == listId }
        _userState.update { it.copy(listIdCount = Pair(listId, count)) }
    }

    //endregion



    //define functions that fetch data

    /*
     * Makes data source calls and updates state with the response
     */
    private fun onGetUsers() {
        viewModelScope.launch {
            try {
                val users = repo.getUserList()
                users.sortBy { it.listId }
                val sortedList = users.sortedWith(compareBy({ it.listId }, { it.name }))
                val arrayList :ArrayList<User> = ArrayList()
                sortedList.forEach { user ->
                    if (!user.name.isNullOrEmpty()) {
                        arrayList.add(user)
                    }
                }


                onUsersChanged(arrayList)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
