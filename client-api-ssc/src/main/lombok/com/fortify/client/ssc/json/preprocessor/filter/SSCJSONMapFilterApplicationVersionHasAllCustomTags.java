/*******************************************************************************
 * (c) Copyright 2020 Micro Focus or one of its affiliates, a Micro Focus company
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
package com.fortify.client.ssc.json.preprocessor.filter;

import java.util.Arrays;
import java.util.List;

import com.fortify.client.ssc.annotation.SSCCopyToConstructors;
import com.fortify.client.ssc.api.query.builder.SSCApplicationVersionsQueryBuilder;
import com.fortify.client.ssc.api.query.builder.SSCEmbedDescriptor.EmbedType;
import com.fortify.util.rest.json.JSONList;
import com.fortify.util.rest.json.JSONMap;
import com.fortify.util.rest.json.preprocessor.filter.AbstractJSONMapFilter;
import com.fortify.util.rest.query.IRestConnectionQueryConfigAware;

/**
 * Filter SSC application versions based on whether the SSC application version contains
 * the configured custom tag name(s).
 * 
 * @author Ruud Senden
 *
 */
public class SSCJSONMapFilterApplicationVersionHasAllCustomTags extends AbstractJSONMapFilter implements IRestConnectionQueryConfigAware<SSCApplicationVersionsQueryBuilder> {
	private final List<String> customTagNames;
	private final EmbedType embedType;
	
	public SSCJSONMapFilterApplicationVersionHasAllCustomTags(MatchMode matchMode, String... customTagNames) {
		// For backward compatibility we use EmbedType.ONDEMAND by default
		this(matchMode, EmbedType.ONDEMAND, customTagNames);
	}
	
	public SSCJSONMapFilterApplicationVersionHasAllCustomTags(MatchMode matchMode, EmbedType embedType, String... customTagNames) {
		super(matchMode);
		this.embedType = embedType;
		this.customTagNames = Arrays.asList(customTagNames);
	}
	
	@Override
	protected boolean isMatching(JSONMap json) {
		List<String> avCustomTagNames = json.get("customTagsNamesOnly", JSONList.class).getValues("name", String.class);
		return avCustomTagNames.containsAll(customTagNames);
	}

	@Override @SSCCopyToConstructors
	public void setRestConnectionQueryConfig(SSCApplicationVersionsQueryBuilder currentBuilder) {
		currentBuilder.embedCustomTags("customTagsNamesOnly", embedType, "name");
	}
}
