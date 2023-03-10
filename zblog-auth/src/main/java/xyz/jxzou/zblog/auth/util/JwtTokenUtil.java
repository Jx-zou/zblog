package xyz.jxzou.zblog.auth.util;


import com.alibaba.fastjson2.JSON;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import org.springframework.stereotype.Component;
import xyz.jxzou.zblog.auth.domain.dto.Payload;
import xyz.jxzou.zblog.auth.domain.vo.JwtUser;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.exception.model.exception.ServletException;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

/**
 * The type Jwt token util.
 */
@Component
public class JwtTokenUtil {

    /**
     * Generate string.
     *
     * @param payloadDto    the payload dto
     * @param rsaPrivateKey the rsa private key
     * @return the string
     * @throws JOSEException the jose exception
     */
    public String generate(Payload payloadDto, RSAPrivateKey rsaPrivateKey) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader
                .Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT).build();
        com.nimbusds.jose.Payload payload = new com.nimbusds.jose.Payload(JSON.toJSONString(payloadDto));
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        JWSSigner jwsSigner = new RSASSASigner(rsaPrivateKey);
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }

    /**
     * Verify payload dto.
     *
     * @param token        the token
     * @param rsaPublicKey the rsa public key
     * @return the payload dto
     * @throws ParseException   the parse exception
     * @throws ServletException the servlet exception
     * @throws JOSEException    the jose exception
     */
    public Payload verify(String token, RSAPublicKey rsaPublicKey) throws ParseException, ServletException, JOSEException {
        JWSObject jwsObject = JWSObject.parse(token);
        RSASSAVerifier verifier = new RSASSAVerifier(rsaPublicKey);
        if (!jwsObject.verify(verifier)) {
            throw ServletResponseEnum.AUTH_FAILED_ERROR.newException();
        }
        return JSON.parseObject(jwsObject.getPayload().toString(), Payload.class);
    }

    /**
     * Gets username.
     *
     * @param token        the token
     * @param rsaPublicKey the rsa public key
     * @return the username
     * @throws ParseException   the parse exception
     * @throws ServletException the servlet exception
     * @throws JOSEException    the jose exception
     */
    public String getUsername(String token, RSAPublicKey rsaPublicKey) throws ParseException, ServletException, JOSEException {
        String jsonJwtUser = this.verify(token, rsaPublicKey).getUsername();
        return JSON.parseObject(jsonJwtUser, JwtUser.class).getUsername();
    }
}
