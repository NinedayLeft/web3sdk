package org.fisco.bcos.channel.test.precompile;

import java.math.BigInteger;

import org.fisco.bcos.web3j.protocol.ObjectMapperFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Common {
	
    public static String transferToJson(int code) throws JsonProcessingException{
    	String msg = "";
    	switch (code)
    	{
    		case -1:
    			msg = "non-authorized";
    			break;
    		case -33:
    			msg = "table name and address exist";
    			break;
    		case -34:
    			msg = "table name and address does not exist";
    			break;
    		case -40:
    			msg = "invalid configuration values";
    			break;
    		case -41:
    			msg = "invalid nodeID";
    			break;
    		case -50:
    			msg = "address and version exist";
    			break;
    		default:
    			msg = "success";
    			break;	
    	}
    	ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
        return mapper.writeValueAsString(new OutJson(code, msg));
    }
    
	public static String getJsonStr(String output) throws JsonProcessingException {
		int code = 0;
		if ("f".equals(output.substring(2, 3))) {
			code = -1;
		} else {
			code = new BigInteger(output.substring(2, output.length()), 16).intValue() - 256;
			if(code < -200)
			{
				code = Integer.valueOf(output.substring(2), 16).intValue();
			}
		}
		return transferToJson(code);
	}
}
