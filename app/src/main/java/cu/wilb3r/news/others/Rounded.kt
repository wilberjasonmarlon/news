package cu.wilb3r.news.others

import android.content.Context
import android.graphics.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import java.nio.charset.Charset
import java.security.MessageDigest


class RoundedCornersTransformation @JvmOverloads constructor(private val mBitmapPool: BitmapPool, private val mRadius: Float, margin: Int,
                                                             cornerType: CornerType = CornerType.DIAGONAL_FROM_TOP_LEFT) : Transformation<Bitmap?> {
    private val ID = "com.bumptech.glide.transformations.FillSpace"
    private val ID_BYTES: ByteArray = ID.toByteArray(Charset.forName("UTF-8"))
    enum class CornerType {
        ALL, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, TOP, BOTTOM, LEFT, RIGHT, OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT, DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
    }

    private val mDiameter: Int
    private val mMargin: Int
    private val mCornerType: CornerType

    constructor(context: Context?, radius: Float, margin: Int) : this(context, radius, margin, CornerType.ALL) {}
    constructor(context: Context?, radius: Float, margin: Int,
                cornerType: CornerType?) : this(Glide.get(context!!).bitmapPool, radius, margin, cornerType!!) {
    }

    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        val right = width - mMargin
        val bottom = height - mMargin
        when (mCornerType) {
            CornerType.ALL -> canvas.drawRoundRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right, bottom), mRadius, mRadius, paint)
            CornerType.TOP_LEFT -> drawTopLeftRoundRect(canvas, paint, right, bottom)
            CornerType.TOP_RIGHT -> drawTopRightRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM_LEFT -> drawBottomLeftRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM_RIGHT -> drawBottomRightRoundRect(canvas, paint, right, bottom)
            CornerType.TOP -> drawTopRoundRect(canvas, paint, right, bottom)
            CornerType.BOTTOM -> drawBottomRoundRect(canvas, paint, right, bottom)
            CornerType.LEFT -> drawLeftRoundRect(canvas, paint, right, bottom)
            CornerType.RIGHT -> drawRightRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_TOP_LEFT -> drawOtherTopLeftRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_TOP_RIGHT -> drawOtherTopRightRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_BOTTOM_LEFT -> drawOtherBottomLeftRoundRect(canvas, paint, right, bottom)
            CornerType.OTHER_BOTTOM_RIGHT -> drawOtherBottomRightRoundRect(canvas, paint, right, bottom)
            CornerType.DIAGONAL_FROM_TOP_LEFT -> drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom)
            CornerType.DIAGONAL_FROM_TOP_RIGHT -> drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom)
            else -> canvas.drawRoundRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right, bottom), mRadius, mRadius, paint)
        }
    }

    private fun drawTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), (mMargin + mDiameter).toFloat()),
                mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin.toFloat(), (mMargin + mRadius).toFloat(), (mMargin + mRadius).toFloat(), bottom), paint)
        canvas.drawRect(RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom), paint)
    }

    private fun drawTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(right - mDiameter, mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()), mRadius,
                mRadius, paint)
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom), paint)
        canvas.drawRect(RectF(right - mRadius, (mMargin + mRadius).toFloat(), right, bottom), paint)
    }

    private fun drawBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin.toFloat(), bottom - mDiameter, (mMargin + mDiameter).toFloat(), bottom),
                mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), bottom - mRadius), paint)
        canvas.drawRect(RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom), paint)
    }

    private fun drawBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius,
                mRadius, paint)
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom), paint)
        canvas.drawRect(RectF(right - mRadius, mMargin.toFloat(), right, bottom - mRadius), paint)
    }

    private fun drawTopRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()), mRadius, mRadius,
                paint)
        canvas.drawRect(RectF(mMargin.toFloat(), (mMargin + mRadius).toFloat(), right, bottom), paint)
    }

    private fun drawBottomRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin.toFloat(), bottom - mDiameter, right, bottom), mRadius, mRadius,
                paint)
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right, bottom - mRadius), paint)
    }

    private fun drawLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), bottom), mRadius, mRadius,
                paint)
        canvas.drawRect(RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom), paint)
    }

    private fun drawRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(right - mDiameter, mMargin.toFloat(), right, bottom), mRadius, mRadius,
                paint)
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom), paint)
    }

    private fun drawOtherTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin.toFloat(), bottom - mDiameter, right, bottom), mRadius, mRadius,
                paint)
        canvas.drawRoundRect(RectF(right - mDiameter, mMargin.toFloat(), right, bottom), mRadius, mRadius,
                paint)
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom - mRadius), paint)
    }

    private fun drawOtherTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), bottom), mRadius, mRadius,
                paint)
        canvas.drawRoundRect(RectF(mMargin.toFloat(), bottom - mDiameter, right, bottom), mRadius, mRadius,
                paint)
        canvas.drawRect(RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom - mRadius), paint)
    }

    private fun drawOtherBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()), mRadius, mRadius,
                paint)
        canvas.drawRoundRect(RectF(right - mDiameter, mMargin.toFloat(), right, bottom), mRadius, mRadius,
                paint)
        canvas.drawRect(RectF(mMargin.toFloat(), (mMargin + mRadius).toFloat(), right - mRadius, bottom), paint)
    }

    private fun drawOtherBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float,
                                              bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()), mRadius, mRadius,
                paint)
        canvas.drawRoundRect(RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), bottom), mRadius, mRadius,
                paint)
        canvas.drawRect(RectF((mMargin + mRadius).toFloat(), (mMargin + mRadius).toFloat(), right, bottom), paint)
    }

    private fun drawDiagonalFromTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float,
                                                 bottom: Float) {
        canvas.drawRoundRect(RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), (mMargin + mDiameter).toFloat()),
                mRadius, mRadius, paint)
        canvas.drawRoundRect(RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius,
                mRadius, paint)
        canvas.drawRect(RectF(mMargin.toFloat(), (mMargin + mRadius).toFloat(), right - mDiameter, bottom), paint)
        canvas.drawRect(RectF((mMargin + mDiameter).toFloat(), mMargin.toFloat(), right, bottom - mRadius), paint)
    }

    private fun drawDiagonalFromTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float,
                                                  bottom: Float) {
        canvas.drawRoundRect(RectF(right - mDiameter, mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()), mRadius,
                mRadius, paint)
        canvas.drawRoundRect(RectF(mMargin.toFloat(), bottom - mDiameter, (mMargin + mDiameter).toFloat(), bottom),
                mRadius, mRadius, paint)
        canvas.drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom - mRadius), paint)
        canvas.drawRect(RectF((mMargin + mRadius).toFloat(), (mMargin + mRadius).toFloat(), right, bottom), paint)
    }

    val id: String
        get() = ("RoundedTransformation(radius=" + mRadius + ", margin=" + mMargin + ", diameter="
                + mDiameter + ", cornerType=" + mCornerType.name + ")")

    init {
        mDiameter = (mRadius * 2).toInt()
        mMargin = margin
        mCornerType = cornerType
    }

    override fun transform(context: Context, resource: com.bumptech.glide.load.engine.Resource<Bitmap?>, outWidth: Int, outHeight: Int):
            BitmapResource {
        val source: Bitmap = resource.get()
        val width = source.height
        val height = source.height
        var bitmap: Bitmap? = mBitmapPool[width, height, Bitmap.Config.ARGB_8888]
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bitmap!!)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
        return BitmapResource.obtain(bitmap, mBitmapPool)!!
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }
}