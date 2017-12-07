package ea.tool.api;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EAQAUtilTest {
    @Test
    public void testFormatQA() {
        Map<String, String> mapIO = new HashMap<String, String>() {{
            put("qa_test", "qa_test");
            put("李存 英", "测试A");
            put("李存 英、测工师", "测试A, 测工师");
            put("李存 英, 测工师", "测试A, 测工师");
            put("测试A、测工师", "测试A, 测工师");
        }};

        for (Map.Entry<String, String> io : mapIO.entrySet()) {
            String ret = EAQAUtil.formatQA(io.getKey());
            Assert.assertEquals(io.getValue(), ret);
        }
    }

    @Test
    public void testRemoveHtml() {
        Map<String, String> mapIO = new HashMap<String, String>() {{
            put(" test", "test");
            put("测试A</font>", "测试A");
            put("测试b<", "测试b");
            put("测试C<>", "测试C");
            put("测试d</", "测试d");
            put("测试e>", "");
            put("</font>测试f", "测试f");
            put(">测试g", "测试g");
            put("<测试H", "");
        }};

        for (Map.Entry<String, String> io : mapIO.entrySet()) {
            String ret = EAQAUtil.removeHtml(io.getKey());
            Assert.assertEquals(io.getValue(), ret);
        }
    }

    @Test
    public void testGetQA() {
        Map<String, String> mapIO = new HashMap<String, String>() {{
            put("测试负责人: test", "test");
            put("QA负责人; test", "test");
            put("QA 负责人 test", "test");
            put("测试leader: test", "test");
            put("测试 leader  test", "test");
            put("QALeader: test", "test");
            put("QA Leader, test", "test");
            put("测试工程师: test", "test");
            put("测试工程师: test", "test");
            put("QA: test", "test");
            put("测试", null);
            put("QAtest", null);
            put("测试：测试A", "测试A");
            put("测试：测试A、测工师", "测试A, 测工师");
            put("测试：测试A, 测工师", "测试A, 测工师");
            put("测试Leader：测试A</font>", "测试A");
        }};

        for (Map.Entry<String, String> io : mapIO.entrySet()) {
            String ret = EAQAUtil.getQA(io.getKey());
            Assert.assertEquals(io.getValue(), ret);
        }
    }
}