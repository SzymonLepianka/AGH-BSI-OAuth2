{
	"info": {
		"_postman_id": "f214a564-2c32-47a7-889c-48a400798df5",
		"name": "OAuth2 DP",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
	},
	"item": [
		{
			"name": "login + code",
			"request": {
				"method": "POST",
				"header": [],
				"url": {
					"raw": "http://localhost:8081/web/login?clientID=2&username=slepianka99&password=password",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8081",
					"path": [
						"web",
						"login"
					],
					"query": [
						{
							"key": "clientID",
							"value": "2"
						},
						{
							"key": "username",
							"value": "slepianka99"
						},
						{
							"key": "password",
							"value": "password"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "dodawanie użytkownika",
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"auth": {
					"type": "basic",
					"basic": [
						{
							"key": "username",
							"value": "linda",
							"type": "string"
						},
						{
							"key": "password",
							"value": "password123",
							"type": "string"
						}
					]
				},
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "    {\r\n        \"studentName\": \"Alex Gomes\"\r\n    }",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8081/testApi/users/add?birth_date=1999-09-09&email=slepianka@wp.pl99&first_name=Szymek99&is_developer=false&password=password&phone_number=123456798&surname=Lepianka99&username=slepianka99",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8081",
					"path": [
						"testApi",
						"users",
						"add"
					],
					"query": [
						{
							"key": "birth_date",
							"value": "1999-09-09"
						},
						{
							"key": "email",
							"value": "slepianka@wp.pl99"
						},
						{
							"key": "first_name",
							"value": "Szymek99"
						},
						{
							"key": "is_developer",
							"value": "false"
						},
						{
							"key": "password",
							"value": "password"
						},
						{
							"key": "phone_number",
							"value": "123456798"
						},
						{
							"key": "surname",
							"value": "Lepianka99"
						},
						{
							"key": "username",
							"value": "slepianka99"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "dodawanie apki hardcoded",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/testApi/clients/add",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"testApi",
						"clients",
						"add"
					]
				}
			},
			"response": []
		},
		{
			"name": "token for code",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8081/api/createToken?clientID=2&authCode=zLTo0JOSmi",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8081",
					"path": [
						"api",
						"createToken"
					],
					"query": [
						{
							"key": "clientID",
							"value": "2"
						},
						{
							"key": "authCode",
							"value": "zLTo0JOSmi"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "refresh token",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8081/api/refreshToken?clientID=1&refreshToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDk4ODQ0MDQsImFjY2Vzc190b2tlbl9pZCI6ODl9.NKo-9nwOQRUl1zGBclaj4BsBhrivlyLaj5iFvKvvIYo",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8081",
					"path": [
						"api",
						"refreshToken"
					],
					"query": [
						{
							"key": "clientID",
							"value": "1"
						},
						{
							"key": "refreshToken",
							"value": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDk4ODQ0MDQsImFjY2Vzc190b2tlbl9pZCI6ODl9.NKo-9nwOQRUl1zGBclaj4BsBhrivlyLaj5iFvKvvIYo"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "validate token",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8081/api/validateToken?clientID=1&accessToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRJRCI6MiwiaWF0IjoxNjEwMjg3NjcxLCJleHAiOjE2MTAyODg1NzEsInNjb3BlcyI6InVzZXJfYmlydGhkYXRlLHVzZXJfZW1haWwsdXNlcl9maXJzdG5hbWUsdXNlcl9waG9uZW51bWJlcix1c2VyX3N1cm5hbWUsdXNlcl91c2VybmFtZSIsInN1YiI6IjEiLCJ1c2VybmFtZSI6InNsZXBpYW5rYTIifQ.fvmLS4OzbA9tyyXNb5ACSOwQx6Bc6CgKFts1mbUrpL8",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8081",
					"path": [
						"api",
						"validateToken"
					],
					"query": [
						{
							"key": "clientID",
							"value": "1"
						},
						{
							"key": "accessToken",
							"value": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRJRCI6MiwiaWF0IjoxNjEwMjg3NjcxLCJleHAiOjE2MTAyODg1NzEsInNjb3BlcyI6InVzZXJfYmlydGhkYXRlLHVzZXJfZW1haWwsdXNlcl9maXJzdG5hbWUsdXNlcl9waG9uZW51bWJlcix1c2VyX3N1cm5hbWUsdXNlcl91c2VybmFtZSIsInN1YiI6IjEiLCJ1c2VybmFtZSI6InNsZXBpYW5rYTIifQ.fvmLS4OzbA9tyyXNb5ACSOwQx6Bc6CgKFts1mbUrpL8"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "revoke token",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/revokeToken?clientID=1&accessToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRJRCI6MiwiaWF0IjoxNjA5ODkxMTc4LCJleHAiOjE2MDk4OTIwNzgsInNjb3BlcyI6InVzZXJfYmlydGhkYXRlLHVzZXJfZW1haWwsdXNlcl9maXJzdG5hbWUsdXNlcl9waG9uZW51bWJlcix1c2VyX3N1cm5hbWUsdXNlcl91c2VybmFtZSIsInN1YiI6IjEifQ.M03fPIL6nxJKRjrqksZwhQ1IFhKTOtf2ePMM0Q8Bka8",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"revokeToken"
					],
					"query": [
						{
							"key": "clientID",
							"value": "1"
						},
						{
							"key": "accessToken",
							"value": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRJRCI6MiwiaWF0IjoxNjA5ODkxMTc4LCJleHAiOjE2MDk4OTIwNzgsInNjb3BlcyI6InVzZXJfYmlydGhkYXRlLHVzZXJfZW1haWwsdXNlcl9maXJzdG5hbWUsdXNlcl9waG9uZW51bWJlcix1c2VyX3N1cm5hbWUsdXNlcl91c2VybmFtZSIsInN1YiI6IjEifQ.M03fPIL6nxJKRjrqksZwhQ1IFhKTOtf2ePMM0Q8Bka8"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "revoke grant type",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/revokeGrantType?clientID=1&authCode=CX3MDWpiM",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"revokeGrantType"
					],
					"query": [
						{
							"key": "clientID",
							"value": "1"
						},
						{
							"key": "authCode",
							"value": "CX3MDWpiM"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "get user data",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/getUserData?clientID=2&accessToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRJRCI6MiwiaWF0IjoxNjA5ODkxNzExLCJleHAiOjE2MDk4OTI2MTEsInNjb3BlcyI6InVzZXJfYmlydGhkYXRlLHVzZXJfZW1haWwsdXNlcl9maXJzdG5hbWUsdXNlcl9waG9uZW51bWJlcix1c2VyX3N1cm5hbWUsdXNlcl91c2VybmFtZSIsInN1YiI6IjEifQ.ZsCFs1Ti52EhfxKCHsRZuuHL4_0IlGCxOBBS2GHSYV0",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"getUserData"
					],
					"query": [
						{
							"key": "clientID",
							"value": "2"
						},
						{
							"key": "accessToken",
							"value": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbGllbnRJRCI6MiwiaWF0IjoxNjA5ODkxNzExLCJleHAiOjE2MDk4OTI2MTEsInNjb3BlcyI6InVzZXJfYmlydGhkYXRlLHVzZXJfZW1haWwsdXNlcl9maXJzdG5hbWUsdXNlcl9waG9uZW51bWJlcix1c2VyX3N1cm5hbWUsdXNlcl91c2VybmFtZSIsInN1YiI6IjEifQ.ZsCFs1Ti52EhfxKCHsRZuuHL4_0IlGCxOBBS2GHSYV0"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "connection with IO-Backend",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/users/slepianka2",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"users",
						"slepianka2"
					]
				}
			},
			"response": []
		}
	]
}