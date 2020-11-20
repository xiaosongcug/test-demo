package com.ReadDLTJ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//
public class DLTJ_MJ_HZ {

	// public JSONArray r=null;
	public static String[] gd = { "011", "012", "013" };// 耕地
	public static String[] yd = { "021", "022", "023" };// 园地
	public static String[] ld = { "031", "032", "033" };// 林地
	public static String[] cd = { "041", "042" };// 草地
	public static String[] qtnyd = { "104", "113", "114", "117", "122", "123" };// 其他农用地
	public static String[] gkyd = { "201", "202", "203", "204", "205" };// 工矿用地
	public static String[] jtyd = { "101", "102", "105", "106", "107", "118" };// 交通用地
	public static String[] slyd = { "111", "112", "115", "116", "119" };// 水利设施用地
	public static String[] qttd = { "124", "125", "126", "127", "043" }; // 其他土地

	public static int hh = 1;// 行号
	public static String pathWrite = "X:\\Users\\Administrator\\Desktop\\123.xls";// 数据输出路径
	public static String pathData = "X:\\Users\\Administrator\\Desktop\\1234.json";// 数据路径
	public static String[] DLName = { "REG_ID", "耕地", "园地", "林地", "草地", "其他农用地", "工矿用地", "交通用地", "水利设施用地", "其他土地用地",
			"总面积" };// 地类区分名称

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// readFile();
		try {
			String json = new DLTJ_MJ_HZ().readFile(pathData);
			JSONArray jsonArray = JSONArray.fromObject(json);
			int size = jsonArray.size();
			double mj = 0;
			double lxmj = 0;
			double xzmj = 0;
			List<String> list = new ArrayList<String>();
			List<String> listlx = new ArrayList<String>();
			List<String> listxz = new ArrayList<String>();
			// System.out.println("id,dltbcode,lxdwcode,xzdwcode");
			/*
			 * System.out.println("REG_ID" + "," + "耕地" + "," + "园地" + "," + "林地" + "," +
			 * "草地" + "," + "其他农用地" + "," + "工矿用地" + "," + "交通用地" + "," + "水利设施用地" + "," +
			 * "其他土地" + "," + "总面积");
			 */
			int d = 0;

			for (int i = 0; i < size; i++) {

				JSONObject res = jsonArray.getJSONObject(i);
				JSONArray res0 = (JSONArray) jsonArray.getJSONObject(i).get("result");
				String reg_id_value = (String) jsonArray.getJSONObject(i).get("value");
				JSONArray res1 = null;
				// System.out.println(i + ":" + jsonArray.getJSONObject(i).get("size") + ";" +
				// reg_id_value);
				/*
				 * double MF = (double) (jsonArray.getJSONObject(i).get("size")); mjzh += MF;
				 */
				for (int p = 0; p < res0.size(); p++) {
					res1 = (JSONArray) res0.getJSONObject(p).get("overinfo");
					double mj1 = 0;

					Map<String, Double> codeMJ = new HashMap();
					for (int j = 0; j < res1.size(); j++) {
						// System.out.println(j);

						Object o = res1.getJSONObject(j).getJSONObject("insertFeature").get("size");
						JSONArray attr = res1.getJSONObject(j).getJSONObject("insertFeature").getJSONArray("attribute");

						String dltbcode = null;
						for (int g = 0; g < attr.size(); g++) {
							String codename = (String) attr.getJSONObject(g).get("name");
							if (codename.equals("DLBM")) {
								dltbcode = (String) attr.getJSONObject(g).get("value");
								list.add((String) attr.getJSONObject(g).get("value"));
							}
						}
						if (codeMJ.containsKey(dltbcode)) {
							Double m = codeMJ.get(dltbcode);
							codeMJ.remove(dltbcode);
							codeMJ.put(dltbcode, Double.parseDouble((String) o) + m);
						} else {
							codeMJ.put(dltbcode, Double.parseDouble((String) o));
						}
						JSONArray lxdw = res1.getJSONObject(j).getJSONObject("insertFeature").getJSONArray("lxdw");
						String lxdwcode = null;
						Double lm = 0.0;
						Double lm1 = 0.0;
						// List<String> listlx1=new ArrayList<String>();
						// List<String> listxz1=new ArrayList<String>();
						for (int k = 0; k < lxdw.size(); k++) {

							// System.out.println(lxdw.getJSONObject(k).get("value"));
							lm = Double.parseDouble((String) lxdw.getJSONObject(k).get("value"));

							lxmj += lm;
							// lm+=lm;
							lm1 += lm;
							lxdwcode = (String) lxdw.getJSONObject(k).get("code");

							// System.out.println("DLBM-->" + lxdwcode + ";area-->" + lm);
							listlx.add(lxdwcode);
							if (codeMJ.containsKey(lxdwcode)) {
								Double m = codeMJ.get(lxdwcode);
								codeMJ.remove(lxdwcode);
								codeMJ.put(lxdwcode, lm + m);
							} else {
								codeMJ.put(lxdwcode, lm);
							}
							// listlx1.add(lxdwcode);
						}
						JSONArray xzdw = res1.getJSONObject(j).getJSONObject("insertFeature").getJSONArray("xzdw");
						String xzdwcode = null;
						Double xm = 0.0;
						Double xm1 = 0.0;
						for (int h = 0; h < xzdw.size(); h++) {

							// xm=null;
							// System.out.println(xzdw.getJSONObject(h).get("value"));
							xm = Double.parseDouble((String) xzdw.getJSONObject(h).get("value"));
							xzdwcode = (String) xzdw.getJSONObject(h).get("code");
							// System.out.println("DLBM-->" + xzdwcode + ";area-->" + xm);
							listxz.add(xzdwcode);
							// listxz1.add(xzdwcode);
							if (codeMJ.containsKey(xzdwcode)) {
								Double m = codeMJ.get(xzdwcode);
								codeMJ.remove(xzdwcode);
								codeMJ.put(xzdwcode, xm + m);
							} else {
								codeMJ.put(xzdwcode, xm);
							}
							// list.add((String) lxdw.getJSONObject(h).get("code"));
							xzmj += xm;
							xm1 += xm;
							// xm+=xm;
						}

						d++;
						Double m = Double.parseDouble((String) o);
						// System.out.println(m);
						mj += m;
						mj1 += m;

					}
					OutputCodeMJ(reg_id_value, codeMJ);
				}

			}

			/*
			 * List<String> l=new ArrayList<String>(); for(int i=0;i<list.size();i++) {
			 * if(!l.contains(list.get(i))) { l.add(list.get(i)); } }
			 */
			// System.out.println(mjzh);
			File file = new File(pathWrite);
			OutputStream outputStream = new FileOutputStream(file);
			// 写入表头
			HSSFRow row1 = sheet.createRow(0);
			for (int Dindex = 0; Dindex < DLName.length; Dindex++) {
				HSSFCell cell21 = row1.createCell((short) Dindex);
				cell21.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell21.setCellValue(DLName[Dindex]);
			}
			workbook.write(outputStream);
			outputStream.close();
			List<String> l = DLTJ_MJ_HZ.qz(list);
			List<String> lx = DLTJ_MJ_HZ.qz(listlx);
			List<String> xz = DLTJ_MJ_HZ.qz(listxz);
			System.out.println("地类图斑面积" + mj);
			System.out.println("零星地物面积" + lxmj);
			System.out.println("线状地物面积" + xzmj);
			System.out.println("地类图斑代码:" + l);
			System.out.println("零星代码:" + lx);
			System.out.println("线状代码:" + xz);
			System.out.println("地类数:" + l.size());
			System.out.println("总面积:" + (mj + lxmj + xzmj));
			System.out.println(d);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 输出每一条REG_ID对应地块地类的面积
	public static void OutputCodeMJ(String REG_ID, Map<String, Double> codeMJ) throws IOException {
		Double gdmj = codeHF(gd, codeMJ);// 耕地
		Double ydmj = codeHF(yd, codeMJ);// 园地
		Double ldmj = codeHF(ld, codeMJ);// 林地
		Double cdmj = codeHF(cd, codeMJ);// 草地
		Double qtnydmj = codeHF(qtnyd, codeMJ);// 其他农用地
		Double gkydmj = codeHF(gkyd, codeMJ);// 工矿用地
		Double jtydmj = codeHF(jtyd, codeMJ);// 交通用地
		Double slydmj = codeHF(slyd, codeMJ);// 水利设施用地
		Double qttdmj = codeHF(qttd, codeMJ); // 其他土地
		Double zmj = gdmj + ydmj + ldmj + cdmj + qtnydmj + gkydmj + jtydmj + slydmj + qttdmj;
		Double[] DLmj = { gdmj, ydmj, ldmj, cdmj, qtnydmj, gkydmj, jtydmj, slydmj, qttdmj, zmj };
		wirteExcel(REG_ID, DLmj);
		/*
		 * System.out.println(REG_ID + "," + gdmj + "," + ydmj + "," + ldmj + "," + cdmj
		 * + "," + qtnydmj + "," + gkydmj + "," + jtydmj + "," + slydmj + "," + qttdmj +
		 * "," + zmj);
		 * 
		 * Set<String> keySet = codeMJ.keySet(); Iterator<String> it =
		 * keySet.iterator(); Double mz = 0.0; while (it.hasNext()) { String key =
		 * it.next(); Double value = codeMJ.get(key); mz += value; }
		 */
	}

	// REG-id对应地块的分类面积（按照草地、耕地、园地等）
	public static Double codeHF(String[] code, Map<String, Double> codeMJ) {
		Double mz = 0.0;
		for (int i = 0; i < code.length; i++) {
			String codeStr = code[i];
			if (codeMJ.containsKey(codeStr)) {
				Double mj = codeMJ.get(codeStr);
				mz += mj;
			}
		}
		return mz;
	}

	// 将数据写入表中
	public static HSSFWorkbook workbook = new HSSFWorkbook();
	public static HSSFSheet sheet = workbook.createSheet();

	public static void wirteExcel(String REG_ID, Double[] dlmj) throws IOException {
		HSSFRow row = sheet.createRow(hh);
		HSSFCell cell0 = row.createCell((short) 0);
		cell0.setCellValue(REG_ID);
		for (int dlmjindex = 0; dlmjindex < dlmj.length; dlmjindex++) {
			HSSFCell cell = row.createCell((short) (dlmjindex + 1));
			cell.setCellValue(dlmj[dlmjindex]);
		}

		hh++;
	}

//去除重复的code代码，并且将面积进行累加
	public static List<String> qz(List<String> l) {
		List<String> r = new ArrayList<String>();
		for (int i = 0; i < l.size(); i++) {
			if (!r.contains(l.get(i))) {
				r.add(l.get(i));
			}
		}
		return r;

	}

	public String resultJSON(JSONArray jsonArray) {
		return null;

	}

	public String readFile(String path) throws IOException {
		/*
		 * BufferedReader reader; try { reader=new BufferedReader(new FileReader(new
		 * File("X:\\Users\\Administrator\\Desktop\\2017.json"))); //JSONObject
		 * data=(JSONObject)JSON.parse(reader.readLine()); // String jsoncotent=new
		 * 
		 * 
		 * } catch (FileNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		BufferedReader reader = null;

		String laststr = "";
		try {
			FileInputStream fileInsputStream = new FileInputStream(path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInsputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return laststr;
	}

}
