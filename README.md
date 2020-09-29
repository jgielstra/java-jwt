# Purpose
Simple example using RSA private key to sign a JWT.

This example includes a JWK `src/resources/jwk.json` that matches the private key created via https://github.com/panva/jose

This JWK can be used to validate the JWT for example in Istio/Envoy/Ambassador GW JWT auth.

# Running

Build it 
`gradle build`

Running it 
`gradle run`

Will dump a JWT to the console