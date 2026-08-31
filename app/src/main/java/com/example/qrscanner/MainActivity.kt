package com.example.qrscanner

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var resultCard: LinearLayout
    private lateinit var typeText: TextView
    private lateinit var timestampText: TextView
    private lateinit var resultText: TextView
    private lateinit var openButton: Button
    private lateinit var shareButton: Button
    private lateinit var copyButton: Button

    private val cameraPermissionRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultCard = findViewById(R.id.resultCard)
        typeText = findViewById(R.id.typeText)
        timestampText = findViewById(R.id.timestampText)
        resultText = findViewById(R.id.resultText)
        openButton = findViewById(R.id.openButton)
        shareButton = findViewById(R.id.shareButton)
        copyButton = findViewById(R.id.copyButton)
        val scanButton = findViewById<Button>(R.id.scanButton)

        openButton.setOnClickListener {
            val text = resultText.text.toString()
            if (isUrl(text)) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(text)))
            }
        }

        shareButton.setOnClickListener {
            val text = resultText.text.toString()
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }

        copyButton.setOnClickListener {
            val text = resultText.text.toString()
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("QR Result", text))
            Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show()
        }

        scanButton.setOnClickListener {
            checkCameraPermissionAndScan()
        }
    }

    private fun isUrl(text: String): Boolean {
        return text.startsWith("http://") || text.startsWith("https://")
    }

    private fun checkCameraPermissionAndScan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startScan()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraPermissionRequestCode
            )
        }
    }

    private fun startScan() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats("QR_CODE")
        integrator.setPrompt("Point your camera at the QR Code")
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
    }

    @Deprecated("Deprecated in Java, pero ito ang inaasahan ng ZXing library")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, R.string.scan_cancelled, Toast.LENGTH_SHORT).show()
            } else {
                val content = result.contents
                resultText.text = content
                typeText.text = if (isUrl(content)) getString(R.string.type_url) else getString(R.string.type_text)
                val sdf = SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault())
                timestampText.text = sdf.format(Date())
                openButton.visibility = if (isUrl(content)) android.view.View.VISIBLE else android.view.View.GONE
                resultCard.visibility = android.view.View.VISIBLE
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScan()
            } else {
                Toast.makeText(this, R.string.camera_permission_needed, Toast.LENGTH_LONG).show()
            }
        }
    }
}
