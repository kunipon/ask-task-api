# alexa-task-scheduler

## アクセストークン取得

* 正式な流れは、認可コードグラントで /oauth/authorize で code取得からの、 /oauth/token でアクセストークン取得
* テストなど正式な流れを取れないときはリソースオーナーパスワードクレデンシャルで、いきなり /oauth/token でアクセストークン取得

### 認可コードグラント
* コード取得
  <http://localhost:8080/oauth/authorize?response_type=code&client_id=alexa-skill&redirect_uri=https%3a%2f%2falexa%2eamazon%2eco%2ejp%2fapi%2fskill%2flink%2fxxxxxxxx&scope=read+write&state=state-test>

* トークン取得

```bash
curl -H "accept: application/json" \
     -H "Authorization:Basic $(echo -n alexa-skill:alexa-skill-secret | openssl base64)" \
     -F "grant_type=authorization_code" \
     -F "code=xxxxxxx" \
     -F "state=state-test" \
     -F "redirect_uri=http://localhost:8080/xxx" \
    http://localhost:8080/oauth/token
```

### リソースオーナーパスワードクレデンシャル

* トークン取得

```bash
curl -X POST \
     -D - -s \
     -u "alexa-skill:alexa-skill-secret" \
     -d "grant_type=password" \
     -d "username=test@example.com" \
     -d "password=password" \
     http://localhost:8080/oauth/token
```

## APIコール

* create

```bash
curl -v \
     -X POST \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer xxxxxxxxx-yyyyyyyyyyyy-zzzzzzzzzzz" \
     -d '{"title":"Test Title","detail":"Test Detail","deadline":"2017-02-28"}' \
     http://localhost:8080/api/tasks
```

## read

```bash
curl -v \
     -H "Authorization: Bearer xxxxxxxxx-yyyyyyyyyyyy-zzzzzzzzzzz" \
     http://localhost:8080/api/tasks/1
```

```bash
curl -v \
     -H "Authorization: Bearer xxxxxxxxx-yyyyyyyyyyyy-zzzzzzzzzzz" \
     http://localhost:8080/api/tasks/1
```

## update

```bash
curl -v \
     -X PUT \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer xxxxxxxxx-yyyyyyyyyyyy-zzzzzzzzzzz" \
     -d '{"title":"Test Title Update","detail":"Test Detail","deadline":"2017-02-28"}' \
     http://localhost:8080/api/tasks/2
```

## delete

```bash
curl -v \
     -X DELETE \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer xxxxxxxxx-yyyyyyyyyyyy-zzzzzzzzzzz" \
     http://localhost:8080/api/tasks/2
```

## SQL

* アクセストークン見たいとき。md5でハッシュ化されてるよ

```sql
select
  token_id
 ,authentication_id
 ,user_name
 ,client_id
 ,refresh_token
from oauth_access_token;
```

* リダイレクトURL変更

```sql
update test.oauth_client_details set web_server_redirect_uri = 'https://alexa.amazon.co.jp/api/skill/link/xxxxxxxxxxxxxx,https://layla.amazon.com/api/skill/link/xxxxxxxxxxxxxxx,https://pitangui.amazon.com/api/skill/link/xxxxxxxxxxxxxx' where client_id = 'alexa-skill';
```