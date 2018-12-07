package com.sly.app.model;

/**
 * 作者 by K
 * 时间：on 2017/9/19 17:53
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class LoginBean {

        private String MemberCode;
        private String Token;

        public String getMemberCode() {
            return MemberCode;
        }

        public void setMemberCode(String MemberCode) {
            this.MemberCode = MemberCode;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }
}
