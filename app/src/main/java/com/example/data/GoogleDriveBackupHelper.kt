package com.example.data

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object GoogleDriveBackupHelper {
    private const val TAG = "GDriveBackup"
    private val client = OkHttpClient()
    
    // Fetch all records as a single structured JSON String
    fun serializeBackup(
        wallets: List<WalletEntity>,
        transactions: List<TransactionEntity>,
        budgets: List<BudgetEntity>
    ): String {
        val root = JSONObject()
        
        // Serialize Wallets
        val walletsArray = JSONArray()
        wallets.forEach { w ->
            val robj = JSONObject().apply {
                put("id", w.id)
                put("name", w.name)
                put("balance", w.balance)
                put("type", w.type)
            }
            walletsArray.put(robj)
        }
        root.put("wallets", walletsArray)

        // Serialize Transactions
        val txsArray = JSONArray()
        transactions.forEach { t ->
            val robj = JSONObject().apply {
                put("id", t.id)
                put("title", t.title)
                put("amount", t.amount)
                put("isIncome", t.isIncome)
                put("category", t.category)
                put("timestamp", t.timestamp)
                put("walletId", t.walletId)
                put("notes", t.notes)
            }
            txsArray.put(robj)
        }
        root.put("transactions", txsArray)

        // Serialize Budgets
        val budgetsArray = JSONArray()
        budgets.forEach { b ->
            val robj = JSONObject().apply {
                put("id", b.id)
                put("category", b.category)
                put("amount", b.amount)
            }
            budgetsArray.put(robj)
        }
        root.put("budgets", budgetsArray)

        return root.toString()
    }

    // Restore from parsed JSON backup payload
    fun deserializeBackup(
        jsonString: String
    ): Map<String, List<Any>>? {
        try {
            val root = JSONObject(jsonString)
            
            val wallets = mutableListOf<WalletEntity>()
            if (root.has("wallets")) {
                val array = root.getJSONArray("wallets")
                for (i in 0 until array.length()) {
                    val w = array.getJSONObject(i)
                    wallets.add(
                        WalletEntity(
                            id = w.optLong("id", 0),
                            name = w.getString("name"),
                            balance = w.getDouble("balance"),
                            type = w.optString("type", "CASH")
                        )
                    )
                }
            }

            val transactions = mutableListOf<TransactionEntity>()
            if (root.has("transactions")) {
                val array = root.getJSONArray("transactions")
                for (i in 0 until array.length()) {
                    val t = array.getJSONObject(i)
                    transactions.add(
                        TransactionEntity(
                            id = t.optLong("id", 0),
                            title = t.getString("title"),
                            amount = t.getDouble("amount"),
                            isIncome = t.getBoolean("isIncome"),
                            category = t.getString("category"),
                            timestamp = t.getLong("timestamp"),
                            walletId = t.getLong("walletId"),
                            notes = t.optString("notes", "")
                        )
                    )
                }
            }

            val budgets = mutableListOf<BudgetEntity>()
            if (root.has("budgets")) {
                val array = root.getJSONArray("budgets")
                for (i in 0 until array.length()) {
                    val b = array.getJSONObject(i)
                    budgets.add(
                        BudgetEntity(
                            id = b.optLong("id", 0),
                            category = b.getString("category"),
                            amount = b.getDouble("amount")
                        )
                    )
                }
            }

            return mapOf(
                "wallets" to wallets,
                "transactions" to transactions,
                "budgets" to budgets
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error deserializing backup content", e)
            return null
        }
    }

    // Try to locate existing backup file in Google Drive
    fun findBackupFileId(accessToken: String): String? {
        val url = "https://www.googleapis.com/drive/v3/files?q=name='hisab_nikash_backup.json' and trashed=false&spaces=drive"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()
            
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e(TAG, "Search request failed: ${response.code} ${response.message}")
                    return null
                }
                val bodyString = response.body?.string() ?: return null
                val rootJson = JSONObject(bodyString)
                val filesArray = rootJson.optJSONArray("files")
                if (filesArray != null && filesArray.length() > 0) {
                    val firstFile = filesArray.getJSONObject(0)
                    return if (firstFile.has("id")) firstFile.getString("id") else null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception searching for backup file", e)
        }
        return null
    }

    // Creates the initial backup file placeholder on drive and returns its fileID
    fun createBackupFilePlaceholder(accessToken: String): String? {
        val url = "https://www.googleapis.com/drive/v3/files"
        val builder = JSONObject().apply {
            put("name", "hisab_nikash_backup.json")
            put("mimeType", "application/json")
        }
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = builder.toString().toRequestBody(mediaType)
        
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e(TAG, "Failed to create placeholder: ${response.code} ${response.message}")
                    return null
                }
                val bodyStr = response.body?.string() ?: return null
                val responseJson = JSONObject(bodyStr)
                return if (responseJson.has("id")) responseJson.getString("id") else null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception creating metadata placeholder", e)
        }
        return null
    }

    // Upload/Update the backup file bytes
    fun uploadBackupBytes(accessToken: String, fileId: String, jsonContent: String): Boolean {
        val url = "https://www.googleapis.com/upload/drive/v3/files/$fileId?uploadType=media"
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonContent.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .patch(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Log.i(TAG, "Backup content successfully uploaded!")
                    return true
                } else {
                    Log.e(TAG, "Failed content upload: ${response.code} ${response.message}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception uploading backup content", e)
        }
        return false
    }

    // Downloads current backup content from Google Drive
    fun downloadBackupContent(accessToken: String, fileId: String): String? {
        val url = "https://www.googleapis.com/drive/v3/files/$fileId?alt=media"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    return response.body?.string()
                } else {
                    Log.e(TAG, "Get backup media content failed: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception downloading media contents", e)
        }
        return null
    }
}
