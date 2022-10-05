package com.learning.weatherappclean.data.util

import com.squareup.moshi.Moshi


 internal class JsonConverter {

     inline fun <reified T> convertFromJson(jsonString: String?): T? {
         return try {
             jsonString.let {
                 val moshiAdapter = Moshi.Builder().build().adapter(T::class.java)
                 moshiAdapter.fromJson(it!!)
             }
         } catch (exception: Exception) {
             null
         }
     }



     inline fun <reified T> convertToJson(data: T): String? {
        return try {
                val moshiAdapter = Moshi.Builder().build().adapter(T::class.java)
                moshiAdapter.toJson(data)
        } catch (exception: Exception) {
            null
        }
    }

}
