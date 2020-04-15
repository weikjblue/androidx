/*
 * Copyright 2020 The Android Open Source Project
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
package androidx.ui.androidview.demos

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.Composition
import androidx.compose.Recomposer
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Box
import androidx.ui.foundation.HorizontalScroller
import androidx.ui.foundation.drawBackground
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.toArgb
import androidx.ui.layout.fillMaxHeight
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredWidth
import androidx.ui.unit.dp

open class ComposeScrollInAndroidScrollDifferentOrientation : ComponentActivity() {

    private lateinit var composition: Composition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compose_in_android_scroll)

        findViewById<View>(R.id.container).setBackgroundColor(Color.DarkGray.toArgb())

        val container = findViewById<ViewGroup>(R.id.container)
        composition = container.setContent(Recomposer.current()) {
            HorizontalScroller(
                modifier = Modifier
                    .padding(48.dp)
                    .drawBackground(Color.Gray, RectangleShape)
                    .fillMaxWidth()
                    .preferredHeight(456.dp)
            ) {
                Box(
                    Modifier
                        .padding(48.dp)
                        .drawBackground(Color.LightGray, RectangleShape)
                        .preferredWidth(360.dp)
                        .fillMaxHeight()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        composition.dispose()
    }
}