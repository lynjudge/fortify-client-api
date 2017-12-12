/*******************************************************************************
 * (c) Copyright 2017 EntIT Software LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to 
 * whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY 
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 ******************************************************************************/
package com.fortify.api.ssc.connection.api;

import com.fortify.api.ssc.annotation.SSCRequiredActionsPermitted;
import com.fortify.api.ssc.connection.SSCAuthenticatingRestConnection;
import com.fortify.api.ssc.connection.api.query.builder.SSCApplicationVersionsQueryBuilder;
import com.fortify.api.util.rest.json.JSONMap;

public class SSCApplicationVersionAPI extends AbstractSSCAPI {
	public SSCApplicationVersionAPI(SSCAuthenticatingRestConnection conn) {
		super(conn);
	}
	
	@SSCRequiredActionsPermitted({"GET=/api/v\\d+/projectVersions"})
	public SSCApplicationVersionsQueryBuilder queryApplicationVersions() {
		return new SSCApplicationVersionsQueryBuilder(conn());
	}
	
	//@SSCRequiredActionsPermitted({"GET=/api/v\\d+/projectVersions"})
	public JSONMap getApplicationVersionById(String applicationVersionId) {
		return new SSCApplicationVersionsQueryBuilder(conn()).id(applicationVersionId).useCache(true).build().getUnique();
	}
	
	//@SSCRequiredActionsPermitted({"GET=/api/v\\d+/projectVersions"})
	public JSONMap getApplicationVersionByName(String applicationName, String versionName) {
		return new SSCApplicationVersionsQueryBuilder(conn()).applicationName(applicationName).versionName(versionName).useCache(true).build().getUnique();
	}
	
	//@SSCRequiredActionsPermitted({"GET=/api/v\\d+/projectVersions"})
	public JSONMap getApplicationVersionByNameOrId(String nameOrId, String separator) {
		return new SSCApplicationVersionsQueryBuilder(conn()).nameOrId(nameOrId, separator).useCache(true).build().getUnique();
	}
	
	/**
	 * @return Browser-accessible deep link for the current application version
	 */
	public final String getApplicationVersionDeepLink(String applicationVersionId) {
		return conn().getBaseUrl()+"html/ssc/index.jsp#!/version/"+applicationVersionId+"/fix";
	}
	
	public static void main(String[] args) {
		SSCAuthenticatingRestConnection conn = SSCAuthenticatingRestConnection.builder().uri("http://ssc:Admin123!@localhost:1710/ssc").build();
		SSCApplicationVersionAPI api = conn.api().applicationVersion();
		for ( int i = 0 ; i < 10 ; i++ ) {
			System.out.println(api.queryApplicationVersions().applicationName("WebGoat").paramFields("id", "name").useCache(true).build().getAll());
			System.out.println(api.queryApplicationVersions().id("6").useCache(true).build().getAll());
			System.out.println(api.queryApplicationVersions().applicationName("WebGoat").versionName("5.0").useCache(true).build().getUnique());
			System.out.println(api.getApplicationVersionByNameOrId("WebGoat:5.0", ":"));
			System.out.println(api.queryApplicationVersions().useCache(true).build().getAll());
		}
	}

}
