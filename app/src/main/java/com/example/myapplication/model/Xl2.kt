package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class xl2(
    val tasks: List<xl2task>,
    @SerialName("next_page_token") val nextPageToken: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("expires_in_ms") val expiresInMs: Int
)

@Serializable
data class xl2task(
    val kind: String,
    val id: String,
    val name: String,
    val type: String,
    @SerialName("user_id") val userId: String,
    val statuses: List<String>,
    @SerialName("status_size") val statusSize: Int,
    val params: TaskParams,
    @SerialName("file_id") val fileId: String,
    @SerialName("file_name") val fileName: String,
    @SerialName("file_size") val fileSize: String,
    val message: String,
    @SerialName("created_time") val createdTime: String,
    @SerialName("updated_time") val updatedTime: String,
    @SerialName("third_task_id") val thirdTaskId: String,
    val phase: String,
    val progress: Int,
    @SerialName("icon_link") val iconLink: String,
    val callback: String,
    @SerialName("reference_resource") val referenceResource: ReferenceResource,
    val space: String
)

@Serializable
data class TaskParams(
    @SerialName("folder_type") val folderType: String,
    @SerialName("predict_type") val predictType: String,
    val url: String
)

@Serializable
data class ReferenceResource(
    @SerialName("@type") val type: String,
    val kind: String,
    val id: String,
    @SerialName("parent_id") val parentId: String,
    val name: String,
    val size: String,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("icon_link") val iconLink: String,
    val hash: String,
    val phase: String,
    val audit: String? = null,
    @SerialName("thumbnail_link") val thumbnailLink: String,
    val params: ReferenceParams,
    val space: String,
    val medias: List<String>,
    val starred: Boolean,
    val tags: List<String>
)

@Serializable
data class ReferenceParams(
    @SerialName("platform_icon") val platformIcon: String
)
