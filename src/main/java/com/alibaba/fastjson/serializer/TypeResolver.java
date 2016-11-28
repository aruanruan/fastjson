package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.ParserConfig;

public interface TypeResolver {
	public String resolveAsString(SerializeConfig config, Class<?> type);
	public void reset(SerializeConfig config);
	public Class<?> resolveAsType(ParserConfig config, String typeName);
	public void reset(ParserConfig config);
}
