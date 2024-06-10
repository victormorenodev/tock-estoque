package com.tockestoque.repository

import com.tockestoque.db.DatabaseFactory.dbQuery
import com.tockestoque.db.schemas.UserTable
import com.tockestoque.model.User
import com.tockestoque.routing.request.SignUpRequest
import io.ktor.http.cio.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserRepository {

    suspend fun findAll(): List<User?> =
        dbQuery {
            UserTable.selectAll().map(::resultToRow)
        }


    fun findById(id: Int): User? =
        TODO("To do")

    suspend fun findByEmail(email: String): User? {
        val user = dbQuery {
            UserTable.selectAll().where { UserTable.email.eq(email) }
                .map { resultToRow(it) }.singleOrNull()
        }
        return user
    }

    suspend fun createUser(user: SignUpRequest): User? {
            val user = dbQuery {
                UserTable.insert {
                    it[email] = user.email
                    it[fullName] = user.fullName
                    it[password] = user.password
                }
            }.resultedValues?.first()
        return resultToRow(user)
    }


    private fun resultToRow(row: ResultRow?): User? {
        return if (row == null) null
        else User(
            id = row[UserTable.id],
            password = row[UserTable.password],
            fullName = row[UserTable.fullName],
            email = row[UserTable.email],
            createdAt = row[UserTable.createdAt].toString(),
        )
    }
}