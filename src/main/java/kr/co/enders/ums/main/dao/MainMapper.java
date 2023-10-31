/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.07
 * 설명 : 메인화면 매퍼
 */
package kr.co.enders.ums.main.dao;

import java.util.List;

import kr.co.enders.ums.main.vo.EmsMainVO;
import kr.co.enders.ums.main.vo.MenuVO;
import kr.co.enders.ums.sys.acc.vo.ServiceVO;
import kr.co.enders.ums.sys.acc.vo.SysMenuVO;
import kr.co.enders.ums.sys.acc.vo.UserVO;

public interface MainMapper {
	/**
	 * 상단 메뉴 목록 조회
	 * @param uilang
	 * @return
	 * @throws Exception
	 */
	public List<MenuVO> getTopMenuList(String uilang) throws Exception;

	/**
	 * 메뉴 기본 정보 조회
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	public SysMenuVO getMenuBasicInfo(String menuId) throws Exception;
	
	/**
	 * 각 서비스 기본 화면에서 첫번째 메뉴 조회
	 * @param sysMenuVO
	 * @return
	 * @throws Exception
	 */
	public SysMenuVO getServiceUserMenu(SysMenuVO sysMenuVO) throws Exception;
	
	/**
	 * 메뉴에 대한 사용자 권한 확인
	 * @param sysMenuVO
	 * @return
	 * @throws Exception
	 */
	public SysMenuVO getUserMenuAuth(SysMenuVO sysMenuVO) throws Exception;
	
	/**
	 * 서비스에대한 사용자 권한 확인
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ServiceVO> getUserService(UserVO userVO) throws Exception;
	
	/**
	 * 시스템메뉴 정보에 등록된 경로인지 확인
	 * @param sourcePath
	 * @return
	 * @throws Exception
	 */
	public SysMenuVO getSourcePathMenu(String sourcePath) throws Exception;
	
	/**
	 * 이메일 시스템 홈 관리자 정보
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getUserInfo(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 결제 정보
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getUserApprov(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 일별 총발송,발송성공,발송실패
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getDayMailSummary(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 일별 열람한 메일
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getDayMailOpen(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 일별 세부에러
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getDayMailDetailErr(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 일별 도메인별
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getDayMailDomain(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 일별 캠폐인별
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getDayMailCampain(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 기간별 발송현황 일별
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getMailSendInfoDay(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 기간별 발송현황 월별
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getMailSendInfoMons(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 일별 발송 일정
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getDayMailSendSch(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 사용제 메뉴
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public List<EmsMainVO> getUserMenu(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 퀵메뉴 저장
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public int insertMainQuickMenu(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 퀵메뉴 삭제
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public int deleteMainQuickMenu(EmsMainVO emsMainVo) throws Exception;
	
	/**
	 * 이메일 시스템 홈 퀵메뉴 전체삭제
	 * @param emsMainVo
	 * @return
	 * @throws Exception
	 */
	public int deleteAllMainQuickMenu(EmsMainVO emsMainVo) throws Exception;
}
