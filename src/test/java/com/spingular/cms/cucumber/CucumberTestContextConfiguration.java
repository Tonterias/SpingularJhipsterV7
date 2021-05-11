package com.spingular.cms.cucumber;

import com.spingular.cms.SpingularApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = SpingularApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
