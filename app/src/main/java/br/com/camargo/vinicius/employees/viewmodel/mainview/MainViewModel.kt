package br.com.camargo.vinicius.employees.viewmodel.mainview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.camargo.vinicius.employees.firebase.FirebaseHelper
import br.com.camargo.vinicius.employees.model.EmployeeEntity

class MainViewModel: ViewModel() {
    private val TAG: String = MainViewModel::class.java.name
    var employeeList = MutableLiveData<ArrayList<EmployeeEntity>>()

    fun fetchList() {
        FirebaseHelper.getDatabase().collection("employees")
            .addSnapshotListener {
                snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed", e)
                }
                if(snapshot != null) {
                    val allEmployees = ArrayList<EmployeeEntity>()
                    val documents = snapshot.documents
                    documents.forEach {
                        val employee = it.toObject(EmployeeEntity::class.java)
                        if (employee != null) {
                            employee.key = it.id
                            allEmployees.add(employee)
                        }
                    }
                    employeeList.value = allEmployees
                }
            }
    }

    fun deleteEmployee(key: String) {
        FirebaseHelper.getDatabase().collection("employees")
            .document(key)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Funcionário excluido com sucesso!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Não foi possível excluir o funcionário!", e)
            }
    }
}