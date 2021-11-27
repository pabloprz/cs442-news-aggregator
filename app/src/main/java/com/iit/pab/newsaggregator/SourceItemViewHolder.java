package com.iit.pab.newsaggregator;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class SourceItemViewHolder {

    TextView name;

    public SourceItemViewHolder(@NonNull View itemView) {
        name = itemView.findViewById(R.id.sourceItemTextView);
    }
}
