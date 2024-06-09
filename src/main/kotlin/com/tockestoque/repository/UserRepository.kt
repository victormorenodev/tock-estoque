package com.tockestoque.repository

import com.tockestoque.db.DatabaseFactory.dbQuery
import com.tockestoque.db.schemas.UserTable
import com.tockestoque.model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class UserRepository {

    suspend fun findAll(): List<User> =
        dbQuery {
            UserTable.selectAll().map(::resultToRow)
        }


    fun findById(id: Int): User? =
        TODO("To do")

    fun findByEmail(username: String): User? =
        TODO("To do")

    fun createUser(user: User): Boolean =
        TODO("To do")

    private fun resultToRow(row: ResultRow): User =
        User(
            id = row[UserTable.id],
            password =row[UserTable.password],
            fullName = row[UserTable.fullName],
            email = row[UserTable.email],
            createdAt = row[UserTable.createdAt].toString(),
        )
}