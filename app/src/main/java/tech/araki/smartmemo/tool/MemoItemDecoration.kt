package tech.araki.smartmemo.tool

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tech.araki.smartmemo.R
import tech.araki.smartmemo.util.dp

class MemoItemDecoration : RecyclerView.ItemDecoration() {
    companion object {
        private val OFFSET_TOP_WIDTH = 5.dp
    }

    // 各Viewのオフセットを管理する
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        // 最初のViewHolderを除き、上部にオフセットを設ける
        if (position != 0)
            outRect.top = OFFSET_TOP_WIDTH
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        repeat(state.itemCount) { position ->
            val view = parent.getChildAt(position) ?: return  // 描画範囲外であればreturn

            // 作成したオフセットに長方形を描画してDividerを作成
            val color = Paint().apply {
                color = ContextCompat.getColor(view.context, R.color.list_border_color)
            }
            c.drawRect(
                0f,  // left
                view.y - OFFSET_TOP_WIDTH,  // top
                parent.width.toFloat(),  // right
                view.y,  // bottom
                color  // background color
            )
        }
    }
}