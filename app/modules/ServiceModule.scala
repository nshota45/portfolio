package modules

import com.google.inject.AbstractModule

class ServiceModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[services.LoginService]).to(classOf[services.impl.LoginService]).asEagerSingleton()
  }
}
