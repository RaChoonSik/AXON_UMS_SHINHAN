
/**
 * 작성자 : 김준희
 * 작성일시 : 2022.12.13
 * 설명 : GRS 암호화 샘플
 */
package kr.co.enders.util;

import java.util.*;
import com.fasoo.adk.packager.*;

public class DoPackagingFSN2 {
	

		/**
		 * @param args 
		 */
		
//		GetFileType() 함수의 리턴 값 설명
	//
	//20  : 파일을 찾을 수 없습니다. 
	//21  : 파일 사이즈가 0 입니다.
	//22  : 파일을 읽을 수 없습니다.
	//29  : 암호화 파일이 아닌 일반파일입니다.
	//26  : FSD Type의 암호화 파일입니다.
	//105 : Wrapsody Type의 암호화 파일입니다.
	//101 : MarkAny Type의 암호화 파일입니다.
	//104 : INCAPS(삼성 PC DRM) Type의 암호화 파일입니다.
	//103 : FSN Type의 암호화 파일입니다.
		
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			System.out.println("test");
			
			String drm_fsdinit_path = "C:\\Dev\\drm\\fsdinit";			//key 폴더 경로 
			String domain_id = "0000000000012280";			//dsdcode
			String ORGFileDir = "C:\\Dev\\drm\\contents\\org\\";
			String DRMFileDir = "C:\\Dev\\drm\\contents\\enc\\";
			String Filename = "test_drm.txt";
			String UserID ="test";
			String UserName ="홍길동"; 
			String WriterId ="test";
			String WriterName ="홍길동";				// 작성자 이름
			String WriterDeptId ="5555";			// 작성자 부서 ID
			String WriterDeptName ="FASOO";			// 작성자 부서 이름
			String OwnerId ="test";					// 소유자 ID 
			String OwnerName ="홍길동";				// 소유자 이름
			String OwnerDeptId ="5555";				// 소유 부서 ID
			String OwnerDeptName ="NS2팀";			// 소유 부서 이름
			String SecurityLevel ="c92d07df11f041fe9a8894b7a61f13f7";		// 문서 등급 코드  

			int iErrCheck = 0; 
			String sErrMessage = "";
			try {
			 
					int iBret = 0;
					WorkPackager oWorkPackager = new WorkPackager();
			 		
					iBret = oWorkPackager.GetFileType(	ORGFileDir+Filename );   //파일 타입 체크
					System.out.println("iBret"+iBret);
					if (iBret == 29) {        //일반  문서일경우 암호화
							
								oWorkPackager.setOverWriteFlag(false);   //OverWrite  true/false

								boolean bret = oWorkPackager.DoPackagingFsn2(
											 drm_fsdinit_path, 
											 domain_id, 
											 ORGFileDir+Filename, 
										     DRMFileDir+Filename, 
											 Filename,
											 UserID,
											 UserName, 
											 WriterId,
											 WriterName,
											 WriterDeptId,
											 WriterDeptName,
											 OwnerId,
											 OwnerName,
											 OwnerDeptId, 
											 OwnerDeptName, 
											 SecurityLevel);		
											
								System.out.println(" bret : " + bret);
								if (!bret) {
									if( oWorkPackager.getLastErrorNum() == 11 ){
										System.out.println("oWorkPackager.getLastErrorNum() == 11");
									}else{
										System.out.println("암호화 중 오류중 오류입니다.");
										System.out.println(" 오류 정보..");
										System.out.println("    ["+ oWorkPackager.getLastErrorNum()+"] "+oWorkPackager.getLastErrorStr());
										iErrCheck = 1;
										sErrMessage = oWorkPackager.getLastErrorStr();
									}

								}else{
									System.out.println("암호화성공 Packaged FileName : "+oWorkPackager.getContainerFileName()+"");

								}	

						}else{ 

							if (oWorkPackager.getLastErrorNum()==0) {
								System.out.println(" 오류 정보..");
								System.out.println("    ["+ oWorkPackager.getLastErrorNum()+"] "+oWorkPackager.getLastErrorStr());
							} else {
								System.out.println(" 오류 정보..");
								System.out.println("    ["+ oWorkPackager.getLastErrorNum()+"] "+oWorkPackager.getLastErrorStr());
							}

						}

			 
			 
					if ( iErrCheck == 0 ) {
						System.out.println("<iErrCheck == 0>");
					}else{
						System.out.println("Download Action Error [message]: "+sErrMessage);
					}
					 
			}catch( Exception e ){
				System.out.println("<script>");
				System.out.println("alert('시스템에 문제가 발생 했습니다. 관리자에게 문의 해주십시요.');");
				System.out.println("</script>");
			}finally{
			 
				System.out.println("test");
				System.out.println("finally");
			 
			}
		}

	}