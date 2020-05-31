package mvvmdemo.mobile.com.flupperdemo.utility

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore


class ImageFilePath {

    companion object
    {

        @JvmStatic
        fun isDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        fun isDucumentUri(uri:Uri,context: Context):String{
            if(isDocument(uri)) {

                val documentId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    DocumentsContract.getDocumentId(uri)
                } else {
                    ""
                }
                var id: String = documentId.split(":")[1];
                var selection: String = MediaStore.Images.Media._ID + "=?";
                var selectionArgs: () -> String = { id };
                return getDataColumn(
                    context,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    selection,
                    selectionArgs as Array<String>
                )!!
            }
            else
                return ""
        }


        /**
         * Method for return file path of Gallery image
         *
         * @param context
         * @param uri
         * @return path of the selected image file from gallery
         */
        var nopath = "Select Video Only"

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @SuppressLint("NewApi")
        @JvmStatic
        fun getPath(context: Context, uri: Uri): String? {

            // check here to KITKAT or new version
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return (Environment.getExternalStorageDirectory().toString() + "/"
                                + split[1])
                    }
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs =
                        arrayOf(split[1])
                    return getDataColumn(
                        context, contentUri, selection,
                        selectionArgs
                    )
                }
            } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )
            } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
                return uri.getPath()
            }
            return nopath
        }

        /**
         * Get the value of the data column for this Uri. This is <span id="IL_AD2" class="IL_AD">useful</span> for MediaStore Uris, and other file-based
         * ContentProviders.
         *
         * @param context
         * The context.
         * @param uri
         * The Uri to query.
         * @param selection
         * (Optional) Filter used in the query.
         * @param selectionArgs
         * (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        @JvmStatic
        fun getDataColumn(
            context: Context, uri: Uri?,
            selection: String?, selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor = uri?.let {
                    context.getContentResolver().query(
                        it, projection,
                        selection, selectionArgs, null
                    )
                }
                if (cursor != null && cursor.moveToFirst()) {
                    val index: Int = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                if (cursor != null) cursor.close()
            }
            return nopath
        }

        /**
         * @param uri
         * The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        @JvmStatic
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri
                .getAuthority()
        }

        /**
         * @param uri
         * The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        @JvmStatic
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri
                .getAuthority()
        }

        /**
         * @param uri
         * The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        @JvmStatic
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri
                .getAuthority()
        }

        /**
         * @param uri
         * The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        @JvmStatic
        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri
                .getAuthority()
        }
    }

}