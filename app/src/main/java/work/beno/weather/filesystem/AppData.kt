package work.beno.weather.filesystem

import android.content.Context
import android.util.Log
import work.beno.weather.helpers.Config
import java.io.*
import java.lang.Exception
import java.lang.NullPointerException

/** Object that handling load and save*/
class AppData(applicationContext: Context) {
    val ctx = applicationContext
    /** loading json object
     * return empty String if error*/
    fun load(filename: String): String {
        val data = ""
        try {
            val fileInputStream: FileInputStream? = ctx.openFileInput(filename)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String?
            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }
            return stringBuilder.toString()
        }catch (e:NullPointerException){
            Log.e("AppData", "can not load "+filename+" file")
        }catch (e:FileNotFoundException){
            Log.e("AppData", filename+" file not found")
        }
        return data
    }
    /** Saving json object
     * return 0 if no error*/
    fun save(jsonObject: String, fileName: String): Int {
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = ctx.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(jsonObject.toByteArray())
        } catch (e: FileNotFoundException){
            e.printStackTrace()
            Log.e("AppData", fileName+" not found for save")
            return 1
        }catch (e: NumberFormatException){
            Log.e("AppData", fileName+" wrong number format for save")
            e.printStackTrace()
            return 1
        }catch (e: IOException){
            Log.e("AppData", fileName+" can't be saved")
            e.printStackTrace()
            return 1
        }catch (e: Exception){
            e.printStackTrace()
            Log.e("AppData", fileName+" error during save")
            return 1
        }
        return 0
    }
    /**takes config as is and save into JSON
     * returns 0 if no errors*/
    fun save(config: Config, fileName: String): Int {
        val jsonObject = "{\"timestamp\":"+config.timestamp+"}"
        Log.d("json", jsonObject)
        return save(jsonObject, fileName)
    }

}
