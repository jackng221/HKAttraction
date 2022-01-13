package shape.computing.hkattraction

import org.junit.Test

import org.junit.Assert.*

class MapActivityTest {

    class FileNameReplace{
        private fun createFileName(location: String):String{
            return location?.replace("[^A-Za-z0-9]".toRegex(), " ")?.trim()?.replace("\\s+".toRegex(), "_") + "_custom_"
        }
        @Test
        fun case1(){
            val location = "test"
            val expectedName = "test_custom_"
            assertEquals(expectedName, createFileName(location))
        }
        @Test
        fun case2(){
            val location = " test location name "
            val expectedName = "test_location_name_custom_"
            assertEquals(expectedName, createFileName(location))
        }
        @Test
        fun case3(){
            val location = "test    location    name"
            val expectedName = "test_location_name_custom_"
            assertEquals(expectedName, createFileName(location))
        }
        @Test
        fun case4(){
            val location = "test    location    name"
            val expectedName = "test_location_name_custom_"
            assertEquals(expectedName, createFileName(location))
        }
        @Test
        fun case5(){
            val location = "(test) @location@ =name="
            val expectedName = "test_location_name_custom_"
            assertEquals(expectedName, createFileName(location))
        }
        @Test
        fun case6(){
            val location = ""
            val expectedName = "_custom_"
            assertEquals(expectedName, createFileName(location))
        }
        // required to ensure location is not null
    }
}