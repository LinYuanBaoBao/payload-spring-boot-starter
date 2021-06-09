package com.linyuanbaobao.payload.support.model;

/**
 * @author linyuan - szlinyuan@ininin.com
 * @since 2021/6/8
 */
public class DingDingRobotMsgDTO {
    private final String msgtype = "text";
    private TextDTO text;

    public DingDingRobotMsgDTO(String content) {
        TextDTO textDTO = new TextDTO();
        textDTO.setContent(content);
        this.text = textDTO;
    }

    public class TextDTO {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public String getMsgtype() {
        return msgtype;
    }

    public TextDTO getText() {
        return text;
    }
}
