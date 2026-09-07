package com.shohan.khatiyan.util

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shohan.khatiyan.data.model.BackupDataJson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object BackupExporter {

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    fun exportToJsonUri(context: Context, uri: Uri, backupData: BackupDataJson): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    gson.toJson(backupData, writer)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun importFromJsonUri(context: Context, uri: Uri): BackupDataJson? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    gson.fromJson(reader, BackupDataJson::class.java)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun exportToCsvUri(context: Context, uri: Uri, transactionsCsv: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(transactionsCsv)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
