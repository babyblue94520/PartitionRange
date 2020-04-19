package com.tt;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreatePartition {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	private static final int rangeType = Calendar.MONTH;
	private static final String alterRangeSQL = "ALTER TABLE %s PARTITION BY RANGE (%s)(\n %s);";
	private static final String dropRangeSQL = "ALTER TABLE %s REMOVE PARTITIONING;";
	private static final String rangeSQL = "PARTITION p%s VALUES LESS THAN (%d) COMMENT = '小於 %s',";
	private static final String lastSQL = "PARTITION pMax VALUES LESS THAN MAXVALUE";
	
//	private static final String alterSubRangeSQL = "ALTER TABLE %s PARTITION BY RANGE (%s)\n SUBPARTITION BY HASH (%s) SUBPARTITIONS %d (\n %s)";
	private static final String alterSubRangeSQL = "ALTER TABLE %s PARTITION BY RANGE (TO_DAYS(%s))\n SUBPARTITION BY HASH (%s) SUBPARTITIONS %d (\n %s)";
	
	
	
	public static void main(String[] args) throws ParseException, InterruptedException {
		System.out.println(genRangePartitionSQL("access_log","time","2019-12-01 0:0:0",24));
//		System.out.println(genSubRangePartitionSQL("Test","time","2019-03-01 0:0:0",10,"sub",10));

	}

	
	public static String genRangePartitionSQL(
		String table
		,String column
		,String startDate
		,int block
	) throws ParseException{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(startDate));
		long t;
		StringBuffer rangeSb = new StringBuffer();
		for(int i = 0;i<block;i++){
			t = calendar.getTime().getTime();
			rangeSb.append(String.format(rangeSQL, sdf2.format(calendar.getTime()),t,sdf2.format(calendar.getTime()))+"\n");
			calendar.add(rangeType,1);
//			System.out.println(sdf.format(calendar.getTime())+" : "+(calendar.getTime().getTime()));
		}
		t = calendar.getTime().getTime();
		rangeSb.append(String.format(lastSQL, t)+"\n");
		System.out.println(String.format(dropRangeSQL,table));
		return String.format(alterRangeSQL,table,column,rangeSb.toString());
	}

	public static String genSubRangePartitionSQL(
		String table
		,String column
		,String startDate
		,int block
		,String subColumn
		,int subBlock
	) throws ParseException{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(startDate));
		long t;
		StringBuffer rangeSb = new StringBuffer();
		for(int i = 0;i<block;i++){
			t = calendar.getTime().getTime();
			rangeSb.append(String.format(rangeSQL, sdf2.format(calendar.getTime()),t)+"\n");
			calendar.add(rangeType,1);
//			System.out.println(sdf.format(calendar.getTime())+" : "+(calendar.getTime().getTime()));
		}
		t = calendar.getTime().getTime();
		rangeSb.append(String.format(lastSQL, t)+"\n");
		System.out.println(String.format(dropRangeSQL,table));
		return String.format(alterSubRangeSQL,table,column,subColumn,subBlock,rangeSb.toString());
	}
}
