package com.makki.languageapp.views.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * @author Maksym.Popovych
 */
class RoundTransform(private val context: Context) : BitmapTransformation(){
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("RoundTransformation".toByteArray())
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, toTransform)
        val bitmap = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        roundedBitmapDrawable.isCircular = true
        roundedBitmapDrawable.setBounds(0, 0, outWidth, outHeight)
        roundedBitmapDrawable.draw(canvas)
        return bitmap
    }
}