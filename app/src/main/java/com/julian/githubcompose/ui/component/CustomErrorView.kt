package com.julian.githubcompose.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.julian.githubcompose.R

@Composable
fun CustomErrorView(msgCode: String?, msgContent: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(R.drawable.ic_cancel),
            contentDescription = "Error View",
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally)
        )

        msgContent?.apply {
            Text(
                text = this, style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
            )
        }

        msgCode?.apply {
            Text(
                text = stringResource(id = R.string.http_error_code_title) + "($this)",
                textAlign = TextAlign.Center
            )
        }
    }
}