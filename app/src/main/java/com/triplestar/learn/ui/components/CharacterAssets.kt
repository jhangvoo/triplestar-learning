package com.triplestar.learn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.triplestar.learn.R

@Composable
fun AliBoyImage(
    size: Dp = 80.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clipToBounds()
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_character_companions),
            contentDescription = "Ali Boy Character",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .height(size)
                .width(size * 2)
                .offset(x = 0.dp)
        )
    }
}

@Composable
fun SaraGirlImage(
    size: Dp = 80.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clipToBounds()
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_character_companions),
            contentDescription = "Sara Girl Character",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .height(size)
                .width(size * 2)
                .offset(x = -size)
        )
    }
}
