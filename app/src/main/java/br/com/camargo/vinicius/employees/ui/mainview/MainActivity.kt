package br.com.camargo.vinicius.employees.ui.mainview

import android.app.Activity
import br.com.camargo.vinicius.employees.adapter.EmployeeListAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.camargo.vinicius.employees.R
import br.com.camargo.vinicius.employees.model.EmployeeEntity
import br.com.camargo.vinicius.employees.ui.newemployee.NewEmployee
import com.google.android.material.floatingactionbutton.FloatingActionButton
import br.com.camargo.vinicius.employees.viewmodel.mainview.MainViewModel
import br.com.camargo.vinicius.employees.viewmodel.mainview.MainViewModelFactory

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var viewModel: MainViewModel
    private val employeeListAdapter: EmployeeListAdapter by lazy {
        EmployeeListAdapter(this::onClickedItem, viewModel, arrayListOf(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factoryViewModel = MainViewModelFactory()
        viewModel = ViewModelProvider(this, factoryViewModel)[MainViewModel::class.java]
        configureFab()
        configureAdapter()
        viewModel.fetchList()
    }

    private fun configureAdapter() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.employeeList.observe(this) {
            recyclerView.adapter = EmployeeListAdapter(this::onClickedItem, viewModel, it, this)
        }
    }

    private fun configureFab() {
        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            val intent = Intent(this, NewEmployee::class.java)
            resultLauncher.launch(intent)
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Cadastro efetuado com sucesso!", Toast.LENGTH_LONG)
            } else {
                Toast.makeText(this, "Ocorreu um erro ao efetuar o cadastro!", Toast.LENGTH_LONG)
            }
        }

    private fun onClickedItem(employeee: EmployeeEntity) {
        val fabDelete = findViewById<FloatingActionButton>(R.id.fabDelete)
        fabDelete.visibility = View.VISIBLE

        val fabUpdate = findViewById<FloatingActionButton>(R.id.fabUpdate)
        fabUpdate.visibility = View.VISIBLE

        fabDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("Tem certeza que deseja fazer a exclusão?")
                .setCancelable(true)
                .setPositiveButton("Sim") { dialog, id ->
                    viewModel.deleteEmployee(employeee.key)
                }
                .setNegativeButton("Não") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        fabUpdate.setOnClickListener {
            val intent = Intent(this, NewEmployee::class.java)
            intent.putExtra("employee", employeee)
            resultLauncher.launch(intent)
        }
    }
}