package SkipList;

class Node<K, V> {
    private K key;
    private V value;
    public Node<K, V> next, down;// ��������ÿ���㶼������ָ��

    public Node() {
    };

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    };// �������캯��

    public K get_key() {
        return this.key;
    }

    public V get_value() {
        return this.value;
    }

    public void setvalue(V value) {
        this.value = value;
    }
}
