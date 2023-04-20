package br.com.camargo.vinicius.employees.viewmodel.newemployee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewEmployeeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewEmployeeViewModel::class.java)) {
            return NewEmployeeViewModel() as T
        }
        throw IllegalArgumentException("UnkownViewModel")
    }
}