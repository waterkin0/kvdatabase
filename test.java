import SkipList.*;

public class test {
    public static void main(String[] args) {
        skipList<Integer, String> t = new skipList<Integer, String>();//key���Ա��string����(������ʹ�÷������������ܻ��bug)����������
        t.insert_element(1, "?");//������
        System.out.println("�����޸�ǰ��" + t.get_value(1)); 
        t.insert_element(1, "��ӭ");//������
        System.out.println("�����޸ĺ�" + t.get_value(1)); 
        t.insert_element(4, "�ҵĲ���");
        t.insert_element(3, "����");
        t.insert_element(6, "waterkin");
        t.insert_element(7, ".top");
        t.insert_element(12, "!!!");
        // t.insert_element("abc", "!!!");
        t.display_list();
        t.display_structure();//չʾ��������Ľṹ
        System.out.println(t.get_value(4)); 
        System.out.println(t.get_value(3)); 
        System.out.println("��СΪ��" + t.size());

        t.delete_element(4);//ɾ��ĳ������
        t.display_structure();
        System.out.println(t.get_value(4)); 
        System.out.println(t.get_value(3)); 
        System.out.println("��СΪ��" + t.size());

        t.save();
        t.load();
        t.display_list();
        t.display_structure();//չʾ��������Ľṹ

        // skipList<String, String> t = new skipList<String, String>();//string��key����
        // t.insert_element("abc", "!!!");
        // t.insert_element("abcd", "ahhahah");
        // t.insert_element("a", "��������");
        // t.insert_element("abc", "��������hahaha");
        // t.display_list();

        // t.save();
        // t.load();
        // t.display_structure();//չʾ��������Ľṹ
        // t.display_list();
    }
}