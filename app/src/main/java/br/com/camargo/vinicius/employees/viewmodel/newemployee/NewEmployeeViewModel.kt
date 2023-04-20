package br.com.camargo.vinicius.employees.viewmodel.newemployee

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.camargo.vinicius.employees.firebase.FirebaseHelper
import br.com.camargo.vinicius.employees.model.EmployeeEntity

class NewEmployeeViewModel : ViewModel() {
    private val TAG: String = NewEmployeeViewModel::class.java.name
    var shouldCloseActivityWithSuccess = MutableLiveData(false)
    var shouldCloseActivityWithError = MutableLiveData(false)
    var textName: String = ""
    var textLastName: String = ""
    var textEmail: String = ""
    var textNis: String = ""
    var employeeEntity: EmployeeEntity? = null
    var isUpdate: Boolean = false

    fun saveEmployee() {
        val employee = hashMapOf(
            "name" to textName,
            "lastname" to textLastName,
            "email" to textEmail,
            "nis" to textNis
        )

        FirebaseHelper.getDatabase().collection("employees")
            .add(employee)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Document added with ID: ${documentReference.id}")
                shouldCloseActivityWithSuccess.value = true
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                shouldCloseActivityWithError.value = true
            }
    }

    fun updateEmployee() {
        val employee = hashMapOf<String, Any>(
            "name" to textName,
            "lastname" to textLastName,
            "email" to textEmail,
            "nis" to textNis
        )

            FirebaseHelper.getDatabase()
                .collection("employees")
                .document(employeeEntity!!.key)
                .update(employee)
                .addOnSuccessListener {
                    Log.d(TAG, "O funcionário foi atualizado com sucesso!")
                    shouldCloseActivityWithSuccess.value = true
                }
                .addOnFailureListener {e->
                    Log.w(TAG, "Erro ao atualizar o funcionário", e)
                    shouldCloseActivityWithError.value = true
                }
    }
}