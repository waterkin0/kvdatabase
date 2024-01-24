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

## Some problems

Resolved:

1. Fisrt time I use 'get_value' to traversal, sometimes it return null, that's because it find the key which not in the  bottom, where it's value is null(all my values are in the bottom, the node above only for leading)
2. Sometimes it can't find the real value, that's a code error led to the table structure was wrong, the 'down' at the end of each layer was connected to the beginning of the next layer
3. Occasionally after delete one element , it will return null when traversaling, the reason is that I only delete the element in the bottom, but forget the one above, resulting in errors in judgment
4. To be honest, the way of java's generic is good, just like c's template, but compared to c's actual use is terrible, I can't get the actual type of the current jump table generic when I read the data, so I can't assign (of course, I can get the type of the generic type, but it's too complicated), the solution is to store this type when storing, and read it when reading
   But as you can see the warning still appears, I really can't do anything about it.

Haven't resolved yet:

1. When using string as a key, although it is mostly normal, problems may still occur and it is not recommended to use string as a key
2. UTF-8 encoding will be read error, the reason is unknown, change to GBK
3. When reading the file, the type of file now is different from the one when you define the file before, There will be an error in the stored data. Maybe I will solve it sometimes in the future

# End

Recently in the study of java and database, so I found such a project to practice, there are still many shortcomings, I hope you understand!
