package com.boss.vestibularsystemdetection.backsupportdetection.Tool;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IOTool {
    private File dirPath;

    public IOTool(String dir_path) {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

        }else {
            //建立資料夾路徑
            File sdPath = Environment.getExternalStorageDirectory();
            dirPath = new File(sdPath.getAbsoluteFile(), dir_path);

            if(!dirPath.exists())
                dirPath.mkdir();
        }
    }

    public void deleteFile(){
        File[] listFiles = dirPath.listFiles();
        for(File file : listFiles)
            file.delete();

        dirPath.delete();
    }

    public List<String> getFileList(){
        MyComparator mMyComparator = new MyComparator();
        File[] files = dirPath.listFiles();
        Arrays.sort(files, mMyComparator);
        List<String> list = new ArrayList<String>();
        for(File file : files) {
            if(file.isDirectory())
                if(file.list().length >= 6)
                    list.add(file.getName());
        }
        return list;
    }

    public void writeFile(String name, List data){
        FileWriter fw = null;
        BufferedWriter bw = null;
        String file_path = dirPath + "/" + name;

        try{
            fw = new FileWriter(file_path, false);
            bw = new BufferedWriter(fw);

            for(Object value : data) {
                String str = value + "\n";
                bw.write(str);
            }
        }catch (FileNotFoundException ex1){
            System.err.println("檔案不存在");
            System.err.println("路徑：" + file_path);
        }catch (IOException ex2) {
            System.err.println("寫入失敗");
        }finally {
            try{
                if(bw != null)
                    bw.close();
            } catch (IOException e) {
                System.err.println("關檔失敗");
            }
        }
    }

    public void writeFile(String name, String data){
        FileWriter fw = null;
        BufferedWriter bw = null;
        String file_path = dirPath + "/" + name;

        try{
            fw = new FileWriter(file_path, false);
            bw = new BufferedWriter(fw);

            bw.write(data);
        }catch (FileNotFoundException ex1){
            System.err.println("檔案不存在");
            System.err.println("路徑：" + file_path);
        }catch (IOException ex2) {
            System.err.println("寫入失敗");
        }finally {
            try{
                if(bw != null)
                    bw.close();
            } catch (IOException e) {
                System.err.println("關檔失敗");
            }
        }
    }

    //取得數據
    public List<String> readFile(String name){
        FileReader fr = null;
        BufferedReader br = null;
        String file_path = dirPath + "/" + name;
        List data = new ArrayList();

        try{
            fr = new FileReader(file_path);
            br = new BufferedReader(fr);

            for(String line=br.readLine(); line != null; line=br.readLine())
                data.add(line);

        } catch (NumberFormatException ex1){
            System.err.println("沒有內容");
        }catch (FileNotFoundException ex2) {
            System.err.println("檔案不存在");
        }catch (IOException ex3){
            System.err.println("讀取失敗");
        }finally {
            try{
                br.close();
            } catch (IOException ex4) {
                System.err.println("關檔失敗");
            }
        }
        return data;
    }

}
