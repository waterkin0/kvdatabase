# README.md

- zh_CN [简体中文](README.zh_CN.md)
- en [English](README.md)

# KVdatabase

A skiplist structure similar to redis

Support for inserting data into this database, deleting data, querying data, long-term storage of data, loading data from files, and displaying the size and structure of the database

Written in java,GBK encoding

## file structure

* data： The data in the database is stored here
* SkipList： About the node and skiplist
* test.java：Perform the basic database operations

## Just a few words

The implementation is very detailed in the code

I have seen two ways to implement skiplists:

1.Layers. each layer is an array, and you can perform operations on each layer individually, such as reading the bottom layer when traversing

2.Together. Not like the above one, each node in this structure has down and next two directions, this structure is simple and easy to understand, but not as convenient as the above one, here I use the second

Then I see that some skiplists also store value in the upper layer, I don't know why, maybe in order to reduce the retrieval time, but this obviously needs to store a lot of duplicate data, a bit of a waste of space, I don't think this is necessary, in addition to the bottom layer really needs value, the above basically plays a guiding role, there is no need to store value

The second is about the storage of this skiplist, you can store only the data, that mean, the bottom layer, storage and access are simple in this way, and then you can also store the structure, that is, all stored in

Originally, I want to write all of them, but I only choose the first one here, because when I actually write, I find that if the structure is stored together, the read also needs to reconstruct the whole skiplist, and it does not save much time

In contrast to the common key that can only be an Integer, the key of this project can be a string, and the string uses hashcode to encode it into an Integer for sorting

## 遇到的小问题

1. get_value遍历查找的时候一开始key匹配就直接返回了，但忘记了key匹配也可能是在上层，即value为null
2. 之前偶尔会出现查找失败的情况，检查才发现有一行赋值出错导致表结构错了，每一层的结尾的down连到了下一层的开头
3. 偶尔会出现删除一个元素后在遍历的时候返回null的现象，原因是因为原来的最上面的那层只有要删除的这个元素，删除之后头没下降，导致判断出错
4. 说实话，java的泛型出发点是好的，和c的模板一样，但相比c实际用起来就是一坨，我在读取数据的时候不能得到当前的跳表泛型的实际类型，从而无法进行赋值（当然想获取泛型类型也可以，但是太复杂了），解决办法是在储存的时候存一下这个类型，读取的时候以这个形式读取
   但是如你所见还是会出现警告，我实在没办法解决了

还可以优化的地方：

1. 在以string作为key的时候，虽然大部分情况下是正常的，但仍然可能会出现问题，不推荐用string作为key
2. UTF-8编码会出现读取错误，原因未知，改用GBK
3. 当读取文件的类型和你开始定义的不一致的时候会出现存储的数据出错，希望可以后续改善

# 后记

最近在学习java和数据库，于是就找了这么一个项目来练手，还有很多不足，希望各位谅解
