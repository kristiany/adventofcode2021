import kotlin.test.assertEquals

fun main() {
    val cur = Ranges(true, 1..3, 1..3, 1..3)
    val res1 = withNoOverlapsOf(cur,
        Ranges(true, 4..5, 1..3, 1..3))
    assertEquals(listOf(cur), res1)
    val res2 = withNoOverlapsOf(cur,
        Ranges(true, 1..2, 4..5, 1..3))
    assertEquals(listOf(cur), res2)
    val res3 = withNoOverlapsOf(cur,
        Ranges(true, 1..2, 2..3, 4..5))
    assertEquals(listOf(cur), res3)

    // x no overlap
    val resx1 = withNoOverlapsOf(cur,
        Ranges(true, 1..2, 1..3, 1..3))
    assertEquals(listOf(Ranges(true, 3..3, 1..3, 1..3)), resx1)
    val resx2 = withNoOverlapsOf(cur,
        Ranges(true, 2..3, 1..3, 1..3))
    assertEquals(listOf(Ranges(true, 1..1, 1..3, 1..3)), resx2)
    val resx3 = withNoOverlapsOf(cur,
        Ranges(true, 2..2, 1..3, 1..3))
    assertEquals(listOf(
        Ranges(true, 1..1, 1..3, 1..3),
        Ranges(true, 3..3, 1..3, 1..3)
    ), resx3)

    // y no overlap
    val resy1 = withNoOverlapsOf(cur,
        Ranges(true, 1..3, 1..2, 1..3))
    assertEquals(listOf(Ranges(true, 1..3, 3..3, 1..3)), resy1)
    val resy2 = withNoOverlapsOf(cur,
        Ranges(true, 1..3, 2..3, 1..3))
    assertEquals(listOf(Ranges(true, 1..3, 1..1, 1..3)), resy2)
    val resy3 = withNoOverlapsOf(cur,
        Ranges(true, 1..3, 2..2, 1..3))
    assertEquals(listOf(
        Ranges(true, 1..3, 1..1, 1..3),
        Ranges(true, 1..3, 3..3, 1..3)
    ), resy3)

    // z no overlap
    val resz1 = withNoOverlapsOf(cur,
        Ranges(true, 1..3, 1..3, 1..2))
    assertEquals(listOf(Ranges(true, 1..3, 1..3, 3..3)), resz1)
    val resz2 = withNoOverlapsOf(cur,
        Ranges(true, 1..3, 1..3, 2..3))
    assertEquals(listOf(Ranges(true, 1..3, 1..3, 1..1)), resz2)
    val resz3 = withNoOverlapsOf(cur,
        Ranges(true, 1..3, 1..3, 2..2))
    assertEquals(listOf(
        Ranges(true, 1..3, 1..3, 1..1),
        Ranges(true, 1..3, 1..3, 3..3)
    ), resz3)

    // xy no overlap
    val resxy1 = withNoOverlapsOf(cur,
        Ranges(true, -2..1, -2..2, 1..3))
    assertEquals(listOf(Ranges(true, 2..3, 1..3, 1..3),
                        Ranges(true, 1..1, 3..3, 1..3)), resxy1)
    val resxy2 = withNoOverlapsOf(cur,
        Ranges(true, 2..5, 3..6, 1..3))
    assertEquals(listOf(Ranges(true, 1..1, 1..3, 1..3),
                        Ranges(true, 2..3, 1..2, 1..3)), resxy2)
    val resxy3 = withNoOverlapsOf(cur,
        Ranges(true, 2..5, -3..2, 1..3))
    assertEquals(listOf(Ranges(true, 1..1, 1..3, 1..3),
                        Ranges(true, 2..3, 3..3, 1..3)), resxy3)
    val resxy4 = withNoOverlapsOf(cur,
        Ranges(true, -2..2, 2..5, 1..3))
    assertEquals(listOf(Ranges(true, 3..3, 1..3, 1..3),
                        Ranges(true, 1..2, 1..1, 1..3)), resxy4)

    // yz
    val resyz1 = withNoOverlapsOf(cur,
        Ranges(true, 1..3, -2..1, -2..2))
    assertEquals(listOf(Ranges(true, 1..3, 2..3, 1..3),
        Ranges(true, 1..3, 1..1, 3..3)), resyz1)
    val resyz2 = withNoOverlapsOf(cur,
        Ranges(true, 1..3, 2..5, 3..6))
    assertEquals(listOf(Ranges(true, 1..3, 1..1, 1..3),
        Ranges(true, 1..3, 2..3, 1..2)), resyz2)
    val resyz3 = withNoOverlapsOf(cur,
        Ranges(true, 1..3, 2..5, -3..2))
    assertEquals(listOf(Ranges(true, 1..3, 1..1, 1..3),
        Ranges(true, 1..3, 2..3, 3..3)), resyz3)
    val resyz4 = withNoOverlapsOf(cur,
        Ranges(true, 1..3, -2..2, 2..5))
    assertEquals(listOf(Ranges(true, 1..3, 3..3, 1..3),
        Ranges(true, 1..3, 1..2, 1..1)), resyz4)

    // xz
    val resxz1 = withNoOverlapsOf(cur,
        Ranges(true, -2..1, 1..3, -2..2))
    assertEquals(listOf(Ranges(true, 2..3, 1..3,1..3),
        Ranges(true, 1..1, 1..3,3..3)), resxz1)
    val resxz2 = withNoOverlapsOf(cur,
        Ranges(true, 2..5, 1..3,3..6))
    assertEquals(listOf(Ranges(true, 1..1, 1..3,1..3),
        Ranges(true, 2..3, 1..3,1..2)), resxz2)
    val resxz3 = withNoOverlapsOf(cur,
        Ranges(true, 2..5, 1..3,-3..2))
    assertEquals(listOf(Ranges(true, 1..1, 1..3,1..3),
        Ranges(true, 2..3, 1..3,3..3)), resxz3)
    val resxz4 = withNoOverlapsOf(cur,
        Ranges(true, -2..2, 1..3,2..5))
    assertEquals(listOf(Ranges(true, 3..3, 1..3,1..3),
        Ranges(true, 1..2, 1..3,1..1)), resxz4)

    // unlit
    val unlit1 = withNoOverlapsOf(Ranges(false, 1..3, 1..3, 1..3),
        Ranges(true, -2..1, -2..2, 1..3))
    assertEquals(listOf(Ranges(false, 2..3, 1..3, 1..3),
        Ranges(false, 1..1, 3..3, 1..3)), unlit1)
    val unlit2 = withNoOverlapsOf(Ranges(false, 1..3, 1..3, 1..3),
        Ranges(false, -2..1, -2..2, 1..3))
    assertEquals(listOf(Ranges(false, 2..3, 1..3, 1..3),
        Ranges(false, 1..1, 3..3, 1..3)), unlit2)
    val unlit3 = withNoOverlapsOf(cur,
        Ranges(false, -2..1, -2..2, 1..3))
    assertEquals(listOf(Ranges(true, 2..3, 1..3, 1..3),
        Ranges(true, 1..1, 3..3, 1..3)), unlit3)

    println("All good!")
}