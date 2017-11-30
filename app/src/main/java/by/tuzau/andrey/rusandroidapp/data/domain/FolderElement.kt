package by.tuzau.andrey.rusandroidapp.data.domain

class FolderElement(
        name: String,
        fullPath: String,
        val listElement: MutableList<AbstractFileSystemElement> = mutableListOf(),
        var rootFolderElement: FolderElement? = null
) : AbstractFileSystemElement(TypeElement.FOLDER, name, fullPath) {

    var countAudioFiles: Int = 0

    operator fun FolderElement.plusAssign(element: FolderElement) {
        countAudioFiles += element.countAudioFiles
    }

    fun addCountAudioFiles(countAudioFiles: Int) {
        this.countAudioFiles += countAudioFiles
    }

    fun addAbstractFileSystemElement(element: AbstractFileSystemElement) {
        listElement.add(element)
    }

}