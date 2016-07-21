package com.wfj.exception.hadoop.hadoop.mapper;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.KeyRange;
import org.apache.hadoop.io.Text;

import com.wfj.exception.util.StringUtils;
import com.wfj.exception.vo.ClientExceptionReq;

public class MesCountMapper extends TableMapper<Text, Text> {

	private static byte[] family = Bytes.toBytes("message");
	private static String[] qualifier;
	static {
		Field[] fields = ClientExceptionReq.class.getDeclaredFields();
		qualifier = new String[fields.length - 1];
		for (int i = 1; i < fields.length; i++) {
			if (StringUtils.isNotBlank(fields[i].getName())) {
				qualifier[i - 1] = fields[i].getName();
			}
		}
	}
	private static String splitStr = ":";

	@Override
	protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
		byte[] r = value.getRow();
		byte[] v = value.getValue(family, "mes_value".getBytes());
		context.write(new Text(Bytes.toString(r)), new Text(Bytes.toString(v)));
	}

}
