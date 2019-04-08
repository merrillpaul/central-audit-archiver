package com.pearson.central.audit

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.pearson.techops.tomcat.secured.Encryptor
import groovy.json.JsonOutput
import groovy.sql.Sql
import org.apache.log4j.Logger

import java.sql.DriverManager

class ArchiveAudit implements RequestHandler<Object, String> {
	private static final Logger LOG = Logger.getLogger(ArchiveAudit)
	private static final Encryptor encryptor = new Encryptor("techopsHosting@123")
	static {
		println("Loading db driver")
		Class.forName("org.postgresql.Driver")
		println("Loaded db driver")
	}
	@Override
	String handleRequest(Object input, Context context) {
		println("running: " + new Date().toString())
		def data = this.dbMetadata

		def connection
		def statement
		def url = "jdbc:postgresql://${data.host.trim()}:${data.port.trim()}/${data.database.trim()}"
		println "Trying a connection to ${url}"
		try {
			connection = DriverManager.getConnection(url, data.user, data.password)
			connection.setAutoCommit(false)
			statement = connection.createStatement()
			def res = statement.executeQuery("select count(1) from audit_txn")
			if (res.next()) {
				println "Count before run is ${res.getInt(1)}"
			}
			res.close()
			statement.close()

			statement = connection.createStatement()
			statement.execute("select archive_audit_data()")
			statement.close()

			res = statement.executeQuery("select count(1) from audit_txn")
			if (res.next()) {
				println "Count after run is ${res.getInt(1)}"
			}
			res.close()
			statement.close()
			connection.commit()

		} catch (Exception e) {
			e.printStackTrace()
			if (connection) {
				try {
					connection.rollback()
				} finally {
					// ignored
				}
			}
		} finally {
			if (connection) {
				connection.close()
			}
		}
		return "ok"
	}

	def getDbMetadata () {
		def data = [
		        host: System.getenv("DB_HOST"),
				port: System.getenv("DB_PORT"),
				user: System.getenv("DB_USER"),
				password: encryptor.decrypt(System.getenv("DB_PASSWORD")),
				database: System.getenv("DB")
		]

		data
	}
}


