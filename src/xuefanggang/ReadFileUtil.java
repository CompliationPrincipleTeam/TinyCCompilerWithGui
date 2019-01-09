package xuefanggang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author dmrfcoder
 * @date 2019/1/7
 */
public class ReadFileUtil {
    private String path;

    public ReadFileUtil(String path) {
        this.path = path;
    }

    public ArrayList<String> readContentStrList() {
        ArrayList<String> contentList = new ArrayList<>();
        File file = new File(path);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            line = bufferedReader.readLine();
            int index = 0;
            while (line != null) {
                line = line.substring(1);
                line = line.substring(0, line.lastIndexOf("]"));

                contentList.add(line);
                System.out.println(line);
                line = bufferedReader.readLine();
            }

            System.out.println(index);

            bufferedReader.close();
            inputStreamReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentList;
    }


}
