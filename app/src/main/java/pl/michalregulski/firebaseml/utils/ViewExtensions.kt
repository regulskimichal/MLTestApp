package pl.michalregulski.firebaseml.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams

fun View.applyNavigationMarginInsets() {
    if (layoutParams !is ViewGroup.MarginLayoutParams) return

    val margin = findMargin(layoutParams as ViewGroup.MarginLayoutParams)

    this.setOnApplyWindowInsetsListener { view, insets ->
        insets.consumeSystemWindowInsets()
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(
                margin.left,
                margin.top,
                margin.right,
                margin.bottom + insets.systemWindowInsetBottom
            )
        }
        insets
    }

    if (this.isAttachedToWindow) {
        this.requestApplyInsets()
    } else {
        this.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                view.removeOnAttachStateChangeListener(this)
                view.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(view: View) = Unit
        })
    }
}

private fun findMargin(viewGroup: ViewGroup.MarginLayoutParams) = Margin(
    viewGroup.leftMargin,
    viewGroup.topMargin,
    viewGroup.rightMargin,
    viewGroup.bottomMargin
)

data class Margin(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
)
