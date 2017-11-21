package by.tuzau.andrey.rusandroidapp.ui.task;

import android.content.AsyncTaskLoader;
import android.content.Context;

import by.tuzau.andrey.rusandroidapp.data.domain.FolderElement;
import by.tuzau.andrey.rusandroidapp.data.service.FileService;

/**
 * Created by User on 20.11.2017.
 */

public class AudioAsyncLoader extends AsyncTaskLoader<FolderElement> {

    private FileService fileService;

    public AudioAsyncLoader(Context context) {
        super(context);
        this.fileService = new FileService(context);
    }

    @Override
    public FolderElement loadInBackground() {
        return fileService.createFolder();
    }
}
