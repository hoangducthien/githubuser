package com.example.github_user.user.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.github_user.R
import com.example.github_user.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import safe

@AndroidEntryPoint
class UserDetailActivity : BaseActivity<UserDetailViewModel>() {

    companion object {
        const val EXTRA_USER_NAME = "USER_NAME"
    }

    override val viewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.getString(EXTRA_USER_NAME)?.let {
            viewModel.getUserDetail(it)
        }
        showContentWithDefaultHeader(getString(R.string.home_title)) { innerPadding ->
            val userDetail by viewModel.userDetailFlow.collectAsStateWithLifecycle()
            Column(modifier = Modifier.padding(innerPadding)) {
                UserDetailHeader(userDetail)
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    UserAttribute(
                        imageResource = R.drawable.baseline_group_24,
                        text1 = "${userDetail.followers}",
                        text2 = stringResource(
                            id = R.string.follower
                        )
                    )
                    Spacer(modifier = Modifier.width(60.dp))
                    UserAttribute(
                        imageResource = R.drawable.baseline_workspace_premium_24,
                        text1 = "${userDetail.following}",
                        text2 = stringResource(
                            id = R.string.following
                        )
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = stringResource(R.string.blog),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = userDetail.blog.safe())
            }
        }
    }
}

@Composable
fun UserAttribute(@DrawableRes imageResource: Int, text1: String, text2: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            modifier = Modifier
                .background(
                    color = Color.LightGray,
                    shape = CircleShape
                )
                .size(60.dp)
                .padding(15.dp), painter = painterResource(imageResource),
            contentDescription = stringResource(R.string.follower)
        )
        Spacer(Modifier.height(5.dp))
        Text(text = text1, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(5.dp))
        Text(text = text2)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserDetailHeader(user: UIUserDetail) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            GlideImage(
                model = user.avatar,
                contentDescription = "user avatar",
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
                Row {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = user.location.safe(),
                        color = Color.Gray,
                    )
                }

            }
        }
    }
}

