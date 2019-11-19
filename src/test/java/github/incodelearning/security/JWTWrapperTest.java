package github.incodelearning.security;

import org.junit.Test;

public class JWTWrapperTest {
    @Test
    public void testJwt() {
        String jwt = JWTWrapper.create("jwt", "{id:1001,name:john,role:admin}", 600000);
        System.out.println(jwt);
        System.out.println(JWTWrapper.parse(jwt));
        // {sub={id:1001,name:john,role:admin},
        // org=jessezhuang.github.io, old=b03221-35da-8fab-cef889, exp=1573887738, iat=1573887138, jti=jwt}
    }
}
