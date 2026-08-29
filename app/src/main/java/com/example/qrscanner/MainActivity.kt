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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class MainActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var openLinkButton: Button
    private val cameraPermissionRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultText = findViewById(R.id.resultText)
        openLinkButton = findViewById(R.id.openLinkButton)
        val scanButton = findViewById<Button>(R.id.scanButton)

        resultText.setOnClickListener {
            val text = resultText.text.toString()
            if (text.isNotBlank() && text != getString(R.string.no_result_yet)) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("QR Result", text))
                Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show()
            }
        }

        openLinkButton.setOnClickListener {
            val text = resultText.text.toString()
            if (isUrl(text)) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
                startActivity(intent)
            }
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
        integrator.setPrompt("Itapat ang camera sa QR code")
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
                resultText.text = result.contents
                if (isUrl(result.contents)) {
                    openLinkButton.visibility = android.view.View.VISIBLE
                } else {
                    openLinkButton.visibility = android.view.View.GONE
                }
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
