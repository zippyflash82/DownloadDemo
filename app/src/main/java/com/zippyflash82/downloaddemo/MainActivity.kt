package com.zippyflash82.downloaddemo

import android.location.LocationManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.blankj.utilcode.util.PathUtils
import com.danimahardhika.cafebar.CafeBar
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.android.material.snackbar.Snackbar
import com.zippyflash82.downloaddemo.utils.showSnackbar
import com.zippyflash82.downloaddemo.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Duration


class MainActivity : AppCompatActivity() {

    private val path = PathUtils.getExternalDownloadsPath()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PRDownloader.initialize(applicationContext)
        btn_start.setOnClickListener {


            when {
                (TextUtils.isEmpty(et_url.text.toString())) -> {
                    showSnackbar(this,"please provide correct url")
                  //showToast("Please Provide Correct Url")
                }

                (URLUtil.isValidUrl(et_url.text.toString())) -> {
                    url = et_url.text.toString()
                    val fileName = url.substring(url.lastIndexOf('/') + 1, url.length)
                    download(et_url.text.toString(), fileName)
                }
            }
        }
        btn_stop.setOnClickListener {
            PRDownloader.pause(downloadId!!)
        }
        btn_resume.setOnClickListener {
            PRDownloader.resume(downloadId!!)
        }
    }

    private fun download(url: String, fileName: String) {
        downloadId = PRDownloader.download(url, path, fileName)
            .build()
            .setOnStartOrResumeListener {
               showToast("Download Started")
                btn_start.isEnabled = false
                btn_stop.isEnabled = true
                btn_resume.isEnabled = true
            }
            .setOnPauseListener {
               showToast("Download Paused")
            }
            .setOnProgressListener {
                var progressPercent = it.currentBytes * 100 / it.totalBytes
                tv_progress.text = "Download Progress: $progressPercent %"
                progress_bar.progress = progressPercent.toInt()
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    showToast("File Downloaded")
                    btn_resume.isEnabled = false
                    btn_stop.isEnabled = false
                    btn_start.isEnabled = true
                }

                override fun onError(error: Error?) {
                    Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                    println("Download Error: $error")
                }
            })
    }

    companion object {
        var downloadId: Int? = null
        var url: String = ""
    }

}