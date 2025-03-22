package com.solarsync.crm;

import com.solarsync.crm.service.JwtUtilTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CrmApplicationTests {

	@Test
	void contextLoads() {
		JwtUtilTest jwtUtilTest = new JwtUtilTest();
	}

}
