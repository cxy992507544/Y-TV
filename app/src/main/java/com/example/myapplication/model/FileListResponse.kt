package com.example.myapplication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileListResponse(
    var kind: String,
    @SerialName("next_page_token") val nextPageToken: String,
    var files: List<File>,
    val version: String,
    @SerialName("version_outdated") val versionOutdated: Boolean,
    @SerialName("sync_time") val syncTime: String
)

@Serializable
data class File(
    val kind: String = "",
    val id: String,
    @SerialName("parent_id") val parentId: String = "",
    val name: String,
    @SerialName("user_id") val userId: String = "",
    val size: String = "",
    val revision: String = "",
    @SerialName("file_extension") val fileExtension: String = "",
    @SerialName("mime_type") val mimeType: String = "",
    val starred: Boolean = false,
    @SerialName("web_content_link") val webContentLink: String = "",
    @SerialName("created_time") val createdTime: String = "",
    @SerialName("modified_time") val modifiedTime: String = "",
    @SerialName("icon_link") val iconLink: String = "",
    @SerialName("thumbnail_link") val thumbnailLink: String = "",
    @SerialName("md5_checksum") val md5Checksum: String = "",
    val hash: String = "",
    val links: Map<String, String> = emptyMap(),  // Assuming links can be dynamic
    val phase: String = "",
    val audit: Audit = Audit("","",""),
    val medias: List<String> = emptyList(),
    val trashed: Boolean = false,
    @SerialName("delete_time") val deleteTime: String = "",
    @SerialName("original_url") val originalUrl: String = "",
    val params: FileParams = FileParams("",""),
    @SerialName("original_file_index") val originalFileIndex: Int = 0,
    val space: String = "",
    val apps: List<String> = emptyList(),
    val writable: Boolean = false,
    @SerialName("folder_type") val folderType: String = "",
    val collection: String? = null,
    @SerialName("sort_name") val sortName: String = "",
    @SerialName("user_modified_time") val userModifiedTime: String = "",
    @SerialName("spell_name") val spellName: List<String> = emptyList(),
    @SerialName("file_category") val fileCategory: String = "",
    val tags: List<String> = emptyList(),
    @SerialName("reference_events") val referenceEvents: List<String> = emptyList(),
    @SerialName("reference_resource") val referenceResource: String? = null
)

@Serializable
data class Audit(
    val status: String,
    val message: String,
    val title: String
)

@Serializable
data class FileParams(
    @SerialName("platform_icon") val platformIcon: String,
    @SerialName("url_info_id") val urlInfoId: String
)
