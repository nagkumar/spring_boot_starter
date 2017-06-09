package org.bookpub.test.bdd;


import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:build/reports/cucumber"},
	glue = {
		"cucumber.api.spring",
		"classpath:org.bookpub.test.bdd"
	},
	monochrome = true)
public class RunCukeTests
{
}