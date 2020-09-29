# Purpose
Simple example using RSA private key to sign a JWT.

This example includes noed app to generate:
  - JWK `src/resources/jwk.json` that matches the private key created via https://github.com/panva/jose
  - CONFIG `src/resources/config.json` that JAVA project will use to generate a JWT
This JWK can be used to validate the JWT for example in Istio/Envoy/Ambassador GW JWT auth.

I did across node.js & JAVA to test portability between runtimes.. 
# Running 
## Node app 
Generates JWK & config to allow clients to sign jwts
JWK `src/main/resources/jwk.json` -> Istio for validation
CONFIG `src/main/resources/config.json` -> User download to laptop/server for issuing JWTS

```
cd node-jwt
npm install
node jwker generate
```
## Java APP sign JWT
From repo root

Build it 
`gradle build`

Running and generate a JWT 
`gradle run`

## Validate nodejs
```
   cd node-jwt
   node jwker validate <JWT from ^^ >
   ```

