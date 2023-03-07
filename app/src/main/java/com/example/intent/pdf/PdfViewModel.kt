package com.example.intent.pdf

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PdfViewModel @Inject constructor(): ViewModel() {

    fun generatePdf(context: Context) {
        // Create PDF Part
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(792, 1120, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        val paintRounded = Paint()
        canvas.drawRoundRect(100f, 300f, 120f, 100f, 0f, 0f, paintRounded)
        canvas.drawText("isim", 80f, 80f, paint)
        pdfDocument.finishPage(page)

        //Build File Part
        val file = File(context.getExternalFilesDir(null), "xyz.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF file generated successfully.", Toast.LENGTH_LONG).show()
            //everything is done

            //you can also go to the generated pdf file
            val fileProvider =
                FileProvider.getUriForFile(context, context.applicationInfo.packageName, file)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = fileProvider
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.startActivity(intent)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }
}