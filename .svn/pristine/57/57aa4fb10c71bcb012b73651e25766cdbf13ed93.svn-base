package org.bigmouth.nvwa.codec.compress;

import java.util.ArrayList;
import java.util.List;

/**
 * Using gzip to compress data.
 * 
 * @author nada
 * 
 */
public class CompressSegmentFactory {

	private int threshold = 1024 * 90;

	// private int threshold = 100;

	public List<CompressSegment> createListSegments(byte[] data) {
		if (null == data)
			throw new NullPointerException("data");

		List<CompressSegment> result = new ArrayList<CompressSegment>();
		getSegment(0, data, result);
		return result;
	}

	private void getSegment(int begin, byte[] data, List<CompressSegment> result) {
		if (begin > (data.length - 1))
			return;

		int seg_length = (begin + threshold - 1) > (data.length - 1) ? (data.length - begin)
				: threshold;
		byte[] seg_data = new byte[seg_length];

		System.arraycopy(data, begin, seg_data, 0, seg_length);

		CompressSegment listSegment = new CompressSegment(seg_data);
		result.add(listSegment);

		begin = begin + threshold;
		getSegment(begin, data, result);
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public static void main(String[] args) {
		byte[] bytes = new byte[5];
		bytes[0] = 0;
		bytes[1] = 1;
		bytes[2] = 2;
		bytes[3] = 3;
		bytes[4] = 4;

		CompressSegmentFactory f = new CompressSegmentFactory();
		List<CompressSegment> result = f.createListSegments(bytes);
		for (CompressSegment s : result) {
			System.out.println("-------------");
			byte[] bs = s.getContent();
			for (byte b : bs)
				System.out.println(b);
		}
	}
}
