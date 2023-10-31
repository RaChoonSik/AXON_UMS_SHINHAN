/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.17
 * 설명 : 공통 Controller
 */
package kr.co.enders.ums.com.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasoo.adk.packager.WorkPackager;

import org.mozilla.universalchardet.UniversalDetector;

import kr.co.enders.ums.com.service.CodeService;
import kr.co.enders.ums.com.service.CryptoService;
import kr.co.enders.ums.com.vo.CodeVO;
import kr.co.enders.ums.com.vo.DownloadVO;
import kr.co.enders.ums.com.vo.UploadVO;
import kr.co.enders.util.CaptchaUtil;
import kr.co.enders.util.PropertiesUtil;
import kr.co.enders.util.StringUtil;
import nl.captcha.Captcha;

@Controller
@RequestMapping("/com")
public class ComController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Autowired
	private PropertiesUtil properties;
	
	/**
	 * 사용자 목록 조회
	 * @param metaJoinVO
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getUserList")
	public ModelAndView getUserList(@ModelAttribute CodeVO codeVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("getUserList deptNo = " + codeVO.getDeptNo());
		
		// 사용자 목록 조회
		CodeVO userVO = new CodeVO();
		userVO.setDeptNo(codeVO.getDeptNo());
		userVO.setStatus("000");
		List<CodeVO> userList = null;
		try {
			userList = codeService.getUserList(userVO);
		} catch(Exception e) {
			logger.error("codeService.getUserList error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userList", userList);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	/**
	 * 파일 업로드 화면을 출력한다.
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/uploadP")
	public String goUploadMain(@ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUpload ext = " + uploadVO.getExt());
		
		model.addAttribute("upload", uploadVO);
		
		return "com/uploadP";
	}
	
	/**
	 * 파일을 지정된 경로로 업로드 한다.
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/upload")
	public String goUploadProc(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadProc folder    = " + uploadVO.getFolder());
		logger.debug("goUploadProc title     = " + uploadVO.getTitle());
		logger.debug("goUploadProc charset   = " + uploadVO.getCharset());
		logger.debug("goUploadProc formName  = " + uploadVO.getFormName());
		logger.debug("goUploadProc rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadProc vFileName = " + uploadVO.getvFileName());
		logger.debug("goUploadProc inputType = " + uploadVO.getInputType());
		
		// 업로드 폴더
		String folder = uploadVO.getFolder();
		
		// 파일 디렉토리 체크, 없으면 생성
		String addressFileDirStr  = properties.getProperty("FILE.UPLOAD_PATH") + "/" + folder;
		
		logger.debug("goUploadProc addressFileDirStr = " + addressFileDirStr);
		
		File addressFileDir = new File(addressFileDirStr);
		if(!addressFileDir.exists()) {
			addressFileDir.mkdirs();
		}
		
		// 업로드 파일 처리, 파일이름을 yyyyMMddHHmmss_idx 형식으로 변경하여 저장.
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String oldFileName = null;
		String newFileName = null;
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				oldFileName		= multipartFile.getOriginalFilename();
				String newFileLong	= Long.toString(System.currentTimeMillis());
				newFileName = newFileLong + "-" + oldFileName.replaceAll(" ", "_");
				String fileLocNm	= addressFileDirStr + "/" + newFileName;
				
				logger.debug("goUploadProc oldFileName = " + oldFileName);
				logger.debug("goUploadProc newFileName = " + newFileName);
				
				try {
					File file = new File(fileLocNm);
					multipartFile.transferTo(file);
				} catch(Exception e) {
					
				}
			}
		}

		model.addAttribute("result", "Success");
		model.addAttribute("uploadVO", uploadVO);
		model.addAttribute("oldFileName", oldFileName);
		model.addAttribute("newFileName", newFileName);
		
		return "com/upload";
	}
	
	/**
	 * 파일을 다운로드 한다.
	 * @param downloadVO
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/down")
	public void fileDownload(@ModelAttribute DownloadVO downloadVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("fileDownload downType = " + downloadVO.getDownType());
		
		String fileName = "";
		String filePath = "";
		
		// 파일연동 샘플 다운로드
		if("003".equals(downloadVO.getDownType())) {
			fileName = "sample01.csv";
			filePath = properties.getProperty("FILE.UPLOAD_PATH") + "/sample/" + fileName; 
		
		// 파일연동 업로드한 파일 다운로드(수신자그룹 파일연동)
		} else if("002".equals(downloadVO.getDownType())) {
			logger.debug("fileDownload tempFlPath = " + downloadVO.getTempFlPath());
			logger.debug("fileDownload segFlPath = " + downloadVO.getSegFlPath());
			
			fileName = downloadVO.getTempFlPath();
			filePath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + downloadVO.getSegFlPath();
		
		// 메일 첨부파일 다운로드
		} else if("009".equals(downloadVO.getDownType())) {
			logger.debug("fileDownload attachNm   = " + downloadVO.getAttachNm());
			logger.debug("fileDownload attachPath = " + downloadVO.getAttachPath());
			
			fileName = downloadVO.getAttachNm();
			filePath = properties.getProperty("FILE.UPLOAD_PATH") + "/" + downloadVO.getAttachPath();
		}
		
		logger.debug("fileDownload fileName = " + fileName);
		logger.debug("fileDownload filePath = " + filePath);
		
		File downTargetFile = new File(filePath);
		
		if (!downTargetFile.exists()) {
			logger.error("fileDownload filePath = " + filePath + " not exist");
		}  
		//byte fileBytes[] = FileUtils.readFileToByteArray(new File(filePath));
		byte fileBytes[] = FileUtils.readFileToByteArray(downTargetFile);
		
		response.setContentType("application/octet-stream");
		response.setContentLength(fileBytes.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8") + "\";");
		response.setHeader("Content-Trasfer-Encoding", "binary");
		response.getOutputStream().write(fileBytes);
		
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	/**
	 * 파일을 지정된 경로로 업로드 한다.(디자인 적용으로 추가)
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	
	@RequestMapping(value="/uploadSegFile")
	public ModelAndView goUploadSegFile(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadSegFile folder = " + uploadVO.getFolder());
		logger.debug("goUploadSegFile rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadSegFile vFileName = " + uploadVO.getvFileName());
		
		// 업로드 폴더
		String folder = uploadVO.getFolder();
		
		// File 관련 정보 
		String encodingType= StringUtil.setNullToString(properties.getProperty("FILE.ENCODING_TYPE"), "UTF-8");

		String fileConvPathStr = properties.getProperty("FILE.CONV_PATH");
		File fileConvDir = new File(fileConvPathStr);
		if(!fileConvDir.exists()) {
			fileConvDir.mkdirs();
		}
		
		// DRM 관련 정보 
		String drmConfPathStr = properties.getProperty("DRM.FSDINIT_PATH");
		String drmFileDirStr =properties.getProperty("DRM.UPLOAD_PATH"); 
		String drmDomainId =properties.getProperty("DRM.DOMAIN_ID");
		 
		//네트워크 환경(외부 내부망)
		String networkEnv =StringUtil.setNullToString(properties.getProperty("NETWORK_ENV"), "OUT");
		
		// 파일 디렉토리 체크, 없으면 생성
		String segFileDirStr  = properties.getProperty("FILE.UPLOAD_PATH") + "/" + folder; 
		
		File segFileDir = new File(segFileDirStr);
		if(!segFileDir.exists()) {
			segFileDir.mkdirs();
		}
		
		File drmFileDir = new File(drmFileDirStr);
		if(!drmFileDir.exists()) {
			drmFileDir.mkdirs();
		}
		
		File drmConfDir = new File(drmConfPathStr);
		boolean drmInfoExist = true; 
		if(!drmConfDir.exists() || "".equals(drmDomainId) ) {
			drmInfoExist = false;
			logger.debug("DRM 설정 정보가 없습니다");
		}
		
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String orgFileName = null;
		String newFileName = null; 
		
		String uploadResult = "Fail";
		
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				try {
					orgFileName = multipartFile.getOriginalFilename();
					String newFileLong	= Long.toString(System.currentTimeMillis());
					//newFileName = newFileLong + "-" + orgFileName.replaceAll(" ", "_");
					//newFileName = newFileLong + "-" + orgFileName.replaceAll("\\s", "_");
					//newFileName = newFileLong + "-" + orgFileName.replaceAll("\\(", "_");
					//newFileName = newFileLong + "-" + orgFileName.replaceAll("\\)", "_");
					orgFileName = orgFileName.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,. ]", "");
					newFileName = newFileLong + "-" + orgFileName.replaceAll("\\s", "_");
					String drmFileLocNm = drmFileDirStr + "/" + newFileName;
					String fileLocNm = segFileDirStr + "/" + newFileName;
					String convFileLocNm =  fileConvPathStr + "/" + newFileName;
					
					logger.debug("goUploadSegFile orgFileName  = " + orgFileName);
					logger.debug("goUploadSegFile newFileName  = " + newFileName);
					logger.debug("goUploadSegFile drmFileLocNm = " + drmFileLocNm);
					logger.debug("goUploadSegFile convFileLocNm = " + convFileLocNm);
					
					/*
					try {
						multipartFile.transferTo(new File(drmFileLocNm));
					} catch (Exception e ) {
						logger.error("goUploadSegFile trasfer file error [orgFileName = " + orgFileName + "]");
					}
					*/ 
					
					try {
						Path drmFileLocPath = Paths.get(drmFileLocNm);
						Files.copy(multipartFile.getInputStream(), drmFileLocPath, StandardCopyOption.REPLACE_EXISTING); 
					} catch (Exception e ) {
						logger.error("goUploadSegFile copy file error [orgFileName = " + orgFileName + "]");
					}
					
					
					if ("IN".equals(networkEnv) && drmInfoExist ) {

						logger.debug("goUploadSegFile Process Network Env = IN");
						//DRM 관련 작업 내용 
						int iBret = 0;
						WorkPackager oWorkPackager = new WorkPackager();
						oWorkPackager.setCharset(encodingType);
						
						iBret = oWorkPackager.GetFileType(drmFileLocNm);//DRM 여부  체크 
						
						if (iBret == 103) {//DRM 문서일경우 drmFileLocNm
							boolean bRet = oWorkPackager.DoExtract(drmConfPathStr, drmDomainId,	drmFileLocNm, convFileLocNm); 
							int drmExtranctErrNum = 0;
							String drmExtranctErrStr = ""; 
							
							drmExtranctErrNum = oWorkPackager.getLastErrorNum();
							drmExtranctErrStr = oWorkPackager.getLastErrorStr();
								
							if (drmExtranctErrNum!=0){ 
								uploadResult = "Fail";
								logger.debug("goUploadSegFile DRM document Decrypt Fail (drmExtranctErrNum) " + drmExtranctErrNum + " (drmExtranctErrStr : " +  drmExtranctErrStr +")");  
							} else {
								logger.debug("goUploadSegFile DRM document Decrypt Success (File Type ) " + iBret );
								
								try { 
									if (!uploadSegFileWrite(convFileLocNm, fileLocNm, encodingType)) {
										uploadResult = "Fail";
										logger.error("goUploadSegFile DRM document uploadSegFileWrite Fail fileLocNm : " + fileLocNm ); 
									} else {
										uploadResult = "Success";
										logger.debug("goUploadSegFile DRM document uploadSegFileWrite Success fileLocNm " + fileLocNm );
									} 
								} catch (Exception e ) {
									logger.error("goUploadSegFile DRM document uploadSegFileWrite Exception fileLocNm : " + fileLocNm  + " / error : " + e.getMessage()); 
								}
							}
						} else {
							if (!uploadSegFileWrite(multipartFile, drmFileLocNm, fileLocNm, encodingType)) {
								uploadResult = "Fail";
								logger.error("goUploadSegFile uploadSegFileWrite Fail fileLocNm : " + fileLocNm );
							} else { 
								uploadResult = "Success";
								logger.debug("goUploadSegFile uploadSegFileWrite Success fileLocNm " + fileLocNm );
							}
			 			}
					} else {
						logger.debug("goUploadSegFile Process Network Env = OUT");
						if (!uploadSegFileWrite(multipartFile, drmFileLocNm, fileLocNm,  encodingType)) {
							uploadResult = "Fail";
							logger.error("goUploadSegFile uploadSegFileWrite Fail fileLocNm : " + fileLocNm );
						} else {
							uploadResult = "Success";
							logger.debug("goUploadSegFile uploadSegFileWrite Success fileLocNm " + fileLocNm );	
						}
					}
					
					if ("Success".equals(uploadResult)) {
						
						Path drmFilePath = Paths.get(drmFileLocNm); 
						Path convFilePath = Paths.get(convFileLocNm); 
						
						try {
							Files.deleteIfExists(drmFilePath); 
							Files.deleteIfExists(convFilePath); 
						} catch (Exception e) {
							logger.error("goUploadSegFile Delete Drm File : + " + drmFilePath  + " / Fail error :" + e.getMessage() );
						}
					}
				} catch (Exception e) {
					uploadResult = "Fail";
					logger.error("goUploadSegFile Exception error = " + e);
				}
			} else {
				uploadResult = "Fail";
				logger.debug("goUploadSegFile Fail : File is empty");
			}
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("oldFileName", orgFileName);
		map.put("newFileName", newFileName);
		map.put("result", uploadResult);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	} 
	
	/* None DRM 
	public ModelAndView goUploadSegFile(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadSegFile folder = " + uploadVO.getFolder());
		logger.debug("goUploadSegFile rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadSegFile vFileName = " + uploadVO.getvFileName());
		
		// 업로드 폴더
		String folder = uploadVO.getFolder();
		
		// 파일 디렉토리 체크, 없으면 생성
		String htmlFileDirStr  = properties.getProperty("FILE.UPLOAD_PATH") + "/" + folder;
		
		logger.debug("goUploadSegFile htmlFileDirStr = " + htmlFileDirStr);
		
		File htmlFileDir = new File(htmlFileDirStr);
		if(!htmlFileDir.exists()) {
			htmlFileDir.mkdirs();
		}
		
		// 업로드 파일 처리, 파일이름을 yyyyMMddHHmmss_idx 형식으로 변경하여 저장.
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String oldFileName = null;
		String newFileName = null;
		List<String> memAlias = new ArrayList<String>();
		String memDelim=",";
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				try {
					oldFileName		= multipartFile.getOriginalFilename();
					String newFileLong	= Long.toString(System.currentTimeMillis());
					newFileName = newFileLong + "-" + oldFileName.replaceAll(" ", "_");
					String fileLocNm	= htmlFileDirStr + "/" + newFileName;
					
					logger.debug("goUploadSegFile oldFileName = " + oldFileName);
					logger.debug("goUploadSegFile newFileName = " + newFileName);
					
					String line;
					
					BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), "EUC-KR"));
					//BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), "UTF-8"));
					//!!!!!!!!!Critical Warning!!!!!!!!!!
					//인코딩 관련 개발서버 및 고객사별로 소스분리 (Source separation by development server and customer related to encoding)
					//해당 소스 전송시 개발서버용 부분은 주석 처리 해야함 (When transmitting the source, the part for the development server needs to be annotated.)
					
					//+개발서버용 (For the Enders  Dev Server ) 
					File file = new File(fileLocNm);
					FileOutputStream fos = new FileOutputStream(file);
					OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
					BufferedWriter bw = new BufferedWriter(osw);
					//-
					//+광주은행의 경우 아래의 항목으로 처리해야함 (For The KJBank )  
					//BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(fileLocNm, true));
					//-
					boolean first = true;
					int aliasCnt = 0;	// 알리아스수
					String alias = "";
					while((line=br.readLine()) != null) {
						StringTokenizer st = new StringTokenizer(line, memDelim);
						String encStr = ""; 
						if (first) {
							while(st.hasMoreTokens()) {
								memAlias.add(st.nextToken());
								aliasCnt++;
							}
							first = false;
							bw.write(line+"\n");
						} else {
							if(line == null || "".equals(line)) break;
							for(int cnt = 0; cnt < aliasCnt; cnt++) {
								alias = (String)memAlias.get(cnt);
								encStr += cryptoService.getEncrypt(alias,st.nextToken()) + memDelim;
							}
							if(encStr.length() > 1 ) {
								encStr = encStr.substring(0, encStr.length() - 1);
								bw.write(encStr+"\n");
							}
						}
					}
					bw.flush();
					bw.close();
				} catch (Exception e) {
					logger.error("goUploadSegFile error = " + e);
				}
			}
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("oldFileName", oldFileName);
		map.put("newFileName", newFileName);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	*/
	
	
	/**
	 * 파일을 지정된 경로로 업로드 한다.(디자인 적용으로 추가) -파일별 
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/uploadFile")
	public ModelAndView goUploadFile(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadFile folder = " + uploadVO.getFolder());
		logger.debug("goUploadFile rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadFile vFileName = " + uploadVO.getvFileName());
		
		// 업로드 폴더
		String folder = uploadVO.getFolder();
		
		// 파일 디렉토리 체크, 없으면 생성
		String htmlFileDirStr  = properties.getProperty("FILE.UPLOAD_PATH") + "/" + folder;
		
		logger.debug("goUploadFile htmlFileDirStr = " + htmlFileDirStr);
		
		File htmlFileDir = new File(htmlFileDirStr);
		if(!htmlFileDir.exists()) {
			htmlFileDir.mkdirs();
		}
		
		// 업로드 파일 처리, 파일이름을 yyyyMMddHHmmss_idx 형식으로 변경하여 저장.
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String oldFileName = null;
		String newFileName = null;
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				oldFileName		= multipartFile.getOriginalFilename();
				String newFileLong	= Long.toString(System.currentTimeMillis());
				newFileName = newFileLong + "-" + oldFileName.replaceAll(" ", "_");
				String fileLocNm	= htmlFileDirStr + "/" + newFileName;
				
				logger.debug("goUploadFile oldFileName = " + oldFileName);
				logger.debug("goUploadFile newFileName = " + newFileName);
				
				try {
					File file = new File(fileLocNm);
					multipartFile.transferTo(file);
				} catch(Exception e) {
					logger.error("goUploadFile error = " + e);
				}
			}
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("oldFileName", oldFileName);
		map.put("newFileName", newFileName);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	
	/**
	 * 파일을 지정된 경로로 업로드 한다.(디자인 적용으로 추가) -디렉토리별 
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/uploadFileDir")
	public ModelAndView goUploadFileDir(MultipartHttpServletRequest multipartRequest, @ModelAttribute UploadVO uploadVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goUploadFileDir folder = " + uploadVO.getFolder());
		logger.debug("goUploadFileDir rFileName = " + uploadVO.getrFileName());
		logger.debug("goUploadFileDir vFileName = " + uploadVO.getvFileName());
		
		// 업로드 폴더
		String folder = uploadVO.getFolder();
		
		// 파일 디렉토리 체크, 없으면 생성
		String htmlFileDirStr  = properties.getProperty("FILE.UPLOAD_PATH") + "/" + folder;
		
		logger.debug("uploadFileDir htmlFileDirStr = " + htmlFileDirStr);
		
		File htmlFileDir = new File(htmlFileDirStr);
		if(!htmlFileDir.exists()) {
			htmlFileDir.mkdirs();
		}
		
		String newFileDirStr=  Long.toString(System.currentTimeMillis());
		
		String fileDirStr  = htmlFileDirStr + "/" +  newFileDirStr;
		File fileDir = new File(fileDirStr);
		if(!fileDir.exists()) {
			fileDir.mkdirs();
		}

		// 업로드 파일 처리, 폴더를 yyyyMMddHHmmss 형식으로 만든 후 하단에 저장함 
		Iterator<String> files = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;
		String oldFileName = null;
		String newFileName = null;
		while(files.hasNext()) {
			multipartFile = multipartRequest.getFile(files.next());
			if(!multipartFile.isEmpty()) {
				oldFileName = multipartFile.getOriginalFilename().replaceAll("," , "");
				newFileName = oldFileName.replaceAll(" ", "_").replaceAll("," , ""); 
				
				logger.debug("uploadFileDir fileDir = " + fileDir);
				logger.debug("uploadFileDir newFileName = " + newFileName);
				
				String fileLocNm = fileDir + "/" + newFileName;
				newFileName = newFileDirStr +"/" + newFileName;
				
				logger.debug("uploadFileDir oldFileName = " + oldFileName);
				logger.debug("uploadFileDir newFileName = " + newFileName );
				logger.debug("uploadFileDir fileLocNm = " + fileLocNm);
				  
				try { File file = new File(fileLocNm); multipartFile.transferTo(file); }
				  catch(Exception e) { logger.error("goUploadFile error = " + e); }
				 
			}
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("oldFileName", oldFileName);
		map.put("newFileName", newFileName);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	
	/**
	 * 사용자의 해당 기능 사용 여부 확인
	 * @param multipartRequest
	 * @param uploadVO
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/userFuncAuth")
	public ModelAndView goUserFuncAuth(@ModelAttribute CodeVO codeVO, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logger.debug("goCheckFuncAuth cd = " + codeVO.getCd());
		
		codeVO.setUserId((String)session.getAttribute("NEO_USER_ID"));
		// 권한여부 체크  목록 조회
		String userFuncAuth ="N";
		try {
			userFuncAuth = codeService.getUserFuncAuth(codeVO);
		} catch(Exception e) {
			logger.error("codeService.getUserFuncAuth error = " + e);
		}
		
		// jsonView 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("auth", userFuncAuth);
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/captchaImg")
	public void cpatchaImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new CaptchaUtil().getImgCaptCha(request, response);
	}
	
	// 사용자가 입력한 보안문자 체크하는 메서드
	@RequestMapping(value="/checkAnswer")
	public ModelAndView chkAnswer(@RequestParam String answer, HttpServletRequest request, HttpServletResponse response)  {
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(answer != null && !"".equals(answer)) {
			if(captcha.isCorrect(answer)) {
				request.getSession().removeAttribute(Captcha.NAME);
				map.put("result", "Success");
			}else {
				map.put("result", "Fail");
			}
		}
		ModelAndView modelAndView = new ModelAndView("jsonView", map);
		return modelAndView;
	}
	
	private String  getDetectedCharset(String fileName, String defaultCharSet) {
		byte[] buf = new byte[2048];
		String encoding = "";
		
		try {
			UniversalDetector detector = new UniversalDetector(null);
			
			FileInputStream fis = new FileInputStream(fileName);
			
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
			detector.dataEnd();

			encoding = detector.getDetectedCharset();
			if (encoding != null) { 
				
				logger.debug("comController.getDetectedCharset ["+ fileName + "] Detected encoding = " + encoding );
				encoding = encoding.toUpperCase(); 
				if (!"UTF-8".equals(encoding)) {
					encoding = "EUC-KR";
				}
			} else {
				encoding = "EUC-KR";
				logger.debug("comController.getDetectedCharset ["+ fileName + "] No Detected Set EUC-KR"); 
			}
			detector.reset();
			fis.close();
			
		} catch(Exception e) {
			encoding = "EUC-KR";
			logger.error("comController.getDetectedCharset fileName = ["+ fileName + "] error = " + e.getMessage());
		}

		return encoding;
	}
	
	private boolean uploadSegFileWrite(MultipartFile multipartFile, String drmFileLocNm, String fileLocNm, String encodingType) {
		boolean retResult = false;

		String orgFileEncoding = getDetectedCharset(drmFileLocNm, encodingType);
		
		/*
		if (StringUtil.isNull(orgFileEncoding)  || !"UTF-8".equals(orgFileEncoding.toUpperCase())) {
			logger.debug("comController.uploadSegFileWrite fileLocNm = ["+ fileLocNm + "] orgEncoding = " + orgFileEncoding + " / setEncoding = EUC-KR");
			orgFileEncoding = "EUC-KR";
		}
		*/
		
		logger.debug(" ============================");
		logger.debug("comController.uploadSegFileWrite NO DRM ["+ fileLocNm + "] Encoding = " + orgFileEncoding );
		logger.debug(" ============================");
		
		try {
			//File orgfile = new File(drmFileLocNm);
			//BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), orgFileEncoding)); 
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(drmFileLocNm),orgFileEncoding));
			File file = new File(fileLocNm);
			FileOutputStream fos = new FileOutputStream(file); 
			OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			BufferedWriter bw = new BufferedWriter(osw);
			List<String> memAlias = new ArrayList<String>();
			String memDelim=",";
			boolean first = true;
			int aliasCnt = 0;
			String alias = "";
			String line; 
			 
			String segEncrypt  = properties.getProperty("FILE.SEG_ENCRYPT");
			
			while((line=br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, memDelim);
				String encStr = ""; 
				if (first) {
					while(st.hasMoreTokens()) {
						memAlias.add(st.nextToken());
						aliasCnt++;
					}
					first = false;
					bw.write(line+"\n");
				} else {
					if(line == null || "".equals(line)) break;
					for(int cnt = 0; cnt < aliasCnt; cnt++) {
						alias = (String)memAlias.get(cnt);
						if (!"Y".equals(segEncrypt)) {
							encStr += st.nextToken() + memDelim;
						} else {
							encStr += cryptoService.getEncrypt(alias,st.nextToken()) + memDelim;
						}
					}
					if(encStr.length() > 1 ) {
						encStr = encStr.substring(0, encStr.length() - 1);
						bw.write(encStr+"\n");
					}
				}
			}
			bw.flush();
			bw.close();	
			retResult = true;
			
		} catch (Exception e) {
			retResult = false;
			logger.error("comController.uploadSegFileWrite fileLocNm = ["+ fileLocNm + "] error = " + e.getMessage());
		}
		
		return retResult;
	}
	
	private boolean uploadSegFileWrite(String drmFileLocNm, String fileLocNm, String encodingType) {
		boolean retResult = false;

		String orgFileEncoding = getDetectedCharset(drmFileLocNm, encodingType);
		
		logger.debug(" ============================");
		logger.debug("comController.uploadSegFileWrite DRM   ["+ fileLocNm + "] Encoding = " + orgFileEncoding );
		logger.debug(" ============================");
		
		try {
			InputStream inFile = new FileInputStream(drmFileLocNm);
			/*if (!"UTF-8".equals(orgFileEncoding)) {
				orgFileEncoding ="EUC-KR";
			}*/
			//BufferedReader br = new BufferedReader(new InputStreamReader(inFile, orgFileEncoding));
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(drmFileLocNm),orgFileEncoding));
			File file = new File(fileLocNm);
			FileOutputStream fos = new FileOutputStream(file); 
			OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			BufferedWriter bw = new BufferedWriter(osw);
			List<String> memAlias = new ArrayList<String>();
			String memDelim=",";
			boolean first = true;
			int aliasCnt = 0;
			String alias = "";
			String line;
			
			String segEncrypt  = properties.getProperty("FILE.SEG_ENCRYPT");
			
			while((line=br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, memDelim);
				String encStr = ""; 
				//String curToken = "";
				if (first) {
					while(st.hasMoreTokens()) {
						memAlias.add(st.nextToken());
						aliasCnt++;
					}
					first = false;
					bw.write(line+"\n");
				} else {
					if(line == null || "".equals(line)) break;
					for(int cnt = 0; cnt < aliasCnt; cnt++) {
						alias = (String)memAlias.get(cnt);
						//curToken = st.nextToken();
						//encStr += curToken + memDelim; 
						if (!"Y".equals(segEncrypt)) {
							encStr +=  st.nextToken() + memDelim;
						} else {
							encStr += cryptoService.getEncrypt(alias, st.nextToken()) + memDelim;
						}
					}
					if(encStr.length() > 1 ) {
						encStr = encStr.substring(0, encStr.length() - 1);
						bw.write(encStr+"\n");
					}
				}
			}
			bw.flush();
			bw.close();	
			inFile.close();
			retResult = true;
			
		} catch (Exception e) {
			retResult = false;
			logger.error("comController.uploadSegFileWrite fileLocNm = ["+ fileLocNm + "] error = " + e.getMessage());
		}
		
		return retResult;
	}
}