package com.shohan.khatiyan.ui.screens.backup

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.R
import com.shohan.khatiyan.data.model.BackupDataJson
import com.shohan.khatiyan.ui.components.ConfirmDialog
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel
import com.shohan.khatiyan.util.BackupExporter
import com.shohan.khatiyan.util.DateUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupRestoreScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPrefs by viewModel.userPreferences.collectAsState()

    var pendingRestoreData by remember { mutableStateOf<BackupDataJson?>(null) }
    var showConfirmRestore by remember { mutableStateOf(false) }

    // Save JSON Launcher
    val exportJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val backupData = viewModel.repository.generateBackupData()
                val success = BackupExporter.exportToJsonUri(context, uri, backupData)
                if (success) {
                    viewModel.userPreferencesRepository.updateLastBackupTime(System.currentTimeMillis())
                    Toast.makeText(context, context.getString(R.string.backup_success), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "ব্যাকআপ ব্যর্থ হয়েছে!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Open JSON Restore Launcher
    val importJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            val backupData = BackupExporter.importFromJsonUri(context, uri)
            if (backupData != null) {
                pendingRestoreData = backupData
                showConfirmRestore = true
            } else {
                Toast.makeText(context, "ফাইলটি সঠিক খতিয়ান ব্যাকআপ নয়!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Export CSV Launcher
    val exportCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val backupData = viewModel.repository.generateBackupData()
                val sb = StringBuilder()
                sb.append("Type,Module,Date,Title,Subtitle,Amount(Paisa),Note\n")
                backupData.transactions.forEach { t ->
                    sb.append("${t.type},${t.module},${t.date},\"${t.title}\",\"${t.subtitle}\",${t.amountPaisa},\"${t.note}\"\n")
                }
                val success = BackupExporter.exportToCsvUri(context, uri, sb.toString())
                if (success) {
                    Toast.makeText(context, "CSV সফলভাবে এক্সপোর্ট হয়েছে!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.backup_restore_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            // Backup Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "JSON ব্যাকআপ নিন",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.backup_json_desc),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (userPrefs.lastBackupTime > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "সর্বশেষ ব্যাকআপ: ${DateUtils.formatDate(userPrefs.lastBackupTime)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { exportJsonLauncher.launch("Khatiyan_Backup_${System.currentTimeMillis()}.json") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.btn_backup))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Restore Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ডেটা পুনরুদ্ধার (Restore)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "সংরক্ষিত JSON ফাইল নির্বাচন করে আগের সমস্ত হিসাব ফিরিয়ে আনুন।",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { importJsonLauncher.launch(arrayOf("application/json", "*/*")) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.btn_restore))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // CSV Export Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Excel / CSV এক্সপোর্ট",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.backup_csv_desc),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { exportCsvLauncher.launch("Khatiyan_Ledger_${System.currentTimeMillis()}.csv") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("CSV ফাইল ডাউনলোড করুন")
                        }
                    }
                }
            }
        }

        if (showConfirmRestore && pendingRestoreData != null) {
            val restoreData = pendingRestoreData!!
            ConfirmDialog(
                title = stringResource(R.string.restore_confirm_title),
                message = stringResource(R.string.restore_confirm_msg),
                onConfirm = {
                    showConfirmRestore = false
                    viewModel.restoreBackup(restoreData) { success ->
                        if (success) {
                            Toast.makeText(context, context.getString(R.string.restore_success), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "পুনরুদ্ধার ব্যর্থ হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onDismiss = { showConfirmRestore = false }
            )
        }
    }
}
