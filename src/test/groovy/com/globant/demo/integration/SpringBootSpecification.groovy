package com.globant.demo.integration

import com.globant.demo.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

@SpringBootTest(classes = [Application])
abstract class SpringBootSpecification extends Specification {

    @Autowired
    WebApplicationContext webApplicationContext
    MockMvc mvc

    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build()
    }
}
