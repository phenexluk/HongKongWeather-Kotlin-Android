package com.hkweather.app.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface

object TemperatureMarkerFactory {
    private const val WIDTH = 112
    private const val HEIGHT = 56

    fun create(
        context: Context,
        temperature: Int,
        unit: String,
        highlighted: Boolean,
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val fillColor = colorForTemperature(temperature)
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fillColor
            style = Paint.Style.FILL
        }
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (highlighted) Color.parseColor("#1D9BF0") else Color.parseColor("#2F3336")
            style = Paint.Style.STROKE
            strokeWidth = if (highlighted) 3f else 2f
        }
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E7E9EA")
            textSize = 26f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
        }

        val rect = RectF(6f, 6f, WIDTH - 6f, HEIGHT - 6f)
        canvas.drawRoundRect(rect, 18f, 18f, bgPaint)
        canvas.drawRoundRect(rect, 18f, 18f, borderPaint)

        canvas.drawText(
            "$temperature°$unit",
            WIDTH / 2f,
            HEIGHT / 2f + 6f,
            textPaint,
        )

        return bitmap
    }

    fun createUserDot(): Bitmap {
        val size = 36
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val outer = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(90, 29, 155, 240)
            style = Paint.Style.FILL
        }
        val inner = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1D9BF0")
            style = Paint.Style.FILL
        }
        val border = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E7E9EA")
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 2f, outer)
        canvas.drawCircle(size / 2f, size / 2f, 8f, inner)
        canvas.drawCircle(size / 2f, size / 2f, 8f, border)
        return bitmap
    }

    private fun colorForTemperature(temp: Int): Int = when {
        temp <= 18 -> Color.parseColor("#272A2E")
        temp <= 24 -> Color.parseColor("#1E2126")
        temp <= 28 -> Color.parseColor("#16181C")
        temp <= 32 -> Color.parseColor("#2F3336")
        else -> Color.parseColor("#71767B")
    }
}
