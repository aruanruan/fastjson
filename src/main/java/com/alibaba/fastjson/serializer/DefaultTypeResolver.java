package com.alibaba.fastjson.serializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;

public class DefaultTypeResolver implements TypeResolver {
	
	private boolean typeRef = false;
	

	public boolean isTypeRef() {
		return typeRef;
	}

	public void setTypeRef(boolean typeRef) {
		this.typeRef = typeRef;
	}

	private AtomicInteger ref = new AtomicInteger(0);
	private Map<Class<?>, String> typeToIndex = new HashMap<Class<?>, String>();
	public String resolveAsString(SerializeConfig config, Class<?> type) {
		if(type == null) return null;
		String typeName = type.getName();
		if(!typeRef) return typeName;
		String index = typeToIndex.get(type);
		if(index != null){
			return index;
		}
		int r = ref.getAndIncrement();
		index = "#" + r;
		typeToIndex.put(type, index);
		return typeName + index;
	}

	public void reset(SerializeConfig config) {
		typeToIndex.clear();
		ref.set(0);
	}

	
	private Map<Integer, Class<?>> indexToType = new HashMap<Integer, Class<?>>();
	public Class<?> resolveAsType(ParserConfig config, String typeName) {
		if(typeName == null || typeName.length() == 0) return null;
		int pos = typeName.indexOf('#');
		if(pos > -1){
			int index = Integer.parseInt(typeName.substring(pos + 1));
			if(pos == 0){
				return indexToType.get(index);
			}else{
				String realTypeName = typeName.substring(0, pos);
				Class<?> type = TypeUtils.loadClass(realTypeName);
				if(type != null){
					indexToType.put(index, type);
				}
				return type;
			}
		}
		return TypeUtils.loadClass(typeName);
	}

	public void reset(ParserConfig config) {
		indexToType.clear();
	}

}
