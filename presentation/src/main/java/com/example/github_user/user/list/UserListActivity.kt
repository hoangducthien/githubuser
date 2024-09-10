package com.example.github_user.user.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.github_user.R
import com.example.github_user.common.BaseActivity
import com.example.github_user.user.detail.UserDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import safe

@AndroidEntryPoint
class UserListActivity : BaseActivity<UserListViewModel>() {

    override val viewModel: UserListViewModel by viewModels()
    // avoid loop calling load more when stay at the bottom of the list & API is failing
    // require to scroll up & down again to trigger load more again
    private var canTriggerLoadMore = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.firstLoad()
        enableEdgeToEdge()

        showContentWithDefaultHeader(getString(R.string.home_title)) { innerPadding ->
            val userList by viewModel.userListFlow.collectAsStateWithLifecycle()
            val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
            val listState = rememberLazyListState()

            // observe list scrolling
            val reachedBottom: Boolean by remember {
                derivedStateOf {
                    val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                    val isReachBottom = lastVisibleItem?.index.safe() != 0 && (lastVisibleItem?.index.safe() >= listState.layoutInfo.totalItemsCount - 2)
                    // only set value again if
                    if (!isReachBottom) {
                        canTriggerLoadMore = true
                    }
                    isReachBottom
                }
            }

            if (reachedBottom && canTriggerLoadMore) {
                canTriggerLoadMore = false
                viewModel.loadMore()
            }
            UserList(
                userList,
                innerPadding,
                listState,
                isLoadingMore
            )
        }
    }
}

@Composable
fun UserList(userList: List<UIUser>, paddingValues: PaddingValues, listState: LazyListState, isLoadingMore: Boolean) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        state = listState
    ) {
        items(userList) { user ->
            UserRow(user = user)
        }

        if (isLoadingMore) {
            item {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserRow(user: UIUser) {
    val context = LocalContext.current
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        onClick = {
            context.startActivity(
                Intent(
                    context,
                    UserDetailActivity::class.java
                ).apply {
                    putExtra(UserDetailActivity.EXTRA_USER_NAME, user.login)
                }
            )
        }
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            GlideImage(
                model = user.avatar,
                contentDescription = stringResource(R.string.user_avatar),
                modifier = Modifier
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xfff7f7f7),
                            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                        )
                    }
                    .size(120.dp)
                    .clip(CircleShape),
                transition = CrossFade
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = user.login.safe(), fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(Modifier.height(10.dp))
                Text(
                    text = user.url.safe(),
                    textDecoration = TextDecoration.Underline,
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(user.url)
                            )
                        )
                    })
            }
        }
    }
}