package com.micropole.baseframe.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.darkhorse.baseframe.utils.DisplayUtils

class LinearLayoutDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val curPos = parent.getChildLayoutPosition(view)
        if (curPos == 0) {
            outRect.top = 0
        } else {
            outRect.top = DisplayUtils.dp2px(space)
        }
    }
}
