package by.tuzau.andrey.rusandroidapp.data.domain;

/**
 * Created by User on 19.11.2017.
 */

public class FileElement extends AbstractFileSystemElement {

    public FileElement(String name, String fullPath) {
        super(TypeElement.FILE, fullPath, name);
    }
}
