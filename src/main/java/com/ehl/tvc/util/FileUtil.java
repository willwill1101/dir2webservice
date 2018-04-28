package com.ehl.tvc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class FileUtil {
	private static String endTag = "end";
	/**
	 * 判断文件是否写入内容完毕，如果完毕返回true，反之false
	 * @param f
	 * @return
	 */
	public static boolean checkIniTransfered(File f){
		OutputStream output = null;
		try {
			output = new FileOutputStream(f, true);
			output.write((endTag+"\n").getBytes());
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(output);
		}
		InputStream input =null;
		try {
			input = new FileInputStream(f);
			List<String> list =  IOUtils.readLines(input);
			if(list.size()>0){
				String endstr =list.get(list.size()-1);
				if(endTag.equals(endstr)){
					return true;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(input);
		}
		return false;
	}
}
