package com.example.github_user.common

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.github_user.R
import com.example.github_user.ui.theme.Github_userTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseActivity<T : BaseViewModel> : ComponentActivity() {

    abstract val viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorDataFlow.collectLatest { exception ->
                    when (exception) {
                        is SocketTimeoutException, is SocketException, is UnknownHostException -> {
                            Toast.makeText(
                                this@BaseActivity,
                                getString(R.string.network_error), Toast.LENGTH_LONG
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                this@BaseActivity,
                                getString(R.string.something_went_wrong), Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    protected fun showContentWithDefaultHeader(
        title: String,
        content: @Composable (innerPadding: PaddingValues) -> Unit
    ) {
        enableEdgeToEdge()
        setContent {
            Github_userTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = title,
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.action_back)
                                    )
                                }
                            },
                        )
                    },
                ) { innerPadding ->
                    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
                    if (uiState == BaseViewModel.STATE_LOADING) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(64.dp))
                        }
                    } else {
                        content(innerPadding)
                    }

                }
            }
        }
    }

}