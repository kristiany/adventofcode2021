import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

fun main() {

    getAvailableRoomTests()

    isCompleteTests()


}

private fun isCompleteTests() {
    val a1 = arrayOf(
        11, 12, 13, 14,
        15, 16, 17, 18,
        19, 20, 21, 22,
        23, 24, 25, 26
    ).toIntArray()
    assertTrue(isComplete(a1, 4))
    val a2 = arrayOf(
        14, 13, 12, 14,
        17, 16, 18, 15,
        19, 22, 20, 21,
        24, 23, 26, 25
    ).toIntArray()
    assertTrue(isComplete(a2, 4))

    val a3 = arrayOf(
        17, 13, 12, 14,
        14, 16, 18, 15,
        19, 22, 20, 21,
        24, 23, 26, 25
    ).toIntArray()
    assertFalse(isComplete(a3, 4))

    val a4 = arrayOf(
        14, 13, 12, 25,
        17, 16, 18, 15,
        19, 22, 20, 21,
        24, 23, 26, 14
    ).toIntArray()
    assertFalse(isComplete(a4, 4))

    val a5 = arrayOf(
        0, 12, 13, 14,
        15, 16, 17, 18,
        19, 20, 21, 22,
        23, 24, 1, 26
    ).toIntArray()
    assertFalse(isComplete(a5, 4))
}

private fun getAvailableRoomTests() {
    val a1 = arrayOf(
        11, 12, 13, 14, // A
        15, 16, 17, 18, // B
        19, 20, 21, 22, // C
        23, 24, 25, 26
    ) // D
        .toIntArray()
    assertEquals(null, getAvailableRoom(a1, 11, 1, 'A'))
    assertEquals(null, getAvailableRoom(a1, 15, 1, 'B'))
    assertEquals(null, getAvailableRoom(a1, 19, 1, 'C'))
    assertEquals(null, getAvailableRoom(a1, 23, 1, 'D'))

    val a2 = arrayOf(
        0, 1, 13, 3, // D in A
        10, 9, 17, 18, // only B
        10, 9, 0, 22, // single C
        14, 24, 25, 26
    ) // one single free slot in D
        .toIntArray()
    assertEquals(null, getAvailableRoom(a2, 11, 1, 'A'))
    assertEquals(2 to 16, getAvailableRoom(a2, 15, 1, 'B'))
    assertEquals(3 to 21, getAvailableRoom(a2, 19, 1, 'C'))
    assertEquals(1 to 23, getAvailableRoom(a2, 23, 1, 'D'))

    val a3 = arrayOf(
        0, 1, 5, 3, // empty
        0, 1, 5, 3, // empty
        0, 1, 5, 3, // empty
        0, 1, 5, 3
    ) // empty
        .toIntArray()
    assertEquals(4 to 14, getAvailableRoom(a3, 11, 1, 'A'))
    assertEquals(4 to 18, getAvailableRoom(a3, 15, 1, 'B'))
    assertEquals(4 to 22, getAvailableRoom(a3, 19, 1, 'C'))
    assertEquals(4 to 26, getAvailableRoom(a3, 23, 1, 'D'))

    val a4 = arrayOf(
        1, 12, 13, 18, // last not correct type
        0, 16, 14, 17, // next to last not correct type
        23, 3, 21, 22, // second not correct type
        20, 24, 25, 26 // first not correct type
    ) //
        .toIntArray()
    assertEquals(null, getAvailableRoom(a4, 11, 1, 'A'))
    assertEquals(null, getAvailableRoom(a4, 15, 1, 'B'))
    assertEquals(null, getAvailableRoom(a4, 19, 1, 'C'))
    assertEquals(null, getAvailableRoom(a4, 23, 1, 'D'))
}