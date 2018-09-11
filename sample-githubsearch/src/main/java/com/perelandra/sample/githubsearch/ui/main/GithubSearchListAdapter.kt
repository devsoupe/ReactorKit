package com.perelandra.sample.githubsearch.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.perelandra.sample.githubsearch.R
import kotlinx.android.synthetic.main.list_item_github_search.view.*

class GithubSearchListAdapter(private val repos: List<String> = emptyList()) : RecyclerView.Adapter<GithubSearchListAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_github_search, parent, false)
    return ViewHolder(itemView)
  }

  override fun getItemCount(): Int = repos.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val repo = repos[position]
    holder.itemView.repo.text = repo
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}