/* eslint-disable  */
const jose = require('jose');
const fs = require('fs');
const basePath = '../src/main/resources/';
const configFile = `${basePath}config.json`;
const jwkFIle = `${basePath}jwk.json`;

function generate() {
    const opts = { use: 'sig' };
    const jwk = jose.JWK.generateSync('RSA', 2048, opts);
    const pem = jwk.toPEM(true);
    fs.writeFileSync(configFile, JSON.stringify({
        privateKey: pem,
        privateKeyId: jwk.kid,
    }));
    fs.writeFileSync(jwkFIle, JSON.stringify(jwk.toJWK(true)));
    console.log(`Wrote ${configFile} and ${jwkFIle}`);
}

function validate(jwt) {
    const jwkStr = fs.readFileSync(jwkFIle);
    const jwkJSON = JSON.parse(jwkStr);
    const jwk = jose.JWK.asKey(jwkJSON);
    // JWT issued from a JAVA program..
    console.log(jose.JWT.verify(jwt, jwk));
}
var args = process.argv.slice(2);

switch (args[0]) {
    case 'generate':
        generate();
        break;
    case 'validate':
        validate(args[1]);
        break;
    default:
        console.log('Usuage: cmd [generate|validate <JWT>]');
}