package com.stenbergroom.githubreader.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.stenbergroom.githubreader.app.R;
import com.stenbergroom.githubreader.app.entity.Repository;

import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {

    private List<Repository> repositories;
    private int rowLayout;

    public RepositoryAdapter(List<Repository> repositories, int rowLayout) {
        this.repositories = repositories;
        this.rowLayout = rowLayout;
    }

    public void addRepositories(List<Repository> repoList){
        this.repositories.addAll(repoList);
        this.notifyItemRangeInserted(0, repoList.size() - 1);
    }

    public void clearRepositories(){
        int size = this.repositories.size();
        if(size > 0){
            for(int i = 0; i < size; i++){
                repositories.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Repository repo = repositories.get(i);
        viewHolder.tvRepositoryName.setText(repo.getName());
        viewHolder.tvRepositoryLanguage.setText(repo.getLanguage());
        viewHolder.tvForkedCount.setText(repo.getForks());
        viewHolder.tvStarredCount.setText(repo.getWatchers());
    }

    @Override
    public int getItemCount() {
        return repositories == null ? 0 : repositories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvRepositoryName, tvRepositoryLanguage, tvForkedCount, tvStarredCount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRepositoryName = (TextView)itemView.findViewById(R.id.tv_repository_name);
            tvRepositoryLanguage = (TextView)itemView.findViewById(R.id.tv_repository_language);
            tvForkedCount = (TextView)itemView.findViewById(R.id.tv_forked_count);
            tvStarredCount = (TextView)itemView.findViewById(R.id.tv_watches_count);
        }
    }
}
