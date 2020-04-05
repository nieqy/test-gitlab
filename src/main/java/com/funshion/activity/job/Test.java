package com.funshion.activity.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.funshion.activity.common.utils.HttpClientUtils;
import com.funshion.activity.common.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangfei on 2018/10/17/017.
 */
public class Test {

    public void read() throws Exception {
        File file = new File("C:\\Users\\zhangfei\\Desktop\\nba.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
        String line;
        List<String> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String result = this.getData(line);
            list.add(result);
        }
        this.saveFile("C:\\Users\\zhangfei\\Desktop\\nbaresult.txt", list);
        br.close();
    }

    private void saveFile(String path, List<String> list) {
        File file = new File(path);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
            for (String s : list) {
                writer.write(s);
                writer.newLine();
            }
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getData(String mac) {
        String key = "04SFLxR1dOIiWaRH08sllCWh0TdNIVuktRHTEw5UbJTuNRXjzrtgLcdXNScA4";
        String sql = "q:mercury_redis_user_profile_" + mac;
        String sign = MD5Utils.getMD5String(sql + key);
        try {
            String ress = HttpClientUtils.get("http://ja.funtv.bestv.com.cn/api/getRdata.json?sql=" + sql + "&sign=" + sign);
            return ress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void ananizy() throws Exception {
        File file = new File("C:\\Users\\zhangfei\\Desktop\\nbaresult.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
        String line;
        List<String> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            try {
                JSONObject json = JSON.parseObject(line);
                JSONArray child = json.getJSONArray("child");
                if (child.size() > 0) {
                    list.add(JSON.toJSONString(child));
                }
            } catch (Exception e) {

            }
        }
        this.saveFile("C:\\Users\\zhangfei\\Desktop\\nbaf.txt", list);
        br.close();
    }

    public static void main(String[] args) throws Exception {
        new Test().ananizy();
    }

}
