package org.ops4j.pax.web.itest;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.OptionUtils.combine;

import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Filter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.web.itest.support.SimpleFilter;
import org.ops4j.pax.web.itest.support.SimpleOnlyFilter;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Toni Menzel (tonit)
 * @since Mar 3, 2009
 */
@RunWith(PaxExam.class)
public class WhiteboardFilterOnlyIntegrationTest extends ITestBase {

	@Configuration
	public static Option[] configure() {
		return combine(
				configureJetty());/*,
				mavenBundle().groupId("org.ops4j.pax.web.samples")
						.artifactId("whiteboard").version(getProjectVersion())
						.noStart());*/

	}

	@Test
	@Ignore
	public void testWhiteBoardFiltered() throws Exception {
		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put("urlPatterns", "*");
//		props.put("alias", "/testfilter");
		SimpleOnlyFilter simpleFilter = new SimpleOnlyFilter();
		ServiceRegistration<Filter> filter = bundleContext.registerService(
				Filter.class, simpleFilter, props);

		testWebPath("http://127.0.0.1:8181/testFilter",
				"Hello Whiteboard Filter");

		filter.unregister();

	}

}
