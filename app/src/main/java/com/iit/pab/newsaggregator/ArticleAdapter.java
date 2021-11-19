package com.iit.pab.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iit.pab.newsaggregator.dto.ArticleDTO;
import com.iit.pab.newsaggregator.utils.DateTimeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {

    private final MainActivity mainActivity;
    private final List<ArticleDTO> articles;

    public ArticleAdapter(MainActivity mainActivity, List<ArticleDTO> articles) {
        this.mainActivity = mainActivity;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleDTO article = articles.get(position);

        setTextField(holder.title, article.getTitle());
        setTextField(holder.author, article.getAuthor());
        setTextField(holder.description, article.getDescription());

        holder.articleCount.setText(
                String.format(Locale.getDefault(), "%d of %d", position + 1, articles.size()));

        if (article.getPublishedAt() != null) {
            holder.date.setText(DateTimeUtils.formatDateTime(article.getPublishedAt()));
            holder.date.setVisibility(View.VISIBLE);
        } else {
            holder.date.setVisibility(View.INVISIBLE);
        }

        if (article.getUrlToImage() != null) {
            Picasso.get().load(article.getUrlToImage()).placeholder(R.drawable.loading)
                    .error(R.drawable.brokenimage).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.noimage);
        }

        holder.title.setOnClickListener(v -> onClick(article.getUrl()));
        holder.image.setOnClickListener(v -> onClick(article.getUrl()));
        holder.description.setOnClickListener(v -> onClick(article.getUrl()));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void onClick(String url) {
        if (url != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            if (intent.resolveActivity(mainActivity.getPackageManager()) != null) {
                mainActivity.startActivity(intent);
            }
        }
    }

    private void setTextField(TextView textView, String content) {
        if (content != null) {
            textView.setText(content);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }
}
