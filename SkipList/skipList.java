package SkipList;

import java.io.*;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Pattern;
import java.lang.ProcessBuilder.Redirect.Type;
import java.lang.reflect.ParameterizedType;
// import java.util.concurrent.Semaphore;

//gbk编码
public class skipList<K, V> {
    private Node<K, V> head_Node;// 头结点
    private int level;// 跳表当前高度
    private int element_length;// 跳表长度
    final int MAXLEVEL = 31;// 跳表最高高度,定为32（从0开始的）
    // static Semaphore mutex = new Semaphore(1);

    private Boolean compare(K a, K b) {// 用来比较
        Integer key1 = a.hashCode();
        Integer key2 = b.hashCode();
        return key1 > key2;
    }

    public skipList() {
        this.element_length = 0;
        this.level = 0;
        this.head_Node = new Node<K, V>();
    }

    public void display_list() {// 展示跳表内存的数据
        Node<K, V> tmp = this.head_Node;
        while (tmp.down != null)
            tmp = tmp.down;// 直达最后一层，开始读取数据
        tmp = tmp.next;// 不存第一个空的头结点
        if (tmp == null) {
            System.out.println("跳表为空!");
            return;
        }
        while (tmp != null) {
            String out_data = "key: " + tmp.get_key() + " value: " + tmp.get_value();
            System.out.println(out_data);
            tmp = tmp.next;
        }
    }

    public void display_structure() {// 从每一层跳表展示其结构(结构只展示key，不然不好排版)
        Node<K, V> tmp = this.head_Node;
        if (tmp.next == null) {
            System.out.println("跳表为空!");
            return;
        }
        Node<K, V> tmpdown = tmp;// 记住下一层的开头，遍历这一层
        do {
            tmpdown = tmpdown.down;// 这里才赋值，方便循环
            tmp = tmp.next;// 不遍历第一个空的头结点
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
        // System.out.println("开始查找");
        while (tmp != null) {
            if (tmp.get_key() == key) {// 注意这里可能找到的是非最底层的那个节点，需要遍历到最底层
                while (tmp.down != null) {
                    tmp = tmp.down;
                    // System.out.println("向下到达key:" + tmp.get_key());
                }
                // System.out.println("找到了！");
                return tmp;
            } else if (tmp.next == null || compare(tmp.next.get_key(), key)) {// 跳表的右侧为null或者大于要求的key，就往下一层的level，这里如果在最后一层还是大于那么自动就是null出循环了
                tmp = tmp.down;
                // System.out.println("向下到达key:" + (tmp == null ? "wu" : tmp.get_key()));
            } else {// 小于就向右
                tmp = tmp.next;
                // System.out.println("向右到达key:" + (tmp == null ? "wu" : tmp.get_key()));
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
            } else if (tmp.next.get_key() == key) { // 检查到右侧节点是目标点，删除然后向下继续删除下面的目标点
                tmp.next = tmp.next.next;
                tmp = tmp.down;
            } else if (compare(tmp.next.get_key(), key)) {// 右侧大于了目标点，说明该层目标点不存在，向下
                if (tmp.down == null) {
                    System.out.println("目标点不存在");//最底层也没有就输出
                    this.element_length++;// 用来抵消最后的--的，因为删除失败了
                }
                tmp = tmp.down;
            } else {// 小于就向右
                tmp = tmp.next;
            }
        }
        while(this.head_Node.next == null && this.head_Node.down != null){//检查头结点是否应该改变
            this.head_Node = this.head_Node.down;
        }
        this.element_length--;
    }

    public void insert_element(K key, V value) { // 插入比较复杂...,大致思路就是找到插入的位置，顺便存储每个拐弯的点，因为这些点的next可能（0.5概率）会插入这个新点
        Node<K, V> findNode = get_element(key);
        if (findNode != null) {// 已经存在就重新赋值给他
            findNode.setvalue(value);
            return;
        }
        element_length++;
        Stack<Node<K, V>> turnNode = new Stack<Node<K, V>>();// 用来存每个拐弯点，一开始用的list，代码量有点大，写完回头发现这里其实用stack更好，改了
        Node<K, V> tmp = this.head_Node;
        while (tmp != null) {// 查找的方法和上面的方法一样 
            if (tmp.next == null) {
                turnNode.add(tmp);// 记录
                tmp = tmp.down;
            } else if (compare(tmp.next.get_key(), key)) {
                turnNode.add(tmp);// 记录
                tmp = tmp.down;
            } else {
                tmp = tmp.next;
            }
        }
        int level = 0;// 当前层数,从第0层最底层开始
        Node<K, V> downNode = null;// 用来存down节点的，保证自底向上的过程中上面的节点down能被赋值为下面的那个
        while (!turnNode.isEmpty()) {// 开始一个个回退进行插入
            if (level > MAXLEVEL)
                break; // 到达最高层！不加了
            Node<K, V> tmpNode = turnNode.pop();// 获取当前需要处理的点
            Node<K, V> addNode;
            if (level == 0)// 只有第0层存这个value数据，上面都是null，节约空间
                addNode = new Node<K, V>(key, value);
            else
                addNode = new Node<K, V>(key, null);
            addNode.down = downNode;
            addNode.next = tmpNode.next;// 插入操作
            tmpNode.next = addNode;
            downNode = addNode; // 存入当前新增的节点，上一层要用
            // String out_data = "在第" + level + "层添加key:" + addNode.get_key() + " value:" + addNode.get_value() + ",下面是" + (addNode.down == null ? null :"key:" + addNode.down.get_key() + " value:" + addNode.down.get_value());
            // System.out.println(out_data);
            Random random = new Random();
            double num = random.nextDouble();// 随机获取一个0-1之间的数
            if (num > 0.5)// 大于0.5就停止插入，即每一层插入后都会判断是否进入上一层
                break;
            level++;// 当前处理层数增加
            if (level > this.level) {
                this.level++;
                Node<K, V> newHead = new Node<K, V>();// 新的头结点
                newHead.down = this.head_Node;
                this.head_Node = newHead;
                turnNode.add(newHead); // 对这个新的节点进行新的循环
            }
        }
    }

    public void save() {
        save_file("data/dataFile_onlybottom");
    }
    public void save(String filepos) {
        save_file(filepos);
    }

    private void save_file(String filepos) {// 存储数据，这里用两种方法，1.只存数据，即只存最后一层的onlybottom，这样每次打开数据库都会重构结构 2.存结构，每一层的都要，但这样比前者要求大的空间
        System.out.print("正在存储中...(方式：只存数据)\n");
        try {
            File f = new File(filepos);
            OutputStream fos = new FileOutputStream(f);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "GBK");

            Node<K, V> tmp = this.head_Node;
            while (tmp.down != null)
                tmp = tmp.down;// 直达最后一层，开始存储数据
            tmp = tmp.next;// 不存第一个空的头结点
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
            System.out.print("存储成功\n");
        } catch (IOException e) {
            System.out.print("出现错误 Exception:" + e + "\n");
        }
    }

    public void load() {
        load_file("data/dataFile_onlybottom");
    }
    public void load(String filepos) {
        load_file(filepos);
    }

    private void load_file(String filepos) {
        System.out.print("正在读取中...\n");
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
            System.out.print("出现错误 Exception:" + e + "\n");
        }
    }

    public int size() {
        return element_length;
    }
}