package by.tuzau.andrey.rusandroidapp.data.service

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import by.tuzau.andrey.rusandroidapp.data.domain.FileElement
import by.tuzau.andrey.rusandroidapp.data.domain.FolderElement
import by.tuzau.andrey.rusandroidapp.data.domain.TypeElement
import java.io.File

class FileService(private val context: Context) {

    private val mapIsAudioEmpty: MutableMap<String, Boolean> = mutableMapOf()

    fun createFolder() : FolderElement {
        val mapFolderElement: MutableMap<String, FolderElement> = mutableMapOf()
        val listAudioPath = getListAudioPath()
        var lastFolderElement: FolderElement? = null
        for (path in listAudioPath) {
            val listName = path.split(File.separator)
            var pathCurrentFolder: String = ""
            for (i in 1 until listName.size - 1) {
                val name = listName[i]
                pathCurrentFolder += File.separator + name

                if (!mapFolderElement.containsKey(pathCurrentFolder)) {
                    val folderElement = FolderElement(name, pathCurrentFolder)
                    folderElement.countAudioFiles = 1
                    if (lastFolderElement != null) {
                        lastFolderElement.addAbstractFileSystemElement(folderElement)
                        folderElement.rootFolderElement = lastFolderElement
                    }
                    lastFolderElement = folderElement
                    mapFolderElement.put(pathCurrentFolder, folderElement)
                    mapIsAudioEmpty.put(pathCurrentFolder, true)
                } else {
                    lastFolderElement = mapFolderElement[pathCurrentFolder]
                    lastFolderElement?.addCountAudioFiles(1)
                }
            }
            // Adding audio file
            val audioName = listName.last()
            lastFolderElement?.addAbstractFileSystemElement(FileElement(audioName,
                    pathCurrentFolder + File.separator + audioName))
            mapIsAudioEmpty.put(pathCurrentFolder, false)
        }

        var rootFolderElement = getRootFolderElement(lastFolderElement!!)
        reduceTreeFolderElement(rootFolderElement)

        rootFolderElement = getRootFolderElement(lastFolderElement!!)

        return rootFolderElement
    }

    private fun getRootFolderElement(anyFolderElement: FolderElement): FolderElement {
        var folderElement = anyFolderElement
        while (true) {
            folderElement = folderElement.rootFolderElement ?: break
        }
        return folderElement
    }

    private fun reduceTreeFolderElement(rootFolderElement: FolderElement) {
        // If only one folder inside this folder and there is not any mp3 files
        val isAudioEmpty = mapIsAudioEmpty[rootFolderElement.fullPath]
        if (isAudioEmpty != null && isAudioEmpty) {
            val countFolder = rootFolderElement.listElement.size
            if (countFolder == 1) {
                // Combine two folder in one
                val folderElement = rootFolderElement.listElement[0] as FolderElement
                folderElement.name = rootFolderElement.name + File.separator + folderElement.name
                val topFolderElement = rootFolderElement.rootFolderElement
                folderElement.rootFolderElement = topFolderElement
                if (topFolderElement != null) {
                    topFolderElement.addAbstractFileSystemElement(folderElement)
                    topFolderElement.listElement.remove(rootFolderElement)
                }
            }
        }
        val countItems: Int = rootFolderElement.listElement.size
        // Getting around all elements into folderElement
        for (i in 0 until countItems) {
            val item = rootFolderElement.listElement.get(i)
            if (item.typeElement == TypeElement.FOLDER) {
                reduceTreeFolderElement(item as FolderElement)
            }
        }
    }

    private fun getListAudioPath(): List<String> {
        val listPath = mutableListOf<String>()
        val resolver: ContentResolver = context.contentResolver
        // Data for the request
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.DATA + " like '%.mp3'";
        val cursor = resolver.query(uri, null, selection, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val pathId = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            do {
                listPath.add(cursor.getString(pathId))
            } while (cursor.moveToNext())
        }
        return listPath
    }

}