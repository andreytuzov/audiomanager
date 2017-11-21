package by.tuzau.andrey.rusandroidapp.ui.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.Loader;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import by.tuzau.andrey.rusandroidapp.R;
import by.tuzau.andrey.rusandroidapp.data.domain.AbstractFileSystemElement;
import by.tuzau.andrey.rusandroidapp.data.domain.FolderElement;
import by.tuzau.andrey.rusandroidapp.data.service.FileService;
import by.tuzau.andrey.rusandroidapp.data.service.exception.ServiceException;
import by.tuzau.andrey.rusandroidapp.ui.adapter.FileSystemArrayAdapter;
import by.tuzau.andrey.rusandroidapp.ui.task.AudioAsyncLoader;

/**
 * Created by User on 19.11.2017.
 */

public class FileSystemActivity extends ListActivity implements FileSystemArrayAdapter.OnClickFolderListener,
        android.app.LoaderManager.LoaderCallbacks<FolderElement> {

    private static final Logger logger = Logger.getLogger("FileSystemActivity");

    private FileSystemArrayAdapter adapter;
    private TextView tvFolderPath;

    private FolderElement currentFolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileService fileService = new FileService(this);
        setContentView(R.layout.activity_filesystem);
        tvFolderPath = findViewById(R.id.tvFolderPath);
        getLoaderManager().initLoader(1, null, this).forceLoad();
    }

    @Override
    public void onClickFolder(AbstractFileSystemElement element) {
        switch (element.getTypeElement()) {
            case FILE:
                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                File file = new File(element.getFullPath());
                intent.setDataAndType(Uri.fromFile(file), "audio/*");
                startActivity(intent);
                break;
            case FOLDER:
                setCurrentFolder((FolderElement) element);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (currentFolder.getRootFolderElement() != null) {
            setCurrentFolder(currentFolder.getRootFolderElement());
        } else {
            finish();
        }
    }

    private void setCurrentFolder(FolderElement currentFolder) {
        this.currentFolder = currentFolder;
        setListAdapter(new FileSystemArrayAdapter(this, currentFolder, this));
        tvFolderPath.setText(currentFolder.getFullPath());
    }


    @Override
    public Loader<FolderElement> onCreateLoader(int id, Bundle args) {
        return new AudioAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<FolderElement> loader, FolderElement folderElement) {
        try {
            setCurrentFolder(folderElement);
        } catch (ServiceException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<FolderElement> loader) {
    }
}
