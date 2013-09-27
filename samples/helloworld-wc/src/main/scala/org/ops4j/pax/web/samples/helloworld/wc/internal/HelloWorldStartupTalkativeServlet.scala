/* Copyright 2013 Guillaume Yziquel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.web.samples.helloworld.wc.internal

import java.io.IOException

import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletResponse

/**
 * Hello World Servlet.
 * 
 * @author Guillaume Yziquel
 * @since 4.0.0, September 25, 2013
 */
class HelloWorldStartupTalkativeServlet extends HttpServlet
{

  @throws[ServletException]
  @throws[IOException]
  protected def doGet(request: HttpServlet, response: HttpServletResponse)
  {
		if (HelloWorldStartupSilentServlet.isActive)
		{
      List( "<body align='center'>",
            "<h1>Silent Servlet activated</h1>",
            "</body>"
      ).foreach(response.getWriter.println(_))
		} else
			throw new ServletException("Silent Servlet is not active.")
	}

}
