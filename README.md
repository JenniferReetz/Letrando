# 🎶 Letrando

**Letrando** é uma aplicação back-end desenvolvida por [Jennifer Reetz](https://github.com/JenniferReetz) que integra a API do Spotify com uma API de letras de músicas para fornecer, de maneira simples e rápida, **letras completas de músicas diretamente a partir do nome da faixa**.

> Projeto pessoal idealizado, planejado e desenvolvido 100% por mim como prática de backend e integração com APIs externas.

---

## :door: Conteúdo

- [Sobre](#sobre)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Como executar o projeto](#como-executar)
- [Autenticação](#autenticação)
- [Endpoints](#endpoints)
- [Insomnia Collection](#insomnia-collection)
  
---

<a name="sobre"></a>
## 💡 Sobre o projeto

Este projeto surgiu da vontade de explorar integrações com APIs externas, trabalhar com autenticação JWT e desenvolver uma aplicação real e útil que entrega valor mesmo sem front-end.

> Foi um desafio muito legal de fazer sozinha e me proporcionou bastante aprendizado com Spring Security, tratamento de erros com APIs externas e estruturação de uma aplicação RESTful robusta.

---

<a name="funcionalidades"></a>
## ✨ Funcionalidades

- 🔐 Autenticação via JWT
- 🧑 Cadastro e login de usuários
- 🎧 Integração com a API do Spotify
- 📄 Busca e exibição de letras de músicas (via API `lyrics.ovh`)
- 📦 Projeto estruturado com Spring Boot
- 🧪 Testes básicos de autenticação e busca

---

<a name="tecnologias"></a>
## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Spotify Web API
- lyrics.ovh API
- H2 Database (para testes locais)
- JWT para autenticação
- Maven

---

<a name="como-executar"></a>
## ▶️ Como executar

### Pré-requisitos

- Java 17+
- Maven
- IDE de sua preferência

### Rodando a aplicação

```bash
git clone https://github.com/JenniferReetz/Letrando.git
cd Letrando
./mvnw spring-boot:run
```
<a name="autenticação"></a>
## 🔐 Autenticação JWT
<details>
  <summary><strong>COMO FAZER?</strong></summary>
Para acessar os endpoints protegidos, você precisa estar autenticado e enviar o token JWT no cabeçalho da requisição como Bearer Token.   

#### Faça login com:
```http
POST /auth/login
```
#### Exemplo de resposta:
```json
{
  "email": "usuario@email.com",
  "password": "senha123"
}
```
#### Use esse token no cabeçalho Authorization de todas as requisições protegidas:
```makefile
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
#### 🛡️ Exemplo com curl
```bash
curl -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  http://localhost:8080/lyrics?track=Shape%20of%20You
```
</details> 


<a name="endpoints"></a>
## :pushpin: Endpoints

<details>
<summary><strong>🔵 GET</strong></summary>

### 📄 Obter letras de músicas

```http
GET http://localhost:8080/lyrics?songName=Die+With+A+Smile
```
##### Exemplo de resposta:
```json
{
	"track": "Die With A Smile",
	"artist": "Lady Gaga",
	"lyrics": "(Ooh, ooh)\r\nI, I just woke up from a dream\r\nWhere you and I had to say goodbye\r\nAnd I don't know what it all means\r\nBut since I survived, I realized\n\n\n\nWherever you go, that's where I'll follow\n\nNobody's promised tomorrow..."
}
```
#### Buscar músicas
```http
  GET http://localhost:8080/spotify/search?q=Die+With+A+Smile
```
##### Exemplo de resposta:
```json
[
	{
		"id": "2plbrEY59IikOBgBGLjaoe",
		"name": "Die With A Smile",
		"album": "Die With A Smile",
		"artist": "Lady Gaga"
	},
	{
		"id": "78mE33YpoWqMQTwgUQRhyo",
		"name": "Die With A Smile",
		"album": "Die With A Smile (Main + Instrumental)",
		"artist": "Lady Gaga"
	},
	mais 8 resultados...
]
```
#### Buscar músicas da playlist do usuário autenticado
```http
  GET http://localhost:8080/playlists/user
```
##### Exemplo de resposta:
```json
[
	{
		"id": 3,
		"name": "Opus Dei",
		"userId": 2,
		"musicIds": [
			"72FVh1OAKWANKJosPdrBkl"
		]
	}
]
```
#### Buscar músicas de uma playlist
```http
  GET http://localhost:8080/playlists/1/musics
```
##### Exemplo de resposta:
```json
[
	{
		"id": "0SiywuOBRcynK0uKGWdCnn",
		"name": "Bad Romance",
		"album": "The Fame Monster (Deluxe Edition)",
		"artist": "Lady Gaga"
	},
	{
		"id": "0qMip0B2D4ePEjBJvAtYre",
		"name": "紅蓮華",
		"album": "LEO-NiNE",
		"artist": "LiSA"
	}
]
```

</details>

<details>
  <summary>
    <strong>🟢 POST</strong>
  </summary>

#### Sign-Up

```http
  POST http://localhost:8080/auth/signup
```

##### Cadastrar um Usuário:
| Chave   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `username` | `string` | **Obrigatório**. Nome do usuário |
| `password` | `string` | **Obrigatório**. Senha do usuário |

##### Exemplo de criação de um cliente:
```json
{
	"username":"usuario",
	"password":"senha1234"
}
```

##### Exemplo de resposta:
```json
Usuário criado com sucesso
```
#### Login

```http
  POST http://localhost:8080/auth/login
```

##### Entrar em um Usuário:
| Chave   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `username` | `string` | **Obrigatório**. Nome do usuário |
| `password` | `string` | **Obrigatório**. Senha do usuário |

##### Exemplo do login de um cliente:
```json
{
	"username":"usuario",
	"password":"senha1234"
}
```

##### Exemplo de resposta:
```json
{
	"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdGVmYW5pZSIsImlhdCI6MTc0NDkzNzM0NiwiZXhwIjoxNzQ0OTQwOTQ2fQ.Ls9RKIwYiAWxhpTH_bykb_7CStzqJu8g46mrnLFnRUk"
}
```
#### Cadastrar uma Playlist

```http
  POST http://localhost:8080/playlists
```

##### Criar uma playlist:
| Chave   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `name` | `string` | **Obrigatório**. Nome da playlist |
| `userId` | `number` | **Obrigatório**. id do usuário |
| `musicIds` | `string` | **Obrigatório**. id das músicas |
##### Exemplo do login de um cliente:
```json
{
  "name": "Opus Dei",
  "userId": 2,
  "musicIds": [
    "72FVh1OAKWANKJosPdrBkl"
  ]
}
```
##### Exemplo de resposta:
```json
{
	"id": 3,
	"name": "Opus Dei",
	"userId": 2,
	"musicIds": [
		"72FVh1OAKWANKJosPdrBkl"
	]
}
```
</details>

<details>
  <summary>
    <strong>🟠 PUT</strong>
  </summary>

#####  Atualiza a playlist
```http
  http://localhost:8080/playlists/1
```

##### Atualizar uma playlist:
| Chave   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `name` | `string` | **Obrigatório**. Nome da playlist |
| `userId` | `number` | **Obrigatório**. id do usuário |
| `musicIds` | `string` | **Obrigatório**. id das músicas |
##### Exemplo de requisição:
```json
{
  "name": "Opus Dei",
  "userId": 2,
  "musicIds": [
    "72FVh1OAKWANKJosPdrBkl"
  ]
}
```
##### Exemplo de resposta:
```json
{
	"id": 3,
	"name": "Opus Dei",
	"userId": 2,
	"musicIds": [
		"72FVh1OAKWANKJosPdrBkl"
	]
}
```
</details>

<details>
  <summary>
    <strong>🔴 DELETE</strong>
  </summary>
	
####  Deleta uma playlist
```http
  http://localhost:8080/playlists/1
```

</details>

<a name="insomnia-collection"></a>
## 🔗 Insomnia Collection
<details>
<summary> <strong>HAR</strong></summary> 
Para testar os endpoints da API, importe o arquivo `.har` no Insomnia:

📁 [Download da Collection](./collection/Insomnia_2025-04-18.har)

**Como importar no Insomnia:**
1. Abra o Insomnia
2. Vá em `File` > `Import` > `From File`
3. Selecione o arquivo `Insomnia_2025-04-18.har`
4. Pronto! Agora é só testar os endpoints da API
</details>
