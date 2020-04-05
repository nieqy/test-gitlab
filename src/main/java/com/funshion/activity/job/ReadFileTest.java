package com.funshion.activity.job;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2018/11/22/022.
 */
public class ReadFileTest {

	public static List<String> readFile(){
		List<String> list = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\zhangfei\\Desktop\\cvte2.txt")),"UTF-8"));
			String line = null;
			while((line=br.readLine())!=null){
				try {
					list.add(line.split(",")[1].replaceAll(":","").toUpperCase());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static void readFile1(List<String> list){
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\zhangfei\\Desktop\\cvte_10.csv")),"UTF-8"));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("C:\\Users\\zhangfei\\Desktop\\cvte_10_1.csv"), true),"UTF-8"));
			String line = null;
			while((line=br.readLine())!=null){
				try {
					if(list.contains(line.split(",")[0])){
						bw.write(line+",1");
					}else{
						bw.write(line+",0");
					}
					bw.newLine();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(bw!=null){
					br.close();
				}
				if(bw!=null){
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<String> readFile2(File file){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String line = null;
			List<String> list = new ArrayList<>();
			while((line=br.readLine())!=null){
				list.add(line);
				/*String[] lineInfos = line.split("\t");
				System.out.println(lineInfos);*/
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
