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

package androidx.ui.layout.test

import androidx.compose.Composable
import androidx.compose.emptyContent
import androidx.test.filters.SmallTest
import androidx.ui.core.Layout
import androidx.ui.core.LayoutCoordinates
import androidx.ui.core.Modifier
import androidx.ui.core.Ref
import androidx.ui.core.constrainHeight
import androidx.ui.core.constrainWidth
import androidx.ui.core.onChildPositioned
import androidx.ui.geometry.Offset
import androidx.ui.layout.DpConstraints
import androidx.ui.layout.ExperimentalLayout
import androidx.ui.layout.Stack
import androidx.ui.layout.IntrinsicSize
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredWidth
import androidx.ui.unit.Dp
import androidx.ui.unit.IntSize
import androidx.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SmallTest
@RunWith(JUnit4::class)
@OptIn(ExperimentalLayout::class)
class IntrinsicTest : LayoutTest() {
    @Test
    fun testMinIntrinsicWidth() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val minIntrinsicWidthSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack(modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                    minIntrinsicWidthSize.value = coordinates.size
                    positionedLatch.countDown()
                }
            ) {
                    FixedIntrinsicsBox(
                        Modifier.preferredWidth(IntrinsicSize.Min).saveLayoutInfo(
                            size = childSize,
                            position = childPosition,
                            positionedLatch = positionedLatch
                        ),
                        10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                    )
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(10.dp.toIntPx(), 50.dp.toIntPx()), minIntrinsicWidthSize.value)
        assertEquals(IntSize(10.dp.toIntPx(), 50.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMinIntrinsicHeight() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val minIntrinsicHeightSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack(modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                    minIntrinsicHeightSize.value = coordinates.size
                    positionedLatch.countDown()
                }
            ) {
                FixedIntrinsicsBox(
                    Modifier.preferredHeight(IntrinsicSize.Min).saveLayoutInfo(
                        size = childSize,
                        position = childPosition,
                        positionedLatch = positionedLatch
                    ),
                    10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                )
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(20.dp.toIntPx(), 40.dp.toIntPx()), minIntrinsicHeightSize.value)
        assertEquals(IntSize(20.dp.toIntPx(), 40.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMaxIntrinsicWidth() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val maxIntrinsicWidthSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack(modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                    maxIntrinsicWidthSize.value = coordinates.size
                    positionedLatch.countDown()
                }
            ) {
                FixedIntrinsicsBox(
                    Modifier.preferredWidth(IntrinsicSize.Max).saveLayoutInfo(
                        size = childSize,
                        position = childPosition,
                        positionedLatch = positionedLatch
                    ),
                    10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                )
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(30.dp.toIntPx(), 50.dp.toIntPx()), maxIntrinsicWidthSize.value)
        assertEquals(IntSize(30.dp.toIntPx(), 50.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMaxIntrinsicHeight() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val maxIntrinsicHeightSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack(modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                    maxIntrinsicHeightSize.value = coordinates.size
                    positionedLatch.countDown()
                }
            ) {
                    FixedIntrinsicsBox(
                        Modifier.preferredHeight(IntrinsicSize.Max).saveLayoutInfo(
                            size = childSize,
                            position = childPosition,
                            positionedLatch = positionedLatch
                        ),
                        10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                    )
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(20.dp.toIntPx(), 60.dp.toIntPx()), maxIntrinsicHeightSize.value)
        assertEquals(IntSize(20.dp.toIntPx(), 60.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMinIntrinsicWidth_respectsIncomingMaxConstraints() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val minIntrinsicWidthSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack {
                ConstrainedBox(
                    DpConstraints(maxWidth = 5.dp),
                    modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                        minIntrinsicWidthSize.value = coordinates.size
                        positionedLatch.countDown()
                    }
                ) {
                    FixedIntrinsicsBox(
                        Modifier.preferredWidth(IntrinsicSize.Min).saveLayoutInfo(
                            size = childSize,
                            position = childPosition,
                            positionedLatch = positionedLatch
                        ),
                        10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                    )
                }
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(5.dp.toIntPx(), 50.dp.toIntPx()), minIntrinsicWidthSize.value)
        assertEquals(IntSize(5.dp.toIntPx(), 50.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMinIntrinsicWidth_respectsIncomingMinConstraints() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val minIntrinsicWidthSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack {
                ConstrainedBox(
                    DpConstraints(minWidth = 15.dp),
                    modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                        minIntrinsicWidthSize.value = coordinates.size
                        positionedLatch.countDown()
                    }
                ) {
                        FixedIntrinsicsBox(
                            Modifier.preferredWidth(IntrinsicSize.Min).saveLayoutInfo(
                                size = childSize,
                                position = childPosition,
                                positionedLatch = positionedLatch
                            ),
                            10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                        )
                }
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(15.dp.toIntPx(), 50.dp.toIntPx()), minIntrinsicWidthSize.value)
        assertEquals(IntSize(15.dp.toIntPx(), 50.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMinIntrinsicHeight_respectsMaxIncomingConstraints() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val minIntrinsicHeightSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack(modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                    minIntrinsicHeightSize.value = coordinates.size
                    positionedLatch.countDown()
                }
            ) {
                ConstrainedBox(DpConstraints(maxHeight = 35.dp)) {
                    FixedIntrinsicsBox(
                        Modifier.preferredHeight(IntrinsicSize.Min).saveLayoutInfo(
                            size = childSize,
                            position = childPosition,
                            positionedLatch = positionedLatch
                        ),
                        10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                    )
                }
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(20.dp.toIntPx(), 35.dp.toIntPx()), minIntrinsicHeightSize.value)
        assertEquals(IntSize(20.dp.toIntPx(), 35.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMinIntrinsicHeight_respectsMinIncomingConstraints() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val minIntrinsicHeightSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack(modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                    minIntrinsicHeightSize.value = coordinates.size
                    positionedLatch.countDown()
                }
            ) {
                ConstrainedBox(DpConstraints(minHeight = 45.dp)) {
                        FixedIntrinsicsBox(
                            Modifier.preferredHeight(IntrinsicSize.Min).saveLayoutInfo(
                                size = childSize,
                                position = childPosition,
                                positionedLatch = positionedLatch
                            ),
                            10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                        )
                }
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(20.dp.toIntPx(), 45.dp.toIntPx()), minIntrinsicHeightSize.value)
        assertEquals(IntSize(20.dp.toIntPx(), 45.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMaxIntrinsicWidth_respectsMaxIncomingConstraints() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val maxIntrinsicWidthSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack(modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                    maxIntrinsicWidthSize.value = coordinates.size
                    positionedLatch.countDown()
                }
            ) {
                ConstrainedBox(DpConstraints(maxWidth = 25.dp)) {
                    FixedIntrinsicsBox(
                        Modifier.preferredWidth(IntrinsicSize.Max).saveLayoutInfo(
                            size = childSize,
                            position = childPosition,
                            positionedLatch = positionedLatch
                        ),
                        10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                    )
                }
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(25.dp.toIntPx(), 50.dp.toIntPx()), maxIntrinsicWidthSize.value)
        assertEquals(IntSize(25.dp.toIntPx(), 50.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMaxIntrinsicWidth_respectsMinIncomingConstraints() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val maxIntrinsicWidthSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack(modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                    maxIntrinsicWidthSize.value = coordinates.size
                    positionedLatch.countDown()
                }
            ) {
                ConstrainedBox(DpConstraints(minWidth = 35.dp)) {
                        FixedIntrinsicsBox(
                            Modifier.preferredWidth(IntrinsicSize.Max).saveLayoutInfo(
                                size = childSize,
                                position = childPosition,
                                positionedLatch = positionedLatch
                            ),
                            10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                        )
                }
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(35.dp.toIntPx(), 50.dp.toIntPx()), maxIntrinsicWidthSize.value)
        assertEquals(IntSize(35.dp.toIntPx(), 50.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMaxIntrinsicHeight_respectsMaxIncomingConstraints() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val maxIntrinsicHeightSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack(modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                    maxIntrinsicHeightSize.value = coordinates.size
                    positionedLatch.countDown()
                }
            ) {
                ConstrainedBox(DpConstraints(maxHeight = 55.dp)) {
                        FixedIntrinsicsBox(
                            Modifier.preferredHeight(IntrinsicSize.Max).saveLayoutInfo(
                                size = childSize,
                                position = childPosition,
                                positionedLatch = positionedLatch
                            ),
                            10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                        )
                }
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(20.dp.toIntPx(), 55.dp.toIntPx()), maxIntrinsicHeightSize.value)
        assertEquals(IntSize(20.dp.toIntPx(), 55.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMaxIntrinsicHeight_respectsMinIncomingConstraints() = with(density) {
        val positionedLatch = CountDownLatch(2)
        val maxIntrinsicHeightSize = Ref<IntSize>()
        val childSize = Ref<IntSize>()
        val childPosition = Ref<Offset>()
        show {
            Stack(modifier = Modifier.onChildPositioned { coordinates: LayoutCoordinates ->
                    maxIntrinsicHeightSize.value = coordinates.size
                    positionedLatch.countDown()
                }
            ) {
                ConstrainedBox(DpConstraints(minHeight = 65.dp)) {
                        FixedIntrinsicsBox(
                            Modifier.preferredHeight(IntrinsicSize.Max).saveLayoutInfo(
                                size = childSize,
                                position = childPosition,
                                positionedLatch = positionedLatch
                            ),
                            10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
                        )
                }
            }
        }
        assertTrue(positionedLatch.await(1, TimeUnit.SECONDS))

        assertEquals(IntSize(20.dp.toIntPx(), 65.dp.toIntPx()), maxIntrinsicHeightSize.value)
        assertEquals(IntSize(20.dp.toIntPx(), 65.dp.toIntPx()), childSize.value)
        assertEquals(Offset(0f, 0f), childPosition.value)
    }

    @Test
    fun testMinIntrinsicWidth_intrinsicMeasurements() = with(density) {
        testIntrinsics({
            FixedIntrinsicsBox(
                Modifier.preferredWidth(IntrinsicSize.Min), 10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
            )
        }) { minIntrinsicWidth, minIntrinsicHeight, maxIntrinsicWidth, maxIntrinsicHeight ->
            assertEquals(10.dp.toIntPx(), minIntrinsicWidth(0))
            assertEquals(40.dp.toIntPx(), minIntrinsicHeight(0))
            assertEquals(10.dp.toIntPx(), maxIntrinsicWidth(0))
            assertEquals(60.dp.toIntPx(), maxIntrinsicHeight(0))
        }
    }

    @Test
    fun testMinIntrinsicHeight_intrinsicMeasurements() = with(density) {
        testIntrinsics({
            FixedIntrinsicsBox(
                Modifier.preferredHeight(IntrinsicSize.Min),
                10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
            )
        }) { minIntrinsicWidth, minIntrinsicHeight, maxIntrinsicWidth, maxIntrinsicHeight ->
            assertEquals(10.dp.toIntPx(), minIntrinsicWidth(0))
            assertEquals(40.dp.toIntPx(), minIntrinsicHeight(0))
            assertEquals(30.dp.toIntPx(), maxIntrinsicWidth(0))
            assertEquals(40.dp.toIntPx(), maxIntrinsicHeight(0))
        }
    }

    @Test
    fun testMaxIntrinsicWidth_intrinsicMeasurements() = with(density) {
        testIntrinsics({
            FixedIntrinsicsBox(
                Modifier.preferredWidth(IntrinsicSize.Max), 10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
            )
        }) { minIntrinsicWidth, minIntrinsicHeight, maxIntrinsicWidth, maxIntrinsicHeight ->
            assertEquals(30.dp.toIntPx(), minIntrinsicWidth(0))
            assertEquals(40.dp.toIntPx(), minIntrinsicHeight(0))
            assertEquals(30.dp.toIntPx(), maxIntrinsicWidth(0))
            assertEquals(60.dp.toIntPx(), maxIntrinsicHeight(0))
        }
    }

    @Test
    fun testMaxIntrinsicHeight_intrinsicMeasurements() = with(density) {
        testIntrinsics({
            FixedIntrinsicsBox(
                Modifier.preferredHeight(IntrinsicSize.Max),
                10.dp, 20.dp, 30.dp, 40.dp, 50.dp, 60.dp
            )
        }) { minIntrinsicWidth, minIntrinsicHeight, maxIntrinsicWidth, maxIntrinsicHeight ->
            assertEquals(10.dp.toIntPx(), minIntrinsicWidth(0))
            assertEquals(60.dp.toIntPx(), minIntrinsicHeight(0))
            assertEquals(30.dp.toIntPx(), maxIntrinsicWidth(0))
            assertEquals(60.dp.toIntPx(), maxIntrinsicHeight(0))
        }
    }
}

@Composable
private fun FixedIntrinsicsBox(
    modifier: Modifier = Modifier,
    minIntrinsicWidth: Dp,
    width: Dp,
    maxIntrinsicWidth: Dp,
    minIntrinsicHeight: Dp,
    height: Dp,
    maxIntrinsicHeight: Dp
) {
    Layout(
        emptyContent(),
        minIntrinsicWidthMeasureBlock = { _, _ -> minIntrinsicWidth.toIntPx() },
        minIntrinsicHeightMeasureBlock = { _, _ -> minIntrinsicHeight.toIntPx() },
        maxIntrinsicWidthMeasureBlock = { _, _ -> maxIntrinsicWidth.toIntPx() },
        maxIntrinsicHeightMeasureBlock = { _, _ -> maxIntrinsicHeight.toIntPx() },
        modifier = modifier
    ) { _, constraints ->
        layout(
            constraints.constrainWidth(width.toIntPx()),
            constraints.constrainHeight(height.toIntPx())
        ) {}
    }
}