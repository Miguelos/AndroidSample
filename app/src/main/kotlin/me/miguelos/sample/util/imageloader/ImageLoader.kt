package me.miguelos.sample.util.imageloader

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView


/**
 * Interface to be implemented to load images into the app
 */
interface ImageLoader {

    /**
     * Load an image
     * @param url image url
     *
     * @param imageView The target ImageView or similar to load the image into.
     */
    fun loadImage(imageView: ImageView?, url: String?)

    /**
     * Load an image
     * @param drawable drawable resource
     * @param imageView The target ImageView or similar to load the image into.
     */
    fun <T : ImageView> loadImage(imageView: T, drawable: Drawable)

    /**
     * Load an image
     * @param resId Resource Id
     * @param imageView The target ImageView or similar to load the image into.
     */
    fun <T : ImageView> loadImage(imageView: T, resId: Int)

    /**
     * Load an image
     * @param bitmap Bitmap of the image
     * @param imageView The target ImageView or similar to load the image into.
     */
    fun <T : ImageView> loadImage(imageView: T, bitmap: Bitmap)

    /**
     * Load an image with a shape of a circle
     * @param uri path to resource, url, ...
     * @param imageView The target ImageView or similar to load the image into.
     */
    fun <T : ImageView> loadCircleImage(imageView: T, url: String)
}
