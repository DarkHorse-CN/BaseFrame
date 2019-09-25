package com.micropole.baseframe.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darkhorse.baseframe.utils.DisplayUtils

class GridLayoutDecoration(private val space: Int, private val hasBorder: Boolean = true, private val hasHeader: Boolean = false) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        var curPos = parent.getChildLayoutPosition(view)
        if (hasHeader) {
            if (curPos > 0) {
                curPos--
            } else {
                return
            }
        }

        val layoutManager = parent.layoutManager as GridLayoutManager
        val spanCount = layoutManager.spanCount

        val realSpace = DisplayUtils.dp2px(space)
        val margin = realSpace * (spanCount - 1) / spanCount

        when {
            curPos % spanCount == 0 -> {
                if (hasBorder) {
                    outRect.left = realSpace
                }
                outRect.right = margin
            }
            curPos % spanCount == spanCount - 1 -> {
                if (hasBorder) {
                    outRect.right = realSpace
                }
                outRect.left = margin
            }
            else -> {
                outRect.left = realSpace - margin
                outRect.right = 2 * margin - realSpace
            }
        }

        outRect.top = realSpace

    }
}
