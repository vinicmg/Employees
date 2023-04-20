package br.com.camargo.vinicius.employees.ui.newemployee

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import br.com.camargo.vinicius.employees.R
import br.com.camargo.vinicius.employees.model.EmployeeEntity
import br.com.camargo.vinicius.employees.viewmodel.newemployee.NewEmployeeViewModel
import br.com.camargo.vinicius.employees.viewmodel.newemployee.NewEmployeeViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NewEmployee : AppCompatActivity(R.layout.activity_new_employee) {
    private lateinit var viewModel: NewEmployeeViewModel
    private lateinit var edtName: TextView
    private lateinit var edtLastName: TextView
    private lateinit var edtEmail: TextView
    private lateinit var edtNis: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factoryViewModel = NewEmployeeViewModelFactory()
        viewModel = ViewModelProvider(this, factoryViewModel)[NewEmployeeViewModel::class.java]
        configureFab()
        configureEditText()
        configureObserver()

        if (intent.hasExtra("employee")) {
            viewModel.employeeEntity = intent.extras?.getSerializable("employee") as EmployeeEntity

            if (viewModel.employeeEntity != null) {
                configureUpdate()
            }
        }
    }

    private fun configureUpdate() {
        edtName.text = viewModel.employeeEntity?.name ?: ""
        edtLastName.text = viewModel.employeeEntity?.lastname ?: ""
        edtEmail.text = viewModel.employeeEntity?.email ?: ""
        edtNis.text = viewModel.employeeEntity?.nis ?: ""

        viewModel.isUpdate = true
    }

    private fun configureObserver() {
        viewModel.shouldCloseActivityWithSuccess.observe(this) {
            if (it) {
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        viewModel.shouldCloseActivityWithError.observe(this) {
            if (it) {
                val intent = Intent()
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }
    }

    private fun configureEditText() {
        edtName = findViewById(R.id.edtName)
        edtLastName = findViewById(R.id.edtLastName)
        edtEmail = findViewById(R.id.edtEmail)
        edtNis = findViewById(R.id.edtNis)

        configureEditTextChanged()
    }

    private fun configureEditTextChanged() {
        edtName.doAfterTextChanged {
            viewModel.textName = it.toString()
        }
        edtLastName.doAfterTextChanged {
            viewModel.textLastName = it.toString()
        }
        edtEmail.doAfterTextChanged {
             viewModel.textEmail = it.toString()
        }
        edtNis.doAfterTextChanged {
            viewModel.textNis = it.toString()
        }
    }

    private fun configureFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fabSaveEmployee)
        fab.setOnClickListener {
            if (validateEntries()) {
                if (viewModel.isUpdate) {
                    viewModel.updateEmployee()
                    return@setOnClickListener
                }
                viewModel.saveEmployee()
            }
        }
    }

    private fun validateEntries(): Boolean {
        if (viewModel.textName.length < 2 || viewModel.textName.length > 30) {
            edtName.error = this.getString(R.string.edt_name_error)
            return false
        }
        if (viewModel.textLastName.length < 2 || viewModel.textLastName.length > 50) {
            edtLastName.error = this.getString(R.string.edt_lastname_error)
            return false
        }
        if (!TextUtils.isEmpty(viewModel.textEmail) && !android.util.Patterns.EMAIL_ADDRESS.matcher(viewModel.textEmail).matches()) {
            edtEmail.error = this.getString(R.string.edt_email_error)
            return false
        }
        if (viewModel.textNis.toDoubleOrNull() == null) {
            edtNis.error = this.getString(R.string.edt_nis_error)
            return false
        }

        return true
    }
}