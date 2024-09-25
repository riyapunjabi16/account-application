package com.thinking.machines.seq;
import java.io.*;
public class SequenceEngine
{
private SequenceEngine()
{
//nobody can extend or instantiate this class
}
public static int getNext(Sequence sequence) throws SequenceException
{
try
{
String fileName=sequence.getName().toLowerCase()+".sq";
File file;
file=new File(fileName);
int next;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(file.exists()==false||randomAccessFile.length()==0) next=sequence.getStartFrom();
else
{
next=Integer.parseInt(randomAccessFile.readLine().trim())+sequence.getStep();
randomAccessFile.seek(0);
}
randomAccessFile.writeBytes(String.format("%-10s",String.valueOf(next))+"\n");
randomAccessFile.close();
return next;
}
catch(Exception exception)
{
throw new SequenceException("Unable to generate next code for"+sequence.getName());
} 
}
}