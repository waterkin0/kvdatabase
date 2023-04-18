import SkipList.*;

public class test {
    public static void main(String[] args) {
        skipList<Integer, String> t = new skipList<Integer, String>();//key可以变成string类型(不建议使用符号命名，可能会出bug)，正常运行
        t.insert_element(1, "?");//存数据
        System.out.println("数据修改前：" + t.get_value(1)); 
        t.insert_element(1, "欢迎");//存数据
        System.out.println("数据修改后：" + t.get_value(1)); 
        t.insert_element(4, "我的博客");
        t.insert_element(3, "访问");
        t.insert_element(6, "waterkin");
        t.insert_element(7, ".top");
        t.insert_element(12, "!!!");
        // t.insert_element("abc", "!!!");
        t.display_list();
        t.display_structure();//展示整个跳表的结构
        System.out.println(t.get_value(4)); 
        System.out.println(t.get_value(3)); 
        System.out.println("大小为：" + t.size());

        t.delete_element(4);//删除某个数据
        t.display_structure();
        System.out.println(t.get_value(4)); 
        System.out.println(t.get_value(3)); 
        System.out.println("大小为：" + t.size());

        t.save();
        t.load();
        t.display_list();
        t.display_structure();//展示整个跳表的结构

        // skipList<String, String> t = new skipList<String, String>();//string的key测试
        // t.insert_element("abc", "!!!");
        // t.insert_element("abcd", "ahhahah");
        // t.insert_element("a", "哈哈哈哈");
        // t.insert_element("abc", "哈哈哈哈hahaha");
        // t.display_list();

        // t.save();
        // t.load();
        // t.display_structure();//展示整个跳表的结构
        // t.display_list();
    }
}