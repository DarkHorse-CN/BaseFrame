package com.darkhorse.baseframe.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import java.io.*

object BitmapUtils {

    /**
     * Bitmap转Base64
     *
     * @param bitmap
     * @return
     */
    fun bitmapToBase64(bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                baos.flush()
                baos.close()

                val bitmapBytes = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return result
    }

    /**
     * @param filePath
     * @return
     */
    fun fileToBase64(filePath: String, compressPercent: Int): String? {
        return bitmapToBase64(fileToBitmap(filePath, compressPercent))
    }


    fun fileToBitmap(filePath: String, compressPercent: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inSampleSize = compressPercent//直接设置它的压缩率，表示1/2
        return BitmapFactory.decodeFile(filePath, options)
    }

    /**
     * 保存Bitmap为文件
     *
     * @param bitmap
     * @param path
     */
    fun saveBitmapFile(bitmap: Bitmap, path: String) {
        val file = File(path)//将要保存图片的路径
        FileUtils.createOrExistsFile(file)
        try {
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * Base64转Bitmap
     *
     * @param base64Data
     * @return
     */
    fun base64ToBitmap(base64Data: String): Bitmap {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * 图片质量压缩
     * @param bitmap
     * @param many 百分比
     * @return
     */
    fun compressBitmap(bitmap: Bitmap, many: Float): Bitmap? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, many.toInt() * 100, baos)
        val isBm = ByteArrayInputStream(baos.toByteArray())
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    /**
     * 按比例裁剪图片
     * @param bitmap 位图
     * @param wScale 裁剪宽 0~100%
     * @param hScale 裁剪高 0~100%
     * @return bitmap
     */
    fun cropBitmap(bitmap: Bitmap, wScale: Float, hScale: Float): Bitmap {
        val w = bitmap.width
        val h = bitmap.height

        val wh = (w * wScale).toInt()
        val hw = (h * hScale).toInt()

        val retX = (w * (1 - wScale) / 2).toInt()
        val retY = (h * (1 - hScale) / 2).toInt()

        return Bitmap.createBitmap(bitmap, retX, retY, wh, hw, null, false)
    }

    /**
     * 放大缩小图片
     * @param bitmap 位图
     * @param w 新的宽度
     * @param h 新的高度
     * @return Bitmap
     */
    fun zoomBitmap(bitmap: Bitmap, w: Int, h: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()
        val scaleWidht = w.toFloat() / width
        val scaleHeight = h.toFloat() / height
        matrix.postScale(scaleWidht, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

}
