package by.tuzau.andrey.rusandroidapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import by.tuzau.andrey.rusandroidapp.R
import by.tuzau.andrey.rusandroidapp.data.domain.AbstractFileSystemElement
import by.tuzau.andrey.rusandroidapp.data.domain.FolderElement
import by.tuzau.andrey.rusandroidapp.data.domain.TypeElement
import by.tuzau.andrey.rusandroidapp.ui.adapter.viewholder.FileViewHolder
import by.tuzau.andrey.rusandroidapp.ui.adapter.viewholder.FolderViewHolder

class FileSystemArrayAdapter(
        context: Context,
        folderElement: FolderElement,
        private val listener: OnClickFolderListener
) : BaseAdapter() {

    /** Count of view's type */
    private val VIEWTYPE_COUNT = 2
    /** List of adapter's data */
    private val data: List<AbstractFileSystemElement> = folderElement.listElement
    /** Object for creating view from .xml file */
    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    interface OnClickFolderListener {
        fun onClickFolder(element: AbstractFileSystemElement)
    }

    override fun getViewTypeCount() = VIEWTYPE_COUNT

    override fun getItemViewType(position: Int) = data[position].typeElement.index

    override fun getCount(): Int = data.size

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val typeElement = data[position].typeElement
        var currentView: View? = view
        if (view == null) {
            // Creating view from .xml file depending on typeElement
            when (typeElement) {
                TypeElement.FILE -> {
                    currentView = inflater.inflate(R.layout.file_element, null)
                    val fileViewHolder = FileViewHolder()
                    fileViewHolder.tvFileName = currentView.findViewById<TextView>(R.id.tvFileName)
                    currentView.tag = fileViewHolder
                }
                TypeElement.FOLDER -> {
                    currentView = inflater.inflate(R.layout.folder_element, null);
                    val folderViewHolder = FolderViewHolder()
                    folderViewHolder.tvFileName = currentView.findViewById<TextView>(R.id.tvFolderName)
                    folderViewHolder.tvCountAudioFiles = currentView.findViewById<TextView>(R.id.tvCountAudioFiles)
                    currentView.tag = folderViewHolder
                    currentView.setOnClickListener({_ -> listener.onClickFolder(data[position])})
                }
            }
        }
        // Setting data values
        when (typeElement) {
            TypeElement.FILE -> {
                val fileViewHolder: FileViewHolder = currentView!!.tag as FileViewHolder
                val file = data[position]
                fileViewHolder.tvFileName.text = file.name
                Log.d("FileSystemArrayAdapter", file.name)
            }
            TypeElement.FOLDER -> {
                val folderViewHolder: FolderViewHolder = currentView!!.tag as FolderViewHolder
                val folder = data!![position] as FolderElement
                folderViewHolder.tvFileName.text = folder.name
                folderViewHolder.tvCountAudioFiles.text = "Count audio: ${folder.countAudioFiles}"
            }
        }
        return currentView!!;
    }

}