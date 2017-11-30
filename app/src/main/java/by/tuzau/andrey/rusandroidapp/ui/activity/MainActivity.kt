package by.tuzau.andrey.rusandroidapp.ui.activity

import android.app.ListActivity
import android.app.LoaderManager
import android.content.Loader
import android.os.Bundle
import android.util.Log
import by.tuzau.andrey.rusandroidapp.R
import by.tuzau.andrey.rusandroidapp.data.domain.AbstractFileSystemElement
import by.tuzau.andrey.rusandroidapp.data.domain.FolderElement
import by.tuzau.andrey.rusandroidapp.ui.adapter.FileSystemArrayAdapter
import by.tuzau.andrey.rusandroidapp.ui.task.AudioAsyncLoader

class MainActivity : ListActivity() {

    val AUDIO_LOADER_ID = 1
    lateinit var currentFolder: FolderElement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filesystem)
        loadData()
    }

    override fun onBackPressed() = if (currentFolder.rootFolderElement != null) {
        showFolder(currentFolder.rootFolderElement!!)
    } else {
        Log.d("sdf", currentFolder)
    }

    private fun loadData() {
        loaderManager.initLoader(AUDIO_LOADER_ID, Bundle.EMPTY,
            object : LoaderManager.LoaderCallbacks<FolderElement> {
                override fun onCreateLoader(id: Int, args: Bundle?): Loader<FolderElement>
                        = AudioAsyncLoader(this@MainActivity)

                override fun onLoaderReset(loader: Loader<FolderElement>?) {
                }

                override fun onLoadFinished(loader: Loader<FolderElement>?, folderElement: FolderElement?) {
                    if (folderElement != null) {
                        currentFolder = folderElement
                        showFolder(currentFolder)
                    }
                }
            })
    }

    private fun showFolder(folderElement: FolderElement) {
        listAdapter = FileSystemArrayAdapter(this, folderElement,
                object : FileSystemArrayAdapter.OnClickFolderListener {
                    override fun onClickFolder(element: AbstractFileSystemElement) {
                        when (element) {
                            is FolderElement -> showFolder(element)
                        }
                    }

                })
    }

}