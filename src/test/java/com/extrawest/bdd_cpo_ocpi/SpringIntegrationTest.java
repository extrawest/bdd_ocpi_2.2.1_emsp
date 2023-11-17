package com.extrawest.bdd_cpo_ocpi;

import com.extrawest.bdd_cpo_ocpi.cucumber.StepsDefinitionTest;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value = "com.extrawest.bdd_cpo_ocpi")
@SpringBootTest(classes = StepsDefinitionTest.class)
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources", glue = "com.extrawest.bdd_cpo_ocpi.cucumber.StepsDefinitionTest")
public class SpringIntegrationTest {
}
