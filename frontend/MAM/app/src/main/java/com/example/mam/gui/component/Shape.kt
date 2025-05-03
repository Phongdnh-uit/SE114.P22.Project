import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class CustomShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val cornerRadius = with(density) { 10.dp.toPx() } // Bo góc
            val curveHeight = with(density) { 90.dp.toPx() } // Chiều cao phần cong phía trên

            // Vẽ phần cong phía trên
            moveTo(0f, curveHeight)
            cubicTo(
                x1 = size.width / 25, y1 = with(density) { (-10).dp.toPx() }, // Điều chỉnh điểm kiểm soát 1
                x2 = size.width * 24 / 25, y2 = with(density) { (-10).dp.toPx() }, // Điều chỉnh điểm kiểm soát 2
                x3 = size.width, y3 = curveHeight // Điểm cuối
            )

            // Vẽ phần bên dưới
            lineTo(size.width, size.height - cornerRadius)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    size.width - cornerRadius,
                    size.height - cornerRadius,
                    size.width,
                    size.height
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(cornerRadius, size.height)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    0f,
                    size.height - cornerRadius,
                    cornerRadius,
                    size.height
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }

        return Outline.Generic(path)
    }
}
