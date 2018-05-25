package com.liuguanghui.littlereader;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuguanghui.littlereader.pojo.NovelVO;
import com.liuguanghui.littlereader.util.JsonResult;
import com.liuguanghui.littlereader.util.SearchResult;


public class TestNetJson {
	
	public static void main(String[] args) throws Exception {
		String jsonString ="hhhhhhh";
		System.out.println(jsonString);
		JsonResult jsonResult = JsonResult.formatToPojo(jsonString, SearchResult.class);
		if(jsonResult.getStatus() == 200) {
			SearchResult<NovelVO> searchResult = (SearchResult<NovelVO>) jsonResult.getData();
			List<NovelVO> list  = searchResult.getItemList();
			
			System.out.println(searchResult.getItemList().toString());
			System.out.println("----------------------");
			System.out.println(list);
			
			 
		    ObjectMapper mapper = new ObjectMapper( ); 
			
			String jsonStr = mapper.writeValueAsString(searchResult.getItemList());
		    /*List<SpiderNovel> users = mapper.reader().withType(new TypeReference<List<SpiderNovel>>() {})  
		      .readValue(jsonStr); */
		    
             JsonNode data = mapper.readTree(jsonStr);
            //JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = mapper.readValue(data.traverse(),
                        mapper.getTypeFactory().constructCollectionType(List.class, NovelVO.class));
            } 
			
            List<NovelVO> users = (List<NovelVO>) obj;
            
            for (NovelVO spiderNovel : users) {
				System.out.println(spiderNovel);
			} 
		}
	}
	
	
	class JsonParserImpl extends JsonParser{

		@Override
		public ObjectCodec getCodec() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setCodec(ObjectCodec c) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Version version() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public JsonToken nextToken() throws IOException, JsonParseException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonToken nextValue() throws IOException, JsonParseException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonParser skipChildren() throws IOException, JsonParseException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isClosed() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public JsonToken getCurrentToken() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCurrentTokenId() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasCurrentToken() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String getCurrentName() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonStreamContext getParsingContext() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonLocation getTokenLocation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonLocation getCurrentLocation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void clearCurrentToken() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public JsonToken getLastClearedToken() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void overrideCurrentName(String name) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getText() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public char[] getTextCharacters() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getTextLength() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getTextOffset() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasTextCharacters() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Number getNumberValue() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public NumberType getNumberType() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getIntValue() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getLongValue() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public BigInteger getBigIntegerValue() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public float getFloatValue() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double getDoubleValue() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public BigDecimal getDecimalValue() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getEmbeddedObject() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public byte[] getBinaryValue(Base64Variant bv) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getValueAsString(String def) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
