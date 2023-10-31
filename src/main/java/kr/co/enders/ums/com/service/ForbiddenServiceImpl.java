package kr.co.enders.ums.com.service;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forbidden.forbiddata.ForbiddenData;

import kr.co.enders.util.PropertiesUtil;

@Service
public class ForbiddenServiceImpl implements ForbiddenService {
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private PropertiesUtil properties;
	
	@Override
	public String getProhibitWordApi(String title, String text) {
		ForbiddenData forbiddenData = new ForbiddenData();
		String result = "";
		try {
			//서버용
			//result = forbiddenData.getProhibitWordApi(title, text);
			//개발용(정상)
			result = forbiddenData.getProhibitWordApi(title, text);
			//개발용(에러)
			//result = forbiddenData.getProhibitWordApiError(title, text);
		} catch(Exception e) {
			result = forbiddenData.makeProhibitWordError();
		}
		return result;
	}
}
