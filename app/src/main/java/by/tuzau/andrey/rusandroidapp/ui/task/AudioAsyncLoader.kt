package by.tuzau.andrey.rusandroidapp.ui.task

import android.content.AsyncTaskLoader
import android.content.Context
import by.tuzau.andrey.rusandroidapp.data.domain.FolderElement
import by.tuzau.andrey.rusandroidapp.data.service.FileService

class AudioAsyncLoader(context: Context) : AsyncTaskLoader<FolderElement>(context) {

    private var folderElement: FolderElement? = null

    override fun onStartLoading() = if (folderElement != null) {
        deliverResult(folderElement)
    } else {
        forceLoad()
    }

    override fun loadInBackground(): FolderElement? {
        val fileService = FileService(context)
        folderElement = fileService.createFolder()
        return folderElement
    }
}