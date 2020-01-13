package cn.zenliu.ktor.features.redis

import cn.zenliu.ktor.features.FeatureTemplate
import cn.zenliu.ktor.features.properties.annotation.*
import com.fasterxml.jackson.databind.*
import io.ktor.application.*
import kotlinx.io.core.*
import org.litote.kmongo.reactivestreams.*  //NEEDED! import KMongo reactivestreams extensions
import org.litote.kmongo.coroutine.* //NEEDED! import KMongo coroutine extensions

import java.nio.*
import java.nio.charset.*

class Kmongo {
	companion
	object KmongoFeature :
		FeatureTemplate.FeatureObjectTemplate<Application, KmongoFeature, KmongoFeature, KmongoFeature.KMongoConf>() {
		override val configClazz = KmongoFeature.KMongoConf::class
		private val objectMapper: ObjectMapper = ObjectMapper()

		@Properties("mongo")
		class KMongoConf(
			var conn: String
		)

		/**
		 * configrate inner ObjectMapper
		 * @param configurate (ObjectMapper) -> Unit
		 */

		fun configObjectMapper(configurate: (ObjectMapper) -> Unit) {
			configurate.invoke(objectMapper)
		}

		val client by lazy {
			KMongo.createClient(config!!.conn).coroutine
		}
		fun newClient(conn: String)=KMongo.createClient(conn)
		fun newClient(conn: com.mongodb.ConnectionString)=KMongo.createClient(conn)
		fun newClient(connSetting: com.mongodb.MongoClientSettings)=KMongo.createClient(connSetting)
		override fun init(
			pipeline: Application,
			configure: KmongoFeature.() -> Unit
		) = run {
			config ?: throw Exception("datasource mongo.conn not set!")
			this
		}



		private
		fun ByteBuffer.decodeString(charset: Charset = Charsets.UTF_8): String {
			return charset.decode(this).toString()
		}

	}
}