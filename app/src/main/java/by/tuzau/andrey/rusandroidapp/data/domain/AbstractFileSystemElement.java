package by.tuzau.andrey.rusandroidapp.data.domain;

import java.io.File;

/**
 * Created by User on 19.11.2017.
 */

public abstract class AbstractFileSystemElement {

    protected final TypeElement typeElement;
    protected String name;
    protected String fullPath;

    public AbstractFileSystemElement(TypeElement typeElement, String fullPath, String name) {
        this.typeElement = typeElement;
        this.fullPath = fullPath;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getFullPath() {
        return fullPath;
    }
}
