package com.example.kthelloandroid

import android.graphics.BlurMaskFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kthelloandroid.ui.theme.KtHelloAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KtHelloAndroidTheme {
                HelloAndroidScreen()
            }
        }
    }
}

@Preview
@Composable
fun HelloAndroidScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF202020)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            ImageSvgWithAnimatedRotateAndDropShadow()
            Text(
                color = Color.White,
                fontWeight = FontWeight(700),
                fontSize = 36.sp,
                text = "Hello Android"
            )
        }
    }
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
fun ImageSvgWithAnimatedRotateAndDropShadow() {
    val infiniteTransRotate = rememberInfiniteTransition()

    // 順時針動畫
    val animeRotateCw by infiniteTransRotate.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // 逆時針動畫
    val animeRotateCcw by infiniteTransRotate.animateFloat(
        initialValue = 0f, targetValue = -360f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // 透明度變化動畫
    val infiniteTransAlpha = rememberInfiniteTransition()
    val animeAlpha by infiniteTransAlpha.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .scale(2f)
            .size(250.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.techc03),
            contentDescription = null,
            modifier = Modifier.graphicsLayer(rotationZ = animeRotateCw)
        )

        Image(
            painter = painterResource(R.drawable.techc02),
            contentDescription = null,
            modifier = Modifier.graphicsLayer(rotationZ = animeRotateCcw)
        )

        Image(
            painter = painterResource(R.drawable.techc01),
            contentDescription = null,
            modifier = Modifier
                .dropShadow(
                    shape = CircleShape,
                    color = Color(0xFF05AEE5).copy(alpha = animeAlpha),
                    blur = 10.dp
                )
                .graphicsLayer(rotationZ = animeRotateCw)
        )
    }
}

// Reference: https://medium.com/@kappdev/custom-drop-shadow-from-figma-in-jetpack-compose-for-any-shape-d20fccac4e20
fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp = 4.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp
) = this.drawBehind {

    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint()
    paint.color = color

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}