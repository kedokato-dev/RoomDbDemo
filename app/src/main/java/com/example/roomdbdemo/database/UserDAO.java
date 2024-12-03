package com.example.roomdbdemo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.roomdbdemo.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);
    @Query("SELECT * FROM user")
    List<User> getListUser();
    @Query("SELECT * FROM USER WHERE userName =:username")
    List<User> checkUser(String username);

    @Update
    void updateUser (User user);
    @Delete
    void deleteUser(User user);
    @Query("DELETE FROM USER")
    void deleteAllUser();
    @Query("SELECT * FROM USER WHERE userName LIKE '%' || :name || '%'")
    List<User> searchUser(String name);
}
