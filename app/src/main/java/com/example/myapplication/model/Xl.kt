package com.example.myapplication.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Xl(
    @SerialName("upload_type") val uploadType: String,
    val url: Url,
    val file: String? = null,
    val task: xltask
)

@Serializable
data class Url(
    val kind: String
)

@Serializable
data class xltask(
    val kind: String,
    val id: String,
    val name: String,
    val type: String,
    @SerialName("user_id") val userId: String,
    val statuses: List<String>,
    @SerialName("status_size") val statusSize: Int,
    val params: Params,
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
    @SerialName("reference_resource") val referenceResource: String? = null,
    val space: String
)

@Serializable
data class Params(
    @SerialName("folder_type") val folderType: String,
    @SerialName("predict_speed") val predictSpeed: String,
    @SerialName("predict_type") val predictType: String
)
