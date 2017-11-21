package by.tuzau.andrey.rusandroidapp.data.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 19.11.2017.
 */

public class FolderElement extends AbstractFileSystemElement {

    private List<AbstractFileSystemElement> listFileSystemElement;
    private FolderElement rootFolderElement;
    private int countAudioFiles;

    public FolderElement(String name, String fullPath) {
        super(TypeElement.FOLDER, fullPath, name);
    }

    public FolderElement(String name, String fullPath, int countAudioFiles, List<AbstractFileSystemElement> listFileSystemElement) {
        super(TypeElement.FOLDER, fullPath, name);
        this.countAudioFiles = countAudioFiles;
        this.listFileSystemElement = listFileSystemElement;
    }

    public void addCountAudioFiles(int countAudioFiles) {
        this.countAudioFiles += countAudioFiles;
    }

    public void addAbstractFileSystemElement(AbstractFileSystemElement abstractFileSystemElement) {
        if (listFileSystemElement == null) {
            listFileSystemElement = new ArrayList<>();
        }
        listFileSystemElement.add(abstractFileSystemElement);
    }

    public int getCountAudioFiles() {
        return countAudioFiles;
    }

    public void setCountAudioFiles(int countAudioFiles) {
        this.countAudioFiles = countAudioFiles;
    }

    public List<AbstractFileSystemElement> getListFileSystemElement() {
        return listFileSystemElement;
    }

    public void setListFileSystemElement(List<AbstractFileSystemElement> listFileSystemElement) {
        this.listFileSystemElement = listFileSystemElement;
    }

    public FolderElement getRootFolderElement() {
        return rootFolderElement;
    }

    public void setRootFolderElement(FolderElement rootFolderElement) {
        this.rootFolderElement = rootFolderElement;
    }
}
