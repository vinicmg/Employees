package br.com.camargo.vinicius.employees.model

import java.io.Serializable

data class EmployeeEntity(
    var key: String = "",
    var name: String = "",
    var lastname: String = "",
    var email: String = "",
    var nis: String = ""
) : Serializable
