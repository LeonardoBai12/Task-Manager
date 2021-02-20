package com.example.bancodedadossqlite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.bancodedadossqlite.TaskAdapter.Adapter.MyViewHolder
import java.util.*

class TaskAdapter {
    class Adapter : RecyclerView.Adapter<MyViewHolder>(), Filterable {
        private lateinit var taskList: List<Task>
        private lateinit var taskListFull: List<Task>
        lateinit var viewModel : TaskViewModel

        fun updateList(taskList: List<Task>) {
            this.taskList = taskList
            this.taskListFull = taskList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val item = LayoutInflater.from(parent.context).inflate(R.layout.row_task, parent, false)
            viewModel = ViewModelProvider(parent.context as FragmentActivity)[TaskViewModel::class.java]
            return MyViewHolder(item)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val task = taskList[position]

            holder.textTask.text = task.task

            onClickOpenLink(holder.imageButton,
                    task.cardLink,
                    holder.itemView.context)
            holder.itemView.setOnClickListener {
                MainActivity.startEditActivity((holder.itemView.context as Activity), task)
            }
            holder.itemView.setOnLongClickListener {
                MainActivity().dialogDeleteTask(holder.itemView.context, task, viewModel.taskDAO)
                true
            }
        }

        override fun getItemCount(): Int {
            return taskList.size
        }


        override fun getFilter(): Filter {

            val filter : Filter

            filter = object : Filter(){
                override fun performFiltering(filter: CharSequence): FilterResults {
                    var filter: CharSequence = filter
                    val results = FilterResults()
                    if (filter.length == 0) {
                        results.count = taskListFull.size
                        results.values = taskListFull
                    } else {
                        val filteredItems: MutableList<Task> = ArrayList<Task>()
                        for (i in taskListFull.indices) {
                            val data: Task = taskListFull.get(i)
                            filter = filter.toString().toLowerCase(Locale.ROOT)
                            val task = data.task.toLowerCase(Locale.ROOT)
                            if (task.contains(filter)) {
                                filteredItems.add(data)
                            }
                        }
                        results.count = filteredItems.size
                        results.values = filteredItems
                    }
                    return results
                }

                override fun publishResults(constraint: CharSequence, results: FilterResults) {
                    taskList = results.values as List<Task>
                    notifyDataSetChanged()
                }

            }
            return filter
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @BindView(R.id.textTask)
            lateinit var textTask: TextView

            @BindView(R.id.imageButton)
            lateinit var imageButton: ImageButton

            init {
                ButterKnife.bind(this, itemView)
            }
        }

        fun openLink(context: Context, url: String) {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/viewerng/viewer?url=" + url + "&saveAsFile=yes"));
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$url&saveAsFile=yes"))
            context.startActivity(intent)
        }

        private fun onClickOpenLink(button: ImageButton?, url: String, context: Context) {
            button!!.setOnClickListener { v: View? ->
                if (isConnected(context)) {
                    if (Patterns.WEB_URL.matcher(url).matches())
                        openLink(context, url)
                    else
                        Toast.makeText(context, "Invalid URL", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Verify your connection!", Toast.LENGTH_LONG).show()
                }
            }
        }

        companion object {
            fun isConnected(context: Context): Boolean {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetInfo = connectivityManager.activeNetworkInfo
                return activeNetInfo != null && activeNetInfo.isConnected
            }
        }
    }
}