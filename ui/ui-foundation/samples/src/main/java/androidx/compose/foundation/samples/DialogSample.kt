/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.foundation.samples

import androidx.annotation.Sampled
import androidx.compose.runtime.Composable
import androidx.compose.runtime.state
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Box
import androidx.compose.foundation.Dialog
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.ui.unit.dp

@Sampled
@Composable
fun DialogSample() {
    val openDialog = state { true }
    val dialogWidth = 200.dp
    val dialogHeight = 50.dp

    if (openDialog.value) {
        Dialog(onCloseRequest = { openDialog.value = false }) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Box(Modifier.preferredSize(dialogWidth, dialogHeight), backgroundColor = Color.White)
        }
    }
}