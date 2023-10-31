package kr.co.enders.ums.com.service;

import org.springframework.stereotype.Service;

@Service
public interface ForbiddenService {
	/**
	 * 금지어 API 처리 
	 * @param key
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public String getProhibitWordApi(String key, String text);
	
}
