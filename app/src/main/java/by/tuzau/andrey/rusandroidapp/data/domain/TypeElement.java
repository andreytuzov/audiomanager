package by.tuzau.andrey.rusandroidapp.data.domain;

/**
 * Created by User on 19.11.2017.
 */

public enum TypeElement {
    FOLDER(0), FILE(1);

    private int index;

    TypeElement(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
