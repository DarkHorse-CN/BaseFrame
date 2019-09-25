package com.darkhorse.baseframe.mvvm

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Switch
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.darkhorse.baseframe.extension.clipCircle
import com.darkhorse.baseframe.extension.clipRoundRect
import com.darkhorse.baseframe.extension.loadImg
import com.darkhorse.baseframe.extension.setGradualBackground


/**
 * Description:
 * Created by DarkHorse on 2018/7/23.
 */
object BindingAdapter {

    @BindingAdapter("imgUrl", "placeholder", "error", requireAll = false)
    @JvmStatic
    fun loadImg(
        imageView: ImageView,
        imgUrl: String?,
        placeholder: Drawable? = null,
        error: Drawable? = null
    ) {
        when (imageView.scaleType) {
            ImageView.ScaleType.MATRIX -> imageView.loadImg(imgUrl, placeholder, error, null)
            ImageView.ScaleType.FIT_XY -> imageView.loadImg(imgUrl, placeholder, error, null)
            ImageView.ScaleType.FIT_START -> imageView.loadImg(imgUrl, placeholder, error, null)
            ImageView.ScaleType.FIT_END -> imageView.loadImg(imgUrl, placeholder, error, null)
            ImageView.ScaleType.CENTER -> imageView.loadImg(imgUrl, placeholder, error, null)
            ImageView.ScaleType.CENTER_CROP -> imageView.loadImg(
                imgUrl,
                placeholder,
                error,
                CenterCrop()
            )
            ImageView.ScaleType.CENTER_INSIDE -> imageView.loadImg(
                imgUrl,
                placeholder,
                error,
                CenterInside()
            )
            else -> imageView.loadImg(imgUrl, placeholder, error, CenterCrop())
        }
    }

    @BindingAdapter("isSelected")
    @JvmStatic
    fun isSelected(view: View, isSelected: Boolean) {
        view.isSelected = isSelected
    }

    @BindingAdapter("isChecked")
    @JvmStatic
    fun isChecked(view: Switch, isChecked: Boolean) {
        view.isChecked = isChecked
    }

    @BindingAdapter("radius")
    @JvmStatic
    fun clipRound(view: View, radius: Float) {
        view.clipRoundRect(radius)
    }

    @BindingAdapter("clipCircle")
    @JvmStatic
    fun clipCircle(view: View, clipCircle: Boolean) {
        view.clipCircle(clipCircle)
    }

    @BindingAdapter("gradualStart", "gradualEnd", "gradualOrientation", requireAll = false)
    @JvmStatic
    fun setGradualBackground(
        view: View,
        startColor: Int,
        endColor: Int,
        orientation: GradientDrawable.Orientation?
    ) {
        view.setGradualBackground(startColor, endColor)
    }

    @SuppressLint("SetJavaScriptEnabled")
    @BindingAdapter("webContent")
    @JvmStatic
    fun initWebView(webView: WebView, webContent: String?) {
        val settings = webView.settings
        settings.javaScriptEnabled = true;
        settings.setSupportZoom(false)
        settings.displayZoomControls = false;
        settings.defaultTextEncodingName = "UTF-8";
        settings.blockNetworkImage = false;

        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY; //取消滚动条白边效果
        webView.webChromeClient = WebChromeClient();
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                webView.loadUrl(
                    "javascript:(function(){" +
                            "var objs = document.getElementsByTagName('img'); " +
                            "for(var i=0;i<objs.length;i++)  " +
                            "{"
                            + "var img = objs[i];   " +
                            "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                            "}" +
                            "})()"
                )
                super.onPageFinished(view, url)
            }
        }
        webView.loadDataWithBaseURL(null, webContent, "text/html", "utf-8", null)
    }

    @SuppressLint("SetJavaScriptEnabled")
    @BindingAdapter("webUrl")
    @JvmStatic
    fun initWebView2(webView: WebView, webUrl: String?) {
        val settings = webView.settings
        settings.javaScriptEnabled = true;
        settings.setSupportZoom(false)
        settings.displayZoomControls = false;
        settings.defaultTextEncodingName = "UTF-8";
        settings.blockNetworkImage = false;

        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY; //取消滚动条白边效果
        webView.webChromeClient = WebChromeClient();
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                webView.loadUrl(
                    "javascript:(function(){" +
                            "var objs = document.getElementsByTagName('img'); " +
                            "for(var i=0;i<objs.length;i++)  " +
                            "{"
                            + "var img = objs[i];   " +
                            "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                            "}" +
                            "})()"
                )
                super.onPageFinished(view, url)
            }
        }
        webView.loadUrl(webUrl)
    }
}
