package config

case class GoogleOauthConfig(
  authorizationEndpoint: String,
  clientId: String,
  clientSecret: String,
  responseType: String,
  scope: String,
  redirectUri: String,
  requestTimeOut: Int,
  grantType: String,
  tokenEndpoint: String
)
