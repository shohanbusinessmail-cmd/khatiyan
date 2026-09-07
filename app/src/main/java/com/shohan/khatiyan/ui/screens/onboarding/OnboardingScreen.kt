package com.shohan.khatiyan.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.R

@Composable
fun OnboardingScreen(
    onFinishOnboarding: (userName: String) -> Unit
) {
    var step by remember { mutableIntStateOf(0) }
    var userName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Skip Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (step < 3) {
                TextButton(onClick = { step = 3 }) {
                    Text("এড়িয়ে যান", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Pager Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (step) {
                0 -> WelcomePage()
                1 -> FeaturesPage()
                2 -> PrivacyPage()
                3 -> SetupPage(userName = userName, onNameChange = { userName = it })
            }
        }

        // Page Indicator & Bottom Buttons
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (step == index) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (step == index) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            Button(
                onClick = {
                    if (step < 3) {
                        step++
                    } else {
                        onFinishOnboarding(if (userName.isBlank()) "ব্যবহারকারী" else userName)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = if (step == 3) "শুরু করুন" else "পরবর্তী",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun WelcomePage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_khatiyan),
            contentDescription = "Khatiyan Logo",
            modifier = Modifier.size(140.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "খতিয়ান",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.app_tagline),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "আপনার জীবনের সব দোকানের বাকী, ব্যাংক লোন, কিস্তি, ব্যক্তিগত ধার এবং আয়-ব্যয়ের হিসাব এক অ্যাপেই রাখুন নিরাপদে।",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FeaturesPage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "কেন খতিয়ান ব্যবহার করবেন?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        val features = listOf(
            "• দোকানের বাকী ও কেনাকাটার হিসাব",
            "• ব্যাংক ও এনজিও লোন ম্যানেজমেন্ট",
            "• পণ্যের ইএমআই / কিস্তির ট্র্যাকিং",
            "• বন্ধু ও আত্মীয়দের সাথে ধার দেনা",
            "• দৈনিক আয় ও ব্যয়ের অটোমেটিক খতিয়ান"
        )

        features.forEach { feature ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(14.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PrivacyPage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "১০০% গোপনীয়তা ও সুরক্ষার নিশ্চয়তা",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "আপনার সব হিসাব কেবল আপনার ফোনেই সংরক্ষিত থাকবে। কোনো ইন্টারনেট সংযোগ বা ক্লাউড সার্ভারে আপনার ডেটা পাঠানো হয় না।",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "✓ সম্পূর্ণ ফ্রি ও বিজ্ঞাপনমুক্ত", fontWeight = FontWeight.Bold)
                Text(text = "✓ অফলাইন-ফার্স্ট ডিজাইন")
                Text(text = "✓ সহজ JSON/CSV ব্যাকআপ ও পিডিএফ সাপোর্ট")
            }
        }
    }
}

@Composable
private fun SetupPage(userName: String, onNameChange: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "প্রাথমিক সেটআপ",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = onNameChange,
            label = { Text("আপনার নাম লিখুন (ঐচ্ছিক)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "ডিফল্ট মুদ্রা:", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "বাংলাদেশি টাকা (৳)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
