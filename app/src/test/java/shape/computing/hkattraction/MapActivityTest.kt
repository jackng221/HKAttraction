package shape.computing.hkattraction

import org.junit.Test

import org.junit.Assert.*

class MapActivityTest {

    //FileNameReplace: create photo file name from location name
    private fun createFileName(location: String): String {
        return location.replace("[^A-Za-z0-9]".toRegex(), " ").trim().replace("\\s+".toRegex(), "_") + "_custom_"
    }
    @Test
    fun oneWord() {
        val location = "test"
        val expectedName = "test_custom_"
        assertEquals(expectedName, createFileName(location))
    }
    @Test
    fun threeWordsTrim() {
        val location = " test location name "
        val expectedName = "test_location_name_custom_"
        assertEquals(expectedName, createFileName(location))
    }
    @Test
    fun threeWordsSpaces() {
        val location = "test    location    name"
        val expectedName = "test_location_name_custom_"
        assertEquals(expectedName, createFileName(location))
    }
    @Test
    fun threeWordsSpChars() {
        val location = "(test) @location@ =name="
        val expectedName = "test_location_name_custom_"
        assertEquals(expectedName, createFileName(location))
    }
    @Test
    fun numbers() {
        val location = "1234 5678"
        val expectedName = "1234_5678_custom_"
        assertEquals(expectedName, createFileName(location))
    }
    @Test
    fun onlySpChars() {
        val location = "!@#$%^&*()_+=-{}|][;':,./?<>"
        val expectedName = "_custom_"
        assertEquals(expectedName, createFileName(location))
    }
    @Test
    fun empty() {
        val location = ""
        val expectedName = "_custom_"
        assertEquals(expectedName, createFileName(location))
    }
}