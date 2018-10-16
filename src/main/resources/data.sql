insert into oauth_client_details (
  client_id,
  client_secret,
  resource_ids,
  scope,
  authorized_grant_types,
  web_server_redirect_uri,
  authorities,
  access_token_validity,
  refresh_token_validity,
  additional_information,
  autoapprove
) values (
  'alexa-skill',
  'alexa-skill-secret',
  'some-resource-1,some-resource-2',
  'read,write,trust',
  'password,authorization_code,refresh_token',
  'http://oauth-callback.com',
  'ROLE_USER',
  NULL,
  NULL,
  '{}',
  ''
);