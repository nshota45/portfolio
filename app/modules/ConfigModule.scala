package modules

import com.google.inject.AbstractModule
import com.typesafe.config.ConfigFactory
import config.GoogleOauthConfig

class ConfigModule extends AbstractModule {
  private lazy val config = ConfigFactory.load()


  override def configure(): Unit = {
    bind(classOf[GoogleOauthConfig]).toInstance(GoogleOauthConfig(
      authorizationEndpoint = config.getString("oauth.google.authorization_endpoint"),
      clientId = config.getString("oauth.google.client_id"),
      clientSecret = config.getString("oauth.google.client_secret"),
      responseType = config.getString("oauth.google.response_type"),
      scope = config.getString("oauth.google.scope"),
      redirectUri = config.getString("oauth.google.redirect_uri"),
      requestTimeOut = config.getInt("oauth.google.request_timeout"),
      grantType = config.getString("oauth.google.grant_type"),
      tokenEndpoint = config.getString("oauth.google.token_endpoint")
    ))
  }
}
