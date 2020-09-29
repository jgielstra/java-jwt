import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.*;
import com.cedarsoftware.util.io.JsonObject;
import com.cedarsoftware.util.io.JsonReader;

public class CreateJWT {
    private static Map readResourceAsMap(String filename) throws FileNotFoundException {
        File file = new File(
                CreateJWT.class.getClassLoader().getResource(filename).getFile()
        );
        return JsonReader.jsonToMaps(new FileInputStream(file), new HashMap<String, Object>() {
        });
    }
    /*
        Reads key in pkcs8 format into JAVA's Privatekey
     */
    private static PrivateKey parsePrivateKey(String pkcs8) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyContent = pkcs8.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
        KeyFactory kf = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        return kf.generatePrivate(keySpecPKCS8);
    }
    public static void main(String[] args) {
        try {
            Map config = readResourceAsMap("config.json");
            long now = System.currentTimeMillis();
            RSAPrivateKey privateKey = (RSAPrivateKey)parsePrivateKey((String)config.get("privateKey"));
            String privateKeyId = (String)config.get("privateKeyId");
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            String signedJwt = JWT.create()
                    .withKeyId(privateKeyId)
                    .withIssuer("serviceaccount@example.com")
                    .withSubject("serviceaccount@example.com")
                    .withAudience("api.example.com")
                    .withIssuedAt(new Date(now))
                    .withExpiresAt(new Date(now + 3600 * 1000L))
                    .sign(algorithm);
            System.out.println(signedJwt);
        } catch( Exception ex) {
            ex.printStackTrace();
        }

    }
}
