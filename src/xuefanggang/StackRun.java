package xuefanggang;

import java.util.ArrayList;

/**
 * @author dmrfcoder
 * @date 2019/1/7
 */
public class StackRun {
    public static void run() {
        ReadFileUtil readFileUtil = new ReadFileUtil("TinyCCompiler/stack.txt");
        ArrayList<String> stackContentList = readFileUtil.readContentStrList();

        DynamicStackJframe dynamicStackJframe = new DynamicStackJframe(500, stackContentList);
    }
}
