
/**
 * 작성자 : 김준희
 * 작성일시 : 2022.12.13
 * 설명 : GRS 복호화 샘플
 */

package kr.co.enders.util; 
import com.fasoo.adk.packager.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption; 

public class DoExtract {
 

	public static void main(String[] args) {  
		
		String drmConfigPath = "C://Dev//drm//fsdinit";
		String drmFilePath = "C://Dev//drm//dec";
		String drmEncPath = "C://Dev//drm//enc";
		String drmDomainId = "0000000000012280"; 
		 
		int error_num = 0;
		String error_str = ""; 
		String Error_Message =""; 
		
		try {
			int iBret = 0;
			WorkPackager oWorkPackager = new WorkPackager();
			oWorkPackager.setCharset("UTF-8");
			
			File dir = new File(drmFilePath);

			String[] filenames = dir.list();
			for (String filename : filenames) {
				System.out.println("File Name : " + filename);
				String targetDrmFileName = drmFilePath + "//" + filename;
				String newFileLong	= Long.toString(System.currentTimeMillis());
				String encFileName =  drmEncPath + "//" + newFileLong + "-" + filename.replaceAll(" ", "_"); 
				
				iBret = oWorkPackager.GetFileType(targetDrmFileName); 
				boolean bRet = false;
				
				if (iBret == 103) {
					
					System.out.println("DRM File type ? " + iBret);
					
					bRet = oWorkPackager.DoExtract(drmConfigPath,drmDomainId, targetDrmFileName, encFileName);
					
					error_num = oWorkPackager.getLastErrorNum();
					error_str = oWorkPackager.getLastErrorStr();
					
					if (error_num!=0){
						System.out.println(" DRM  문서 복호화 실패 ");
						System.out.println("error_num = ? " + error_num);
						System.out.println("error_str = ?[ " + error_str+" ]");
					}	
					else
					{
						System.out.println(" DRM  문서 복호화 성공 ");
						System.out.println(" DRM  File type:"+iBret); 
					}
				}else{ 
					Error_Message = "NOT Support File";
					Path source = Paths.get(targetDrmFileName);
					Files.move(source, source.resolveSibling(encFileName),
					                StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}catch( Exception e ){
			System.out.println("error_num  = :"+error_num);
			System.out.println("error_str = :"+error_str);
			System.out.println("Error_Message = :"+Error_Message);
	
		}finally{ 
			System.out.println("finally"); 
		}
		
	}

}
