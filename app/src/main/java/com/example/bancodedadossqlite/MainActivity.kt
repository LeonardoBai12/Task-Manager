package com.example.bancodedadossqlite

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_main)
open class MainActivity : AppCompatActivity() {

    private val adapter = TaskAdapter.Adapter()
    private lateinit var viewModel : TaskViewModel

    @ViewById(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    @AfterViews
    fun afterViews(){
        createRecyclerView()
        setupViewModel()
    }

    fun setupViewModel(){
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        viewModel.taskDAO =  TaskDAO(this)
        viewModel.loadTasks().observe(this, this::updateList)
    }

    fun updateList(tasks: List<Task>){
        adapter.updateList(tasks)
    }

    fun createRecyclerView(){
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayout.VERTICAL))
        recyclerView.setAdapter(adapter)
    }

    fun dialogDeleteTask(context : Context, task: Task, taskDAO: TaskDAO) {
        lateinit var dialog:AlertDialog
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Confirm")
        builder.setMessage("Delete this task?")

        builder.setPositiveButton("YES") { dialog, which ->
            taskDAO.delete(task)
            dialog.dismiss()
        }
        builder.setNegativeButton("NO") { dialog, which -> // Do nothing
            dialog.dismiss()
        }

        dialog = builder.create()
        dialog.show()
    }

    @Click(R.id.buttonAdd)
    fun addClick() {
        val task = Task()
        startEditActivity(this, task )
    }

    companion object{
        fun startEditActivity(activity : Activity, task : Task){
            val intent = Intent(activity, EditTaskActivity_::class.java)
            val bundle = Bundle()
            bundle.putSerializable("Task", task)
            intent.putExtras(bundle)
            activity.startActivityForResult(intent, 8)
        }
    }

    @OnActivityResult(8)
    fun onActivityResult(resultCode: Int, data: Intent) {
        if (resultCode == RESULT_OK) {
            val task = data.getSerializableExtra("newTask") as Task
            if (task.id == null)
                viewModel.taskDAO.newTask(task)
            else
                viewModel.taskDAO.update(task)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.task_menu, menu)

        val drawable = menu.getItem(0).icon
        drawable.mutate()

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)

        val textChangeListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                adapter.getFilter().filter(query)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.getFilter().filter(query)
                return true
            }
        }
        searchView.setOnQueryTextListener(textChangeListener)

        return super.onCreateOptionsMenu(menu)
    }

}