package com.iit.pab.newsaggregator;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView date;
    TextView author;
    ImageView image;
    TextView description;
    TextView articleCount;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.articleTitle);
        date = itemView.findViewById(R.id.articleDate);
        author = itemView.findViewById(R.id.articleAuthor);
        image = itemView.findViewById(R.id.articleImage);
        description = itemView.findViewById(R.id.articleDescription);
        articleCount = itemView.findViewById(R.id.articleCount);

        description.setMovementMethod(new ScrollingMovementMethod());   // Scrollable description
    }
}
