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

package androidx.compose.foundation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.drawBehind

/**
 * Component that allow you to specify an area on the screen and perform canvas drawing on this
 * area. You MUST specify size with modifier, whether with exact sizes via [LayoutSize] modifier,
 * or relative to parent, via [LayoutSize.Fill], [ColumnScope.LayoutWeight], etc. If parent
 * wraps this child, only exact sizes must be specified.
 *
 * @sample androidx.compose.foundation.samples.CanvasSample
 *
 * @param modifier mandatory modifier to specify size strategy for this composable
 * @param onCanvas lambda that will be called to perform drawing. Note that this lambda will be
 * called during draw stage, you have no access to composition scope, meaning that [Composable]
 * function invocation inside it will result to runtime exception
 */
@Composable
fun Canvas(modifier: Modifier, onCanvas: DrawScope.() -> Unit) =
    Spacer(modifier.drawBehind(onCanvas))