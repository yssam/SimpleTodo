package com.example.sam.simpletodo.Model;

/**
 * Created by Sam on 6/22/16.
 */
import java.util.Date;

/**
 * Created by qiming on 6/21/2016.
 */
public class Item {
    public long pos;
    public Date dueDate;
    public int priority;
    public String text;


    public Item(int pos, String text, Date dueDate, int priority) {
        this.pos = pos;
        this.text = text;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public Item(int pos, String text, Date dueDate) {
        this.pos = pos;
        this.text = text;
        this.dueDate = dueDate;
      }

    public Item(int pos, String text) {
        this.pos = pos;
        this.text = text;
    }
}
