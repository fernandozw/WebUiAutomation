package com.thinkingdata.lib;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class HandleUtils {
	/**
	 * 将详细的异常的堆栈信息转换成字符串
	 * 
	 * @param e 异常对象
	 * @return
	 */
	public static String handleErrInfo(Exception e) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			// 将出错的栈信息输出到printWriter中
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
		return sw.toString();
	}

	/**
	 * 将字符串处理成StrCharSequence数组
	 * 
	 * @param sourceStr 原字符串
	 * @return
	 */
	public static CharSequence[] handleStrTocs(String sourceStr) {
		// 构造以字符串长度为数组长度的CharSequence数组
		CharSequence[] cs = (new CharSequence[sourceStr.length()]);
		// 将字符串转换成字符数组
		char[] strArray = sourceStr.toCharArray();
		// 将字符数组中的单个元素转换成单个字符串
		for (int i = 0; i < sourceStr.length(); i++) {
			char[] item = new char[1];
			item[0] = strArray[i];
			cs[i] = String.valueOf(item);
		}
		return cs;
	}

	@Test
	public void test1() {

		String string = "{\"code\":\"A00000\",\"data\":{\"hot_score\":null,\"cover_image_url\":\"http://liuxiong-test-dev001-jylt.qiyi.virtual:8088/image/xiuchang/xiuchang_5d1da3dbd956ff1aeca39cb9_0.jpg\",\"initiator\":\"\",\"page_info\":{\"total_page\":3,\"total\":26,\"page\":3,\"page_size\":12},\"description\":\"娃哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈啊娃哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈啊娃哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈啊娃哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈啊娃哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈啊娃哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈啊\",\"topic_id\":26,\"title\":\"哈之凝视\",\"items\":[{\"video_id\":136304,\"qipu_id\":38417324009,\"vid\":\"d9a5364a2890e216895810a5d9a1f513\",\"title\":\"相思\",\"cover_image_url\":\"http://m.iqiyipic.com/u8/image/20190509/44/1c/uv_38417324009_d_616_1080_0.jpg\",\"share_image_url\":\"http://m.iqiyipic.com/u8/image/20190509/44/1c/uv_38417324009_d_616_120_120.jpg\",\"cover_thumb_image_url\":\"http://m.iqiyipic.com/u8/image/20190509/44/1c/uv_38417324009_d_616_360_0.jpg\",\"status\":1,\"author\":{\"user_id\":990014771,\"nick_name\":\"贪玩的小陶陶\",\"user_icon\":\"http://www.qiyipic.com/common/fix/headicons/male-70.png\",\"followed\":0},\"stat\":{\"like_count\":0,\"comment_count\":0,\"share_count\":0},\"liked\":0,\"rec_pb\":null,\"width\":720,\"height\":1280,\"show_type\":2,\"wp_url\":\"http://pan.thinkingdata.com/external/E832S970-ToU23i3-cZwKHGB5e54CaW3iZaKYRE20ZjDMMT1JnNO6MX7PAVmFUX02smHEc4ZmmzdYbS5wBWaqZBxdAt_UbI-w-xG7Y-lazrztZsF-9Vj9nSWQIUQsEw4-KDSSdflgTYWvJNSR5fde5MXwolOFSPYZDdpYB57A6maO1YSjyYgsTuTLWOtuK3e2U3CwWEx_ra6bNL0KGPCYg.mp4\",\"process_status\":2,\"add_time\":\"2019-04-05 00:17:28\",\"hot_comments\":null,\"hot_score\":37.65,\"onoffline_time\":\"2019-05-14 18:40:30\",\"readable_time\":\"2019-05-14\",\"is_on_live\":0,\"cdn_url\":\"http://pdata.video.thinkingdata.com/videos/v1/20190509/45/bb/2f0711404229aa17ca19a58f9e3a43ac.mp4?qd_src=pizza&qd_uid=0&qd_p=&qd_tm=1562298432445&qd_k=&pv=0.2&qd_sc=654b6e36cf64b6166baa5c8ce5613002\",\"ad\":null,\"label\":null,\"topic_title\":\"哈之凝视\",\"topic_id\":26},{\"video_id\":205654,\"qipu_id\":38868644709,\"vid\":\"c7cf2b361e47296115ec2fa49b3d4738\",\"title\":\"哈哈哈小崽子还挺有劲\",\"cover_image_url\":\"http://m.iqiyipic.com/u4/image/20190522/0c/a5/uv_38868644709_d_601_1080_0.jpg\",\"share_image_url\":\"http://m.iqiyipic.com/u4/image/20190522/0c/a5/uv_38868644709_d_601_120_120.jpg\",\"cover_thumb_image_url\":\"http://m.iqiyipic.com/u4/image/20190522/0c/a5/uv_38868644709_d_601_360_0.jpg\",\"status\":1,\"author\":{\"user_id\":2535064107,\"nick_name\":\"Kylie\",\"user_icon\":\"http://img7.qiyipic.com/passport/20170902/bb/3f/passport_2535064107_150433100657092_130_130.jpg\",\"followed\":0},\"stat\":{\"like_count\":0,\"comment_count\":0,\"share_count\":0},\"liked\":0,\"rec_pb\":null,\"width\":720,\"height\":1280,\"show_type\":2,\"wp_url\":\"http://pan.thinkingdata.com/external/aTLXCQf6tSkcm61EHfugAjYIFnO-UpgUIwggoUqQiSUKDlB4upr1T2aNL1BMRxqKXzyp5SkzBfkoRWL57Lvdi_NAZDYBfI8sGTxUlW81lsLjp96Hp7MHWjxv7ExlGzpQcg3glAd42doym4Tfv9f1FkxM8RTIbNZI2RNt6swXUZRusdMiJHwdIxEYxmE5Hl51NZ-q4IzcqYZueUUC6l4U4w.mp4\",\"process_status\":2,\"add_time\":\"2019-05-22 11:35:18\",\"hot_comments\":null,\"hot_score\":0.0,\"onoffline_time\":\"2019-05-31 14:23:05\",\"readable_time\":\"2019-05-31\",\"is_on_live\":0,\"cdn_url\":\"http://pdata.video.thinkingdata.com/videos/v1/20190522/22/e1/eab4b3777f57b4ae575ae2d79c80601c.mp4?qd_src=pizza&qd_uid=0&qd_p=&qd_tm=1562298432446&qd_k=&pv=0.2&qd_sc=7ae0201587a8d468edf2d95e40c7cc5e\",\"ad\":null,\"label\":null,\"topic_title\":\"哈之凝视\",\"topic_id\":26}]},\"msg\":\"正常\",\"trace_id\":\"a98d71997ce746778a8f81e6fc359d46\"}";
		List<Object> videosObject = JsonPath.read(string, "$.data.items");
		List<Integer> idArray = new ArrayList<Integer>();
		System.out.println(videosObject.size());
		for (int i = 0; i < videosObject.size(); i++) {
			Object idString = JsonPath.read(videosObject.get(i), "$.video_id");
			Object urlObject = JsonPath.read(videosObject.get(i), "$.video_id");
			idArray.add((Integer) idString);
		}
		System.out.println(idArray);
	}

}
