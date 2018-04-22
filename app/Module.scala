import com.google.inject.AbstractModule
import modules.{ConfigModule,ServiceModule}

class Module extends AbstractModule {
  private lazy val configModule = new ConfigModule()
  private lazy val serviceModule = new ServiceModule()

  override def configure(): Unit = {
    install(configModule)
    install(serviceModule)
  }
}
