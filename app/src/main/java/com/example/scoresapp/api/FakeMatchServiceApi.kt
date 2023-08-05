package com.example.scoresapp.api

import android.content.Context
import com.example.scoresapp.api.responses.GetMatchesResponse
import com.example.scoresapp.constants.Constants
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.nio.charset.Charset
import javax.inject.Inject

class FakeMatchServiceApi @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi
): MatchServiceApi {

    override suspend fun getMatches(): GetMatchesResponse? = withContext(Dispatchers.IO) {
        val json: String
        val inputStream: InputStream = context.assets.open(Constants.RESPONSE_JSON_FILE_NAME)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = buffer.toString(Charset.defaultCharset())
        val adapter: JsonAdapter<GetMatchesResponse> = moshi.adapter(GetMatchesResponse::class.java)
        return@withContext adapter.fromJson(json)
    }
}