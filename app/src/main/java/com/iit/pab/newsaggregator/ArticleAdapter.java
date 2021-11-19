package com.iit.pab.newsaggregator;

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

        article.setTitle(null);

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

        Picasso.get().load(article.getUrlToImage()).placeholder(R.drawable.loading)
                .error(R.drawable.brokenimage).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void onClick() {
        // TODO
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
