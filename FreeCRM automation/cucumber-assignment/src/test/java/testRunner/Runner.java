package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
		features= {".//src/test/resources/features/PositiveTestCase.feature"},
		glue= {"stepDefinition"},
		monochrome= true,
//		dryRun= true,
		plugin= {"pretty",
				"html: test-output"
				}
		)

public class Runner extends AbstractTestNGCucumberTests  {

}
