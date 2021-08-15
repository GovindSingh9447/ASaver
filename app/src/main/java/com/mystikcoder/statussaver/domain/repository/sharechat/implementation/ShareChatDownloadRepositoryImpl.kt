package com.mystikcoder.statussaver.domain.repository.sharechat.implementation

import com.mystikcoder.statussaver.domain.model.response.DownloadRequestResponse
import com.mystikcoder.statussaver.domain.repository.sharechat.abstraction.ShareChatDownloadRepository
import org.jsoup.Jsoup

class ShareChatDownloadRepositoryImpl: ShareChatDownloadRepository {

    override suspend fun downloadShareChatFile(url: String): DownloadRequestResponse {

        kotlin.runCatching {
            val document = Jsoup.connect(url).get()

            val videoUrl = document.select("meta[property=\"og:video:secure_url\"]")
                .last()
                .attr("content")

            return if (videoUrl != null){
                DownloadRequestResponse(isSuccess = true , downloadLink = videoUrl)
            }else{
                val imageUrl = document.select("meta[property=\"og:image\"]")
                    .last()
                    .attr("content")

                if (imageUrl != null){
                    DownloadRequestResponse(isSuccess = true , downloadLink = imageUrl)
                } else{
                    DownloadRequestResponse(errorMessage = "No data found")
                }
            }
        }.getOrElse {
            return DownloadRequestResponse(errorMessage = it.message ?: "Something Went Wrong")
        }
    }
}