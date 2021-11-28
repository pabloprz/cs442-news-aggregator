package com.iit.pab.newsaggregator;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iit.pab.newsaggregator.dto.SourceDTO;

public class SourceItemAdapter extends ArrayAdapter<SourceDTO> {

    private final MainActivity mainActivity;
    private final SourceDTO[] objects;

    public SourceItemAdapter(@NonNull Context context, int resource, @NonNull SourceDTO[] objects) {
        super(context, resource, objects);
        this.mainActivity = (MainActivity) context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SourceItemViewHolder vh;

        if (convertView == null) {

            LayoutInflater inflater = mainActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);

            vh = new SourceItemViewHolder(convertView);

            convertView.setTag(vh);
        } else {
            vh = (SourceItemViewHolder) convertView.getTag();
        }

        SourceDTO source = objects[position];
        vh.name.setText(source.getName());
        // Color is set; if no color, default is white
        vh.name.setTextColor(Color.parseColor(
                mainActivity.colorCategories.getOrDefault(source.getCategory(), "#FFFFFF")));

        return convertView;
    }
}
