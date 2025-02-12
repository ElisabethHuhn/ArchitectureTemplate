package com.huhn.fetchrewards.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huhn.fetchrewards.R
import com.huhn.fetchrewards.datasource.remotedatasource.networkModel.User
import com.huhn.fetchrewards.ui.theme.LightBlue
import com.huhn.fetchrewards.viewmodel.MainViewModelImpl
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRoute(
    screenTitle: Int,
) {
    val viewModel : MainViewModelImpl = koinViewModel()
    val state by viewModel.userState.collectAsStateWithLifecycle()

    UserScreen (
        screenTitle = screenTitle,
        state = state,
        onUserEvent = viewModel::onUserEvent,
    )
}

@ExperimentalMaterial3Api
@Composable
fun UserScreen(
    screenTitle : Int,
    state: UserUIState,
    onUserEvent: (UserEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(screenTitle),
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
            )
        },


    ) {it

        LaunchedEffect(key1 = "firsttime" ){
            onUserEvent(UserEvent.OnInitializeEvent)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .testTag(tag = "landing_screen"),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(95.0.dp))
            UserList(
                userIdList = state.userIdList,
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = {
                    onUserEvent(UserEvent.OnGetUsersEvent)
                },
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .testTag(tag = "refresh_button")
            ) {
                Text(text = "Refresh Users")
            }
        }
    }
}

@Composable
fun UserList(
    userIdList: ArrayList<User>,
    modifier: Modifier = Modifier,
) {
    var lastListId by remember { mutableStateOf(-1) }
    var count by remember { mutableStateOf(-1) }

    LazyColumn (
        modifier = modifier
            .testTag(tag = "user_list"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

       item {
           if (userIdList.isNotEmpty()) {
               Text(text = "List of Users by ListID then by name")
           }
           else {
               Text(text = "No users found. Please Refresh Users")
           }
       }

        items(
            items = userIdList,
            key = { it.id }
        ) { user : User ->
            if (user.listId != lastListId) {
                count = userIdList.count { it.listId == user.listId }
            }

            UserRow(
//                showListId = user.listId != lastListId,
                listIdCount = count,
                user = user,
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
            )
            lastListId = user.listId
        }
    }
}

@Composable
fun UserRow(
//    showListId: Boolean,
    listIdCount : Int,
    user: User,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(tag = "user_row_surface"),
        color = LightBlue.copy(alpha = 0.4f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(tag = "user_row"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            user.name?.let {
                when {
                    user.name.isNotEmpty() -> {
                        Spacer(modifier = Modifier.height(10.dp))
//                        when {
//                            showListId -> {
//                                Text(
//                                    text = "List ID: ${user.listId} has $listIdCount users",
//                                    fontWeight = FontWeight.Bold
//                                )
//                                Spacer(modifier = Modifier.height(10.dp))
//                            }
//                        }
                        Text(
                            text = "List ID: ${user.listId} has $listIdCount users",
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "User ID: ${user.id}")
                        Text(text = "User Name: ${user.name}")
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}


//@Preview
//@Composable
//fun UserRowPreview() {
//    UserRow(
//        lastId = 0,
//        user = User(
//            id = 2,
//            listId = 2,
//            name = "Leanne Graham"
//        ),
//        modifier = Modifier.fillMaxWidth(),
//        onClick = {}
//    )
//}

val userTestList = arrayListOf<User>(
    User(2, 2, "testName2"),
    User(5, 5, "testName5"),
    User(1, 1, "testName"),
    User(3, 2, "testName"),
    User(4, 4, ""),
    User(6, 2, "testZ"),
    User(7, 2, "testA"),
)
//@Preview
//@Composable
//fun UserListPreview() {
//
//    userTestList.sortBy { it.listId }
//    val sortedList = userTestList.sortedWith(compareBy({ it.listId }, { it.name }))
//    val arrayList :ArrayList<User> = ArrayList()
//    arrayList.addAll(sortedList)
//    UserList(
//        userIdList = arrayList ,
//        modifier = Modifier.fillMaxWidth()
//    )
//}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UserScreenPreview() {
    UserScreen(
        screenTitle = R.string.user_title,
        state = UserUIState(
            isInitialized = true,
            isRefreshing = false,
            userIdList = userTestList,
            user = null
        ),
        onUserEvent = {}
    )
}