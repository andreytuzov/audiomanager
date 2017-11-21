package by.tuzau.andrey.rusandroidapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import by.tuzau.andrey.rusandroidapp.R;
import by.tuzau.andrey.rusandroidapp.data.domain.AbstractFileSystemElement;
import by.tuzau.andrey.rusandroidapp.data.domain.FileElement;
import by.tuzau.andrey.rusandroidapp.data.domain.FolderElement;
import by.tuzau.andrey.rusandroidapp.data.domain.TypeElement;
import by.tuzau.andrey.rusandroidapp.ui.adapter.viewholder.FileViewHolder;
import by.tuzau.andrey.rusandroidapp.ui.adapter.viewholder.FolderViewHolder;

/**
 * Created by User on 19.11.2017.
 */
public class FileSystemArrayAdapter extends BaseAdapter {

    /** Count of view's type */
    private static final int VIEWTYPE_COUNT = 2;
    /** List of adapter's data */
    private List<AbstractFileSystemElement> data;
    /** Object for creating view from .xml file */
    private final LayoutInflater inflater;

    private final OnClickFolderListener listener;

    public interface OnClickFolderListener {
        void onClickFolder(AbstractFileSystemElement element);
    }

    public FileSystemArrayAdapter(Context context, FolderElement folderElement, OnClickFolderListener listener) {
        this.data = folderElement.getListFileSystemElement();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }

    @Override
    public int getViewTypeCount() {
        return VIEWTYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getTypeElement().getIndex();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        TypeElement typeElement = data.get(position).getTypeElement();
        if (view == null) {
            // Creating view from .xml file depending on typeElement
            switch (typeElement) {
                case FILE:
                    view = inflater.inflate(R.layout.file_element, null);
                    FileViewHolder fileViewHolder = new FileViewHolder();
                    fileViewHolder.tvFileName = (TextView) view.findViewById(R.id.tvFileName);
                    view.setTag(fileViewHolder);
                    break;
                case FOLDER:
                    view = inflater.inflate(R.layout.folder_element, null);
                    FolderViewHolder folderViewHolder = new FolderViewHolder();
                    folderViewHolder.tvFileName = (TextView) view.findViewById(R.id.tvFolderName);
                    folderViewHolder.tvCountAudioFiles = (TextView) view.findViewById(R.id.tvCountAudioFiles);
                    view.setTag(folderViewHolder);
                    break;
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickFolder(data.get(position));
                }
            });
        }
        // Setting data values
        switch (typeElement) {
            case FILE:
                FileViewHolder fileViewHolder = (FileViewHolder) view.getTag();
                FileElement file = (FileElement) data.get(position);
                fileViewHolder.tvFileName.setText(file.getName());
                break;
            case FOLDER:
                FolderViewHolder folderViewHolder = (FolderViewHolder) view.getTag();
                FolderElement folder = (FolderElement) data.get(position);
                folderViewHolder.tvFileName.setText(folder.getName());
                folderViewHolder.tvCountAudioFiles.setText("Count audio: " + folder.getCountAudioFiles());
                break;
        }
        return view;
    }
}
