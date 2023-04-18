package SkipList;

import java.io.*;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Pattern;
import java.lang.ProcessBuilder.Redirect.Type;
import java.lang.reflect.ParameterizedType;
// import java.util.concurrent.Semaphore;

//gbk����
public class skipList<K, V> {
    private Node<K, V> head_Node;// ͷ���
    private int level;// ����ǰ�߶�
    private int element_length;// ������
    final int MAXLEVEL = 31;// ������߸߶�,��Ϊ32����0��ʼ�ģ�
    // static Semaphore mutex = new Semaphore(1);

    private Boolean compare(K a, K b) {// �����Ƚ�
        Integer key1 = a.hashCode();
        Integer key2 = b.hashCode();
        return key1 > key2;
    }

    public skipList() {
        this.element_length = 0;
        this.level = 0;
        this.head_Node = new Node<K, V>();
    }

    public void display_list() {// չʾ�����ڴ������
        Node<K, V> tmp = this.head_Node;
        while (tmp.down != null)
            tmp = tmp.down;// ֱ�����һ�㣬��ʼ��ȡ����
        tmp = tmp.next;// �����һ���յ�ͷ���
        if (tmp == null) {
            System.out.println("����Ϊ��!");
            return;
        }
        while (tmp != null) {
            String out_data = "key: " + tmp.get_key() + " value: " + tmp.get_value();
            System.out.println(out_data);
            tmp = tmp.next;
        }
    }

    public void display_structure() {// ��ÿһ������չʾ��ṹ(�ṹֻչʾkey����Ȼ�����Ű�)
        Node<K, V> tmp = this.head_Node;
        if (tmp.next == null) {
            System.out.println("����Ϊ��!");
            return;
        }
        Node<K, V> tmpdown = tmp;// ��ס��һ��Ŀ�ͷ��������һ��
        do {
            tmpdown = tmpdown.down;// ����Ÿ�ֵ������ѭ��
            tmp = tmp.next;// ��������һ���յ�ͷ���
            String out_data = "";
            while (tmp != null) {
                out_data += "-";
                out_data += tmp.get_key().hashCode();
                // out_data += ':' + (String)tmp.get_value();
                tmp = tmp.next;
            }
            System.out.println(out_data);
            tmp = tmpdown;
        } while (tmpdown != null);
    }

    public Node<K, V> get_element(K key) {
        Node<K, V> tmp = this.head_Node;
        // System.out.println("��ʼ����");
        while (tmp != null) {
            if (tmp.get_key() == key) {// ע����������ҵ����Ƿ���ײ���Ǹ��ڵ㣬��Ҫ��������ײ�
                while (tmp.down != null) {
                    tmp = tmp.down;
                    // System.out.println("���µ���key:" + tmp.get_key());
                }
                // System.out.println("�ҵ��ˣ�");
                return tmp;
            } else if (tmp.next == null || compare(tmp.next.get_key(), key)) {// ������Ҳ�Ϊnull���ߴ���Ҫ���key��������һ���level��������������һ�㻹�Ǵ�����ô�Զ�����null��ѭ����
                tmp = tmp.down;
                // System.out.println("���µ���key:" + (tmp == null ? "wu" : tmp.get_key()));
            } else {// С�ھ�����
                tmp = tmp.next;
                // System.out.println("���ҵ���key:" + (tmp == null ? "wu" : tmp.get_key()));
            }
        }
        return null;
    }

    public V get_value(K key) {
        Node<K, V> tmpNode = get_element(key);
        if (tmpNode != null)
            return tmpNode.get_value();
        return null;
    }

    public void delete_element(K key) {
        Node<K, V> tmp = this.head_Node;
        while (tmp != null) {
            if (tmp.next == null) {
                tmp = tmp.down;
            } else if (tmp.next.get_key() == key) { // ��鵽�Ҳ�ڵ���Ŀ��㣬ɾ��Ȼ�����¼���ɾ�������Ŀ���
                tmp.next = tmp.next.next;
                tmp = tmp.down;
            } else if (compare(tmp.next.get_key(), key)) {// �Ҳ������Ŀ��㣬˵���ò�Ŀ��㲻���ڣ�����
                if (tmp.down == null) {
                    System.out.println("Ŀ��㲻����");//��ײ�Ҳû�о����
                    this.element_length++;// ������������--�ģ���Ϊɾ��ʧ����
                }
                tmp = tmp.down;
            } else {// С�ھ�����
                tmp = tmp.next;
            }
        }
        while(this.head_Node.next == null && this.head_Node.down != null){//���ͷ����Ƿ�Ӧ�øı�
            this.head_Node = this.head_Node.down;
        }
        this.element_length--;
    }

    public void insert_element(K key, V value) { // ����Ƚϸ���...,����˼·�����ҵ������λ�ã�˳��洢ÿ������ĵ㣬��Ϊ��Щ���next���ܣ�0.5���ʣ����������µ�
        Node<K, V> findNode = get_element(key);
        if (findNode != null) {// �Ѿ����ھ����¸�ֵ����
            findNode.setvalue(value);
            return;
        }
        element_length++;
        Stack<Node<K, V>> turnNode = new Stack<Node<K, V>>();// ������ÿ������㣬һ��ʼ�õ�list���������е��д���ͷ����������ʵ��stack���ã�����
        Node<K, V> tmp = this.head_Node;
        while (tmp != null) {// ���ҵķ���������ķ���һ�� 
            if (tmp.next == null) {
                turnNode.add(tmp);// ��¼
                tmp = tmp.down;
            } else if (compare(tmp.next.get_key(), key)) {
                turnNode.add(tmp);// ��¼
                tmp = tmp.down;
            } else {
                tmp = tmp.next;
            }
        }
        int level = 0;// ��ǰ����,�ӵ�0����ײ㿪ʼ
        Node<K, V> downNode = null;// ������down�ڵ�ģ���֤�Ե����ϵĹ���������Ľڵ�down�ܱ���ֵΪ������Ǹ�
        while (!turnNode.isEmpty()) {// ��ʼһ�������˽��в���
            if (level > MAXLEVEL)
                break; // ������߲㣡������
            Node<K, V> tmpNode = turnNode.pop();// ��ȡ��ǰ��Ҫ����ĵ�
            Node<K, V> addNode;
            if (level == 0)// ֻ�е�0������value���ݣ����涼��null����Լ�ռ�
                addNode = new Node<K, V>(key, value);
            else
                addNode = new Node<K, V>(key, null);
            addNode.down = downNode;
            addNode.next = tmpNode.next;// �������
            tmpNode.next = addNode;
            downNode = addNode; // ���뵱ǰ�����Ľڵ㣬��һ��Ҫ��
            // String out_data = "�ڵ�" + level + "�����key:" + addNode.get_key() + " value:" + addNode.get_value() + ",������" + (addNode.down == null ? null :"key:" + addNode.down.get_key() + " value:" + addNode.down.get_value());
            // System.out.println(out_data);
            Random random = new Random();
            double num = random.nextDouble();// �����ȡһ��0-1֮�����
            if (num > 0.5)// ����0.5��ֹͣ���룬��ÿһ�����󶼻��ж��Ƿ������һ��
                break;
            level++;// ��ǰ�����������
            if (level > this.level) {
                this.level++;
                Node<K, V> newHead = new Node<K, V>();// �µ�ͷ���
                newHead.down = this.head_Node;
                this.head_Node = newHead;
                turnNode.add(newHead); // ������µĽڵ�����µ�ѭ��
            }
        }
    }

    public void save() {
        save_file("data/dataFile_onlybottom");
    }
    public void save(String filepos) {
        save_file(filepos);
    }

    private void save_file(String filepos) {// �洢���ݣ����������ַ�����1.ֻ�����ݣ���ֻ�����һ���onlybottom������ÿ�δ����ݿⶼ���ع��ṹ 2.��ṹ��ÿһ��Ķ�Ҫ����������ǰ��Ҫ���Ŀռ�
        System.out.print("���ڴ洢��...(��ʽ��ֻ������)\n");
        try {
            File f = new File(filepos);
            OutputStream fos = new FileOutputStream(f);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "GBK");

            Node<K, V> tmp = this.head_Node;
            while (tmp.down != null)
                tmp = tmp.down;// ֱ�����һ�㣬��ʼ�洢����
            tmp = tmp.next;// �����һ���յ�ͷ���
            String keyclass = tmp.get_key().getClass().getTypeName();
            String valueclass = tmp.get_value().getClass().getTypeName();
            String save = keyclass + ':' + valueclass + "\n";
            writer.append(save);
            while (tmp != null) {
                save = tmp.get_key() + ":" + tmp.get_value() + "\n";
                writer.append(save);
                tmp = tmp.next;
            }
            writer.close();
            fos.close();
            System.out.print("�洢�ɹ�\n");
        } catch (IOException e) {
            System.out.print("���ִ��� Exception:" + e + "\n");
        }
    }

    public void load() {
        load_file("data/dataFile_onlybottom");
    }
    public void load(String filepos) {
        load_file(filepos);
    }

    private void load_file(String filepos) {
        System.out.print("���ڶ�ȡ��...\n");
        try {
            File f = new File(filepos);
            InputStream fos = new FileInputStream(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fos));
            String line = reader.readLine();
            String[] datas = line.split(":", 2);
            String keyclass = datas[0];
            K key; V value;
            while ((line = reader.readLine()) != null) {
                datas = line.split(":", 2);
                if(keyclass.endsWith("Integer")){
                    key = (K)Integer.valueOf(Integer.parseInt(datas[0]));
                }
                else{
                    key = (K) datas[0];
                }
                value = (V) datas[1];
                this.insert_element(key, value);
            }
            reader.close();
        } catch (IOException e) {
            System.out.print("���ִ��� Exception:" + e + "\n");
        }
    }

    public int size() {
        return element_length;
    }
}